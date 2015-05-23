<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.sql.*" %>
<%@ page import = "bitpay.*" %>
	
	<%
	response.setContentType("text/html; charset=utf-8;");
	request.setCharacterEncoding("utf-8");
	
	String name = request.getParameter("menu_name");
	String price = request.getParameter("menu_price");
	
	//test
	System.out.println(User.getMerchantId() +  " " + name + " " + price);
	
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
		String query = "insert into menu (merchant_id,name,price,sales) values (?,?,?,?)";
		
		/*
		 Statement statement = conn.createStatement();
	     statement.executeUpdate(query);
	     statement.close();  */
	     
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,User.getMerchantId());
		pstmt.setString(2,name);
		pstmt.setInt(3,Integer.parseInt(price));
		pstmt.setInt(4,0);
		pstmt.executeUpdate();
		
		System.out.println("success");

	} 
	catch(Exception e) {
		e.printStackTrace();
	} finally {
		if(rs != null) try { rs.close();} catch(SQLException sqle) {}
		if(pstmt != null) try { pstmt.close();} catch(SQLException sqle) {}
		if(conn != null) try { conn.close();} catch(SQLException sqle) {}
		//response.sendRedirect("addMenu.html");
	}
%>

<script>
window.close();
</script>
			
			