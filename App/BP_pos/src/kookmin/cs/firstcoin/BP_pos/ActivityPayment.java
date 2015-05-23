/**
 * @brief ���� ��Ƽ��Ƽ Ŭ����
 * @details �ֹ��ǿ��� �ֹ��� ��� �ֹ����� ������ ����Ʈ�� ������Ʈ�� ���ְ� �ֹ������� ���� ��쿡�� �ֹ������� ���ư��� ������ư�� ���� ��쿡�� ���� DB�� �ֹ������� �����ŵ�ϴ�. 
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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class ActivityPayment extends ActionBarActivity {
	int smallerDimension;

	// db�� ����Ǿ����� Ȯ���ϱ� ���� ����
	int flag = 0;
	private TextView mNowBtc;
	private TextView ShopText;

	private TextView text_sumKrw;
	private TextView text_sumBtc;
	private TextView text_btc;

	private Button mBtnModify;
	private Button mBtnPay;
	private ListView listView;
	MySQLiteHandler handler;
	private Toolbar toolbar;

	ArrayList<Product> mData = null;
	OrderAdapter orderAdapter;

	private int totalPrice;
	private String totalSum;
	private String orderId_db;
	private String OrderString;
	private String totalBtc;

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	private List<NameValuePair> params;
	private ResponseHandler<String> responseHandler;

	//auto checker
	private HttpClient httpclient_auto;
	private HttpPost httppost_auto;
	private HttpResponse httpresponse_auto;
	
	private AlertDialog qr_dialog;
	private FragmentOrder orderfrg;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		// Find screen size
		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		int height = point.y;
		smallerDimension = width < height ? width : height;
		smallerDimension = smallerDimension * 3 / 4;
		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.pay_toolbar);
		mNowBtc = (TextView) findViewById(R.id.main_btc_info);
		text_sumKrw = (TextView) findViewById(R.id.sum_krw);
		text_sumBtc = (TextView) findViewById(R.id.sum_btc);
		text_btc = (TextView) findViewById(R.id.qrcode_text_price);

		mBtnModify = (Button) findViewById(R.id.btn_modify_price);
		mBtnPay = (Button) findViewById(R.id.btn_payment);
		listView = (ListView) findViewById(R.id.order_statement);

		// �� �ݾ� ���� �о��
		Intent in = getIntent();
		totalPrice = in.getExtras().getInt("TotalSum");
		totalSum = totalPrice + "";
		text_sumKrw.setText(totalSum);

		// btc
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
				Log.e("log_payment_content", orderString);
				OrderString = orderString;

				// / db�� �ֹ������� �����Ѵ� . �� �� orderID�� �޾ƿ´�
				try {
					StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
					httpclient = new DefaultHttpClient();
					httppost = new HttpPost("http://203.246.112.131/pos_order_insert.php");

					params = new ArrayList<NameValuePair>(4);
					params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));
					params.add(new BasicNameValuePair("total", Integer.toString(totalPrice)));
					params.add(new BasicNameValuePair("content", orderString));
					params.add(new BasicNameValuePair("total_btc", totalBtc));

					httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String response = httpclient.execute(httppost, responseHandler);
					// /////////response���� order_id�� ����Ǿ��ִ�.
					orderId_db = response;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy); // ���������� ��Ʈ��ũ ����

				actionCreateOrder();

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

	public void actionCreateOrder() {
		// ���� : qr�ڵ带 �����ϴ� �κ�
		// ���� ��ǥ : ��Ʈ������ �޴� �ּҿ� ��Ʈ���� ȯ��ݾ��� db�κ��� �����޾Ƽ� qr�ڵ带 ����
		// qr�ڵ忡 ���� �� ���� : ��Ʈ���� �ּ�, �ݾ�, order_id
		// ȣ���ϴ� php �̸� : pos_get_address.php
		// php�� �����ϴ� ���� : order_id
		// php���� �޴� ���� : address,total_btc(�����ؾ��� total_btc�� ������)

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/pos_get_payment_info.php");

			Log.e("orderId_db", orderId_db);

			params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("order_id", orderId_db));

			httppost.setEntity(new UrlEncodedFormEntity(params));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			String qr_text = response;

			actionCreateQRcode(qr_text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void actionCreateQRcode(String qr_text) {
		/**********************************************
		 * 
		 * QR�ڵ� �����ϴ� �κ�!!!!!!!!!!!!!!!!!!!!!
		 * 
		 ***************************/

		LayoutInflater inflater = getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.dialog_qrcode, null);

		Log.e("log_total", qr_text);
		// Encode with a QR Code image

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qr_text, null, Contents.Type.TEXT,
				BarcodeFormat.QR_CODE.toString(), smallerDimension);
		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			ImageView myImage = (ImageView) dialoglayout.findViewById(R.id.qrcode_image_address);
			myImage.setImageBitmap(bitmap);
			Log.v("debud", "myImage");
		} catch (WriterException e) {
			e.printStackTrace();
			Log.v("debud", "catch");
		}
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(dialoglayout);
		builder.setNegativeButton("�������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("log_dia", "cancel");
				httpclient_auto.getConnectionManager().shutdown();

				try {
					HttpClient httpclient_cancel = new DefaultHttpClient();
					HttpPost httppost_cancel = new HttpPost("http://203.246.112.131/pos_order_cancel.php");

					Log.e("orderId_db", orderId_db);

					params = new ArrayList<NameValuePair>(1);
					params.add(new BasicNameValuePair("order_id", orderId_db));

					httppost_cancel.setEntity(new UrlEncodedFormEntity(params));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();

					String response = httpclient_cancel.execute(httppost_cancel, responseHandler);
					response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

				} catch (Exception e) {
					e.printStackTrace();
				}

				Toast.makeText(ActivityPayment.this, "������ ��ҵǾ����ϴ�", Toast.LENGTH_LONG).show();
				qr_dialog.cancel();
				finish();
				startActivity(new Intent(ActivityPayment.this, ActivityMain.class));
			}
		});
		
		//���̾�α׿��� back ��ư ������ �� ó��
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog,
					int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.e("log_dia", "back _ cancel");
					httpclient_auto.getConnectionManager().shutdown();

					try {
						HttpClient httpclient_cancel = new DefaultHttpClient();
						HttpPost httppost_cancel = new HttpPost("http://203.246.112.131/pos_order_cancel.php");

						Log.e("orderId_db", orderId_db);

						params = new ArrayList<NameValuePair>(1);
						params.add(new BasicNameValuePair("order_id", orderId_db));

						httppost_cancel.setEntity(new UrlEncodedFormEntity(params));
						ResponseHandler<String> responseHandler = new BasicResponseHandler();

						String response = httpclient_cancel.execute(httppost_cancel, responseHandler);
						response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

					} catch (Exception e) {
						e.printStackTrace();
					}

					Toast.makeText(ActivityPayment.this, "������ ��ҵǾ����ϴ�", Toast.LENGTH_LONG).show();
					qr_dialog.cancel();
					finish();
					startActivity(new Intent(ActivityPayment.this, ActivityMain.class));
					return true;
				}
				return false;
			}
		});

		qr_dialog = builder.create();
		qr_dialog.show();
		


		new Thread(new Runnable() {

			public void run() {
				Looper.prepare();
				checker();
				Looper.loop();
			}
		}).start();
	}

	private void checker() {
		try {
			httpclient_auto = new DefaultHttpClient();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httppost_auto = new HttpPost("http://203.246.112.131/pos_auto_checker.php");

			params = new ArrayList<NameValuePair>(1);

			params.add(new BasicNameValuePair("order_id", orderId_db));
			httppost_auto.setEntity(new UrlEncodedFormEntity(params));
			responseHandler = new BasicResponseHandler();
			String httpresponse_auto = httpclient.execute(httppost_auto, responseHandler);
			httpresponse_auto = new String(httpresponse_auto.getBytes("ISO-8859-1"), "UTF-8");

			qr_dialog.cancel();
			Toast.makeText(ActivityPayment.this, "������ �Ϸ�Ǿ����ϴ�", Toast.LENGTH_LONG).show();
			startActivity(new Intent(ActivityPayment.this, ActivityMain.class));
			finish();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getTotalBtc(String totalSum) {
		String totalBtc = "";
		String response = "";

		try {

			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://203.246.112.131/pos_get_total_btc.php");

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("total", totalSum));
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
			// // $btc,$total_btc

		} catch (Exception e) {
			e.printStackTrace();
		}

		// string remake
		String[] data_token = response.split(",");
		String btc_data = data_token[0];
		totalBtc = data_token[1];

		// ////////////////////// btc
		double btc_1krw; // 1���� btc ��Ʈ����
		double krw_1btc; // 1��Ʈ������ krw��

		krw_1btc = 100000 / Double.parseDouble(btc_data);
		btc_1krw = Double.parseDouble(btc_data) / 100000;

		toolbar.setTitle("1BTC = " + krw_1btc + " KRW");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		return totalBtc;
	}
}