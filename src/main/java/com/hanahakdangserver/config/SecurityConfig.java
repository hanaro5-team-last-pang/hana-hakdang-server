package com.hanahakdangserver.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hanahakdangserver.auth.security.CustomAuthenticationEntryPoint;
import com.hanahakdangserver.auth.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
        .csrf(CsrfConfigurer::disable) // CSRF 보호 비활성화
        .formLogin(FormLoginConfigurer::disable) // 폼 로그인 비활성화; JWT 사용하기 때문
        .logout(AbstractHttpConfigurer::disable) // 로그아웃 비활성화
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/swagger-ui/**")
            .permitAll()
            .requestMatchers("/signup/**", "/check/**", "/send-email", "/verify-email", "/login")
            .permitAll()
            .requestMatchers("/profile-card/me/**").authenticated()
            .requestMatchers("/lectures/**", "/lectures/category/**", "/search/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/lectures/queue/mentor/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/lectures/register/").authenticated()
            .requestMatchers(HttpMethod.POST, "/lectures/{lectureId}/enroll/").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/lectures/{enrollmentId}/enroll-withdraw/")
            .authenticated()
            .requestMatchers(HttpMethod.GET, "/lectures/history/mentee/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/lectures/queue/mentee/**").authenticated()
            .requestMatchers("/classrooms/**").authenticated()
//            // 리뷰 요청에 대한 인증
            .requestMatchers(HttpMethod.GET, "/lectures/reviews/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/lectures/reviews/{lectureId}/**")
            .authenticated()
            .requestMatchers(HttpMethod.DELETE, "/lectures/reviews/**").authenticated()

            .requestMatchers(HttpMethod.GET, "/lectures/faq/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/lectures/faq/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/lectures/faq/**").authenticated()

            .requestMatchers("/user-info").authenticated()
//            .requestMatchers("/error/**", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg")
//            .permitAll() // 임시
//            .requestMatchers(HttpMethod.OPTIONS, "/**")
//            .permitAll()
            .anyRequest().permitAll());

    return http.build();
  }

  @Bean
  @Qualifier("securityPasswordEncoder")
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "PUT"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}