<?
/*
	filename : pos_remote_status_update.php
	자바에서 받는 변수 : order_id, status
	기능 : 원거리주문의 상태가 
*/

$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);


// db update

$order_id = $_POST[order_id];
$status = $_POST[status];

$update = "update orders set order_status =" . $status . " where order_id = " . $order_id;
//echo $update;
mysql_query($update);

/***********************************************************/
// user로 push
/***********************************************************/
 
// 해당 order_id의 데이터를 가져옴
$getUserEmail = "select store_name, user_email from orders, merchant where order_id = " . $order_id . " and orders.merchant_id=merchant.merchant_id";
//echo $getUserEmail;
$result =  mysql_query($getUserEmail);
$row_array = mysql_fetch_array($result);
$store_name = $row_array[store_name];
$user_email = $row_array[user_email];

// user_email에 등록된 regId값을 가져옴 
$getRegId = "select user_regId from customer where user_email = '" . $user_email . "'";
$result_regid = mysql_query($getRegId);
$regIdInfo = mysql_fetch_array($result_regid);
$regid = $regIdInfo['user_regId'];


//echo $regid;

if($status == 5)
{
// push 전송
$headers = array(
            'Content-Type:application/json',
            'Authorization:key=AIzaSyB_p-Y6L74jbd-G54qn4ZeIxsXguGX21L0'	// API KEY
            );
	
////업데이트 상황에 따라 구체적 메시지 전송

	$arr = array();
	$arr['data'] = array();
	$arr['data']['type']="13";
	$arr['data']['title'] = '업데이트';
	$arr['data']['update'] = $status;
	
	$arr['data']['message'] = $store_name . '에 주문하신 상품이 준비되었습니다.';
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
}
?>
