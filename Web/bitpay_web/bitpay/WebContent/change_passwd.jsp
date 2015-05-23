<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.sql.*" %>
<%@ page import = "bitpay.*" %>

<%
	response.setContentType("text/html; charset=euc-kr;");
	request.setCharacterEncoding("utf-8");
	
	String id = request.getParameter("inputId3");
	String cur_passwd = request.getParameter("inputcurpass");
	String new_passwd1 = request.getParameter("inputnewpass1");
	String new_passwd2 = request.getParameter("inputnewpass2");
	
	//
	System.out.println(id + " " + cur_passwd + " " + new_passwd1 + " " + new_passwd2);
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	if(User.getId().equals(id) == false)
	{
%>

		<script>
		alert(' 현재 로그인 된 아이디가 아닙니다.');
		history.go(-2);
		</script>
<%		if(true) return;
	}
	
	else if(new_passwd1 == "" || new_passwd2 == "" || new_passwd1 == null || new_passwd2 == null)
	{
%>

		<script>
		alert(' 변경 할 비밀번호를 입력해 주세요.');
		//exit();
		history.go(-2);
		</script>
		
<%		if(true) return;
	}
	
	try {
		String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay";
		String dbId = "root";
		String dbPass = "apmsetup";
		
			
		conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass);
		
		String sql1 = "select * from merchant where login_id = ? and password = ?";
		pstmt = conn.prepareStatement(sql1);
		pstmt.setString(1,id);
		pstmt.setString(2,cur_passwd);
		
		rs = pstmt.executeQuery();
		
		if(rs.next() == false) {
			rs.previous();
%>

			<script>
			alert(' 아이디와 비밀번호가 정확하지 않습니다.');
			history.go(-2);
			</script>
			
<%			if(true) return;
		}
		
		rs.close();
		pstmt.close();
		
		if(new_passwd1.equals(new_passwd2) == false) {
%>
				<script>
				alert('새로 입력한 비밀번호가 일치하지 않습니다.');
				history.go(-2);
				</script>
<% 				if(true) return;
		}
		
		PreparedStatement pstmt2 = null;
		String sql2 = "update merchant set password=? where login_id=?";
		pstmt2 = conn.prepareStatement(sql2);
		pstmt2.setString(1,new_passwd1);
		pstmt2.setString(2,id);
		
		
		pstmt2.executeUpdate();
		
		User.setPassword(new_passwd1);
		pstmt2.close();
		
%>

		<script>
		alert(' 비밀번호가 변경되었습니다.');
		history.go(-2);
		</script>
		
<%		if(true) return;
		
	} 
	catch(Exception e) {
		
	} finally {
		if(rs != null) try { rs.close();} catch(SQLException sqle) {}
		if(pstmt != null) try { pstmt.close();} catch(SQLException sqle) {}
		if(conn != null) try { conn.close();} catch(SQLException sqle) {}
	}
%>
			
			