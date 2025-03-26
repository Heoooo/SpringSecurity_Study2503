package com.mysite.todoapp.todotitle;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	//입력 페이지(POST) => DB Save
	
	//상세 페이지
	
}
