package com.hanahakdangserver.auth.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum;

@Log4j2
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final String ERROR_CODE_ATTRIBUTE = "errorCode";

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      org.springframework.security.core.AuthenticationException authException) throws IOException {

    // 요청 속성에서 ErrorCode 가져오기
    AuthResponseExceptionEnum errorCode = (AuthResponseExceptionEnum) request.getAttribute(
        ERROR_CODE_ATTRIBUTE);

    if (errorCode == null) {
      errorCode = AuthResponseExceptionEnum.AUTHORIZATION_FAILED; // 기본 ErrorCode 설정
    }

    log.error("에러 메시지: {}", errorCode.getMessage());

    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType("application/json; charset=UTF-8");
    response.getWriter()
        .write("{\"message\": \"" + errorCode.getMessage() + "\", \"result\": null}");
  }
}
