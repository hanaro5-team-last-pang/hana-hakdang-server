package com.hanahakdangserver.lecture.enrollment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.lecture.enrollment.dto.LectureEnrollmentDTO;
import com.hanahakdangserver.lecture.enrollment.dto.MenteeEnrollmentDetailDTO;
import com.hanahakdangserver.lecture.enrollment.dto.MenteeEnrollmentResponse;
import com.hanahakdangserver.lecture.enrollment.repository.EnrollmentRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.user.repository.UserRepository;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MenteeEnrollmentService {

  private static final Integer PAGE_SIZE = 20;

  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;
  private final EnrollmentRepository enrollmentRepository;

  @Value("${classroom.interval-to-open-lecture}")
  private long intervalToOpenLecture;

  public MenteeEnrollmentResponse getEnrollmentHistoryList(Long menteeId,
      Integer pageNum) {
    return getEnrollmentResultList(menteeId, pageNum,
        enrollmentRepository::getPastLecturesByMenteeId, true);
  }

  public MenteeEnrollmentResponse getEnrollmentQueueList(Long menteeId,
      Integer pageNum) {
    return getEnrollmentResultList(menteeId, pageNum,
        enrollmentRepository::getUpcomingLecturesByMenteeId, false);
  }

  /**
   * 공통된 로직을 처리하는 메서드
   *
   * @param menteeId     멘티 ID
   * @param pageNum      페이지 번호
   * @param lecturesFunc 강의 조회 함수
   * @return MenteeEnrollmentResponse
   */
  private MenteeEnrollmentResponse getEnrollmentResultList(Long menteeId, Integer pageNum,
      LectureEnrollmentFetcher lecturesFunc, Boolean isHistory) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
    Page<LectureEnrollmentDTO> resultList = lecturesFunc.fetchResult(pageRequest, menteeId);

    List<MenteeEnrollmentDetailDTO> details = resultList.getContent().stream()
        .map(result -> mapToDetailDTO(result, isHistory))
        .collect(Collectors.toList());

    return MenteeEnrollmentResponse.builder()
        .totalCount(resultList.getTotalElements())
        .enrollmentList(details)
        .build();
  }

  /**
   * QueryDsl 조회 결과인 LectureEnrollmentDTO을 MenteeEnrollmentDetailDTO로 매핑
   *
   * @param dto Lecture, Enrollment 엔티티를 담은 객체
   * @return MenteeEnrollmentDetailDTO로 매핑된 결과
   */
  private MenteeEnrollmentDetailDTO mapToDetailDTO(LectureEnrollmentDTO dto, Boolean isHistory) {

    Lecture lecture = dto.getLecture();
    Boolean possibleToEnter =
        dto.getEnrollment().getIsCanceled() || lecture.getIsCanceled() || !lecture.getIsDone()
            ? false
            : determineEnterPossibility(lecture.getStartTime(), lecture.getEndTime());

    return MenteeEnrollmentDetailDTO.builder()
        .classroomId(
            isHistory ? null
                : lecture.getClassroom().getId().toString()) // 수강 내역 조회 시에는 classroomId 반환X
        .enrollmentId(
            isHistory ? null : dto.getEnrollment().getId()) // 수강 내역 조회 시에는 enrollmentId 반환X
        .lectureId(lecture.getId())
        .mentorId(lecture.getMentor().getId())
        .mentorName(lecture.getMentor().getName())
        .title(lecture.getTitle())
        .startTime(lecture.getStartTime())
        .endTime(lecture.getEndTime())
        .isEnrollmentCanceled(dto.getEnrollment().getIsCanceled())
        .isDone(lecture.getIsDone())
        .isLectureCanceled(lecture.getIsCanceled())
        .possibleToEnter(possibleToEnter)
        .build();
  }

  /**
   * 현재 시간이 강의 시작시간 15분 전보다 이후이면서 강의 종료시간 보다는 이전이면 강의실 입장 가능 -- Lecture의 isDone은 true라는 전제(멘토가 강의 시작
   * 버튼을 누른 이후)
   *
   * @param startTime 강의 시작시간
   * @param endTime   강의 종료시간
   * @return 강의실 입장 가능여부; false면 불가능
   */
  private Boolean determineEnterPossibility(LocalDateTime startTime, LocalDateTime endTime) {
    LocalDateTime now = LocalDateTime.now();
    return now.isAfter(startTime.minusMinutes(intervalToOpenLecture)) && now.isBefore(endTime);
  }

  /**
   * 함수형 인터페이스? -> 참고) https://bcp0109.tistory.com/313
   */
  @FunctionalInterface
  interface LectureEnrollmentFetcher {

    Page<LectureEnrollmentDTO> fetchResult(PageRequest pageRequest, Long menteeId);
  }
}
