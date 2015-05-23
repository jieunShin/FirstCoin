<?// reg_id값을 db에서 가져옴
$connect = mysql_connect("localhost", "root", "apmsetup");

mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");

mysql_query("set names utf-8");

mysql_select_db("bitpay",$connect);


//// 새로운 데이터 확인
$url = "https://blockchain.info/tobtc?currency=KRW&value=100000";

$options = array(
	CURLOPT_RETURNTRANSFER => true,     // return web page
    CURLOPT_HEADER         => false,    // don't return headers
    CURLOPT_FOLLOWLOCATION => true,     // follow redirects
    CURLOPT_ENCODING       => "",       // handle all encodings
    CURLOPT_USERAGENT      => "spider", // who am i
    CURLOPT_AUTOREFERER    => true,     // set referer on redirect
    CURLOPT_CONNECTTIMEOUT => 120,      // timeout on connect
    CURLOPT_TIMEOUT        => 120,      // timeout on response
    CURLOPT_MAXREDIRS      => 10,       // stop after 10 redirects
    CURLOPT_SSL_VERIFYPEER => false     // Disabled SSL Cert checks
);

$ch      = curl_init( $url );
curl_setopt_array( $ch, $options );
$content = curl_exec( $ch );
$err     = curl_errno( $ch );
$errmsg  = curl_error( $ch );
$header  = curl_getinfo( $ch );
curl_close( $ch );

$header['errno']   = $err;
$header['errmsg']  = $errmsg;
$header['content'] = $content;
$btc = $header["content"];

echo $btc;

$date = date("Y-m-d H:i:s");
$query = "update btcinfo set btc=" . $btc . ", update_time = '" . $date . "' where id=1";
mysql_query($query);

?>