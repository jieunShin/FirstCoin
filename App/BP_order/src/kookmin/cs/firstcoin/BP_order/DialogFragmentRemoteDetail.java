package kookmin.cs.firstcoin.BP_order;

import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import kookmin.cs.firstcoin.order.R;

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

import com.coinplug.lib.wallet.CPUserService;
import com.coinplug.lib.wallet.CPWalletService;
import com.coinplug.lib.wallet.ICPUser;
import com.coinplug.lib.wallet.ICPWallet;
import com.coinplug.lib.wallet.listener.CPWAmountListener;
import com.coinplug.lib.wallet.model.CPCurrencyAmount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class DialogFragmentRemoteDetail extends DialogFragment {
		
	ArrayList<LongDistance> mOrder;
	AlertDialog d;
	int mPosition;
	View v;
	
	TextView storename;
	TextView requesttime;
	TextView content;
	TextView price;
	TextView btcprice;
	TextView state;
	
	//newInstance���� �Ķ���ͷ� ����Ʈ�� position�� extra�� �ް� �� ��ġ ����
	public static DialogFragmentRemoteDetail newInstance(int position) {
		
		DialogFragmentRemoteDetail fragment = new DialogFragmentRemoteDetail(position);
		return fragment;
	}
	DialogFragmentRemoteDetail(int position){
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		// ���̾�α� �����׸�Ʈ�� Ŀ���� �� ����
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail, null);
	    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
	    mBuilder.setView(v);
	    mBuilder.setTitle("�ֹ� �� ����");
	    // ��ư ����
	    mBuilder.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
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
		 
	    storename = (TextView)v.findViewById(R.id.detail_storename);
	    requesttime = (TextView)v.findViewById(R.id.detail_requesttime);
	    content = (TextView)v.findViewById(R.id.detail_content);
	    price = (TextView)v.findViewById(R.id.detail_price);
	    btcprice = (TextView)v.findViewById(R.id.detail_btcprice);
	    state = (TextView)v.findViewById(R.id.detail_state);
	    
	       
		 return mBuilder.create();
	}
	//---------------  ���̾�αװ� ����ڿ��� ������ �� ȣ��Ǵ� ��   ---------------//
	//���⼭ Ȯ�ΰ� ��ҹ�ư �������̵��ؼ� ��� ���۽�Ŵ onCreateDialog���� �Ʒ��ڵ带 ������ ������
	public void onStart()
	{
	    super.onStart();
	    storename.setText(mOrder.get(mPosition).getStoreName());
	    requesttime.setText(mOrder.get(mPosition).getOrderTime());
	    content.setText(mOrder.get(mPosition).getContent());
	    price.setText(mOrder.get(mPosition).getPrice()+" ��");
	    btcprice.setText(mOrder.get(mPosition).getTotal_btc()+ " BTC");
	    
	    switch(Integer.parseInt(mOrder.get(mPosition).getOrderStatus()))
		{
			case 0: // �ֹ� ��û�� �� ����
				state.setText("���� ���");
				break;
			case -1: // �ֹ� ��û�� ������ ����
				state.setText("�ֹ� ����");
				break;
			case 1: // �ֹ� ������ ������ qrcode�� ������Ʈ ���� ���� ����
				state.setText("���� ����");
				break;
			case 2: // �ֹ� ���α��� �� ����
				state.setText("���� ���");
				break;
			case 3: // ���� �ϷḸ ���� ����
				state.setText("���� �Ϸ�");
				break;
			case 4: // pos���� ���� �ϷḦ Ȯ���ϰ� ��ǰ �غ� ���� ������Ʈ �� ����
				state.setText("��ǰ �غ� ��");
				break;
			case 5: // �Ⱦ� ����� ����
				state.setText("�غ� �Ϸ�");
				break;
		}
	    
	    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d!=null){
			//Ȯ�ι�ư
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			//Ȯ�� ��ư Ŭ����
			positiveButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						dismiss(); //���̾�α� â �ݱ�
					
				} // onClick ����
			});
			
		}
	   
	}//onstart()
	
	public void onDismiss(DialogInterface dialog){
		
		
	}
	public DialogFragmentRemoteDetail() {
		
	}
	
}
