package com.hanahakdangserver.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.news.dto.NewsResponse;
import com.hanahakdangserver.news.service.NewsService;

import java.util.List;

import static com.hanahakdangserver.news.enums.NewsResponseSuccessEnum.CRAWLING_REQUESTED;
import static com.hanahakdangserver.news.enums.NewsResponseSuccessEnum.NEWS_FETCHED;

@Tag(name = "뉴스", description = "뉴스 API 목록")
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  @Operation(summary = "모든 뉴스 조회", description = "데이터베이스에 저장된 모든 뉴스를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "뉴스 조회 성공"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  @GetMapping
  public ResponseEntity<ResponseDTO<List<NewsResponse>>> getAllNews() {
    List<NewsResponse> newsResponses = newsService.getAllNews();
    return NEWS_FETCHED.createResponseEntity(newsResponses);
  }

  @Operation(summary = "뉴스 크롤링 요청", description = "파이썬 서버에서 최신 뉴스를 크롤링하여 저장합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "뉴스 크롤링 요청 성공"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  @PostMapping("/request-crawling")
  public ResponseEntity<ResponseDTO<String>> requestCrawling() {
    newsService.saveNewsFromPython();
    return CRAWLING_REQUESTED.createResponseEntity("크롤링 요청 성공");
  }
}
