package com.mysite.todoapp.member;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.todoapp.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	public Member create(String username, String password, String email) {
		
		//객체 생성
		Member member = new Member();
		
		//넘어온 값으로 객체 세팅
		member.setUsername(username);
		member.setEmail(email);

		//패스워드 암호화
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		member.setPassword(passwordEncoder.encode(password));
		
		memberRepository.save(member);
		
		return member;
	}
	
	//회원 한 명에 대한 정보 가져오기
	public Member getMember(String username) {
		
		Optional<Member> member = memberRepository.findByUsername(username);
		
		if (member.isPresent()) {
			return member.get();
		}
		else {
			throw new DataNotFoundException("Member Not Found!");
		}
	}
}
