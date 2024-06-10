package com.jung.notify.repository;

import com.jung.notify.domain.AmericaStockManage;
import com.jung.notify.domain.Member;
import com.jung.notify.dto.AmericaStockDto;
import com.jung.notify.dto.QAmericaStockDto_SelectAmericaStock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jung.notify.domain.QAmericaStock.americaStock;
import static com.jung.notify.domain.QAmericaStockManage.americaStockManage;


@Repository
public class AmericaStockManageRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public AmericaStockManageRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(AmericaStockManage saveAmericaStockManage) {

        AmericaStockManage findAmericaStockManage = queryFactory.select(americaStockManage)
                .from(americaStockManage)
                .where(americaStockManage.americaStock.eq(saveAmericaStockManage.getAmericaStock())
                        .and(americaStockManage.member.eq(saveAmericaStockManage.getMember())))
                .fetchOne();

        if (findAmericaStockManage == null) {
            saveAmericaStockManage.changeIsUse();
            em.persist(saveAmericaStockManage);
        }else{
            findAmericaStockManage.changeIsUse();
        }

    }

    public Page<AmericaStockDto.SelectAmericaStock> selectAmericaStockManageList(Pageable pageable, Member searchMember) {

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
                .from(americaStockManage)
                .leftJoin(americaStockManage.americaStock, americaStock)
                .where(americaStockManage.member.eq(searchMember)
                        .and(americaStockManage.isUse.eq(true)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long americaStockManageListCount = queryFactory
                .select(americaStockManage.count())
                .from(americaStockManage)
                .where(americaStockManage.member.eq(searchMember)
                        .and(americaStockManage.isUse.eq(true)))
                .fetchOne();

        return new PageImpl<>(results, pageable, americaStockManageListCount);
    }

}
