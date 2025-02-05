package com.hanahakdangserver.lecture.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.lecture.dto.LectureCategoriesCountDTO;
import com.hanahakdangserver.lecture.dto.LectureCategoriesResponse;
import com.hanahakdangserver.lecture.dto.LectureDetailDTO;
import com.hanahakdangserver.lecture.dto.LecturesResponse;
import com.hanahakdangserver.lecture.dto.MentorLecturesResponse;
import com.hanahakdangserver.lecture.enums.AccessRole;
import com.hanahakdangserver.lecture.enums.LectureCategory;
import com.hanahakdangserver.lecture.service.LectureCountService;
import com.hanahakdangserver.lecture.service.LecturesService;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.GET_CATEGORY_COUNT_SUCCESS;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.GET_CATEGORY_LIST_SUCCESS;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.GET_LECTURE_DETAIL_SUCCESS;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.GET_MENTOR_LECTURES_SUCCESS;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.GET_TOTAL_CATEGORY_LIST_SUCCESS;
import static com.hanahakdangserver.lecture.enums.LectureResponseSuccessEnum.GET_TOTAL_LIST_SUCCESS;

@Tag(name = "강의 조회", description = "강의 조회 API 목록")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LecturesController {

  private final LecturesService lecturesService;
  private final LectureCountService lectureCountService;

  @Operation(summary = "전체 강의 목록 조회", description = "아직 시작되지 않은 강의의 전체 목록을 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "전체 강의 목록 조회 성공")
  })
  @GetMapping
  public ResponseEntity<ResponseDTO<LecturesResponse>> getTotalLecturesList(
      @RequestParam(value = "page", defaultValue = "0") Integer pageNum) {

    LecturesResponse lecturesResponse = lecturesService.getTotalLecturesList(pageNum);

    return GET_TOTAL_LIST_SUCCESS.createResponseEntity(lecturesResponse);
  }

  @Operation(summary = "카테고리별 강의 목록 조회", description = "아직 시작되지 않은 강의의 카테고리별 목록을 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리별 강의 목록 조회 성공")
  })
  @GetMapping("/category")
  public ResponseEntity<ResponseDTO<LecturesResponse>> getCategoryLecturesList(
      @RequestParam(value = "name") List<String> categories,
      @RequestParam(value = "page", defaultValue = "0") Integer pageNum
  ) {

    List<LectureCategory> categoryEnums = categories.stream()
        .map(LectureCategory::parsing)
        .collect(Collectors.toList());

    LecturesResponse lecturesResponse = lecturesService.getCategoryLecturesList(categoryEnums,
        pageNum);

    return GET_CATEGORY_LIST_SUCCESS.createResponseEntity(lecturesResponse);
  }

  @Operation(summary = "특정 강의 상세 조회", description = "특정 강의에 대한 상세 정보를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "특정 강의 상세 조회에 성공했습니다."),
      @ApiResponse(responseCode = "404", description = "해당 강의가 존재하지 않습니다..")
  })
  @GetMapping("/{lectureId}")
  public ResponseEntity<ResponseDTO<LectureDetailDTO>> getLectureDetail(
      @PathVariable(value = "lectureId") Long lectureId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    AccessRole accessRole = (userDetails == null)
        ? AccessRole.NOT_LOGIN
        : userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_MENTOR"))
            ? AccessRole.MENTOR
            : AccessRole.MENTEE;

    Long userId = (userDetails != null) ? userDetails.getId() : null;
    LectureDetailDTO result = lecturesService.getLectureDetail(lectureId, accessRole, userId);

    return GET_LECTURE_DETAIL_SUCCESS.createResponseEntity(result);
  }

  @Operation(summary = "등록한 강의 목록 조회", description = "멘토가 자신이 등록한 강의 목록을 조회한다.")
  @ApiResponse(responseCode = "200", description = "등록한 강의 목록 조회에 성공했습니다.")
  @PreAuthorize("isAuthenticated() and hasRole('MENTOR')")
  @GetMapping("/queue/mentor")
  public ResponseEntity<ResponseDTO<MentorLecturesResponse>> getMentorLecturesList(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam(value = "page", defaultValue = "0") Integer pageNum) {

    MentorLecturesResponse result = lecturesService.getMentorLecturesList(pageNum,
        userDetails.getId());

    return GET_MENTOR_LECTURES_SUCCESS.createResponseEntity(result);
  }


  @Operation(summary = "카테고리 전체 목록 조회", description = "강의 카테고리 목록을 조회할 수 있다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리 전체 목록 조회 성공")
  })
  @GetMapping("/categories")
  public ResponseEntity<ResponseDTO<LectureCategoriesResponse>> getCategories() {
    LectureCategoriesResponse response = lecturesService.getCategories();

    return GET_TOTAL_CATEGORY_LIST_SUCCESS.createResponseEntity(response);
  }


  @Operation(summary = "특정 5개 카테고리별 강의 개수 조회", description = "금융 상품, 디지털 교육, 종합 자산 관리, 투자, 하나금융연구소 카테고리만 조회")
  @ApiResponse(responseCode = "200", description = "특정 5개 카테고리별 강의 개수 조회 성공")
  @GetMapping("/count")
  public ResponseEntity<ResponseDTO<List<LectureCategoriesCountDTO>>> getCategoryCounts() {
    List<LectureCategoriesCountDTO> response = lectureCountService.getCategoryCounts();
    return GET_CATEGORY_COUNT_SUCCESS.createResponseEntity(response);
  }
}
