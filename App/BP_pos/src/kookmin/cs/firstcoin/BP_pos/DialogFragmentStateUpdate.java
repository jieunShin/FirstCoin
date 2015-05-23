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
	
	private String[] state={"결제 완료","상품 준비 중","준비 완료","전달완료\n: 접수 리스트에서 사라짐"};
	private String total_btc;
	private String qr_code;

	// newInstance에서 파라미터로 리스트의 position을 extra로 받고 그 위치 삭제
	public static DialogFragmentStateUpdate newInstance(int position) {

		DialogFragmentStateUpdate fragment = new DialogFragmentStateUpdate(position);
		return fragment;
	}

	DialogFragmentStateUpdate(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		// 다이얼로그 프래그먼트에 커스텀 뷰 설정
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_state_update, null);
	    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
	    mBuilder.setView(v);
	    mBuilder.setTitle("주문 상태를 다음 단계로 업데이트 하시겠습니까?");
	    // 버튼 설정
	    mBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				
			}
		});
	    
	    mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {

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
		
	    current = (TextView)v.findViewById(R.id.state_update_current);
	    next = (TextView)v.findViewById(R.id.state_update_next);
	          
		 return mBuilder.create();
	}

	// --------------- 다이얼로그가 사용자에게 보여질 때 호출되는 것 ---------------//
	// 여기서 확인과 취소버튼 오버라이딩해서 기능 동작시킴 onCreateDialog에서 아래코드를 적으면 에러남
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
		
		int state_num = Integer.parseInt(mOrder.get(mPosition).getOrderStatus());
		current.setText(state[state_num-3]);
		next.setText(state[state_num-2]);
		
		AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
			// 확인버튼
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			// 취소버튼
			Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
			// 확인 버튼 클릭시
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							try {
								// 기존 주문 상태를 1 증가시킴
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
					Toast.makeText(getActivity(), "상태 업데이트 완료", Toast.LENGTH_LONG).show();
					dismiss(); // 다이얼로그 창 닫기

				} // onClick 종료
			});
			// 취소버튼 클릭시
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "상태 업데이트 취소", Toast.LENGTH_SHORT).show();
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
