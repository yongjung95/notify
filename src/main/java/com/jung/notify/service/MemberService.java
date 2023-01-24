package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.MemberRole;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MailService mailService;

    public boolean saveMember(MemberDto.SaveMember saveMember) {
        MemberDto.SelectMember selectMember = findMemberByIdOrEmail(saveMember.getId(), saveMember.getEmail());

        if (selectMember != null) {
            return false;
        }

        Member member = Member.builder()
                .id(saveMember.getId())
                .passwd(bCryptPasswordEncoder.encode(saveMember.getPasswd()))
                .lineToken(saveMember.getLineToken())
                .email(saveMember.getEmail())
                .memberRole(MemberRole.MEMBER)
                .build();

        memberRepository.save(member);

        return true;
    }

    public boolean updateMember(MemberDto.UpdateMember updateMember) {
        Member member = findMemberByUid(updateMember.getUid());

        if (member == null) {
            return false;
        }

        if (StringUtils.hasText(updateMember.getPasswd())) {
            updateMember.setPasswd(bCryptPasswordEncoder.encode(updateMember.getPasswd()));
        }

        member.updateMember(updateMember);

        memberRepository.save(member);

        return true;
    }

    public boolean resetPasswd(MemberDto.UpdateMember updateMember) {
        Member member = findMemberByUid(updateMember.getUid());

        if (member == null) {
            return false;
        }

        String passwd = RandomString.make();

        updateMember.setPasswd(bCryptPasswordEncoder.encode(passwd));

        member.updateMember(updateMember);

        memberRepository.save(member);

        mailService.sendResetPasswd(member.getEmail(), passwd);

        return true;
    }

    public MemberDto.SelectMember findMemberByEmail(String email) {
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findMemberByEmail(email));

    }

    public MemberDto.SelectMember findMemberByIdAndEmail(String id, String email) {
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findMemberByIdAndEmail(id, email));
    }

    public Member findMemberByUid(Long uid) {
        return memberRepository.findByUid(uid);
    }

    public MemberDto.SelectMember findMemberById(String id) {
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findById(id));
    }

    public MemberDto.SelectMember findMemberByIdOrEmail(String id, String email) {
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findByIdOrEmail(id, email));
    }

    public MemberDto.SelectMember findByEmailAndNotUid(String email, Long uid) {
        return MemberMapper.INSTANCE.memberToSelectMember(memberRepository.findByEmailAndNotUid(email, uid));
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
