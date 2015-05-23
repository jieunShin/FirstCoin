<?
/*
	filename : pos_auto_checker.php
	자바에서 받는 변수 : order_id
	기능 : 간편결제의 order_id를 받아 결제가 이뤄졌는지 확인하는 php
*/

$connect = mysql_connect("localhost", "root", "apmsetup");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);


$order_id = $_POST[order_id];

$query = "select order_status from orders where order_id=" . $order_id ;

while(true)
{
	$result = mysql_query($query);
	$row_array = mysql_fetch_array($result);
	
	if($row_array[order_status] == 3)
		break;
}
echo "Success";
?>