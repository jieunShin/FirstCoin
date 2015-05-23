<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.sql.*" %>
<%@ page import = "bitpay.*" %>

<%
	response.setContentType("text/html; charset=utf-8;");
	request.setCharacterEncoding("utf-8");
	
	String id = request.getParameter("InputId");
	String passwd = request.getParameter("InputPassword");
	
	//test
	System.out.println(id + " " + passwd);
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try {
		String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay";
		String dbId = "root";
		String dbPass = "apmsetup";
		
			
		conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass);
		
		String sql = "select * from merchant where login_id=? and password=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,id);
		pstmt.setString(2,passwd);
		
		rs = pstmt.executeQuery();
		
		if(rs.next() == false) {
			rs.previous();
%>

			<script>
			alert('올바른 아이디와 비밀번호를 입력해 주세요');
			history.go(-1);
			</script>
			
<%		
		}
		
		else {
			User.setId(id);
			User.setPassword(passwd);
			User.setMerchantId(rs.getInt("merchant_id"));
			response.sendRedirect("menu.jsp");
		}

	} 
	catch(Exception e) {
		e.printStackTrace();
	} finally {
		if(rs != null) try { rs.close();} catch(SQLException sqle) {}
		if(pstmt != null) try { pstmt.close();} catch(SQLException sqle) {}
		if(conn != null) try { conn.close();} catch(SQLException sqle) {}
	}
%>
			
			