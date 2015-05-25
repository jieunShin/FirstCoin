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
	    state = (TextView)v.findViewById(R.id.detail_state);
	    
	       
		 return mBuilder.create();
	}
	//---------------  ���̾�αװ� ����ڿ��� ������ �� ȣ��Ǵ� ��   ---------------//
	//���⼭ Ȯ�ΰ� ��ҹ�ư �������̵��ؼ� ��� ���۽�Ŵ onCreateDialog���� �Ʒ��ڵ带 ������ ������
	public void onStart()
	{
	    super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
	    
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
					// TODO Auto-generated method stub
						dismiss(); //���̾�α� â �ݱ�
					
				} // onClick ����
			});
			
		}
	   
	}//onstart()
	
	public void onDismiss(DialogInterface dialog){
		
		
	}
	public DialogFragmentRemoteDetail() {
		// TODO Auto-generated constructor stub
	}
	
}
