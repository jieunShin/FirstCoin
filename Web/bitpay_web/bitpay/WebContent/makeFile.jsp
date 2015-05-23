<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.sql.*"%>
<%@ page import="bitpay.*"%>
<%@ page import="java.io.PrintWriter" %>

<%
	response.setContentType("application/vnd.ms-excel; charset=euc-kr;");
	request.setCharacterEncoding("utf-8");
								
	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int no = 0;

	try {
			String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay?useUnicode=true&characterEncoding=UTF-8";
			String dbId = "root";
			String dbPass = "apmsetup";

			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPass);

			String sql1 = "select * from menu where merchant_id=?";
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, User.getMerchantId());

			rs = pstmt.executeQuery();
			
			PrintWriter writer = response.getWriter();
			writer.println("menu_id\tname\tprice\tsales");

			while (rs.next()) {
				no++;
				writer.println(rs.getInt("menu_id")+"\t"+rs.getString("name")+"\t"+rs.getInt("price")+"\t"+rs.getInt("sales"));
			}

			no = 0;
			
		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle) {
				}
		}
	
%>
