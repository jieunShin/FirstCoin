/**
 * @brief 상품 삭제 다이얼로그
 * @details 주문탭에서 상품 리스트를 long click 했을 때 뜨는 다이얼로그, 확인을 누르면 어플리케이션 리스트와 DB내역 상품내역을 삭제합니다.
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

	// newInstance에서 파라미터로 리스트의 position을 extra로 받고 그 위치 삭제
	public static DialogFragmentAccept newInstance(int position) {

		DialogFragmentAccept fragment = new DialogFragmentAccept(position);
		return fragment;
	}

	DialogFragmentAccept(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mOrder = DataLongDistance.get(getActivity()).getLongDistances();
		return new AlertDialog.Builder(getActivity()).setTitle("승인 처리 하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {

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

	// --------------- 다이얼로그가 사용자에게 보여질 때 호출되는 것 ---------------//
	// 여기서 확인과 취소버튼 오버라이딩해서 기능 동작시킴 onCreateDialog에서 아래코드를 적으면 에러남
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
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
								// pos상인의 일회용 비트코인 주소 만들기와 DB저장까지 모두 하는 함수
								Log.e("test", "what?");
								StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
										.build();
								StrictMode.setThreadPolicy(policy); // 강제적으로
																	// 네트워크 접속
								actionCreateOrder();

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});
					Toast.makeText(getActivity(), "승인 처리 완료", Toast.LENGTH_LONG).show();
					dismiss(); // 다이얼로그 창 닫기

				} // onClick 종료
			});
			// 취소버튼 클릭시
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "승인 처리 취소", Toast.LENGTH_SHORT).show();
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

			// 기존 : 원거리주문 승인 시 qr코드를 생성하는 부분
			// library 제거 후 수정 목표 : db의 orders 
			
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