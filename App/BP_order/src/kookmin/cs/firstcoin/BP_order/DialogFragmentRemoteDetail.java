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
	
	//newInstance에서 파라미터로 리스트의 position을 extra로 받고 그 위치 삭제
	public static DialogFragmentRemoteDetail newInstance(int position) {
		
		DialogFragmentRemoteDetail fragment = new DialogFragmentRemoteDetail(position);
		return fragment;
	}
	DialogFragmentRemoteDetail(int position){
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		// 다이얼로그 프래그먼트에 커스텀 뷰 설정
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail, null);
	    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
	    mBuilder.setView(v);
	    mBuilder.setTitle("주문 상세 내역");
	    // 버튼 설정
	    mBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
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
		 
	    storename = (TextView)v.findViewById(R.id.detail_storename);
	    requesttime = (TextView)v.findViewById(R.id.detail_requesttime);
	    content = (TextView)v.findViewById(R.id.detail_content);
	    price = (TextView)v.findViewById(R.id.detail_price);
	    btcprice = (TextView)v.findViewById(R.id.detail_btcprice);
	    state = (TextView)v.findViewById(R.id.detail_state);
	    
	       
		 return mBuilder.create();
	}
	//---------------  다이얼로그가 사용자에게 보여질 때 호출되는 것   ---------------//
	//여기서 확인과 취소버튼 오버라이딩해서 기능 동작시킴 onCreateDialog에서 아래코드를 적으면 에러남
	public void onStart()
	{
	    super.onStart();
	    storename.setText(mOrder.get(mPosition).getStoreName());
	    requesttime.setText(mOrder.get(mPosition).getOrderTime());
	    content.setText(mOrder.get(mPosition).getContent());
	    price.setText(mOrder.get(mPosition).getPrice()+" 원");
	    btcprice.setText(mOrder.get(mPosition).getTotal_btc()+ " BTC");
	    
	    switch(Integer.parseInt(mOrder.get(mPosition).getOrderStatus()))
		{
			case 0: // 주문 요청만 들어간 상태
				state.setText("승인 대기");
				break;
			case -1: // 주문 요청이 거절된 상태
				state.setText("주문 거절");
				break;
			case 1: // 주문 승인은 했으나 qrcode가 업데이트 되지 않은 상태
				state.setText("현장 결제");
				break;
			case 2: // 주문 승인까지 된 상태
				state.setText("결제 대기");
				break;
			case 3: // 결제 완료만 끝난 상태
				state.setText("결제 완료");
				break;
			case 4: // pos에서 결제 완료를 확인하고 상품 준비 중을 업데이트 한 상태
				state.setText("상품 준비 중");
				break;
			case 5: // 픽업 대기인 상태
				state.setText("준비 완료");
				break;
		}
	    
	    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d!=null){
			//확인버튼
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			//확인 버튼 클릭시
			positiveButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						dismiss(); //다이얼로그 창 닫기
					
				} // onClick 종료
			});
			
		}
	   
	}//onstart()
	
	public void onDismiss(DialogInterface dialog){
		
		
	}
	public DialogFragmentRemoteDetail() {
		
	}
	
}
