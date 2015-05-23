<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.sql.*"%>
<%@ page import="bitpay.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>


<!DOCTYPE html>
<html>
    
    <head>
    	<title>bitpay menu</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
        <script type="text/javascript" src="http://netdna.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
        <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css"
        rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="menu.css">
        
        
        
        <script>  

		function checkForm() {   
			if (upload.file1.value == "") {   
				alert("파일을 업로드해주세요.");   

  				return false;   
  			}  else if(!checkFileType(upload.file1.value)) {   
				alert("엑셀파일만 업로드 해주세요.");   
				return false;   
			}   
			document.upload.submit();
		}   


		function checkFileType(filePath){   

 			var fileLen = filePath.length;   
			var arr = new Array();
			arr = filePath.split('.');
			var len = arr.length();

 			fileFormatfileFormat = fileFormat.toLowerCase();   
 
			if(arr[len-1] == "xls" || arr[len-1] == "xlsx")
 			{   
				return true;    
			} else
			{     return false;     }   

		}   
		
		function modifyMenu(menu_id,merchant_id) {
			/*var f = document.form;
		    f.str.value = ""+menu_id+","+merchant_id;
		    f.method = "post";
		    f.target = "POP";
		    f.action = "modifyForm.jsp";
		     
		    var popup = window.open("","Modify","width=100, height=100, left=100, top=100, scrollbars=yes");
		    Popup.focus();
		    f.submit();*/
		    //alert("start");
		    
		    var urlEncoding = "modifyForm.jsp" + "?menu_id=" + menu_id + "&merchant_id=" + merchant_id;
		    window.open(urlEncoding, "Modify", "resizable=no width=300 height=300");

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
                            <li class="active">
                                <a href="#">Menu</a>
                            </li>
                            <li class="">
                                <a href="transaction.jsp">Transaction</a>
                            </li>
                            <li class="">
                                <a href="statistic.jsp">Statistic</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <h1 contenteditable="true" class="text-center">메뉴</h1>
        <form action="delete.jsp" method="POST" accept-charset="utf-8">
        <div class="section">
            <div class="container">
                <div class="col-md-12">
                	
                    <table class="table">
                        <thead>
                            <tr>
                                <th>체크</th>
                                <th>No</th>
                                <th>상품명</th>
                                <th>가격</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
									response.setContentType("text/html; charset=euc-kr;");
									request.setCharacterEncoding("utf-8");
								
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

										String sql1 = "select * from menu where merchant_id=?";
										pstmt = conn.prepareStatement(sql1);
										pstmt.setString(1, "" + User.getMerchantId());

										rs = pstmt.executeQuery();
										
										int merchant_id = User.getMerchantId();
										int menu_id;

										while (rs.next()) {
											no++;
											menu_id = rs.getInt("menu_id");
								%>
								<tr>
									<td><input type="checkbox" name="check_menu" value=<%=rs.getString("name") %>></td>
									<td><%=no%></td>
									<td><%=rs.getString("name")%></td>
									<td><%=rs.getString("price")%></td>
									<td><input type="button" value="수정" onclick="modifyMenu('<%=rs.getString("menu_id") %>','<%=User.getMerchantId()%>')"></td>
								</tr>
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
						
                        </tbody>
                    </table>
                    <div class="col-md-1">
                        <input class="btn btn-primary" type="submit" value="삭제">
                    </div>
                    <div class="col-md-1">
                    	<a class="btn btn-primary" href="#" onclick="window.open('addMenu.html','name','resizable=no width=300 height=200');return false">추가</a>
                    </div>
                    
                    <div class="col-md-10">
                        <a class="btn btn-primary" href="makeFile.jsp">파일추출</a>
                    </div>
                </div>                    
            </div>
            </div>
           </form>
        <div class="section">
            <div class="container">               
            <div class="col-md-12">
            
            
                         <form action="excel_insert.jsp" name="upload" method="POST" enctype="multipart/form-data">

				<input type="file" name="file1" size="20">
				<input type="submit" value="업로드">

				</form>
            </div>
            <!-- 
                <div class="row">
                    <div class="col-md-10">
                        <a class="btn btn-primary" href="makeFile.jsp">파일추출</a>
                    </div>
                    
                    
                </div>
                
                 -->
            </div>
        </div>
       
        <div class="section text-center">
            <div class="container">
                <div class="row">
                </div>
            </div>
        </div>
    </body>

</html>