package com.hanahakdangserver.lecture.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {

  // 리스트 -> 문자열 (데이터베이스에 저장될 때 호출)
  @Override
  public String convertToDatabaseColumn(List<Integer> attribute) {
    return attribute != null
        ? attribute.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(","))
        : null;
  }

  // 문자열 -> 리스트 (엔티티를 로드할 때 호출)
  @Override
  public List<Integer> convertToEntityAttribute(String dbData) {
    return dbData != null && !dbData.isEmpty()
        ? Arrays.stream(dbData.split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toList())
        : null;
  }
}
