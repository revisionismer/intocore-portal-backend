package com.intocore.user.web.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intocore.common.dto.ResponseDto;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.user.domain.User;
import com.intocore.user.service.user.UserService;
import com.intocore.user.web.dto.user.UserInfoRespDto;
import com.intocore.user.web.dto.user.UserPasswordReqDto;
import com.intocore.user.web.dto.user.UserProfileRespDto;
import com.intocore.user.web.dto.user.UserUpdateInfoReqDto;
import com.intocore.user.web.dto.user.UserUpdateInfoRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class UserApiController {

	private final UserService userService;
	
	@GetMapping("/auth/me")
    public ResponseEntity<?> isCurrentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (principalDetails == null || principalDetails.getUser() == null) {
            return new ResponseEntity<>(new ResponseDto<>(-1, "로그인 정보가 없습니다.", null), HttpStatus.UNAUTHORIZED);
        }

        User loginUser = principalDetails.getUser();
        UserInfoRespDto userInfoRespDto = userService.userInfoByUserId(loginUser.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
    }
	
	@GetMapping("/s/info")
	public ResponseEntity<?> userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserInfoRespDto userInfoRespDto = userService.userInfoByUserId(loginUser.getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 정보 조회 성공", userInfoRespDto), HttpStatus.OK);
	}
	
	@PutMapping("/s/update/profileImage")
	public ResponseEntity<?> profileImageUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam("profileImageFile") MultipartFile profileImageFile) { // 1-1. html input file의 name 값과 매핑해줘야 한다.(중요)
		
		User loginUser = principalDetails.getUser();
		
		UserProfileRespDto userProfileRespDto = userService.userProfilePictureUpdate(loginUser.getId(), profileImageFile);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "프로필 사진 변경 성공", userProfileRespDto), HttpStatus.OK);
	}
	
	@PutMapping("/s/{id}/update")
	public ResponseEntity<?> updateUserInfo(@PathVariable("id") Long id, @RequestBody UserUpdateInfoReqDto userUpdateInfoReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		UserUpdateInfoRespDto userUpdateInfoReqpDto = userService.userProfileInfoUpdate(id, loginUser, userUpdateInfoReqDto);
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 정보 업데이트 성공", userUpdateInfoReqpDto), HttpStatus.OK);
	}
	
	@PutMapping("/s/{id}/password")
	public ResponseEntity<?> updateUserPassword(@PathVariable("id") Long id, @RequestBody UserPasswordReqDto userPasswordReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User loginUser = principalDetails.getUser();
		
		userService.userPasswordUpdate(id, loginUser, userPasswordReqDto);
		
		return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 비밀번호 업데이트 성공", null), HttpStatus.OK);
	}
	
}
