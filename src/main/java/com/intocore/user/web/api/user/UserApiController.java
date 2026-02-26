package com.intocore.user.web.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intocore.common.dto.ResponseDto;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.user.domain.User;
import com.intocore.user.service.user.UserService;
import com.intocore.user.web.dto.user.UserInfoRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class UserApiController {

	private final UserService userService;
	
	@GetMapping("/s/info")
	public ResponseEntity<?> userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserInfoRespDto userInfoRespDto = userService.userInfoByUserId(loginUser.getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
}
