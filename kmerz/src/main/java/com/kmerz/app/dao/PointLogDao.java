package com.kmerz.app.dao;

import java.util.List;

import com.kmerz.app.vo.PointLogVo;

public interface PointLogDao {
	public void insertPointLog(PointLogVo pointLogVo);
	public int selectPreTotal(int user_no);
	public List<PointLogVo> selectUserNo(int user_no);
}
