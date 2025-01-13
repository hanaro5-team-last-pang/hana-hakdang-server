package com.hanahakdangserver.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.lecture.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
