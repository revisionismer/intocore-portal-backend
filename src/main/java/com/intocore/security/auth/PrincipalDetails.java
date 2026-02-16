package com.intocore.security.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.intocore.common.constant.user.UserEnum;
import com.intocore.user.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final User user;

	// 1-1. 로그인 사용자의 권한을 생성해서 Security Context에서 사용할 수 있게 포장해주는 메서드(만약 권한이 없는 사용자가 접근할 경우 자동으로 USER권한을 부여하는 로직 추가)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {  
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		UserEnum roleEnum = user.getRole();
		
		// Tip : UserEnum에서 toString과 name의 차이 -> toString은 ADMIN("관리자") 이렇게 되어 있으면 관리자라고 정해진 String을 가져오고 name은 ADMIN을 가져온다.
		String role = (roleEnum == null) ? UserEnum.USER.name() : roleEnum.name();
		
		authorities.add(() -> "ROLE_" + role);
		
		return authorities;
	}

	// 1-2. 계정의 패스워드 반환
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	// 1-3. 계정의 username 반환
	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	// 1-4. 계정의 사용 기간 만료 여부(최신 버전부터는 따로 구현안해도 기본값 true(항상 로그인 허용)), ex) 계약직 계정 만료, 체험판 계정 기간 종료, 휴먼 계정 정책 전환
	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}
	
	// 1-5. 계정의 잠금 여부(true : 잠금 X, false : 잠금 O), ex) 로그인 5회 실패 시 잠금, 관리자 강제 차단, 이상 로그인 탐지 등
	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	// 1-6. 계정의 비밀번호가 만료되었는지 여부(true : 비밀번호 유효, false : 비밀번호 만료), ex) 90일마다 비밀번호 변경 정책 적용시, 보안 강화 서비스, 기업 내부 시스템 등
	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}
	
	// 1-7. 계정의 활성화 상태 체크(true : 활성화, false : 비활성화), ex) 이메일 인증 전 사용자, 관리자 승인 전 사용자 등
	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
	
}
