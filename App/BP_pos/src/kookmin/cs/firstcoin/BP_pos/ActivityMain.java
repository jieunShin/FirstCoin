/**
 * @brief �α��� �� ����ڿ��� �������� ���ο�Ƽ��Ƽ �Դϴ�.
 * @details ����ȭ�鿡 ��Ÿ���� �׼ǹٿ� �� �������� drawer�޴� ���� ������ ��Ƽ��Ƽ Ŭ�����Դϴ�.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class ActivityMain extends ActionBarActivity {

	// Drawer ���� ������
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView mDrawerList;
	private Toolbar toolbar;

	// View Pager ���� ������
	ViewPager pager;
	private String titles[] = new String[] { "�ֹ��ϱ�", "�ŷ� ����", "���ġ", "���Ÿ� �ֹ�Ȯ��" };
	SlidingTabLayout slidingTabLayout;

	// Gcm ���� ������ //
	private String SENDER_ID = "1080621977751";
	private GoogleCloudMessaging gcm;
	private String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.navdrawer);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(UserInfo.getShopName());
		toolbar.setTitleTextColor(Color.WHITE);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
		}
		pager = (ViewPager) findViewById(R.id.viewpager);
		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

		slidingTabLayout.setViewPager(pager);
		slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return Color.YELLOW;
			}
		});

		drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
		mDrawerLayout.setDrawerListener(drawerToggle);
		String[] values = new String[] { "��������", "����", "������", "��й�ȣ ����", "�α׾ƿ�", "����" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, values);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0: // ��������
					startActivity(new Intent(ActivityMain.this, ActivityNotify.class));
					mDrawerLayout.closeDrawer(Gravity.START);
					break;
				case 1: // ����
					startActivity(new Intent(ActivityMain.this, ActivityHelp.class));
					mDrawerLayout.closeDrawer(Gravity.START);
					break;
				case 2: // ������
					startActivity(new Intent(ActivityMain.this, ActivityInfo.class));
					mDrawerLayout.closeDrawer(Gravity.START);
					break;
				case 3: // ��й�ȣ ����
					startActivity(new Intent(ActivityMain.this, ActivityChangepw.class));
					mDrawerLayout.closeDrawer(Gravity.START);
					break;
				case 4: // �α׾ƿ�
					SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.clear();
					editor.putString("id", UserInfo.getId());
					editor.commit();

					UserInfo.setId("");
					UserInfo.setShopName("");

					Toast.makeText(ActivityMain.this, "�α׾ƿ� �Ǿ����ϴ�", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(ActivityMain.this, ActivityLogin.class);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear
																		// back
																		// stack
					// ����
					moveTaskToBack(true);
					finish();
					startActivity(myIntent);
					android.os.Process.killProcess(android.os.Process.myPid());
					mDrawerLayout.closeDrawer(Gravity.START);
					break;
				case 5: // ������
					finishApp();
					break;
				}
			}

		});
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case android.R.id.home:
			mDrawerLayout.openDrawer(Gravity.START);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	void finishApp() {
		moveTaskToBack(true);
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			super.onBackPressed();

		}
	}

	// --------------- back��ư ������ �ÿ� ---------------//
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			} else {
				// finish();
				AlertDialog.Builder d = new AlertDialog.Builder(this);
				d.setTitle("�ȳ�");
				d.setMessage("�����Ͻðڽ��ϱ�?");
				d.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				d.setPositiveButton("��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// MainNavigationOverActionbarActivity.this.finish();
						finishApp();

					}
				}).show();
			}
			return true;
		}
		// MainNavigationOverActionbarActivity.this.finish();
		return super.onKeyDown(keyCode, event);
	}
}
