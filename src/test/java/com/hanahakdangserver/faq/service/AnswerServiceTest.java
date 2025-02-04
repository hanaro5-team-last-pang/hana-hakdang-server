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
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.faq.repository.AnswerRepository;
import com.hanahakdangserver.faq.repository.FaqRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.user.entity.User;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private FaqRepository faqRepository;

  @InjectMocks
  private AnswerService answerService;

  private User mentor;
  private User otherUser;
  private Lecture lecture;
  private Faq faq;
  private Answer answer;

  @BeforeEach
  void setUp() {
    mentor = User.builder()
        .id(1L)
        .name("멘토")
        .build();

    otherUser = User.builder()
        .id(2L)
        .name("일반 사용자")
        .build();

    lecture = Lecture.builder()
        .id(100L)
        .mentor(mentor)
        .title("테스트 강의")
        .build();

    faq = Faq.builder()
        .id(1L)
        .user(otherUser)
        .lecture(lecture)
        .content("FAQ 질문입니다.")
        .build();

    answer = Answer.builder()
        .id(1L)
        .faq(faq)
        .content("테스트 답변입니다.")
        .build();
  }

  @Test
  @DisplayName("답변 생성 - 강의 개설자가 등록할 수 있음")
  void createAnswer() {
    // Given
    AnswerRequest request = new AnswerRequest(1L, "답변 내용입니다.");

    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));
    when(answerRepository.save(any(Answer.class))).thenReturn(answer);

    ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);

    // When
    AnswerResponse response = answerService.createAnswer(1L, request, mentor.getId());

    // Then
    assertNotNull(response);
    verify(answerRepository).save(answerCaptor.capture());

    Answer capturedAnswer = answerCaptor.getValue();
    assertEquals("답변 내용입니다.", capturedAnswer.getContent());
    assertEquals(faq, capturedAnswer.getFaq());
  }


  @Test
  @DisplayName("답변 생성 - 강의 개설자가 아닌 사용자가 등록 시 예외 발생")
  void createAnswer_Fail() {
    // Given
    AnswerRequest request = new AnswerRequest(1L, "답변 내용입니다.");

    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));

    // When & Then
    assertThrows(RuntimeException.class,
        () -> answerService.createAnswer(1L, request, otherUser.getId()));
  }


  @Test
  @DisplayName("답변 삭제 - 강의 개설자가 삭제 가능")
  void deleteAnswer_Success() {
    // Given
    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));
    when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

    ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);

    // When
    answerService.deleteAnswer(1L, 1L, mentor.getId());

    // Then
    verify(answerRepository).delete(answerCaptor.capture());
    assertEquals(1L, answerCaptor.getValue().getId());
  }

  @Test
  @DisplayName("답변 삭제 - 강의 개설자가 아닌 사용자가 삭제 시 예외 발생")
  void deleteAnswer_Fail_WrongUser() {
    // Given
    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));

    // When & Then
    assertThrows(RuntimeException.class,
        () -> answerService.deleteAnswer(1L, 1L, otherUser.getId()));
  }


  @Test
  @DisplayName("답변 삭제 - 존재하지 않는 답변 삭제 시 예외 발생")
  void deleteAnswer_Fail_NotExsit() {
    // Given
    lenient().when(answerRepository.findById(99L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> answerService.deleteAnswer(1L, 99L, mentor.getId()));
  }

}
