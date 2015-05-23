/**
 * @brief �������� ��Ƽ��Ƽ
 * @details �����κ��� �������� ���� content�� �޾ƿͼ� �ͽ������ ����Ʈ�� ������ ����ݴϴ�.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ActivityNotify extends ActionBarActivity {
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	DataNotify nDB;
	ArrayList<String> arrayContent;
	AdptMain adapter;
	ExpandableListView listMain;
	String notification;
	String content;
	private Toolbar toolbar;
	int cnt = 0;// �� �������� ������ ���� ���� ����
	private ArrayList<String> arrayGroup = new ArrayList<String>();// �׷�
	private HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();// �ڽ�

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify);

		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.help_toolbar);
		toolbar.setTitle("��������");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		adapter = new AdptMain(this, arrayGroup, arrayChild);// ����ͼ���
		nDB = new DataNotify();// ��������
		listMain = (ExpandableListView) this.findViewById(R.id.expandableListView1);
		for (int i = 0; i < nDB.getCnt(); i++) {
			setArrayData(nDB.getNotification(i), i);
		}
	
		listMain.setAdapter(adapter);// ����� ����
		// �׷��� Ŭ������ ��
		listMain.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				arrayContent.clear();// ���� ��� �ڽ� ���빰 ����
				try {
					StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
					String url = new String("http://203.246.112.131/pos_notify_content.php?" + "title="
							+ URLEncoder.encode(nDB.getNotification(groupPosition), "UTF-8"));
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						String res = EntityUtils.toString(resEntity);
						res = new String(res.getBytes("ISO-8859-1"), "UTF-8");
						arrayContent.add(res);// �������� groupPosition��° ���빰 �޾ƿͼ�
												// arrayContent�� �߰�

					}// / �����͸� �޾ƿ�
						// �޾ƿ� ������ ���� : �޴��̸�,����\n
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//
				arrayChild.put(arrayGroup.get(groupPosition), arrayContent);// groupPosition��°
																			// �׷���
																			// �ڽĿ�
																			// arrayContent
																			// ����
				return false;
			}
		});
		// �׷��� ��ĥ �� �̺�Ʈ - ���⼭ ��ġ�� �׷��� ������ ��� �׷� ����
		listMain.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				int groupCnt = adapter.getGroupCount();// �� �׷��� ��
				// �ڽ��� ������ ��� �׷� ����
				for (int i = 0; i < groupCnt; i++) {//
					if (i != groupPosition) {// �ڽ��� ������
						listMain.collapseGroup(i);// �׷� �ݱ�
					}
				}
			}
		});

	}

	// �׷�� �� �׷쿡 ���� �ڽ� ����� �Լ� - �ʱ�ȭ
	private void setArrayData(String title, int cnt) {
		arrayGroup.add(title);// �׷쿡 title(��������)�� �߰�
		arrayContent = new ArrayList<String>();// �ڽľ�̿� ���� ���빰
		arrayContent.add("");// �ڽĿ� �߰� - �������� ���빰 (�� ����)

		arrayChild.put(arrayGroup.get(cnt), arrayContent);// cnt��° �׷쿡
															// arrayContent����
	}
}