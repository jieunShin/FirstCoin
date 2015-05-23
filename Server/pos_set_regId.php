<?
/*
	filename : pos_set_regId.php
	자바에서 받는 변수 : merchantId, regId 
	기능 : merchant_id와 매칭된 기기의 regid를 등록한다
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];
$reg_id = $_POST[reg_id];

$sql = "update merchant set reg_id = '" . $reg_id . "' where merchant_id = " . $merchant_id;

$result = mysql_query($sql);

if($result){ // 성공한경우 
	echo "Success to regist gcm";
}
else {
	echo "Failed to regist gcm";
}
mysql_close($connect);

?>