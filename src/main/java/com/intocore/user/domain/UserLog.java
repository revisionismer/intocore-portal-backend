package com.intocore.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_log_tb")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // createdAt 자동 생성을 위해 필요
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1 관계 (유저 한 명이 여러 개의 접속 로그를 가짐)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String ip;

    @Column(nullable = false, length = 100)
    private String device;

    @Column(nullable = false, length = 50)
    private String country;

    @CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false, updatable = false)
 	private LocalDateTime createdDate;

    @Builder
    public UserLog(User user, String ip, String device, String country) {
        this.user = user;
        this.ip = ip;
        this.device = device;
        this.country = country;
    }
}