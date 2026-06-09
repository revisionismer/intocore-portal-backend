package com.intocore.user.web.api.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intocore.common.dto.ResponseDto;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.user.domain.User;
import com.intocore.user.service.user.UserNotificationService;
import com.intocore.user.web.dto.user.UserNotificationReqDto;
import com.intocore.user.web.dto.user.UserNotificationRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class UserNotificationApiController {
	
	private final UserNotificationService userNotificationService;
	
	@GetMapping("/s")
	public ResponseEntity<?> readNotificationPreferences(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		List<UserNotificationRespDto> result = userNotificationService.readAllNotificationByUserId(loginUser.getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 알림설정 데이터 리스트 조회성공", result), HttpStatus.OK);
	}

	@PutMapping("/s/preferences")
	public ResponseEntity<?> updateNotificationPreferences(@RequestBody UserNotificationReqDto userNotificationReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		userNotificationService.updateNotification(loginUser.getId(), userNotificationReqDto);
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 알림설정 업데이트 성공", null), HttpStatus.OK);
	}
	
}
