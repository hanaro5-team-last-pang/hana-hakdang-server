package com.hanahakdangserver.lecture.repository;

import com.hanahakdangserver.lecture.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface LectureRepositoryCustom {

  /**
   * 아직 강의 시작 시간이 되지 않은 모든 강의 목록을 반환
   *
   * @param pageRequest 페이지네이션을 위한 Pageable 구현체
   * @return Lecture 엔티티에 대한 페이지네이션 조회 결과
   */
  Page<Lecture> searchAllPossibleLectures(PageRequest pageRequest);
}
