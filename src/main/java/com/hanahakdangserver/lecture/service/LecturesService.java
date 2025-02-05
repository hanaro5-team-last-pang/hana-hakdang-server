package com.hanahakdangserver.lecture.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.server.ResponseStatusException;

import com.hanahakdangserver.lecture.dto.LectureCategoriesDetailDTO;
import com.hanahakdangserver.lecture.dto.LectureCategoriesResponse;
import com.hanahakdangserver.lecture.dto.LectureDetailDTO;
import com.hanahakdangserver.lecture.dto.LecturesResponse;
import com.hanahakdangserver.lecture.dto.MentorLectureDetailDTO;
import com.hanahakdangserver.lecture.dto.MentorLecturesFilterDTO;
import com.hanahakdangserver.lecture.dto.MentorLecturesResponse;
import com.hanahakdangserver.lecture.enrollment.repository.EnrollmentRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.enums.AccessRole;
import com.hanahakdangserver.lecture.enums.LectureCategory;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.product.entity.Tag;
import com.hanahakdangserver.product.repository.TagRepository;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.LECTURE_NOT_FOUND;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LecturesService {

  private final Integer PAGE_SIZE = 6;

  private final Integer MENTOR_PAGE_SIZE = 20;

  private final LectureRepository lectureRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final UserRepository userRepository;
  private final TagRepository tagRepository;

  @Value("${classroom.interval-to-open-lecture}")
  private long intervalToOpenLecture;

  /**
   * 수강신청 가능한 전체 강의 목록 반환
   *
   * @param pageNum 페이지네이션을 위한 페이지 번호
   * @return 전체 강의 목록을 담은 LecturesResponse
   */
  public LecturesResponse getTotalLecturesList(Integer pageNum) {

    PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE);
    Page<Lecture> lectures = lectureRepository.searchAllPossibleLectures(pageRequest);

    List<LectureDetailDTO> lectureDetails = lectures.getContent().stream().map(
        lecture -> convertLectureToDetailDTO(lecture, false, null, null)
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
        lecture -> convertLectureToDetailDTO(lecture, false, null, null)
    ).collect(Collectors.toList());

    return LecturesResponse.builder()
        .totalCount(lectures.getTotalElements())
        .lectureList(lectureDetails)
        .build();
  }

  /**
   * 특정 강의의 모든 정보를 조회하여 반환
   *
   * @param lectureId 상세조회 하고자 하는 강의 Id
   * @return 강의의 모든 정보를 담은 LectureDetailDTO
   */
  public LectureDetailDTO getLectureDetail(Long lectureId, AccessRole role, Long userId) {

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    LocalDateTime now = LocalDateTime.now();

    // 강의 시작시간이 현재 시간보다 같거나 작으면 강의의 isFull을 true(모집마감)으로 변경
    if (lecture.getStartTime().isBefore(now) || lecture.getStartTime().isEqual(now)) {
      lecture.updateIsFull(true);
    }

    return convertLectureToDetailDTO(lecture, true, role, userId);
  }

  /**
   * 특정 멘토가 등록한 모든 강의 목록을 반환
   *
   * @param pageNum  페이지네이션을 위한 페이지 번호
   * @param mentorId 멘토의 userId
   * @return 멘토의 모든 강의를 ableToStart, NotStarted, Done으로 분류하여 MentorLecturesResponse
   */
  public MentorLecturesResponse getMentorLecturesList(Integer pageNum, Long mentorId) {

    PageRequest pageRequest = PageRequest.of(pageNum, MENTOR_PAGE_SIZE,
        Sort.by("startTime").descending());

    Page<Lecture> lectures = lectureRepository.searchAllLecturesOfMentor(pageRequest, mentorId);

    MentorLecturesFilterDTO mentorFilter = filterMentorLectures(lectures.getContent());

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
   * @param role     상세 조회하는 유저가 비로그인, 멘토, 멘티인지 여부, NOT_LOGIN / MENTOR / METEE; 상세 조회가 아닐 경우 null
   * @return LectureDetailDTO
   */
  private LectureDetailDTO convertLectureToDetailDTO(Lecture lecture, Boolean isDetail,
      AccessRole role, Long userId) {

    Integer currParticipants = calculateCurrentParticipants(lecture);
    String description = isDetail ? lecture.getDescription() : null;
    List<String> tags = isDetail ? fetchAndGetTags(lecture) : null;
    String enrollStatus = checkEnrollStatus(userId, lecture.getId(), role);

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
        .enrollStatus(enrollStatus)
        .build();
  }

  private Integer calculateCurrentParticipants(Lecture lecture) {
    if (lecture.getIsFull()) {
      return lecture.getMaxParticipants();
    }
    return (int) lecture.getEnrollments().stream()
        .filter(enrollment -> !enrollment.getIsCanceled())
        .count();
  }

  private List<String> fetchAndGetTags(Lecture lecture) {
    return lecture.getTagList().stream()
        .map(tag -> tagRepository.findById(tag.getTag().getId())
            .map(Tag::getTagName)
            .orElse(""))
        .collect(Collectors.toList());
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

  private String checkEnrollStatus(Long userId, Long lectureId, AccessRole role)
      throws ResponseStatusException {
    if (role == null) { // 강의 상세 조회가 아닌 경우 role이 null로 들어옴
      return null;
    }

    return switch (role) {
      case MENTOR, NOT_LOGIN -> role.getStatus();
      case MENTEE -> enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
          .map(e -> "ENROLLED")
          .orElse("NOT_ENROLLED");
    };
  }

  /**
   * 시작 가능, 예정, 완료 세 가지 상태로 분류된 멘토의 강의 목록을 DTO로 구성
   *
   * @param lectures 강의 목록
   */
  private MentorLecturesFilterDTO filterMentorLectures(List<Lecture> lectures) {
    List<MentorLectureDetailDTO> ableToStart = new ArrayList<>();
    List<MentorLectureDetailDTO> notStarted = new ArrayList<>();
    List<MentorLectureDetailDTO> done = new ArrayList<>();

    for (Lecture lecture : lectures) {
      MentorLectureDetailDTO mentorLectureDetail = convertMentorLectureToDetailDTO(lecture);
      categorizeLecture(mentorLectureDetail, ableToStart, notStarted, done);
    }

    return MentorLecturesFilterDTO.builder()
        .ableToStart(ableToStart)
        .notStarted(notStarted)
        .done(done)
        .build();
  }

  /**
   * 멘토가 등록한 강의 목록을 시작 가능, 예정, 완료 세 가지 상태로 분류
   *
   * @param lectureDetail 강의 정보
   * @param ableToStart   시작 가능한 강의를 담는 리스트
   * @param notStarted    예정된 강의를 담는 리스트
   * @param done          완료된 강의를 담는 리스트
   */
  private void categorizeLecture(MentorLectureDetailDTO lectureDetail,
      List<MentorLectureDetailDTO> ableToStart,
      List<MentorLectureDetailDTO> notStarted,
      List<MentorLectureDetailDTO> done) {

    LocalDateTime now = LocalDateTime.now();

    if (lectureDetail.getAbleToStart()) {
      ableToStart.add(lectureDetail);
    } else if (!lectureDetail.getIsCanceled()
        && now.isBefore(lectureDetail.getStartTime().plusMinutes(intervalToOpenLecture))) {
      notStarted.add(lectureDetail);
    } else {
      done.add(lectureDetail);
    }
  }

  /**
   * startTime과 isDone을 통해 강의 시작가능, 강의 예정, 완료된 강의 필터링
   *
   * @param lecture Lecture 객체
   * @return Lecture 객체를 MentorLectureDetailDTO로 변환
   */
  private MentorLectureDetailDTO convertMentorLectureToDetailDTO(Lecture lecture) {

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startTime = lecture.getStartTime();

    Boolean ableToStart = determineAbleToStart(now, startTime);

    // 강의 시작 가능시간을 넘겼지만 isCanceled가 false인 경우 자동 취소 강의로 처리
    if (shouldCancelLecture(now, lecture)) {
      lecture.updateIsCanceled(true);
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

  private Boolean determineAbleToStart(LocalDateTime now, LocalDateTime startTime) {
    if (now.isBefore(startTime.minusMinutes(intervalToOpenLecture))) {
      return false;
    } else if (now.isBefore(startTime.plusMinutes(intervalToOpenLecture))) {
      return true;
    }
    return false;
  }

  private boolean shouldCancelLecture(LocalDateTime now, Lecture lecture) {
    LocalDateTime startTime = lecture.getStartTime();
    return now.isAfter(startTime.plusMinutes(intervalToOpenLecture))
        && !lecture.getIsCanceled()
        && !lecture.getIsDone();
  }

  /**
   * 강의 카테고리 전체 목록을 조회하여 반환
   *
   * @return LectureCategoriesResponse
   */
  public LectureCategoriesResponse getCategories() {
    List<LectureCategoriesDetailDTO> categories = Arrays.stream(
            LectureCategory.values())
        .map(category -> new LectureCategoriesDetailDTO(category.name(), category.getDescription()))
        .collect(Collectors.toList());

    return LectureCategoriesResponse.builder()
        .categories(categories)
        .build();
  }
}
