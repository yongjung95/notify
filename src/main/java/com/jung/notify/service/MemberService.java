package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberDto.SelectMember saveMember(MemberDto.SaveMember saveMember){
        Optional<MemberDto.SelectMember> selectMember = findMemberById(saveMember.getId());

        if (selectMember.isPresent()) {
            return null;
        }

        Member member = Member.builder()
                .id(saveMember.getId())
                .passwd(saveMember.getPasswd())
                .lineToken(saveMember.getLineToken())
                .build();

        memberRepository.save(member);

        return MemberMapper.INSTANCE.memberToSelectMember(member);
    }

    public MemberDto.SelectMember findMemberByUid(Long uid){
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findByUid(uid));
    }

    public Optional<MemberDto.SelectMember> findMemberById(String id){
        return Optional.ofNullable(MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findById(id).orElse(null)));
    }

    public List<MemberDto.SelectMember> findAllMember(){
        return MemberMapper.INSTANCE.membersToSelectMembers(memberRepository.findAllMember());
    }
}
