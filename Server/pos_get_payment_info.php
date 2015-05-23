<?php
/*
	filename : pos_get_address.php
	자바에서 받는 변수 : order_id
	기능 : order_id를 바탕으로 total 금액을 읽어오고, btc로 환산해서 전송.
	반환값 : address, btc환산금액, 현재 시세
	
*/
$connect = mysql_connect($host, $user, $password) or die("메뉴 목록을 불러올 수 없습니다.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");
mysql_select_db("bitpay",$connect);

$get_address_sql = "select address from address order by update_time desc limit 0,1";
$address_result = mysql_query($get_address_sql);
$row_array = mysql_fetch_array($address_result);
$address = $row_array[address];

$order_id = $_POST[order_id];

$query = "select total_btc from orders where order_id=" . $order_id;
$result = mysql_query($query);
$row_array = mysql_fetch_array($result);
$total_btc = $row_array[total_btc];

$get_btc = "select btc from btcinfo order by update_time desc limit 0,1";
$btc_result = mysql_query($get_btc);
$btc_row_array = mysql_fetch_array($btc_result);
$btc = $btc_row_array[btc];
$krw_1btc = (100000 / $btc);

$qr_code = "bitcoin:" . $address . "?label=coinplug&amount=" . $total_btc . "&message=" . $order_id;

$update = "update orders set qr_code = '" . $qr_code . "' where order_id=" . $order_id;
mysql_query($update);

echo $qr_code;
mysql_close($connect);
?>
	
