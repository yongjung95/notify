package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
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
public class StockRepositoryQuerydsl {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    private final StockRepository stockRepository;

    public StockRepositoryQuerydsl(EntityManager em, StockRepository stockRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.stockRepository = stockRepository;
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

        List<Stock> stockList = stockRepository.findByCorpNameContains(corpName);


        return new PageImpl<>(results, pageable, stockList.size());
    }

}
