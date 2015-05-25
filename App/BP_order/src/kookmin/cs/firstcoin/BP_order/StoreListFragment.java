package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StoreListFragment extends ListFragment {
	ArrayList<Store> mStores;
	FragmentManager fm;
	private static StoreAdapter adapter;

	public static StoreListFragment newInstance() {
		StoreListFragment fragment = new StoreListFragment();
		return fragment;
	}

	public StoreListFragment() {
		// TODO Auto-generated constructor stub
	}

	public static StoreAdapter getTrxAdapter() {
		return adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		mStores = StoreData.get(getActivity()).getStores();
		adapter = new StoreAdapter(mStores);
		setListAdapter(adapter);
	}// onCreate
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		String merchant_id = mStores.get(position).getMerchantID();
        String store_name = mStores.get(position).getName();
		Intent in = new Intent(getActivity(), ActivityOrder.class);
		in.putExtra("merchant_id", merchant_id + "");
		in.putExtra("store_name", store_name);
		startActivity(in);
	}// onListItemClick

	public class StoreAdapter extends ArrayAdapter<Store> {

		public StoreAdapter(ArrayList<Store> Stores) {
			super(getActivity(), 0, Stores);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_view_store_layout, null);
			}

			Store t = getItem(position);
			// data
			TextView mData = (TextView) convertView.findViewById(R.id.list_store_name);
			mData.setText(t.getName());
			// price
			TextView mPrice = (TextView) convertView.findViewById(R.id.list_store_address);
			mPrice.setText(t.getAddress());
			// content
			TextView mContent = (TextView) convertView.findViewById(R.id.list_store_phone);
			mContent.setText(t.getPhoneNumber());

			return convertView;
		}
	}// พ๎ด๐ลอ

}
