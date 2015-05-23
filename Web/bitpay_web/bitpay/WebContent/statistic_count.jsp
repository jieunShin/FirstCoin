<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.sql.*"%>
<%@ page import="bitpay.*"%>

<!DOCTYPE html>
<html>
    
    <head>
        <style>
            #curve_chart {position:relative; margin-left:-150px; text-align:center;}
        </style>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
        <script type="text/javascript" src="http://netdna.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
        <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css"
        rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="menu.css">
        
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1.1", {packages:["bar"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['매뉴', '판매량'],
          
          <%
       	response.setContentType("text/html; charset=euc-kr;");
			request.setCharacterEncoding("utf-8");	
        
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay";
				String dbId = "root";
				String dbPass = "apmsetup";

				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPass);

				PreparedStatement pstmt2 = null;
				String sql = "select menu.name, sum(count) as count from order_item, menu where order_item.menu_id=menu.menu_id and merchant_id=? group by order_item.menu_id order by count desc";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,User.getMerchantId());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					System.out.println(rs.getString("menu.name") + " " + rs.getString("count"));
				
					if(rs.isLast()) {
						
			%>
						['<%= rs.getString("menu.name") %>', <%= rs.getString("count") %>]
			<%
					}
					else {
			%>
					['<%= rs.getString("menu.name") %>', <%= rs.getString("count") %>],
			<%
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(rs != null) try { rs.close();} catch(SQLException sqle) {}
				if(pstmt != null) try { pstmt.close();} catch(SQLException sqle) {}
				if(conn != null) try { conn.close();} catch(SQLException sqle) {}
			}
			
			
        %>
        
        ]);

        var options = {
        	 // legend: { position: 'bottom' }
        };

        var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

        chart.draw(data, options);
      }
    </script>
        
    </head>
    
    <body>
        <div class="navbar navbar-default navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-ex-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <div class="col-md-12">
                        <a href="login.html"><img height="50" alt="Brand" src="web_logo.png" class="center-block"></a>
                    </div>
                </div>
                <div class="collapse navbar-collapse" id="navbar-ex-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="set.html">개인정보 관리</a>
                        </li>
                        <li>
                            <a href="logout.jsp">로그아웃</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="section text-center">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="lead nav nav-justified nav-tabs">
                            <li>
                                <a href="menu.jsp">Menu</a>
                            </li>
                            <li class="">
                                <a href="transaction.jsp">Transaction</a>
                            </li>
                            <li class="active">
                                <a href="#">Statistic</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="section">
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h1 contenteditable="true" class="text-center">통계</h1>
                        <!-- 이곳에 코딩!!! -->
                        <a class="btn btn-warning" href="statistic.jsp">월   별</a>
                        <a class="btn btn-warning" href="statistic_day.jsp">일   별</a>
                        <a class="btn btn-warning" href="statistic_count.jsp">물품별</a>
                        <div id="columnchart_material" style="width: 1000px; height: 500px;"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>