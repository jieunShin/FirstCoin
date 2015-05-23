/**
 * @brief ��ǰ ���� ���̾�α�
 * @details �ֹ��ǿ��� ��ǰ ����Ʈ�� long click ���� �� �ߴ� ���̾�α�, Ȯ���� ������ ���ø����̼� ����Ʈ�� DB���� ��ǰ������ �����մϴ�.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

class DialogFragmentDelete extends DialogFragment {
	ArrayList<Product> mProducts;
	AlertDialog d;
	//
	int mPosition;
	String delName = "";
	String delPrice = "";

	// newInstance���� �Ķ���ͷ� ����Ʈ�� position�� extra�� �ް� �� ��ġ ����
	public static DialogFragmentDelete newInstance(int position) {

		DialogFragmentDelete fragment = new DialogFragmentDelete(position);
		return fragment;
	}

	DialogFragmentDelete(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mProducts = DataProduct.get(getActivity()).getProducts();
		return new AlertDialog.Builder(getActivity()).setTitle("���� ���� �Ͻðڽ��ϱ�?")
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dismiss();

					}
				}).setNegativeButton("���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dismiss();

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

	// --------------- ���̾�αװ� ����ڿ��� ������ �� ȣ��Ǵ� �� ---------------//
	// ���⼭ Ȯ�ΰ� ��ҹ�ư �������̵��ؼ� ��� ���۽�Ŵ onCreateDialog���� �Ʒ��ڵ带 ������ ������
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
		d = (AlertDialog) getDialog();
		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Log.v("" + mPosition, "m");
							delName = mProducts.get(mPosition).getName();
							delPrice = mProducts.get(mPosition).getPrice();
							Log.v("run()", "���ư���");
							try {

								URL url = new URL("http://203.246.112.131/pos_deleteItem.php?" + "merchant_id="
										+ URLEncoder.encode(UserInfo.getMerchantId(), "UTF-8") + "&name="
										+ URLEncoder.encode(delName, "UTF-8") + "&price="
										+ URLEncoder.encode(delPrice, "UTF-8"));
								url.openStream();
								delName = "";
								delPrice = "";

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});

					if (delName == "" && delPrice == "") {

						mProducts.remove(mPosition); // �Ķ���ͷ� �޾ƿ�
						// �ش� ��������
						// ������ ����
						// �����Ѵ�.
						// ���� ����

						// orderAdapter.notifyDataSetChanged(); // ����͸�
						// ������Ʈ�ؼ�
						// �����Ͱ�
						// �ٲ���ٴ°���
						// �����ϰ�
						// ����Ʈ
						// ���
						// �װ���
						// ��Ÿ����
						// ���.

						Toast.makeText(getActivity(), "��ǰ ���� �Ϸ�", Toast.LENGTH_SHORT).show();
						((ListFragmentOrder) ListFragmentOrder.newInstance()).getOrderAdapter().notifyDataSetChanged();
						dismiss();
					} else {
						Toast.makeText(getActivity(), "��ǰ ���� ����", Toast.LENGTH_LONG).show();
					}
					Toast.makeText(getActivity(), "��ǰ ���� �Ϸ�", Toast.LENGTH_SHORT).show();
					
					
				}

			});
		}// if
		
	}// onstart()

	public void onDismiss(DialogInterface dialog) {

	}

	public DialogFragmentDelete() {
		// TODO Auto-generated constructor stub
	}

}
