package com.intocore.user.web.dto.user;

import com.intocore.user.domain.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserProfileRespDto {
	private Long id;
	private String username;
	private String role;
	private String profileImageUrl;
	
	public UserProfileRespDto(User userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
		this.role = userEntity.getRole().getValue();
		this.profileImageUrl = userEntity.getProfileImageUrl();
	}
}
