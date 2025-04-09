package com.mysite.todoapp.todowork;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.member.MemberService;
import com.mysite.todoapp.todotitle.Todotitle;
import com.mysite.todoapp.todotitle.TodotitleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todowork")
public class TodoworkController {
	
	private final TodotitleService todotitleService;
	private final TodoworkService todoworkService;
	private final MemberService memberService;
	
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
}
