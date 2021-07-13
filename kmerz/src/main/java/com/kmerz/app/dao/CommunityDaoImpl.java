package com.kmerz.app.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kmerz.app.vo.CommunityVo;

@Repository
public class CommunityDaoImpl implements CommunityDao {

	private static final String NAMESPACE = "com.kmerz.app.community.";
	
	@Inject
	SqlSession sqlsession;
	

	@Override
	public int countAllCommunity() {
		
		return 0;
	}

	@Override
	public void insertCommunity(CommunityVo communityVo) {
		sqlsession.insert(NAMESPACE+"insertCommunity", communityVo);
	}

}
