package com.hanahakdangserver.auth.security;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import static com.hanahakdangserver.auth.enums.AuthResponseSuccessEnum.LOG_IN_SUCCESS;

/**
 * 인증 성공 시 실행되는 핸들러. 세션 ID를 기반으로 쿠키를 생성하여 응답 헤더에 추가한다. 응답 바디에 로그인 성공 메시지를 작성하여 클라이언트에 전달한다.
 */
@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    HttpSession session = request.getSession(true);

    // 세션에 SecurityContext 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);
    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

    response.setHeader("Set-Cookie",
        session.getId() + "; Path=/; HttpOnly; Secure;");
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // 응답 JSON 객체 생성
    Map<String, String> responseBody = Map.of("message", LOG_IN_SUCCESS.getMessage());

    // JSON 문자열로 변환
    String jsonResponse = objectMapper.writeValueAsString(responseBody);

    response.getWriter().write(jsonResponse);

  }
}
