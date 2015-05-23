package kookmin.cs.firstcoin.BP_order;

import kookmin.cs.firstcoin.order.R;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class ActivityAppInfo extends ActionBarActivity{
	private Toolbar toolbar;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_info);
		
		//액션바 ( 툴바) 설정 부분
		toolbar = (Toolbar) findViewById(R.id.info_toolbar);
		toolbar.setTitle("앱정보");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

	}

}