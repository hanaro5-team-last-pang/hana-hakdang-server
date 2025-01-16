package com.hanahakdangserver.auth.mapper;

import com.hanahakdangserver.auth.dto.MenteeSignupRequest;
import com.hanahakdangserver.auth.dto.MentorSignupRequest;

public class AuthMapper {

  public static MentorSignupRequest toMentorSignupRequest(MentorSignupRequest mentorSignupRequest,
      String encodedPassword) {
    return MentorSignupRequest.builder()
        .code(mentorSignupRequest.getCode())
        .email(mentorSignupRequest.getEmail())
        .name(mentorSignupRequest.getName())
        .password(encodedPassword)
        .confirmedPassword(encodedPassword)
        .birth(mentorSignupRequest.getBirth())
        .build();
  }

  public static MenteeSignupRequest toMenteeSignupRequest(MenteeSignupRequest menteeSignupRequest,
      String encodedPassword) {
    return MenteeSignupRequest.builder()
        .email(menteeSignupRequest.getEmail())
        .name(menteeSignupRequest.getName())
        .password(encodedPassword)
        .confirmedPassword(encodedPassword)
        .birth(menteeSignupRequest.getBirth())
        .build();
  }

}
