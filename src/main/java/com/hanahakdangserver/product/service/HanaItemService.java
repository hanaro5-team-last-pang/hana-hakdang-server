package com.hanahakdangserver.product.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.LectureTag;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.product.dto.HanaItemResponse;
import com.hanahakdangserver.product.entity.HanaItem;
import com.hanahakdangserver.product.entity.Tag;
import com.hanahakdangserver.product.repository.HanaItemRepository;
import static com.hanahakdangserver.product.enums.HanaItemResponseExceptionEnum.LECTURE_NOT_FOUND;
import static com.hanahakdangserver.product.enums.HanaItemResponseExceptionEnum.TAGS_NOT_FOUND;
import static com.hanahakdangserver.product.enums.HanaItemResponseExceptionEnum.PRODUCTS_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HanaItemService {

  private final HanaItemRepository hanaItemRepository;
  private final LectureRepository lectureRepository;

  /**
   * 강의의 태그 리스트 기반으로 상품 조회
   *
   * @param lectureId 강의 아이디
   * @return 매핑된 상품 리스트
   */
  public List<HanaItemResponse> getItemsByLectureId(Long lectureId) {
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    List<LectureTag> tagList = lecture.getTagList(); // 강의 테이블에서 태그 리스트 가져오기
    if (tagList.isEmpty()) {
      throw TAGS_NOT_FOUND.createResponseStatusException();
    }

    List<Long> tagIds = tagList.stream().map(LectureTag::getTag).map(Tag::getId)
        .collect(Collectors.toList());

    List<HanaItem> items = hanaItemRepository.findAllByTagIds(tagIds);
    if (items.isEmpty()) {
      throw PRODUCTS_NOT_FOUND.createResponseStatusException();
    }

    return items.stream()
        .map(item -> HanaItemResponse.builder()
            .id(item.getId())
            .itemTitle(item.getItemTitle())
            .itemContent(item.getItemContent())
            .hanaUrl(item.getHanaUrl())
            .build())
        .collect(Collectors.toList());
  }
}