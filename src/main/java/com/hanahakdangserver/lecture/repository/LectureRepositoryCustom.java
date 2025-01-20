package com.hanahakdangserver.lecture.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.enums.LectureCategory;

public interface LectureRepositoryCustom {

  /**
   * 아직 강의 시작 시간이 되지 않은 모든 강의 목록을 반환
   *
   * @param pageRequest 페이지네이션을 위한 Pageable 구현체
   * @return Lecture 엔티티에 대한 페이지네이션 조회 결과
   */
  Page<Lecture> searchAllPossibleLectures(PageRequest pageRequest);

  /**
   * 전달된 카테고리에 맞게 필터링된 강의 목록을 반환
   *
   * @param pageRequest  페이지네이션을 위한 Pageable 구현체
   * @param categoryList 필터링 해야하는 카테고리 목록
   * @return Lecture 엔티티에 대한 조회 결과
   */
  Page<Lecture> searchAllCategoryLectures(PageRequest pageRequest,
      List<LectureCategory> categoryList);
}
