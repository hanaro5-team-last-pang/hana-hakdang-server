package com.hanahakdangserver.user.provider;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.hanahakdangserver.user.dto.RandomCareerInfoDTO;

public class CareerInfoProvider {

  @RequiredArgsConstructor
  @Getter
  public enum PositionType {
    STAFF("사원"),
    ASSOCIATE("주임"),
    SENIOR_ASSOCIATE("대리"),
    MANAGER("과장"),
    SENIOR_MANAGER("차장"),
    EXECUTIVE_MANAGER("부장");

    private final String description;

  }

  @Getter
  @RequiredArgsConstructor
  public enum BranchType {
    SEONG_SU_STATION("성수역점");

    private final String branchName;
  }


  private static PositionType getRandomPosition() {
    int randomNum = ThreadLocalRandom.current().nextInt(PositionType.values().length);
    return PositionType.values()[randomNum];
  }

  private static BranchType getRandomBranch() {
    int randomNum = ThreadLocalRandom.current().nextInt(BranchType.values().length);
    return BranchType.values()[randomNum];
  }

  private static LocalDate getRandomDate() {
    LocalDate now = LocalDate.now();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    int randomYear = random.nextInt(now.getYear());
    int randomMonth = random.nextInt(now.getMonthValue());
    int randomDay = random.nextInt(now.getDayOfMonth());
    return LocalDate.of(randomYear, randomMonth, randomDay);
  }

  public static RandomCareerInfoDTO getRandomCareerInfo() {
    return RandomCareerInfoDTO.builder()
        .branchName(getRandomBranch().getBranchName())
        .position(getRandomPosition().name())
        .startDate(getRandomDate())
        .build();
  }

}
