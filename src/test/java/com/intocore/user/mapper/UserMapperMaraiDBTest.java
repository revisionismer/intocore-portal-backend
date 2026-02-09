package com.intocore.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.intocore.user.domain.User;

@SpringBootTest
@ActiveProfiles("prod")
class UserMapperMaraiDBTest {
	
	@Autowired
	private UserMapper userMapper;

	@Test
	void 마리아DB연동테스트() {
		
		User testUser = new User(10L, "testAdmin", "1234", "ROLE_ADMIN");
		
		userMapper.insertTestUser(testUser);
		
		String username = userMapper.findUsernameById2(10L);
		
	    System.out.println("MariaDB 조회 결과: " + username);
	    
	    assertThat(username).isEqualTo("testAdmin");
		
	}

}
