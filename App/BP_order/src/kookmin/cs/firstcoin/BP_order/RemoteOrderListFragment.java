package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//---------------  원거맂 주문 탭의 리스트   ---------------//
public class RemoteOrderListFragment extends ListFragment {
	ArrayList<RemoteOrder> mOrder;
	FragmentManager fm;
	private static RemoteOrderListFragment fragment = null;
	private static RemoteOrderAdapter adapter;

	public static RemoteOrderListFragment newInstance() {
		fragment = new RemoteOrderListFragment();
		return fragment;
	}

	// --------------- ---------------//
	public static RemoteOrderAdapter getOrderAdapter() {
		return adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		mOrder = RemoteOrderData.get(getActivity()).getOrder();
		adapter = new RemoteOrderAdapter(getActivity(), mOrder);
		setListAdapter(adapter);
	}// onCreate

	public void onResume() {
		super.onResume();
	}// onResume

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// --------------- 롱 클릭 ---------------//
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				return true;
			}
		});
	}

	// --------------- 일반 클릭 ---------------//
	public void onListItemClick(ListView l, View v, int position, long id) {

	}// onListItemClick
}
