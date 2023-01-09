package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.QStockDto_SelectStock;
import com.jung.notify.dto.StockDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

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
                .rightJoin(stockManage.stock, stock)
                .on(stockManage.member.eq(searchMember))
                .where(stock.corpName.contains(corpName))
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

}
