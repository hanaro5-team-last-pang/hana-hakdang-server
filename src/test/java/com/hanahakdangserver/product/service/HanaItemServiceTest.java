package com.hanahakdangserver.product.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.LectureTag;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.product.dto.HanaItemResponse;
import com.hanahakdangserver.product.entity.HanaItem;
import com.hanahakdangserver.product.entity.Tag;
import com.hanahakdangserver.product.repository.HanaItemRepository;
import com.hanahakdangserver.product.enums.HanaItemResponseExceptionEnum;

@ExtendWith(MockitoExtension.class)
public class HanaItemServiceTest {

  @Mock
  private HanaItemRepository hanaItemRepository;

  @Mock
  private LectureRepository lectureRepository;

  @InjectMocks
  private HanaItemService hanaItemService;

  private Lecture lecture;
  private Tag tag;
  private HanaItem hanaItem;

  @BeforeEach
  void setUp() {
    tag = Tag.builder()
        .id(1L)
        .tagName("정기예금")
        .build();

    lecture = Lecture.builder()
        .id(101L)
        .title("금융 강의")
        .tagList(List.of(LectureTag.builder().tag(tag).build()))
        .build();

    hanaItem = HanaItem.builder()
        .id(201L)
        .tag(tag)
        .itemTitle("3·6·9 정기예금")
        .itemContent("3개월마다 중도해지 혜택 제공")
        .hanaUrl("https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419598_115126.jsp")
        .build();
  }

  @Test
  @DisplayName("강의 ID 기반 상품 추천 - 성공")
  void getItemsByLectureId_Success() {
    // Given
    Long lectureId = 101L;
    when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
    when(hanaItemRepository.findAllByTagIds(List.of(tag.getId()))).thenReturn(List.of(hanaItem));

    // When
    List<HanaItemResponse> response = hanaItemService.getItemsByLectureId(lectureId);

    // Then
    assertNotNull(response);
    assertEquals(1, response.size());
    assertEquals("3·6·9 정기예금", response.get(0).getItemTitle());
    assertEquals("https://www.kebhana.com/cont/mall/mall08/mall0801/mall080101/1419598_115126.jsp",
        response.get(0).getHanaUrl());
  }
  
  @Test
  @DisplayName("강의 ID 기반 상품 추천 - 태그가 없을 때 예외 발생")
  void getItemsByLectureId_NoTagsFound() {
    // Given
    Lecture lectureWithoutTags = Lecture.builder()
        .id(102L)
        .title("태그 없는 강의")
        .tagList(List.of()) // 태그 없음
        .build();

    when(lectureRepository.findById(lectureWithoutTags.getId())).thenReturn(
        Optional.of(lectureWithoutTags));

    // When & Then
    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> hanaItemService.getItemsByLectureId(lectureWithoutTags.getId()));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals(HanaItemResponseExceptionEnum.TAGS_NOT_FOUND.getMessage(),
        exception.getReason());
  }

  @Test
  @DisplayName("강의 ID 기반 상품 추천 - 해당 태그의 상품이 없을 때 예외 발생")
  void getItemsByLectureId_NoProductsFound() {
    // Given
    Long lectureId = 101L;
    when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
    when(hanaItemRepository.findAllByTagIds(List.of(tag.getId()))).thenReturn(List.of());

    // When & Then
    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> hanaItemService.getItemsByLectureId(lectureId));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals(HanaItemResponseExceptionEnum.PRODUCTS_NOT_FOUND.getMessage(),
        exception.getReason());
  }

}
