<?
/*
   filename : pos_statistic_month.php
   자바에서 받는 변수 : merchant_id
   기능 : 해당 연도의 월별 통계 조회
*/
$connect = mysql_connect($host, $user, $password) or die("월 별 통계 자료를 볼 수 없습니다");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];
$year = date(Y);

$sql = "Insert into statistic (merchant_id, total, total_btc, date) 
SELECT merchant_id,  SUM(total), SUM(total_btc),  date_format(payment_time,'%Y-%m-%d') as date
FROM orders
where payment_time is not null
GROUP BY merchant_id, date_format(payment_time,'%Y-%m-%d')
on duplicate key update 
`merchant_id`=values(merchant_id), `date`=values(date), `total`=values(total), `total_btc`=values(total_btc)";

$query = "select date_format(date, '%Y-%m') as month, sum(total) as total, sum(total_btc) as total_btc from statistic 
where merchant_id = '" . $merchant_id . "'" . " group by date_format(date, '%Y-%m') order by date desc limit 0,31 ";

$update = mysql_query($sql);
$result = mysql_query($query);

$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
   echo ($row_array[month] . "," . $row_array[total]. "," . $row_array[total_btc] . "\n");
}
?>