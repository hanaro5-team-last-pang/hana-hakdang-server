package com.hanahakdangserver.faq.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hanahakdangserver.faq.dto.AnswerRequest;
import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.faq.repository.AnswerRepository;
import com.hanahakdangserver.faq.repository.FaqRepository;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private FaqRepository faqRepository;

  @InjectMocks
  private AnswerService answerService;

  private User user;
  private Faq faq;
  private Answer answer;


  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(1L)
        .name("테스트 유저")
        .build();

    faq = Faq.builder()
        .id(1L)
        .user(user)
        .content("FAQ 질문입니다.")
        .build();

    answer = Answer.builder()
        .id(1L)
        .faq(faq)
        .content("테스트 답변입니다.")
        .build();
  }


  @Test
  @DisplayName("답변 생성 - 성공")
  void createAnswer_Success() {
    // Given
    AnswerRequest request = new AnswerRequest(1L, "답변 내용입니다.");
    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));
    when(answerRepository.save(any(Answer.class))).thenReturn(answer);

    ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);

    // When
    AnswerResponse response = answerService.createAnswer(request);

    // Then
    assertNotNull(response);
    verify(answerRepository).save(answerCaptor.capture());

    Answer capturedAnswer = answerCaptor.getValue();
    assertEquals("답변 내용입니다.", capturedAnswer.getContent());
    assertEquals(faq, capturedAnswer.getFaq());
  }

  @Test
  @DisplayName("답변 삭제 - 성공")
  void deleteAnswer_Success() {
    // Given
    when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
    ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);

    // When
    answerService.deleteAnswer(1L);

    // Then
    verify(answerRepository).delete(answerCaptor.capture());
    assertEquals(1L, answerCaptor.getValue().getId());
  }

  @Test
  @DisplayName("답변 삭제 - 존재하지 않는 답변 삭제 예외 발생")
  void deleteAnswer_AnswerNotFound() {
    // Given
    when(answerRepository.findById(99L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> answerService.deleteAnswer(99L));
  }
}