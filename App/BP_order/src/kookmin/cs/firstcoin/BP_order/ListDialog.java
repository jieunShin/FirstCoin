package kookmin.cs.firstcoin.BP_order;

import kookmin.cs.firstcoin.order.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ListDialog extends Dialog {

	TextView pdName = null;
	TextView pdPrice = null;
	TextView pdQuantity = null;
	Button OkBtn = null;
	Button CancelBtn = null;
	OrderAdapter adapter = null;
	String select_item = null;
	Spinner spinner = null;

	public ListDialog(Context context) {
		super(context);
		setContentView(R.layout.list_layout);
		pdName = (TextView) findViewById(R.id.product_name);
		pdPrice = (TextView) findViewById(R.id.product_price);

		OkBtn = (Button) findViewById(R.id.list_btn_ok);
		CancelBtn = (Button) findViewById(R.id.list_btn_cancel);

		// 스피너 설정 부분
		String[] numbers = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, numbers);
		spinner.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setSelection(0);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

				select_item = v.toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}// OnCreate끝

	public void setOkClickListener(View.OnClickListener okListener) {
		OkBtn.setOnClickListener(okListener);
	}

	public void setCancleClickListener(View.OnClickListener cancelListener) {
		CancelBtn.setOnClickListener(cancelListener);
	}

	public void setOnCancelListener(OnCancelListener onCancelListener) {

	}

	public void setInfo(String name, String price, String quantity) {
		pdName.setText(name);
		pdPrice.setText(price+"원");
	}

}
