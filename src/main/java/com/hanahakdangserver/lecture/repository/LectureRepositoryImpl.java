package com.hanahakdangserver.lecture.repository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.hanahakdangserver.enrollment.entity.QEnrollment;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.QLecture;
import com.hanahakdangserver.lecture.enums.LectureCategory;

@Log4j2
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

    log.info("[전체 강의 조회] resultList 길이: {}, 페이지네이션 없이 totalCount 크기: {}", resultList.size(),
        totalCount);

    return new PageImpl<>(resultList, pageRequest, totalCount);
  }

  /**
   * searchAllPossibleLectures() 메서드와 동일한 조건에서 카테고리 필터링 추가
   *
   * @param pageRequest  페이지네이션을 위한 Pageable 구현체
   * @param categoryList 필터링 해야하는 카테고리 목록
   * @return Page 객체 (쿼리 결과, (페이지 시작, 페이지 크기), 페이지네이션과 관계 없는 전체 결과 수)
   */
  @Override
  public Page<Lecture> searchAllCategoryLectures(PageRequest pageRequest,
      List<LectureCategory> categoryList) {

    LocalDateTime now = LocalDateTime.now(clock);

    // 카테고리 리스트에서 category name만 추출
    List<String> categoryNames = categoryList.stream()
        .map(LectureCategory::getDescription)
        .toList();

    List<Lecture> resultList = queryFactory
        .selectDistinct(lecture)
        .from(lecture)
        .leftJoin(lecture.enrollments, enrollment)
        .where(
            lecture.startTime.goe(now),
            lecture.isCanceled.isFalse(),
            enrollment.isNull().or(enrollment.isCanceled.isFalse()),
            lecture.category.name.in(categoryNames) // 주어진 카테고리에 해당하는 강의
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
                enrollment.isNull().or(enrollment.isCanceled.isFalse()),
                lecture.category.name.in(categoryNames)
            )
            .fetchOne()
    ).orElse(0L); // 결과가 null일 경우 기본값 0 반환

    log.info("[키워드별 강의 조회] resultList 길이: {}, 페이지네이션 없이 totalCount 크기: {}", resultList.size(),
        totalCount);

    return new PageImpl<>(resultList, pageRequest, totalCount);
  }

  /**
   * searchAllPossibleLectures() 메서드와 동일한 조건에서 키워드 필터링 추가
   *
   * @param pageRequest 페이지네이션을 위한 Pageable 구현체
   * @param keyword     검색어
   * @return Page 객체 (쿼리 결과, (페이지 시작, 페이지 크기), 페이지네이션과 관계 없는 전체 결과 수)
   */
  @Override
  public Page<Lecture> searchWithKeyword(PageRequest pageRequest, String keyword) {

    LocalDateTime now = LocalDateTime.now(clock);

    // keyword를 포함하는 LectureCategory 검색; 없으면 빈 리스트
    List<String> containedCategories = LectureCategory.getDescriptionContainsKeyword(keyword);
    log.info("keyword를 포함하는 카테고리 개수: {}", containedCategories.size());

    // 키워드 조건
    // 강의 제목, 강의 카테고리에 키워드가 포함되는지 여부
    // TODO : 멘토명도 추후 추가 필요
    BooleanExpression keywordCondition = lecture.title.containsIgnoreCase(keyword)
        .or(lecture.category.name.in(containedCategories));

    List<Lecture> resultList = queryFactory
        .selectDistinct(lecture)
        .from(lecture)
        .leftJoin(lecture.enrollments, enrollment)
        .where(
            lecture.startTime.goe(now),
            lecture.isCanceled.isFalse(),
            enrollment.isNull().or(enrollment.isCanceled.isFalse()),
            keywordCondition
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
                enrollment.isNull().or(enrollment.isCanceled.isFalse()),
                keywordCondition
            )
            .fetchOne()
    ).orElse(0L); // 결과가 null일 경우 기본값 0 반환

    log.info("[키워드 검색 강의 조회] resultList 길이: {}, 페이지네이션 없이 totalCount 크기: {}", resultList.size(),
        totalCount);

    return new PageImpl<>(resultList, pageRequest, totalCount);
  }
}
