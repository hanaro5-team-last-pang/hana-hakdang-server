package com.hanahakdangserver.lecture.enrollment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.hanahakdangserver.lecture.enrollment.dto.LectureEnrollmentDTO;
import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;
import com.hanahakdangserver.lecture.enrollment.entity.QEnrollment;
import com.hanahakdangserver.lecture.entity.QLecture;

@Log4j2
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private final QEnrollment enrollment = QEnrollment.enrollment;
  private final QLecture lecture = QLecture.lecture;

  @Override
  public Boolean isAlreadyEnrolled(Long userId, Long lectureId) {

    List<Enrollment> resultList = queryFactory
        .selectDistinct(enrollment)
        .from(enrollment)
        .where(
            enrollment.user.id.eq(userId),
            enrollment.lecture.id.eq(lectureId),
            enrollment.isCanceled.isFalse()
        )
        .fetch();

    return !resultList.isEmpty();
  }

  @Override
  public List<Enrollment> getExistingEnrollment(Long userId, Long lectureId) {

    List<Enrollment> resultList = queryFactory
        .selectDistinct(enrollment)
        .from(enrollment)
        .where(
            enrollment.user.id.eq(userId),
            enrollment.lecture.id.eq(lectureId)
        )
        .fetch();

    return resultList;
  }

  @Override
  public Page<LectureEnrollmentDTO> getPastLecturesByMenteeId(PageRequest pageRequest,
      Long userId) {

    // 취소되지 않은 수강신청이면서 취소되지 않은 강의
    BooleanExpression baseCondition = enrollment.isCanceled.isFalse()
        .and(lecture.isCanceled.isFalse())
        .and(lecture.endTime.before(LocalDateTime.now()));

    // 취소된 수강신청
    BooleanExpression canceledEnrollmentCondition = enrollment.isCanceled.isTrue();

    // 취소된 강의
    BooleanExpression canceledLectureCondition = enrollment.lecture.isCanceled.isTrue();

    BooleanExpression finalCondition = canceledLectureCondition
        .or(canceledEnrollmentCondition)
        .or(baseCondition)
        .and(enrollment.user.id.eq(userId));

    List<LectureEnrollmentDTO> resultList = queryFactory
        .select(Projections.constructor(
            LectureEnrollmentDTO.class,
            lecture,    // Lecture 엔티티
            enrollment  // Enrollment 엔티티
        ))
        .from(enrollment)
        .join(enrollment.lecture, lecture)
        .where(finalCondition)
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .fetch();

    // 동일 조건으로 페이지네이션 없이 총 개수 계산
    Long totalCount = Optional.ofNullable(
        queryFactory
            .select(lecture.countDistinct())
            .from(enrollment)
            .join(enrollment.lecture, lecture)
            .where(finalCondition)
            .fetchOne()
    ).orElse(0L);

    log.info("[멘티의 수강 내역 조회] resultList 길이: {}, 페이지네이션 없이 totalCount 크기: {}", resultList.size(),
        totalCount);

    return new PageImpl<>(resultList, pageRequest, totalCount);
  }
}
