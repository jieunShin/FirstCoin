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
        <script type="text/javascript" src="https://www.google.com/jsapi?autoload={
            'modules':[{
              'name':'visualization',
              'version':'1',
              'packages':['corechart']
            }]
          }"></script>
        <script type="text/javascript">
            google.setOnLoadCallback(drawChart);
        
              function drawChart() {
                var data = google.visualization.arrayToDataTable([
                  ['Month', 'Total'],
                  
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

						
						//String sql1 = "insert into statistic (`merchant_id`,`date`,`total`) SELECT merchant_id, date_format(payment_time,'%Y-%m-%d') as date , SUM(total) FROM orders where payment_time is not null GROUP BY merchant_id, date_format(payment_time,'%Y-%m-%d') on duplicate key update `merchant_id`=values(merchant_id), `date`=values(date), `total`=values(total)";
						String sql1 = "Insert into statistic (merchant_id, total, total_btc, date) SELECT merchant_id,  SUM(total), SUM(total_btc),  date_format(payment_time,'%Y-%m-%d') as date FROM orders where payment_time is not null GROUP BY merchant_id, date_format(payment_time,'%Y-%m-%d') on duplicate key update `merchant_id`=values(merchant_id), `date`=values(date), `total`=values(total), `total_btc`=values(total_btc)";
						
						pstmt = conn.prepareStatement(sql1);
						pstmt.executeUpdate();
						pstmt.close();
						
						PreparedStatement pstmt2 = null;
						//String sql2 = "select date_format(date, '%Y-%m-%d') as day, total from statistic where merchant_id=? group by date_format(date, '%Y-%m-%d') order by date asc limit 0,31 ";
						String sql2 = "select date_format(date, '%Y-%m-%d') as day, total, total_btc from statistic where merchant_id = ? group by date_format(date, '%Y-%m-%d') order by date desc limit 0,31";
						
						pstmt2 = conn.prepareStatement(sql2);
						pstmt2.setString(1, ""+User.getMerchantId());
						rs = pstmt2.executeQuery();
						
						while(rs.next()) {
							
							System.out.println(rs.getString("day") + " " + rs.getString("total"));
							
							if(rs.isLast()) {
					%>
								['<%= rs.getString("day")%>', <%= rs.getString("total") %>]
					<%
							}
							else {
					%>
							['<%= rs.getString("day")%>', <%= rs.getString("total") %>],
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
                  curveType: 'function',
                  legend: { position: 'right' },
                  series:{
                	  0: {lineWidth:2, pointSize:8, color:'#FF6600', pointShape:'diamond'}
                  }
                };
        
                var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
        
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
                                <a href="statistic.jsp">Statistic</a>
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
                        <div id="curve_chart" style="width: 1500px; height: 750px"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>