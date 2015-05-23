<?
/*
   filename : pos_transaction_init.php
   자바에서 받는 변수 : id
   기능 : 최근 30개의 거래 내역 출력
*/
$connect = mysql_connect($host, $user, $password) or die("거래내역을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];

$query = "select * from orders where merchant_id = '" . $merchant_id . "' order by payment_time desc limit 0,30";
$result = mysql_query($query);

$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
   echo ($row_array[payment_time] . "," . $row_array[total] . "," . $row_array[content]. "," . $row_array[total_btc] . "\n");
}
?>