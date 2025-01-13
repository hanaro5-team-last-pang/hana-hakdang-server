package com.hanahakdangserver.product.hanaitem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanahakdangserver.product.hanaitem.entity.HanaItem;

public interface HanaItemRepository extends JpaRepository<HanaItem, Long> {

  /**
   * 태그 id로 HanaItem 리스트 조회
   *
   * @param tagId
   * @return 해당 태그를 가진 HanaItem 리스트
   */
  @Query("SELECT h FROM HanaItem h WHERE h.tagId.id = :tagId")
  List<HanaItem> findAllByTagId(@Param("tagId") Long tagId);

}
