/**
 * @brief 시간 선택 다이얼로그
 * @details 거래내역 탭에서 시간 버튼을 클릭했을 때 뜨는 다이얼로그
 */


package kookmin.cs.firstcoin.BP_pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DialogFragmentTime extends DialogFragment {
	public final static String EXTRA_HOUR = "hour";
	public final static String EXTRA_INT_HOUR = "hour";

	private int hour;

	public static DialogFragmentTime newInstance(int h) {
		Bundle args = new Bundle();
		DialogFragmentTime fragment = new DialogFragmentTime(h);
		args.putInt(EXTRA_HOUR, h);

		fragment.setArguments(args);
		return fragment;
	}

	public DialogFragmentTime() {
	}

	public DialogFragmentTime(int h) {
		// TODO Auto-generated constructor stub
		hour = h;

	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_time, null);
		TimePicker timePicker = (TimePicker) v
				.findViewById(R.id.dialog_picker_time);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				hour = hourOfDay;
				getArguments().putInt(EXTRA_HOUR, hourOfDay);
			}
		});

		return new AlertDialog.Builder(getActivity()).setView(v)
				.setTitle("시작 다이얼로그")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (getTargetFragment() != null) {
							Intent i = new Intent();
							i.putExtra(EXTRA_INT_HOUR,
									getArguments().getInt(EXTRA_HOUR));

							getTargetFragment().onActivityResult(
									getTargetRequestCode(), Activity.RESULT_OK,
									i);
						}

					}
				}).setOnKeyListener(new DialogInterface.OnKeyListener() {
					public boolean onKey(DialogInterface dialog,
							int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							dismiss();
							return true;
						}
						return false;
					}
				}).
				create();
	}
}
