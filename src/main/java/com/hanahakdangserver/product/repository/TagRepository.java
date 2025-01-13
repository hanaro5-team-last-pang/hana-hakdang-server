package com.hanahakdangserver.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.product.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
