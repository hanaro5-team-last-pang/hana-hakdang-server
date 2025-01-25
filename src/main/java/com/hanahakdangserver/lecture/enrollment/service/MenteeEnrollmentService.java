package com.hanahakdangserver.lecture.enrollment.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

  public MenteeEnrollmentResponse getEnrollmentHistoryList(Long menteeId,
      Integer pageNum) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
    Page<LectureEnrollmentDTO> resultList = enrollmentRepository.getPastLecturesByMenteeId(
        pageRequest,
        menteeId);

    List<MenteeEnrollmentDetailDTO> details = resultList.getContent().stream().map(
        result -> mapToDetailDTO(result, false)
    ).collect(Collectors.toList());

    return MenteeEnrollmentResponse.builder().totalCount(resultList.getTotalElements())
        .enrollmentList(details).build();
  }

  /**
   * QueryDsl 조회 결과인 LectureEnrollmentDTO을 MenteeEnrollmentDetailDTO로 매핑
   *
   * @param dto             Lecture, Enrollment 엔티티를 담은 객체
   * @param possibleToEnter 강의실 입장 가능여부; getEnrollmentHistoryList 반환값에서는 항상 false
   * @return MenteeEnrollmentDetailDTO로 매핑된 결과
   */
  private MenteeEnrollmentDetailDTO mapToDetailDTO(LectureEnrollmentDTO dto,
      Boolean possibleToEnter) {

    Lecture lecture = dto.getLecture();

    return MenteeEnrollmentDetailDTO.builder()
        .classroomId(lecture.getClassroom().getId())
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
}
