package com.jung.notify.service;

import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.StockDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private EntityManager em;

    @Test
    public void stock_목록_조회_및_회원_관심종목_조회() {
        StockDto.SelectStockRequest selectStockRequest = new StockDto.SelectStockRequest();

        PageRequest pageRequest = PageRequest.of(selectStockRequest.getPage(), selectStockRequest.getSize());

        Page<StockDto.SelectStock> stockList = stockService.selectStockList("삼성", pageRequest, "member1");

        for (StockDto.SelectStock selectStock : stockList) {
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

        em.flush();
        em.clear();
    }

    @Test
    public void 관심종목_등록_수정() {
        stockService.saveStockManage(5L, "member1");

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "corpName"));

        Page<StockDto.SelectStock> stockList = stockService.selectStockList("삼성", pageRequest, "member1");

        System.out.println(stockList.getTotalElements());

        for (StockDto.SelectStock selectStock : stockList) {
            System.out.println(selectStock);
        }

    }

    @Test
    public void 개발DB_종목_목록_조회() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "corpName"));

        Page<StockDto.SelectStock> stockList = stockService.selectStockList("삼성", pageRequest, "yongjung95");

        for (StockDto.SelectStock selectStock : stockList) {
            System.out.println(selectStock);
        }

    }

    @Test
    public void 개발DB_관심목록_조회() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "corpName"));

        Page<StockDto.SelectStock> stockList = stockService.selectStockManageList( pageRequest, "yongjung95");

        for (StockDto.SelectStock selectStock : stockList) {
            System.out.println(selectStock);
        }

    }
}