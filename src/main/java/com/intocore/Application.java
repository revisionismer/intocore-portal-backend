package com.intocore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })  // 2-1. 시큐리티 동작 막기 : 구현전이니 막자(비밀번호 입력하라는거 안뜸)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
