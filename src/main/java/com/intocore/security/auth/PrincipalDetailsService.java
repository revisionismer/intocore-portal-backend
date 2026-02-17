package com.intocore.security.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.intocore.user.domain.User;
import com.intocore.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {  // 1-1. UserDetailsService : 인증 과정에서 사용자 정보를 조회해서 UserDetails 객체로 반환하는 역할
	
	private final UserRepository userRepository;

	// 1-2. DB에서 전달된 username으로 사용자 조회하여
	//      사용자가 존재하면 PrincipalDetails로 감싸서 SecurityContext에서 사용할수 있도록 인증 객체 반환
	//      존재하지 않으면 UsernameNotFoundException을 터트려 로그인 실패 처리
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOp = userRepository.findByUsername(username);
		
		if(userOp.isPresent()) {
			User userPS = userOp.get();
			
			return new PrincipalDetails(userPS);
			
		} else {
			throw new UsernameNotFoundException("아이디 비밀번호를 확인해주세요.");
		} 
		
	}

}
