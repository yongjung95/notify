package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.QStockManage;
import com.jung.notify.domain.StockManage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jung.notify.domain.QMember.member;
import static com.jung.notify.domain.QStockManage.stockManage;

@Repository
public class StockManageRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public StockManageRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(StockManage stockManage) {
        em.persist(stockManage);
    }

    public List<StockManage> findAllByMember(Member searchMember) {
        QStockManage qStockManage1;
        return queryFactory
                .select(stockManage)
                .from(stockManage)
                .leftJoin(stockManage.member, member)
                .where(stockManage.member.eq(searchMember))
                .fetch();

    }

}
