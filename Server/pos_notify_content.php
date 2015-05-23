<?
/*
   filename : pos_notify_content.php
   자바에서 받는 변수 : title
   기능 : 공지사항의 제목에 맞는 내용을 보여준다
*/
$db = mysql_connect($host, $user, $password) or die("공지사항을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

$board = mysql_select_db('bitpay', $db);

$title = $_REQUEST[title];

$qry = "SELECT content FROM board where title = " ."'" . $title . "'";
$result = mysql_query($qry);

$row_array = mysql_fetch_array($result);
echo $row_array[content]."\n";

?>