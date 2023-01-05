package com.jung.notify.repository;

import com.jung.notify.domain.Stock;
import com.jung.notify.dto.StockDto;
import com.jung.notify.mapper.StockMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void 주식_전체_수_조회() {
        long count = stockRepository.count();

        System.out.println(count);
    }

    @Test
    public void 종목명으로_검색하기() {
        List<Stock> stockList = stockRepository.findByCorpNameContains("삼성");

        System.out.println(stockList.size());
    }

    @Test
    public void 종목명으로_검색하기_페이징() {
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "corpName"));

        Page<Stock> stockPageList = stockRepository.findByCorpNameContains("삼성", pageRequest);

        List<Stock> stockList = stockPageList.getContent();

        System.out.println(stockPageList.getTotalElements());
        System.out.println(stockList.size());
    }

    @Test
    public void 종목명으로_검색하기_매퍼사용() {
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "corpName"));

        Page<Stock> stockPageList = stockRepository.findByCorpNameContains("삼성", pageRequest);

        List<StockDto.SelectStock> selectStocks = StockMapper.INSTANCE.stocksToSelectStocks(stockPageList.getContent());

        for (StockDto.SelectStock selectStock : selectStocks) {
            System.out.println(selectStock);
        }

    }

}