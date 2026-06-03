package com.intocore.user.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserPasswordReqDto {
	
	private String curPassword;
	private String newPassword;
	private String newPasswordChk;
}
