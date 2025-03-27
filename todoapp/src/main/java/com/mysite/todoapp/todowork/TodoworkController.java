package com.mysite.todoapp.todowork;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.todoapp.todotitle.Todotitle;
import com.mysite.todoapp.todotitle.TodotitleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todowork")
public class TodoworkController {
	
	private final TodotitleService todotitleService;
	private final TodoworkService todoworkService;
	
	@PostMapping("/create/{id}")
	public String createTodowork(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) {
		
		Todotitle todotitle = todotitleService.getTodotitle(id);
		todoworkService.create(todotitle, content);
		
		return String.format("redirect:/todotitle/detail/%s", id);
	}
}
