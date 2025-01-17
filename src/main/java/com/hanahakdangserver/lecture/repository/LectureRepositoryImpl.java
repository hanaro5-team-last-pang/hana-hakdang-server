package com.hanahakdangserver.lecture.repository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hanahakdangserver.enrollment.entity.QEnrollment;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.QCategory;
import com.hanahakdangserver.lecture.entity.QLecture;
import com.hanahakdangserver.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final Clock clock;

  private final QLecture lecture = QLecture.lecture;
  private final QEnrollment enrollment = QEnrollment.enrollment;
  private final QUser user = QUser.user;
  private final QCategory category = QCategory.category;

  @Override
  public Page<Lecture> searchAllPossibleLectures(PageRequest pageRequest) {

    LocalDateTime now = LocalDateTime.now(clock);

    List<Lecture> resultList = queryFactory
        .selectDistinct(lecture)
        .leftJoin(lecture.enrollments, enrollment)
        .on(enrollment.isCanceled.isFalse())
        .fetchJoin()
        .where(
            lecture.startTime.goe(now),
            lecture.isCanceled.isFalse()
        )
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .fetch();

    // 동일 조건으로 페이지네이션 없이 총 개수 계산
    Long totalCount = Optional.ofNullable(
        queryFactory
            .select(lecture.count())
            .from(lecture)
            .where(lecture.startTime.goe(now))
            .fetchOne()
    ).orElse(0L); // 결과가 null일 경우 기본값 0 반환

    return new PageImpl<>(resultList, pageRequest, totalCount);
  }

}
