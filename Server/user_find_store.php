<?
/*
	filename : user_find_store.php
	자바에서 받는 변수 : keyword
	기능 : keyword를 포함하는 상점을 검색
	반환 : merchant_id, store_name, address, phone
*/

$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$keyword = $_POST[keyword];
$query = "select merchant_id, store_name, address, phone from merchant where store_name like '%" . $keyword . "%'";

$result = mysql_query($query);

while($row_array = mysql_fetch_array($result))
{
   echo ($row_array[merchant_id] . "," . $row_array[store_name] . "," . $row_array[address] . "," . $row_array[phone] . "\n");
}

?>