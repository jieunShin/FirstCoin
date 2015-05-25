package kookmin.cs.firstcoin.BP_order;

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
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;

public class TransactionData {
	private ArrayList<Transaction> mTransactions;
	private Context context;
	// �ڽ��� �ν��Ͻ��� ����� ������.
	private static TransactionData sTransactionData;
	// ���
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	// --------------- ---------------//
	private TransactionData(Context context) {
		this.context = context;
		mTransactions = new ArrayList<Transaction>();
		setTransactionList();
	}

	// --------------- �ܺο��� TransactionDataȣ���� �� ����ϴ� �޼ҵ� ---------------//
	public static TransactionData get(Context c) {
		if (sTransactionData == null) {
			sTransactionData = new TransactionData(c.getApplicationContext());
		}
		return sTransactionData;
	}

	// --------------- �ܺο��� ArrayList<Transaction>�� ȣ���� �� ����ϴ� �޼ҵ�
	// ---------------//
	public ArrayList<Transaction> getTransactions() {
		return mTransactions;
	}

	public void setListClear() {
		mTransactions.clear();
	}

	// --------------- �������� ����Ÿ �޾ƿ� ---------------//
	@SuppressLint("NewApi")
	public void setTransactionList() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/user_transaction_init.php");

		// post�� ���� ���� �Է�
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("user_email", UserInfo.getEmail()));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / �����͸� �޾ƿ�
			// �޾ƿ� ������ ���� : ��¥,�ݾ�,�ֹ�����\n
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
