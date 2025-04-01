로그인/로그아웃 구현(스프링 시큐리티를 이용한 적용 => 크게 봤을 때 3단계 또는 4단계) => 디테일한 코딩보다 전체 흐름을 알고 있는게 훨씬 더 중요하다
1단계
	-폼(Form) 기반 로그인에 대한 URL 지정
	-로그인 페이지 지정, 로그인 성공 후 이동하는 페이지 지정
	-http.formLogin() 메소드 사용해서 구현 -> SecurityConfig.java 파일에서 작업
	-Controller.java 파일에서 요청에 대한 처리도 작업
	-로그인 인증 시 필요한 페이지 관련된 프론트엔드 템플릿 디자인 파일도 작업
2단계
	-가장 중요한 부분
	-3단계에서 authenticationManager() 메소드를 사용하여 로그인 인증 처리 시 내부에서 사용하는 파일과 메소드를 구현해 놓는 단계
	-필요한 파일들
		+MemberRepository.java 파일에서 findByusername(String username) 추가
		+MemberRole.java 파일 생성(enum) => 로그인 시 권한 부여를 위해
		+MemberSecurityService.java => UserDetailsService 인터페이스를 implements 하여 loadUserByUsername 메소드 강제 구현
	-loadUserByUsername() 메소드는 뭐지?
		+이름 그대로 사용자 이름(아이디)로 스프링 시큐리티의 Member 객체를 조회하여 리턴하는 메소드
		+Member 객체 조회하였는데 DB에 없으면 => UsernameNotFoundException 예외 발생
		+정상적으로 회원 객체가 검색되었다면 => 적절한 권한 부여 => 회원 객체 반환 => 반환된 객체를 스프링 시큐리티에서 사용
		+스프링 시큐리티는 loadUserByUsername() 메소드에서 반환된 회원 객체의 비번이 사용자가 입력한 비번과 일치하는지 여부를 내부에서 자동 검사
3단계
	-로그인 인증 및 권한 부여 프로세스를 처리하는 메소드를 작성하는 단계
	-AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) ...
	-SecurityConfig.java 파일에서 작업
4단계
	-가장 간단, 마무리 작업
	-프론트엔드 로그인 관련 템플릿 파일에서 작업
	
	
Spring Security 폼(Form) 기반 로그인 과정
1단계
	-요청 전송 및 내부에서 에러가 발생하면 로그인 URL로 에러 전송
2단계
	-AuthenticationFilter(인증 필터) 작동 시작
3단계
	-AuthenticationManager 호출 및 인증 처리
4단계
	-AuthenticationProvider 호출
5단계
	-인증 결과 저장
	