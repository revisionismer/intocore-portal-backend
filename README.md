# intocore-portal-backend

1. 프로젝트 생성 및 Git 연동
2. 기본적인 구조와 필수 디펜던시 설정
3. 패키지 구조 변경 및 DB 연동 테스트(h2, mariaDB)
4. Mybatis 연결 및 JPA 설정 후 User 엔티티 생성 및 테스트
5. PrincipalDetails, PrincipalDetailsService 생성 및 구현(UserDetails와 UserDetailsService 재정의)
6. JWT 인증/인가 구현 및 테스트
7. JWT 인증/인가 Bearer + access\_token을 헤더에 넣는 방식에서 HttpOnly 쿠키 인증 방식으로 변경
8. JWT 인증/인가 HttpOnly 쿠키 인증 방식 버전 커스텀 로그아웃 로직 구현
