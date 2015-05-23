/**
 * @brief 어플리케이션 정보 액티비티
 * @details 어플리케이션 앱 정보에 관련 내용을 띄워주는 액티비티입니다.
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
		
		//액션바 ( 툴바) 설정 부분
		toolbar = (Toolbar) findViewById(R.id.info_toolbar);
		toolbar.setTitle("앱정보");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);
	}

}