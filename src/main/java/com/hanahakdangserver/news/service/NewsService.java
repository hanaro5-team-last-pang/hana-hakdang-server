package com.hanahakdangserver.news.service;

import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.news.entity.News;
import com.hanahakdangserver.news.repository.NewsRepository;
import com.hanahakdangserver.news.dto.NewsResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  private static final int PAGE_SIZE = 6;
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


  public NewsResponse getAllNews(int page) {
    Pageable pageable = PageRequest.of(page, PAGE_SIZE);
    Page<News> newsPage = newsRepository.findAll(pageable);

    List<News> newsList = newsPage.getContent();
    if (newsList.isEmpty()) {
      throw NO_NEWS_FOUND.createResponseStatusException();
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    List<NewsResponse.NewsItem> newsItems = newsList.stream()
        .map(news -> NewsResponse.NewsItem.builder()
            .id(news.getId())
            .title(news.getTitle())
            .content(news.getContent())
            .newsUrl(news.getNewsUrl())
            .newsThumbnailUrl(news.getNewsThumbnailUrl())
            .createdAt(news.getCreatedAt().format(formatter))  // LocalDateTime → String 변환
            .build())
        .collect(Collectors.toList());

    return NewsResponse.builder()
        .totalCount(newsPage.getTotalElements())
        .newsList(newsItems)
        .build();
  }
}
