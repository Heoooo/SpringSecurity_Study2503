package com.mysite.todoapp.member;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult, Model model) {
		
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
		
		try {
			//데이터베이스 저장
			memberService.create(
				memberCreateForm.getUsername(),
				memberCreateForm.getPassword1(),
				memberCreateForm.getEmail()
			);
			
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("idAlreadyExists", "이미 데이터베이스에 중복된 사용자입니다.");
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("idAlreadyExists", e.getMessage());
			return "signup_form";
		}
		//DataIntegrityViolationException
		//데이터베이스에 중복 데이터가 이미 존재할 경우 DataIntegrityViolationException이 예외 발생
		//bindingResult.reject(오류 코드, 오류 메시지) => MemberCreateForm 검증 오류 외 일반적 오류 발생시킬 때 사용
		//
		//reject() 첫 번째 옵션 값 => 오류 코드 또는 유효성 검사 식별자 역할
		//필수는 아니고 관련해서 의미있는 이름으로 명명 => idAlreadyExists
		//필수는 아니지만 명확하고 일관되게 사용하는 것이 좋음
		//
		//reject() vs rejectValue()
		//보통은 Spring 공식 문서에서도 rejectValue() 사용 권장
		//하지만 reject() 메소드 사용하는 경우도 있는데
		//1. 오류가 발생한 특정 필드를 알 수 없는 경우
		//2. 매우 간단한 오류 메시지만 필요한 경우
		//3. 이전 코드와의 호환성을 유지해야 하는 경우
		
		//return "redirect:/member/signup";
		model.addAttribute("alertMsg", "입력하신 정보로 회원가입이 완료되었습니다.");
		return "signup_form";
	}
}
