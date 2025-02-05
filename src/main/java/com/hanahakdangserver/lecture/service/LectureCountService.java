package com.hanahakdangserver.lecture.service;

import com.hanahakdangserver.lecture.dto.LectureCategoriesCountDTO;
import com.hanahakdangserver.lecture.enums.LectureCategory;
import com.hanahakdangserver.lecture.repository.LectureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LectureCountService {

  private final LectureRepository lectureRepository;

  /**
   * @return 카운트 DTO 반환
   */
  public List<LectureCategoriesCountDTO> getCategoryCounts() {
    List<LectureCategory> selectedCategories = Arrays.asList(
        LectureCategory.FINANCIAL_PRODUCTS,
        LectureCategory.DIGITAL_EDUCATION,
        LectureCategory.COMPREHENSIVE_ASSET_MANAGEMENT,
        LectureCategory.INVESTMENT,
        LectureCategory.HANA_IF
    );

    //  한글 이름을 리스트로 변환
    List<String> categoryNames = selectedCategories.stream()
        .map(LectureCategory::getDescription)
        .collect(Collectors.toList());

    List<Object[]> results = lectureRepository.findLectureCountsForSpecificCategories(
        categoryNames);

    return selectedCategories.stream()
        .map(category -> new LectureCategoriesCountDTO(
            category.name(),
            category.getDescription(),
            results.stream()
                .filter(row -> row[0].equals(category.getDescription()))
                .map(row -> (Long) row[1])
                .findFirst()
                .orElse(0L)
        ))
        .collect(Collectors.toList());
  }
}


