package com.hanahakdangserver.search.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.search.dto.LectureResultDetailDTO;
import com.hanahakdangserver.search.dto.SearchResponse;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SearchService {

  private final Integer PAGE_SIZE = 6;

  private final LectureRepository lectureRepository;

  public SearchResponse getSearchResult(String keyword, Integer pageNum) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("startTime").ascending());
    Page<Lecture> searchResult = lectureRepository.searchWithKeyword(pageRequest, keyword);

    List<LectureResultDetailDTO> lectureResultDetails = searchResult.getContent().stream().map(
        result -> {
          Integer currParticipants;

          if (result.getIsFull()) {
            currParticipants = result.getMaxParticipants();
          } else {
            // 연관된 enrollment의 개수를 계산
            currParticipants = result.getEnrollments() != null
                ? result.getEnrollments().size()
                : 0;
          }

          return LectureResultDetailDTO.builder()
              .lectureId(result.getId())
              .mentorName(result.getMentor().getName())
              .category(result.getCategory().getName())
              .title(result.getTitle())
              .startTime(result.getStartTime())
              .endTime(result.getEndTime())
              .duration(calculateDuration(result.getStartTime(), result.getEndTime()))
              .currParticipants(currParticipants)
              .maxParticipants(result.getMaxParticipants())
              .isFull(result.getIsFull())
              .thumbnailImgUrl(result.getThumbnailUrl())
              .build();
        }
    ).collect(Collectors.toList());

    return SearchResponse.builder().totalCount(searchResult.getTotalElements())
        .lectureList(lectureResultDetails).build();
  }

  /**
   * 예상 종료시간과 시작 시간과의 차이를 통해 예상 진행 시간을 계산
   *
   * @param startTime 시작 시간
   * @param endTime   예상 종료시간
   * @return 시작 시간과 예상 종료시간의 차이를 hour 단위로 반환
   */
  private Integer calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
    Duration diff = Duration.between(startTime, endTime);
    return (int) Math.ceil(diff.toMinutes() / 60.0);
  }
}
