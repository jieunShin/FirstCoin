<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.sql.*" %>
<%@ page import = "bitpay.*" %>

<%
	String menu_id = request.getParameter("menu_id");
	String merchant_id = request.getParameter("merchant_id");
	
	System.out.println(menu_id + " " + merchant_id);
	
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modify</title>
</head>
<body>
	<form action="modifyMenu.jsp" method="post">
		<div class="row">
        	<div class="col-md-12 text-center">
                <div class="form-group">
                	<div class="col-sm-3">
                     	<label for="input" class="control-label">menu_id</label>
                     </div>
                     <div class="col-sm-9">
						<input type="text" class="form-control" name="menu_id" readonly="true" value=<%=menu_id %>> <br>
					 </div>
					 
					 <div class="col-sm-3">
                     	<label for="input" class="control-label">merchant_id</label>
                     </div>
                     <div class="col-sm-9">
						<input type="text" class="form-control" name="merchant_id" readonly="true" value=<%=merchant_id %>> <br>
					 </div>
					 
                     <div class="col-sm-3">
                     	<label for="input" class="control-label">메뉴명</label>
                     </div>
                     <div class="col-sm-9">
						<input type="text" class="form-control" name="menu_name" placeholder="메뉴명"> <br>
					 </div>
					 
					 <div class="col-sm-3">
                     	<label for="input" class="control-label">가격</label>
                     </div>
                     <div class="col-sm-9">
					 	<input type="text" class="form-control" name="menu_price" placeholder="가격"> <br><Br>
					 </div>
				</div>
			</div>
		</div>
		<input type="submit" value="수정">	
	</form>
</body>
</html>