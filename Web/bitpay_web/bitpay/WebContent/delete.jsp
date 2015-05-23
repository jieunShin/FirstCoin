<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%@ page import = "bitpay.*" %>

<%

	request.setCharacterEncoding("utf-8");
	String [] sCheck = request.getParameterValues("check_menu");
	
	for(int i=0; i<sCheck.length; i++) {
		System.out.println(sCheck[i]);
	}
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try {
		
		String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay?useUnicode=true&characterEncoding=UTF-8";
		String dbId = "root";
		String dbPass = "apmsetup";
		
		//test
		System.out.println("test");
			
		conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass);
		

		System.out.println("driver connection success");
		
		for(int i=0; i<sCheck.length; i++) {
			String query = "delete from menu where name=? and merchant_id=?";
		
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,sCheck[i]);
			pstmt.setInt(2,User.getMerchantId());
			pstmt.executeUpdate();
		}
		
		System.out.println("success");

	} 
	catch(Exception e) {
		e.printStackTrace();
	} finally {
		if(rs != null) try { rs.close();} catch(SQLException sqle) {}
		if(pstmt != null) try { pstmt.close();} catch(SQLException sqle) {}
		if(conn != null) try { conn.close();} catch(SQLException sqle) {}
		response.sendRedirect("menu.jsp");
	}
%>