/**
 * @brief 결제 액티비티 클래스
 * @details 주문탭에서 주문한 모든 주문결제 내역을 리스트에 업데이트를 해주고 주문수정을 누를 경우에는 주문탭으로 돌아가고 결제버튼을 누를 경우에는 서버 DB에 주문내역을 저장시킵니다. 
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

	// db에 저장되었는지 확인하기 위한 변수
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
		// 액션바 ( 툴바) 설정 부분
		toolbar = (Toolbar) findViewById(R.id.pay_toolbar);
		mNowBtc = (TextView) findViewById(R.id.main_btc_info);
		text_sumKrw = (TextView) findViewById(R.id.sum_krw);
		text_sumBtc = (TextView) findViewById(R.id.sum_btc);
		text_btc = (TextView) findViewById(R.id.qrcode_text_price);

		mBtnModify = (Button) findViewById(R.id.btn_modify_price);
		mBtnPay = (Button) findViewById(R.id.btn_payment);
		listView = (ListView) findViewById(R.id.order_statement);

		// 총 금액 값을 읽어옴
		Intent in = getIntent();
		totalPrice = in.getExtras().getInt("TotalSum");
		totalSum = totalPrice + "";
		text_sumKrw.setText(totalSum);

		// btc
		totalBtc = getTotalBtc(totalSum);
		text_sumBtc.setText(totalBtc);
	

		// database open
		handler = MySQLiteHandler.open(getApplicationContext());
		// 현제 db에 저장되어 있는 값을 전부 읽어옴
		Cursor c = handler.select();

		mData = new ArrayList<Product>();

		while (c.moveToNext()) {
			Product pd = new Product();
			pd.setName(c.getString(c.getColumnIndex("name")));
			pd.setPrice(c.getString(c.getColumnIndex("price")));
			pd.setNumber(c.getString(c.getColumnIndex("orderNum")));

			mData.add(pd);
		}

		// listview에 현재 db 내용 모두 보여줌
		orderAdapter = new OrderAdapter(this, mData);
		listView = (ListView) findViewById(R.id.order_statement);
		listView.setAdapter(orderAdapter);

		// 주문 수정 버튼 클릭시
		mBtnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// table의 내용 모두 지우기
				for (int i = 0; i < mData.size(); i++) {
					String menuName = mData.get(i).getName();
					handler.delete(menuName);
				}

				// 이전 activity로 돌아간다.
				finish();
			}
		});

		// 결제 버튼 클릭시
		mBtnPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// drop sqlite table
				for (int i = 0; i < mData.size(); i++) {
					String menuName = mData.get(i).getName();
					handler.delete(menuName);
				}

				String orderString = "";
				// string 가공
				for (int i = 0; i < mData.size(); i++) {
					orderString += mData.get(i).getName() + ":" + mData.get(i).getNumber() + "/";
				}
				Log.e("log_payment_content", orderString);
				OrderString = orderString;

				// / db에 주문내역을 저장한다 . 이 때 orderID를 받아온다
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
					// /////////response에는 order_id가 저장되어있다.
					orderId_db = response;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy); // 강제적으로 네트워크 접속

				actionCreateOrder();

			}
		});

	} // OnCreate 끝

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
		// 기존 : qr코드를 생성하는 부분
		// 수정 목표 : 비트코인을 받는 주소와 비트코인 환산금액을 db로부터 제공받아서 qr코드를 생성
		// qr코드에 들어가야 할 정보 : 비트코인 주소, 금액, order_id
		// 호츨하는 php 이름 : pos_get_address.php
		// php로 전송하는 정보 : order_id
		// php에서 받는 정보 : address,total_btc(지불해야할 total_btc가 얼마인지)

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
		 * QR코드 생성하는 부분!!!!!!!!!!!!!!!!!!!!!
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
		builder.setNegativeButton("결제취소", new OnClickListener() {

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

				Toast.makeText(ActivityPayment.this, "결제가 취소되었습니다", Toast.LENGTH_LONG).show();
				qr_dialog.cancel();
				finish();
				startActivity(new Intent(ActivityPayment.this, ActivityMain.class));
			}
		});
		
		//다이얼로그에서 back 버튼 눌렀을 때 처리
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

					Toast.makeText(ActivityPayment.this, "결제가 취소되었습니다", Toast.LENGTH_LONG).show();
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
			Toast.makeText(ActivityPayment.this, "결제가 완료되었습니다", Toast.LENGTH_LONG).show();
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
		double btc_1krw; // 1원이 btc 비트코인
		double krw_1btc; // 1비트코인이 krw원

		krw_1btc = 100000 / Double.parseDouble(btc_data);
		btc_1krw = Double.parseDouble(btc_data) / 100000;

		toolbar.setTitle("1BTC = " + krw_1btc + " KRW");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		return totalBtc;
	}
}