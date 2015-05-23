/**
 * @brief ��ǰ ���� ���� ���̾�α�
 * @details 
 * �ֹ����� ��ǰ ����Ʈ�� click ���� �� ������ ������ �� �ִ� ���̾�α�.
 * ��ǰ ������ �����ϸ� ������ŭ�� ������ �Ѿ׿� �ջ��Ѵ�.
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
		price.setText(pPrice+"��");

		spinner = (Spinner) v.findViewById(R.id.spinner);
		adspin = ArrayAdapter.createFromResource(getActivity(), R.array.quantity,
				android.R.layout.simple_spinner_dropdown_item);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adspin);
		spinner.setSelection(0);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("����").setView(v);
		builder.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// ����ٰ� �ۼ� ���� �������̵� onstart���� �ۼ�
			}
		});
		builder.setNegativeButton("���", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// ���⵵ �������̵�
			}
		});
		//���̾�α׿��� back ��ư ������ �� ó��
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

	// --------------- ���̾�αװ� ����ڿ��� ������ �� ȣ��Ǵ� �� ---------------//
	// ���⼭ Ȯ�ΰ� ��ҹ�ư �������̵��ؼ� ��� ���۽�Ŵ onCreateDialog���� �Ʒ��ڵ带 ������ ������
	public void onStart() {
		super.onStart();
		d = (AlertDialog) getDialog();
		// ��������
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
				newQuantity = position1;// ���õ� ������ ������ ����\
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
			// Ȯ�ι�ư Ŭ����
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (prevQuantity != "0") {// ���� ���õ� ���� �ִٸ�
						sum -= Integer.parseInt(pPrice) * Integer.parseInt(prevQuantity);// ����
																							// ��
																							// ����
						sum += Integer.parseInt(pPrice) * newQuantity;// ���ο� ��
																		// ����
					} else {
						sum += Integer.parseInt(pPrice) * newQuantity;// ���ο� ��
																		// ����
					}
					mProducts.get(position).setNumber("" + newQuantity);
					if (getTargetFragment() != null) {
						// ������ ���� ���� ���� sum�� ����Ʈ�� �Ǿ Ÿ�� �����׸�Ʈ�� �ѱ��
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
