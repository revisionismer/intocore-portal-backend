package com.intocore.user.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInfoReqDto {
	
	private String name;
	private String password;
	private String phone;
	private String gender;
	private String website;
}
