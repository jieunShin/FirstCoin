<?
/*
   filename : pos_logcheck.php
   자바에서 받는 변수 : id, password
   기능 : 로그인 시 id와 password와 맞는지 확인. 인증되면 상점 이름을 보여준다.
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