package com.mysite.todoapp.member;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	
	//로그인 요청->/member/login
	@GetMapping("/login")
	public String login() {
		
		return "login_form";
	}
	
	//회원가입 요청->/member/signup
	@GetMapping("/signup")
	public String signup(MemberCreateForm memberCreateForm) {
		
		return "signup_form";
	}
	
	
	//회원강입 폼 전송->데이터베이스 입력
	@PostMapping("/signup")
	public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
		
		//에러가 발생하면
		if(bindingResult.hasErrors()) {
			return "signup_form";
		}
		
		//패스워드 비교
		if(!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개 패스워드가 일치하지 않습니다.");
			return "signup_form";
			//첫 번째 파라미터 => 오류가 발생한 필드 이름
			//두 번째 파라미터 => 오류를 나타내는 코드
			//세 번째 파라미터 => 에러 메세지
			//rejectValue 메소드는 BindingReuslt 객체에 오류 정보를 추가
			//이 오류 정보는 나중에 프론트엔드(템플릿)에서 처리되어 사용자에게 출력
		}
		
		//데이터베이스 저장
		memberService.create(
			memberCreateForm.getUsername(),
			memberCreateForm.getPassword1(),
			memberCreateForm.getEmail()
		);
		
		return "redirect:/member/signup";
	}
}
