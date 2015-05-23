/**
 * @brief 거래내역 데이터 액티비티
 * @details 거래내역을 서버로부터 불러와서 저장
 */

package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DataTransaction {
	private ArrayList<Transaction> mTransactions;
	private Context context;
	// 자신의 인스턴스를 멤버로 가진다.
	private static DataTransaction sTransactionData;
	// 통신
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	// --------------- ---------------//
	private DataTransaction(Context context) {
		this.context = context;
		mTransactions = new ArrayList<Transaction>();
		setTransactionList();
	}

	// --------------- 외부에서 TransactionData호출할 때 사용하는 메소드 ---------------//
	public static DataTransaction get(Context c) {
		if (sTransactionData == null) {
			sTransactionData = new DataTransaction(c.getApplicationContext());
		}
		return sTransactionData;
	}

	// --------------- 외부에서 ArrayList<Transaction>를 호출할 때 사용하는 메소드
	// ---------------//
	public ArrayList<Transaction> getTransactions() {
		return mTransactions;
	}

	public void setListClear() {
		mTransactions.clear();
	}

	// --------------- 서버에서 데이타 받아옴 ---------------//
	public void setTransactionList() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/pos_transaction_init.php");

		// post로 보낼 정보 입력
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / 데이터를 받아옴
			// 받아온 데이터 형태 : 날짜,금액,주문내역\n
			String[] data = response.split("\n");

			for (int i = 0; i < data.length; i++) {
				Transaction trx = new Transaction(data[i]);
				mTransactions.add(trx);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
