package com.hanahakdangserver.redis;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static com.hanahakdangserver.redis.RedisExceptionEnum.CANT_CONVERT_INTO_DTO;
import static com.hanahakdangserver.redis.RedisExceptionEnum.CANT_CONVERT_INTO_JSON;

/**
 * 레디스 Bound Set
 *
 * @param <E> 집합에 들어갈 원소의 타입
 * @author magae1
 */
@Log4j2
public class RedisBoundSet<E> {

  private final BoundSetOperations<String, String> boundSetOpts;
  private final ObjectMapper objectMapper;
  private final String key;

  /**
   * @param key           bound로 설정할 key값
   * @param redisTemplate 레디스와 연결할 템플릿
   * @param objectMapper  json을 obj로, obj을 json으로 변환해주는 맵퍼
   */
  public RedisBoundSet(String key, RedisTemplate<String, String> redisTemplate,
      ObjectMapper objectMapper) {
    this.key = key;
    this.boundSetOpts = redisTemplate.boundSetOps(key);
    this.objectMapper = objectMapper;
  }

  /**
   * 집합에 원소를 추가합니다.
   *
   * @param element 원소
   */
  public void add(E element) {
    log.debug("Adding {} into {}", element, key);
    try {
      String elementJsonStr = objectMapper.writeValueAsString(element);
      boundSetOpts.add(elementJsonStr);
    } catch (JsonProcessingException e) {
      log.error(CANT_CONVERT_INTO_JSON.getMessage());
    }
  }

  /**
   * 집합에 있는 모든 원소를 가져옵니다.
   *
   * @param clazz 원소 타입 토큰
   * @return 모든 원소 객체의 집합
   */
  public Set<E> getAll(Class<E> clazz) {
    log.debug("Getting all elements from {}", key);
    Set<E> resultSet = new HashSet<>();

    try {
      for (String str : Objects.requireNonNull(boundSetOpts.members())) {
        E elementObj = objectMapper.readValue(str, clazz);
        resultSet.add(elementObj);
      }
    } catch (JsonProcessingException e) {
      log.error(CANT_CONVERT_INTO_DTO.getMessage());
    }

    return resultSet;
  }

  /**
   * 집합에 원소 존재 여부 확인합니다.
   *
   * @param element 원소
   * @return 원소가 존재하면 {@code true}, 존재하지 않으면 {@code false}
   */
  public boolean exists(E element) {
    log.debug("Checking if element exists in {}", key);
    try {
      String elementJsonStr = objectMapper.writeValueAsString(element);
      return Boolean.TRUE.equals(boundSetOpts.isMember(elementJsonStr));
    } catch (JsonProcessingException e) {
      log.error(CANT_CONVERT_INTO_JSON.getMessage());
    }
    return false;
  }

  /**
   * 집합에서 원소를 삭제합니다.
   *
   * @param element 원소
   */
  public void remove(E element) {
    log.debug("Removing {} from {}", element, key);
    try {
      String elementJsonStr = objectMapper.writeValueAsString(element);
      boundSetOpts.remove(elementJsonStr);
    } catch (JsonProcessingException e) {
      log.error(CANT_CONVERT_INTO_JSON.getMessage());
    }
    log.debug("Removed element {}", element);
  }

  /**
   * 집합에 속하는 원소들의 개수를 확인합니다.
   *
   * @return 집합 속 원소들의 개수
   */
  public Long count() {
    log.debug("Count of elements in {}", key);
    return boundSetOpts.size();
  }

}
