package com.jung.notify.config;

import com.jung.notify.domain.MemberRole;
import com.jung.notify.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록 된다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginService loginService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //csrf 비활성화
        http.authorizeRequests()
                .antMatchers("/login","/loginForm","/member/new","/v1/member").permitAll() // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/admin/**").hasAuthority(MemberRole.ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/home", true)
                .failureForwardUrl("/loginError")
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/img/**","/frontend/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }
}