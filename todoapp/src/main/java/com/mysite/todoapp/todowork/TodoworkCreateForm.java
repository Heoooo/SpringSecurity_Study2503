package com.mysite.todoapp.todowork;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoworkCreateForm {
	
	@Size(min=5, max=20)
	@NotEmpty(message = "관련된 할 일 Todowork 입력은 필수입니다.")
	private String content;
	
}
