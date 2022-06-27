package com.jung.notify.service;

import com.jung.notify.dto.Member;
import com.jung.notify.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long saveMember(Member member){
        return memberRepository.save(member);
    }

    public Member findMemberByUid(Long uid){
        return memberRepository.findByUid(uid);
    }

    public Member findMemberById(String id){
        return memberRepository.findById(id);
    }
}
