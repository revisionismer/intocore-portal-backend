package com.intocore.user.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intocore.common.constant.user.UserEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // 1-3. JPA에서 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문에 추가(중요)
@Getter @Setter
@Table(name = "user_tb")
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	@Column(length = 100, unique = true, nullable = false)  // 1-16. OAuth2 로그인을 위해 20 -> 100자로 늘린다.
	@Email(message = "이메일 형식이어야 합니다.")
	private String username;  // 1-2. 계정 명
	
	@Column(nullable = false)
	private String password; // 1-3. 계정 비밀번호
	
	@Column(nullable = false)
	private String name;  // 1-4. 사용자 이름
	
	private String website; // 1-5. 웹사이트
	
	private String bio;  // 1-6. 자기소개

	@Column(nullable = false)
	private String phone;  // 1-7. 휴대폰 번호
	
	private String gender;  // 1-8. 성별
	
	private String profileImageUrl;  // 1-9. 프로필 이미지 경로
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserEnum role; // ADMIN, CUSTOMER // 1-10. 권한

	@CreatedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = false)
 	private LocalDateTime createdDate;  // 1-11. 생성일자
 	 
 	@LastModifiedDate
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	@Column(nullable = true)
 	private LocalDateTime updatedDate;  // 1-12. 수정일
 	
 	private String refreshToken;
 	
 	@Builder
	public User(Long id, String username, String password, String name, String phone, UserEnum role, LocalDateTime createdDate, LocalDateTime updatedDate) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.role = role;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
}
