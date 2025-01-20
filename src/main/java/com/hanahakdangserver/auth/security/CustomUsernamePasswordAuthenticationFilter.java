package com.hanahakdangserver.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 커스텀 인증 필터 클래스 Spring Security의 기본 UsernamePasswordAuthenticationFilter를 참고 JSON 형식으로 요청 데이터를 받고,
 * 이메일을 사용자 이름(Username)으로 사용하는 방식으로 로그인 인증을 처리한다.
 */
public class CustomUsernamePasswordAuthenticationFilter extends
    AbstractAuthenticationProcessingFilter {

  private static final String CONTENT_TYPE = "application/json";
  private static final String USERNAME_KEY = "email";
  private static final String PASSWORD_KEY = "password";
  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
      "/login", "POST");

  private final ObjectMapper objectMapper;

  public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  /**
   * 인증 시도 메서드. 클라이언트가 전송한 JSON 데이터를 읽어 사용자 인증 정보를 추출하고, 추출한 이메일과 비밀번호를 사용하여 인증을 시도한다.
   * <p>
   * 추가설명 : HTTP 요청의 본문(body)은 바이트 형식으로 전달. 따라서 문자열로 변환하기 위해 StreamUtils를 사용 변환된 문자열을
   * objectMapper.readValue()를 통해 Map으로 파싱하여 클라이언트가 전송한 JSON 데이터를 처리
   *
   * @param request  HTTP 요청 객체
   * @param response HTTP 응답 객체
   * @return 인증된 사용자의 인증 토큰
   */

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
      throw new AuthenticationServiceException(
          "Authentication Content-Type not supported: " + request.getContentType());
    }

    String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

    Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

    String email = usernamePasswordMap.get(USERNAME_KEY);
    String password = usernamePasswordMap.get(PASSWORD_KEY);

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email,
        password);

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
