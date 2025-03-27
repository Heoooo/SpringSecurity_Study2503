package com.mysite.todoapp.todotitle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodotitleService {
	
	private final TodotitleRepository todotitleRepository;
	
	//getList() => 목록 리스트 전부 가져오기
	public List<Todotitle> getList() {
		List<Todotitle> todotitleList = todotitleRepository.findAll();
		
		return todotitleList;
	}
	
	//getTodotitle() => ID 값으로 검색해서 한 개 타이틀 가져오기
	
	
	//create() => 타이틀 제목, 내용을 데이터베이스에 입력하기
	public void create(String subject, String content) {
		
		//객체 생성
		Todotitle tt = new Todotitle();
		
		//넘어온 값으로 객체 셋팅
		tt.setSubject(subject);
		tt.setContent(content);
		tt.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS")));
		
		todotitleRepository.save(tt);
	}
	
}
