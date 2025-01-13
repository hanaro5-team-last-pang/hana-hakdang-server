package com.hanahakdangserver.product.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.product.tag.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
