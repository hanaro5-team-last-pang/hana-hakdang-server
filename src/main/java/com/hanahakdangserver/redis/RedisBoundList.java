package com.hanahakdangserver.redis;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static com.hanahakdangserver.redis.RedisExceptionEnum.CANT_CONVERT_INTO_DTO;
import static com.hanahakdangserver.redis.RedisExceptionEnum.CANT_CONVERT_INTO_JSON;


/**
 * 레디스 BoundList
 *
 * @param <E> 리스트에 들어갈 원소의 타입
 */
@Log4j2
public class RedisBoundList<E> {

  private final ObjectMapper objectMapper;

  private final RedisTemplate<String, String> redisTemplate;
  private final Map<String, BoundListOperations<String, String>> boundListOptsMap;


  /**
   * RedisBoundList 생성자
   *
   * @param redisTemplate 레디스와 연결할 템플릿
   * @param objectMapper  json을 object로, object를 json으로 변환해주는 맵퍼
   */
  public RedisBoundList(RedisTemplate<String, String> redisTemplate,
      ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
    this.boundListOptsMap = new HashMap<>();
  }

  private BoundListOperations<String, String> getOrInsertBoundListOpts(String key) {
    BoundListOperations<String, String> boundListOpts = boundListOptsMap.get(key);
    if (boundListOpts != null) {
      return boundListOpts;
    }
    boundListOpts = redisTemplate.boundListOps(key);
    boundListOptsMap.put(key, boundListOpts);
    return boundListOpts;
  }

  /**
   * 리스트에 원소를 추가합니다.
   *
   * @param key     bound 키
   * @param element 추가할 원소
   */
  public void push(String key, E element) {
    BoundListOperations<String, String> boundListOpts = getOrInsertBoundListOpts(key);
    try {
      String elementJsonStr = objectMapper.writeValueAsString(element);
      boundListOpts.rightPush(elementJsonStr);
      log.debug("Pushed {} - {} into Redis bound list", key, element);
    } catch (JsonProcessingException e) {
      log.error(CANT_CONVERT_INTO_JSON.getMessage());
    }
  }

  /**
   * bound {@code key}를 가지는 리스트의 TTL을 설정합니다.
   *
   * @param key      bound 키
   * @param timeout  만료시간
   * @param timeUnit 만료시간 단위
   */
  public void setTimeToLive(String key, long timeout, TimeUnit timeUnit) {
    redisTemplate.expire(key, timeout, timeUnit);
  }


  /**
   * 채팅 리스트를 가져옵니다.
   *
   * @param key Redis 키
   */
  public List<E> getList(String key) {
    BoundListOperations<String, String> boundListOpts = getOrInsertBoundListOpts(key);
    List<String> list = boundListOpts.range(0, -1); // 전체 리스트 가져오기
    log.debug("레디스에 저장된 채팅 목록: {}", list);
    if (list == null || list.isEmpty()) {
      return Collections.emptyList(); // 비어 있는 경우 빈 리스트 반환
    }
    return list.stream()
        .map(json -> {
          try {
            return objectMapper.readValue(json, new TypeReference<E>() {
            });
          } catch (JsonProcessingException e) {
            log.error(CANT_CONVERT_INTO_DTO.getMessage(), json, e);
            return null; // 실패한 항목 무시
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

}
