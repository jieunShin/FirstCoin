<?
/*
   filename : pos_transaction_search.php
   자바에서 받는 변수 : id,fromDate,fromTime,toDate,toTime
   기능 : 지정한 기간의 거래 내역 조회
*/
////// 시간과 날짜를 2015-01-22 09 형태로 같이 받음

$connect = mysql_connect($host, $user, $password) or die("거래내역을 불러올 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];
$from = $_POST[from];
$to = $_POST[to];

$query = "select * from orders where merchant_id = '" . $merchant_id . "' and payment_time >= '" . $from . "' and payment_time <= '" . $to . "' order by payment_time desc";

$result = mysql_query($query);

$num_result = mysql_num_rows($result);

if($num_result == 0) {
	echo "not found";
}

while($row_array = mysql_fetch_array($result))
{
   echo ($row_array[payment_time] . "," . $row_array[total] . "," . $row_array[content]. "," . $row_array[total_btc] . "\n");
}

?>