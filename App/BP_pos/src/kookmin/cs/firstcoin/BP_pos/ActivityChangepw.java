/**
 * @brief �н����带 �ٲٴ� ��Ƽ��Ƽ
 * @details ���̵�� ���� ��й�ȣ �׸��� ������ �� ��й�ȣ�� ������ ������ ���� ��й�ȣ�� �� ��й�ȣ�� �������ݴϴ�. 
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

		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.pw_toolbar);
		toolbar.setTitle("��й�ȣ ����");
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
								"���� ��й�ȣ�� �Է����ּ���.", Toast.LENGTH_SHORT)
								.show();
					} else if (newPassword.length()<1 || newPassword2.length()<1) {
						Toast.makeText(ActivityChangepw.this,
								"���ο� ��й�ȣ�� �Է����ּ���.", Toast.LENGTH_SHORT)
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
									"���ο� ��й�ȣ�� ���� �ٸ��� �ԷµǾ����ϴ�.",
									Toast.LENGTH_SHORT).show();
						} else if (response.contains("User not found")) {
							Toast.makeText(ActivityChangepw.this,
									"���� ��й�ȣ�� �ùٸ��� �ʽ��ϴ�.", Toast.LENGTH_SHORT)
									.show();
						} else if (response.contains("Success")) {
							Toast.makeText(ActivityChangepw.this,
									"��й�ȣ�� ����Ǿ����ϴ�.", Toast.LENGTH_SHORT)
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