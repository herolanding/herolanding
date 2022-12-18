package com.hero.herolanding.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hero.herolanding.domain.Board;
import com.hero.herolanding.domain.Member;
import com.hero.herolanding.domain.Reply;
import com.hero.herolanding.dto.BoardDTO;
import com.hero.herolanding.dto.ReplyDTO;
import com.hero.herolanding.dto.SendDTO;
import com.hero.herolanding.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
//===================== 글 작성 ===============================
	@GetMapping("/board/write")
	public String write_form(Model model)
	{	
		model.addAttribute("boardDTO" , new BoardDTO());
		return "board/board_write";
	} // 글 작성 Form으로 이동 시켜주는 로직
	
	@PostMapping("/board/write")
	public String Board_write(BoardDTO dto , BindingResult bindingResult)
	{
		Board board = new Board();
		Member member = new Member(); // 추후에 로그인한 아이디가 있다면 아이디를 통해 멤버 정보를
									  // 가져와서 입력해주면 됨
		if(!StringUtils.hasText(dto.getPost_title()))
		{
			bindingResult.addError(new FieldError("dto", "post_title", dto.getPost_title(), false, null, null , "제목은 필수입니다."));
		} // 제목 검사 ( 값을 입력했는지 검사 )
		
		if(!StringUtils.hasText(dto.getPost_content()))
		{
			bindingResult.addError(new FieldError("dto", "post_content", dto.getPost_content(), false, null, null , "내용은 필수입니다."));
		} // 내용 검사 ( 값을 입력했는지 검사 )
		
		if(dto.getContinent() == null)
		{
			bindingResult.addError(new FieldError("dto", "continent", null, false, null, null , "여행지역을 입력해주세요"));
		} // 지역 선택했는지 검사
		
		if(dto.getWriteType() == null)
		{
			bindingResult.addError(new FieldError("dto", "writeType", dto.getWriteType(), false, null, null , "게시판을 선택해주세요"));
		} // 게시판 선택했는지 검사
		
		
		if(bindingResult.hasErrors()) return "board/board_write"; // 에러가 있는 경우 입력폼으로 다시 이동
		
		board.setBoardTitle(dto.getPost_title()); // 사용자가 입력한 제목
		board.setBoardContents(dto.getPost_content()); // 사용자가 입력한 내용
		board.setBoardCount(0L); // 조회수는 0
		board.setBoardType(dto.getWriteType()); // 사용자가 선택한 게시판 종류
		board.setContinent(dto.getContinent()); // 사용자가 선택한 대륙 종류
		board.setInsertDate(LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))); // 지금 시간을 포맷해서 넣어줌
		board.setReportCount(0L); // 신고 횟수는 0
		board.setUpdateDate(""); // 게시글 작성이기 때문에 update 날짜는 NULL
//		board.setMember(member);
		boardService.inputBoardContents(board);
		
		return "redirect:/";
	} // 작성한 글 DB에 저장하기 위한 로직
	
//===================== 리스트 불러오기 ===============================
	
	@GetMapping("/board/list")
	public String board_list(Model model)
	{	
		model.addAttribute("list" , boardService.findAll());
		return "board/board_list";
	} // 전체 글 리스트를 보여주기 위한 로직
	
	@GetMapping("/board/{id}/view")
	public String board_detail(@PathVariable("id") Long boardId , Model model)
	{	
		Board board = boardService.findById(boardId);
		List<Reply> comments = boardService.getComments(boardId);
		model.addAttribute("comments", comments);
		model.addAttribute("board", board);
		return "board/board_view";
	} // 아이디를 통해 게시물을 보기 위한 로직
	
	
//===================== 게시물 삭제 ===============================
	@GetMapping("/board/{id}/delete")
	public String boardDelte(@PathVariable("id")Long boardId)
	{	
		boardService.removeById(boardId);
		return "redirect:/board/list";
	} // 아이디를 통해 삭제하기 위한 로직
	
//===================== 게시물 수정 ===============================
	@GetMapping("/board/edit/{id}")
	public String editForm(Model model, @PathVariable("id")Long boardId)
	{	
		model.addAttribute("board", boardService.findById(boardId));
		model.addAttribute("boardDTO", new BoardDTO());
		return "board/board_edit";
	} // 수정 Form 으로 이동하기 위한 로직
	
	@PostMapping("/board/edit/{id}")
	public String edit(@PathVariable("id")Long boardId , BoardDTO boardDTO)
	{	
		boardDTO.setModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		boardService.update(boardId,boardDTO);
		return "redirect:/board/list";
	} // 업데이트를 위한 로직
	
	
	@RequestMapping(value = "/board/comment", method = RequestMethod.POST)
	@ResponseBody
	public void writeComment(Model model, SendDTO dto)
	{	
		dto.setWrite_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		Reply reply = new Reply();
		Board board = new Board();
//		Member member = new Member(); // 어떤 사람이 글을 썻는지 알기 위한 변수
		board.setBoardNum(dto.getBoardId()); // 어떤 게시판에 글을 썻는지 알기 위한 변수
		
		reply.setReplyContent(dto.getResult()); // 댓글 내용
		reply.setReplyDepthLevel(1); // 답글 구현 아직 안해서 depth는 무조건 1
		reply.setReplyInsertDate(LocalDateTime.now().
				format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))); // 댓글 작성 시간
		reply.setReplyUpdateDate(""); // 업데이트 날짜는 null
		reply.setBoard(board); // board 입력
//		reply.setMember(member); // member입력
		
		boardService.inputComment(reply);
	} // 댓글 짜는 로직입니다 .AJAX 사용이기 때문에 따로 return 값없고 db에 댓글만 입력합니다.
	
	@RequestMapping(value = "/board/comment_update" , method = RequestMethod.POST)
	@ResponseBody
	public void updateComment(ReplyDTO dto)
	{
		System.out.println(dto);
	}
	
}