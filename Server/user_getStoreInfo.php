<?php
/*
	filename : user_getLocation.php
	user app에서 상점의 위치정보를 받아오는 php
	java에서 보내는 값 : merchant_id
*/

$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];
$query = "select map_x, map_y from merchant where merchant_id = " . $merchant_id;
$result = mysql_query($query);

$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
	echo ($row_array["map_x"] . "," . $row_array["map_y"]);
}
?>