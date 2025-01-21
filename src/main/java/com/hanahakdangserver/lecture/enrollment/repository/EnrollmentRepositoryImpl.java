package com.hanahakdangserver.lecture.enrollment.repository;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;
import com.hanahakdangserver.lecture.enrollment.entity.QEnrollment;

@Log4j2
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private final QEnrollment enrollment = QEnrollment.enrollment;

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
}
