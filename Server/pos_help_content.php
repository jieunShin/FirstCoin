<?
/*
   filename : pos_help_content.php
   기능 : 도움말 게시물 내용을 보여준다

   도움말 게시물의 제목을 클릭하면 그 아래에 제목에 해당하는 내용이 출력되도록 해준다.
*/

$db = mysql_connect($host, $user, $password) or die("도움말을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

$board = mysql_select_db('bitpay', $db);

$title = $_REQUEST[title];

$qry = "SELECT content FROM question where title = " ."'" . $title . "'";
$result = mysql_query($qry);

$row_array = mysql_fetch_array($result);
echo $row_array[content]."\n";

?>