/**
 * @brief 상품추가 다이얼로그 프래그먼트
 * @details 주문탭의 상품추가 버튼을 눌렀을 때 뜨는 다이얼로그, 확인을 눌렀을 때 어플리케이션 리스트와 DB에 상품이 추가됩니다.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogFragmentAdd extends DialogFragment {
	ArrayList<Product> mProducts = null;
	String addName = "";
	String addPrice = "";
	String pName = "";
	String pPrice = "";
	String pNum = "0";
	TextView pdInfo = null;
	private EditText pdName = null;
	private EditText pdPrice = null;
	Button OkBtn = null;
	Button CancelBtn = null;
	AlertDialog.Builder builder;
	View v;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// return null;
		mProducts = DataProduct.get(getActivity()).getProducts();
		v = getActivity().getLayoutInflater().inflate(R.layout.order_add_popup, null);
		pdName = (EditText) v.findViewById(R.id.popup_add_name);
		pdPrice = (EditText) v.findViewById(R.id.popup_add_price);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("상품 추가").setView(v);
		builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 여기다가 작성 안함 오버라이딩 onstart에서 작성
			}
		});
		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 여기도 오버라이딩
			}
		});
		//다이얼로그에서 back 버튼 눌렀을 때 처리
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog,
					int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					return true;
				}
				return false;
			}
		});

		return builder.create();
	}

	// --------------- 다이얼로그가 사용자에게 보여질 때 호출되는 것 ---------------//
	// 여기서 확인과 취소버튼 오버라이딩해서 기능 동작시킴 onCreateDialog에서 아래코드를 적으면 에러남
	public void onStart() {
		super.onStart();
		AlertDialog d = (AlertDialog) getDialog();
		pdInfo = (TextView) v.findViewById(R.id.popup_add_info);

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
					addName = pdName.getText().toString();
					addPrice = pdPrice.getText().toString();

					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							try {
								URL url = new URL("http://203.246.112.131/pos_addItem.php?" + "merchant_id="
										+ URLEncoder.encode(UserInfo.getMerchantId(), "UTF-8") + "&name="
										+ URLEncoder.encode(addName, "UTF-8") + "&price="
										+ URLEncoder.encode(addPrice, "UTF-8"));
								url.openStream();

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});

					if (addName != "" && addPrice != "") {
						Product product = new Product();
						product.setName(addName);
						product.setPrice(addPrice);
						product.setNumber("0");
						mProducts.add(product);
						ListFragmentOrder.getOrderAdapter().notifyDataSetChanged();
						Toast.makeText(getActivity(), "상품 추가 완료", Toast.LENGTH_SHORT).show();
						dismiss(); // 다이얼로그 창 닫기
					} else {
						Toast.makeText(getActivity(), "상품 추가 실패", Toast.LENGTH_LONG).show();
					}
				}
			});
			// 취소버튼 클릭시
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "상품 추가 취소", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}
	}// onStart
}
