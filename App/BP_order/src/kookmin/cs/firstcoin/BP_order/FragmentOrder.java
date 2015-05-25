package kookmin.cs.firstcoin.BP_order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
 * '주문내역' 탭의 fragment
 * */
public class FragmentOrder extends Fragment implements View.OnClickListener {
	private final static int REQUEST_QUANTITY = 0;
	private final static int REQUEST_PRICE = 1;
	FragmentManager fm;
	private static final String TAB_NUMBER1 = "tab_number";
	View rootView;

	Intent i;

	// Button mSearchButton;
	Button mFastfoodButton;
	Button mCoffeeButton;
	Button mChineseButton;
	Button mJapaneseButton;
	Button mFlourfoodButton;
	Button mWesternButton;

	public static FragmentOrder newInstance() {
		// FragmentOrder를 생성한다.
		return new FragmentOrder();
	}

	public FragmentOrder() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_order, container, false);

		mFastfoodButton = (Button) view.findViewById(R.id.fastfoodButton);
		mCoffeeButton = (Button) view.findViewById(R.id.coffeeButton);
		mChineseButton = (Button) view.findViewById(R.id.chineseButton);
		mJapaneseButton = (Button) view.findViewById(R.id.japaneseButton);
		mFlourfoodButton = (Button) view.findViewById(R.id.flourfoodButton);
		mWesternButton = (Button) view.findViewById(R.id.westernfoodButton);

		mFastfoodButton.setOnClickListener(this);
		mCoffeeButton.setOnClickListener(this);
		mChineseButton.setOnClickListener(this);
		mJapaneseButton.setOnClickListener(this);
		mFlourfoodButton.setOnClickListener(this);
		mWesternButton.setOnClickListener(this);

		return view;
	}

	public void onClick(View v) {

		Intent in = new Intent(getActivity(), ActivityStoreList.class);

		switch (v.getId()) {
		case R.id.fastfoodButton:
			in.putExtra("storeNum", "1");
			startActivity(in);
			break;
		case R.id.coffeeButton:
			in.putExtra("storeNum", "2");
			startActivity(in);
			break;
		case R.id.chineseButton:
			in.putExtra("storeNum", "3");
			startActivity(in);
			break;
		case R.id.japaneseButton:
			in.putExtra("storeNum", "4");
			startActivity(in);
			break;
		case R.id.flourfoodButton:
			in.putExtra("storeNum", "5");
			startActivity(in);
			break;
		case R.id.westernfoodButton:
			in.putExtra("storeNum", "6");
			startActivity(in);
			break;
		}
	}

}
