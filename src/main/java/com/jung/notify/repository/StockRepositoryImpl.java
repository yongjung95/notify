package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.QStockDto_SelectStock;
import com.jung.notify.dto.QStockDto_SelectStockManageMember;
import com.jung.notify.dto.StockDto;
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

import static com.jung.notify.domain.QMember.member;
import static com.jung.notify.domain.QStock.stock;
import static com.jung.notify.domain.QStockManage.stockManage;

public class StockRepositoryImpl implements StockRepositoryQuerydsl {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public StockRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<StockDto.SelectStock> selectStockList(String corpName, Pageable pageable, Member searchMember) {
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable);

        List<StockDto.SelectStock> results = queryFactory
                .select(new QStockDto_SelectStock(
                        stock.id,
                        stock.corpCode,
                        stock.corpName,
                        stock.stockCode,
                        stockManage.id,
                        stockManage.isUse
                ))
                .from(stock)
                .leftJoin(stockManage)
                .on(stock.id.eq(stockManage.stock.id)
                        .and(stockManage.member.eq(searchMember)))
                .where(stock.corpName.contains(corpName))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long stockListCount = queryFactory
                .select(stock.count())
                .from(stock)
                .where(stock.corpName.contains(corpName))
                .fetchOne();

        return new PageImpl<>(results, pageable, stockListCount);
    }

    public List<StockDto.SelectStockManageMember> selectStockManageAllByMember(Member searchMember) {
        return queryFactory
                .select(new QStockDto_SelectStockManageMember(
                        stock.corpName,
                        stock.stockCode,
                        member.lineToken
                ))
                .from(stockManage)
                .join(stockManage.stock, stock)
                .join(stockManage.member, member)
                .where(stockManage.isUse.eq(true)
                        .and(stockManage.member.eq(searchMember)
                        ))
                .fetch();
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Object> pathBuilder = new PathBuilder<>(stock.getType(), stock.getMetadata());
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
