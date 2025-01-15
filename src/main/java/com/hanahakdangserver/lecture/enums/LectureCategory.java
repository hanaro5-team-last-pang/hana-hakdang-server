package com.hanahakdangserver.lecture.enums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum LectureCategory {

  FINANCIAL_PRODUCTS("금융 상품"),
  DIGITAL_EDUCATION("디지털 교육"),
  INHERITANCE("상속"),
  TRUST("신탁"),
  RETIREMENT_DESIGN("은퇴 설계"),
  COMPREHENSIVE_ASSET_MANAGEMENT("종합 자산 관리"),
  STOCK_INVESTMENT("주식 투자");

  private final String description;

  /**
   * 입력되는 inputValue값과 일치하는 category가 있다면 LectureCategory 반환, 없다면 null 반환
   * DTO에서 검증을 위해 추가한 역직렬화
   *
   * @param inputValue LectureCategory로 예상되는 문자열
   * @return LectureCategory || null
   */
  @JsonCreator
  public static LectureCategory parsing(String inputValue) {
    return Stream.of(LectureCategory.values())
        .filter(category -> category.name().equals(inputValue))
        .findFirst()
        .orElse(null);
  }
}
