package com.hanahakdangserver.lecture.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.lecture.category.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
