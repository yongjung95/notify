package com.jung.notify.controller;

import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.StockDto;
import com.jung.notify.service.StockService;
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
public class StockController {

    private final StockService stockService;

    private final ResponseService responseService;

    @GetMapping("/stock")
    public String stock(@AuthenticationPrincipal User user, Model model, StockDto.SelectStockRequest selectStockRequest) {
        PageRequest pageRequest = PageRequest.of(selectStockRequest.getPage(), selectStockRequest.getSize(), Sort.by(Sort.Direction.ASC, "corpName"));

        Page<StockDto.SelectStock> stockList = stockService.selectStockList(selectStockRequest.getCorpName(), pageRequest, user.getUsername());

        model.addAttribute("data", responseService.getPagingListResult(stockList));
        model.addAttribute("selectStockRequest", selectStockRequest);

        return "stock/list";
    }

    @GetMapping("/stock/manage")
    public String stockManage(@AuthenticationPrincipal User user, Model model) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "corpName"));

        model.addAttribute("data", responseService.getPagingListResult(stockService.selectStockManageList(pageRequest, user.getUsername())));
        model.addAttribute("pageRequest", pageRequest);

        return "stock/manage/list";
    }

    @GetMapping("/stock/manage/list")
    public String stockManageList(@AuthenticationPrincipal User user, Model model, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "corpName"));

        model.addAttribute("data", responseService.getPagingListResult(stockService.selectStockManageList(pageRequest, user.getUsername())));
        model.addAttribute("pageRequest", pageRequest);

        return "stock/manage/tableList";
    }
}

