<?/*
   filename : pos_addItem.php
   자바에서 받는 변수 : merchant_id, name, price
   기능 : 메뉴 리스트 추가
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
$price = (int)$price;
//	echo $price."\n";

$query = "insert into menu (merchant_id, name, price) values ('".$merchant_id."', '".$name."', '".$price."')";

$result = mysql_query($query);
$menu_id = mysql_insert_id();

if($result != 0)
{
	echo "Success,";
	echo $menu_id;
}
else
	echo "Fail";	

mysql_close();
?>