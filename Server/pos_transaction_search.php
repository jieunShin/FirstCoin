<?
/*
   filename : pos_transaction_search.php
   �ڹٿ��� �޴� ���� : id,fromDate,fromTime,toDate,toTime
   ��� : ������ �Ⱓ�� �ŷ� ���� ��ȸ
*/
////// �ð��� ��¥�� 2015-01-22 09 ���·� ���� ����

$connect = mysql_connect($host, $user, $password) or die("�ŷ������� �ҷ��� �� �����ϴ�");

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