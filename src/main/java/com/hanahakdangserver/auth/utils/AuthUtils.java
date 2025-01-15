package com.hanahakdangserver.auth.utils;

import java.util.UUID;

public class AuthUtils {

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }
}
