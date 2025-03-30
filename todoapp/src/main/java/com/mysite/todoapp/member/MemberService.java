package com.mysite.todoapp.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public Member create(String username, String password, String email) {
		
		//객체 생성
		Member member = new Member();
		
		//넘어온 값으로 객체 세팅
		member.setUsername(username);
		member.setEmail(email);

		//패스워드 암호화
		member.setPassword(passwordEncoder.encode(password));
		
		memberRepository.save(member);
		
		return member;
	}
	
}
