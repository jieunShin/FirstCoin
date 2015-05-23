package kookmin.cs.firstcoin.BP_order;

import kookmin.cs.firstcoin.order.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentEmpty extends Fragment implements View.OnClickListener {
	FragmentManager fm;
	View rootView;
	public static FragmentEmpty newInstance() {
		// FragmentOrder를 생성한다.
		return new FragmentEmpty();
	}

	public FragmentEmpty() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_empty, container, false);
		return view;
	}

	public void onClick(View v) {

	
	}

}
