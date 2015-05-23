<?
/*
   filename : pos_help_title.php
   기능 : 도움말 게시물의 제목을 보여줌
   설정 메뉴에서 도움말을 클릭하면 저장되어 있는 게시물들의 제목이 보여진다.
*/
$db = mysql_connect($host, $user, $password) or die("도움말을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");


$board = mysql_select_db('bitpay', $db);

$qry = "SELECT * FROM question order by question_id desc";
$result = mysql_query($qry);
$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
   echo $row_array[title]."\n";
}

?>