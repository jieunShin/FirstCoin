<?
/*
	filename : user_get_remote_order.php
	자바에서 받는 변수 : user_email
	기능 : 원거리주문 내역 출력
*/
$connect = mysql_connect($host, $user, $password) or die("거래내역을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$user_email = $_POST[user_email];

$query = "select * from orders where user_email = '" . $user_email . "' and remote_order='Y' order by order_time desc";

$result = mysql_query($query);

while($row_array = mysql_fetch_array($result))
{
	$getStoreName = "select store_name from merchant where merchant_id = " . $row_array[merchant_id];
	$store = mysql_query($getStoreName);
	$store_array = mysql_fetch_array($store);
	$store_name = $store_array[store_name];
	
   echo ($row_array[order_id] . "," . $row_array[merchant_id] . "," . $store_name . "," . $row_array[order_time] . "," . $row_array[order_status] . "," . $row_array[total] . "," . $row_array[content] . "," . $row_array[total_btc] . "\n");
}

?>