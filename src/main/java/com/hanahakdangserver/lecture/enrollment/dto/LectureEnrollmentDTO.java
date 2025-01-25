package com.hanahakdangserver.lecture.enrollment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;
import com.hanahakdangserver.lecture.entity.Lecture;

/**
 * QueryDsl 결과 반환시 Lecture 엔티티와 Enrollment 엔티티를 한꺼번에 반환하기 위한 DTO (내부적으로만 사용)
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class LectureEnrollmentDTO {

  private Lecture lecture;

  private Enrollment enrollment;
}
