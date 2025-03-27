package com.mysite.todoapp.todotitle;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todotitle")
public class TodotitleController {
	
	private final TodotitleService todotitleService;
	
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
	public String todotitleCreate() {
		
		return "todotitle_form";
	}
	
	//입력 페이지(POST) => DB Save
	@PostMapping("/create")
	public String todotitleCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
		
		//DB 저장
		todotitleService.create(subject, content);
		
		return "redirect:/todotitle/list";
	}
	
	//상세 페이지
	
}
