package com.jung.notify.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.domain.Stock;
import com.jung.notify.dto.StockDto;
import com.jung.notify.mapper.StockMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private ResponseService responseService;

    @Test
    public void stock_리턴_값_테스트() throws JsonProcessingException {
        StockDto.SelectStockRequest selectStockRequest = new StockDto.SelectStockRequest();
        selectStockRequest.setCorpName("삼성");

        PageRequest pageRequest = PageRequest.of(selectStockRequest.getPage(), selectStockRequest.getSize());

        Page<Stock> stocks = stockService.selectStockList(selectStockRequest.getCorpName(), pageRequest);

        List<StockDto.SelectStock> list = StockMapper.INSTANCE.stocksToSelectStocks(stocks.getContent());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(responseService.getPagingListResult(list, stocks));
        System.out.println(json);
    }
}