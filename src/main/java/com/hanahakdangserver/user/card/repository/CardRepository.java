package com.hanahakdangserver.user.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.user.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
