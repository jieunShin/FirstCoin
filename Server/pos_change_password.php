<?
/*
   filename : pos_change_password.php
   자바에서 받는 변수 : id, password, 바꿀password, 바꿀 password 한번 더 입력한것
   기능 : 사용자의 비밀번호 변경

   사용자의 아이디와 비밀번호를 입력하면 회원인지 인증을 한다.
   회원임이 확인되면 새로운 비밀번호로 변경시켜준다.
   이 때 새로 입력한 비밀번호와 다시 입력한 값이 같아야 변경된다.
*/
$connect = mysql_connect($host, $user, $password) or die("메뉴 목록을 불러올 수 없습니다.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

//사용자를 인증하기 위해 id와 password를 입력받음
$id = $_POST[id];
$password = $_POST[password];

//새로 입력한 두 비밀번호
$new_pw = $_POST[new_password];
$new_pw2 = $_POST[new_password2];

//db에 등록되어있는 회원인지 확인
$qry = "select * from merchant where login_id = '" . $id . "' and password = '" . $password  ."'";

$result = mysql_query($qry);
$num = mysql_num_rows($result);
//echo $num;

//num이 0이면 일치하는 회원정보가 없다는 의미이므로 비밀번호 변경이 불가능
if(mysql_num_rows($result)==0) {
	echo "User not found";
}
else {

	if($new_pw == $new_pw2) {
		$qry = "update merchant set password = '" . $new_pw . "' where login_id = '".$id."'";
		$result = mysql_query($qry);
		echo "Success";
	}
	else
		echo "Can't change password";
}

mysql_close();
?>