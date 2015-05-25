/**
 * @brief ���۾�Ƽ��Ƽ
 * @details ���ø����̼��� �������� �� ���� ���� ����Ǵ� ��Ƽ��Ƽ.
 */

package kookmin.cs.firstcoin.BP_order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

public class ActivityStart extends ActionBarActivity {

	private ImageView img;
	private AnimationDrawable animation;

	private BtcInfo btcInfo;
	public UserInfo userInfo;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		img = (ImageView) findViewById(R.id.imageAnimation); // /////xml
		img.setVisibility(ImageView.VISIBLE);
		img.setBackgroundResource(R.drawable.animation); // //////drawable

		animation = (AnimationDrawable) img.getBackground();
		animation.start();

		// Btc ���� ����
		btcInfo = new BtcInfo();
		btcInfo.setBtcInfo();

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				startActivity(new Intent(ActivityStart.this, ActivityLogin.class));
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 3000); // ////////�ð�����
	}
}