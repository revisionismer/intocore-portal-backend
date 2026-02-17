package com.intocore.security.jwt.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intocore.security.auth.PrincipalDetails;
import com.intocore.security.jwt.JwtProperties;
import com.intocore.security.jwt.dto.SignInDto;
import com.intocore.security.jwt.service.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 1-1. JWT 기반 로그인 인증을 처리하기 위한 커스텀 인증 필터
//      UsernamePasswordAuthenticationFilter를 상속하여 로그인 요청을 가로채고
//      추후 attemptAuthentication, successfulAuthentication 등을 구현할 예정

//  TIP : Controller 이전은 Filter 세계, Controller 이후는 Spring 세계 : Security Filter는 DispatcherServlet 전에 동작하는 서블릿 필터 그래서 Autowired나 @Component를 사용하면 안되고 생성자 주입으로 객체를 받아야한다.

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {  // Tip : UsernamePasswordAuthenticationFilter -> POST /login 요청이 들어오면 실행되는 스프링 시큐리티의 기본 로그인 인증 필터

	private static String secretKey = JwtProperties.SECRET_KEY;
	
	byte[] secretKeyBytes = secretKey.getBytes();	
	
	private AuthenticationManager authenticationManager;
	
	private JwtService jwtService;
	
	// 1-2. SecurityConfig에서 AuthenticationManager와 JwtService를 주입받아 JWT 로그인 인증 처리에 사용
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}
	
	// 1-3. attemptAuthentication : 로그인 요청이 들어왔을 때 실제 인증을 시도하는 메서드
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		// 1-4. username, password를 전달 받아 인증을 수행
		try {
			// 1-5. 로그 찍어보기
			System.out.println("JwtAuthenticationFilter : 인증 처리 중");
			
			// 1-6. ObjectMapper 이용 -> JSON 데이터 파싱해주는 객체
			ObjectMapper om = new ObjectMapper();
						
			// 1-7. 먼저 username과 password 값을 받을 SignInDto를 생성해서 inputStream에서 SignInDto.class 형태로 받는다.
			SignInDto dto = om.readValue(request.getInputStream(), SignInDto.class);
			
			System.out.println("SinInDto : " + dto.getUsername() + ", " + dto.getPassword());	
			
			// 1-8. authenticationManager에 제출하기 위한 인증요청용 토큰 만들기(아직 인증전)
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
									
			// 1-9. authenticationManager가 인증을 수행한다.
			//      내부적으로 UserDetailsService.loadUserByUsername가 호출되고,
			//      비밀번호 검증 후 인증된 Authentication 객체가 반환된다.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
						
			// 1-10. 인증 성공 시 Authentication 내부에 저장된 PrincipalDetails를 꺼낸다. -> 여기서 Authentication 객체는 시큐리티 컨텍스트에 들어가기전 인증하고 완료된 인증 객체.
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
									
			// 1-11. 출력이 되면 로그인이 되었다는 뜻
			System.out.println("==================================================");	
			System.out.println("로그인 완료됨 : "  + principalDetails.getUsername());							
			System.out.println("==================================================");
					
			// 1-12. 검증이 완료된 Authentication을 반환.
			return authentication;
						
		} catch (Exception e) {
			throw new InternalAuthenticationServiceException(e.getMessage());  // 1-13. SecurityConfig에 설정해놓은 authenticationEntryPoint에 걸린다.
		}
	}
	
	// 1-14. 인증이 정상적으로 완료되었으면 successfulAuthentication 메소드가 실행된다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
	
		// 1-15. 인증이 완료되면 JWT 토큰을 만들어서 request를 요청한 사용자에게 JWT토큰을 return해주면 된다.
		System.out.println("인증 성공");
		
		// 1-16. 방금 인증이 완료된 Authentication형 authResult에서 PrincipalDetails 객체를 가져온다.
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

		// 1-17. JWT 토큰 만들기 1 : Header값 생성
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// 1-18. JWT 토큰 만들기 2 : claims 부분 설정(토큰 안에 담을 내용)
		Map<String, Object> claims = new HashMap<>();;
		claims.put("id", principalDetails.getUser().getId());
		claims.put("username", principalDetails.getUser().getUsername());
		claims.put("role", principalDetails.getUser().getRole());
		
		// 1-19. JWT 토큰 만들기 3 : 만료 시간 설정(Access token) ->  1000 * 60L * 60L * 1 = 1시간, 500 * 60L * 60L * 1 = 30분
		Long expiredTime = 1000 * 60L * 60L * 1;
//		Long expiredTime = 8 * 60L * 60L * 1;
		
		Date date = new Date();
		date.setTime(date.getTime() + expiredTime);
		
		log.info("access_token 만료일자 : " + date);
		
		// 1-20. JWT 토큰 만들기 4 : hmacSha 형식 key 만들기 
		Key key = Keys.hmacShaKeyFor(secretKeyBytes);
	
		// 1-21. JWT 토큰 Builder : access_token
		String access_token = Jwts.builder()
				.setHeader(headers) 
				.setClaims(claims)
				.setSubject("access_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		System.out.println("access_token = " + access_token);	
		
		// 1-22. JWT 토큰 Builder : refresh token -> expiredTime을 24시간보다 약간 크게 설정함.
		expiredTime *= 23;
		expiredTime += 100000;
		date.setTime(System.currentTimeMillis() + expiredTime);
		
		String refresh_token = Jwts.builder()
				.setHeader(headers) 
				.setSubject("refresh_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		System.out.println("refresh_token = " + refresh_token);
		
		// 1-23. refresh token 저장.
		jwtService.setRefreshToken(principalDetails.getUser().getUsername(), refresh_token);		
				
		// 1-24. JWT 토큰 response header에 담음(주의 : Bearer 다음에 한칸 띄우고 저장해야함)
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token);
		
		// 1-25. access_token 쿠키에 저장.
		Cookie cookie = new Cookie("access_token", access_token);
				
		// 1-26. 쿠키는 항상 도메인 주소가 루트("/")로 설정되어 있어야 모든 요청에서 사용 가능.
		cookie.setPath("/");
		cookie.setSecure(true);
			
		response.addCookie(cookie);
		
		// 1-27. 로그인 성공시 응답 객체 만들어 주기 1 : response객체 기본 세팅
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		// 1-28. 응답해줄 Map형태의 객체
		Map<String, Object> responseData = new HashMap<>();
		
		// 1-29. 응답 데이터 셋팅
		responseData.put("code", 1);
		responseData.put("message", "jwt 인증 성공");
		responseData.put("username", principalDetails.getUser().getUsername());
		responseData.put("role", principalDetails.getUser().getRole());
		
		// 1-30. ObjectMapper를 이용해 json형태의 String으로 변환
		String result = new ObjectMapper().writeValueAsString(responseData);
		
		// 1-31. response에 write
		response.getWriter().write(result);
	
		// Tip : super.successfulAuthentication(request, response, chain, authResult)
	    // → 기본 세션 기반 인증 처리 로직 수행 (SecurityContext 저장 + 세션 생성 + JSESSIONID 발급)
	    // → 우리는 Stateless JWT 인증 방식을 사용하므로 호출하지 않음
	//	super.successfulAuthentication(request, response, chain, authResult);
	}
	
	// 2-1. 로그인 실패시 처리(Custom)
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		
		log.warn("로그인 실패: {}", failed.getMessage());

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		// 2-2. 응답해줄 Map형태의 객체
		Map<String, Object> responseData = new HashMap<>();
		
		// 2-3. 응답 데이터 셋팅 -> 2023-10-05 : Custom 로그인 실패처리 message 내용 변경 ex) 인증 실패 -> 아이디와 비밀번호를 확인해주세요.
		responseData.put("code", -1);
		responseData.put("message", "아이디와 비밀번호를 확인해주세요.");
		
		// 2-4. ObjectMapper를 이용해 json형태의 String으로 변환
		String result = new ObjectMapper().writeValueAsString(responseData);
		
		// 2-5. response에 write
		response.getWriter().write(result);
	}
}
