package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

//---------------  원거리 주문 탭의 리스트   ---------------//
public class LongDistanceRequestFragment extends ListFragment {
	ArrayList<LongDistance> mLongDistance;
	FragmentManager fm;
	private static LongDistanceRequestFragment fragment = null;
	private static LongDistanceRequestAdapter adapter;

	public static LongDistanceRequestFragment newInstance() {
		fragment = new LongDistanceRequestFragment();
		return fragment;
	}

	// --------------- ---------------//
	public static LongDistanceRequestAdapter getLongDistanceAdapter() {
		return adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		mLongDistance = DataLongDistance.get(getActivity()).getLongDistances();
		adapter = new LongDistanceRequestAdapter(getActivity(), mLongDistance);
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
		Intent intent = new Intent(v.getContext(), ActivityMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
		// 결제진행 다이얼로그 생성 
		DialogFragmentRemoteDetail remotedetailDialog = DialogFragmentRemoteDetail.newInstance(position);
		remotedetailDialog.show(fm,"상세내역");
		
	}// onListItemClick

	// 어댑터 분리함!!
}
