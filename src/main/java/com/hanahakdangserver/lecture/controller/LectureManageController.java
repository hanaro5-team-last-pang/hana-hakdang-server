package com.hanahakdangserver.lecture.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.lecture.dto.LectureRequest;
import com.hanahakdangserver.lecture.service.LectureManageService;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.CREATE_LECTURE_SUCCESS;

@Log4j2
@Tag(name = "멘토 강의", description = "멘토 강의 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureManageController {

  private final LectureManageService lectureManageService;

  /**
   * 강의 엔티티 및 강의실 엔티티 생성 엔드포인트 추가) 이미지 S3 업로드
   *
   * @param imageFile      이미지 파일
   * @param lectureRequest JSON
   * @return "강의 생성 성공" 메시지
   */

  @Operation(summary = "강의 등록", description = "멘토가 강의를 생성하고 등록을 시도한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "강의 등록에 성공했습니다."),
      @ApiResponse(responseCode = "404", description = "해당 카테고리가 존재하지 않습니다."),
      @ApiResponse(responseCode = "404", description = "해당 태그가 존재하지 않습니다."),
      @ApiResponse(responseCode = "404", description = "해당 유저가 존재하지 않습니다.")
  })
  @PreAuthorize("isAuthenticated() and hasRole('MENTOR')")
  @PostMapping("/register")
  public ResponseEntity<ResponseDTO<Void>> registerNewLecture(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
      @Valid @RequestPart(value = "data", required = false) LectureRequest lectureRequest
  ) throws IOException {

    lectureManageService.registerNewLecture(userDetails.getId(), imageFile, lectureRequest);

    return CREATE_LECTURE_SUCCESS.createResponseEntity(null);
  }
}
