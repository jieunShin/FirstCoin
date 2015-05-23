/**
 * @brief 상품 수량 선택 다이얼로그
 * @details 
 * 주문탭의 상품 리스트를 click 했을 때 수량을 선택할 수 있는 다이얼로그.
 * 상품 수량을 선택하면 수량만큼의 가격을 총액에 합산한다.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DialogFragmentQuantity extends DialogFragment {
	View v;
	AlertDialog d;
	ArrayAdapter<CharSequence> adspin;
	int position;
	int newQuantity;
	int sum = 0;
	String prevQuantity;
	String pPrice;
	String pName;
	ArrayList<Product> mProducts;
	Spinner spinner;
	private TextView name = null;
	private TextView price = null;
	EditText quantity;
	AlertDialog.Builder builder;

	public DialogFragmentQuantity(int position) {
		// TODO Auto-generated constructor stub
		this.position = position;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}// onCreate

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mProducts = DataProduct.get(getActivity()).getProducts();
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_quantity, null);

		prevQuantity = mProducts.get(position).getNumber();
		pPrice = mProducts.get(position).getPrice();
		pName = mProducts.get(position).getName();
		name = (TextView) v.findViewById(R.id.product_name);
		price = (TextView) v.findViewById(R.id.product_price);
		name.setText(pName);
		price.setText(pPrice+"원");

		spinner = (Spinner) v.findViewById(R.id.spinner);
		adspin = ArrayAdapter.createFromResource(getActivity(), R.array.quantity,
				android.R.layout.simple_spinner_dropdown_item);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adspin);
		spinner.setSelection(0);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("수량").setView(v);
		builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 여기다가 작성 안함 오버라이딩 onstart에서 작성
			}
		});
		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 여기도 오버라이딩
			}
		});
		//다이얼로그에서 back 버튼 눌렀을 때 처리
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog,
					int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					return true;
				}
				return false;
			}
		});

		return builder.create();
	}// onCreateDialog

	// --------------- 다이얼로그가 사용자에게 보여질 때 호출되는 것 ---------------//
	// 여기서 확인과 취소버튼 오버라이딩해서 기능 동작시킴 onCreateDialog에서 아래코드를 적으면 에러남
	public void onStart() {
		super.onStart();
		d = (AlertDialog) getDialog();
		// 수량선택
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
				newQuantity = position1;// 선택된 수량을 변수에 저장\
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
			// 확인버튼 클릭시
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (prevQuantity != "0") {// 값이 선택된 적이 있다면
						sum -= Integer.parseInt(pPrice) * Integer.parseInt(prevQuantity);// 이전
																							// 값
																							// 빼고
						sum += Integer.parseInt(pPrice) * newQuantity;// 새로운 값
																		// 더함
					} else {
						sum += Integer.parseInt(pPrice) * newQuantity;// 새로운 값
																		// 더함
					}
					mProducts.get(position).setNumber("" + newQuantity);
					if (getTargetFragment() != null) {
						// 수량에 따른 변경 값인 sum을 인텐트에 실어서 타겟 프래그먼트로 넘긴다
						Intent intent = new Intent();
						intent.putExtra("quantity_value", sum);
						getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
					}
					d.dismiss();
				}
			});
		}
	}
}
