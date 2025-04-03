package com.mysite.todoapp.member;

import lombok.Getter;

@Getter
public enum MemberRole {
	
	//enum => class처럼 생각 + 상수(클래스에 상수를 추가)
	//자바는 열거 타입을 이용하여 변수를 선언할 때 변수 타입으로 사용이 가능
	//열거형 이전에는 static final 상수 등을 이용하여 사용
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	//생성자
	//생성자를 사용하면 => 이제 ADMIN의 "ROLE_ADMIN"은 생성자 value 값이 되어진다.
	//좀 더 상수를 구체화하고 있다고 생각하면 된다.
	//기본적으로 상수 마다 하나씩 만들어질 때 자동으로 호출이 되어진다 생각하고 변수들도 상수 마다 셋팅되어졌다 생각
	//메소드도 넣을 수 있는데 상황에 따라 롬복을 사용하여 가져오는 것만 구현할 때는 @Getter만 작성
	//기본적으로 java.lang.Enum을 상속하므로 Enum의 메소드 사용이 가능
	//상수들을 배열로 가져와서 사용도 가능
	MemberRole(String value) {
		this.value = value;
	}
	
	//변수 선언
	private String value;
	//로그인 시 해당 멤버 객체한테 권한을 부여할 때는 => MemberRole.ADMIN.getValue() or MemberRole.USER.getValue()
}
