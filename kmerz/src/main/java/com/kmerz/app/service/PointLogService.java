package com.kmerz.app.service;

import java.util.List;

import com.kmerz.app.vo.PointLogVo;

public interface PointLogService {
	public void addPointLog(PointLogVo pointlogVo);
	public List<PointLogVo> getPointLogList(int user_no);
}
