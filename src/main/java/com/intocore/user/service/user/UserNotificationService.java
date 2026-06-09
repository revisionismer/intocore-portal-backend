package com.intocore.user.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intocore.user.domain.User;
import com.intocore.user.domain.UserNotification;
import com.intocore.user.domain.UserNotificationRepository;
import com.intocore.user.domain.UserRepository;
import com.intocore.user.web.dto.user.UserNotificationReqDto;
import com.intocore.user.web.dto.user.UserNotificationRespDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserNotificationService {
	
	private final UserRepository userRepository;
	
	private final UserNotificationRepository userNotificationRepository;
	
	@Transactional(readOnly = true)
	public List<UserNotificationRespDto> readAllNotificationByUserId(Long userId) {
		
		User user = userRepository.findById(userId)
				 .orElseThrow(() -> new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다."));
		
		List<UserNotification> result = userNotificationRepository.findAllByUserId(userId);
		
		return result.stream()
					 .map( notification -> new UserNotificationRespDto(notification.getId(), notification.isApprovalEnabled(), notification.isNoticeEnabled(), user.getId()))
					 .collect(Collectors.toList());
	}
	
	@Transactional
	public void updateNotification(Long userId, UserNotificationReqDto userNotificationReqDto) {
		
		User user = userRepository.findById(userId)
				 .orElseThrow(() -> new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다."));
		
		UserNotification userNotification = userNotificationRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("설정 정보를 찾을 수 없습니다."));
		
		userNotification.updateSettings(userNotificationReqDto.isApprovalEnabled(), userNotificationReqDto.isNoticeEnabled(), user);
	}
}
