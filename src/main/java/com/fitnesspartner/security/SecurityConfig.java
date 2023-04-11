package com.fitnesspartner.security;

import com.fitnesspartner.security.authentication.AuthenticationFailureHandlerImpl;
import com.fitnesspartner.security.authentication.AuthenticationSuccessHandlerImpl;
import com.fitnesspartner.security.authentication.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    private final AuthenticationFailureHandlerImpl authenticationFailureHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 사용자 요청 정보로 UserPasswordAuthenticationToken 발급하는 필터
    @Bean
    public CustomAuthenticationFilter authenticationFilter() {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        // 필터 URL 설정
        authenticationFilter.setFilterProcessesUrl("/users/login");
        // 인증 성공 핸들러
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 인증 실패 핸들러
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // BeanFactory에 의해 모든 property가 설정되고 난 뒤 실행
        authenticationFilter.afterPropertiesSet();
        return authenticationFilter;
    }
}
