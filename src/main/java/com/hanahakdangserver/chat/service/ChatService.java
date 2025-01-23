package com.hanahakdangserver.chat.service;

import java.util.List;

import com.hanahakdangserver.chat.dto.ChatMessageResponse;
import com.hanahakdangserver.chat.enums.ChatResponseExceptionEnum;
import com.hanahakdangserver.redis.RedisBoundList;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        throw ChatResponseExceptionEnum.INVALID_CLASSROOM_ID.createRuntimeException();
      }
      String key = classroomId.toString();
      log.debug("키에 대한 채팅 메시지: {}", key);
      return redisBoundList.getList(key);
    } catch (Exception e) {
      throw ChatResponseExceptionEnum.FAILED_TO_RETRIEVE_CHAT_MESSAGES.createRuntimeException(e);
    }
  }
}
