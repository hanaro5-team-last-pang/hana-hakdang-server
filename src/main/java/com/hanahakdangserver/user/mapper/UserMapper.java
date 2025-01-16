package com.hanahakdangserver.user.mapper;

import com.hanahakdangserver.auth.dto.MenteeSignupRequest;
import com.hanahakdangserver.auth.dto.MentorSignupRequest;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;

public class UserMapper {

  public static User toEntity(MenteeSignupRequest mentee) {
    return User.builder()
        .role(Role.MENTEE)
        .email(mentee.getEmail())
        .name(mentee.getName())
        .password(mentee.getPassword())
        .birthDate(mentee.getBirth())
        .build();
  }

  public static User toEntity(MentorSignupRequest mentor, CareerInfo career) {
    return User.builder()
        .role(Role.MENTOR)
        .email(mentor.getEmail())
        .name(mentor.getName())
        .password(mentor.getPassword())
        .careerInfo(career)
        .birthDate(mentor.getBirth())
        .build();
  }

}
