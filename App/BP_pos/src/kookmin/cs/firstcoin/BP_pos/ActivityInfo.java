/**
 * @brief ���ø����̼� ���� ��Ƽ��Ƽ
 * @details ���ø����̼� �� ������ ���� ������ ����ִ� ��Ƽ��Ƽ�Դϴ�.
 */


package kookmin.cs.firstcoin.BP_pos;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class ActivityInfo extends ActionBarActivity{
	private Toolbar toolbar;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		//�׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.info_toolbar);
		toolbar.setTitle("������");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);
	}

}