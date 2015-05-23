<?php
/*
	filename : user_remote_payment.php
	parameter : user_email, order_id
	
	원거리주문 결제가 완료되었을 때 
	db에 시간 업데이트 & pos로 push
*/

$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$order_id = $_POST[order_id];
$payment_time = date("Y-m-d H:i:s", time());

// update orders set payment_time=0 where order_id=150
// 원거리 주문 내역을 db에 삽입
$sql = "update orders set order_status=3, payment_time = '" . $payment_time . "' where order_id = " . $order_id;
$result = mysql_query($sql);

/***********************************************************/
// pos로 push 
 /*********************************************************/
 
 // pos reg_id값을 db에서 가져옴
	$getRegId = "select merchant.reg_id from merchant, orders where order_id = " . $order_id . " and orders.merchant_id = merchant.merchant_id";

	$result2 = mysql_query($getRegId);
	
	$regIdInfo = mysql_fetch_array($result2);
	$regid = $regIdInfo['reg_id'];
	  
  $headers = array(
            'Content-Type:application/json',
            'Authorization:key=AIzaSyB_p-Y6L74jbd-G54qn4ZeIxsXguGX21L0'	// API KEY
            );
 
    // 푸시 내용, data 부분을 자유롭게 사용해 클라이언트에서 분기할 수 있음.
    $arr = array();
    $arr['data'] = array();
	$arr['data']['type']="02";
    $arr['data']['title'] = '결제완료';
    $arr['data']['message'] = '결제가 완료되었습니다.';
	$arr['data']['order_id'] = $order_id;
    $arr['registration_ids'] = array();
    $arr['registration_ids'][0] = $regid;
 
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'https://android.googleapis.com/gcm/send');
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS,json_encode($arr));
    $response = curl_exec($ch);
    curl_close($ch);
 
    // 푸시 전송 결과 반환.
    $obj = json_decode($response);
 
    // 푸시 전송시 성공 수량 반환.
    $cnt = $obj->{"success"};
	echo $cnt;
 mysql_close($connect);
 ?>