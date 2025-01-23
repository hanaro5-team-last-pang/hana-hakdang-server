package com.hanahakdangserver.user.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.common.ResponseDTO;
import com.hanahakdangserver.user.dto.AccountRequest;
import com.hanahakdangserver.user.dto.UserInfoResponse;
import com.hanahakdangserver.user.service.UserService;
import static com.hanahakdangserver.user.enums.UserResponseSuccessEnum.FETCH_USER_SUCCESS;
import static com.hanahakdangserver.user.enums.UserResponseSuccessEnum.UPDATE_ACCOUNT_SUCCESS;

@Log4j2
@Tag(name = "유저 API", description = "유저 관련 api 목록")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  @Operation(summary = "계정 수정", description = "유저는 본인의 계정 수정을 시도한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "계정 수정을 성공했습니다."),
      @ApiResponse(responseCode = "400", description = "계정 수정에 실패했습니다.")
  })
  @PatchMapping("/account")
  public ResponseEntity<ResponseDTO<Object>> updateProfileCard
      (@RequestPart(value = "accountData", required = false) AccountRequest accountRequest,
          @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
          @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

    userService.updateAccount(userDetails.getId(), imageFile, accountRequest);

    return UPDATE_ACCOUNT_SUCCESS.createResponseEntity(null);

  }


  @Operation(summary = "유저 정보 조회", description = "현재 로그인 된 유저의 userId와 name을 반환합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "유저 정보를 성공적으로 반환했습니다."),
  })
  @GetMapping("/user-info")
  public ResponseEntity<ResponseDTO<UserInfoResponse>> getUserInfo(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UserInfoResponse userInfoResponse = userService.getUserInfo(userDetails.getId());
    return FETCH_USER_SUCCESS.createResponseEntity(userInfoResponse);
  }

}
