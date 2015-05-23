/**
 * @brief 패스워드를 바꾸는 액티비티
 * @details 아이디와 현재 비밀번호 그리고 변경할 새 비밀번호를 서버에 보내서 기존 비밀번호를 새 비밀번호로 변경해줍니다. 
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

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityChangepw extends ActionBarActivity {

	private TextView idText;
	private EditText curpassEdit;
	private EditText newpassEdit;
	private EditText newpassEdit2;
	private Button yesBtn;
	private Button noBtn;
	private Toolbar toolbar;

	private HttpPost httppost;
	private HttpClient httpclient;

	private List<NameValuePair> nameList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepw);

		idText = (TextView) findViewById(R.id.cpw_id);
		curpassEdit = (EditText) findViewById(R.id.cpw_cpw);
		newpassEdit = (EditText) findViewById(R.id.cpw_npw);
		newpassEdit2 = (EditText) findViewById(R.id.cpw_npw2);
		yesBtn = (Button) findViewById(R.id.cpw_yes);
		noBtn = (Button) findViewById(R.id.cpw_no);

		idText.setText("ID : " + UserInfo.getId());

		// 액션바 ( 툴바) 설정 부분
		toolbar = (Toolbar) findViewById(R.id.pw_toolbar);
		toolbar.setTitle("비밀번호 변경");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		yesBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					httpclient = new DefaultHttpClient();
					StrictMode
							.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
									.permitNetwork().build());
					httppost = new HttpPost(
							"http://203.246.112.131/pos_change_password.php");

					final String Id = UserInfo.getId();
					final String curPassword = curpassEdit.getText().toString();
					final String newPassword = newpassEdit.getText().toString();
					final String newPassword2 = newpassEdit2.getText()
							.toString();

					if (curPassword.length()<1) {
						Toast.makeText(ActivityChangepw.this,
								"현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT)
								.show();
					} else if (newPassword.length()<1 || newPassword2.length()<1) {
						Toast.makeText(ActivityChangepw.this,
								"새로운 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT)
								.show();
					} else {
						nameList = new ArrayList<NameValuePair>(4);

						nameList.add(new BasicNameValuePair("id", Id));
						nameList.add(new BasicNameValuePair("password",
								curPassword));
						nameList.add(new BasicNameValuePair("new_password",
								newPassword));
						nameList.add(new BasicNameValuePair("new_password2",
								newPassword2));

						httppost.setEntity(new UrlEncodedFormEntity(nameList,
								"utf-8"));
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String response = httpclient.execute(httppost,
								responseHandler);
						response = new String(response.getBytes("ISO-8859-1"),
								"UTF-8");

						Log.e("log_pw_response", response);

						if (response.contains("Can't change password")) {
							Toast.makeText(ActivityChangepw.this,
									"새로운 비밀번호가 서로 다르게 입력되었습니다.",
									Toast.LENGTH_SHORT).show();
						} else if (response.contains("User not found")) {
							Toast.makeText(ActivityChangepw.this,
									"현재 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT)
									.show();
						} else if (response.contains("Success")) {
							Toast.makeText(ActivityChangepw.this,
									"비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT)
									.show();
							finish();
						}

					}
				} catch (Exception e) {
				}

			}
		});
		noBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}