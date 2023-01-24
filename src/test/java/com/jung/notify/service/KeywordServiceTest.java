package com.jung.notify.service;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.KeywordDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class KeywordServiceTest {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private EntityManager em;

//    @BeforeEach
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

        Keyword keyword1 = Keyword.builder()
                .keyword("삼성")
                .member(member1)
                .build();

        Keyword keyword2 = Keyword.builder()
                .keyword("애플")
                .member(member1)
                .build();

        em.persist(keyword1);
        em.persist(keyword2);

        em.flush();
        em.clear();
    }

    @Test
    public void 키워드_생성_테스트() {
        KeywordDto.SaveKeywordDto saveKeywordDto = KeywordDto.SaveKeywordDto.builder()
                .keyword("삼성")
                .memberId("member1")
                .build();

        KeywordDto.SelectKeyword selectKeyword = keywordService.saveKeyword(saveKeywordDto);

        System.out.println(selectKeyword);
    }

    @Test
    public void 키워드_보유_조회() {
        KeywordDto.SelectKeyword oneByKeyword = keywordService.findOneByKeyword("하이닉스", "member1");

        System.out.println(oneByKeyword);
    }
    
    @Test
    public void 회원_키워드_목록_조회() {
        List<KeywordDto.SelectKeyword> memberKeywordList = keywordService.findAllByMember("member1");

        for (KeywordDto.SelectKeyword selectKeyword : memberKeywordList) {
            System.out.println(selectKeyword);
        }
    }

    @Test
    public void 회원_키워드_삭제() {
        KeywordDto.SelectKeyword selectKeyword = new KeywordDto.SelectKeyword();

        selectKeyword.setId(11L);
        selectKeyword.setKeyword("삼성");

        keywordService.delete(selectKeyword);

        List<KeywordDto.SelectKeyword> memberKeywordList = keywordService.findAllByMember("member1");

        for (KeywordDto.SelectKeyword keyword : memberKeywordList) {
            System.out.println(keyword);
        }
    }

    @Test
    public void 회원_키워드_메시지_발송() {
        keywordService.sendKeywordList();
    }
}