<?
/*
	filename : pos_remote_response_ok_2.php

수정 후 ---------------------
	자바에서 받는 변수 : order_id
	기능 : total_btc 생성 & 삽입, qrcode 스트링 생성 & 삽입
	원거리주문 승낙, db 업데이트, user로 push
*/

$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

// db update

$order_id = $_POST[order_id];

// 해당 order_id의 total_btc, user_email, order_time을 가져옴
$getUserEmail = "select total_btc, store_name, user_email, order_time from orders, merchant where order_id = " . $order_id . " and orders.merchant_id=merchant.merchant_id";
//echo $getUserEmail;
$result =  mysql_query($getUserEmail);
$row_array = mysql_fetch_array($result);
$total_btc = $row_array[total_btc];
$store_name = $row_array[store_name];
$user_email = $row_array[user_email];
$order_time = $row_array[order_time];

/// address 가져오기
$get_address_sql = "select address from address order by update_time desc limit 0,1";
$address_result = mysql_query($get_address_sql);
$row_array = mysql_fetch_array($address_result);
$address = $row_array[address];

$qr_code = "bitcoin:" . $address . "?label=coinplug&amount=" . $total_btc . "&message=orderid" . $order_id;
//echo $qr_code . "\n" ;
$status = "";

if($qr_code == "")
{
	$status = 1;
	$msg = $store_name . '에 주문하신 내역이 승인되었습니다.' . '현장결제 바랍니다.' ;
}
else 
{
	$status = 2;
	$msg = $store_name . '에 주문하신 내역이 승인되었습니다.' . '결제를 진행해주세요.' ;
}
$accept = "update orders set order_status=" . $status . ", qr_code = '" . $qr_code . "' where order_id = " . $order_id . ";" ;
//echo $accept;
mysql_query($accept);

/***********************************************************/
// user로 push
/***********************************************************/
 
// user_email에 등록된 regId값을 가져옴 
$getRegId = "select user_regId from customer where user_email = '" . $user_email . "'";
$result_regid = mysql_query($getRegId);
$regIdInfo = mysql_fetch_array($result_regid);
$regid = $regIdInfo['user_regId'];

//echo "\n" . $regid;
//echo $msg;
// push 전송
$headers = array(
            'Content-Type:application/json',
            'Authorization:key=AIzaSyB_p-Y6L74jbd-G54qn4ZeIxsXguGX21L0'	// API KEY
            );
 
 // 푸시 내용
	
	$arr = array();
	$arr['data'] = array();
	$arr['data']['type']="11";
	$arr['data']['title'] = '주문승인';
	$arr['data']['order_id'] = $order_id;
	$arr['data']['message'] = $msg;
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
?>
