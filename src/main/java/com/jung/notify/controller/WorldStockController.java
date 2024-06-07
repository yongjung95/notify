package com.jung.notify.controller;

import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.WorldStockDto;
import com.jung.notify.service.WorldStockService;
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
public class WorldStockController {

    private final WorldStockService worldStockService;

    private final ResponseService responseService;

    @GetMapping("/world-stock")
    public String worldStock(@AuthenticationPrincipal User user, Model model, WorldStockDto.SelectWorldStockRequest selectWorldStockRequest) {
        PageRequest pageRequest = PageRequest.of(selectWorldStockRequest.getPage(), selectWorldStockRequest.getSize(), Sort.by(Sort.Direction.ASC, "corpName"));

        Page<WorldStockDto.SelectWorldStock> selectWorldStockList = worldStockService.selectWorldStockList(selectWorldStockRequest.getKoreanName(), pageRequest);

        model.addAttribute("data", responseService.getPagingListResult(selectWorldStockList));
        model.addAttribute("selectWorldStockRequest", selectWorldStockRequest);

        return "worldStock/list";
    }
}
