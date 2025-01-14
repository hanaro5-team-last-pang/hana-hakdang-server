package com.hanahakdangserver.notification.exception;

public class KafkaTopicCreationFailException extends RuntimeException {

  public KafkaTopicCreationFailException(String message) {
    super(message);
  }
}
