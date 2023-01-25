package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.dto.StockDto;
import com.jung.notify.service.MemberService;
import com.jung.notify.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public SingleResult<?> stock(@AuthenticationPrincipal User user, @RequestBody @Valid StockDto.StockManageRequest stockManageRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        MemberDto.SelectMember selectMember = memberService.findMemberById(user.getUsername());

        if(selectMember == null) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        return stockService.saveStockManage(stockManageRequest.getStockId(), user.getUsername()) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.STOCK_IS_NOT_FOUND);
    }
}
