package com.hanahakdangserver.redis;

import java.time.Duration;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.hanahakdangserver.redis.RedisExceptionEnum.CANT_DELETE_BY_KEY;
import static com.hanahakdangserver.redis.RedisExceptionEnum.CANT_GET_VALUE;
import static com.hanahakdangserver.redis.RedisExceptionEnum.ERROR_DURING_OPERATION;

/**
 * RedisTemplate<String, String>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class RedisString {

  private final RedisTemplate<String, String> redisTemplate;

  /**
   * Redis에 값을 저장
   *
   * @param key   저장할 키
   * @param value 저장할 value
   */
  public void put(String key, String value) {
    try {
      log.debug("Putting {}-{} into Redis", key, value);
      redisTemplate.opsForValue().set(key, value);
    } catch (Exception e) {
      log.error(ERROR_DURING_OPERATION.getMessage());
    }
  }

  /**
   * Redis에 값을 저장 + TTL 설정
   *
   * @param key      저장할 키
   * @param value    저장할 value
   * @param duration 설정할 TTL
   */
  public void putWithTTL(String key, String value, Duration duration) {
    try {
      log.debug("Putting {}-{} into Redis with TTL {}", key, value, duration);
      redisTemplate.opsForValue().set(key, value, duration);
    } catch (Exception e) {
      log.error(ERROR_DURING_OPERATION.getMessage());
    }
  }

  /**
   * key를 통해 value를 가져옴
   *
   * @param key 키
   * @return 가져온 값의 Optional
   */
  public Optional<String> get(String key) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      log.debug("Getting {}-{} from Redis", key, value);
      return Optional.ofNullable(value);
    } catch (Exception e) {
      log.error(CANT_GET_VALUE.getMessage());
      return Optional.empty();
    }
  }

  /**
   * key로 삭제
   *
   * @param key 삭제할 키
   * @return 삭제 성공 여부
   */
  public boolean delete(String key) {
    try {
      log.debug("Deleting {} from Redis", key);
      Boolean result = redisTemplate.delete(key);
      return Boolean.TRUE.equals(result);
    } catch (Exception e) {
      log.error(CANT_DELETE_BY_KEY.getMessage());
      return false;
    }
  }

  /**
   * Redis에 키가 존재하는지 확인
   *
   * @param key 확인할 키
   * @return 키 존재 여부
   */
  public boolean exists(String key) {
    try {
      log.debug("Checking existence of {} in Redis", key);
      Boolean result = redisTemplate.hasKey(key);
      return Boolean.TRUE.equals(result);
    } catch (Exception e) {
      log.error(ERROR_DURING_OPERATION.getMessage());
      return false;
    }
  }

  /**
   * Redis에서 키의 값을 증가시킴. INCR 명령어 구현
   *
   * @param key   증가시킬 키
   * @param delta 증가량 (양수)
   * @return 증가된 값
   */
  public Long increment(String key, long delta) {
    try {
      log.debug("Incrementing {} by {} in Redis", key, delta);
      Long newValue = redisTemplate.opsForValue().increment(key, delta);
      log.debug("New value of {} is {}", key, newValue);
      return newValue;
    } catch (Exception e) {
      log.error(ERROR_DURING_OPERATION.getMessage());
      return null;
    }
  }

  /**
   * Redis에서 키의 값을 감소시킴. DECR 명령어 구현
   *
   * @param key   감소시킬 키
   * @param delta 감소량 (양수음수)
   * @return 감소된 값
   */
  public Long decrement(String key, long delta) {
    try {
      log.debug("Decrementing {} by {} in Redis", key, delta);
      Long newValue = redisTemplate.opsForValue().increment(key, -delta);  // -delta로 감소시킴
      log.debug("New value of {} is {}", key, newValue);
      return newValue;
    } catch (Exception e) {
      log.error(ERROR_DURING_OPERATION.getMessage());
      return null;
    }
  }

}
