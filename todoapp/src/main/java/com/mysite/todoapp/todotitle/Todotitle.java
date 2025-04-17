package com.mysite.todoapp.todotitle;

import java.util.List;

import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.todowork.Todowork;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
	
	@OneToMany(mappedBy = "todotitle", cascade = CascadeType.REMOVE)
	private List<Todowork> todoworkList;
	//어떤 두 Entity 간의 일대다 관계 정의
	//즉, todoworkList 필드가 데이터베이스 관련 테이블에 어떠헥 매핑되어야 하는지를 지정
	//
	//@OneToMany
	//이 애너테이션은 todoworkList 필드가 일대다 관계를 나타냄을 의미
	//즉, 하나의 Entity 인스턴스는 다른 Entity의 여러 인스턴스와 연결될 수 있다는 것을 의미
	//이 애너테이션을 사용하여 정의된 todoworkList 필드는 실제 데이터베이스 테이블에 todoworkList 라는 별도 컬럼이 생성되지 않음
	//
	//mappedBy = "abc"
	//이 속성은 관계의 역방향 매핑 속성을 지정
	//Todowork 엔티티에서 소유 엔티티를 참조하는 필드를 의미 => Todowork쪽에 컬럼명이 abc_id 이런 식으로 생성됨을 의미
	//todoworkList 필드는 Todowork 엔티티의 abc필드와 매핑 => 즉, Todowork 엔티티의 abc 필드가 소유 엔티티를 참조
	//abc_id 필드 => 소유 엔티티를 참조하는 외래키 (소유 엔티티 테이블의 id 필드와 연결)
	//
	//cascade = CascadeType.REMOVE
	//소유 엔티티가 삭제되면 todoworkList의 해당 TodoWork 엔티티도 삭제되어야 함을 의미
	//
	//todoworkList 필드는 실제 테이블에 존재하지 않지만
	//todoworkList 필드를 사용하여 소유 엔티티의 인스턴스 목록에 엑세스하고 조작이 가능
	//JPA는 이렇듯 @OneToMany 애너테이션 매핑을 사용하여 두 엔티티 간 일대다 관계를 만들어주고 관리
	
	
	@ManyToOne
	//여기서는 로그인 한 사용자 한 명이 타이틀 여러 개를 등록할 수 있기 때문에 => @ManyToOne 애너테이션 사용
	//서버를 리스타트 하면 => 테이블에는 writer_id 형식으로 필드 생성
	//이 필드에는 말 그대로 작성자 아이디(고유넘버)가 저장 => 기존 데이터가 있는 상태라면 => null 입력
	private Member writer;
	
	private String modifyDate;
}
