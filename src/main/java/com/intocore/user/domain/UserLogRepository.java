package com.intocore.user.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog, Long>{

	List<UserLog> findAllByUserId(Long userId);
}
