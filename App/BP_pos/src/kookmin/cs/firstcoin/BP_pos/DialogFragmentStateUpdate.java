package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

class DialogFragmentStateUpdate extends DialogFragment {

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	List<NameValuePair> params;

	ArrayList<LongDistance> mOrder;
	AlertDialog d;
	View v;
	int mPosition;
	
	TextView current;
	TextView next;
	
	private String[] state={"���� �Ϸ�","��ǰ �غ� ��","�غ� �Ϸ�","���޿Ϸ�\n: ���� ����Ʈ���� �����"};
	private String total_btc;
	private String qr_code;

	// newInstance���� �Ķ���ͷ� ����Ʈ�� position�� extra�� �ް� �� ��ġ ����
	public static DialogFragmentStateUpdate newInstance(int position) {

		DialogFragmentStateUpdate fragment = new DialogFragmentStateUpdate(position);
		return fragment;
	}

	DialogFragmentStateUpdate(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		// ���̾�α� �����׸�Ʈ�� Ŀ���� �� ����
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_state_update, null);
	    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
	    mBuilder.setView(v);
	    mBuilder.setTitle("�ֹ� ���¸� ���� �ܰ�� ������Ʈ �Ͻðڽ��ϱ�?");
	    // ��ư ����
	    mBuilder.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				
			}
		});
	    
	    mBuilder.setNegativeButton("���", new DialogInterface.OnClickListener() {

	    	@Override
	    	public void onClick(DialogInterface dialog, int which) {
	    		// TODO Auto-generated method stub


	    	}
	    });
		//���̾�α׿��� back ��ư ������ �� ó��
		mBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog,
					int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		
	    current = (TextView)v.findViewById(R.id.state_update_current);
	    next = (TextView)v.findViewById(R.id.state_update_next);
	          
		 return mBuilder.create();
	}

	// --------------- ���̾�αװ� ����ڿ��� ������ �� ȣ��Ǵ� �� ---------------//
	// ���⼭ Ȯ�ΰ� ��ҹ�ư �������̵��ؼ� ��� ���۽�Ŵ onCreateDialog���� �Ʒ��ڵ带 ������ ������
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
		
		int state_num = Integer.parseInt(mOrder.get(mPosition).getOrderStatus());
		current.setText(state[state_num-3]);
		next.setText(state[state_num-2]);
		
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
								// ���� �ֹ� ���¸� 1 ������Ŵ
								int temp = Integer.parseInt(mOrder.get(mPosition).getOrderStatus());
								String str = Integer.toString(temp + 1);
								mOrder.get(mPosition).setOrderStatus(str);

								httpclient = new DefaultHttpClient();
								httppost = new HttpPost("http://203.246.112.131/pos_remote_status_update.php");

								params = new ArrayList<NameValuePair>(1);
								params.add(new BasicNameValuePair("order_id", mOrder.get(mPosition).getOrderId()));
								params.add(new BasicNameValuePair("status", str));

								httppost.setEntity(new UrlEncodedFormEntity(params));
								httpresponse = httpclient.execute(httppost);
								ResponseHandler<String> responseHandler = new BasicResponseHandler();

								String responses = httpclient.execute(httppost, responseHandler);
								responses = new String(responses.getBytes("ISO-8859-1"), "UTF-8");

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});
					((LongDistanceRequestFragment) LongDistanceRequestFragment.newInstance()).getLongDistanceAdapter()
							.notifyDataSetChanged();
					Toast.makeText(getActivity(), "���� ������Ʈ �Ϸ�", Toast.LENGTH_LONG).show();
					dismiss(); // ���̾�α� â �ݱ�

				} // onClick ����
			});
			// ��ҹ�ư Ŭ����
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "���� ������Ʈ ���", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}

	}// onstart()

	public void onDismiss(DialogInterface dialog) {

	}

	public DialogFragmentStateUpdate() {
		// TODO Auto-generated constructor stub
	}

}
