package com.mysite.todoapp.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService{
	
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//1.회원 검색 => findByUsername
		Optional<Member> m = memberRepository.findByUsername(username);
		
		//2.검색된 멤버가 없다면
		if(m.isEmpty()) {
			throw new UsernameNotFoundException("데이터베이스에 존재하는 회원이 없습니다.");
		}
		
		//3.검색된 멤버가 있다면
		Member member = m.get();
		
		//4.권한 부여를 위한 리스트 생성
		List<GrantedAuthority> authorities = new ArrayList<>();
		//이 리스트는 스프링 시큐리티 내부에서 사용자 권한을 나타내는 GrantedAuthority 객체를 보관하기 위한 것
		//GrantedAuthority 타입은 사용자 보안 권한을 캡슐화하는 객체 의미
		//쉽게 말해서 => 이 한줄의 코드는 GrantedAuthority 객체를 보관할 빈 컨테이너를 설정
		//이 컨테이너는 나중에 사용자의 특정 역할 및 권한을 기반으로 채워진다.
		//이러한 역할 및 권한은 애플리케이션 내에서 접근 제어 메커니즘을 강제 구현하는데 있어서 매우 중요
		//특정 사용자가 승인된 리소스와 기능, 페이지에만 접근할 수 있도록 구현
		
		//5. admin 사용자인 경우 관리자 권한 부여하고, 그 외의 일반 유저 권한 부여 => ~.add() + MemberRole.java
		
		
		//6.사용자 반환 => (1)사용자 이름 (2)비밀번호 (3)권한
		return new User(member.getUsername(), member.getPassword(), authorities);
	}
}
