package com.hanahakdangserver.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.user.entity.CareerInfo;

public interface CareerInfoRepository extends JpaRepository<CareerInfo, Long> {

  /**
   * 멘토 id로 명함 조회
   *
   * @param mentorId
   */
//  @Query("SELECT c FROM CareerInfo c WHERE c.mentor.id = :mentorId")
//  Optional<CareerInfo> findByMentorId(@Param("mentorId") Long mentorId);

}
