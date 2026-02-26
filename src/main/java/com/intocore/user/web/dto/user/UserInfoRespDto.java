package com.intocore.user.web.dto.user;

import com.intocore.user.domain.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserInfoRespDto {
	
	private Long id;
	private String username;
	private String name;
	private String phone;
	private String bio;
	private String website;
	private String profileImageUrl;
	private String gender;
	
	public UserInfoRespDto(User userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
		this.name = userEntity.getName();
		this.phone = userEntity.getPhone();
		this.bio = userEntity.getBio();
		this.website = userEntity.getWebsite();
		this.profileImageUrl = userEntity.getProfileImageUrl();
		this.gender = userEntity.getGender();
	}

}