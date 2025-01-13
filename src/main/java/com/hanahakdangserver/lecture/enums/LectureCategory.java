package com.hanahakdangserver.lecture.enums;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
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
}
