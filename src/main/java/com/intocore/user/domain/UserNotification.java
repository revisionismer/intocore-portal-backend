package com.intocore.user.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // 1-3. JPA에서 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문에 추가(중요)
@Getter @Setter
@Table(name = "user_notification_tb")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	// 결재완료 알림 수신 여부
    @Column(nullable = false)
    private boolean isApprovalEnabled = false; // 기본값은 false로 설정

    // 공지사항 알림 수신 여부
    @Column(nullable = false)
    private boolean isNoticeEnabled = false;

    // User 엔티티와 1:1 관계 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true) // 유니크 제약조건 추가
    private User user;
    
    @CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = false)
 	private LocalDateTime createdDate; 
 	 
 	@LastModifiedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = true)
 	private LocalDateTime updatedDate;  
 	
 	public void updateSettings(boolean approvalEnabled, boolean noticeEnabled, User user) {
 	    this.isApprovalEnabled = approvalEnabled;
 	    this.isNoticeEnabled = noticeEnabled;
 	    this.user = user;
 	}
}
