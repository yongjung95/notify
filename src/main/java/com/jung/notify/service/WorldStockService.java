package com.jung.notify.service;

import com.jung.notify.dto.WorldStockDto;
import com.jung.notify.repository.WorldStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorldStockService {

    private final WorldStockRepository worldStockRepository;

    public Page<WorldStockDto.SelectWorldStock> selectWorldStockList(String koreanName, Pageable pageable) {
        return worldStockRepository.selectWorldStockList(koreanName, pageable);
    }
}
