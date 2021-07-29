package com.kmerz.app.dao;

import java.util.List;

import com.kmerz.app.vo.PostsVo;

public interface PostDao {
	public List<PostsVo> selectAllPosts();
	public List<PostsVo> selectStatusPosts(String status);
	public PostsVo selectPost(int post_no);
	public List<PostsVo> selectCommunityPostList(String community_id, String status);
	public void posting(PostsVo vo);
	public List<PostsVo> selectCategoryPostList(String community_id, int category_no, String status);
	public int selectUserPostCount(int user_no);
	public int selectSeqPostno();
	// 게시글 상태 변경
	public void updateStatus(int target, String status);
	
}
