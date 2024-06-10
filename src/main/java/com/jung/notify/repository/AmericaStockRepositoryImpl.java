package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.AmericaStockDto;
import com.jung.notify.dto.QAmericaStockDto_SelectAmericaStock;
import com.jung.notify.dto.QAmericaStockDto_SelectAmericaStockManageMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.jung.notify.domain.QAmericaStock.americaStock;
import static com.jung.notify.domain.QAmericaStockManage.americaStockManage;
import static com.jung.notify.domain.QMember.member;


public class AmericaStockRepositoryImpl implements AmericaStockRepositoryQuerydsl {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public AmericaStockRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AmericaStockDto.SelectAmericaStock> selectAmericaStockList(String koreanName, Pageable pageable, Member searchMember) {

        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable);

        List<AmericaStockDto.SelectAmericaStock> results = queryFactory
                .select(new QAmericaStockDto_SelectAmericaStock(
                        americaStock.id,
                        americaStock.symbol,
                        americaStock.exchange,
                        americaStock.englishName,
                        americaStock.koreanName,
                        americaStockManage.id,
                        americaStockManage.isUse
                ))
                .from(americaStock)
                .leftJoin(americaStockManage)
                .on(americaStock.id.eq(americaStockManage.americaStock.id)
                        .and(americaStockManage.member.eq(searchMember)))
                .where(americaStock.koreanName.contains(koreanName))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long stockListCount = queryFactory
                .select(americaStock.count())
                .from(americaStock)
                .where(americaStock.koreanName.contains(koreanName))
                .fetchOne();

        return new PageImpl<>(results, pageable, stockListCount);
    }

    public List<AmericaStockDto.SelectAmericaStockManageMember> selectAmericaStockManageAllByMember(Member searchMember) {
        return queryFactory
                .select(new QAmericaStockDto_SelectAmericaStockManageMember(
                        americaStock.koreanName,
                        americaStock.symbol,
                        americaStock.exchange,
                        member.lineToken
                ))
                .from(americaStockManage)
                .join(americaStockManage.americaStock, americaStock)
                .join(americaStockManage.member, member)
                .where(americaStockManage.isUse.eq(true)
                        .and(americaStockManage.member.eq(searchMember)
                        ))
                .fetch();
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Object> pathBuilder = new PathBuilder<>(americaStock.getType(), americaStock.getMetadata());
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC
                        , pathBuilder.getString(order.getProperty())
                );
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }
}
