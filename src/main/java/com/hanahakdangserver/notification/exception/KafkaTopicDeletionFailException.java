package com.hanahakdangserver.notification.exception;

public class KafkaTopicDeletionFailException extends RuntimeException {

  public KafkaTopicDeletionFailException(String message) {
    super(message);
  }
}
