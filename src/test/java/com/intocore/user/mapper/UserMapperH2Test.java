package com.intocore.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class UserMapperH2Test {
	
	@Autowired
	private UserMapper userMapper;

	@Test
	void h2연동테스트() {
		String username = userMapper.findUsernameById1(1L);
		
		System.out.println("조회 결과: " + username);
		
		assertThat(username).isEqualTo("admin");
	}

}
