package com.hanahakdangserver.user.careerinfo.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.hanahakdangserver.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class CareerInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(nullable = false)
  private User mentor;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private String task;

  @Column(nullable = false, length = 2048)
  private String certifiacteUrl;

  @Builder
  public CareerInfo(User mentor, LocalDate startDate, LocalDate endDate, String companyName,
      String task, String certifiacteUrl) {
    this.mentor = mentor;
    this.startDate = startDate;
    this.endDate = endDate;
    this.companyName = companyName;
    this.task = task;
    this.certifiacteUrl = certifiacteUrl;
  }
}
