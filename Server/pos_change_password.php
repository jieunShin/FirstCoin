<?
/*
   filename : pos_change_password.php
   �ڹٿ��� �޴� ���� : id, password, �ٲ�password, �ٲ� password �ѹ� �� �Է��Ѱ�
   ��� : ������� ��й�ȣ ����

   ������� ���̵�� ��й�ȣ�� �Է��ϸ� ȸ������ ������ �Ѵ�.
   ȸ������ Ȯ�εǸ� ���ο� ��й�ȣ�� ��������ش�.
   �� �� ���� �Է��� ��й�ȣ�� �ٽ� �Է��� ���� ���ƾ� ����ȴ�.
*/
$connect = mysql_connect($host, $user, $password) or die("�޴� ����� �ҷ��� �� �����ϴ�.");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

//����ڸ� �����ϱ� ���� id�� password�� �Է¹���
$id = $_POST[id];
$password = $_POST[password];

//���� �Է��� �� ��й�ȣ
$new_pw = $_POST[new_password];
$new_pw2 = $_POST[new_password2];

//db�� ��ϵǾ��ִ� ȸ������ Ȯ��
$qry = "select * from merchant where login_id = '" . $id . "' and password = '" . $password  ."'";

$result = mysql_query($qry);
$num = mysql_num_rows($result);
//echo $num;

//num�� 0�̸� ��ġ�ϴ� ȸ�������� ���ٴ� �ǹ��̹Ƿ� ��й�ȣ ������ �Ұ���
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