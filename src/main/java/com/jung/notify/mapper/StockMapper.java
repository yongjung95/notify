package com.jung.notify.mapper;

import com.jung.notify.domain.Stock;
import com.jung.notify.dto.StockDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockDto.SelectStock stockToSelectStock(Stock stock);

    List<StockDto.SelectStock> stocksToSelectStocks(List<Stock> stocks);
}
