<?/*
   filename : pos_addItem.php
   �ڹٿ��� �޴� ���� : merchant_id, name, price
   ��� : �޴� ����Ʈ �߰�
*/
$connect = mysql_connect($host, $user, $password) or die("�޴� ����� �ҷ��� �� �����ϴ�.");

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