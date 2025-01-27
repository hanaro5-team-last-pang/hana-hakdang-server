package com.hanahakdangserver.review.projection;

import java.time.LocalDateTime;

public interface ReviewProjection {

  Double getAverageScore();

  Long getTotalCount();

  Integer getScore();

  Integer getCount();

  Long getId();

  Long getLectureId();

  Long getUserId();

  String getUserName();

  String getImageUrl();

  String getLectureTitle();

  String getContent();

  LocalDateTime getCreatedAt();
}
