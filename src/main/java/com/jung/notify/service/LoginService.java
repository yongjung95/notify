package com.jung.notify.service;

import com.jung.notify.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        Optional<MemberDto.SelectMember> selectMember = memberService.findMemberById(insertedId);

        if (!selectMember.isPresent()) {
            throw new UsernameNotFoundException(insertedId);
        }

        String pw = selectMember.get().getPasswd(); //"d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db"
        String roles = selectMember.get().getMemberRole().toString(); //"USER"

        return User.builder()
                .username(insertedId)
                .password(pw)
                .roles(roles)
                .build();
    }
}