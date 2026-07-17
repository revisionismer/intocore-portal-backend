package com.intocore.user.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intocore.user.domain.User;
import com.intocore.user.domain.UserLog;
import com.intocore.user.domain.UserLogRepository;
import com.intocore.user.web.dto.user.UserLogRespDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)  // RuntimeException 말고도 모든 예외가 터졌을시 롤백시킨다.
@RequiredArgsConstructor
public class UserLogService {
	
	private final UserLogRepository userLogRepository;

	public void insertAccessLog(User user, HttpServletRequest request) {
		
		// 1-2. IP 추출 (프록시, 로드 밸런서 환경 고려)
        String ip = getClientIp(request);
        
        // 1-3. 접속 기기 및 브라우저 정보(User-Agent) 추출
        String userAgent = request.getHeader("User-Agent");
        
        // 1-4. DB 컬럼 길이(100자) 초과 방지를 위한 방어 로직
        if (userAgent == null) {
            userAgent = "Unknown Device";
        } else if (userAgent.length() > 100) {
            userAgent = userAgent.substring(0, 99); 
        }
        
        // 1-5. 국가 정보 추출 (우선순위: CDN 헤더 -> 브라우저 로케일 -> 기본값)
        String country = request.getHeader("CF-IPCountry");
        if (country == null) {
        	country = request.getHeader("CloudFront-Viewer-Country");
        }
        
        if (country == null) {
        	country = request.getLocale().getCountry();
        }
        
        if (country == null || country.isEmpty()) {
        	country = "UNKNOWN";
        }
        
        // 1-6. UserLog 엔티티 빌더로 생성.
        UserLog userLog = UserLog.builder()
                .user(user)
                .ip(ip)
                .device(extractDevice(userAgent)) 
                .country(country) // 접속 국가는 우선 "KR"로 고정
                .build();
        
        // 1-7. 접속 로그 저장.
        userLogRepository.save(userLog);
		
	}
	
	// 2-1. 유저 접속 로그 리스트 조회
	@Transactional(readOnly = true)
	public List<UserLogRespDto> getUserLogs(User user) {
		
		List<UserLog> logs = userLogRepository.findAllByUserId(user.getId());
		
		return logs.stream()
				   .map( log -> new UserLogRespDto(log, user.getUsername()) )
				   .collect(Collectors.toList());
	}
	
	// 1-1. user IP 정보 뽑아내는 메소드.
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 다중 프록시를 거쳐 콤마(,)로 구분되어 들어올 경우 첫 번째 IP만 추출
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        // 로컬호스트 IPv6 형태 처리
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        return ip;
    }
    
    // 1-8. 긴 User-Agent를 짧게 줄여주는 함수
    private String extractDevice(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown Device";
        }

        String os = "Unknown OS";
        String browser = "Unknown Browser";

        // 1-9. 운영체제 찾기
        if (userAgent.contains("Windows")) {
        	os = "Windows";
        } else if (userAgent.contains("Mac")) {
        	os = "Mac";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
        	os = "iOS";
        } else if (userAgent.contains("Android")) {
        	os = "Android";
        } else if (userAgent.contains("Linux")) {
        	os = "Linux";
        }

        // 1-10. 브라우저 찾기
        if (userAgent.contains("Edg")) {
        	browser = "Edge";
        } else if (userAgent.contains("Chrome")) {
        	browser = "Chrome";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
        	browser = "Safari";
        } else if (userAgent.contains("Firefox")) {
        	browser = "Firefox";
        }

        return os + " / " + browser;
    }
}
