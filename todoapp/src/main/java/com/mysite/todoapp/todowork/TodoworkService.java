package com.mysite.todoapp.todowork;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.mysite.todoapp.todotitle.Todotitle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodoworkService {
	
	private final TodoworkRepository todoworkRepository;
	
	public void create(Todotitle todotitle, String content) {
		
		//객체 생성
		Todowork tw =new Todowork();
		
		//넘어온 값으로 객체 셋팅
		tw.setContent(content);
		tw.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS")));
		tw.setTodotitle(todotitle);
		
		//Save
		todoworkRepository.save(tw);
	}
	
}
