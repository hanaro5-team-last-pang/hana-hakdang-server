package com.hanahakdangserver.lecture.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessRole {
  MENTOR("MENTOR"),
  NOT_LOGIN("NOT_ENROLLED"),
  MENTEE("MENTEE");

  private final String status;
}
