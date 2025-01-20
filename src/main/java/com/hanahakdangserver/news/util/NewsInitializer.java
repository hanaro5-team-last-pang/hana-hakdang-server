package com.hanahakdangserver.news.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hanahakdangserver.news.service.NewsService;


@Component
public class NewsInitializer {

  private final NewsService newsService;

  public NewsInitializer(NewsService newsService) {
    this.newsService = newsService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initializeNewsData() {
    newsService.saveNewsFromPython(); // Fetch news from Python and save to DB
  }
}
