<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.sql.*" %>
<%@ page import = "bitpay.*" %>
	
	<%
	response.setContentType("text/html; charset=utf-8;");
	request.setCharacterEncoding("utf-8");
	
	String menu_id = request.getParameter("menu_id");
	String merchant_id = request.getParameter("merchant_id");
	String name = request.getParameter("menu_name");
	String price = request.getParameter("menu_price");
	
	//test
	System.out.println(menu_id +  " " + merchant_id + " " + name + " " + price);
	
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
		
		//String query = "insert into menu (merchant_id,name,price,stuck) values (" + User.getMerchantId() + " ,'" + name + "', " + Integer.parseInt(price) + ",0)";
		String query = "update menu set name = ?, price = ? where merchant_id = ? and menu_id = ?";
	     
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1,name);
		pstmt.setInt(2,Integer.parseInt(price));
		pstmt.setInt(3,Integer.parseInt(merchant_id));
		pstmt.setInt(4,Integer.parseInt(menu_id));
		pstmt.executeUpdate();
		
		System.out.println("success");

	} 
	catch(Exception e) {
		e.printStackTrace();
	} finally {
		if(rs != null) try { rs.close();} catch(SQLException sqle) {}
		if(pstmt != null) try { pstmt.close();} catch(SQLException sqle) {}
		if(conn != null) try { conn.close();} catch(SQLException sqle) {}
		//response.sendRedirect("modifyForm.jsp");
	}
%>

<script>
window.close();</script>
			
			