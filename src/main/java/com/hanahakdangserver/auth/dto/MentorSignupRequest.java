package com.hanahakdangserver.auth.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "멘토 회원 가입 요청")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MentorSignupRequest {

  @Schema(description = "사원 번호")
  @NotNull
  @NotBlank
  private String code;

  @Schema(description = "이메일", example = "user@example.com")
  @NotBlank
  @Email(message = "잘못된 이메일 형식입니다.")
  @NotNull
  private String email;

  @Schema(description = "이름", example = "김하나")
  @NotNull
  @NotBlank
  private String name;

  @Schema(description = "비밀번호")
  @NotNull
  @NotBlank
  private String password;

  @Schema(description = "비밀번호 확인")
  @NotNull
  @NotBlank
  private String confirmedPassword;


  @Schema(description = "생년월일")
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate birth;

}
