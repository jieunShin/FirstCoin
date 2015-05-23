<?
/*
	filename : get_total_btc.php
	자바에서 받는 변수 : total
	기능 : 원단위로 받은 총 금액을 btc단위로 환산해서 전송
*/

$connect = mysql_connect($host, $user, $password) or die("메뉴 목록을 불러올 수 없습니다.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");
mysql_select_db("bitpay",$connect);

$query = "select btc from btcinfo order by update_time desc limit 0,1";
$result = mysql_query($query);
$row_result = mysql_fetch_array($result);
$btc = $row_result[btc];

$total = $_POST[total];
$total_btc = ($btc * $total) / 100000;
echo $total_btc;
//echo $btc;
?>