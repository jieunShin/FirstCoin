<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "bitpay.*" %>

<%
	System.out.println(User.getId() + " " + User.getPassword() + " " + User.getMerchantId());

	User.setId("");
	User.setPassword("");
	User.setMerchantId(-1);
	
	System.out.println(User.getId() + " " + User.getPassword());
	
	response.sendRedirect("login.html");
%>