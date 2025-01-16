package com.hanahakdangserver.user.mapper;

import com.hanahakdangserver.user.dto.RandomCareerInfoDTO;
import com.hanahakdangserver.user.entity.CareerInfo;

public class CareerInfoMapper {

  public static CareerInfo toEntity(RandomCareerInfoDTO randomCareerInfoDTO, String code) {
    return CareerInfo.builder()
        .code(code)
        .startDate(randomCareerInfoDTO.getStartDate())
        .branchName(randomCareerInfoDTO.getBranchName())
        .position(randomCareerInfoDTO.getPosition())
        .build();
  }

}
