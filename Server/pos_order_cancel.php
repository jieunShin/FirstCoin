<?php
/*
	filename : pos_order_cancel.php
	자바에서 받는 변수 : order_id
	기능 : 결제취소 된 주문에 대해 db에 상태를 업데이트 한다.
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$order_id = $_POST[order_id];

$sql = "update orders set order_status=-1 where order_id=" . $order_id;

$result = mysql_query($sql);
?>