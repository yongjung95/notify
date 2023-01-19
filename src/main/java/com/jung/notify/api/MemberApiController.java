package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.common.StringUtil;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    private final ResponseService responseService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @PostMapping("/v1/member")
    public SingleResult<?> member(@RequestBody MemberDto.SaveMember saveMember){
        List<String> list = Arrays.asList(saveMember.getId(), saveMember.getPasswd(), saveMember.getEmail());
        if(list.stream().anyMatch(StringUtil::isNullOrEmpty)) return responseService.getFailResult(ErrorCode.PARAMETER_IS_EMPTY);

        saveMember.setPasswd(bCryptPasswordEncoder.encode(saveMember.getPasswd()));

        memberService.saveMember(saveMember);

        return responseService.getSuccessResult();
    }
}
