package com.intocore.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.intocore.common.constant.user.UserEnum;
import com.intocore.user.domain.User;
import com.intocore.user.domain.UserRepository;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testCreateUser() {
        // 1-1. 테스트용 회원 정보
        String username = "testuser@test.com";
        String rawPassword = "1234";
   
        // 1-2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 1-3. User 엔티티 생성
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .name("테스트유저")
                .phone("010-1111-1111")
                .role(UserEnum.USER)
                .createdDate(LocalDateTime.now())
                .build();

        // 1-4. DB 저장
        User savedUser = userRepository.save(user);

        // 1-5. 저장 후 검증
        assertThat(savedUser.getId()).isNotNull();
        assertThat(passwordEncoder.matches(rawPassword, savedUser.getPassword())).isTrue();

        System.out.println("회원가입 완료, 암호화된 비밀번호: " + savedUser.getPassword());
    }

}
