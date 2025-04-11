package com.mysite.todoapp.todotitle;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodotitleCreateForm {
	
	@NotEmpty(message = "Subject 항목은 필수 입력입니다.")
	@Size(max = 100)
	private String subject;
	
	@NotEmpty(message = "Content 항목은 필수 입력입니다.")
	private String content;
	
}
