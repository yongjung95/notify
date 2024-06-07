package com.jung.notify.repository;

import com.jung.notify.domain.WorldStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldStockRepository extends JpaRepository<WorldStock, Long>, WorldStockRepositoryQuerydsl {
}
