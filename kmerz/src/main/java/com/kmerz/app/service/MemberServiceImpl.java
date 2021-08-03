package com.kmerz.app.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmerz.app.dao.DeclaredDao;
import com.kmerz.app.dao.MemberDao;
import com.kmerz.app.dao.PointLogDao;
import com.kmerz.app.dto.MemberPagingDto;
import com.kmerz.app.vo.MemberVo;
import com.kmerz.app.vo.PointLogVo;

@Service
public class MemberServiceImpl implements MemberService {

	public static final int STATUS_DENY = -2; // 로그인 불가능
	public static final int STATUS_CLOSE = -1; // 탈퇴
	public static final int STATUS_ALLOW = 0; // 로그인 가능
	public static final int STATUS_WRITE_LOCK = 1; // 로그인 가능, 게시글 쓰기 불가능
	
	
	@Inject
	MemberDao memberDao;
	
	@Inject
	PointLogDao pointlogDao;
	
	@Inject
	DeclaredDao declaredDao;
	
	// 회원 가입
	@Transactional
	@Override
	public void joinMember(MemberVo memberVo) {
		final int point = 100;
		int user_no = memberDao.selectSeqUserNO();
		memberVo.setUser_no(user_no);
		memberVo.setUser_point(point);
		memberVo.setUser_totalpoint(point);
		memberDao.insertMember(memberVo);
		
		// 포인트 로그
		PointLogVo logVo = new PointLogVo();
		logVo.setUser_no(user_no);
		logVo.setPoint_content("회원가입");
		logVo.setPoint_score(point);
		logVo.setPoint_total(point);
		pointlogDao.insertPointLog(logVo);
		
	}

	// 모든 회원 검색
	@Transactional
	@Override
	public List<MemberVo> getAllMembers(MemberPagingDto memberPagingDto) {
		List<MemberVo> list = memberDao.selectAll(memberPagingDto);
		for(MemberVo vo : list) {
			// 유저 신고수
			int deCount = declaredDao.selectTargetUserCount(vo.getUser_no());
			vo.setUser_declared_count(deCount);
			// 유저 상태정보
			switch(vo.getUser_status()) {
			case STATUS_DENY:
				vo.setStr_user_status("이용 정지");
				break;
			case STATUS_CLOSE:
				vo.setStr_user_status("탈퇴");
				break;
			case STATUS_ALLOW:
				vo.setStr_user_status("승인");
				break;
			case STATUS_WRITE_LOCK:
				vo.setStr_user_status("글쓰기 정지");
				break;
			}
		}
		return list;
	}

	@Override
	public int getAllCount(MemberPagingDto memberPagingDto) {
		// 모든 유저 카운트
		return memberDao.selectAllCount(memberPagingDto);
	}
	
	
	// 로그인
	@Transactional
	@Override
	public MemberVo login(String user_id, String user_pw) {
		// 로그인 체크
		MemberVo memberVo = memberDao.selectUser(user_id, user_pw);
		
		if(memberVo != null) {
			Timestamp today = Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIDNIGHT));
			int user_no = memberVo.getUser_no();
			// 오늘 최초 로그인
			if(memberVo.getUser_currentlogin() != null) {
				//if(memberVo.getUser_currentlogin().after(today)) { // test
				if(memberVo.getUser_currentlogin().before(today)) {
					// 매일 첫 로그인 포인트
					final int DAILYPT = 10; 
					PointLogVo logVo = new PointLogVo();
					logVo.setUser_no(memberVo.getUser_no());
					logVo.setPoint_content("매일 첫 로그인 포인트");
					logVo.setPoint_score(DAILYPT);
					int preTotal = pointlogDao.selectPreTotal(user_no);
					logVo.setPoint_total(preTotal+DAILYPT);
					pointlogDao.insertPointLog(logVo);
					memberDao.updateUserPoint(user_no, DAILYPT);
				}
			}
			
			// 로그인 시간 업데이트
			memberDao.updateCurrentLogin(memberVo.getUser_no());
		}
		return memberVo;
	}

	@Override
	public MemberVo selectID(String user_id) {
		return memberDao.selectID(user_id);
	}

	@Override
	public int getUserIdCheckResult(String user_id) {
		int count = memberDao.selectUserIdCount(user_id);
		return count;
	}
	
	@Override
	public int getUserNameCheckResult(String user_name) {
		int count = memberDao.selectUserNameCount(user_name);
		return count;
	}

	@Override
	public void changeUserName(int user_no, String user_name) {
		memberDao.updateUserName(user_no, user_name);
	}

	@Override
	public MemberVo selectNO(int user_no) {
		// 유저 번호로 유저 정보 가져오기
		return memberDao.selectNO(user_no);
	}
	public void changeUserPw(int user_no, String newPw) {
		memberDao.updateUserPw(user_no, newPw);
	}

	@Override
	public void changeProfileImage(int user_no, String filePath) {
		memberDao.updateUserProfileImage(user_no, filePath);
	}

	@Override
	public void setStatusDeny(int user_no) {
		// 사용자 차단
		memberDao.updateUserStatus(user_no, STATUS_DENY);
	}
	
	@Override
	public void setStatusClose(int user_no) {
		// 회원 탈퇴
		memberDao.updateUserStatus(user_no, STATUS_CLOSE);
	}

	@Override
	public void setStatusAllow(int user_no) {
		// 사용자 허용
		memberDao.updateUserStatus(user_no, STATUS_ALLOW);
	}

	@Override
	public void setStatusWriteLock(int user_no) {
		// 사용자 쓰기 차단
		memberDao.updateUserStatus(user_no, STATUS_WRITE_LOCK);
	}

	

}
