package com.hanahakdangserver.email.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Async
  public void send(String email, String authToken) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      helper.setTo(email);
      helper.setSubject("하나학당 회원가입 인증 링크입니다.");

      String htmlContent = "<html>" +
          "<body>" +
          "<p>회원가입을 완료하려면 아래 링크를 클릭하세요✨</p>" +
          "<a href='http://localhost:8080/verify-email?email=" + email
          + "&authToken="
          + authToken + "'>회원가입 인증</a>" +
          "</body>" +
          "</html>";
      helper.setText(htmlContent, true);

      javaMailSender.send(mimeMessage);

    } catch (jakarta.mail.MessagingException e) {
      throw new RuntimeException(e);
    }
  }

}
