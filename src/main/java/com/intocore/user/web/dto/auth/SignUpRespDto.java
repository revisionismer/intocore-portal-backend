package com.intocore.user.web.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SignUpRespDto {
	
	private Long id;
	private String username;
	private String password;
	private String name;
	private String phone;
	private String gender;
	private String website;
	
	public SignUpRespDto(Long id, String username, String password, String name, String phone, String gender, String website) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.gender = gender;
		this.website = website;
	}
	
}
