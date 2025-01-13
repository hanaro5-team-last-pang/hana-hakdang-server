package com.hanahakdangserver.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
