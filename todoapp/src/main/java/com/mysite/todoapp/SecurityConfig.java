package com.mysite.todoapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //protected 생략 가능
		
		//람다식 사용하여 Spring Security 인가 설정 시작
		//authorizeHttpRequests 메소드를 통해 권한 설정을 위한 객체를 생성하고, 람다식을 사용하여 여러가지 설정 내용 정의
		http
			.csrf(
				(csrf) -> csrf
				.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			);
		return http.build();
	}
	
}
