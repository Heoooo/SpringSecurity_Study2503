package com.mysite.todoapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@Configuration : 스프링 환경설정 파일임을 의미하는 애너테이션 => 여기서는 스프링 시큐리티 설정을 위해 사용
//@EnableWebSecurity : 모든 요청 URL이 스프링 시큐리티 프레임워크의 통제를 받게하는 애너테이션
//이 애너테이션을 사용하면 내부적으로 SpringSecurityFilterChain이 동작하면서 URL 필터가 적용
public class SecurityConfig {
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //protected 생략 가능
		
		//람다식 사용하여 Spring Security 인가 설정 시작
		//authorizeHttpRequests 메소드를 통해 권한 설정을 위한 객체를 생성하고, 람다식을 사용하여 여러가지 설정 내용 정의
		http
			.formLogin(
					(formLogin) -> formLogin
					.loginPage("/member/login")
					.defaultSuccessUrl("/todotitle/list")
			)
			.logout(
					(logout) -> logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
					.logoutSuccessUrl("/todotitle/list")
					.invalidateHttpSession(true)
			)
			.csrf(
				(csrf) -> csrf
				.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			)
			.headers(
				(headers) -> headers
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
			)
			//이 설정은 프레임 구조의 페이지에 접속할 때 필요하지만, 반드시 필수적인 것은 아니다.
			//X-Frame-Options 웹사이트가 다른 웹사이트에 프레임으로 포함될 수 있는지 여부를 제어하는 데 사용
			//SAMEORIGIN 모드로 설정하면 해당 웹 사이트가 같은 도메인에 속한 웹사이트에서만 프레임으로 포함될 수 있도록 설정
			//즉, 다른 도메인의 웹사이트에서는 해당 웹사이트를 프레임으로 포함할 수 없음을 의미
			//
			//H2 콘솔의 관리자 화면은 frame 구조로 작성되었고, 로그인 시 화면이 깨져 보일 수 있다.
			//스프링 시큐리티는 사이트내 콘텐츠가 다른 사이트에 포함되지 않도록 하기 위해 X-Frame-Options 헤더값 사용
			//외부 해킹 공격을 막기위해서도 사용
			//
			//보안 강화 측면에서 사용하기도 하지만 반대로 모든 도메인에서 포함을 허용한다면 필요하지 않을 수도 있음
			//결론적으로, 프레임 구조 페이지에 접속할 때 이 코드를 사용하는 것이 일반적으로 권장
			//하지만, 웹사이트의 특정 요구사항에 따라 이 코드가 필요하지 않을 수도 있다.
		
			
			//로그아웃 처리
			//-람다 표현식 => 로그아웃 설정을 위한 람다 표현식 사용
			//-logout 객체 => 매개변수로 전달되는 logout 객체는 로그아웃 설정을 위한 다양한 메소드 제공
			//-logoutRequestMatcher
			//	로그아웃 요청을 처리할 URL 패턴 지정, 지정된 경로로 들어오는 요청을 로그아웃 요청으로 인식 => /member/logout
			//-AntPathRequestMatcher
			//	URL 패턴 매칭을 위한 클래스로, 다양한 패턴을 지원 => 유연한 설정 가능
			//-logoutSuccessUrl(경로)
			//-invalidateHttpSession
			//	로그아웃 시 HTTP 세션을 무효화할지 여부를 설정, true로 설정하면 로그아웃 시 세션이 만료되어 사용자 상태 정보가 삭제
			
			.authorizeHttpRequests(
				(authorizeHttpRequests) -> authorizeHttpRequests
				
				//[1]
				//모든 요청(URL)을 매칭하도록 설정 => permitAll()
				//인증되지 않은 어떤 페이지의 요청도 허락한다는 뜻
				//쉽게 말해, 모든 사용자가 로그인 없이 어떤 페이지든 접근할 수 있도록 스프링 시큐리티의 권한 설정을 완전 열린 상태로 설정
				//.requestMatchers(new AntPathRequestMatcher("/member/**")).permitAll()
				//.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
				
				//[2]
				//페이지별 접근 권한(인가 or 허가) 설정
				//여기서 기본적으로 기억해야 하는 것 => 아무튼 접근 권한이 없는데 접근하려고 하면(설령 로그인했어도) 403 Forbidden 에러 발생
				//403 에러 대비용 템플릿 페이지를 만들어주는게 필요! (방법은 다양)
				//	-application.properties 파일에서 403 에러 페이지에 대한 설정 필요
				//	-이 때, 노란 밑줄(주의)가 나오면 툴에서 하라는대로 보통 해주면 처리 완료(보통은 거의 없음)
				//	-templates > error 폴더 생성 후 => 403.html 에러 페이지 작업을 해서 배치
				//
				//-첫 번째 권한 => 등록은 관리자만 가능하게끔 => 관리자 권한만 허용 => ~.hasAuthority("ROLE_ADMIN") 사용
				//-두 번째 권한 => 상세 페이지 내용은 최소한 인증(로그인)한 사용자만 볼 수 있게끔 허용 => 일반 유저 권한이 필요 => ~.authenticated() 사용
				//-세 번째 권한 => 그 외 나머지 페이지는 모두 허용 => ~.permitAll() 사용
				.requestMatchers(new AntPathRequestMatcher("/todotitle/create")).hasAuthority("ROLE_ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/todotitle/detail/**")).authenticated()
				.anyRequest().permitAll()
			)
			;
		return http.build();
	}
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	protected AuthenticationManager authenticationManger(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
