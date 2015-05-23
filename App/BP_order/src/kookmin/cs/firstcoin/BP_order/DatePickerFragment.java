package kookmin.cs.firstcoin.BP_order;

import kookmin.cs.firstcoin.order.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;


public class DatePickerFragment extends DialogFragment {
	public final static String EXTRA_YEAR = "year", EXTRA_MONTH = "month",
			EXTRA_DAY = "day";
	public final static String EXTRA_INT_YEAR = "year",
			EXTRA_INT_MONTH = "month", EXTRA_INT_DAY = "day";
	private int year;
	private int month;
	private int day;

	public static DatePickerFragment newInstance(int y, int m, int d) {
		Bundle args = new Bundle();
		DatePickerFragment fragment = new DatePickerFragment(y, m, d);
		args.putInt(EXTRA_YEAR, y);
		args.putInt(EXTRA_MONTH, m);
		args.putInt(EXTRA_DAY, d);
		fragment.setArguments(args);
		return fragment;
	}

	public DatePickerFragment() {
	}

	public DatePickerFragment(int y, int m, int d) {
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
						if (getTargetFragment() != null){
							Intent i = new Intent();
							i.putExtra(EXTRA_INT_YEAR, getArguments().getInt(EXTRA_YEAR));
							i.putExtra(EXTRA_INT_MONTH, getArguments().getInt(EXTRA_MONTH));
							i.putExtra(EXTRA_INT_DAY, getArguments().getInt(EXTRA_DAY));
							getTargetFragment().onActivityResult(getTargetRequestCode(),
									Activity.RESULT_OK, i);
						}
					}
				}).create();
	}
}
