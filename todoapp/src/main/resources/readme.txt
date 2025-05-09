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
	

템플릿 페이지단에서 글 등록 제한하기
1.
기본적으로 템플릿 페이지에서 한다면?
	-disabled 이용
	-sec:authorize="isAnonymous()" vs sec:authorize="isAuthenticated()" 이용
	-isAnonymous() => 너 지금 로그아웃 상태구나.. 의미
	-isAuthenticated() => 로그인 상테 체크 => 즉, 너 지금 로그인 상태구나.. 의미
2.
필요한 파일들? (관련된 할 일 등록하는 상황일 때)
	-TodoworkController.java => 해당 메소드 위에 @PreAuthorize 적용
	-SecurityConfig.java => 상단에 @EnableMethodSecurity 적용
	-todotitle_detail.html 수정

	
폼 클래스를 사용하여 입력값 체크하기 (앞서 배운 내용 복습)
1.
필요한 파일들
	1) build.gradle 파일 수정 필요
		-implementation 'org.springframework.boot:spring-boot-starter-validation'	
	2) TodotitleCreateForm.java 폼 클래스 생성	
	3) TodotitleController.java 폼 클래스 전달	
	4) todotitle_form.html 템플릿 페이지 수정
2.
폼 클래스 만들기
	- TodotitleCreateForm 새엇ㅇ
	- 여러 다양한 애너테이션을 사용하여 검증 체크 => @NotEmpty, @Size...
3.
컨트롤러에 해당 폼클래스 객체 전송
	-@Valid TodotitleCreateForm todotitleCreateForm, BindingResult bindingResult
	-에러가 발생하면
		if (bindingResult.hasErrors()) {
			return "todotitle_form";
		}
4.
템플릿 페이지 수정
	-<form th:action="@{...}" th:object="${todotitleCreateForm}" method="POST">
	-상단에 에러 출력
	<div class="alert alert-danger" role="alert" th:if="${#fields.hasAnyErrors()}">
		<div th:each="err : ${#fields.allErrors()}" th:text="${err}"/>
	</div>
5.
에러 발생 주의
	-템플릿 페이지에서 form 태그쪽에 th:object 속성 => 이 속성을 추가했기 때문에 에러 발생 주의!
	-todotitleCreateForm과 같이 매개변수로 바인딩한 객체는 Model 객체로 전달하지 않아도 템플릿에서 사용이 가능
	-따라서, @GetMapping("/create") 요청 시 전달값으로 todotitleCreateForm 객체를 전달하는게 필요
	-required 삭제하고 테스트
	-에러 내용 출력 후 폼에 입력한 내용이 사라지는 문제?
		해당 폼에 th:field="*{subject}" 등 필드 속성을 사용하여 변경
		content 폼도 마찬가지로 작업
		
		
Spring Boot Validation 라이브러리
사용자 입력 값 검증 시 사용할 수 있는 애너테이션 종류
	@Size : 입력 문자 길이 제한할 때 사용 => min, max 사용
	@NotEmpty : 허용하지 않는 것 => Null, 빈 문자열("") / 허용하는 것 => 공백(" ")	
	@NotNull : 허용하지 않는 것 => Null / 허용하는 것 => 빈 문자열(""), 공백(" ")
	@NotBlank : 허용하지 않는 것 => Null, 빈 문자열, 공백
	@Null : Null만 허용
	
	@Past : 현재 기준으로 과거 날짜만 입력이 가능
	@Future : 현재 기준으로 미래 날짜만 입력이 가능
	@FutureOrPresent : 현재 기준으로 미래 또는 오늘 날짜만 입력이 가능
	@PastFutureOrPresent : 현재 시간이거나 과거의 날짜, 시간이어야 함
	
	@Max : 설정한 최댓값 이하로만 입력이 가능하도록 제한 => @Max(value=)
	@Min : 설정한 최솟값 이상으로만 입력이 가능하도록 제한 => @Min(value=)
	
	@Positive : 값을 양수로 제한
	@Negative : 값을 음수로 제한
	@PositiveOrZero : 값을 양수와 0만 가능하도록 제한
	@NegativeOrZero : 값을 음수와 0만 가능하도록 제한
	
	@Email : 이메일 형식을 검사 => 단, 빈 문자열("")인 경우는 통과(보통 이메일 경우 @Pattern 애너테이션을 통한 정규식 검사를 많이 사용)
	@Pattern : 입력 값을 정규식 패턴으로 검증하고자 할 때 사용 => 이메일, 패스워드 규칙 정할 때 많이 사용 => @Pattern(regexp= )
	
정리 및 몇 가지 애너테이션 실습 
	-아이디 입력하거나 패스워드 입력 같은 경우 => 공백이 들어가면 안되니깐 => @NotBlank도 많이 사용
	-닉네임이나 별명, 이메일 입력 같은 경우 => 특수 문자 제한을 주고자 하는 경우에는 => @Pattern을 통해 정규식 만들어 사용
	
	문제1: 입력은 영문, 숫자만 입력이 가능하고, 특수문자를 제외한 5~10 자리 내외로 입력합니다.
		@Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message =" ")
		
		-@Pattern : 필드에 대한 정규식 검사를 수행
		-regexp : 정규식 패턴을 지정
		-message : 검사를 실패할 경우 출력할 문구 지정
		-^ :  문자열 시작을 의미
		-$ : 문자열 끝을 의미
		-[a-zA-Z0-9] : 영문 대소문자와 숫자를 의미
		-{5,10} : 최소, 최대 길이를 결정 => 앞의 문자가 5번 이상 10번 이하로 반복되어야 함을 의미
		
	문제2: 기존 정규식에 특정 특수문자만 허용을 해주고자 한다면?
		문자 클래스에 해당 특수문자를 추가
		[a-zA-Z0-9_-] => 끝에 허용할 문자를 추가
		
		[이스케이핑 처리]
		이스케이프(Escape) : (v)탈출하다, 벗어나다, 달아나다 (n)탈출, 도망, 도피, 모면, 벗어남
		
		기본적으로 정규식 메타문자(.?-*{[^$+|\)는 어떤 특정한 의미를 가지고 있다.
		예를 들어, 점(.)은 이중적인 의미를 가지는데 => 대부분의 정규 표현식 엔진에서 점(.)은 모든 문자를 의미하는 특수 문자로 해석
		
		그래서 특정한 의미가 아닌 점(.)이라는 일반 문자 자체로써 사요되게끔 하고 싶을 때 => 이스케이핑 처리
		안해주면 정규식 패턴이 의도대로 검색을 하지 못하거나 문법적 오류가 발생할 수 있다.
		
		예를 들면, 다른 메타문자와의 중간에 위치하거나 끝에 위치해서 모호함이 발생하면 에러가 발생할 수 있다.
		그런 경우 이스케이핑 처리를 통해서 사용 => 백슬래시(\) 사용
		이때 \' 또는 \\ 이렇게 사용
		
		결론 => 이스케이핑 처리는 이중적 모호성을 없애고 문자 자체로 엔진이 해석하게 해주기 위해서 처리한다.
		
		[정규식 메타문자 정리]
			.(점) : 임의의 문자 하나를 의미
			
			* : 앞의 문자가 0번이상 반복됨을 의미
			+ : 앞의 문자가 1번 이상 반복됨을 의미
			? : 앞의 문자가 0번 또는 1번 반복됨을 의미
			
			{} : 반복 횟수를 지정
			[] : 문자 클래스를 의미
			() : 문자 그룹을 정의 => 이미지나 파일명에서 정보를 추출할 때 많이 사용
			
			^ : 문자 클래스 내에서 부정을 의미하거나 문자열의 시작을 의미
			$ : 문자열 끝 의미
			
			\d : 숫자를 의미 => [0-9]
			\s : 공백 문자를 의미
			 
	문제3 : 특정 문자열을 반드시 포함했는지 여부를 체크하려면?
		예) "문자열이 'bts'를 반드시 포함하고, 영문자 또는 숫자 5자리 이상 10자리 이하로만 구성된 문자열이어야 한다."
		
		[분석]
		.*bts.*와 같이 특정 문자열이 포함되어야 하는 조건을 추가
		^(?=.*bts.*)[a-zA-Z0-9_-]{5,10}$
		
		[긍정적 선형 탐색]
		긍정적 선형 탐색은 영어로 => Positive lookahead
		전방 탐색 또는 양성적(음성적)이라는 표현으로 부르느 경우도 있는데 일반적이지는 않음
		정규 표현식에서 특정 패턴이 뒤에 나타나는지를 확인하는 탐색 기능
		즉, 현재 위치에서 뒤를 미리 살펴보고 해당 패턴이 있는지 확인하는 것
		
		[예시로 구분해보기]
		bts12345(o)
		abcdefg(x)
		bts@123(x)
		abcdef_bts(o)
		bts(o)
	
	문제4 : 위 문제에서 대소문자 구분이 가능한가?
		기본적으로 대소문자를 구분 => 대소문자를 구분하지 않으려면 (?i) 옵션을 추가
		이때, 추가는 맨 앞에 해준다 => 그러면 해당 정규 표현식 전체에 대소문자를 구분하지 않는 옵션이 적용
		예) 대소문자를 구분하지 않고 "hello" 또는 "world"를 찾는 정규 표현식
		String regexp = "(?i)(hello|world)";
		
		[기타]
		(?i) 이 옵션은 정규 표현식 전체에 적용 => 특정 부분에서 적용하고 싶다면 좀 더 복잡한 정규식을 사용
		다른 정규 표현식 엔진이나 프로그래밍 언어에서는 (?i)와 같은 옵션의 표기법이 다를 수 있다.
		(?i) 옵션 사용은 => 대소문자를 구분하지 않는 검색을 수행할 때 유용 => 비번, 사용자 이름 등 별로 대소문자 구분이 필요없을 때 유용
		
	문제 5 : 비밀번호 전체가 숫자만으로 구성된 입력 값은 허용이 안되며, 최소한 영문자 한 개는 들어가야만 한다.
	(대소문자 고분 없음 / _-.?!* 이 6개 특수문자는 허용 / 자릿수는 최소 6에서 12까지 입력하는 것을 허용)
		원본 => ^[a-zA-Z0-9]{6,12}$
		수정 => ^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-.?!*]{6,12}$
		
		[분석]
		긍정적 선형 탐색(Positive lookahead) => 현재 위치에서 뒤쪽을 미리 살펴보고 특정 패턴이 있는지 확인하는 탐색 기능
		.* => 임의의 문자가 0개 이상 반복될 수 있음을 의미
		[a-zA-Z] => 영문자(대문자 또는 소문자) 하나를 의미
		
		결론1 => "뒤쪽에 반드시 영문자가 하나 이상 포함되어 있어야 한다" 라는 것을 의미
		결론2 => "반드시 영문자를 하나 이상 포함시켜서 숫자만으로 구성된 비밀번호 구성을 허용하지 않겠다."
	
	문제6 : 앞서 문제에서 만든 정규식에 공백을 허용한다면?
		원본 => ^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-.?!*]{6,12}$
		수정 => ^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-.?!*\s]{6,12}$
		
		[대소문자에 따른 정규식 메타문자 의미]
		\s : space를 표현하는 것이며, 공백 문자를 의미
		\S : non space를 표현하는 것이며, 공백 문자가 아닌 것을 의미
		
		\d : digit를 표현하는 것이며, 숫자를 의미
		\D : non digit를 표현하는 것이며, 숫자가 아닌 것을 의미
		
		\w : word를 표현하는 것이며, 알파멧 + 숫자 + _ 중의 한 문자임을 의미
		\W : non word를 표현하는 것이며, 알파멧 + 숫자 + _ 가 아니 것을 의미
		
		\t : tab 문자를 의미
		.(점) : 모든 문자를 의미 => 단, 줄 바꿈 문자 제외
		
		[\s 또는 \S 언제 사용하지?]
		공백이 있어야 할 때 사용
		문자열 내에서 공백을 허용하지 않을 때 그리고 특정 위치에 공백이 있어야 할 때 사용
		ex) 이메일, 전화번호, 핸드폰번호, 사용자이름, 비밀번호 ..
		
		["name 필드가 공백 없이 문자로만 이루어져야 한다"를 정규 표현식으로 만든다면?]
		@Pattern(regexp="^\\S+$")
		private String name;
		
		["name 필드가 숫자 없이 문자로만 이루어져야 한다"를 정규 표현식으로 만든다면?]
		@Pattern(regexp="^\\D+$")
		private String name;
		
	문제7 : 아래 예시를 보면서 의미하는 바가 무엇인지 말해보시오.
		[예시1]
		\s+ : 하나 이상의 공백 문자를 의미
		\S+ : 하나 이상의 공백 문자가 아닌 문자를 의미
		^\s+ : 문자열 시작 부분에 하나 이상의 공백 문자가 있는지 확인
		\S+$ : 문자열 끝 부분에 하나 이상의 공백 문자가 아닌 문자가 있는지 확인
		
		[예시2]
		영문자만 입력 가능하고, 문자열 시작은 반드시 공백 문자로 시작한다. => ^\s[a-zA-Z]+$
		추가적으로 숫자도 허용 => ^\s[a-zA-Z\d]+$
		추가적으로 특수문자로 대부분 허용 => ^\s[a-zA-Z\d\W]+$
		추가적으로 공백 여러 개를 허용 => ^\s+[a-zA-Z]+$
		추가적으로 최소/최대 길이를 제한한다면 => ^\s+[a-zA-Z]{3,10}$
		
	문제8 : 아래 5가지 조건을 만족하는 다소 복잡한 비밀번호 정규식을 만들어보시오.
		[조건]
		조건1 => 최소한 한 개의 영문자(대소문자 구분 없음)을 포함
		조건2 => 최소한 한 개의 숫자를 포함 => 이게 앞에서 한 비밀번호 정규식하고 다른 차이점
		조건3 => 최소한 한 개의 특수문자 포함 => !@#$%^&*() 이 정도만 사용 가능 => 번호 1번 부터 0번까지를 의미
		조건4 => 자릿수는 8자 이상 12자 이하로 구성
		조건5 => 숫자 표현은 0-9 사용하지 않고 다른 것을 사용 => \d
		
		[주의사항]
		일부 특수문자는 정규식에서 특별한 의미를 가지므로 백슬래시(\)로 이스케이핑 처리를 해줄 수 있다.
		
		[정규식]
		regexp="^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()])[a-zA-Z\d!@#$%^&*()]{8,12}$"
		
	문제9 : 앞서 만든 비밀번호 정규식에 "반드시 비밀번호 시작은 특수문자로 시작해야 한다"는 조건을 넣어서 다시 만들어보시오.
		
		원본 => regexp="^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()])[a-zA-Z\d!@#$%^&*()]{8,12}$"
		수정 => regexp="^[!@#$%^&*()](?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()]{7,11}$"
		
		[분석]
		반드시 맨 앞에 특수문자가 와야 하니까 탐색 기능 대신에 문자 클래스를 사용하여 정의하면 됨
		^[!@#$%^&*() => 이것은 이들 중 하나로 반드시 시작을 해야 함을 의미
		
		[주의사항]
		자릿수 조정을 조금 해주는게 좋음
		왜냐하면 비밀번호 첫 자리가 이미 특수문자로 확정되었기 때문에, 나머지 자릿수는 7~11로 줄어들기 때문
		만약 자릿수 조정을 해주지 않으면 => 13자리 비밀번호 입력도 가능
		
	문제10 : 앞서 만든 비밀번호 정규식에 "연속된 동일한 문자를 허용하지 않는다"는 조건을 넣어서 다시 만들어보시오. (다소 강력한 규칙이 되는 것이므로 주의)
		
		원본 => regexp="^[!@#$%^&*()](?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()]{7,11}$"
		수정 => regexp=""
		
		[전체 정규식 조건]
		비밀번호는 반드시 특수문자로 시작
		영문자, 숫자, 특수문자를 각각 하나 이상 포함
		연속해서 동일한 문자 사용할 수 없음
		비밀번호 길이는 8자리 이상 12자리 이하 => 최소 길이 계산 => 특수문자 1개 + 나머지 7게 = 총 8자
		
		[전후방 탐색 : Look-around]
		어떤 패턴의 직전에 위치하는 문자열이나 또는 직후에 위치하는 문자열을 찾고자 할 때 => 전후방 탐색 기능 사용
		전후방 탐색 => "전방탐색(lookahead)", "후방탐색(lookbehind)" 으로 분류 => 기호 뒤에 작성된 패턴을 앞쪽 또는 뒤쪽에서 탐색
				
		[전후방 탐색에 사용되는 기호]
		(?=...) => 긍정적 전방(선행) 탐색
		(?!...) => 부정적 전방(선행) 탐색		
		(?<=...) => 긍정적 후방 탐색		
		(?<!...) => 부정적 후방 탐색
		
		[전후방 탐색 대표 예]
		(?=...)
			긍정적 전방 탐색 => URL에서 http 또는 https 등의 프로토콜만 찾아내기
			http://www.loacalhost.com
			https://www.localhost.com
			ftp://127.0.0.1
			
			정규식 작성 => ^.+(?=:)
			정규식 설명 => : 직전까지의 한 글자 이상의 문자열 => 다만 테스트 사이트 등에서 테스트할 때 멀티라인 옵션인 m 등을 추가해서 서치해야 함
			
			[멀티라인 옵션 m을 사용하지 않고 정규식 작성]
			정규식 작성 => (http|https|ftp)(?=://) => 이러면 멀티옵션을 주지 않고도 서치 가능
			정규식 설명 => (?=://) => 긍정적 전방 탐색을 의미 => 탐색된 :// 문자(열)는 실제 매치 결과에는 포함되지 않는다.
			
		(?<=...)
			긍정적 후방 탐색 => URL에서 도메인 주소만 찾아내기
			http://www.localhost.com
			https://www.localhost.com
			ftp://127.0.0.1
			
			[방법1]
			정규식 작성 => (?<=://).+
			
			[방법2]
			정규식 작성 => (?<=\/{2}).+
			정규식 설명 => // 바로 뒤에 위치하는 한 글자 이상의 문자열을 매치 (//이 문자는 실제 매치 결과에 포함되지 않는다)
			
			[방법3] : 탐색된 문자열의 끝에 공백이 포함되어 있을 떼 => 이를 제거하는 방법 => \b
			정규식 작성 => (?<=\/{2}).+\b
			정규식 설명 => \b : 단어 경계를 의미(word boundary)
			
		[Negative Lookahead]
		명칭 => 부정적 전방 탐색 또는 부정형 전방 탐색 (전방 -> 선행)
		아무튼 핵심은 탐색 => 앞에서든 뒤에서는 일단 일치하는 텍스트를 찾는 것이 핵심
		
		부정적 전방 탐색(Negative Lookahead) => 앞쪽에서 지정한 패턴과 일치하지 않는 텍스트 서치
		부정적 후방 탐색(Negative Lookbehind) => 뒤쪽에서 지정한 패턴과 일치하지 않는 텍스트 서치 => 일치하지 않는 패턴을 이용해서 뒤쪽에서 서치
		
		부정적 탐색(전후방 모두)에서 부정형을 나타낼때 사용하는 기호 => 긍정적 탐색에서 사용하는 = 대신 => 느낌표(!) 사용
		결론 => 부정적 탐색 => 일치하지 않는 것을 검색 => 이 말은 특정 패턴이 없는 경우를 탐색
		
		[예시]
		(?<=...)
			긍정적 후방 탐색
			"나는 3일 전에 삼성 갤럭시 폰을 2대 샀는데 $1500 달러가 들었다. 30일 전에 내가 예상했던 비용 보다 $100 달러가 더 들었다."
			정규식 문제 => 일반 숫자는 찾지 않고 달러 기호가 붙어있는 숫자만 찾기
			정규식 작성 => (?<=\$)\d+
			
		(?<!...)
			부정적 후방 탐색
			"나는 3일 전에 삼성 갤럭시 폰을 2대 샀는데 $1500 달러가 들었다. 30일 전에 내가 예상했던 비용 보다 $100 달러가 더 들었다."
			정규식 문제 => 일반 숫자만 찾기
			정규식 작성 => \b(?<!\$)\d+\b
			
			[\b 기호(메타 문자)]
			1)\b(word boundary) 기호(메타 문자)는 무엇?
				이 기호는 정규 표현식에서 단어 경계를 의미
				즉, 단어와 단어 사이, 또는 단어와 다른 문자 사이의 경계를 의미
				더 쉽게 말해, 단어의 시작이나 끝을 가리키는 것 => 단어라고 함은 보통 영문자, 숫자, _ 등을 포함하는 문자열을 의미
				
			2) \b 기호 예시
				"cat"이라는 단어 자체만 서치 => "category"나 "concatenate"처럼 다른 단어 안에 포함된 "cat"은 찾지 않음 => \bcat\b
				문자열 처음에 "start"라는 단어가 있는 경우 서치 => ^\bstart\b
				문자열 끝에 "end"라는 단어가 있는 경우 서치 => \bend\b$
			
			3) \b 기호가 필요한 이유
				정확한 단어 매칭 => 다른 문자와의 혼동 없이 특정 단어만을 찾고 싶을 때 사용
				단어의 시작과 끝 지정 => 단어의 위치를 정확하게 지정하여 더 복잡한 패턴을 만들 수 있다.
			
			4) \b 기호를 안쓰면
				cat : cat category concatenate 등 "cat"이 포함된 모든 문자열 서치
				^cat : 문자열 처음에 "cat"으로 시작하는 모든 문자열 서치
				
			5) \b 기호 측징
				단어 사이의 경계뿐만 아니라, 문자열의 시작이나 끝도 단어 경계로 간주
				정규 표현식에서 \b 기호는 단어의 시작이나 끝을 나타내는 매우 유용한 메타 문자
				\B 기호하고 더불어 제법 유용하게 많이 사용
				
			[\B 기호는 무엇]
				기본적으로 앞에서 설명했듯이 대문자가 갖는 기본적인 의미는 => non => non-word boundary (비단어 경계)
				단어 경계가 아닌 위치를 의미
				"thelotteworld, theater, other, they"
					정규 표현식 : \Bthe\B (the 이 단어가 다른 단어 안에 완전히 포함되어 있을 때만 매치)
					매치되는 곳 : the (other의 the만 매치) => \B 기호를 사용하지 않으면 전부 매치
			
			[연속 문자 비허용 정규식 분석]
			^(?!.*(.)\1) => 부정적 전방 탐색(Negative Lookahead) => 이를 사용하여 특정 패턴이 없는 경우를 탐색
			.* : 임의의 문자 0개 이상
			(.) : 임의의 문자 하나를 캡쳐하여 그룹1에 저장
			\1 : 앞서 캡처한 그룹 1의 문자를 다시 참조
			
			즉, 어떤 문자든지 연속해서 두번 나타나는 경우가 없도록 패턴을 설정
			
			문제 : 연속된 동일한 영문자가 2번 이상 반복되는 것을 제외하고 선택하시오.
			정규식 => /(?!.*(.)\1)\w+/gm
			
			[위 정규식 설명]
			문자열의 처음부터 시작하여, 동일한 문자가 연속해서 나타나지 않는 문자들로만 이루어진 단어를 찾는다는 의미
			부정적 전방 탐색 부분은 "동일한 문자가 연속해서 나타나는 패턴"을 부정하기 때문에, 이를 해당하지 않는 문자열(연속하지 않는 문자)만 매치
			
			[백워드 참조]
			백워드 참조는 앞서 캡처한 그룹을 다시 참조하는 기능 => 괄호() 영역에 캡처한 그룹을 => \1, \2 등의 형태로 참조
			부정형 전방 탐색(Negative Lookahead) => (?!패턴) 형태로 사용하며, 패턴에 해당하는 문자(또는 문자열)가 없는 경우에만 매치
			
			위 정규식 => ^(?!.*(.)\1)[\w+!@#]+$
			
	문제10 : 앞서 만든 비밀번호 정규식에 "연속된 동일한 문자를 허용하지 않는다"는 조건을 넣어서 다시 만들어보시오. (다소 강력한 규칙이 되는 것이므로 주의)
		
		[전체 정규식 조건]
		비밀번호는 반드시 특수문자로 시작
		영문자, 숫자, 특수문자를 각각 하나 이상 포함
		연속해서 동일한 문자를 사용할 수 없음
		비밀번호의 길이는 8자리 이상 12자리 이하
		
		원본 => regexp="^[!@#$%^&*()](?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()]{7,11}$"
		수정 => regexp="^(?!.*(.)\1)[!@#$%^&*()](?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()]{7,11}$"
		
	
	문제11 : 정규식으로 연속된 동일한 문자가 3번 이상 반복되는 경우에 대해서 제한하려면?
		수정 => regexp="^(?!.*(.)\1{2})[!@#$%^&*()](?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()]{7,11}$"
	

	
레코드 수정 및 삭제 구현
	-Todotitle 레코드 한 개에 대한 수정 및 삭제 기능 구현
	
	[필요한 파일들]
	todotitle_detail.html
	todotitle_form.html (입력+수정) => 주의사항!
	
	Todotitle.java
	TodotitleController.java
	TodotitleService.java
	SecurityConfig.java => @PreAuthorize 메소드 실행을 위해 => @EnableMethodSecurity(prePostEnabled = true) => 안해주면 500번대 에러 발생 가능
	
	[수정 구현 단계]
	1. 수정 버튼 만들기 => 템플릿 페이지에서 수정 버튼 생성
	
	2. 엔티티(Todotitle) 파일 => 레코드가 수정된 날짜를 기록하기 위해 새로운 필드 추가
	
	3. 컨트롤러(Controller) 파일 => 수정 요청에 대한 처리
		-@GetMapping() 요청에 대한 처리
		-@{|/todotitle/modify/${todotitle.id}|}
		-수정 메소드 작성 => 이 때 3가지 인자 필요
			todotitleModify(
				TodotitleCreateForm todotitleCreateForm,
				@PathVariable("id") Integer id,
				Principal principal
			)
		-최종 단계에서 에러 처리 및 수정 후 리다이렉트 등에 대한 처리 필요 => @PostMapping() 요청에 대한 처리
		
	4. 수정에서 할 일들 => 즉, todotitleModify() 메소드에서 할 일
		-기존 Todotitle 정보를 가져와서 폼에 출력 => 기존 정보를 수정하기 위해서
		-템플릿 폼 페이지는 기존 입력 폼에서 사용하는 것을 그대로 사용 => 재활용
		-템플릿 재사용을 위해서 todotitleCreateForm 객체에 id 값으로 조회한 레코드의 제목(subject)과 내용(content)을 담아서 템플릿으로 전달
		
	5. 폼 페이지 템플릿 변경 => 입력과 수정에서 모두 사용하려고
		(1)
		-th:action="@{/todotitle/create}" => 이 부분을 삭제
		-삭제하는 이유 => 이 템플릿을 입력과 수정 시에 같이 사용하기 위해서 => 액션 주소 값을 삭제
		-form 태그 action 속성 특징 => 이 값이 없더라도 자동으로 현재 접속한 url을 기준으로 폼 값이 전송되는 규칙이 내부에 기본값으로 설정
			예) http://localhost:8080/todotitle/modify/3
			이렇게 수정하는 폼 페이지에 접속하면 action 속성에 /todotitle/modify/3과 같은 URL이 기본값으로 설정
		(2)
		-<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/> => 추가
			이유 : CSRF 이 값을 스프링 시큐리티가 반드시 필요로 하기 때문에
			
	6. 서비스 파일에 관련 메소드 추가
		-수정 메소드 추가 => modify()
		-public void modify(Todotitle todotitle, String subject, String content) {...} => save()
	
	7. 최종 컨트롤러(TodotitleController) 파일 수정
		-수정 관련한 에러 처리 및 수정 후 리다이렉트 등에 대한 최종적인 수정 마무리 => @PostMapping() 요청에 대한 처리
		-BindingResult, bindingResult.hasErrors() 사용하여 폼 관련 에러 발생 시 출력
		
		
	[삭제 구현 단계]
	삭제는 매우 쉽다. 그러나 어떻게 구현하느냐에 따라 생각해봐야 하는 것들은 조금 있다.
		-삭제할 때 비번을 입력해서 확인할 것인지? (여기서 작성자와 로그인 아이디가 일치하면 바로 삭제하는 방식)
		-자바스크립트 사용할 것인지?
		-todotitle 글 하나가 삭제될 때 여기에 딸린 글들도 같이 삭제가 되어지는지?
	1.필요한 파일들
		-todotitle_detail.html
		-layout.html (레이아웃 파일은 자바스크립트 코드[fragment]를 삽입해야 하기 때문에 작업이 필요할 수 있음)
		
		-TodotitleController.java
		-TodotitleService.java
		
	2. 삭제 버튼 만들기 => todotitle_detail
		-수정 버튼 만들 때와 똑같이 해주면 되나 float-end로 정렬 시 주의가 필요!
		-자바스크립트로 삭제 여부 확인하고자 한다면 해당 스크립트 코드가 필요
	
	3. 컨트롤러 작업 => TodotitleConroller
		-todotitleDelete() 메소드 작성
		-POST는 필요없고 GET 방식에 대해서만 처리해주면 끝	
		
	4. 서비스 작업 => TodotitleService
		-delete() 메소드 작성
		
	[Todowork 삭제 구현 단계]
	1. 체크 사항
		-일단 기존에 Todotitle 삭제를 구현할 때 삭제 스크립트를 작성해놓았기 때문에 다시 작성할 필요는 X
	
	2. 필요한 파일들
		-todotitle_detail.html
		
		-todoworkController.java
		-todoworkService.java
		
	3. 구현 순서
		-todowork 삭제 버튼 만들기 => todotitle_detail
		-컨트롤러 작업 => todoworkController => todoworkDelete() 메소드 작성
		-서비스 작업 => todoworkService => getTodowork() + delete() 메소드 작성