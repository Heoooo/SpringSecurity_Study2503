package com.mysite.todoapp.todowork;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.todoapp.DataNotFoundException;
import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.todotitle.Todotitle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodoworkService {
	
	private final TodoworkRepository todoworkRepository;
	
	public void create(Todotitle todotitle, String content, Member member) {
		
		//객체 생성
		Todowork tw =new Todowork();
		
		//넘어온 값으로 객체 셋팅
		tw.setContent(content);
		tw.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS")));
		tw.setTodotitle(todotitle);
		tw.setWriter(member);
		
		//Save
		todoworkRepository.save(tw);
	}
	
	//getTodowork() => ID 값에 맞는 Todowork 객체 하나 가져오기
	public Todowork getTodowork(Integer id) {
		
		//ID 값으로 검색 => findById()
		Optional<Todowork> todowork = todoworkRepository.findById(id);
		
		//존재하면
		if(todowork.isPresent()) {
			return todowork.get();		
		}
		else {
			throw new DataNotFoundException("Todowork Not Found~!!");
		}
	}
	
	//Todowork Delete
	public void delete(Todowork todowork) {
		todoworkRepository.delete(todowork);
	}
}
