package com.hanahakdangserver.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.lecture.entity.LectureTag;

@Repository
public interface LectureTagRepository extends JpaRepository<LectureTag, Long> {

}
