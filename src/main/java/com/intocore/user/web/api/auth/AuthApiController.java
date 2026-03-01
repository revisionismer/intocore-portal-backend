package com.intocore.user.web.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intocore.common.dto.ResponseDto;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.user.domain.User;
import com.intocore.user.web.dto.user.UserInfoRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class AuthApiController {

	@GetMapping("/me")
	public ResponseEntity<?> me(Authentication authentication) {

	    if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof PrincipalDetails principalDetails)) {
	        return new ResponseEntity<>(new ResponseDto<>(0, "로그인한 회원이 없습니다.", null), HttpStatus.OK);
	    }
	    
	    User user = principalDetails.getUser();
	    
	    UserInfoRespDto userInfoRespDto = new UserInfoRespDto(user);
	    
	    return new ResponseEntity<>(new ResponseDto<>(1, user.getUsername() + " 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
}
