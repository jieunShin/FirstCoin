<?
/*
   filename : pos_help_title.php
   ��� : ���� �Խù��� ������ ������
   ���� �޴����� ������ Ŭ���ϸ� ����Ǿ� �ִ� �Խù����� ������ ��������.
*/
$db = mysql_connect($host, $user, $password) or die("������ �ҷ��� �� �����ϴ�");

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