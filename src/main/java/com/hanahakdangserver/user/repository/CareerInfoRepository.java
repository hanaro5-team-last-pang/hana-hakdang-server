package com.hanahakdangserver.user.repository;

import com.hanahakdangserver.user.entity.CareerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerInfoRepository extends JpaRepository<CareerInfo, Long> {

  /**
   * 멘토 id로 명함 조회
   *
   * @param mentorId
   */
//  @Query("SELECT c FROM CareerInfo c WHERE c.mentor.id = :mentorId")
//  Optional<CareerInfo> findByMentorId(@Param("mentorId") Long mentorId);

}
