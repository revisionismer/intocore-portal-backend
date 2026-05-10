package com.intocore.user.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intocore.common.constant.user.UserEnum;
import com.intocore.user.domain.User;
import com.intocore.user.domain.UserRepository;
import com.intocore.user.web.dto.auth.SignUpReqDto;
import com.intocore.user.web.dto.auth.SignUpRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SignUpRespDto signUp(SignUpReqDto signUpReqDto) {
		
		User newUser = new User();
		
		newUser.setUsername(signUpReqDto.getUsername());
		
		if(signUpReqDto.getPassword().equals(signUpReqDto.getPasswordCheck())) {
			newUser.setPassword(passwordEncoder.encode(signUpReqDto.getPassword()));
		}
		
		newUser.setName(signUpReqDto.getName());
		newUser.setGender(signUpReqDto.getGender());
		newUser.setPhone(signUpReqDto.getPhone());
		newUser.setWebsite(signUpReqDto.getWebsite());
		newUser.setRole(UserEnum.USER);
		
		User userEntity = userRepository.save(newUser);
		
		return new SignUpRespDto(
					userEntity.getId(),
					userEntity.getUsername(),
					userEntity.getPassword(), 
					userEntity.getName(), 
					userEntity.getPhone(), 
					userEntity.getGender(), 
					userEntity.getWebsite());
	}
}
