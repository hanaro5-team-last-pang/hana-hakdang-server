package com.hanahakdangserver.config;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hanahakdangserver.auth.security.CustomAuthenticationFailureHandler;
import com.hanahakdangserver.auth.security.CustomAuthenticationSuccessHandler;
import com.hanahakdangserver.auth.security.CustomUsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final ObjectMapper objectMapper;
  private final UserDetailsService userDetailsService;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .addFilterBefore(customJsonUsernamePasswordAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class)
        .httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
        .csrf(CsrfConfigurer::disable) // CSRF 보호 비활성화
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .logout(AbstractHttpConfigurer::disable) // 로그아웃 비활성화
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/swagger-ui/**")
            .permitAll()
            .requestMatchers("/signup/**", "/check/**", "/send-email", "/verify-email", "/login")
            .permitAll()
            .requestMatchers("/profile-card/me/**").authenticated()
            .requestMatchers("/lectures/**", "/lectures/category/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/lectures/queue/mentor/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/lectures/register/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/lectures/{lectureId}/enroll/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/lectures/{enrollmentId}/enroll-withdraw/**")
            .authenticated()
            .requestMatchers("/classrooms/**").authenticated()
            // 리뷰 요청에 대한 인증
            .requestMatchers("/review/lecture/").hasRole("MENTOR")
            .requestMatchers(HttpMethod.GET, "/faq/**").authenticated() // 조회는 모든 인증 사용자 가능
            .requestMatchers(HttpMethod.POST, "/faq/**").hasRole("MENTEE") // 등록은 멘티만 가능
            .requestMatchers(HttpMethod.DELETE, "/faq/**").hasRole("MENTEE") // 삭제는 멘티만 가능
            .requestMatchers("/review/**").authenticated()
            .requestMatchers("/user-info").authenticated()
//            .requestMatchers("/error/**", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg")
//            .permitAll() // 임시
//            .requestMatchers(HttpMethod.OPTIONS, "/**")
//            .permitAll()
            .anyRequest().permitAll());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(provider);
  }

  @Bean
  public CustomUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
    CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(
        objectMapper);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
    filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
    return filter;
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