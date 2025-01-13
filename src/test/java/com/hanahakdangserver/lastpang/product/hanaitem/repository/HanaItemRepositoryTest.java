package com.hanahakdangserver.lastpang.product.hanaitem.repository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.product.hanaitem.entity.HanaItem;
import com.hanahakdangserver.product.hanaitem.repository.HanaItemRepository;
import com.hanahakdangserver.product.tag.entity.Tag;
import com.hanahakdangserver.product.tag.repository.TagRepository;

@DataJpaTest
public class HanaItemRepositoryTest {

  @Autowired
  private HanaItemRepository hanaItemRepository;

  @Autowired
  private TagRepository tagRepository;

  private Tag tag;

  @BeforeEach
  void setUp() {
    tag = Tag.builder()
        .tagName("예적금")
        .build();
    tagRepository.save(tag);

    // HanaItem 생성 및 저장
    HanaItem hanaItem1 = HanaItem.builder()
        .tag(tag)
        .itemTitle("예금 상품 추천")
        .itemContent("예금 내용")
        .hanaUrl("https://www.naver.com/")
        .build();

    HanaItem hanaItem2 = HanaItem.builder()
        .tag(tag)
        .itemTitle("적금 상품 추천")
        .itemContent("적금 내용")
        .hanaUrl("https://www.naver.com/")
        .build();

    hanaItemRepository.save(hanaItem1);
    hanaItemRepository.save(hanaItem2);
  }

  @DisplayName("findAllByTagId 메서드 테스트")
  @Test
  void testFindAllByTagId() {
    Long tagId = tag.getId();

    // when
    List<HanaItem> items = hanaItemRepository.findAllByTagId(tagId);

    // then
    assertThat(items).isNotNull();
    assertThat(items.size()).isEqualTo(2);
    assertThat(items.get(0).getTag().getId()).isEqualTo(tagId);
    assertThat(items.get(1).getTag().getId()).isEqualTo(tagId);
  }
}
