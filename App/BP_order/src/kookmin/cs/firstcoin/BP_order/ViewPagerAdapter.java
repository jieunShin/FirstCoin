package kookmin.cs.firstcoin.BP_order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 4;
	private String titles[];

	public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
		super(fm);
		titles = titles2;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return FragmentOrder.newInstance();
		case 1:
			return FragmentOrderState.newInstance();
		case 2:
			return FragmentSimpleOrder.newInstance();
		case 3:
			return FragmentMyTransaction.newInstance();

		}
		return null;
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}