package com.hanahakdangserver.user.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MentiSignupRequest {

  private Long id;
  private String email;
  private String name;
  private String password;
  private String confirmedPassword;
  private LocalDate birth;

}
