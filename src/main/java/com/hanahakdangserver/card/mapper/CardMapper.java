package com.hanahakdangserver.card.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.entity.Card;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;

@Getter
@Builder
public class CardMapper {

  public static Map<String, String> toSimpleInfo(CareerInfo careerInfo) {
    Map<String, String> simpleInfo = new HashMap<>();

    simpleInfo.put("지점명", careerInfo.getBranchName());
    simpleInfo.put("직급", careerInfo.getPosition());
    simpleInfo.put("입행날짜", String.valueOf(careerInfo.getStartDate()));

    return simpleInfo;
  }

  //멘토 회원가입 시 기본으로 생성되는 명함
  public static Card toDefaultEntity(User mentor, Map<String, String> simpleInfo) {
    return Card.builder()
        .mentor(mentor)
        .shortIntroduction("안녕하세요!" + mentor.getName() + " 멘토 입니다.")
        .simpleInfo(simpleInfo)
        .build();
  }

  public static ProfileCardResponse toDTO(Card card) {
    return ProfileCardResponse.builder()
        .mentor_name(card.getMentor().getName())
        .short_introduction(card.getShortIntroduction())
        .simple_info(card.getSimpleInfo())
        .detail_info(card.getDetailInfo())
        .build();
  }

}
