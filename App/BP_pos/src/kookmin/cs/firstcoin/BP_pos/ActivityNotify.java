/**
 * @brief 공지사항 액티비티
 * @details 서버로부터 공지사항 관련 content를 받아와서 익스펜더블 리스트에 내용을 띄워줍니다.
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
	int cnt = 0;// 총 공지사항 개수를 세기 위한 변수
	private ArrayList<String> arrayGroup = new ArrayList<String>();// 그룹
	private HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();// 자식

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify);

		// 액션바 ( 툴바) 설정 부분
		toolbar = (Toolbar) findViewById(R.id.help_toolbar);
		toolbar.setTitle("공지사항");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		adapter = new AdptMain(this, arrayGroup, arrayChild);// 어댑터선언
		nDB = new DataNotify();// 서버가정
		listMain = (ExpandableListView) this.findViewById(R.id.expandableListView1);
		for (int i = 0; i < nDB.getCnt(); i++) {
			setArrayData(nDB.getNotification(i), i);
		}
	
		listMain.setAdapter(adapter);// 어댑터 설정
		// 그룹을 클릭했을 때
		listMain.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				arrayContent.clear();// 기존 모둔 자식 내용물 삭제
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
						arrayContent.add(res);// 서버에서 groupPosition번째 내용물 받아와서
												// arrayContent에 추가

					}// / 데이터를 받아옴
						// 받아온 데이터 형태 : 메뉴이름,가격\n
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//
				arrayChild.put(arrayGroup.get(groupPosition), arrayContent);// groupPosition번째
																			// 그룹의
																			// 자식에
																			// arrayContent
																			// 넣음
				return false;
			}
		});
		// 그룹을 펼칠 때 이벤트 - 여기서 펼치는 그룹을 제외한 모든 그룹 닫음
		listMain.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				int groupCnt = adapter.getGroupCount();// 총 그룹의 수
				// 자신을 제외한 모든 그룹 닫음
				for (int i = 0; i < groupCnt; i++) {//
					if (i != groupPosition) {// 자신을 제외한
						listMain.collapseGroup(i);// 그룹 닫기
					}
				}
			}
		});

	}

	// 그룹과 그 그룹에 대한 자식 만드는 함수 - 초기화
	private void setArrayData(String title, int cnt) {
		arrayGroup.add(title);// 그룹에 title(공지사항)을 추가
		arrayContent = new ArrayList<String>();// 자식어레이에 넣을 내용물
		arrayContent.add("");// 자식에 추가 - 공지사항 내용물 (빈 내용)

		arrayChild.put(arrayGroup.get(cnt), arrayContent);// cnt번째 그룹에
															// arrayContent넣음
	}
}