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
        .mentor_name(card.getMentor().getName())
        .short_introduction(card.getShortIntroduction())
        .simple_info(card.getSimpleInfo())
        .detail_info(card.getDetailInfo())
        .build();
  }

}
