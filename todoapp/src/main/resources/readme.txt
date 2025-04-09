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
	-이 단계를 달리 말하면 => UserDetailsService를 통한 사용자 정보 로드
	-이 과정에서 스프링 시큐리티는 기본적으로 UserDetailsService 및 PasswordEncoder 사용
	-AuthenticationProvider 구현체는 UserDatailsService를 호출하여 입력된 아이디와 일치하는 사용자 정보를 데이터베이스 조회
	-UserDetailsService는 일반적으로 데이터베이스 및 다양한 저장소에서 사용자 정보를 가져올 수 있도록 구현
	-암호화된 비밀번호 일치 확인 => 회원이 존재한다면 PasswordEncoder를 사용하여 요청된 비밀번호와 일치하는지 확인 => 자동 체크
	
	[!]
	UserDetailsService는 뭐지?
	-스프링 시큐리티에서 사용자 정보를 제공하는 핵심 인터페이스
	-일반적으로 데이터베이스에서 사용자 정보를 조회하여 가져온 후 UserDetails 객체로 반환
	-사용자 정보(아이디, 비밀번호, 권한 등)을 제공하는 역할을 수행 => 주로 사용자 인증을 위해 사용
	-스프링 시큐리티에서 제공하는 인터페이스이므로, 해당 인터페이스를 구현하여 사용자 정보를 제공하는 구현 클래스 작성
	
	public class MemberSecurity implements UserDetailsService {
		public UserDetails loadUserByUsername(String username(아이디))
	}
5단계
	-인증 결과 저장
	-AuthenticationProvider로 부터 반환된 Authentication 객체를 SecurityContext에 저장(인증 결과 저장)
	-인증 성공 여부에 따라서 아래 각각 핸들러를 실행
		+AuthenticationSuccessHandler
		+AuthenticationFailureHandler


Writer(작성자) 처리하기 => DB 연동 작업 =>Todotitle, Todowork 모두 메커니즘 거의 동일
1단계
	-Todotitle.java(엔티티 파일) 변경 필요
	-Writer를 DB에 입력해야 하니까.. (사용자이름 넣을 필드 생성 필요)
	-생각할게 뭐가 있을까?
		1)이 필드에 어떤 값을 넣으면 될까? => 다양한 값을 넣을 수 있지만 회원 고유 넘버(ID) 값을 입력
		2)애너테이션 붙여야 하나?
2단계
	-TodotitleController.java 수정 필요
	-인증(로그인)한 회원 객체의 정보를 얻어야 하니까
	-이 때, Member 객체 정보를 얻는 방법으로 => Principal 객체 활용
	-객체 정보가 필요한 곳에서 => Principal principal 해서 사용0
	-어떤 부분에서 필요할까?
		1) 할 일 등록하는 단계에서 회원 정보 필요 => @PostMapping("/create") 요청 시 필요
		2) todotitleCreate() 메소드에서 작성자 아이디(고유넘버) 전달
		3) 이 때, principal 객체 활용
3단계
	-TodotitleService.java 수정 필요
	-전달된 회원 객체를 받아서 데이터베이스에 입력해야 하니까 -> 가장 간단
4단계
	-MemberService.java 수정 필요(추가 메소드 작업)
	-getMember() => 한 명의 회원 정보 요청이 들어오면 반환
5단계
	-todotitle_list.html 템플릿 페이지 수정 필요
	-이 때, 데이터베이스 필드에는 고유넘버(ID)가 들어가지만 가져올 떄는 ${todotitle.writer.username} 출력	
	

Writer(작성자) 처리하기 2 => todowork 테이블에 입력
1단계
	-Todowork.java (엔티티 파일) 수정
	-작성자 들어갈 새 필드 추가
		@ManyToOne
		private Member writer;
2단계
	-TodoworkController.java 수정
	-현재 로그인한 사용자의 사용자이름(ID) 확인 => principal.getName() 호출
	-호출해서 사용자 아이디를 서비스 메소드로 Member 객체 넘김
3단계
	-TodoworkService.java 수정
	-넘어온 Member 객체 받아서 데이터베이스에 입력 => writer_id 필드에 고유넘버(ID) 값이 입력
4단계
	-todotitle_detail.html 템플릿 페이지 수정
	-필요하면 style.css 수정
	-할 일 타이틀 작성자 => ${todotitle.writer.username}
	-관련된 할 일 작성자 => ${tw.writer.username}
	-null 체크 => ${tw.writer != null}
	-날짜 출력 => 날짜 객체가 아닌 문자열 타입임을 기억 => ${tw.createDate}
최종 필드 구성(Todowork 엔티티 파일)
	 todotitle_id => 타이틀 고유넘버(ID) 값이 들어감
	 writer_id => 회원 고유넘버(ID) 값이 들어감
	 
	 
메서드에 인증(로그인) 권한 부여하기
Principal 객체 vs @PreAuthorize 애너테이션 관계
1.
이 객체는 기본적으로 사용자가 인증(로그인)을 해야지만 생성이 되는 객체이기 때문에 이런 것을 잘 기억하는게 중요!
무슨 문제가 있나?
	-등록 페이지를 관리자 전용에서 모두 허용으로 권한을 풀면 => 에러 발생
	-이 때, 발생하는 에러는 => "Principal" is null
2.
이 문제를 해결하는 방법은?
	-기본적으로 이 객체(Principal)를 사용하는 해당 메소드에 특정 애너테이션을 사용 => @PreAuthorize("isAuthenticated()")
	-@PreAuthorize("isAuthenticated()") => 이게 붙으면 인증(로그인)된 경우에만 메소드가 실행
	-즉, 로그인한 사용자만이 이 메소드를 호출할 수 있게 된다.
3.
로그인 하지 않은 상태인데 @PreAuthorize("isAuthenticated()") 붙은 메소드가 호출되면?
	-인증하라고 로그인 페이지로 강제 이동
4.
기본적인 사용 방법
	-Principal 객체가 사용되어지는 메소드를 찾아서 @PreAuthorize 적용
	-그런 후 @PreAuthorize 애너테이션이 해당 프로젝트에서 동작할 수 있도록 스프링 시큐리티 설정을 수정 => SecurityConfig.java
	-상단에 @EnableMethodSecurity(prePostEnabled = true) 적용
	-만약, 이 적용을 안해주면 @PreAuthorize 애너테이션을 붙여봤자 설정이 되지 않음
	-따라서, 뭔가 @PreAuthorize 애너테이션이 제대로 동작하지 않는다 생각이 들면 설정 파일 상단에 적용이 되었는지 체크