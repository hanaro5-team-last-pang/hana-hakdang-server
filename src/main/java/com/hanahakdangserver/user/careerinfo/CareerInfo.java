package com.hanahakdangserver.user.careerinfo;

import java.time.LocalDate;

import com.hanahakdangserver.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CareerInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(nullable = false)
  private User mentorId;

  @Column(nullable = false)
  private LocalDate startDt;

  @Column(nullable = false)
  private LocalDate endDt;

  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private String task;

  @Column(nullable = false, length = 2048)
  private String certifiacteUrl;
}
