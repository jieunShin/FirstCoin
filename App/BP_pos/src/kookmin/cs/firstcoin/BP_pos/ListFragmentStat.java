/**
 * @brief 통계 리스트 프래그먼트
 * @details 통계탭에서 통계 정보 리스트 입니다.
 */


package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragmentStat extends ListFragment {
	ArrayList<Stat> mStat;
	FragmentManager fm;
	private static StatAdapter adapter;

	public static ListFragmentStat newInstance() {
		ListFragmentStat fragment = new ListFragmentStat();
		return fragment;
	}

	public ListFragmentStat() {
		// TODO Auto-generated constructor stub
	}

	public static StatAdapter getStatAdapter() {
		return adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		mStat = DataStat.get(getActivity()).getStat();
		adapter = new StatAdapter(mStat);
		setListAdapter(adapter);
	}// onCreate

	
	 public void onListItemClick(ListView l, View v, int position, long id) {
	 // 다시 활성화 // Idiom i =
		 DialogFragmentStat inforDialog = new DialogFragmentStat(position);
		 inforDialog.show(fm, "stat");
	 }//onListItemClick
	 

	public class StatAdapter extends ArrayAdapter<Stat> {

		public StatAdapter(ArrayList<Stat> stat) {
			super(getActivity(), 0, stat);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_view_stat_layout, null);
			}

			Stat t = getItem(position);

			// month
			TextView Statmonth = (TextView) convertView
					.findViewById(R.id.list_stat_month);
			Statmonth.setText(t.getMonth());

			// total
			TextView Stattotal = (TextView) convertView
					.findViewById(R.id.list_stat_total);
			Stattotal.setText(t.getTotal()+"원");

			return convertView;
		}
	}// stat어댑터

}