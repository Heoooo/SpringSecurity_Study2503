package com.mysite.todoapp.todowork;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoworkCreateForm {

	@NotEmpty(message = "관련된 할 일 Todowork 입력은 필수입니다.")
	private String content;
	
}
