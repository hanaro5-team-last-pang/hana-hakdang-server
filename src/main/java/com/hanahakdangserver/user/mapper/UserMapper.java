package com.hanahakdangserver.user.mapper;

import com.hanahakdangserver.user.dto.MentiSignupRequest;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;

public class UserMapper {

  public static User toMentiEntity(String encodedPassword, MentiSignupRequest menti) {
    return User.builder()
        .id(menti.getId())
        .role(Role.MENTEE)
        .email(menti.getEmail())
        .name(menti.getName())
        .password(encodedPassword)
        .birthDate(menti.getBirth())
        .build();
  }

}
