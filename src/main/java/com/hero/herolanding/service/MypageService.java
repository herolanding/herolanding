package com.hero.herolanding.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hero.herolanding.domain.Board;
import com.hero.herolanding.domain.Member;
import com.hero.herolanding.repository.LoginRepository;
import com.hero.herolanding.repository.MypageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {

	private final MypageRepository mypageRepository;
	private final LoginRepository loginRepository;

//--------<updateMemberData() / 마이페이지 회원정보 수정 메서드>-------------------------------------------------------------------------------------	
	@Transactional
	public Member updateMemberData(Member updateMemberData) {

		// 앞단에서 가져온 데이터로 수정메서드 사용
		mypageRepository.updateMemberData(updateMemberData, updateMemberData.getMemberId());

		// id값은 변하지 않으므로 앞단에서 가져온 id값으로 수정된 데이터를 불러와 객체에 넣어줌.
		Member NewMember = loginRepository.findByLoginId(updateMemberData.getMemberId());

		return NewMember;
	}

//--------<updateMemberData() / 마이페이지 회원정보 수정 메서드>-------------------------------------------------------------------------------------	
	@Transactional
	public void deleteMember(String loginId) {
		
		System.out.println("S" + loginId);
		
		mypageRepository.deleteMemberData(loginId);
	}
	
//--------<BoardCountByMemberId() / 해당 아이디로 작성한 모든 게시글들을 리턴하는 메서드>-------------------------------------------------------------------------------------	
	@Transactional
	public List<Board> BoardCountByMemberId(String loginId){
		System.out.println("22222222222");
		System.out.println("ID : "+loginId);
		System.out.println("service : "+mypageRepository.BoardCountByMemberId(loginId).size());
		return mypageRepository.BoardCountByMemberId(loginId);
	}
	

} // MypageService class
