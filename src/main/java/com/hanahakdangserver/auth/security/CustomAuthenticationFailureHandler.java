package com.hanahakdangserver.auth.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.USER_NOT_FOUND;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  /**
   * 인증 실패 시 실행되는 핸들러.
   * * 응답 바디에 로그인 실패 메시지를 작성하여 클라이언트에 전달한다.
   */

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {

    String message = "";

    if (exception instanceof BadCredentialsException) {
      message = USER_NOT_FOUND.getMessage();
    }

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{'message' :'" + message + "'}");
  }
}

