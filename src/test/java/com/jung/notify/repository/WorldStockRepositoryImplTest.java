package com.jung.notify.repository;

import com.jung.notify.dto.WorldStockDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorldStockRepositoryImplTest {

    @Autowired
    private WorldStockRepository worldStockRepository;

    @Test
    void 미국주식_목록조회() throws Exception {
        //given
        String koreanName = "애플";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "koreanName"));

        //when
        Page<WorldStockDto.SelectWorldStock> selectWorldStocks = worldStockRepository.selectWorldStockList(koreanName, pageRequest);

        //then
        List<WorldStockDto.SelectWorldStock> result = selectWorldStocks.getContent();

        Assertions.assertThat(result.size()).isEqualTo(8);
    }
}