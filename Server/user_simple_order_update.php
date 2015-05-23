<?php
/*
	결제가 완료되면 실행되는 파일
	결제시간을 업데이트 한다
	
	 payment_id, pickup_time, qr_code, order_status, remote_order 모두 업데이트 해야한다
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$time = date("Y-m-d H:i:s");
$order_id = $_POST[order_id];

$query = "update orders set orders.payment_time = cast('" . $time . "' as datetime), order_status = 3 
where orders.order_id = '" . $order_id . "'";
$result = mysql_query($query);
?>