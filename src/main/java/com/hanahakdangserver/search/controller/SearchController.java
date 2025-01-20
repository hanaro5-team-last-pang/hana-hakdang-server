package com.hanahakdangserver.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.search.dto.SearchResponse;
import com.hanahakdangserver.search.service.SearchService;
import static com.hanahakdangserver.search.enums.SearchResponseSuccessEnum.GET_SEARCH_RESULT_SUCCESS;

@Tag(name = "검색", description = "검색 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;

  @Operation(summary = "키워드 검색 결과 조회", description = "정보에 키워드를 포함하는 강의 목록 검색 결과를 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "검색 결과 조회에 성공했습니다.")
  })
  @GetMapping
  public ResponseEntity<ResponseDTO<SearchResponse>> getSearchResult(
      @RequestParam(value = "keyword") String keyword,
      @RequestParam(value = "page", defaultValue = "0") Integer pageNum) {

    SearchResponse searchResponse = searchService.getSearchResult(keyword, pageNum);

    return GET_SEARCH_RESULT_SUCCESS.createResponseEntity(searchResponse);
  }
}
