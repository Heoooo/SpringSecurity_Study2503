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
	-사용자가 로그인 폼을 통해 아이디와 비밀번호를 입력하고 전송하면, 이는 POST 요청으로 서버로 전송
	-이 때, 기본적인 입력 정보가 제대로 전달되지 않으면 error를 자동 전달 => /member/login?error
	-따라서, 로그인 폼 페이지 템플릿에서 에러를 받아서 처리하는 코드 작성이 필요
	-로그인 구현이 안되었다면 서버 리스타트 시 제공되는 비밀번호, user 아이디로 로그인 가능
2단계
	-AuthenticationFilter(인증 필터) 작동 시작
	-UsernamePasswordAuthenticationFilter 등의 인증 필터들이 순차적으로 실행
	-UsernamePasswordAuthenticationFilter는 사용자 요청으로부터 사용자 입력 정보(아이디, 비밀번호)를 추출하여 객체에 저장
	-추출된 정보를 기반으로 UsernamePasswordAuthenticationToken 객체 생성
	-생성된 UsernamePasswordAuthenticationToken 객체를 AuthenticationManager에게 전달하여 인증을 요청
3단계
	-AuthenticationManager 호출 및 인증 처리
	-AuthenticationManager는 전달받은 UsernamePasswordAuthenticationToken 객체를 ProviderManager에게 전달
	-ProviderManager는 해당 토큰을 처리할 수 있는 Provider를 서치
	-적절한 Provider가 발견되면, Provider의 authenticate() 메소드가 호출되어 실제 인증 로직이 수행
	
	[!]
	AuthenticationProvider는 뭐지?
	-스프링 시큐리티에서 사용자 인증을 담당하는 핵심 인터페이스 => 이 인터페이스를 커스터마이징하여 다양한 인증 방식을 구현
	-즉, 사용자가 입력한 인증 정보(아이디, 비밀번호)가 유효한지 판단하는 로직을 구현하는 역할
	-AuthenticationProvider 인터페이스를 구현하는 구현 클래스는 반드시 authenticate(Authentication authentication) 메소드를 오버라이드
	-위에서 "적절한 Provider가 발견되면" => 이 말은 기본적으로 스프링 시큐리티는 AuthenticationProvider 인터페이스를 구현한 구현 클래스가
	 빈(Bean)으로 등록되어 있다면 해당 AuthenticationProvider를 이용해서 인증을 진행
	-만약 AuthenticationProvider를 구현한 구현 클래스가 없다면 스프링 시큐리티에서 제공하는 AuthenticationProvider가 실행되며 인증 진행
4단계
	-AuthenticationProvider 호출
5단계
	-인증 결과 저장
	