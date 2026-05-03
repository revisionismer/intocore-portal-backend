package com.intocore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })  // 2-1. 시큐리티 동작 막기 : 구현전이니 막자(비밀번호 입력하라는거 안뜸)
@EnableJpaAuditing // 2-2. JPA Auditing : 엔티티에 @CreatedBy, @UpdatedBy 적용하기 위해 삽입.
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
