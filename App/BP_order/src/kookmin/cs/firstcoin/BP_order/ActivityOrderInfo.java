/*
 * @file ActivityOrderInfo.java
 * @brief �ֹ� ������ ������ �����ϰ�, �������� ���ο��� �����Ѵ�
 * @details �ֹ� ���� ��ư�� Ŭ���ϸ�, �ֹ����̸��� �ֹ��� ����ó ���� ������ �Բ� �ֹ������� ������ ���޵ȴ�.
 *          ���������� �ֹ������� �����ϰ�, �ش� ������ ���� app���� gcm push�� �����Ѵ�.
 * */

package kookmin.cs.firstcoin.BP_order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kookmin.cs.firstcoin.order.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivityOrderInfo extends ActionBarActivity {

	TimePickerDialog timePickerDialog;
	Button btnTime;
	Button btnCreateOrder;
	EditText userNameEdit;
	EditText userPhoneEdit;
	TextView pickuptime;
	private Toolbar toolbar;

	private HttpPost httppost;
	private HttpClient httpclient;

	private List<NameValuePair> nameList;

	private String merchant_id;
	private String totalSum;
	private String orderString;
	private String pickupTime;
	private String totalBtc;

	private ProgressDialog dialog = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderinfo);

		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.orderinfo_toolbar);
		toolbar.setTitle("�ֹ� ���� �ۼ�");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		btnTime = (Button) findViewById(R.id.btn_timepicker);
		btnCreateOrder = (Button) findViewById(R.id.btn_createOrder);
		userNameEdit = (EditText) findViewById(R.id.userName);
		userPhoneEdit = (EditText) findViewById(R.id.userPhone);
		pickuptime = (TextView) findViewById(R.id.pickuptime);

		btnTime.setOnClickListener(new myClick());

		// intent�κ��� ���� ���� ����
		Intent in = getIntent();

		totalSum = in.getExtras().getString("TotalSum");
		merchant_id = in.getExtras().getString("merchant_id");
		orderString = in.getExtras().getString("orderString");
		totalBtc = in.getExtras().getString("totalBtc");

		findViewById(R.id.btn_createOrder).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(ActivityOrderInfo.this, "", "Loading..", true);
				// �ε� ���̾�α� �ٷ� ���
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						remote();

						Looper.loop();
					}
				}).start();
			}
		});

	}

	private void remote() {
		try {
			httpclient = new DefaultHttpClient();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httppost = new HttpPost("http://203.246.112.131/user_remote_order.php");
			boolean flagName = false;
			boolean flagPhone = false;
			String userName = userNameEdit.getText().toString();
			String userPhone = userPhoneEdit.getText().toString();
			// �̸��� ������ üũ
			for (int i = 0; i < userName.length(); i++) {
				if (userName.charAt(i) != ' ')
					flagName = true;
			}
			// ��ȭ��ȣ�� ������ üũ
			for (int i = 0; i < userPhone.length(); i++) {
				if (userPhone.charAt(i) != ' ')
					flagPhone = true;
			}
			if (flagName == false && flagPhone == false) {
				AlertDialog.Builder d = new AlertDialog.Builder(this);
				d.setTitle("���!");
				d.setMessage("�̸��� ��ȭ��ȣ�� �Է��ϼ���!");
				d.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				d.show();
				dialog.dismiss();
				return;
			} else if (flagName == false) {
				AlertDialog.Builder d = new AlertDialog.Builder(this);
				d.setTitle("���!");
				d.setMessage("�̸��� �Է��ϼ���!");
				d.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				d.show();
				dialog.dismiss();
				return;
			} else if (flagPhone == false) {
				AlertDialog.Builder d = new AlertDialog.Builder(this);
				d.setTitle("���!");
				d.setMessage("��ȭ��ȣ�� �Է��ϼ���!");
				d.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				d.show();
				dialog.dismiss();
				return;
			}

			nameList = new ArrayList<NameValuePair>(7);
			nameList.add(new BasicNameValuePair("merchant_id", merchant_id));
			nameList.add(new BasicNameValuePair("user_email", UserInfo.getEmail()));
			nameList.add(new BasicNameValuePair("order", orderString));
			nameList.add(new BasicNameValuePair("total", totalSum));
			nameList.add(new BasicNameValuePair("pickup", pickupTime));
			nameList.add(new BasicNameValuePair("order_info", userName + " / " + userPhone));
			nameList.add(new BasicNameValuePair("total_btc", totalBtc));
			Log.e("totalBtc", totalBtc);

			httppost.setEntity(new UrlEncodedFormEntity(nameList, "utf-8"));

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
			Log.e("log_res", response);

			// finish();

			runOnUiThread(new Runnable() {
				public void run() {
					dialog.dismiss();
					Intent intent = new Intent(ActivityOrderInfo.this, ActivityMain.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class myClick implements View.OnClickListener {
		public void onClick(final View v) {
			OnTimeSetListener callback = new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// �ÿ� ���� ������ �����ֱ�
					pickupTime = hourOfDay + ":" + minute;
					pickuptime.setText(pickupTime);
				}
			};

			Date date = new Date();
			SimpleDateFormat hour = new SimpleDateFormat("HH");
			SimpleDateFormat minute = new SimpleDateFormat("mm");
			int strHour = Integer.parseInt(hour.format(date));
			int strMinute = Integer.parseInt(minute.format(date));

			timePickerDialog = new TimePickerDialog(v.getContext(), callback, strHour, strMinute, true);
			timePickerDialog.show();

		}
	}
}