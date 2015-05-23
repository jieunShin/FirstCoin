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

//---------------  ���Ÿ� �ֹ� ���� ����Ʈ   ---------------//
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
		// --------------- �� Ŭ�� ---------------//
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				return true;
			}
		});
	}

	// --------------- �Ϲ� Ŭ�� ---------------//
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(v.getContext(), ActivityMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
		// �������� ���̾�α� ���� 
		DialogFragmentRemoteDetail remotedetailDialog = DialogFragmentRemoteDetail.newInstance(position);
		remotedetailDialog.show(fm,"�󼼳���");
		
	}// onListItemClick

	// ����� �и���!!
}
