<?
/*
   filename : pos_logcheck.php
   �ڹٿ��� �޴� ���� : id, password
   ��� : �α��� �� id�� password�� �´��� Ȯ��. �����Ǹ� ���� �̸��� �����ش�.
*/

$connect = mysql_connect("localhost", "root", "apmsetup");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);


$id = $_POST[id];
$password = $_POST[password];

$qry = "select * from merchant where login_id = '".$id."' and password = '".$password."'";

$result = mysql_query($qry);
$num = mysql_num_rows($result);

if($num == 0) {
	echo "User not found";
}
else {
	while($row = mysql_fetch_array($result)) {
		echo "User found,";
		echo $row["merchant_id"].",";
		echo $row["store_name"];
	}
}

mysql_close($connect);

?>