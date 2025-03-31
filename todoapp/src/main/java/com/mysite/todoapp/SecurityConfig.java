package com.mysite.todoapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
		;
		return http.build();
	}
	
}
