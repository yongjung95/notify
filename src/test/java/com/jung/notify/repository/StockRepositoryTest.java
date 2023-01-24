package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.StockDto;
import com.jung.notify.mapper.StockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void 주식_전체_수_조회() {
        long count = stockRepository.count();

        System.out.println(count);
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

    @BeforeEach
    public void before() {
        Member member1 = Member.builder().id("member1").build();
        Member member2 = Member.builder().id("member2").build();
        Member member3 = Member.builder().id("member3").build();
        Member member4 = Member.builder().id("member4").build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        Stock stock1 = Stock.builder()
                .corpCode("111111")
                .corpName("삼성전자")
                .stockCode("111111")
                .build();

        Stock stock2 = Stock.builder()
                .corpCode("222222")
                .corpName("삼성바이오")
                .stockCode("222222")
                .build();

        Stock stock3 = Stock.builder()
                .corpCode("333333")
                .corpName("삼성디스플레이")
                .stockCode("333333")
                .build();

        Stock stock4 = Stock.builder()
                .corpCode("444444")
                .corpName("LG전자")
                .stockCode("444444")
                .build();

        em.persist(stock1);
        em.persist(stock2);
        em.persist(stock3);
        em.persist(stock4);

        StockManage stockManage1 = StockManage.builder()
                .stock(stock1)
                .member(member1)
                .isUse(true)
                .build();

        StockManage stockManage2 = StockManage.builder()
                .stock(stock2)
                .member(member1)
                .isUse(true)
                .build();

        em.persist(stockManage1);
        em.persist(stockManage2);
    }

    @Test
    public void 주식_종목_조회() {
        Member member = memberRepository.findById("member1");


        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "corpName"));

        Page<StockDto.SelectStock> stockList = stockRepository.selectStockList("삼성", pageRequest, member);

        System.out.println(stockList.getTotalElements());

        for (StockDto.SelectStock selectStock : stockList) {
            System.out.println(selectStock);
        }
    }

}