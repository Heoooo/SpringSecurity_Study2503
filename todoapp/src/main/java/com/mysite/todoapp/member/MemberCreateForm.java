package com.mysite.todoapp.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {
	
	@Size(min=3, max=25)
	@NotEmpty(message="ID 입력은 필수입니다.")
	private String username;
	
	@NotEmpty(message="비밀번호 입력은 필수입니다.")
	private String password1;
	
	@NotEmpty(message="비밀번호 확인은 필수입니다.")
	private String password2;
	
	@NotEmpty(message="이메일 입력은 필수입니다.")
	@Email
	private String email;
}
