<?php
/*
   filename : user_showMenuList.php
   자바에서 받는 변수 : id
   기능 : db에 저장된 각 id에 해당하는 메뉴 이름과 가격을 출력
*/
$connect = mysql_connect($host, $user, $password) or die("메뉴 목록을 불러올 수 없습니다.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$id = $_POST[id];
$query = "select name, price from menu where merchant_id = '".$id."' "; 

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