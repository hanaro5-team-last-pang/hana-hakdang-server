package com.hanahakdangserver.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.lecture.enums.LectureCategory;
import com.hanahakdangserver.lecture.entity.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

//  // 제목으로 강의 검색
//  @Query("SELECT l FROM Lecture l WHERE l.title LIKE %:title%")
//  List<Lecture> searchByTitle(@Param("title") String title);
//
//  // 특정 카테고리에 따른 강의 조회
//  @Query("SELECT l FROM Lecture l WHERE l.category = :category AND l.isCanceled = false ")
//  List<Lecture> findByCategory(@Param(("category")) LectureCategory category);
//
//  // 특정 카테고리의 강의 수 조회
//  @Query("SELECT COUNT(l) FROM Lecture l WHERE l.category = :category")
//  Long countByCategory(@Param("category") LectureCategory category);
//
//  // 무료 강의 조회 (가격이 0인 경우)
//  @Query("SELECT l FROM Lecture l WHERE l.price = 0 AND l.isCanceled = false")
//  List<Lecture> findFreeLectures();
//
//  // 유료 강의 조회 (가격이 0보다 큰 경우)
//  @Query("SELECT l FROM Lecture l WHERE l.price > 0 AND l.isCanceled = false")
//  List<Lecture> findPaidLectures();
//
//  // 강의 취소 여부에 따라 강의 조회
//  @Query("SELECT l FROM Lecture l WHERE l.isCanceled = :isCanceled")
//  List<Lecture> findByIsCanceled(@Param("isCanceled") boolean isCanceled);

}
