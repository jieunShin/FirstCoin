<?
/*
   filename : pos_transaction_init.php
   �ڹٿ��� �޴� ���� : id
   ��� : �ֱ� 30���� �ŷ� ���� ���
*/
$connect = mysql_connect($host, $user, $password) or die("�ŷ������� �ҷ��� �� �����ϴ�");

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