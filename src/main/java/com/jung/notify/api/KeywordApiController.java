package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.jung.notify.dto.KeywordDto;
import com.jung.notify.mapper.KeywordMapper;
import com.jung.notify.service.KeywordService;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class KeywordApiController {

    private final MemberService memberService;

    private final KeywordService keywordService;

    private final ResponseService responseService;

    @PostMapping("/v1/keyword")
    public SingleResult<?> keyword(@AuthenticationPrincipal User user, @RequestBody KeywordDto.SaveKeywordDto saveKeywordDto) {
        Optional<Member> member = memberService.findMemberById(user.getUsername());

        if (!member.isPresent()) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

            Optional<Keyword> findKeyword = keywordService.findOneByKeyword(saveKeywordDto.getKeyword(), member.orElse(null));

        if (findKeyword.isPresent()) {
            return responseService.getFailResult(ErrorCode.DUPLICATION_KEYWORD);
        }

        Keyword keyword = Keyword.builder()
                .keyword(saveKeywordDto.getKeyword())
                .member(member.get())
                .created(LocalDateTime.now()).build();

        keywordService.saveKeyword(keyword);

        return responseService.getSingleResult(KeywordMapper.INSTANCE.keywordToSelectKeyword(keyword));
    }

    @DeleteMapping("/v1/keyword")
    public SingleResult<?> keyword(@AuthenticationPrincipal User user, @RequestBody KeywordDto.RemoveKeywordDto removeKeywordDto) {
        Optional<Member> member = memberService.findMemberById(user.getUsername());

        List<Keyword> deleteKeywords = new ArrayList<>();

        for (Long keywordId : removeKeywordDto.getKeywordId()) {
            Optional<Keyword> keyword = keywordService.findOne(keywordId, member.orElse(null));

            if (!keyword.isPresent()) {
                return responseService.getFailResult(ErrorCode.KEYWORD_IS_NOT_FOUND);
            }

            deleteKeywords.add(keyword.get());
        }

        if (!deleteKeywords.isEmpty()) {
            for (Keyword keyword : deleteKeywords) {
                keywordService.delete(keyword);
            }
        }

        return responseService.getSuccessResult();
    }
}
