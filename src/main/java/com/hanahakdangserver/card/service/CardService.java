package com.hanahakdangserver.card.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.entity.Card;
import com.hanahakdangserver.card.mapper.CardMapper;
import com.hanahakdangserver.card.repository.CardRepository;
import static com.hanahakdangserver.card.enums.CardResponseExceptionEnum.CARD_NOT_FOUND;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

  private final CardRepository cardRepository;

  public ProfileCardResponse getProfileCard(Long userId) {
    Card card = cardRepository.findByMentorId(userId)
        .orElseThrow(() -> CARD_NOT_FOUND.createResponseStatusException());
    log.debug("received cardDetailInfo : {}", card.getDetailInfo());
    return CardMapper.toDTO(card);
  }

}
