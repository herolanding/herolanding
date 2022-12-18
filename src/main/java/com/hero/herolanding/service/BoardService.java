package com.hero.herolanding.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hero.herolanding.domain.Board;
import com.hero.herolanding.domain.Reply;
import com.hero.herolanding.dto.BoardDTO;
import com.hero.herolanding.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	
	private final BoardRepository boardRepository;
	
	@Transactional
	public void inputBoardContents(Board board)
	{
		boardRepository.inputBoardContents(board);
	}
	
	@Transactional
	public List<Board> findAll()
	{
		return boardRepository.FindAll();
	}
	
	@Transactional
	public Board findById(Long boardId)
	{	
		Board board = boardRepository.findById(boardId);
		board.updateViewCount(board.getBoardCount());
		return board;
	}

	@Transactional
	public void removeById(Long boardId)
	{
		boardRepository.removeById(boardId);
	}
//	
	@Transactional
	public List<Reply> getComments(Long boardId)
	{
		return boardRepository.getComments(boardId);
	}
	
	@Transactional
	public void inputComment(Reply reply)
	{
		boardRepository.inputComment(reply);
	}
//	
//	@Transactional
//	public List<Board> findByType(String type)
//	{
//		return boardRepository.findByType(type);
//	}
//	
	@Transactional
	public void update(Long boardId , BoardDTO updateBoard)
	{
		boardRepository.update(boardId, updateBoard);
	}
}