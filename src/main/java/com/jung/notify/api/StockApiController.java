package com.jung.notify.api;

import com.jung.notify.api.response.model.PagingListResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.domain.Stock;
import com.jung.notify.dto.StockDto;
import com.jung.notify.service.MemberService;
import com.jung.notify.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockApiController {

    private final MemberService memberService;

    private final StockService stockService;

    private final ResponseService responseService;

    @GetMapping("/v1/stock")
    public PagingListResult<?> stock(StockDto.SelectStockRequest selectStockRequest) {
//        Optional<Member> member = memberService.findMemberById(user.getUsername());
//
//        if (!member.isPresent()) {
//            return responseService.getFailPagingListResult(ErrorCode.MEMBER_IS_NOT_FOUND);
//        }

        PageRequest pageRequest = PageRequest.of(selectStockRequest.getPage(), selectStockRequest.getSize(), Sort.by(Sort.Direction.ASC, "corpName"));

        Page<Stock> stocks = stockService.selectStockList(selectStockRequest.getCorpName(), pageRequest);

        return responseService.getPagingListResult(stocks);
    }
}
