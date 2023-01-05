package com.jung.notify.service;

import com.jung.notify.domain.Stock;
import com.jung.notify.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    public Page<Stock> selectStockList(String corpName, Pageable pageable) {
        return stockRepository.findByCorpNameContains(corpName, pageable);
    }
}
