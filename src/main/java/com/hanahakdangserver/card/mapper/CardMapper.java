package com.hanahakdangserver.card.mapper;

import lombok.Builder;
import lombok.Getter;

import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.entity.Card;

@Getter
@Builder
public class CardMapper {

  public static ProfileCardResponse toDTO(Card card) {
    return ProfileCardResponse.builder()
        .mentorName(card.getMentor().getName())
        .shortIntroduction(card.getShortIntroduction())
        .simpleInfo(card.getSimpleInfo())
        .detailInfo(card.getDetailInfo())
        .build();
  }

}
