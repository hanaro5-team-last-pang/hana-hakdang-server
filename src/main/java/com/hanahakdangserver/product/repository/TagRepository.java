package com.hanahakdangserver.product.repository;

import com.hanahakdangserver.product.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
