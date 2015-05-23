package kookmin.cs.firstcoin.BP_order;

import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

public class HelpData {
	int cnt;
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	String allNotifStr = "";
	String[] notifStr;
	String[] notifContent;

	HelpData() {
		cnt = 0;
		notifContent = new String[100];
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/pos_help_title.php");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
			final String[] str_token = response.split("\n");
			cnt = str_token.length;
			StringTokenizer st = new StringTokenizer(response, "\n");// 토큰
			notifStr = new String[cnt];
			for (int i = 0; i < cnt; i++) {
				notifStr[i] = str_token[i];
			}

		} catch (Exception e) {
			// dialog.dismiss();

		}

	}

	private void trans() {

		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/pos_help_title.php");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
			final String[] str_token = response.split("\n");
			cnt = str_token.length;
			StringTokenizer st = new StringTokenizer(response, "\n");// 토큰
			notifStr = new String[cnt];
			for (int i = 0; i < cnt; i++) {
				notifStr[i] = str_token[i];
			}
		} catch (Exception e) {
			Log.e("notify2", e.toString());
		}
	}

	int getCnt() {
		return cnt;
	}

	String getNotifications() {
		return allNotifStr;
	}

	String getNotification(int index) {
		return notifStr[index];
	}

	String getNotifContent(int index) {
		return notifContent[index];
	}

}