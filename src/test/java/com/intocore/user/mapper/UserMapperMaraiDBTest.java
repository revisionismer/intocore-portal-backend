package com.intocore.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.intocore.common.constant.user.UserEnum;
import com.intocore.user.domain.User;

@SpringBootTest
@ActiveProfiles("prod")
class UserMapperMaraiDBTest {
	
	@Autowired
	private UserMapper userMapper;

	// 2026-02-14 : 여기서부터 다시
	@Test
	void 마리아DB연동테스트() {
		
		User testUser = new User();
		testUser.setId(10L);
		testUser.setUsername("testAdmin");
		testUser.setName("테스터");
		testUser.setPassword("1234");
		testUser.setRole(UserEnum.ADMIN);
		testUser.setPhone("010-1111-1111");
		
		userMapper.insertTestUser(testUser);
		
		String username = userMapper.findUsernameById2(10L);
		
	    System.out.println("MariaDB 조회 결과: " + username);
	    
	    assertThat(username).isEqualTo("testAdmin");
		
	}

}
