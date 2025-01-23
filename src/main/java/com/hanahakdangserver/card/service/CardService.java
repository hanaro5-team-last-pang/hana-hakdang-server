package com.hanahakdangserver.card.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.card.dto.ProfileCardRequest;
import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.entity.Card;
import com.hanahakdangserver.card.mapper.CardMapper;
import com.hanahakdangserver.card.repository.CardRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.EMAIL_NOT_FOUND;
import static com.hanahakdangserver.card.enums.CardResponseExceptionEnum.CARD_NOT_FOUND;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.LECTURE_NOT_FOUND;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

  private final CardRepository cardRepository;
  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;

  //기본 명함
  @Transactional
  public void create(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> EMAIL_NOT_FOUND.createResponseStatusException());
    CareerInfo careerInfo = user.getCareerInfo();

    Map<String, String> toSimpleInfo = CardMapper.toSimpleInfo(careerInfo);
    Card defaultCard = CardMapper.toDefaultEntity(user, toSimpleInfo);

    cardRepository.save(defaultCard);
  }

  public ProfileCardResponse get(Long lectureId) {

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> LECTURE_NOT_FOUND.createResponseStatusException());
    Long mentorId = lecture.getMentor().getId();

    Card card = cardRepository.findByMentorId(mentorId)
        .orElseThrow(() -> CARD_NOT_FOUND.createResponseStatusException());

    log.debug("received cardDetailInfo : {}", card.getDetailInfo());

    return CardMapper.toDTO(card);

  }

  @Transactional
  public void update(Long userId, ProfileCardRequest cardRequest) {
    Card currentCard = cardRepository.findByMentorId(userId)
        .orElseThrow(() -> CARD_NOT_FOUND.createResponseStatusException());

    Card card = currentCard.update
        (cardRequest.getShortIntroduction(), cardRequest.getSimpleInfo(),
            cardRequest.getDetailInfo());

    cardRepository.save(card);

  }
}
