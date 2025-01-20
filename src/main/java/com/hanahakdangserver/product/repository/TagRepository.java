package com.hanahakdangserver.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.product.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

  // tagName에 특정 문자열이 포함된 id 리스트 반환
  List<Tag> findByTagNameContaining(String tagName);
}
