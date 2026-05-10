package com.intocore.user.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SignUpReqDto {
	
	@Size(min = 4, max = 20)
	@NotBlank(message = "아이디는 공백일 수 없습니다.")  // NULL, 빈 문자열, 스페이스만 있는 문자열 필터링
	@Email(message = "이메일 형식이어야 합니다.")
	private String username;
	
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	private String password;
	
	@NotBlank(message = "비밀번호 확인은 공백일 수 없습니다.")
	private String passwordCheck;
	
	@NotBlank(message = "이름은 공백일 수 없습니다.")
	private String name;
	
	private String gender;
	private String phone;
	private String website;
}
