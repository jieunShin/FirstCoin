<?
/*
	filename : user_showMenuList.php
	자바에서 받는 변수 : merchant_id
	기능 : 선택한 카테고리에 해당하는 상점 중 내 위치 주변의 상점정보를 불러온다
*/

$connect = mysql_connect($host, $user, $password) or die("메뉴 목록을 불러올 수 없습니다.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_POST[merchant_id];
$query = "select name, price from menu where merchant_id = '" . $merchant_id . "' "; 

$result = mysql_query($query);
$i = 0;

while($data = mysql_fetch_array($result)) {
	echo $data['name'].",".$data['price']."\n";
	$i++;
}

if($i == 0) {
	echo "not found";
}

mysql_close();
?>