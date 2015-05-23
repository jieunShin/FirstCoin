<?
/*
	filename : pos_remote_response_no.php
	자바에서 받는 변수 : order_id, msg
*/

$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);


// db update

$order_id = $_POST[order_id];
// $reject_msg = $_POST[msg];

$reject = "update orders set order_status=-1 where order_id = '" . $order_id . "';" ;
//echo $reject;
mysql_query($reject);
//select merchant.store_name from merchant, orders where orders.order_id=19 and orders.merchant_id=merchant.merchant_id

/***********************************************************/
// user로 push
/***********************************************************/
 
// 해당 order_id의 데이터를 가져옴
$getUserEmail = "select store_name, user_email, order_time from orders, merchant where order_id = " . $order_id . " and orders.merchant_id=merchant.merchant_id";
//echo $getUserEmail;
$result =  mysql_query($getUserEmail);
$row_array = mysql_fetch_array($result);
$store_name = $row_array[store_name];
$user_email = $row_array[user_email];
$order_time = $row_array[order_time];

// user_email에 등록된 regId값을 가져옴 
$getRegId = "select user_regId from customer where user_email = '" . $user_email . "'";
$result_regid = mysql_query($getRegId);
$regIdInfo = mysql_fetch_array($result_regid);
$regid = $regIdInfo['user_regId'];

//echo $regid;

// push 전송
$headers = array(
            'Content-Type:application/json',
            'Authorization:key=AIzaSyB_p-Y6L74jbd-G54qn4ZeIxsXguGX21L0'	// API KEY
            );
 
 // 푸시 내용
/*    $arr = array();
    $arr['data'] = array();
    $arr['data']['title'] = '새로운 주문도착';
    $arr['data']['message'] = '여기는 주문내용주문내용';
	$arr['data']['order_id'] = $order_id;
    $arr['registration_ids'] = array();
    $arr['registration_ids'][0] = $regid;*/
	
	$arr = array();
	$arr['data'] = array();
	$arr['data']['type']="12";
	$arr['data']['title'] = '주문거절';
	$arr['data']['order_id'] = $order_id;
	$arr['data']['message'] = $store_name . '에 주문하신 내역이 거절되었습니다.';
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
