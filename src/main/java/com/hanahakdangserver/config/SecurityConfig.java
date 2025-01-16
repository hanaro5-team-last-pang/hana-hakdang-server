package com.hanahakdangserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
        .csrf(CsrfConfigurer::disable) // CSRF 보호 비활성화
        .formLogin(FormLoginConfigurer::disable) // 폼 로그인 비활성화; 임시
        .logout(AbstractHttpConfigurer::disable) // 로그아웃 비활성화; 임시
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/swagger-ui/**")
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/signup/menti", "/check/**", "/send-email", "/verify-email",
                "/review/**", "faq/**", "answer/**")
            .permitAll()
            .requestMatchers("/signup/**", "/check/**", "/send-email", "/verify-email")
            .permitAll()
//            .requestMatchers("/error/**", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg")
//            .permitAll() // 임시
//            .requestMatchers(HttpMethod.OPTIONS, "/**")
//            .permitAll()
            .anyRequest().permitAll());

    return http.build();
  }
}