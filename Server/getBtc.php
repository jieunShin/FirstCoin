<?php
/*
	filename : getBtc.php
	자바에서 받는 변수 : -
	기능 : 별도의 서버에서 실시간 btc 시세 받아 데이터베이스에 저장해놓은 정보를 자바로 전달
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

echo $btc;
?>
	
