<?
/*
   filename : pos_deleteItem.php
   �ڹٿ��� �޴� ���� : merchant_id, name, price
   ��� : �޴� ����Ʈ ����

   ����-�ֹ�tab���� ���� ��ư�� Ŭ���� �� �޴� �̸��� �׿� ���� ���� ������ �Է��ϸ� 
   DB���� �׿� �´� �׸��� ã�� �����Ѵ�.
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

//����
$query = "delete from menu where merchant_id = '".$merchant_id."' and name = '".$name."'"; 
$result = mysql_query($query);

//�޴� ���� ���� - �޴� ������ �ٽ� ����
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