package com.jung.notify.service;

import com.jung.notify.domain.Member;
import com.jung.notify.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        Member selectMember = memberRepository.findById(insertedId);

        if (selectMember == null) {
            throw new UsernameNotFoundException(insertedId);
        }

        String pw = selectMember.getPasswd(); //"d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db"
        String roles = selectMember.getMemberRole().toString(); //"USER"

        return User.builder()
                .username(insertedId)
                .password(pw)
                .roles(roles)
                .build();
    }
}