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

  GLOBAL("글로벌"),
  FINANCIAL_PRODUCTS("금융 상품"),
  ECONOMIC_ISSUE("금융 이슈 정리"),
  DIGITAL_EDUCATION("디지털 교육"),
  REAL_ESTATE("부동산"),
  INHERITANCE("상속"),
  TRUST("신탁"),
  PENSION("연금"),
  RETIREMENT_DESIGN("은퇴 설계"),
  COMPREHENSIVE_ASSET_MANAGEMENT("종합 자산 관리"),
  FINANCIAL_MARKET_TREND("최신 금융시장 동향"),
  INVESTMENT("투자"),
  HANA_IF("하나금융연구소");

  private final String description;

  /**
   * 입력되는 inputValue값과 일치하는 category가 있다면 LectureCategory 반환, 없다면 null 반환 DTO에서 검증을 위해 추가한 역직렬화
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
