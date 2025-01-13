package com.hanahakdangserver.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
