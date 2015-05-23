package kookmin.cs.firstcoin.BP_order;

import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;
import android.util.Log;

public class BtcInfo {
	private static double btc; // 1원이 btc 비트코인
	private static double krw; // 1비트코인이 krw원

	void setBtcInfo() {

		int cnt;
		HttpPost httppost;
		HttpResponse httpresponse;
		HttpClient httpclient;

		try {

			httpclient = new DefaultHttpClient();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httppost = new HttpPost("http://203.246.112.131/getBtc.php");

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);

			btc = 100000 / Double.parseDouble(response);
			krw = Double.parseDouble(response) / 100000;
		} catch (Exception e) {

		}
	}

	public static double getBtc() {
		return btc;
	}

	public double getKrw() {
		return krw;
	}
}
