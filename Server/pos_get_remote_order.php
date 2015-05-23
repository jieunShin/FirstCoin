<?
/*
	filename : pos_get_remote_order.php
	자바에서 받는 변수 : merchant_id
	기능 : 원거리주문 내역 출력
*/
$connect = mysql_connect($host, $user, $password) or die("거래내역을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];

$query = "select * from orders where merchant_id = '" . $merchant_id . "' and remote_order='Y' order by order_time desc";

//echo $query;

$result = mysql_query($query);

//$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
	$data = explode(' / ', $row_array[user_info]);
	$name = $data[0];
	$phone = $data[1];
	
	echo ($row_array[order_id] . "," . $row_array[user_email] . "," . $row_array[order_time] . "," . $row_array[order_status] . "," . $row_array[total] . "," 
   . $row_array[content] . "," . $row_array[pickup_time] . "," . $name . "," . $phone . "," . $row_array[total_btc] . "\n");
}

?>