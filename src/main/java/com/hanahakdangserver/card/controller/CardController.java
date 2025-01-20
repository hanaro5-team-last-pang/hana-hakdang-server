package com.hanahakdangserver.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.service.CardService;
import com.hanahakdangserver.common.ResponseDTO;
import static com.hanahakdangserver.card.enums.CardResponseSuccessEnum.GET_PROFILE_CARD_SUCCESS;

@Log4j2
@Tag(name = "명함 API", description = "명함 관련 API 목록")
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
  public ResponseEntity<ResponseDTO<Object>> getProfileCard(
      @PathVariable Long userId) {
    ProfileCardResponse cardResponse = cardService.getProfileCard(userId);
    log.debug("result : {}",
        GET_PROFILE_CARD_SUCCESS.createResponseEntity(cardResponse));
    return GET_PROFILE_CARD_SUCCESS.createResponseEntity(cardResponse);
  }

}
