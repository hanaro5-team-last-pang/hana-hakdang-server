package com.hanahakdangserver.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmailAndIsActiveTrue(String email);

  Long findIdByEmail(String email);

}
