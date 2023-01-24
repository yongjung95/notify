package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.common.StringUtil;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    private final ResponseService responseService;

    @PostMapping("/v1/member")
    public SingleResult<?> member(@RequestBody MemberDto.SaveMember saveMember) {
        List<String> list = Arrays.asList(saveMember.getId(), saveMember.getPasswd(), saveMember.getEmail());

        if (list.stream().anyMatch(StringUtil::isNullOrEmpty))
            return responseService.getFailResult(ErrorCode.PARAMETER_IS_EMPTY);

        if (!memberService.checkEmail(saveMember.getEmail())) {
            return responseService.getFailResult(ErrorCode.NOT_EMAIL_PATTERN);
        }

        return memberService.saveMember(saveMember) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.DUPLICATION_MEMBER);
    }

    @PutMapping("/v1/member")
    public SingleResult<?> memberUpdate(@RequestBody MemberDto.UpdateMember updateMember) {
        if (!memberService.checkEmail(updateMember.getEmail())) {
            return responseService.getFailResult(ErrorCode.NOT_EMAIL_PATTERN);
        }

        MemberDto.SelectMember selectMember = memberService.findByEmailAndNotUid(updateMember.getEmail(), updateMember.getUid());

        if (selectMember != null) {
            return responseService.getFailResult(ErrorCode.DUPLICATION_MEMBER);
        }

        return memberService.updateMember(updateMember) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
    }

    @PostMapping("/v1/member/id")
    public SingleResult<?> findMemberId(@RequestBody MemberDto.SelectMember selectMember) {
        if (!memberService.checkEmail(selectMember.getEmail())) {
            return responseService.getFailResult(ErrorCode.NOT_EMAIL_PATTERN);
        }

        MemberDto.SelectMember findMember = memberService.findMemberByEmail(selectMember.getEmail());

        if (findMember == null) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        return responseService.getSingleResult(findMember.getId());
    }

    @PostMapping("/v1/member/passwd")
    public SingleResult<?> findMemberPasswd(@RequestBody MemberDto.SelectMember selectMember) {
        if (!StringUtils.hasText(selectMember.getId()) || !StringUtils.hasText(selectMember.getEmail())) {
            return responseService.getFailResult(ErrorCode.PARAMETER_IS_EMPTY);
        }

        if (!memberService.checkEmail(selectMember.getEmail())) {
            return responseService.getFailResult(ErrorCode.NOT_EMAIL_PATTERN);
        }

        MemberDto.SelectMember findMember = memberService.findMemberByIdAndEmail(selectMember.getId(), selectMember.getEmail());

        if (findMember == null) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        return memberService.resetPasswd(MemberMapper.INSTANCE.selectMemberToUpdateMember(findMember)) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
    }
}
