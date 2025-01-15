package com.hanahakdangserver.lecture.service;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.classroom.utils.SnowFlakeGenerator;
import com.hanahakdangserver.lecture.dto.LectureRequest;
import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.CategoryRepository;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LectureService {

  private final LectureRepository lectureRepository;
  private final CategoryRepository categoryRepository;
  private final ClassroomRepository classroomRepository;

  private final SnowFlakeGenerator snowFlakeGenerator; // snowflake 생성기

  /**
   * 강의 객체와 강의실 객체를 생성한 후 DB에 저장합니다.
   *
   * @param lectureRequest 강의 생성 Request JSON
   */
  @Transactional
  public void registerNewLecture(MultipartFile imgaeFile, LectureRequest lectureRequest) {

    Long uniqueId = snowFlakeGenerator.nextId(); // 강의실에 대해 전역적으로 고유한 Id 생성

    Classroom classroom = classroomRepository.save(
        Classroom.builder().id(uniqueId).build()
    ); // 연관된 강의실 생성 -> DB에 저장

    Category category = categoryRepository.findByName(lectureRequest.getCategory().getDescription()).orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));

//    TODO: 썸네일 이미지 S3에 업로드 처리 로직 추가 필요

    lectureRepository.save(
        Lecture.builder()
              .classroom(classroom)
              .category(category)
              .title(lectureRequest.getTitle())
              .startTime(lectureRequest.getStart_time())
              .duration(lectureRequest.getDuration())
              .price(lectureRequest.getPrice())
              .maxParticipants(lectureRequest.getMax_participants())
              .description(lectureRequest.getDescription())
              .tagList(lectureRequest.getTags())
            .build()
    );
  }
}
