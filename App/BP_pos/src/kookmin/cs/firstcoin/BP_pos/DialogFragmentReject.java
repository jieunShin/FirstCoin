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
import android.widget.Toast;

class DialogFragmentReject extends DialogFragment {

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	List<NameValuePair> params;

	ArrayList<LongDistance> mLongdistance = null;
	AlertDialog d;
	int mPosition;


	// newInstance에서 파라미터로 리스트의 position을 extra로 받고 그 위치 삭제
	public static DialogFragmentReject newInstance(int position) {

		DialogFragmentReject fragment = new DialogFragmentReject(position);
		return fragment;
	}

	DialogFragmentReject(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mLongdistance = DataLongDistance.get(getActivity()).getLongDistances();
		return new AlertDialog.Builder(getActivity()).setTitle("거절 처리 하시겠습니까?")
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
								// 임의의 코드
								String msg = "상품 재고가 없습니다.";
								httpclient = new DefaultHttpClient();

								httppost = new HttpPost("http://203.246.112.131/pos_remote_response_no.php");

								params = new ArrayList<NameValuePair>(1);
								params.add(new BasicNameValuePair("order_id", mLongdistance.get(mPosition).getOrderId()));
								params.add(new BasicNameValuePair("msg", msg));

								httppost.setEntity(new UrlEncodedFormEntity(params));
								httpresponse = httpclient.execute(httppost);
								ResponseHandler<String> responseHandler = new BasicResponseHandler();

								String response = httpclient.execute(httppost, responseHandler);
								response = new String(response.getBytes("ISO-8859-1"), "UTF-8");


							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});
					
					Toast.makeText(getActivity(), "거절 처리 완료", Toast.LENGTH_LONG).show();
					dismiss(); // 다이얼로그 창 닫기

				} // onClick 종료
			});
			// 취소버튼 클릭시
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "거절 처리 취소", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}

	}// onstart()

	public void onDismiss(DialogInterface dialog) {

	}

	public DialogFragmentReject() {
		// TODO Auto-generated constructor stub
	}

}
