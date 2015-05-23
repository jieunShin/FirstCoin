/**
 * @brief ��ǰ ���� ���̾�α�
 * @details �ֹ��ǿ��� ��ǰ ����Ʈ�� long click ���� �� �ߴ� ���̾�α�, Ȯ���� ������ ���ø����̼� ����Ʈ�� DB���� ��ǰ������ �����մϴ�.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

class DialogFragmentAccept extends DialogFragment {

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	List<NameValuePair> params;

	ArrayList<LongDistance> mOrder;
	AlertDialog d;
	int mPosition;

	private String mBtcPayAddress;
	private String mBtcPayPrice;
	private String mPayName;
	private String mPayTransactionId;

	// newInstance���� �Ķ���ͷ� ����Ʈ�� position�� extra�� �ް� �� ��ġ ����
	public static DialogFragmentAccept newInstance(int position) {

		DialogFragmentAccept fragment = new DialogFragmentAccept(position);
		return fragment;
	}

	DialogFragmentAccept(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		return new AlertDialog.Builder(getActivity()).setTitle("���� ó�� �Ͻðڽ��ϱ�?")
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).setNegativeButton("���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

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
		AlertDialog d = (AlertDialog) getDialog();
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
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							try {
								// pos������ ��ȸ�� ��Ʈ���� �ּ� ������ DB������� ��� �ϴ� �Լ�
								Log.e("test", "what?");
								StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
										.build();
								StrictMode.setThreadPolicy(policy); // ����������
																	// ��Ʈ��ũ ����
								actionCreateOrder();

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});
					Toast.makeText(getActivity(), "���� ó�� �Ϸ�", Toast.LENGTH_LONG).show();
					dismiss(); // ���̾�α� â �ݱ�

				} // onClick ����
			});
			// ��ҹ�ư Ŭ����
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "���� ó�� ���", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}

	}// onstart()

	public void onDismiss(DialogInterface dialog) {

	}

	public DialogFragmentAccept() {
		// TODO Auto-generated constructor stub
	}

	public void actionCreateOrder() {
		try {

			// ���� : ���Ÿ��ֹ� ���� �� qr�ڵ带 �����ϴ� �κ�
			// library ���� �� ���� ��ǥ : db�� orders 
			
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/pos_remote_response_ok_2.php");

			params = new ArrayList<NameValuePair>(1);

			params.add(new BasicNameValuePair("order_id", mOrder.get(mPosition).getOrderId()));
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responses = httpclient.execute(httppost, responseHandler);
			responses = new String(responses.getBytes("ISO-8859-1"), "UTF-8");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionUpdate() {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/pos_remote_response_ok.php");

			params = new ArrayList<NameValuePair>(4);

			Log.e("Update", mOrder.get(mPosition).getOrderId());
			Log.e("Update", mBtcPayAddress);
			Log.e("Update", mBtcPayPrice);
			params.add(new BasicNameValuePair("order_id", mOrder.get(mPosition).getOrderId()));
			params.add(new BasicNameValuePair("qr_code", mBtcPayAddress));
			params.add(new BasicNameValuePair("total_btc", mBtcPayPrice));
			params.add(new BasicNameValuePair("payment_id", mPayTransactionId));

			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responses = httpclient.execute(httppost, responseHandler);
			responses = new String(responses.getBytes("ISO-8859-1"), "UTF-8");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}