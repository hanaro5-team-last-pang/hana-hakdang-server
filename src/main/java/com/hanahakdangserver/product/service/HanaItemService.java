package com.hanahakdangserver.product.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.product.dto.HanaItemResponse;
import com.hanahakdangserver.product.entity.HanaItem;
import com.hanahakdangserver.product.repository.HanaItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        .orElseThrow(() -> new EntityNotFoundException("강의를 조회하지 못 했습니다"));
    List<Integer> tagIds = lecture.getTagList(); // 강의 테이블에서 태그 리스트 가져오기

    List<HanaItem> items = hanaItemRepository.findAllByTagIds(tagIds);

    return items.stream()
        .map(item -> HanaItemResponse.builder()
            .id(item.getId())
            .itemTitle(item.getItemTitle())
            .itemContent(item.getItemContent())
            .hanaUrl(item.getHanaUrl())
            .lectureId(lectureId)
            .build())
        .collect(Collectors.toList());
  }


}
