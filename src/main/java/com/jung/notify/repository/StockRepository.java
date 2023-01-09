package com.jung.notify.repository;

import com.jung.notify.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryQuerydsl {

    Page<Stock> findByCorpNameContains(String corpName, Pageable pageable);

}
