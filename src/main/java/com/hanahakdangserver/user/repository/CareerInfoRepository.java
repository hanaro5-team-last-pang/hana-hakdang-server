package com.hanahakdangserver.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanahakdangserver.user.entity.CareerInfo;

public interface CareerInfoRepository extends JpaRepository<CareerInfo, Long> {

  /**
   * 멘토 id로 명함 조회
   *
   * @param mentorId
   */
  @Query("SELECT c FROM CareerInfo c WHERE c.mentor.id = :mentorId")
  Optional<CareerInfo> findByMentorId(@Param("mentorId") Long mentorId);

}
