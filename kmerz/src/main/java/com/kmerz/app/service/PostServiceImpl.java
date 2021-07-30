package com.kmerz.app.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmerz.app.dao.CategoryDao;
import com.kmerz.app.dao.CommunityDao;
import com.kmerz.app.dao.DeclaredDao;
import com.kmerz.app.dao.MemberDao;
import com.kmerz.app.dao.PostDao;
import com.kmerz.app.vo.CategoryVo;
import com.kmerz.app.vo.CommunityVo;
import com.kmerz.app.vo.MemberVo;
import com.kmerz.app.vo.PostsVo;
@Service
public class PostServiceImpl implements PostService{

	// 읽을수 있는 게시글 양수, 읽을 수 없는 게시글 음수
	private static final int POST_LOCK = -2;
	private static final int POST_DELETE = -1;
	private static final int POST_CREATE = 0;
	private static final int POST_UPDATE = 1;
	
	@Inject
	PostDao postdao;
	@Inject
	MemberDao memberDao;
	@Inject
	CommunityDao communityDao;
	@Inject
	CategoryDao categoryDao;
	@Inject
	DeclaredDao declaredDao;
	
	@Transactional
	@Override
	public List<PostsVo> selectAllPosts() {
		// 모든 게시글(관리자 페이지에서 필요함)
		List<PostsVo> PostsList = postdao.selectAllPosts();
		if(PostsList != null) {
			for(PostsVo postVo : PostsList) {
				// 유저 이름
				MemberVo memberVo = memberDao.selectNO(postVo.getUser_no());
				postVo.setUser_name(memberVo.getUser_name());
				// 커뮤니티 이름
				CommunityVo commVo = communityDao.getOneCommunity(postVo.getCommunity_id());
				postVo.setCommunity_name(commVo.getCommunity_name());
				// 카테고리 이름
				CategoryVo categoryVo = categoryDao.selectNO(postVo.getCategory_no()); 
				postVo.setCategory_name(categoryVo.getCategory_name());
				// 신고수
				int declared_count = declaredDao.selectTargetIDCount(postVo.getPost_no(), DeclaredServiceImpl.TYPE_POST);
				postVo.setDeclared_count(declared_count);
			}
		}
		return PostsList;
	}
	
	@Transactional
	@Override
	public List<PostsVo> selectAllowPosts() {
		// 승인된 모든 게시글 
		List<PostsVo> PostsList = postdao.selectAllowPosts(POST_CREATE);
		if(PostsList != null) {
			for(PostsVo postVo : PostsList) {
				// 유저 이름
				MemberVo memberVo = memberDao.selectNO(postVo.getUser_no());
				postVo.setUser_name(memberVo.getUser_name());
				// 커뮤니티 이름
				CommunityVo commVo = communityDao.getOneCommunity(postVo.getCommunity_id());
				postVo.setCommunity_name(commVo.getCommunity_name());
				// 카테고리 이름
				CategoryVo categoryVo = categoryDao.selectNO(postVo.getCategory_no()); 
				postVo.setCategory_name(categoryVo.getCategory_name());
			}
		} else {
			System.out.println("읽을 글이 없습니다.");
		}
		return PostsList;
	}

	@Transactional
	@Override
	public PostsVo selectPost(int post_no) {
		// 관리자용 게시글 보기 (조회수 증가 x)
		PostsVo postVo = postdao.selectPost(post_no);
		//System.out.println("포스트노:" + post_no);
		System.out.println("포스트븨오:" + postVo);
		if(postVo != null) {
			// 유저 이름
			MemberVo memberVo = memberDao.selectNO(postVo.getUser_no());
			postVo.setUser_name(memberVo.getUser_name());
			// 커뮤니티 이름
			CommunityVo commVo = communityDao.getOneCommunity(postVo.getCommunity_id());
			postVo.setCommunity_name(commVo.getCommunity_name());
			// 카테고리 이름
			CategoryVo categoryVo = categoryDao.selectNO(postVo.getCategory_no()); 
			postVo.setCategory_name(categoryVo.getCategory_name());	
		}
		return postVo;
	}
	
	
	@Transactional
	@Override
	public PostsVo viewPost(int post_no) {
		PostsVo postVo = postdao.selectPost(post_no);
		//System.out.println("포스트노:" + post_no);
		//System.out.println("포스트븨오:" + postVo);
		if(postVo != null) {
			// 유저 이름
			MemberVo memberVo = memberDao.selectNO(postVo.getUser_no());
			postVo.setUser_name(memberVo.getUser_name());
			// 커뮤니티 이름
			CommunityVo commVo = communityDao.getOneCommunity(postVo.getCommunity_id());
			postVo.setCommunity_name(commVo.getCommunity_name());
			// 카테고리 이름
			CategoryVo categoryVo = categoryDao.selectNO(postVo.getCategory_no()); 
			postVo.setCategory_name(categoryVo.getCategory_name());	
		}
		postdao.updateReadCount(post_no);
		return postVo;
	}

	
	
	
	@Override
	public List<PostsVo> getCommunityPostList(String community_id) {
		// 커뮤니티 전체글 보기
		List<PostsVo> list = postdao.selectCommunityPostList(community_id, POST_CREATE);
		return list;
	}

	@Override
	public List<PostsVo> getCategoryPostList(String community_id, int category_no) {
		// 커뮤니티-카테고리 글 보기
		List<PostsVo> list = postdao.selectCategoryPostList(community_id, category_no, POST_CREATE);
		return list;
	}
	
	@Override
	public void posting(PostsVo vo) {
		// 새로운 게시글 작성
		postdao.posting(vo);
	}

	
	@Override
	public int getUserPostCount(int user_no) {
		// 유저의 게시글 수 세기
		int count = postdao.selectUserPostCount(user_no);
		return count;
	}

	@Override
	public int getNewPostSeq() {
		// 새로운 글 시퀀스 생성
		return postdao.selectSeqPostno();
	}

	@Override
	public PostsVo selectLoadPost(int init_post){
		// TODO Auto-generated method stub
		return postdao.selectLoadPost(init_post);
	}
	
	@Override
	public void updatePost(PostsVo postsVo) {
		// 게시글 수정하기
		postdao.updatePost(postsVo);
	}
	
	@Override
	public void deletePost(int post_no) {
		// 게시글 삭제하기
		postdao.updateStatus(post_no, POST_DELETE);
	}
	
	@Override
	public void lockPost(int post_no) {
		// 포스트 잠그기
		postdao.updateStatus(post_no, POST_LOCK);
	}
	
	@Override
	public void unlockPost(int post_no) {
		// 포스트 잠금 풀기 
		postdao.updateStatus(post_no, POST_CREATE);
	}

	

	
}
