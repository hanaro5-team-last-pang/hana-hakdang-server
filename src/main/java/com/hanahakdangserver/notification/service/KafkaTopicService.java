package com.hanahakdangserver.notification.service;


import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import com.hanahakdangserver.notification.exception.KafkaTopicCreationFailException;
import com.hanahakdangserver.notification.exception.KafkaTopicDeletionFailException;


/**
 * Kafka 토픽에 대한 service. 토픽 별로 publisher가 메시지를 생성하고, consumer가 구독합니다.
 *
 * @author magae1
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class KafkaTopicService {

  private final AdminClient adminClient;

  /**
   * 새 토픽을 생성합니다. 이미 존재하는 {@code topicName}의 토픽에 대해서는 설정만 변경됩니다.
   *
   * @param topicName      생성할 토픽 이름
   * @param partitionCount 토픽에 대한 파티션 수
   * @param ttl            토픽에 보관할 메시지의 수명(초 단위). 0 보다 크거나 같아야 합니다.
   * @return 생성된 토픽 이름을 반환. {@code topicName}과 동일
   * @throws KafkaTopicCreationFailException 토픽 생성 실패 오류
   */
  public String createTopic(String topicName, int partitionCount, int ttl)
      throws KafkaTopicCreationFailException {
    assert ttl >= 0;
    try {
      NewTopic newTopic = TopicBuilder
          .name(topicName)
          .partitions(partitionCount)
          .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(1000 * ttl))
          .compact()
          .build();
      adminClient.createTopics(Collections.singletonList(newTopic));
      return topicName;
    } catch (Exception e) {
      log.error("토픽 {} 생성 실패", topicName);
      throw new KafkaTopicCreationFailException("토픽 생성 실패");
    }
  }

  /**
   * 토픽에 대한 설명을 가져옵니다.
   *
   * @param topicName 설명을 가져올 토픽 이름
   * @return DescribeTopicsResult을 반환
   */
  public DescribeTopicsResult describeTopic(String topicName) {
    return adminClient.describeTopics(Collections.singleton(topicName));
  }

  /**
   * 토픽을 삭제합니다.
   *
   * @param topicName 삭제할 토픽 이름
   * @return 삭제된 토픽 이름을 반환. {@code topicName}과 동일
   * @throws KafkaTopicDeletionFailException 토픽 삭제 실패 오류
   */
  public String deleteTopic(String topicName) throws KafkaTopicDeletionFailException {
    try {
      adminClient.deleteTopics(Collections.singletonList(topicName));
      return topicName;
    } catch (Exception e) {
      log.error("토픽 {} 삭제 실패", topicName);
      throw new KafkaTopicDeletionFailException("토픽 삭제 실패");
    }
  }
}
