package com.hanahakdangserver.lecture.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hanahakdangserver.lecture.dto.LectureDetailDTO;
import com.hanahakdangserver.lecture.dto.LecturesResponse;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LecturesService {

  private final Integer PAGE_SIZE = 6;

  private final LectureRepository lectureRepository;

  public LecturesResponse getTotalLecturesList(Integer pageNum) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
    Page<Lecture> lectures = lectureRepository.searchAllPossibleLectures(pageRequest);

    List<LectureDetailDTO> lectureDetails = lectures.getContent().stream().map(
        lecture -> {

          Integer currParticipants;

          if (lecture.getIsFull()) {
            currParticipants = lecture.getMaxParticipants();
          } else {
            // 연관된 enrollment의 개수를 계산
            currParticipants = lecture.getEnrollments() != null
                ? lecture.getEnrollments().size()
                : 0;
          }

          return LectureDetailDTO.builder()
                .lecture_id(lecture.getId())
  //              .mentor_name(lecture.getMentor().getName())
                .category(lecture.getCategory().getName())
                .title(lecture.getTitle())
                .start_date(lecture.getStartTime())
                .duration(lecture.getDuration())
                .curr_participants(currParticipants)
                .max_participants(lecture.getMaxParticipants())
                .is_full(lecture.getIsFull())
                .thumbnail_img_url(lecture.getThumbnailUrl())
              .build();
        }
    ).collect(Collectors.toList());

    return LecturesResponse.builder().total_count(lectures.getTotalElements()).lecture_list(lectureDetails).build();
  }
}
