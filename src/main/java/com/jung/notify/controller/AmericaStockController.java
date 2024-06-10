package com.jung.notify.controller;

import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.AmericaStockDto;
import com.jung.notify.service.AmericaStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AmericaStockController {

    private final AmericaStockService americaStockService;

    private final ResponseService responseService;

    @GetMapping("/america-stock")
    public String americaStock(@AuthenticationPrincipal User user, Model model, AmericaStockDto.SelectAmericaStockRequest selectAmericaStockRequest) {
        PageRequest pageRequest = PageRequest.of(selectAmericaStockRequest.getPage(), selectAmericaStockRequest.getSize(), Sort.by(Sort.Direction.ASC, "symbol"));

        Page<AmericaStockDto.SelectAmericaStock> selectWorldStockList = americaStockService.selectAmericaStockList(selectAmericaStockRequest.getKoreanName(), pageRequest, user.getUsername());

        model.addAttribute("data", responseService.getPagingListResult(selectWorldStockList));
        model.addAttribute("selectAmericaStockRequest", selectAmericaStockRequest);

        return "americaStock/list";
    }

    @GetMapping("/america-stock/manage")
    public String stockManage(@AuthenticationPrincipal User user, Model model) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "corpName"));

        model.addAttribute("data", responseService.getPagingListResult(americaStockService.selectAmericaStockManageList(pageRequest, user.getUsername())));
        model.addAttribute("pageRequest", pageRequest);

        return "americaStock/manage/list";
    }

    @GetMapping("/america-stock/manage/list")
    public String americaStockManageList(@AuthenticationPrincipal User user, Model model, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "corpName"));

        model.addAttribute("data", responseService.getPagingListResult(americaStockService.selectAmericaStockManageList(pageRequest, user.getUsername())));
        model.addAttribute("pageRequest", pageRequest);

        return "americaStock/manage/tableList";
    }
}
