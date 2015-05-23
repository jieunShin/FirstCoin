<?php
/*
	filename : pos_order_insert.php
	자바에서 받는 변수 : merchant_id, total, total_btc, content
	기능 : 처음 주문을 하였을 때 호출되는 파일
	merchant_id와 total 값을 입력하고, 삽입 후의 order_id를 반환한다
	스트링형태의 주문내역을 split함수를 이용하여 메뉴와 수량을 분루하여 데이터베이스에 저장한다.
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$time = date("Y-m-d H:i:s");
$merchant_id = $_POST[merchant_id];
$total = $_POST[total];
$total_btc = $_POST[total_btc];
$content = $_POST[content];

$order_re = "";

////content 가공하기

/*
order 주문내역은 --- 메뉴이름:갯수/메뉴이름:갯수/ 
로 구성
*/

$sql = "insert into orders (merchant_id, order_time, total, content, total_btc) values(" . $merchant_id . ",'" . $time . "'," . $total . ",'" . $content . "'," . $total_btc . ")";

$result = mysql_query($sql);
$order_id = mysql_insert_id();

//// order_item
$data = explode('/', $content);

for($i=0; $i<count($data)-1; $i++)	/// '메뉴이름:갯수/'의 형태이므로 카운트-1 해줘야함!!!
{
	$info = explode(':',$data[$i]);
	$menu_name = $info[0];
	$count = $info[1];
	
	$order_re = $order_re . $menu_name . " " . $count . "/" ;
	
	// db로 전송 order_item
	$item_query = "insert into order_item (order_id, count, menu_id) select " . $order_id . ", " . $count .
	", menu_id from menu where merchant_id = '" . $merchant_id . "' and name = '" . $menu_name . "'";
	
	mysql_query($item_query);		
}
$sql_update = "update orders set orders.content = '" . $order_re . "' where orders.order_id = " . $order_id;
mysql_query($sql_update);
echo $order_id;
?>