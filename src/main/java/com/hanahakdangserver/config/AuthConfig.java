package com.hanahakdangserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

  @Value("check-email")
  private String emailCheckHashKey;


  @Bean
  public String emailCheckHashKey() {
    return emailCheckHashKey;
  }


  @Autowired
  @Qualifier("securityPasswordEncoder")
  private PasswordEncoder passwordEncoder;

}
