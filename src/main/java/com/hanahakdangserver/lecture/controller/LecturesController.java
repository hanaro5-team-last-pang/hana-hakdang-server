package com.hanahakdangserver.lecture.controller;

import com.hanahakdangserver.lecture.dto.LecturesResponse;
import com.hanahakdangserver.lecture.service.LecturesService;
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

@Tag(name = "강의", description = "강의 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LecturesController {

  private final LecturesService lecturesService;

  @Operation(summary = "전체 강의 목록 조회", description = "아직 시작되지 않은 전체 강의 목록을 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "전체 강의 목록 조회 성공")
  })
  @GetMapping
  public ResponseEntity<LecturesResponse> getTotalLecturesList(@RequestParam(value="page", defaultValue="0") Integer pageNum) {

    LecturesResponse lecturesResponse = lecturesService.getTotalLecturesList(pageNum);

    // TODO : 공통 응답 형식으로 변경 필요
    return ResponseEntity.ok(lecturesResponse);
  }
}
