package com.intocore.user.web.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intocore.common.dto.ResponseDto;
import com.intocore.user.service.auth.AuthService;
import com.intocore.user.web.dto.auth.SignUpReqDto;
import com.intocore.user.web.dto.auth.SignUpRespDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class AuthApiController {

	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {
		
		SignUpRespDto signUpRespDto = authService.signUp(signUpReqDto);
	    
	    return new ResponseEntity<>(new ResponseDto<>(1, "회원 가입 성공", signUpRespDto), HttpStatus.OK);
	}
}
