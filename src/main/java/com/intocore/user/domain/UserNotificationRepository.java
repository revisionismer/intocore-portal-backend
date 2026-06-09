package com.intocore.user.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
	
	Optional<UserNotification> findByUserId(Long userId);
	
	List<UserNotification> findAllByUserId(Long userId);
}
