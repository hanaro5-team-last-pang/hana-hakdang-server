package com.hanahakdangserver.classroom.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.classroom.dto.ClassroomEnterResponse;
import com.hanahakdangserver.classroom.dto.ClassroomStartResponse;
import com.hanahakdangserver.classroom.service.ClassroomService;
import com.hanahakdangserver.common.ResponseDTO;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseSuccessEnum.CLASSROOM_ENTERED;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseSuccessEnum.CLASSROOM_STARTED;

@Tag(name = "강의실", description = "강의실 API")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/classrooms")
public class ClassroomController {

  private final ClassroomService classroomService;

  @Operation(summary = "강의 시작", description = "멘토가 강의를 시작합니다. 이후에 멘토들이 강의실에 입장할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "강의를 시작합니다."),
      @ApiResponse(responseCode = "404", description = "강의 정보를 찾을 수 없습니다", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)),
      }),
      @ApiResponse(responseCode = "400", description = "강의가 취소됐습니다.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)),
      }),
      @ApiResponse(responseCode = "400", description = "강의실을 열 수 있는 시간이 아닙니다.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)),
      }),
  })
  @PostMapping("/{classroomId}/start")
  @PreAuthorize("hasRole('MENTOR')")
  public ResponseEntity<ResponseDTO<ClassroomStartResponse>> start(@PathVariable Long classroomId) {
    log.info("Start classroom with id {}", classroomId);
    ClassroomStartResponse startResponse = classroomService.startClassroom(classroomId);
    return CLASSROOM_STARTED.createResponseEntity(startResponse);
  }

  @Operation(summary = "강의실 입장", description = "멘티들이 강의실에 입장합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "강의실에 입장합니다."),
      @ApiResponse(responseCode = "404", description = "강의 정보를 찾을 수 없습니다", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)),
      }),
      @ApiResponse(responseCode = "400", description = "사용할 수 없는 강의실입니다.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)),
      }),
  })
  @PostMapping("/{classroomId}/enter")
  @PreAuthorize("hasRole('MENTEE')")
  public ResponseEntity<ResponseDTO<ClassroomEnterResponse>> enter(@PathVariable Long classroomId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    log.info("Enter classroom with id {}", classroomId);
    ClassroomEnterResponse enterResponse = classroomService.enterClassroom(classroomId,
        userDetails.getId());
    return CLASSROOM_ENTERED.createResponseEntity(enterResponse);
  }
}
