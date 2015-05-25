package kookmin.cs.firstcoin.BP_order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.coinplug.lib.wallet.CPConfiguration;
import com.coinplug.lib.wallet.CPUserService;
import com.coinplug.lib.wallet.ICPUser;
import com.coinplug.lib.wallet.ICPWallet;
import com.coinplug.lib.wallet.listener.CPWBasicListener;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class ActivityLogin extends ActionBarActivity {


	private EditText mEmailEdit;
	private EditText mPassEdit;
	private CheckBox mAutoLogin;
	private ProgressDialog dialog = null;

	// private EditText mSignupEmail, mSignupPass;
	private CPConfiguration mConfiguration;
	private ICPUser mUserService;
	private ICPWallet mWalletService;

	// Gcm 관련 변수들 //
	private String SENDER_ID = "1080621977751";
	private GoogleCloudMessaging gcm;
	private String regid;

	// private static BtcInfo btcInfo = new BtcInfo();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Gcm 관련
		gcm = GoogleCloudMessaging.getInstance(this);

		SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
		String id = pref.getString("id", "");
		String pw = pref.getString("password", "");

		// 자동로그인 유효한 경우
		if (id != "" & pw != "") {
			UserInfo.setEmail(id);
			registerInBackground();
			startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
		}

		try {
			mConfiguration = CPConfiguration.getInstance(this);
			mConfiguration.setCoinplugKey(CLIENT_ID, CLIENT_SECRET);
			// testnet
			// mConfiguration.setBaseURL("https://sandbox.okbitcard.com");
			// real net
			mConfiguration.setBaseURL("https://www.coinplug.com");

			mUserService = CPUserService.getInstance(this);
		} catch (Exception e) {

			e.printStackTrace();
		}

		mEmailEdit = (EditText) findViewById(R.id.login_id);
		mEmailEdit.setText(id);
		mPassEdit = (EditText) findViewById(R.id.login_password);
		mAutoLogin = (CheckBox) findViewById(R.id.check_auto_login);

		findViewById(R.id.login_btn_login).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(ActivityLogin.this, "", "Loading..", true);

				// 로딩 다이얼로그 바로 출력
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						Looper.loop();
					}
				}).start();
				final String userName = mEmailEdit.getText().toString();
				final String password = mPassEdit.getText().toString();
				try {
					mUserService.userLogin(userName, password, new CPWBasicListener() {

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Toast.makeText(ActivityLogin.this, "아이디와 비밀번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers) {
							// TODO Auto-generated method stub
							UserInfo.setEmail(userName);
							runOnUiThread(new Runnable() {
								public void run() {
									dialog.dismiss();
								}
							});// 자동로그인 체크한 경우
							if (mAutoLogin.isChecked()) {
								SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								editor.putString("id", userName);
								editor.putString("password", password);
								editor.commit();
							} else {
								SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								editor.clear();
								editor.putString("id", userName);
								editor.commit();
							}

							registerInBackground();

							Toast.makeText(ActivityLogin.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();

							startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
							finish();

						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			}
		});
	}

	// Gcm 관련 함수
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					regid = gcm.register(SENDER_ID);
					sendRegistrationIdToBackend();

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
		// /db로 regid 전달
		HttpPost httppost;
		HttpClient httpclient;

		List<NameValuePair> nameList;

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/user_set_regId.php");

			nameList = new ArrayList<NameValuePair>(2);

			nameList.add(new BasicNameValuePair("user_email", UserInfo.getEmail()));
			nameList.add(new BasicNameValuePair("reg_id", regid));

			httppost.setEntity(new UrlEncodedFormEntity(nameList));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			if (response.contains("Success")) {
				Log.e("log_gcm", "gcm등록성공" + regid);
			} else {
				Log.e("log_gcm", "gcm등록실패");
			}
		} catch (Exception e) {
		}

	}

}
