package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.StockDto;
import com.jung.notify.repository.StockManageRepository;
import com.jung.notify.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    private final MemberService memberService;

    private final StockManageRepository stockManageRepository;

    public Page<Stock> selectStockList(String corpName, Pageable pageable) {
        return stockRepository.findByCorpNameContains(corpName, pageable);
    }

    public Page<StockDto.SelectStock> selectStockList(String corpName, Pageable pageable, String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return stockRepository.selectStockList(corpName, pageable, member);
    }

    public void saveStockManage(Long stockManageId, Long stockId, String memberId, boolean isUse) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        Stock stock = stockRepository.findById(stockId).orElseThrow(NullPointerException::new);

        StockManage stockManage = StockManage.builder()
                .id(stockManageId)
                .stock(stock)
                .member(member).build();

        stockManageRepository.save(stockManage);
    }
}
