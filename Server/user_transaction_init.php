<?
/* filename : user_transaction_init.php
   자바에서 받는 변수: id
   기능: 최근 30개의 거래 내역 출력
*/
$connect = mysql_connect($host, $user, $password) or die("거래내역을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$user_email = $_POST[user_email];

$query = "select * from orders where user_email = '" . $user_email . "' order by payment_time desc limit 0,30";

$result = mysql_query($query);

$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
	$findStoreName = "select store_name from merchant where merchant_id=" . $row_array[merchant_id];
	$store_result = mysql_query($findStoreName);
	$row_store_result = mysql_fetch_array($store_result);
	$store_name = $row_store_result[store_name];	

	echo ($row_array[payment_time] . "," . $row_array[total] . ",". $row_array[content] . "," . $row_array[total_btc] . "," . $store_name . "\n");
}

?>
