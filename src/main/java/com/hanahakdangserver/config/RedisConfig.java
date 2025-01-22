package com.hanahakdangserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${classroom.bound-key.entrance}")
  private String classroomEntranceHashBoundKey;

  @Value("${classroom.bound-key.lecture-id}")
  private String classroomLectureIdHashBoundKey;

  @Value("${classroom.bound-key.mentee-id-set}")
  private String classroomMenteeIdSetHashBoundKey;


  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(redisHost, redisPort);
  }

  @Bean
  public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

    return redisTemplate;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public String classroomEntranceHashBoundKey() {
    return classroomEntranceHashBoundKey;
  }

  @Bean
  public String classroomLectureIdHashBoundKey() {
    return classroomLectureIdHashBoundKey;
  }

  @Bean
  public String classroomMenteeIdSetHashBoundKey() {
    return classroomMenteeIdSetHashBoundKey;
  }

}
