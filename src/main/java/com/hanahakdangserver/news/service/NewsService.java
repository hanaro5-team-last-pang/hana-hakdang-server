package com.hanahakdangserver.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hanahakdangserver.news.dto.NewsResponse;
import com.hanahakdangserver.news.entity.News;
import com.hanahakdangserver.news.repository.NewsRepository;


@EnableAsync
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

  private final NewsRepository newsRepository;
  private final WebClient webClient;

  // Flask에서 데이터 가져오기
  private List<Map<String, String>> fetchNewsFromPython() {
    return webClient.get().uri("/news")
        .retrieve()
        .bodyToMono(List.class)
        .block();
  }

  @Async
  @Transactional(readOnly = false)
  public void saveNewsFromPython() {
    List<Map<String, String>> articles = fetchNewsFromPython();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    List<News> newsList = new ArrayList<>();
    for (Map<String, String> article : articles) {
      LocalDateTime createdAt = LocalDateTime.parse(article.get("date"), formatter);
      News news = News.builder()
          .title(article.get("title"))
          .content(article.get("content"))
          .newsUrl(article.get("newsUrl"))
          .newsThumbnailUrl(article.get("newsThumbnailUrl"))
          .createdAt(createdAt)
          .build();
      newsList.add(news);
    }
    newsRepository.saveAll(newsList);
  }

  // 데이터베이스에서 모든 뉴스 조회
  @Transactional(readOnly = true)
  public List<NewsResponse> getAllNews() {
    return newsRepository.findAll().stream()
        .map(news -> NewsResponse.builder()
            .id(news.getId())
            .title(news.getTitle())
            .content(news.getContent())
            .newsUrl(news.getNewsUrl())
            .newsThumbnailUrl(news.getNewsThumbnailUrl())
            .createdAt(news.getCreatedAt().toString())
            .build())
        .toList();
  }
}
