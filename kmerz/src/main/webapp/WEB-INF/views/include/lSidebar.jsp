<%@page import="com.kmerz.app.vo.CommunityVo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="lSidebar">
	<div class="communityList border sticky">
	<div class="commHeader hr">
		<span class="font-24">커뮤니티 리스트</span>
	</div>
		<ul>	
		<%List<CommunityVo> list = (List<CommunityVo>)request.getAttribute("commList"); %>
		<%for(int i = 0; i < list.size(); i++){ %>
			<li><a href="/c"><img src="/resources/images/starcraft_small.jpg"><span><%=list.get(i).getCommunity_name() %></span></a></li>
			<%}; %>
		</ul>
	</div>
</div>