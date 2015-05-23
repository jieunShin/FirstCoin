/**
 * @brief ��ǰ�߰� ���̾�α� �����׸�Ʈ
 * @details �ֹ����� ��ǰ�߰� ��ư�� ������ �� �ߴ� ���̾�α�, Ȯ���� ������ �� ���ø����̼� ����Ʈ�� DB�� ��ǰ�� �߰��˴ϴ�.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogFragmentAdd extends DialogFragment {
	ArrayList<Product> mProducts = null;
	String addName = "";
	String addPrice = "";
	String pName = "";
	String pPrice = "";
	String pNum = "0";
	TextView pdInfo = null;
	private EditText pdName = null;
	private EditText pdPrice = null;
	Button OkBtn = null;
	Button CancelBtn = null;
	AlertDialog.Builder builder;
	View v;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// return null;
		mProducts = DataProduct.get(getActivity()).getProducts();
		v = getActivity().getLayoutInflater().inflate(R.layout.order_add_popup, null);
		pdName = (EditText) v.findViewById(R.id.popup_add_name);
		pdPrice = (EditText) v.findViewById(R.id.popup_add_price);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("��ǰ �߰�").setView(v);
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
	}

	// --------------- ���̾�αװ� ����ڿ��� ������ �� ȣ��Ǵ� �� ---------------//
	// ���⼭ Ȯ�ΰ� ��ҹ�ư �������̵��ؼ� ��� ���۽�Ŵ onCreateDialog���� �Ʒ��ڵ带 ������ ������
	public void onStart() {
		super.onStart();
		AlertDialog d = (AlertDialog) getDialog();
		pdInfo = (TextView) v.findViewById(R.id.popup_add_info);

		if (d != null) {
			// Ȯ�ι�ư
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			// ��ҹ�ư
			Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
			// Ȯ�� ��ư Ŭ����
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addName = pdName.getText().toString();
					addPrice = pdPrice.getText().toString();

					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							try {
								URL url = new URL("http://203.246.112.131/pos_addItem.php?" + "merchant_id="
										+ URLEncoder.encode(UserInfo.getMerchantId(), "UTF-8") + "&name="
										+ URLEncoder.encode(addName, "UTF-8") + "&price="
										+ URLEncoder.encode(addPrice, "UTF-8"));
								url.openStream();

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});

					if (addName != "" && addPrice != "") {
						Product product = new Product();
						product.setName(addName);
						product.setPrice(addPrice);
						product.setNumber("0");
						mProducts.add(product);
						ListFragmentOrder.getOrderAdapter().notifyDataSetChanged();
						Toast.makeText(getActivity(), "��ǰ �߰� �Ϸ�", Toast.LENGTH_SHORT).show();
						dismiss(); // ���̾�α� â �ݱ�
					} else {
						Toast.makeText(getActivity(), "��ǰ �߰� ����", Toast.LENGTH_LONG).show();
					}
				}
			});
			// ��ҹ�ư Ŭ����
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "��ǰ �߰� ���", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}
	}// onStart
}
