package com.mysite.todoapp.todotitle;

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
	
}
