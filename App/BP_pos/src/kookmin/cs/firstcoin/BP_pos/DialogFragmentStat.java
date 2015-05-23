package kookmin.cs.firstcoin.BP_pos;

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

public class DialogFragmentStat extends DialogFragment {
	ArrayList<Stat> mStat;
	int position;
	AlertDialog d;
	View v;
	
	TextView date;
	TextView price;
	TextView btcprice;
	
	public DialogFragmentStat(int position) {
		// TODO Auto-generated constructor stub
		this.position = position;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// 다이얼로그 프래그먼트에 커스텀 뷰 설정
		mStat = DataStat.get(getActivity()).getStat();
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_stat_detail, null);
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
		mBuilder.setView(v);
		mBuilder.setTitle("통계 상세 내역");
		// 버튼 설정
		mBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub


			}
		});
		//다이얼로그에서 back 버튼 눌렀을 때 처리
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
		
		date = (TextView)v.findViewById(R.id.detail_date);
		price = (TextView)v.findViewById(R.id.detail_price);
		btcprice = (TextView)v.findViewById(R.id.detail_btcprice);
		
		return mBuilder.create();
	}
	
	public void onStart()
	{
	    super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
	    
	    date.setText(mStat.get(position).getMonth());
	    price.setText(mStat.get(position).getTotal()+" 원");
	    btcprice.setText(mStat.get(position).getTotal_btc()+" BTC");
	    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d!=null){
			//확인버튼
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			//확인 버튼 클릭시
			positiveButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						dismiss(); //다이얼로그 창 닫기
					
				} // onClick 종료
			});
			
		}
	   
	}//onstart()
	
	

}
