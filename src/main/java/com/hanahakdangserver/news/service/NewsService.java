package com.hanahakdangserver.news.service;

import com.hanahakdangserver.news.entity.News;
import com.hanahakdangserver.news.enums.NewsExceptionEnum;
import com.hanahakdangserver.news.repository.NewsRepository;
import com.hanahakdangserver.news.dto.NewsResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

  private final NewsRepository newsRepository;
  private final WebClient webClient;

  private List<Map<String, String>> fetchNewsFromPython() {
    try {
      return webClient.get()
          .uri("/news")
          .retrieve()
          .bodyToMono(List.class)
          .block();
    } catch (Exception e) {
      throw NewsExceptionEnum.NEWS_FETCH_FAILED.createResponseStatusException();
    }
  }

  @Transactional
  public void saveNewsFromPython() {
    List<Map<String, String>> articles = fetchNewsFromPython();
    if (articles == null || articles.isEmpty()) {
      throw NewsExceptionEnum.NEWS_FETCH_FAILED.createResponseStatusException();
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
    List<News> newsList = new ArrayList<>();
    for (Map<String, String> article : articles) {
      try {
        LocalDateTime createdAt = LocalDateTime.parse(article.get("date"), formatter);
        News news = News.builder()
            .title(article.get("title"))
            .content(article.get("content"))
            .newsUrl(article.get("newsUrl"))
            .newsThumbnailUrl(article.get("newsThumbnailUrl"))
            .createdAt(createdAt)
            .build();
        newsList.add(news);
      } catch (DateTimeParseException e) {
        throw NewsExceptionEnum.INVALID_DATE_FORMAT.createResponseStatusException();
      }
    }

    try {
      newsRepository.saveAll(newsList);
    } catch (Exception e) {
      throw NewsExceptionEnum.NEWS_SAVE_FAILED.createResponseStatusException();
    }

  }

  public List<NewsResponse> getAllNews() {
    List<News> newsList = newsRepository.findAll();
    if (newsList.isEmpty()) {
      throw NewsExceptionEnum.NO_NEWS_FOUND.createResponseStatusException();
    }

    return newsList.stream()
        .map(news -> NewsResponse.builder()
            .id(news.getId())
            .title(news.getTitle())
            .content(news.getContent())
            .newsUrl(news.getNewsUrl())
            .newsThumbnailUrl(news.getNewsThumbnailUrl())
            .createdAt(news.getCreatedAt().toString())
            .build())
        .collect(Collectors.toList());
  }
}
