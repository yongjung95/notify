package com.jung.notify.validator;

import com.jung.notify.dto.Member;
import com.jung.notify.dto.MemberRole;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class LoginIdPwValidator implements UserDetailsService {

    private final MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(rawPassword.toString().getBytes());
                    return bytesToHex(md.digest());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (rawPassword == null) {
                    throw new IllegalArgumentException("rawPassword cannot be null");
                }
                if (encodedPassword == null || encodedPassword.length() == 0) {
                    return false;
                }

                String encodedRawPw = this.bytesToHex(rawPassword.toString().getBytes());
                if (encodedPassword.length() != encodedRawPw.length()) {
                    return false;
                }
                for (int i = 0; i < encodedPassword.length(); i++) {
                    if (encodedPassword.charAt(i) != encodedRawPw.charAt(i))
                        return false;
                }
                return true;
            }

            private String bytesToHex(byte[] bytes) {
                StringBuilder builder = new StringBuilder();
                for (byte b : bytes) {
                    builder.append(String.format("%02x", b));
                }
                return builder.toString();
            }
        };
    }

    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        Member member = memberService.findMemberById(insertedId);

        if (member == null) {
            return null;
        }

        String passwd = member.getPasswd();
        MemberRole memberRole = member.getMemberRole();

        return User.builder()
                .username(insertedId)
                .password(passwd)
                .roles(memberRole.toString())
                .build();
    }
}