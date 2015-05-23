<?
/*
	filename : user_get_remote_pay_info.php
	자바에서 받는 변수 : order_id
	기능 : total_btc, qr_code를 자바로 전달
*/

$connect = mysql_connect($host, $user, $password) or die("거래내역을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$order_id = $_POST[order_id];

$query = "select total_btc, qr_code from orders where order_id = " . $order_id;

$result = mysql_query($query);
$row_array = mysql_fetch_array($result);
$qr_code = $row_array[qr_code];
$total_btc = $row_array[total_btc];

$data = explode(':', $qr_code);
$data_2 = explode('?', $data[1]);
$address = $data_2[0];
echo $total_btc . "," . $address . "\n";
mysql_close($connect);
?>