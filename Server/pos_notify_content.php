<?
/*
   filename : pos_notify_content.php
   �ڹٿ��� �޴� ���� : title
   ��� : ���������� ���� �´� ������ �����ش�
*/
$db = mysql_connect($host, $user, $password) or die("���������� �ҷ��� �� �����ϴ�");

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