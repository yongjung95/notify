package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.AmericaStockDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
class AmericaStockRepositoryImplTest {

    @Autowired
    private AmericaStockRepository americaStockRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 미국주식_목록조회() throws Exception {
        //given
        String koreanName = "애플";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "koreanName"));

        Member findMember = memberRepository.findByUid(1L);

        //when
        Page<AmericaStockDto.SelectAmericaStock> selectWorldStocks = americaStockRepository.selectAmericaStockList(koreanName, pageRequest, findMember);

        //then
        List<AmericaStockDto.SelectAmericaStock> result = selectWorldStocks.getContent();

        Assertions.assertThat(result.size()).isEqualTo(8);
    }
}