package com.jung.notify.repository;

import com.jung.notify.domain.StockApiInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.jung.notify.domain.QStockApiInfo.stockApiInfo;

@Transactional
@Repository
public class StockApiInfoRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public StockApiInfoRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Long save(StockApiInfo stockApiInfo) {
        em.persist(stockApiInfo);

        return stockApiInfo.getId();
    }

    public StockApiInfo findByDate(String date) {
        return queryFactory.select(stockApiInfo).from(stockApiInfo).where(stockApiInfo.issueDate.eq(date)).fetchOne();
    }

}
