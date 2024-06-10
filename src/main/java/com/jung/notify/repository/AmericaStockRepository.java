package com.jung.notify.repository;

import com.jung.notify.domain.AmericaStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmericaStockRepository extends JpaRepository<AmericaStock, Long>, AmericaStockRepositoryQuerydsl {
}
