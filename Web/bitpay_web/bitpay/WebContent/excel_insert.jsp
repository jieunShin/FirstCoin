<%@page contentType="text/html; charset=euc-kr" language="java" errorPage=""%>  
<%@page import="java.util.*,java.io.*" %>
<%@page import="com.oreilly.servlet.MultipartRequest" %>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@page import="jxl.*" %>  
<%@ page import="java.sql.*" %>
<%@ page import="bitpay.*" %>

 
<%   

	String savePath = "C:/Users/Administrator/workspace/bitpay/WebContent/fileStorage"; // ������ ���丮   

	int sizeLimit = 30 * 1024 * 1024 ; // �뷮����   
	String formName = "";   
	String fileName = "";   

	Vector vFileName = new Vector();   
	Vector vFileSize = new Vector();   

	String[] aFileName = null;   
	String[] aFileSize = null;   

	long fileSize = 0;   

 	MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, "euc-kr", new DefaultFileRenamePolicy());   
	Enumeration formNames = multi.getFileNames();    

 	while (formNames.hasMoreElements()) 
	{    
		formName = (String)formNames.nextElement();    
		fileName = multi.getFilesystemName(formName);    

 		if(fileName != null)  // ������ ���ε� �Ǹ�
		{      
			fileSize = multi.getFile(formName).length();   
			vFileName.addElement(fileName);   
			vFileSize.addElement(String.valueOf(fileSize));    
		}    
	}   

    aFileName = (String[])vFileName.toArray(new String[vFileName.size()]);   
	aFileSize = (String[])vFileSize.toArray(new String[vFileSize.size()]);   

%>  

  

<%   

	Workbook workbook = Workbook.getWorkbook(new File(savePath + "/" + fileName));    
	Sheet sheet = workbook.getSheet(0);   

	int col = sheet.getColumns();  // ��Ʈ�� �÷��� ���� ��ȯ�Ѵ�.    
	int row = sheet.getRows();   // ��Ʈ�� ���� ���� ��ȯ�Ѵ�.  

%>


<HTML>  
<HEAD>  
	<TITLE>Excel Document Reader</TITLE>  
</HEAD>  

<BODY>  

<%

	Connection conn = null;
	PreparedStatement pstmt = null;

	try
	{

		Class.forName("com.mysql.jdbc.Driver");
     	String jdbcUrl = "jdbc:mysql://203.246.112.131:3306/bitpay?useUnicode=true&characterEncoding=UTF-8";
		String dbId = "root";
		String dbPass = "apmsetup";

		conn = DriverManager.getConnection(jdbcUrl, dbId, dbPass);

     	for (int i = 0 ; i < row ; i++)            // ���� : ���̺� ������ ���
		{     
			String menu_num  = sheet.getCell(0,i).getContents(); //ù��° ���ڰ� �� ��, �ι�° ���ڰ��� �� ��..!
			String read_name  = sheet.getCell(1,i).getContents();  
			String read_price  = sheet.getCell(2,i).getContents();  
      		String read_sales = sheet.getCell(3,i).getContents();

     		String sql= "insert into menu (merchant_id,name,price,sales) values (?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1,User.getMerchantId());
      		pstmt.setString(2,read_name);
			pstmt.setInt(3,Integer.parseInt(read_price));
      		pstmt.setInt(4,Integer.parseInt(read_sales));

  		   	pstmt.executeUpdate();

		}                
     }

	catch(Exception e)
	{
		e.printStackTrace();
	
%>

<script language=javascript>

	alert("ó���� ������ �߻��Ͽ����ϴ�\n����� �ٽ� �õ��ϼ���!!");
	history.back();

</script>

<%  

	} 
	finally
	{
		if(conn != null)
		{
   			conn.close();
		}
		
		response.sendRedirect("menu.jsp");
	}

%>  

</BODY>  

</HTML> 