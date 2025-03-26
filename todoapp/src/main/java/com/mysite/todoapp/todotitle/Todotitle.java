package com.mysite.todoapp.todotitle;

import java.util.List;

import com.mysite.todoapp.todowork.Todowork;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Todotitle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	//@Column 에너테이션은 content 필드가 Entity와 연결된 데이터베이스 테이블 열에 매핑되어야 함을 의미
	//columnDefinition = "TEXT"는 데이터베이스 열 정의를 위한 특정 지침을 제공->TEXT로 설정
	//이 컬럼이 많은 양의 텍스트 데이터를 저장할 수 있음을 의미->DB에 따라 TEXT 열 크기 제한 상이
	
	private String createDate;
	//private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "abc", cascade = CascadeType.REMOVE)
	private List<Todowork> todoworkList;
	//어떤 두 Entity 간의 일대다 관계 정의
	//즉, todoworkList 필드가 데이터베이스 관련 테이블에 어떠헥 매핑되어야 하는지를 지정
}
