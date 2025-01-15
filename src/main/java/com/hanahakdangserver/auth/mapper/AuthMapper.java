package com.hanahakdangserver.auth.mapper;

import com.hanahakdangserver.auth.dto.EmailDTO;

public class AuthMapper {

  public static EmailDTO toDTO(String email) {
    return EmailDTO.builder()
        .email(email)
        .build();
  }

}
