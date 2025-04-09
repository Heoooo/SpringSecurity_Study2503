package com.mysite.todoapp.todowork;

import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.todotitle.Todotitle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Todowork {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String createDate;
	//private LocalDate createDate;
	
	@ManyToOne
	private Todotitle todotitle;
	
	@ManyToOne
	private Member writer;
}
