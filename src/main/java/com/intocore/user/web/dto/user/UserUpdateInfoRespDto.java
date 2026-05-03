package com.intocore.user.web.dto.user;

import com.intocore.user.domain.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserUpdateInfoRespDto {

	private String username;
	private String name;
	private String password;
	private String profileImageUrl;
	private String phone;
	private String gender;
	private String website;
	private String bio;
	private String roleName;
	
	public UserUpdateInfoRespDto(User userEntity) {
		this.username = userEntity.getUsername();
		this.name = userEntity.getName();
		this.password = userEntity.getPassword();
		this.profileImageUrl = userEntity.getProfileImageUrl();
		this.phone = userEntity.getPhone();
		this.gender = userEntity.getGender();
		this.bio = userEntity.getBio();
		this.roleName = userEntity.getRole().toString();
	}
	
}
