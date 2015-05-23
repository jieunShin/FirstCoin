<?
/*
	filename : user_set_regId.php
	자바에서 받는 변수 : user_email, reg_id 
	기능 : order app에서 로그인한 기기의 regid를 등록한다
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$user_email = $_POST[user_email];
$user_regId = $_POST[reg_id];

$find_sql = "select * from customer where user_email = '" . $user_email . "'";
$find_result = mysql_query($find_sql);
$exist = mysql_num_rows($find_result);

if($exist)	// 이미 등록된 적 있는 사용자
{
	$sql = "update customer set user_regId = '" . $user_regId . "' where user_email = '" . $user_email . "'";
	$data = mysql_fetch_array($find_result);

	$regist = mysql_query($sql);
	$customer_id = $data[customer_id];
}
else
{
	$sql = "insert into customer (user_email, user_regId) values ('" . $user_email . "', '" . $user_regId . "')";
	$regist = mysql_query($sql);
	$customer_id = mysql_insert_id();
}
//echo $sql;

//echo $result;
if($regist){ // 성공한경우 
	echo "Success to regist gcm," . $customer_id;
}
else {
	echo "Failed to regist gcm," . $customer_id;
}
mysql_close($connect);
?>