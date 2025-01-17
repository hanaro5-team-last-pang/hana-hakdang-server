package com.hanahakdangserver.lecture.repository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hanahakdangserver.enrollment.entity.QEnrollment;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.QLecture;
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

  /**
   * Lecture 테이블에서 현재 시간보다 크거나 같으면서(미래) isCanceled가 false이고(강의 취소 X) 연관된 Enrollment 중 수강 신청 취소 되지
   * 않은(isCanceled가 false) 데이터를 조회하는 쿼리
   *
   * @param pageRequest 페이지네이션을 위한 Pageable 구현체
   * @return Page 객체 (쿼리 결과, (페이지 시작, 페이지 크기), 페이지네이션과 관계 없는 전체 결과 수)
   */

  @Override
  public Page<Lecture> searchAllPossibleLectures(PageRequest pageRequest) {

    LocalDateTime now = LocalDateTime.now(clock);

    List<Lecture> resultList = queryFactory
        .selectDistinct(lecture)
        .from(lecture)
        .leftJoin(lecture.enrollments, enrollment)
        .where(
            lecture.startTime.goe(now),
            lecture.isCanceled.isFalse(),
            enrollment.isNull() // 연관된 enrollment가 없을 경우 포함
                .or(enrollment.isCanceled.isFalse())
            // enrollment가 있을 경우 조건
        )
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .fetch();

    // 동일 조건으로 페이지네이션 없이 총 개수 계산
    Long totalCount = Optional.ofNullable(
        queryFactory
            .select(lecture.countDistinct())
            .from(lecture)
            .leftJoin(lecture.enrollments, enrollment)
            .where(
                lecture.startTime.goe(now),
                lecture.isCanceled.isFalse(),
                enrollment.isNull()
                    .or(enrollment.isCanceled.isFalse())
            )
            .fetchOne()
    ).orElse(0L); // 결과가 null일 경우 기본값 0 반환

    return new PageImpl<>(resultList, pageRequest, totalCount);
  }

}
