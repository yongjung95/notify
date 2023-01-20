package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.MemberRole;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public boolean saveMember(MemberDto.SaveMember saveMember) {
        Optional<MemberDto.SelectMember> selectMember = findMemberByIdOrEmail(saveMember.getId(), saveMember.getEmail());

        if (selectMember.isPresent()) {
            return false;
        }

        Member member = Member.builder()
                .id(saveMember.getId())
                .passwd(saveMember.getPasswd())
                .lineToken(saveMember.getLineToken())
                .email(saveMember.getEmail())
                .memberRole(MemberRole.MEMBER)
                .build();

        memberRepository.save(member);

        return true;
    }

    public MemberDto.SelectMember updateMember(MemberDto.UpdateMember updateMember) {
        Member member = memberRepository.findByUid(updateMember.getUid());

        member.updateMember(updateMember);

        memberRepository.save(member);

        return MemberMapper.INSTANCE.memberToSelectMember(member);
    }

    public MemberDto.SelectMember findMemberByUid(Long uid) {
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findByUid(uid));
    }

    public Optional<MemberDto.SelectMember> findMemberById(String id) {
        return Optional.ofNullable(MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findById(id).orElse(null)));
    }

    public Optional<MemberDto.SelectMember> findMemberByIdOrEmail(String id, String email) {
        return Optional.ofNullable(MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findByIdOrEmail(id, email).orElse(null)));
    }

    public List<MemberDto.SelectMember> findAllMember() {
        return MemberMapper.INSTANCE.membersToSelectMembers(memberRepository.findAllMember());
    }

    public boolean checkEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
