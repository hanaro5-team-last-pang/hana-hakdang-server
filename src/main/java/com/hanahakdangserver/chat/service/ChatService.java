package com.hanahakdangserver.chat.service;

import java.util.List;

import com.hanahakdangserver.chat.dto.ChatMessageResponse;
import com.hanahakdangserver.redis.RedisBoundList;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hanahakdangserver.chat.enums.ChatResponseExceptionEnum.FAILED_TO_RETRIEVE_CHAT_MESSAGES;
import static com.hanahakdangserver.chat.enums.ChatResponseExceptionEnum.INVALID_CLASSROOM_ID;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

  private final RedisBoundList<ChatMessageResponse> redisBoundList;

  /**
   * 채팅 내역을 가져옵니다.
   *
   * @param classroomId 강의실 ID
   * @return 채팅 메시지 리스트
   */
  public List<ChatMessageResponse> getChatMessage(Long classroomId) {
    try {
      if (classroomId == null || classroomId <= 0) {
        throw INVALID_CLASSROOM_ID.createResponseStatusException();
      }
      String key = classroomId.toString();
      log.debug("ClassroomId: {}", key);
      return redisBoundList.getList(key);
    } catch (Exception e) {
      throw FAILED_TO_RETRIEVE_CHAT_MESSAGES.createResponseStatusException();
    }
  }
}
