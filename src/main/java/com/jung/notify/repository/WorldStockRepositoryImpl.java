package com.jung.notify.repository;

import com.jung.notify.dto.QWorldStockDto_SelectWorldStock;
import com.jung.notify.dto.WorldStockDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jung.notify.domain.QWorldStock.worldStock;

public class WorldStockRepositoryImpl implements WorldStockRepositoryQuerydsl {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public WorldStockRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<WorldStockDto.SelectWorldStock> selectWorldStockList(String koreanName, Pageable pageable) {
        List<WorldStockDto.SelectWorldStock> results = queryFactory
                .select(new QWorldStockDto_SelectWorldStock(
                        worldStock.id,
                        worldStock.symbol,
                        worldStock.exchange,
                        worldStock.englishName,
                        worldStock.koreanName
                ))
                .from(worldStock)
                .where(worldStock.koreanName.contains(koreanName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long stockListCount = queryFactory
                .select(worldStock.count())
                .from(worldStock)
                .where(worldStock.koreanName.contains(koreanName))
                .fetchOne();

        return new PageImpl<>(results, pageable, stockListCount);
    }
}
