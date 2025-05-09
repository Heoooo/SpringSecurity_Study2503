package com.mysite.todoapp.todotitle;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.todoapp.member.Member;
import com.mysite.todoapp.member.MemberService;
import com.mysite.todoapp.todowork.TodoworkCreateForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todotitle")
public class TodotitleController {
	
	private final TodotitleService todotitleService;
	private final MemberService memberService;
	
	//목록 페이지
	@GetMapping("/list")
	public String list(Model model) {
		
		//목록 리스트 가져오기
		List<Todotitle> todotitleList = todotitleService.getList();
		model.addAttribute("todotitleList", todotitleList);
		
		return "todotitle_list";
		
	}
	
	//입력 페이지(GET)
	@GetMapping("/create")
	public String todotitleCreate(TodotitleCreateForm todotitleCreateForm) {
		
		return "todotitle_form";
	}
	
	//입력 페이지(POST) => DB Save
	@PostMapping("/create")
	public String todotitleCreate(
			@Valid TodotitleCreateForm todotitleCreateForm,
			BindingResult bindingResult,
			Principal principal
			) {
		
		//에러 발생하면
		if (bindingResult.hasErrors()) {
			return "todotitle_form";
		}
		
		//데이터베이스에 저장하기 전에 principal 객체로부터 필요한 정보를 받아 입력 메소드에 전달(Member)
		Member member = memberService.getMember(principal.getName());
		
		//데이터베이스에 저장
		todotitleService.create(todotitleCreateForm.getSubject(), todotitleCreateForm.getContent(), member);
		
		return "redirect:/todotitle/list";
	}
	
	
	/* 기존 코드2
	@PostMapping("/create")
	public String todotitleCreate(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="content") String content,
			Principal principal
		) {
		
		//데이터베이스 저장하기 전 principal 객체로부터 필요한 정보를 받아 입력 메소드에 전달(Member)
		Member member = memberService.getMember(principal.getName());
		
		//DB 저장
		todotitleService.create(subject, content, member);
				
		return "redirect:/todotitle/list";
	}
	 */
	
	
	/* 기존 코드1
	@PostMapping("/create")
	public String todotitleCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
		
		//DB 저장
		todotitleService.create(subject, content);
		
		return "redirect:/todotitle/list";
	}
	*/
	
	//상세 페이지
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, TodoworkCreateForm todoworkCreateForm) {
		
		//ID 값으로 타이틀 한 개 가져오기
		Todotitle todotitle = todotitleService.getTodotitle(id);
		model.addAttribute("todotitle", todotitle);
		
		return "todotitle_detail";
	}
	
	
	//Todotitle 수정 처리(GET)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String todotitleModify(
			TodotitleCreateForm todotitleCreateForm,
			@PathVariable("id") Integer id,
			Principal principal
			) {
		//넘어온 ID 값에 맞는 객체 하나 반환받기
		Todotitle todotitle = todotitleService.getTodotitle(id);
		
		//조건문을 통해서 비교
		if(!todotitle.getWriter().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 레코드 수정 권한이 없습니다.");
		}
		
		//폼 페이지에 출력할 값 셋팅 (DB에서 가져온 값으로 셋팅)
		todotitleCreateForm.setSubject(todotitle.getSubject());
		todotitleCreateForm.setContent(todotitle.getContent());
		
		return "todotitle_form";
	}
	
	
	//Todotitle 수정 처리(POST)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String todotitleModify(
			@Valid TodotitleCreateForm todotitleCreateForm,
			BindingResult bindingResult,
			Principal principal,
			@PathVariable("id") Integer id
			) {
		//에러가 있으면 출력
		if (bindingResult.hasErrors()) {
			return "todotitle_form";
		}
		
		//넘어온 ID 값에 맞는 Todotitle 객체 하나 가져오기 => getTodotitle() 사용
		Todotitle todotitle = todotitleService.getTodotitle(id);
		
		//작성자 아이디와 로그인 아이디가 같은지 비교
		if (!todotitle.getWriter().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "레코드에 대한 수정 권한이 없습니다.");
		}
		
		//서비스 modify() 메소드 호출
		todotitleService.modify(todotitle, todotitleCreateForm.getSubject(), todotitleCreateForm.getContent());
		
		//다시 상세 페이지로 리다이렉트
		return String.format("redirect:/todotitle/detail/%s", id);
		
	}
	
	
	//Todotitle 삭제 처리(GET)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String todotitleDelete(Principal principal, @PathVariable("id") Integer id) {
		//넘어온 ID 값에 맞는 객체 하나 가져오기
		Todotitle todotitle = todotitleService.getTodotitle(id);
		
		//작성자 아이디와 로그인 아이디가 일치하는지 비교
		if(!todotitle.getWriter().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "레코드에 대한 삭제 권한이 없습니다.");
		}
		
		//삭제
		todotitleService.delete(todotitle);
		
		//다시 메인 페이지로 이동(리다이렉트)
		return "redirect:/todotitle/list";
	}
}
