<%@page contentType="text/html; charset=euc-kr" language="java" errorPage=""%>  
<%@page import="java.util.*,java.io.*" %>
<%@page import="com.oreilly.servlet.MultipartRequest" %>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@page import="jxl.*" %>  
<%@ page import="java.sql.*" %>
<%@ page import="bitpay.*" %>

 
<%   

	String savePath = "C:/Users/Administrator/workspace/bitpay/WebContent/fileStorage"; // 저장할 디렉토리   

	int sizeLimit = 30 * 1024 * 1024 ; // 용량제한   
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

 		if(fileName != null)  // 파일이 업로드 되면
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

	int col = sheet.getColumns();  // 시트의 컬럼의 수를 반환한다.    
	int row = sheet.getRows();   // 시트의 행의 수를 반환한다.  

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

     	for (int i = 0 ; i < row ; i++)            // 수동 : 테이블 형태의 방식
		{     
			String menu_num  = sheet.getCell(0,i).getContents(); //첫번째 인자가 열 값, 두번째 인자값이 행 값..!
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

	alert("처리중 오류가 발생하였습니다\n잠시후 다시 시도하세요!!");
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