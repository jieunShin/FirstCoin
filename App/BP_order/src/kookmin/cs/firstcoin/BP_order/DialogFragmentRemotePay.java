package kookmin.cs.firstcoin.BP_order;

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

import com.coinplug.lib.wallet.CPWalletService;
import com.coinplug.lib.wallet.ICPWallet;
import com.coinplug.lib.wallet.listener.CPWAmountListener;
import com.coinplug.lib.wallet.model.CPCurrencyAmount;

class DialogFragmentRemotePay extends DialogFragment {

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	List<NameValuePair> params;

	ArrayList<LongDistance> mOrder;
	AlertDialog d;
	int mPosition;
	View v;
	
	TextView content;
	TextView price;
	
	private String total_btc;
	private String qr_code;
	private ICPWallet mWalletService;

	// newInstance에서 파라미터로 리스트의 position을 extra로 받고 그 위치 삭제
	public static DialogFragmentRemotePay newInstance(int position) {

		DialogFragmentRemotePay fragment = new DialogFragmentRemotePay(position);
		return fragment;
	}

	DialogFragmentRemotePay(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		try {
			mWalletService = CPWalletService.getInstance(getActivity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		// 다이얼로그 프래그먼트에 커스텀 뷰 설정
		v = getActivity().getLayoutInflater().inflate(R.layout.dialog_pay, null);
	    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
	    mBuilder.setView(v);
	    mBuilder.setTitle("결제를 진행 하시겠습니까?");
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
		 
	    content = (TextView)v.findViewById(R.id.detail_content);
	    price = (TextView)v.findViewById(R.id.detail_price);
	   
		 return mBuilder.create();
	
	}

	// --------------- 다이얼로그가 사용자에게 보여질 때 호출되는 것 ---------------//
	// 여기서 확인과 취소버튼 오버라이딩해서 기능 동작시킴 onCreateDialog에서 아래코드를 적으면 에러남
	public void onStart() {
		super.onStart(); 
		
		content.setText(mOrder.get(mPosition).getContent());
	    price.setText(mOrder.get(mPosition).getPrice()+" 원");
		
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
								// 1. qrcode, btc 정보 받다오고
								SetInfo();
								// 2. BTC 보내고
								sendBtc();
								// 3. 디비 업데이트와 푸시 보내고
								PaymentPush();

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});
					Toast.makeText(getActivity(), "결제 완료", Toast.LENGTH_LONG).show();
					dismiss(); // 다이얼로그 창 닫기

				} // onClick 종료
			});
			// 취소버튼 클릭시
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "결제 진행 취소", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}

	}// onstart()

	public void onDismiss(DialogInterface dialog) {

	}

	public DialogFragmentRemotePay() {
		// TODO Auto-generated constructor stub
	}

	private void SetInfo() {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/user_get_remote_pay_info.php");

			params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("order_id", mOrder.get(mPosition).getOrderId()));

			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / 데이터를 받아옴
			// 받아온 데이터 형태 : total_btc, qr_code
			String[] data = response.split(",");

			Log.e("total_btc", data[0]);
			Log.e("qr_code", data[1]);

			total_btc = data[0];
			qr_code = data[1];

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendBtc() {
		// 주소, 이메일 ,전화번호
		String sendAddress = qr_code;
		// 보내는 비트코인 량
		String amount = total_btc;

		try {
			mWalletService.walletSendMoney(amount, sendAddress, "note", new CPWAmountListener() {

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
					// TODO Auto-generated method stub
					

				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, CPCurrencyAmount amount) {

					Log.e("log_success", headers.toString());
					// TODO Auto-generated method stub
				
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("log_remote_", e.toString());
		}
	}

	private void PaymentPush() {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://203.246.112.131/user_remote_payment.php");

			params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("order_id", mOrder.get(mPosition).getOrderId()));

			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
