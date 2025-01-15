package com.hanahakdangserver.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisService {

  private final RedisTemplate redisTemplate;

  // TODO: 시간 지나면 다시 발급받도록 refreshtoken 추가

  /**
   * Redis key값은 이메일, value는 token과 check. token : 라이브러리를 통해 생성받은 유니크 키 check : 유저가 이메일 링크를 눌렀는지 판단.
   * default value : false
   *
   * @param email
   * @param token
   */
  public void saveEmailAndValues(String email, String token) {
    redisTemplate.delete(email);

    redisTemplate.opsForHash().put(email, "check", "false");
    redisTemplate.opsForHash().put(email, "token", token);
  }

  public String getTokenByEmail(String email) {
    return (String) redisTemplate.opsForHash().get(email, "token");
  }

  public String getCheckByEmail(String email) {
    return (String) redisTemplate.opsForHash().get(email, "check");
  }

  public void updateCheckToTrue(String email) {
    redisTemplate.opsForHash().put(email, "check", "true");
  }

}
