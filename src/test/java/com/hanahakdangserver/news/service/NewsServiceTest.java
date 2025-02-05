package com.hanahakdangserver.news.service;

import com.hanahakdangserver.news.dto.NewsResponse;
import com.hanahakdangserver.news.entity.News;
import com.hanahakdangserver.news.repository.NewsRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

  @Mock
  private NewsRepository newsRepository;

  @Mock
  private WebClient webClient;

  @Mock
  private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

  @Mock
  private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

  @Mock
  private WebClient.ResponseSpec responseSpec;

  @InjectMocks
  private NewsService newsService;

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy.MM.dd HH:mm");

  @Test
  @DisplayName("뉴스 데이터 가져오기")
  void fetchNewsFromPython() {
    // Given
    List<Map<String, String>> mockArticles = List.of(
        Map.of("title", "테스트 뉴스 1", "content", "내용 1", "newsUrl", "https://example.com/1",
            "newsThumbnailUrl", "https://example.com/image1.jpg", "date", "2025.02.03 17:53"),
        Map.of("title", "테스트 뉴스 2", "content", "내용 2", "newsUrl", "https://example.com/2",
            "newsThumbnailUrl", "https://example.com/image2.jpg", "date", "2025.02.02 14:20")
    );

    // WebClient Mock
    doReturn(requestHeadersUriSpec).when(webClient).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(Mono.just(mockArticles)).when(responseSpec).bodyToMono(List.class);

    // When & Then
    assertDoesNotThrow(() -> newsService.saveNewsFromPython());
  }


  @Test
  @DisplayName("뉴스 데이터 DB 저장")
  void saveCrawlingData() {
    // Given
    List<Map<String, String>> mockArticles = List.of(
        Map.of("title", "테스트 뉴스 1", "content", "내용 1", "newsUrl", "https://example.com/1",
            "newsThumbnailUrl", "https://example.com/image1.jpg", "date", "2025.02.03 17:53"),
        Map.of("title", "테스트 뉴스 2", "content", "내용 2", "newsUrl", "https://example.com/2",
            "newsThumbnailUrl", "https://example.com/image2.jpg", "date", "2025.02.02 14:20")
    );

    // WebClient Mock
    doReturn(requestHeadersUriSpec).when(webClient).get();
    doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
    doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    doReturn(Mono.just(mockArticles)).when(responseSpec).bodyToMono(List.class);

    doReturn(Collections.emptyList()).when(newsRepository).saveAll(any());

    // When & Then
    assertDoesNotThrow(() -> newsService.saveNewsFromPython());

    verify(newsRepository, times(1)).saveAll(any());
  }

  @Test
  @DisplayName("DB 저장된 뉴스 데이터 불러오기 (전체 개수 포함)")
  void getAllNewsFromDB() {
    // Given
    Pageable pageable = PageRequest.of(0, 6);
    List<News> mockNewsList = List.of(
        new News(1L, "테스트 뉴스 1", "내용 1", "https://example.com/1", "https://example.com/image1.jpg",
            LocalDateTime.parse("2025.02.03 17:53", FORMATTER)),
        new News(2L, "테스트 뉴스 2", "내용 2", "https://example.com/2", "https://example.com/image2.jpg",
            LocalDateTime.parse("2025.02.02 14:20", FORMATTER))
    );

    Page<News> mockPage = new PageImpl<>(mockNewsList, pageable, 10); // 전체 뉴스 개수: 10
    when(newsRepository.findAll(pageable)).thenReturn(mockPage);

    // When
    NewsResponse result = newsService.getAllNews(0);

    // Then
    assertNotNull(result);
    assertEquals(10, result.getTotalCount());  // 전체 뉴스 개수 확인
    assertEquals(2, result.getNewsList().size());  // 현재 페이지 뉴스 개수 확인

    // 첫 번째 뉴스 검증
    NewsResponse.NewsItem firstNews = result.getNewsList().get(0);
    assertEquals("테스트 뉴스 1", firstNews.getTitle());
    assertEquals("내용 1", firstNews.getContent());
    assertEquals("https://example.com/1", firstNews.getNewsUrl());
    assertEquals("https://example.com/image1.jpg", firstNews.getNewsThumbnailUrl());
    assertEquals("2025-02-03", firstNews.getCreatedAt());

    // 두 번째 뉴스 검증
    NewsResponse.NewsItem secondNews = result.getNewsList().get(1);
    assertEquals("테스트 뉴스 2", secondNews.getTitle());
    assertEquals("내용 2", secondNews.getContent());
    assertEquals("https://example.com/2", secondNews.getNewsUrl());
    assertEquals("https://example.com/image2.jpg", secondNews.getNewsThumbnailUrl());
    assertEquals("2025-02-02", secondNews.getCreatedAt());
  }
}
