package com.hanahakdangserver.auth.security;

import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;

  private static final String ERROR_CODE_ATTRIBUTE = "errorCode";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String accessToken = tokenProvider.resolveTokenFromRequest(request); // request 헤더에서 토큰 가져오기
      if (StringUtils.hasText(accessToken)) {
        handleAccessToken(accessToken, request, response);
      }
      filterChain.doFilter(request, response); // 다음 필터로 넘어가기
    } catch (Exception e) {
      handleInvalidToken(request, response, AuthResponseExceptionEnum.AUTHORIZATION_FAILED);
    }
  }

  private void handleAccessToken(String accessToken, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    try {
      if (tokenProvider.validateToken(accessToken)) {
        // TODO : 로그아웃 구현
        setAuthentication(accessToken);
      }
    } catch (ExpiredJwtException e) {
      log.warn("Access token has expired", e);
      handleInvalidToken(request, response, AuthResponseExceptionEnum.UNAUTHORIZED_TOKEN);
    }
  }

  private void setAuthentication(String token) {
    // 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
    SecurityContextHolder.getContext()
        .setAuthentication(tokenProvider.getUserAuthentication(token));
  }

  // 토큰 관련 예외 처리
  private void handleInvalidToken(HttpServletRequest request, HttpServletResponse response,
      AuthResponseExceptionEnum errorCode) throws IOException {
    request.setAttribute(ERROR_CODE_ATTRIBUTE, errorCode);
  }
}
