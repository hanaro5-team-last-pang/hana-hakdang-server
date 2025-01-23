package com.hanahakdangserver.redis;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Getter
@AllArgsConstructor
public enum RedisExceptionEnum {
  ERROR_DURING_OPERATION(HttpStatus.INTERNAL_SERVER_ERROR, "작업 중 오류가 발생했습니다."),
  CANT_GET_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "value를 가져올 수 없습니다."),
  CANT_DELETE_BY_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "key를 통해 삭제할 수 없습니다."),
  CANT_CONVERT_INTO_JSON(HttpStatus.BAD_REQUEST, "json으로 변환할 수 없습니다."),
  CANT_CONVERT_INTO_DTO(HttpStatus.BAD_REQUEST, "객체로 변환할 수 없습니다."),
  CANT_DESERIALIZE_JSON(HttpStatus.BAD_REQUEST, "JSON 역직렬화 중 오류가 발생했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  public ResponseStatusException createResponseStatusException() {
    return new ResponseStatusException(this.httpStatus, this.message);
  }
}

