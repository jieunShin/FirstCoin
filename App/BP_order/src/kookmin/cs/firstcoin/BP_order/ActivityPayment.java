package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityPayment extends ActionBarActivity {

	private TextView mNowBtc;
	private TextView ShopText;

	private TextView text_sumKrw;
	private TextView text_sumBtc;
	private Button mBtnModify;
	private Button mBtnPay;
	private ListView listView;
	MySQLiteHandler handler;
	private Toolbar toolbar;

	ArrayList<Product> mData = null;
	OrderAdapter orderAdapter;

	private String merchant_id;
	private int totalPrice;
	private String totalSum;
	private String totalBtc;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.pay_toolbar);
		toolbar.setTitle("�ֹ� Ȯ��");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		text_sumKrw = (TextView) findViewById(R.id.sum_krw);
		text_sumBtc = (TextView) findViewById(R.id.sum_btc);
		mBtnModify = (Button) findViewById(R.id.btn_modify_price);
		mBtnPay = (Button) findViewById(R.id.btn_payment);
		listView = (ListView) findViewById(R.id.order_statement);

		// �� �ݾ� ���� �о��
		Intent in = getIntent();
		merchant_id = in.getExtras().get("merchant_id").toString();
		totalPrice = in.getExtras().getInt("TotalSum");
		totalSum = totalPrice + "";
		text_sumKrw.setText(totalSum);

		totalBtc = getTotalBtc(totalSum);
		text_sumBtc.setText(totalBtc);
		
		// database open
		handler = MySQLiteHandler.open(getApplicationContext());
		// ���� db�� ����Ǿ� �ִ� ���� ���� �о��
		Cursor c = handler.select();

		mData = new ArrayList<Product>();

		while (c.moveToNext()) {
			Product pd = new Product();
			pd.setName(c.getString(c.getColumnIndex("name")));
			pd.setPrice(c.getString(c.getColumnIndex("price")));
			pd.setNumber(c.getString(c.getColumnIndex("orderNum")));

			mData.add(pd);
		}

		// listview�� ���� db ���� ��� ������
		orderAdapter = new OrderAdapter(this, mData);
		listView = (ListView) findViewById(R.id.order_statement);
		listView.setAdapter(orderAdapter);

		// �ֹ� ���� ��ư Ŭ����
		mBtnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// table�� ���� ��� �����
				for (int i = 0; i < mData.size(); i++) {
					String menuName = mData.get(i).getName();
					handler.delete(menuName);
				}

				// ���� activity�� ���ư���.
				finish();
			}
		});

		// ���� ��ư Ŭ����
		mBtnPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// drop sqlite table
				for (int i = 0; i < mData.size(); i++) {
					String menuName = mData.get(i).getName();
					handler.delete(menuName);
				}

				String orderString = "";
				// string ����
				for (int i = 0; i < mData.size(); i++) {
					orderString += mData.get(i).getName() + ":" + mData.get(i).getNumber() + "/";
				}

				Log.e("log_str", orderString);

				// ��ü ���� �� �� activity�� �̵�
				Intent in = new Intent(ActivityPayment.this, ActivityOrderInfo.class);
				in.putExtra("merchant_id", merchant_id);
				in.putExtra("TotalSum", totalPrice + "");
				in.putExtra("orderString", orderString);
				in.putExtra("totalBtc", totalBtc);
				startActivity(in);
				finish();

			}
		});

	} // OnCreate ��

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < mData.size(); i++) {
				String menuName = mData.get(i).getName();
				handler.delete(menuName);
			}

			finish();
		}
		return false;
	}
	
	public String getTotalBtc(String totalSum)
	{
		String totalBtc="";
			
		try {
			
	        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://203.246.112.131/get_total_btc.php");

	        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
	        params.add(new BasicNameValuePair("total", totalSum));
	        httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String response = httpclient.execute(httppost, responseHandler);
	        response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
	        totalBtc = response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return totalBtc;
	}

}
