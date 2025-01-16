package com.hanahakdangserver.user.utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import com.hanahakdangserver.user.dto.RandomCareerInfoDTO;

@Log4j2
public class CareerInfoGenerator {

  @Getter
  @ToString
  @RequiredArgsConstructor
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
  @ToString
  @RequiredArgsConstructor
  public enum BranchType {
    SEONG_SU_STATION("성수역점");

    private final String branchName;
  }


  protected static PositionType getRandomPosition() {
    int randomNum = ThreadLocalRandom.current().nextInt(PositionType.values().length);
    return PositionType.values()[randomNum];
  }

  protected static BranchType getRandomBranch() {
    int randomNum = ThreadLocalRandom.current().nextInt(BranchType.values().length);
    return BranchType.values()[randomNum];
  }

  protected static LocalDate getRandomDate() {
    LocalDate now = LocalDate.now();
    LocalDate randomDate;
    for (; ; ) {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      int randomYear = random.nextInt(24) + 2000;
      int randomMonth = random.nextInt(11) + 1;
      int randomDay = random.nextInt(30) + 1;
      try {
        randomDate = LocalDate.of(randomYear, randomMonth, randomDay);
      } catch (DateTimeException dateTimeException) {
        log.error("Wrong date format");
        continue;
      }
      if (randomDate.isBefore(now)) {
        return randomDate;
      }
    }
  }

  public static RandomCareerInfoDTO getRandomCareerInfo() {
    return RandomCareerInfoDTO.builder()
        .branchName(getRandomBranch().getBranchName())
        .position(getRandomPosition().name())
        .startDate(getRandomDate())
        .build();
  }

}
