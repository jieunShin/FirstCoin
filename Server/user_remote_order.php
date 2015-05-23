<?php
/*
	filename : user_remote_order.php
	parameter : user_email, merchant_id, content, total, pickup 시간, order_info
	원거리주문을 받아 데이터베이스에 저장하고 해당 상점으로 푸시알림을 보낸다
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

// user

$merchant_id = $_POST[merchant_id];
$user_email = $_POST[user_email];
$order = $_POST[order];
$total = $_POST[total];
$pickup_time = $_POST[pickup];
$order_info = $_POST[order_info];
$pickup_datetime = date(Y) . "-" . date(m) . "-" . date(d) . " " . $pickup_time; 
$order_time = date("Y-m-d H:i:s", time());
$total_btc = $_POST[total_btc];

// echo $pickup_datetime;
$getCustomInfo = "select customer_id from customer where user_email = '" . $user_email . "'";

// echo $getCustomInfo;

$result1 = mysql_query($getCustomInfo);

$cumtomInfo = mysql_fetch_array($result1);
$customer_id = $cumtomInfo['customer_id'];
$user_regId = $cumtomInfo['user_regId'];

/*		
order 주문내역은 --- 메뉴이름:갯수/메뉴이름:갯수/ 
로 구성
*/	

if($order!="")	
{
// 원거리 주문 내역을 db에 삽입
	$sql = "insert into orders (merchant_id, pickup_time, order_time, user_email, total, total_btc, content, remote_order, customer_id, user_info, order_status) values('" . 
		$merchant_id . "', '" . $pickup_datetime . "', '" . $order_time . "', '" . $user_email . "', '" . $total . "', '" . $total_btc . "','" . $order . "', 'Y', '" . $customer_id . "','" . $order_info . "', 0)";

	$result = mysql_query($sql);
	$order_id = mysql_insert_id();

	// order_item에 추가	
	// $order 형태-----  메뉴이름:갯수/
	$data = explode('/', $order);
	$order_re = "";

	for($i=0; $i<count($data)-1; $i++)	/// '메뉴이름:갯수/'의 형태이므로 카운트-1 해줘야함!!!
	{
		$info = explode(':',$data[$i]);
		$menu_name = $info[0];
		$count = $info[1];
		
		// db로 전송 order_item
		
		$order_re = $order_re . $menu_name . " " . $count . "/" ;

		$item_query = "insert into order_item (order_id, count, menu_id) select " . $order_id . ", " . $count .
		", menu_id from menu where merchant_id = '" . $merchant_id . "' and name = '" . $menu_name . "'";
		
	//	echo $item_query . "\n";
		mysql_query($item_query);		
	}

	$get_address_sql = "select address from address order by update_time desc limit 0,1";
	$address_result = mysql_query($get_address_sql);
	$row_array = mysql_fetch_array($address_result);
	$address = $row_array[address];

	$qr_code = "bitcoin:" . $address . "?label=coinplug$amount=" . $total_btc . "message=" . $order_id;

	$sql_update = "update orders set qr_code = '" . $qr_code . "', orders.content = '" . $order_re . "' where orders.order_id = " . $order_id;
	mysql_query($sql_update);			

/***********************************************************/
// pos로 push 
/*********************************************************/

	// pos reg_id값을 db에서 가져옴
	$getRegId = "select reg_id from merchant where merchant_id = '" . $merchant_id . "'";

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
	$arr['data']['type']="01";
	$arr['data']['title'] = '주문도착';
	$arr['data']['message'] = '새로운 주문이 도착했습니다';
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
}
?>