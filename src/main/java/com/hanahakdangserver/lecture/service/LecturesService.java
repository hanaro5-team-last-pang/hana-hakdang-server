package com.hanahakdangserver.lecture.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;

import com.hanahakdangserver.lecture.dto.LectureDetailDTO;
import com.hanahakdangserver.lecture.dto.LecturesResponse;
import com.hanahakdangserver.lecture.dto.MentorLectureDetailDTO;
import com.hanahakdangserver.lecture.dto.MentorLecturesFilterDTO;
import com.hanahakdangserver.lecture.dto.MentorLecturesResponse;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.enums.LectureCategory;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.product.entity.Tag;
import com.hanahakdangserver.product.repository.TagRepository;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.LECTURE_NOT_FOUND;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LecturesService {

  private final Integer PAGE_SIZE = 6;

  private final Integer MENTOR_PAGE_SIZE = 20;

  private final LectureRepository lectureRepository;
  private final TagRepository tagRepository;

  @Value("${classroom.interval-to-open-lecture}")
  private long intervalToOpenLecture;

  public LecturesResponse getTotalLecturesList(Integer pageNum) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
    Page<Lecture> lectures = lectureRepository.searchAllPossibleLectures(pageRequest);

    List<LectureDetailDTO> lectureDetails = lectures.getContent().stream().map(
        lecture -> convertLectureToDetailDTO(lecture, false)
    ).collect(Collectors.toList());

    return LecturesResponse.builder()
        .totalCount(lectures.getTotalElements())
        .lectureList(lectureDetails)
        .build();
  }

  public LecturesResponse getCategoryLecturesList(List<LectureCategory> categoryList,
      Integer pageNum) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
    Page<Lecture> lectures = lectureRepository.searchAllCategoryLectures(pageRequest, categoryList);

    List<LectureDetailDTO> lectureDetails = lectures.getContent().stream().map(
        lecture -> convertLectureToDetailDTO(lecture, false)
    ).collect(Collectors.toList());

    return LecturesResponse.builder()
        .totalCount(lectures.getTotalElements())
        .lectureList(lectureDetails)
        .build();
  }

  public LectureDetailDTO getLectureDetail(Long lectureId) {

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    LocalDateTime now = LocalDateTime.now();

    // 강의 시작시간이 현재 시간보다 같거나 작으면 강의의 isFull을 true(모집마감)으로 변경
    if (lecture.getStartTime().isBefore(now) || lecture.getStartTime().isEqual(now)) {
      lecture.updateIsFull(true);
    }

    return convertLectureToDetailDTO(lecture, true);
  }

  public MentorLecturesResponse getMentorLecturesList(Integer pageNum, Long mentorId) {

    PageRequest pageRequest = PageRequest.of(pageNum, MENTOR_PAGE_SIZE,
        Sort.by("startTime").descending());

    Page<Lecture> lectures = lectureRepository.searchAllLecturesOfMentor(pageRequest, mentorId);

    List<MentorLectureDetailDTO> ableToStart = new ArrayList<>();
    List<MentorLectureDetailDTO> notStarted = new ArrayList<>();
    List<MentorLectureDetailDTO> done = new ArrayList<>();

    for (Lecture lecture : lectures.getContent()) {

      MentorLectureDetailDTO mentorLectureDetail = convertMentorLectureToDetailDTO(lecture);

      if (mentorLectureDetail.getAbleToStart()) {
        ableToStart.add(mentorLectureDetail);
      } else if (!mentorLectureDetail.getIsCanceled() && now().isBefore(
          mentorLectureDetail.getStartTime().plusMinutes(intervalToOpenLecture))) {
        notStarted.add(mentorLectureDetail);
      } else {
        done.add(mentorLectureDetail);
      }
    }

    MentorLecturesFilterDTO mentorFilter = MentorLecturesFilterDTO.builder()
        .ableToStart(ableToStart).notStarted(notStarted).done(done).build();

    return MentorLecturesResponse.builder()
        .totalCount(lectures.getTotalElements())
        .lectureList(mentorFilter)
        .build();
  }

  /**
   * Lecture 엔티티를 LectureDetailDTO로 변환
   *
   * @param lecture  DTO로 변환이 필요한 Lecture 엔티티
   * @param isDetail DTO에 description, 태그 목록이 필요한지 여부, false면 builder에 null이 들어감
   * @return LectureDetailDTO
   */
  private LectureDetailDTO convertLectureToDetailDTO(Lecture lecture, Boolean isDetail) {
    Integer currParticipants;
    String description;

    if (lecture.getIsFull()) {
      currParticipants = lecture.getMaxParticipants();
    } else {
      // 연관된 enrollment의 개수를 계산
      currParticipants = lecture.getEnrollments() != null
          ? (int) lecture.getEnrollments().stream()
          .filter(enrollment -> !enrollment.getIsCanceled()) // isCanceled가 false인 경우로 필터링
          .count()
          : 0;
    }

    List<String> tags;
    // isDetail이 true면 description, 태그 목록 반환 필요
    if (isDetail) {
      description = lecture.getDescription();
      tags = lecture.getTagList().stream().map(
          tag -> {
            Tag specificTag = tagRepository.findById(tag.getTag().getId()).orElse(null);
            if (specificTag != null) {
              return specificTag.getTagName();
            }
            return "";
          }
      ).collect(Collectors.toList());
    } else {
      description = null;
      tags = null;
    }

    return LectureDetailDTO.builder()
        .lectureId(lecture.getId())
        .mentorName(lecture.getMentor().getName())
        .category(lecture.getCategory().getName())
        .tags(tags)
        .title(lecture.getTitle())
        .description(description)
        .startTime(lecture.getStartTime())
        .endTime(lecture.getEndTime())
        .duration(calculateDuration(lecture.getStartTime(), lecture.getEndTime()))
        .currParticipants(currParticipants)
        .maxParticipants(lecture.getMaxParticipants())
        .isFull(lecture.getIsFull())
        .thumbnailImgUrl(lecture.getThumbnailUrl())
        .build();
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

  /**
   * startTime과 isDone을 통해 강의 시작가능, 강의 예정, 완료된 강의 필터링
   *
   * @param lecture Lecture 객체
   * @return Lecture 객체를 MentorLectureDetailDTO로 변환
   */
  private MentorLectureDetailDTO convertMentorLectureToDetailDTO(Lecture lecture) {

    LocalDateTime now = LocalDateTime.now();

    Boolean ableToStart;

    if (now.isBefore(lecture.getStartTime().minusMinutes(intervalToOpenLecture))) {
      // 현재 시간이 강의 시작시간 15분 전보다 이전인 경우 -> 예정된 강의지만 강의 시작은 불가능
      ableToStart = false;
    } else if (now.isBefore(lecture.getStartTime().plusMinutes(intervalToOpenLecture))) {
      // 현재 시간이 강의 시작시간 15분 전, 15분 후 사이인 경우 -> 강의 시작 가능
      ableToStart = true;
    } else if (now.isAfter(lecture.getStartTime().plusMinutes(intervalToOpenLecture))) {
      // 현재 시간이 강의 시작 가능 시간을 넘겨 버린 경우 -> 완료된 강의로 판단
      ableToStart = false;

      // 강의 시작 가능시간을 넘겼지만 isCanceled가 false인 경우 자동 취소 강의로 처리
      if (!lecture.getIsCanceled() && !lecture.getIsDone()) {
        lecture.updateIsCanceled(true);
      }
    } else {
      ableToStart = false;
    }

    return MentorLectureDetailDTO.builder()
        .classroomId(lecture.getClassroom().getId())
        .lectureId(lecture.getId())
        .title(lecture.getTitle())
        .isCanceled(lecture.getIsCanceled())
        .isDone(lecture.getIsDone())
        .ableToStart(ableToStart)
        .startTime(lecture.getStartTime())
        .endTime(lecture.getEndTime())
        .build();
  }
}
