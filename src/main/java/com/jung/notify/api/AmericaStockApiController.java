package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.AmericaStockDto;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.service.AmericaStockService;
import com.jung.notify.service.MemberService;
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
public class AmericaStockApiController {

    private final MemberService memberService;

    private final AmericaStockService americaStockService;

    private final ResponseService responseService;

    @PostMapping("/v1/america-stock")
    public SingleResult<?> americaStock(@AuthenticationPrincipal User user, @RequestBody @Valid AmericaStockDto.AmericaStockManageRequest americaStockManageRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        MemberDto.SelectMember selectMember = memberService.findMemberById(user.getUsername());

        if(selectMember == null) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        return americaStockService.saveAmericaStockManage(americaStockManageRequest.getAmericaStockId(), user.getUsername()) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.STOCK_IS_NOT_FOUND);
    }
}
