package com.jung.notify.repository;

import com.jung.notify.dto.WorldStockDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorldStockRepositoryQuerydsl {

    Page<WorldStockDto.SelectWorldStock> selectWorldStockList(String koreanName, Pageable pageable);
}
