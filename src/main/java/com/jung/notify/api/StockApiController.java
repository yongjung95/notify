package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.domain.Member;
import com.jung.notify.dto.StockDto;
import com.jung.notify.service.MemberService;
import com.jung.notify.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StockApiController {

    private final MemberService memberService;

    private final StockService stockService;

    private final ResponseService responseService;

//    @GetMapping("/v1/stock")
//    public PagingListResult<?> stock(StockDto.SelectStockRequest selectStockRequest) {
////        Optional<Member> member = memberService.findMemberById(user.getUsername());
////
////        if (!member.isPresent()) {
////            return responseService.getFailPagingListResult(ErrorCode.MEMBER_IS_NOT_FOUND);
////        }
//
//        PageRequest pageRequest = PageRequest.of(selectStockRequest.getPage(), selectStockRequest.getSize(), Sort.by(Sort.Direction.ASC, "corpName"));
//
//        Page<Stock> stocks = stockService.selectStockList(selectStockRequest.getCorpName(), pageRequest);
//
//        return responseService.getPagingListResult(stocks);
//    }

    @PostMapping("/v1/stock")
    public SingleResult<?> stock(@AuthenticationPrincipal User user, @RequestBody StockDto.StockManageRequest stockManageRequest) {
        System.out.println(stockManageRequest);

        if (stockManageRequest.getStockId() == null) {
            return responseService.getFailResult(ErrorCode.PARAMETER_IS_EMPTY);
        }

        Optional<Member> member = memberService.findMemberById(user.getUsername());

        if (!member.isPresent()) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        stockService.saveStockManage(stockManageRequest.getStockId(), user.getUsername());

        return responseService.getSuccessResult();
    }
}
