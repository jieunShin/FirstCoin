<?
/*
   filename : pos_deleteItem.php
   자바에서 받는 변수 : merchant_id, name, price
   기능 : 메뉴 리스트 삭제

   메인-주문tab에서 삭제 버튼을 클릭할 때 메뉴 이름과 그에 대한 가격 정보를 입력하면 
   DB에서 그와 맞는 항목을 찾아 삭제한다.
*/

$connect = mysql_connect($host, $user, $password) or die("메뉴 목록을 불러올 수 없습니다.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$merchant_id = $_REQUEST[merchant_id];
$name = $_REQUEST[name];
$price = $_REQUEST[price];

//삭제
$query = "delete from menu where merchant_id = '".$merchant_id."' and name = '".$name."'"; 
$result = mysql_query($query);

//메뉴 정보 갱신 - 메뉴 정보를 다시 전달
$query = "select name, price from menu where merchant_id = '".$merchant_id."' "; 
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