package com.hanahakdangserver.news.service;

import com.hanahakdangserver.news.entity.News;
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

import static com.hanahakdangserver.news.enums.NewsResponseExceptionEnum.INVALID_DATE_FORMAT;
import static com.hanahakdangserver.news.enums.NewsResponseExceptionEnum.NEWS_FETCH_FAILED;
import static com.hanahakdangserver.news.enums.NewsResponseExceptionEnum.NEWS_SAVE_FAILED;
import static com.hanahakdangserver.news.enums.NewsResponseExceptionEnum.NO_NEWS_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

  private final NewsRepository newsRepository;
  private final WebClient webClient;

  private List<Map<String, String>> fetchNewsFromPython() {
    try {
      List<Map<String, String>> response = webClient.get()
          .uri("")
          .retrieve()
          .bodyToMono(List.class)
          .block();
      return response;
    } catch (Exception e) {
      throw NEWS_FETCH_FAILED.createResponseStatusException();
    }
  }

  @Transactional
  public void saveNewsFromPython() {
    List<Map<String, String>> articles = fetchNewsFromPython();
    if (articles == null || articles.isEmpty()) {
      throw NEWS_FETCH_FAILED.createResponseStatusException();
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
        throw INVALID_DATE_FORMAT.createResponseStatusException();
      }
    }

    try {
      newsRepository.saveAll(newsList);
    } catch (Exception e) {
      throw NEWS_SAVE_FAILED.createResponseStatusException();
    }

  }

  public List<NewsResponse> getAllNews() {
    List<News> newsList = newsRepository.findAll();
    if (newsList.isEmpty()) {
      throw NO_NEWS_FOUND.createResponseStatusException();
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