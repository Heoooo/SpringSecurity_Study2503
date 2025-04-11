package com.mysite.todoapp.todotitle;

import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todotitle")
public class TodotitleController {
	
	private final TodotitleService todotitleService;
	private final MemberService memberService;
	
	//목록 페이지
	@GetMapping("/list")
	public String list(Model model) {
		
		//목록 리스트 가져오기
		List<Todotitle> todotitleList = todotitleService.getList();
		model.addAttribute("todotitleList", todotitleList);
		
		return "todotitle_list";
		
	}
	
	//입력 페이지(GET)
	@GetMapping("/create")
	public String todotitleCreate(TodotitleCreateForm todotitleCreateForm) {
		
		return "todotitle_form";
	}
	
	//입력 페이지(POST) => DB Save
	@PostMapping("/create")
	public String todotitleCreate(
			@Valid TodotitleCreateForm todotitleCreateForm,
			BindingResult bindingResult,
			Principal principal
			) {
		
		//에러 발생하면
		if (bindingResult.hasErrors()) {
			return "todotitle_form";
		}
		
		//데이터베이스에 저장하기 전에 principal 객체로부터 필요한 정보를 받아 입력 메소드에 전달(Member)
		Member member = memberService.getMember(principal.getName());
		
		//데이터베이스에 저장
		todotitleService.create(todotitleCreateForm.getSubject(), todotitleCreateForm.getContent(), member);
		
		return "redirect:/todotitle/list";
	}
	
	
	/* 기존 코드2
	@PostMapping("/create")
	public String todotitleCreate(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="content") String content,
			Principal principal
		) {
		
		//데이터베이스 저장하기 전 principal 객체로부터 필요한 정보를 받아 입력 메소드에 전달(Member)
		Member member = memberService.getMember(principal.getName());
		
		//DB 저장
		todotitleService.create(subject, content, member);
				
		return "redirect:/todotitle/list";
	}
	 */
	
	
	/* 기존 코드1
	@PostMapping("/create")
	public String todotitleCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
		
		//DB 저장
		todotitleService.create(subject, content);
		
		return "redirect:/todotitle/list";
	}
	*/
	
	//상세 페이지
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		
		//ID 값으로 타이틀 한 개 가져오기
		Todotitle todotitle = todotitleService.getTodotitle(id);
		model.addAttribute("todotitle", todotitle);
		
		return "todotitle_detail";
	}
}
