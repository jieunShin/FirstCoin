/**
 * @brief 상품 삭제 다이얼로그
 * @details 주문탭에서 상품 리스트를 long click 했을 때 뜨는 다이얼로그, 확인을 누르면 어플리케이션 리스트와 DB내역 상품내역을 삭제합니다.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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

class DialogFragmentDelete extends DialogFragment {
	ArrayList<Product> mProducts;
	AlertDialog d;
	//
	int mPosition;
	String delName = "";
	String delPrice = "";

	// newInstance에서 파라미터로 리스트의 position을 extra로 받고 그 위치 삭제
	public static DialogFragmentDelete newInstance(int position) {

		DialogFragmentDelete fragment = new DialogFragmentDelete(position);
		return fragment;
	}

	DialogFragmentDelete(int position) {
		mPosition = position;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mProducts = DataProduct.get(getActivity()).getProducts();
		return new AlertDialog.Builder(getActivity()).setTitle("정말 삭제 하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dismiss();

					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dismiss();

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
		d = (AlertDialog) getDialog();
		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Log.v("" + mPosition, "m");
							delName = mProducts.get(mPosition).getName();
							delPrice = mProducts.get(mPosition).getPrice();
							Log.v("run()", "돌아가나");
							try {

								URL url = new URL("http://203.246.112.131/pos_deleteItem.php?" + "merchant_id="
										+ URLEncoder.encode(UserInfo.getMerchantId(), "UTF-8") + "&name="
										+ URLEncoder.encode(delName, "UTF-8") + "&price="
										+ URLEncoder.encode(delPrice, "UTF-8"));
								url.openStream();
								delName = "";
								delPrice = "";

							} catch (Exception e) {
								Log.e("Error", e.getMessage());
							}
						}
					});

					if (delName == "" && delPrice == "") {

						mProducts.remove(mPosition); // 파라미터로 받아온
						// 해당 포지션의
						// 데이터 값을
						// 삭제한다.
						// 내가 수정

						// orderAdapter.notifyDataSetChanged(); // 어댑터를
						// 업데이트해서
						// 데이터가
						// 바뀌었다는것을
						// 인지하고
						// 리스트
						// 뷰로
						// 그것을
						// 나타내는
						// 기능.

						Toast.makeText(getActivity(), "상품 삭제 완료", Toast.LENGTH_SHORT).show();
						((ListFragmentOrder) ListFragmentOrder.newInstance()).getOrderAdapter().notifyDataSetChanged();
						dismiss();
					} else {
						Toast.makeText(getActivity(), "상품 삭제 실패", Toast.LENGTH_LONG).show();
					}
					Toast.makeText(getActivity(), "상품 삭제 완료", Toast.LENGTH_SHORT).show();
					
					
				}

			});
		}// if
		
	}// onstart()

	public void onDismiss(DialogInterface dialog) {

	}

	public DialogFragmentDelete() {
		// TODO Auto-generated constructor stub
	}

}
