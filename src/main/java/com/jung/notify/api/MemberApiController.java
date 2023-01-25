package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    private final ResponseService responseService;

    @PostMapping("/v1/member")
    public SingleResult<?> member(@RequestBody @Valid MemberDto.SaveMember saveMember, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        return memberService.saveMember(saveMember) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.DUPLICATION_MEMBER);
    }

    @PutMapping("/v1/member")
    public SingleResult<?> memberUpdate(@RequestBody @Valid MemberDto.UpdateMember updateMember, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        MemberDto.SelectMember selectMember = memberService.findByEmailAndNotUid(updateMember.getEmail(), updateMember.getUid());

        if (selectMember != null) {
            return responseService.getFailResult(ErrorCode.DUPLICATION_MEMBER);
        }

        return memberService.updateMember(updateMember) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
    }

    @PostMapping("/v1/member/id")
    public SingleResult<?> findMemberId(@RequestBody @Valid MemberDto.SearchMemberId searchMemberId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        MemberDto.SelectMember findMember = memberService.findMemberByEmail(searchMemberId.getEmail());

        if (findMember == null) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        return responseService.getSingleResult(findMember.getId());
    }

    @PostMapping("/v1/member/passwd")
    public SingleResult<?> findMemberPasswd(@RequestBody @Valid MemberDto.SearchMemberPw searchMemberPw, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                return responseService.getFailParameter(allError.getDefaultMessage());
            }
        }

        MemberDto.SelectMember findMember = memberService.findMemberByIdAndEmail(searchMemberPw.getId(), searchMemberPw.getEmail());

        if (findMember == null) {
            return responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
        }

        return memberService.resetPasswd(MemberMapper.INSTANCE.selectMemberToUpdateMember(findMember)) ? responseService.getSuccessResult() : responseService.getFailResult(ErrorCode.MEMBER_IS_NOT_FOUND);
    }
}
