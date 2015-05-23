<?
/*
	filename : user_getStoreInfo_category.php
	category에 해당하는 상점의 정보를 받아오는 php
	java에서 보내는 값 : category, x1,y1, x2,y2
*/
$connect = mysql_connect($host, $user, $password);

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);

$category = $_REQUEST[category];
$x1 = $_REQUEST[x1];
$y1 = $_REQUEST[y1];
$x2 = $_REQUEST[x2];
$y2 = $_REQUEST[y2];

if ($x1 > $x2) {
	$min_x = $x2;
	$max_x = $x1;
}
else {
	$min_x = $x1;
	$max_x = $x2;	
}

if ($y1 > $y2) {
	$min_y = $y2;
	$max_y = $y1;
}
else {
	$min_y = $y1;
	$max_y = $y2;	
}

$query = "select category.category_name, merchant_id, store_name, address, phone, map_x, map_y "
    . "from merchant, category where merchant.category = '" . $category . "' and category.category_id = '" . $category . "'	
	and map_x between " . $min_x . " and " . $max_x . " and map_y between " . $min_y . " and " . $max_y; 
	
//echo $query;
$result = mysql_query($query);

$num_result = mysql_num_rows($result);

while($row_array = mysql_fetch_array($result))
{
	for($i=0; $i<7; $i++)
	{
		echo ($row_array[$i] . ",");
	}
	echo "\n";
}
?>