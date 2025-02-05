package com.hanahakdangserver.lecture.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.projection.MentorIdOnly;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {

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

  /**
   * 강의실 ID와 멘토 ID를 기반으로 최신 강의를 가져옵니다.
   *
   * @param classroomId 강의실 ID
   * @return 강의를 찾을 수 없으면 {@code Optional.empty()}이 반환
   */
  @Query("SELECT l "
      + "FROM Lecture l "
      + "WHERE l.classroom.id = :classroomId "
      + "ORDER BY l.id DESC LIMIT 1")
  Optional<Lecture> findLatestLectureIdByClassroomId(Long classroomId);


  /**
   * 강의를 개설한 멘토 ID를 검색
   *
   * @param lectureId 강의 ID
   * @return 강의를 찾을 수 없으면 {@code Optional.empty()}를 반환
   */
  @Query("SELECT l.mentor.id as mentorId "
      + "FROM Lecture l "
      + "WHERE l.id = :lectureId")
  Optional<MentorIdOnly> findMentorIdById(Long lectureId);


  // 특정 카테고리들의 강의 개수 조회
  @Query("SELECT c.name, COUNT(l) FROM Lecture l " +
      "JOIN l.category c " +
      "WHERE c.name IN :categoryNames AND l.isDone = false " +
      "GROUP BY c.name")
  List<Object[]> findLectureCountsForSpecificCategories(
      @Param("categoryNames") List<String> categoryNames);
}

