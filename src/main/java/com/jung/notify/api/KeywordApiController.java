package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.KeywordDto;
import com.jung.notify.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeywordApiController {
    private final KeywordService keywordService;

    private final ResponseService responseService;

    @PostMapping("/v1/keyword")
    public SingleResult<?> keyword(@AuthenticationPrincipal User user, @RequestBody @Valid KeywordDto.SaveKeywordDto saveKeywordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        saveKeywordDto.setMemberId(user.getUsername());

        return responseService.getSingleResult(keywordService.saveKeyword(saveKeywordDto));
    }

    @DeleteMapping("/v1/keyword")
    public SingleResult<?> keyword(@AuthenticationPrincipal User user, @RequestBody @Valid KeywordDto.RemoveKeywordDto removeKeywordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        List<KeywordDto.SelectKeyword> deleteKeywords = new ArrayList<>();

        for (Long keywordId : removeKeywordDto.getKeywordId()) {
            KeywordDto.SelectKeyword selectKeyword = keywordService.findOne(keywordId, user.getUsername());

            if (selectKeyword == null) {
                return responseService.getFailResult(ErrorCode.KEYWORD_IS_NOT_FOUND);
            }

            deleteKeywords.add(selectKeyword);
        }

        if (!deleteKeywords.isEmpty()) {
            for (KeywordDto.SelectKeyword keyword : deleteKeywords) {
                keywordService.delete(keyword);
            }
        }

        return responseService.getSuccessResult();
    }
}
