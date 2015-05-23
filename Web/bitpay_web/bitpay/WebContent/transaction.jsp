<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.sql.*"%>
<%@ page import="bitpay.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>


<!DOCTYPE html>
<html>

<head>
<title>Bitpay Transaction</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript" src="date_picker.js"></script>
<script type="text/javascript"
	src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script type="text/javascript"
	src="http://netdna.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link
	href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet" href="menu.css">
</head>

<body>
	<form action="transaction.jsp" method="post">
		<div class="navbar navbar-default navbar-static-top">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target="#navbar-ex-collapse">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<div class="col-md-12">
						<a href="login.html"><img height="50" alt="Brand" src="web_logo.png"
							class="center-block"></a>
					</div>
				</div>
				<div class="collapse navbar-collapse" id="navbar-ex-collapse">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="set.html">개인정보 관리</a></li>
						<li><a href="logout.jsp">로그아웃</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="section text-center">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<ul class="lead nav nav-justified nav-tabs">
							<li><a href="menu.jsp">Menu</a></li>
							<li class="active"><a href="#">Transaction</a></li>
							<li class=""><a href="statistic.jsp">Statistic</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<h1 class="text-center">거래내역</h1>
		<div class="section">
			<div class="container">
				<div class="row">
					<div class="col-md-12 text-right">
						<input name="target_date" placeholder="first date" type="text">
						<input type="button" value="달력" onClick="datePicker(event,'target_date')">
						
						<input name="target_date2" placeholder="last date" type="text">
						<input type="button" value="달력" onClick="datePicker(event,'target_date2')">
						
						<br><br>
						<input type="checkbox" name="distance" value="Y"> 원거리 여부
						<br><br>
						
						<input type="submit" value="검색">
						
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table">
							<thead>
								<tr>
									<th>No</th>
									<th>거래내역</th>
									<th>결제금액</th>
									<th>결제시간</th>
									<th>상태</th>
									<th>원거리주문</th>
								</tr>
							</thead>
							<tbody>

								<%
									response.setContentType("text/html; charset=euc-kr;");
									request.setCharacterEncoding("utf-8");
								
									String firstdate = request.getParameter("target_date");
									String lastdate = request.getParameter("target_date2");
									lastdate += " 24";
									String[] str = request.getParameterValues("distance");
									
									if(firstdate == null || lastdate == null){
										firstdate = "0000-00-00";
										
										java.util.Date today = new java.util.Date();
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										
										lastdate = sdf.format(today)+" 24";
									}
									
									System.out.println(firstdate + " " + lastdate);

									Class.forName("com.mysql.jdbc.Driver");
									Connection conn = null;
									PreparedStatement pstmt = null;
									ResultSet rs = null;
									int no = 0;

									try {
										String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay";
										String dbId = "root";
										String dbPass = "apmsetup";

										conn = DriverManager.getConnection(jdbcUrl, dbId, dbPass);
										
										String sql1;

										if(str != null) {
											sql1 = "select * from orders where remote_order = 'Y'and merchant_id = ? and order_time >= ? and order_time <= ? order by order_time desc";
											//sql1 = "select order_time, total, menu.name, order_item.count, order_status, remote_order from orders, menu, order_item where orders.remote_order='Y' and orders.order_time >= ? and orders.order_time <= ? and orders.merchant_id=? and order_time is not null and order_item.order_id = orders.order_id and order_item.menu_id = menu.menu_id order by order_time desc limit 0,30";
											//sql1 = "select orders.order_time, orders.total, menu.name, order_item.count, orders.order_status, orders.remote_order from orders, menu, order_item where orders.remote_order='Y' and orders.order_time >= ? and orders.order_time <= ? and orders.merchant_id=? and order_time is not null and order_item.order_id = orders.order_id and order_item.munu_id=menu.menu_id order by order_time desc limit 0,30";
											//sql1 = "select * from orders, menu, order_item where orders.merchant_id=? and orders.order_time >= ? and orders.order_time <= ? and orders.remote_order='Y' and orders.order_id = order_item = order_id and order_item.menu_id = menu.menu_id order by orders.order_time desc";
											//sql1 = "select * from orders where merchant_id=? and order_time >= ? and order_time <= ? and remote_order = 'Y' order by order_time desc";
										}
										else {
											sql1 = "select * from orders where merchant_id=? and order_time >= ? and order_time <= ? order by order_time desc";
											//sql1 = "select * from orders where merchant_id=? and order_time >= ? and order_time <= ? order by order_time desc";
										}
										pstmt = conn.prepareStatement(sql1);
										
										/*
										pstmt.setInt(1,User.getMerchantId());
										pstmt.setString(2,firstdate);
										pstmt.setString(3,lastdate);  */  
										
										pstmt.setInt(1,User.getMerchantId());
										pstmt.setString(2,firstdate);
										pstmt.setString(3,lastdate);
										  
										
										
										//pstmt.setInt(1,User.getMerchantId());
										

										rs = pstmt.executeQuery();

										while (rs.next()) {
											no++;
								%>
								
								 
								<tr>
									<td><%=no%></td>
									<td><%=rs.getString("content")%></td>
									<td><%=rs.getString("total")%></td>
									<td><%=rs.getString("order_time")%></td>
									
									<%
										String status = rs.getString("order_status");
										if(status.equals("-1")) {
											if(rs.getString("remote_order").equals('Y')) {
									%>
												<td>거절</td>
									<%
											}else {
									%>
												<td>결제취소</td>
									<% 	
											}
										}
										else if(status.equals("0")) {
									%>
											<td>주문</td>
									<%	}
										else if(status.equals("1")) {
									%>
											<td>승인</td>
									<%	}
										else if(status.equals("2")) {
									%>
											<td>QRcode</td>
									<%	}
										else if(status.equals("3")) {
									%>
											<td>결제완료</td>
									<%	}
										else if(status.equals("4")) {
									%>
											<td>상품준비중</td>
									<%	}
										else if(status.equals("5")) {
									%>
											<td>픽업대기</td>
									<%	}
										else if(status.equals("6")) {
									%>
											<td>픽업완료</td>
									<%	}
										else if(status.equals("")){
									%>
											<td></td>
									<%	} 
										else {
									%>
											<td>에러</td>
									<%	}
									%>
									
								
									<td><%=rs.getString("remote_order")%></td>
								</tr>   
								
								<!-- 
								<tr>
									<td><%=no%></td>
									<td><%=no%></td>
									<td><%=rs.getString("total")%></td>
									<td><%=rs.getString("order_time")%></td>
									<td><%=rs.getString("order_status")%></td>
									<td><%=rs.getString("remote_order")%></td>
								</tr>   -->
								
								<%
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

								<!-- 
                                <tr>
                                    <td>1</td>
                                    <td>Mark</td>
                                    <td>Otto</td>
                                    <td>@mdo</td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>Jacob</td>
                                    <td>Thornton</td>
                                    <td>@fat</td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>Larry</td>
                                    <td>the Bird</td>
                                    <td>@twitter</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>Mark</td>
                                    <td>Otto</td>
                                    <td>@mdo</td>
                                </tr>      -->
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

	</form>
</body>

</html>