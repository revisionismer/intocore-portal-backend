package com.intocore.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.security.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomLogoutHandler implements LogoutHandler {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final JwtService jwtService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		PrincipalDetails principalDetails = null;
		
	    if(authentication != null && authentication.getPrincipal() instanceof PrincipalDetails){
	        principalDetails = (PrincipalDetails)authentication.getPrincipal();
	    }
	    
	    response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
				
		Map<String, Object> responseData = new HashMap<>();
				
		responseData.put("code", 1);
		responseData.put("message", "JWT 로그아웃 성공");
				
	    if(principalDetails != null){
	        responseData.put("userId", principalDetails.getUser().getId());
	        responseData.put("username", principalDetails.getUser().getUsername());
	        responseData.put("role", principalDetails.getUser().getRole());
	    }
		
		jwtService.logout(request, response);
		
		try {
		    String result = new ObjectMapper().writeValueAsString(responseData);
		    response.getWriter().write(result);
		    response.setStatus(HttpServletResponse.SC_OK);
		    
		    response.getWriter().flush();
		} catch (Exception e) {
		    log.info(e.getMessage());
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
 	}

}
