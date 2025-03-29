package com.mysite.todoapp.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	
	//로그인 요청->/member/login
	@GetMapping("/member/login")
	public String login() {
		
		return "login_form";
	}
	
	//회원가입 요청->/member/signup
	@GetMapping("/member/signup")
	public String signup() {
		
		return "signup_form";
	}
}
