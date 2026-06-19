package com.intocore.user.web.api.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intocore.common.dto.ResponseDto;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.user.domain.User;
import com.intocore.user.service.user.UserLogService;
import com.intocore.user.web.dto.user.UserLogRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class UserLogsApiController {

    private final UserLogService userLogService;
    
    @GetMapping("/s/access-logs")
    public ResponseEntity<?> readUserLoginLogs(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        
        // 시큐리티 세션에서 현재 로그인한 유저 정보를 꺼냅니다.
        User loginUser = principalDetails.getUser();
        
        List<UserLogRespDto> result = userLogService.getUserLogs(loginUser);
   
        return new ResponseEntity<>(new ResponseDto<>(1, loginUser.getId() + "번 유저 접속 로그 정보 조회 성공", result), HttpStatus.OK);
    }
}
