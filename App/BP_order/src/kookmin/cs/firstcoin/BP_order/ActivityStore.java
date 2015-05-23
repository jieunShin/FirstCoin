package kookmin.cs.firstcoin.BP_order;

import kookmin.cs.firstcoin.order.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class ActivityStore extends ActionBarActivity {

	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);

		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.info_toolbar);
		toolbar.setTitle("���� �˻� ���");
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		StoreListFragment listFrg = new StoreListFragment();
		ft.add(R.id.container_list_store, listFrg);
		ft.commit();

	}

}
