/**
 * @brief �α��� ��Ƽ��Ƽ
 * @details �α����ϱ� ���ؼ� user�κ��� �Է¹��� ���̵�� ��й�ȣ�� ������ �����Ѵٸ� ���ξ�Ƽ��Ƽ�� ��Ƽ��Ƽ �̵���Ű�� ��Ƽ��Ƽ�Դϴ�.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.io.IOException;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class ActivityLogin extends ActionBarActivity {
	private EditText idEdit;
	private EditText passEdit;
	private ImageButton loginBtn;
	private CheckBox mAutoLogin;

	private ProgressDialog dialog = null;

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;

	private List<NameValuePair> nameList;

	// Gcm ���� ������ //
	final private String SENDER_ID = "1080621977751";
	private GoogleCloudMessaging gcm;
	private String regid;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		gcm = GoogleCloudMessaging.getInstance(this);

		SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
		String id = pref.getString("id","");
		String pw = pref.getString("password", "");
		String merchant_id = pref.getString("merchant_id", "");
		String shop_name = pref.getString("shop_name", "");
		
		// �ڵ��α��� ��ȿ�� ���
		if(id != "" & pw != "" & merchant_id != "" & shop_name != "")
		{
			UserInfo.setId(id);
			UserInfo.setMerchantId(merchant_id);
			UserInfo.setShopName(shop_name);
			registerInBackground();
			startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
		}
			
		// API9���� �����Ǵ� �Լ���....
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); // ���������� ��Ʈ��ũ ����

		idEdit = (EditText) findViewById(R.id.login_id);
		idEdit.setText(id);
		passEdit = (EditText) findViewById(R.id.login_password);
		loginBtn = (ImageButton) findViewById(R.id.login_log);
		mAutoLogin = (CheckBox) findViewById(R.id.check_auto_login);
		
		LinearLayout layer = (LinearLayout) findViewById(R.id.login_layer);

		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final int userIdlength = idEdit.getText().length();
				final int userPasswordlength = passEdit.getText().length();

				if (userIdlength == 0 || userPasswordlength == 0) {
					Toast.makeText(ActivityLogin.this, "���̵�� ��й�ȣ�� �Է��� �ּ���", Toast.LENGTH_SHORT).show();
					return;
				}
				// �α��� ��ư�� ������ ��� ��ٸ��� ���� ��µǴ� dialog
				dialog = ProgressDialog.show(ActivityLogin.this, "", "Loading..", true);

				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						login();
						Looper.loop();
					}
				}).start();
			}
		});
	}// end of onCreate

	// server�� login ��û
	private void login() {
		try {
			httpclient = new DefaultHttpClient();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httppost = new HttpPost("http://203.246.112.131/pos_logcheck.php");

			final String userId = idEdit.getText().toString();
			final String userPassword = passEdit.getText().toString();

			nameList = new ArrayList<NameValuePair>(2);

			nameList.add(new BasicNameValuePair("id", userId));
			nameList.add(new BasicNameValuePair("password", userPassword));
			httppost.setEntity(new UrlEncodedFormEntity(nameList));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// ���� �����͸� ","�� �������� �Ľ�
			final String[] str_token = response.split(",");

			runOnUiThread(new Runnable() {
				public void run() {
					dialog.dismiss();
				}
			});

			// equals not working
			if (str_token[0].contains("User found")) {
				runOnUiThread(new Runnable() {
					public void run() {
						UserInfo.setId(userId);
						UserInfo.setMerchantId(str_token[1]);
						UserInfo.setShopName(str_token[2]);

						Log.e("log_login", str_token[1]);
						Log.e("log_login", UserInfo.getMerchantId());

						registerInBackground();

						// �ڵ��α��� üũ�� ���
						if(mAutoLogin.isChecked())
						{
							SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
							SharedPreferences.Editor editor = pref.edit();
							editor.putString("id", userId);
							editor.putString("password", userPassword);
							editor.putString("merchant_id", str_token[1]);
							editor.putString("shop_name", str_token[2]);
							editor.commit();
						}
						else
						{
							SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
							SharedPreferences.Editor editor = pref.edit();
							editor.clear();
							editor.putString("id", userId);							
							editor.commit();
						}	
						

						Toast.makeText(ActivityLogin.this, UserInfo.getShopName() + "�� ȯ���մϴ�", Toast.LENGTH_SHORT)
								.show();
						startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
						finish();
					}
				});
			} else {
				Toast.makeText(ActivityLogin.this, "ȸ�������� �����ϴ�", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			dialog.dismiss();
			System.out.println("Exception:" + e.getMessage());
		}

	} // end login

	// Gcm ���� �Լ�
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					regid = gcm.register(SENDER_ID);
					sendRegistrationIdToBackend();
					Log.e("log_regId", regid);
				} catch (IOException ex) {
				}
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend() {
		// Your implementation here.
		// /db�� regid ����
		HttpPost httppost;
		HttpClient httpclient;

		List<NameValuePair> nameList;

		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/pos_set_regId.php");

			nameList = new ArrayList<NameValuePair>(2);

			nameList.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));
			nameList.add(new BasicNameValuePair("reg_id", regid));

			httppost.setEntity(new UrlEncodedFormEntity(nameList));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			if (response.contains("Success")) {
				Log.e("log_gcm", "gcm��ϼ���" + regid);
			} else {
				Log.e("log_gcm", "gcm��Ͻ���");
			}
		} catch (Exception e) {
		}

	}

}