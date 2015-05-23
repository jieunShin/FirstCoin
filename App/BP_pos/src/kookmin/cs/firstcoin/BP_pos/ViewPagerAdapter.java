/**
 * @brief 뷰페이저 어댑터
 * @details 4개의 탭에 대한 4개의 프래그먼트
 */

package kookmin.cs.firstcoin.BP_pos;

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
			return FragmentTrx.newInstance();
		case 2:
			return FragmentStat.newInstance();
		case 3:
			return FragmentLongDistance.newInstance();

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