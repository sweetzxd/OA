<%@ page language="java" pageEncoding="utf-8"%>
<%
	request.setAttribute("ctx", request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath() + "/");
	request.setAttribute("ws", "ws://" + request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath() + "/");
%>

