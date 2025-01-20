package com.hanahakdangserver.config;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
            .requestMatchers("/lecture/**").permitAll() // TODO : 추후 authenticated로
            .requestMatchers("/lectures/**").permitAll() // TODO : 추후 authenticated로
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