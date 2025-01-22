package com.hanahakdangserver.card.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.card.dto.ProfileCardRequest;
import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.service.CardService;
import com.hanahakdangserver.common.ResponseDTO;
import static com.hanahakdangserver.card.enums.CardResponseSuccessEnum.GET_PROFILE_CARD_SUCCESS;
import static com.hanahakdangserver.card.enums.CardResponseSuccessEnum.UPDATE_PROFILE_CARD_SUCCESS;

@Log4j2
@Tag(name = "명함", description = "명함 관련 API 목록")
@RestController
@RequiredArgsConstructor
public class CardController {

  private final CardService cardService;

  @Operation(summary = "명함 조회", description = "유저는 멘토의 명함 조회를 시도")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "명함 조회를 성공했습니다."),
      @ApiResponse(responseCode = "400", description = "명함이 존재하지 않습니다.")
  })
  @GetMapping("/profile-card/{userId}")
  public ResponseEntity<ResponseDTO<ProfileCardResponse>> getProfileCard(
      @PathVariable Long userId) {
    ProfileCardResponse cardResponse = cardService.get(userId);
    log.debug("result : {}",
        GET_PROFILE_CARD_SUCCESS.createResponseEntity(cardResponse));
    return GET_PROFILE_CARD_SUCCESS.createResponseEntity(cardResponse);
  }

  @Operation(summary = "내 명함 조회", description = "멘토는 본인의 명함 조회를 시도")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "명함 조회를 성공했습니다."),
      @ApiResponse(responseCode = "400", description = "명함이 존재하지 않습니다.")
  })
  @PreAuthorize("isAuthenticated() and hasRole('MENTOR')")
  @GetMapping("/profile-card/me")
  public ResponseEntity<ResponseDTO<ProfileCardResponse>> getMyProfileCard(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    ProfileCardResponse cardResponse = cardService.get(userDetails.getId());
    log.debug("result : {}",
        GET_PROFILE_CARD_SUCCESS.createResponseEntity(cardResponse));
    return GET_PROFILE_CARD_SUCCESS.createResponseEntity(cardResponse);
  }

  @Operation(summary = "명함 수정", description = "멘토는 본인의 명함 수정을 시도")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "명함 수정을 완료했습니다."),
      @ApiResponse(responseCode = "400", description = "명함 수정에 실패했습니다.")
  })
  @PreAuthorize("isAuthenticated() and hasRole('MENTOR')")
  @PatchMapping("/profile-card")
  public ResponseEntity<ResponseDTO<Object>> updateProfileCard(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody ProfileCardRequest profileCardRequest) {

    cardService.update(userDetails.getId(), profileCardRequest);
    log.debug("result : {}",
        UPDATE_PROFILE_CARD_SUCCESS.createResponseEntity());
    return UPDATE_PROFILE_CARD_SUCCESS.createResponseEntity();
  }
}
