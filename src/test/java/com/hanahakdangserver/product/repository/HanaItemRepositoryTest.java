package com.hanahakdangserver.product.repository;

import com.hanahakdangserver.product.entity.HanaItem;
import com.hanahakdangserver.product.entity.Tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class HanaItemRepositoryTest {

  @Autowired
  private HanaItemRepository hanaItemRepository;

  @Autowired
  private TagRepository tagRepository;

  private Tag tag1, tag2;

  //  @BeforeEach
  void setUp() {
    // 태그 생성 및 저장
    tag1 = Tag.builder()
        .tagName("정기예금")
        .build();
    tag2 = Tag.builder()
        .tagName("외화상품")
        .build();
    tagRepository.save(tag1);
    tagRepository.save(tag2);

    // HanaItem 생성 및 저장
    HanaItem item1 = HanaItem.builder()
        .tag(tag1)
        .itemTitle("Item1")
        .itemContent("Content1")
        .hanaUrl("https://example.com/item1")
        .build();
    HanaItem item2 = HanaItem.builder()
        .tag(tag2)
        .itemTitle("Item2")
        .itemContent("Content2")
        .hanaUrl("https://example.com/item2")
        .build();

    hanaItemRepository.save(item1);
    hanaItemRepository.save(item2);
  }

  @DisplayName("findAllByTagIds 메서드 테스트")
//  @Test
  void testFindAllByTagIds() {
    // Given
    List<Integer> tagIds = List.of(tag1.getId().intValue(), tag2.getId().intValue());

    // When
    List<HanaItem> items = hanaItemRepository.findAllByTagIds(tagIds);

    // Then
    assertThat(items).isNotNull();
    assertThat(items.size()).isEqualTo(2);
    assertThat(items.get(0).getTag().getId()).isIn(tagIds.stream().map(Long::valueOf).toList());
    assertThat(items.get(1).getTag().getId()).isIn(tagIds.stream().map(Long::valueOf).toList());
  }
}
