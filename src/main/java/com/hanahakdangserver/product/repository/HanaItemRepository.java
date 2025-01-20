package com.hanahakdangserver.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.product.entity.HanaItem;

@Repository
public interface HanaItemRepository extends JpaRepository<HanaItem, Long> {

  /**
   * 태그 id로 HanaItem 리스트 조회
   *
   * @param tagIds
   * @return 해당 태그를 가진 HanaItem 리스트
   * @return 해당 태그를 가진 HanaItem 리스트
   */
  @Query("SELECT h FROM HanaItem h WHERE h.tag.id IN :tagIds")
  List<HanaItem> findAllByTagIds(@Param("tagIds") List<Long> tagIds);

}
