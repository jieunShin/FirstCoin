package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrxInforDialogFragment extends DialogFragment {
	ArrayList<Transaction> mTransactions;
	int position;
	AlertDialog d;
	View v;
	
	TextView storename;
	TextView requesttime;
	TextView content;
	TextView price;
	TextView btcprice;
	
	public TrxInforDialogFragment(int position) {
		// TODO Auto-generated constructor stub
		this.position = position;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// ���̾�α� �����׸�Ʈ�� Ŀ���� �� ����
		mTransactions = TransactionData.get(getActivity()).getTransactions();
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_trx_detail, null);
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
		mBuilder.setView(v);
		mBuilder.setTitle("�ֹ� �� ����");
		// ��ư ����
		mBuilder.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

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
	    
		storename = (TextView)v.findViewById(R.id.detail_storename);
		requesttime = (TextView)v.findViewById(R.id.detail_requesttime);
		content = (TextView)v.findViewById(R.id.detail_content);
		price = (TextView)v.findViewById(R.id.detail_price);
		btcprice = (TextView)v.findViewById(R.id.detail_btcprice);
		return mBuilder.create();
	}
	
	public void onStart()
	{
	    super.onStart();    
	    storename.setText(mTransactions.get(position).getStore_name());
	    requesttime.setText(mTransactions.get(position).getDate());
	    content.setText(mTransactions.get(position).getContent());
	    price.setText(mTransactions.get(position).getPrice()+" ��");
	    btcprice.setText(mTransactions.get(position).getTotal_btc()+ " BTC");
	    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d!=null){
			//Ȯ�ι�ư
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			//Ȯ�� ��ư Ŭ����
			positiveButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						dismiss(); //���̾�α� â �ݱ�
					
				} // onClick ����
			});
			
		}
	   
	}//onstart()
	
	

}
