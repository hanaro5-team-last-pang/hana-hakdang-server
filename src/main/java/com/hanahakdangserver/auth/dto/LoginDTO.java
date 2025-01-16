package com.hanahakdangserver.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTO {

  private String email;
  private String password;

}
