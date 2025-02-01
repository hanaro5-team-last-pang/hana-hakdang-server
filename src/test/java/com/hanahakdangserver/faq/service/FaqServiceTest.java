package com.hanahakdangserver.faq.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hanahakdangserver.faq.dto.FaqRequest;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.faq.repository.AnswerRepository;
import com.hanahakdangserver.faq.repository.FaqRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class FaqServiceTest {

  @Mock
  private FaqRepository faqRepository;

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private LectureRepository lectureRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private FaqService faqService;

  private User user;
  private Lecture lecture;
  private Faq faq;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(1L)
        .name("테스트 유저")
        .build();

    lecture = Lecture.builder()
        .id(1L)
        .title("테스트 강의")
        .build();

    faq = Faq.builder()
        .id(1L)
        .user(user)
        .lecture(lecture)
        .content("테스트 FAQ 질문")
        .build();
  }


  @Test
  @DisplayName("FAQ 생성")
  void createFaq() {
    // Given
    FaqRequest request = new FaqRequest("FAQ 질문 내용");
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
    when(faqRepository.save(any(Faq.class))).thenReturn(faq);

    ArgumentCaptor<Faq> faqCaptor = ArgumentCaptor.forClass(Faq.class);

    // when
    FaqResponse response = faqService.createFaq(1L, request, 1L);

    // Then
    assertNotNull(response);
    verify(faqRepository).save(faqCaptor.capture());

    Faq capturedFaq = faqCaptor.getValue();
    assertEquals("FAQ 질문 내용", capturedFaq.getContent());
    assertEquals(user, capturedFaq.getUser());
    assertEquals(lecture, capturedFaq.getLecture());
  }


  @Test
  @DisplayName("FAQ 삭제 - 성공")
  void deleteFaq_Success() {
    // Given
    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));

    ArgumentCaptor<Faq> faqCaptor = ArgumentCaptor.forClass(Faq.class);

    // When
    faqService.deleteFaq(1L, 1L);

    // Then
    verify(faqRepository).delete(faqCaptor.capture());
    assertEquals(1L, faqCaptor.getValue().getId());
  }


  @Test
  @DisplayName("FAQ 삭제 - 권한 없는 유저 삭제")
  void deleteFaq_UnauthorizedUser() {
    // Given
    when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));

    // When & Then
    assertThrows(RuntimeException.class, () -> faqService.deleteFaq(1L, 2L));
  }


  @Test
  @DisplayName("FAQ 삭제 - 존재하지 않는 FAQ 삭제")
  void deleteFaq_FaqNotFound() {
    // Given
    when(faqRepository.findById(99L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> faqService.deleteFaq(99L, 1L));
  }

}
