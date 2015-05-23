/**
 * @brief 날짜 선택 다이얼로그
 * @details 거래내역에서 년,월,일 버튼을 클릭했을 때 뜨는 다이얼로그
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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DialogFragmentDate extends DialogFragment {
	public final static String EXTRA_YEAR = "year", EXTRA_MONTH = "month",
			EXTRA_DAY = "day";
	public final static String EXTRA_INT_YEAR = "year",
			EXTRA_INT_MONTH = "month", EXTRA_INT_DAY = "day";
	private int year;
	private int month;
	private int day;

	public static DialogFragmentDate newInstance(int y, int m, int d) {
		Bundle args = new Bundle();
		DialogFragmentDate fragment = new DialogFragmentDate(y, m, d);
		args.putInt(EXTRA_YEAR, y);
		args.putInt(EXTRA_MONTH, m);
		args.putInt(EXTRA_DAY, d);
		fragment.setArguments(args);
		return fragment;
	}

	public DialogFragmentDate() {
	}

	public DialogFragmentDate(int y, int m, int d) {
		// TODO Auto-generated constructor stub
		year = y;
		month = m;
		day = d;
	}

	

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_date, null);
		DatePicker datePicker = (DatePicker) v
				.findViewById(R.id.dialog_picker_date);
		datePicker.init(year, month, day, new OnDateChangedListener() {
				
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				
				getArguments().putInt(EXTRA_YEAR, year);
				getArguments().putInt(EXTRA_MONTH, monthOfYear);
				getArguments().putInt(EXTRA_DAY, dayOfMonth);
			}
		});
		

		return new AlertDialog.Builder(getActivity()).setView(v)
				.setTitle("시작 다이얼로그")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (getTargetFragment() != null){
							Intent i = new Intent();
							i.putExtra(EXTRA_INT_YEAR, getArguments().getInt(EXTRA_YEAR));
							i.putExtra(EXTRA_INT_MONTH, getArguments().getInt(EXTRA_MONTH));
							i.putExtra(EXTRA_INT_DAY, getArguments().getInt(EXTRA_DAY));
							getTargetFragment().onActivityResult(getTargetRequestCode(),
									Activity.RESULT_OK, i);
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
