package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.QStockDto_SelectStock;
import com.jung.notify.dto.StockDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jung.notify.domain.QStock.stock;
import static com.jung.notify.domain.QStockManage.stockManage;

@Repository
public class StockManageRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public StockManageRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(StockManage saveStockManage) {
        StockManage findStockManage = queryFactory.select(stockManage)
                .from(stockManage)
                .where(stockManage.stock.eq(saveStockManage.getStock())
                        .and(stockManage.member.eq(saveStockManage.getMember())))
                .fetchOne();

        if (findStockManage == null) {
            saveStockManage.changeIsUse();
            em.persist(saveStockManage);
        }else{
            findStockManage.changeIsUse();
        }

    }

    public Page<StockDto.SelectStock> selectStockManageList(Pageable pageable, Member searchMember) {
        List<StockDto.SelectStock> results = queryFactory
                .select(new QStockDto_SelectStock(
                        stock.id,
                        stock.corpCode,
                        stock.corpName,
                        stock.stockCode,
                        stockManage.id,
                        stockManage.isUse
                ))
                .from(stockManage)
                .leftJoin(stockManage.stock, stock)
                .where(stockManage.member.eq(searchMember)
                        .and(stockManage.isUse.eq(true)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long StockManageListCount = queryFactory
                .select(stockManage.count())
                .from(stockManage)
                .where(stockManage.member.eq(searchMember)
                        .and(stockManage.isUse.eq(true)))
                .fetchOne();

        return new PageImpl<>(results, pageable, StockManageListCount);

    }

}
