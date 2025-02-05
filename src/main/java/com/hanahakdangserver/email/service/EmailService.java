package com.hanahakdangserver.email.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${front.domain}")
  private String domain;

  @Async
  public void send(String email, String authToken) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      helper.setTo(email);
      helper.setSubject("하나학회 회원가입 인증 링크입니다✨");
      String content = String.format("<a href='%s?email=%s&authToken=%s'>회원가입 인증 링크</a>", domain,
          email, authToken);
//      String content = authToken; //임시
      helper.setText(content, true);

      javaMailSender.send(mimeMessage);

    } catch (jakarta.mail.MessagingException e) {
      throw new RuntimeException(e);
    }
  }

}
