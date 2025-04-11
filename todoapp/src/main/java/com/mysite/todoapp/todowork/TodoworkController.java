package com.mysite.todoapp.todowork;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.member.MemberService;
import com.mysite.todoapp.todotitle.Todotitle;
import com.mysite.todoapp.todotitle.TodotitleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todowork")
public class TodoworkController {
	
	private final TodotitleService todotitleService;
	private final TodoworkService todoworkService;
	private final MemberService memberService;
	
	@PostMapping("/create/{id}")
	public String createTodowork(
			Model model,
			@PathVariable("id") Integer id,
			@Valid TodoworkCreateForm todoworkCreateForm,
			BindingResult bindingResult,
			Principal principal
	){
		//상세 페이지에서 todotitle 객체가 필요하기 때문에 넘어온 id 값에 맞는 객체를 Model에 담아서 전달
		Todotitle todotitle = todotitleService.getTodotitle(id);
		
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("todotitle", todotitle);
			return "todotitle_detail";
		}
		
		Member member = memberService.getMember(principal.getName());
		
		//Todotitle todotitle = todotitleService.getTodotitle(id);
		todoworkService.create(todotitle, todoworkCreateForm.getContent(), member);
		
		return String.format("redirect:/todotitle/detail/%s", id);
	}
	
	/* 기존 코드1
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createTodowork(
			Model model,
			@PathVariable("id") Integer id,
			@RequestParam(value="content") String content,
			Principal principal
	){
		Member member = memberService.getMember(principal.getName());
		
		Todotitle todotitle = todotitleService.getTodotitle(id);
		todoworkService.create(todotitle, content, member);
		
		return String.format("redirect:/todotitle/detail/%s", id);
	}
	*/
}
