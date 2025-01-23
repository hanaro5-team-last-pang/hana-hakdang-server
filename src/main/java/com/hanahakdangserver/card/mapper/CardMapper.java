package com.hanahakdangserver.card.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;

import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.entity.Card;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;

@Getter
@Builder
public class CardMapper {

  public static Map<String, String> toDefaultSimpleInfo(CareerInfo careerInfo) {
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
        .simple_info(card.getSimpleInfo() != null ? toListInfo(card.getSimpleInfo()) : null)
        .detail_info(card.getDetailInfo() != null ? toListInfo(card.getDetailInfo()) : null)
        .build();
  }

  public static List<Map<String, String>> toListInfo(Map<String, String> info) {
    return info.entrySet().stream()
        .map(entry -> createInfo(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private static Map<String, String> createInfo(String key, String value) {
    Map<String, String> map = new HashMap<>();
    map.put("key", key);
    map.put("value", value);
    return map;
  }

  public static Map<String, String> toMapInfo(List<Map<String, String>> listInfo) {
    Map<String, String> info = new HashMap<>();

    for (Map<String, String> map : listInfo) {
      String key = map.get("key");
      String value = map.get("value");
      info.put(key, value);
    }
    return info;
  }

}
