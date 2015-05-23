package kookmin.cs.firstcoin.BP_order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import kookmin.cs.firstcoin.order.R;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coinplug.lib.wallet.CPUserService;
import com.coinplug.lib.wallet.CPWalletService;
import com.coinplug.lib.wallet.ICPUser;
import com.coinplug.lib.wallet.ICPWallet;
import com.coinplug.lib.wallet.listener.CPWAmountListener;
import com.coinplug.lib.wallet.listener.CPWGetAddressListener;
import com.coinplug.lib.wallet.listener.CPWGetBalanceListener;
import com.coinplug.lib.wallet.model.CPCurrencyAmount;

/*
 * '간편결제' 탭의 fragment
 * */
public class FragmentSimpleOrder extends Fragment {
	private final static int REQUEST_QUANTITY = 0;
	private final static int REQUEST_PRICE = 1;
	FragmentManager fm;
	private static final String TAB_NUMBER1 = "tab_number";
	View rootView;

	// 비트코인 보내기 관련 View
	private Button mButtonQrscan;
	private TextView mTextStatus;
	private TextView mTextResult;
	private TextView mSender, mAmount;
	private TextView mMessage;
	private Button mSend;
	private TextView myAddress;

	// Ok 비트카드 충건 관련 View
	private EditText pinText;
	private Button pinScan;
	private Button chargeBit;
	private String pin;

	// 비트코인 보내기 관련 스트링
	private String mOrderContent;
	private String mSendAddress;
	private String mSendAmount;
	private String mSendMessage;
	private ProgressDialog dialog = null;
	
	Intent i;

	private ICPWallet mWalletService;
	private ICPUser mUserService;
	private TextView mNowBtc;
	private TextView shopText;
	private Button mRefresh;
	private ImageView QrCode;
	private static BtcInfo btcInfo;
	
	private String order_id;

	public static FragmentSimpleOrder newInstance() {
		// FragmentSimpleOrder를 생성한다.
		return new FragmentSimpleOrder();
	}

	public FragmentSimpleOrder() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_simple_order, container, false);

		// 비트코인 보내기 관련
		mButtonQrscan = (Button) view.findViewById(R.id.button_simpleorder_qrscan);
		mSender = (TextView) view.findViewById(R.id.simple_order_sender);
		mAmount = (TextView) view.findViewById(R.id.simple_order_amount);
		mMessage = (TextView) view.findViewById(R.id.simple_order_message);
		mSend = (Button) view.findViewById(R.id.simple_order_send);
		mNowBtc = (TextView) view.findViewById(R.id.simple_order_btctokrw);
		myAddress = (TextView) view.findViewById(R.id.simple_order_address);
		QrCode = (ImageView) view.findViewById(R.id.qrcode_image);

		// OK 비트카드 충전 관련
		pinText = (EditText) view.findViewById(R.id.simple_order_pintext);
		;
		pinScan = (Button) view.findViewById(R.id.simple_order_pinscan);
		chargeBit = (Button) view.findViewById(R.id.button_simpleorder_charge);

		try {
			mUserService = CPUserService.getInstance(getActivity());
			mWalletService = CPWalletService.getInstance(getActivity());

		} catch (Exception e) {
			e.printStackTrace();
		}

		getAddress();
		getRate();

		mButtonQrscan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 로딩 다이얼로그 출력
				dialog = ProgressDialog.show(getActivity(), "", "Loading..", true);
				try {
					mWalletService = CPWalletService.getInstance(getActivity());
					sendBtc();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		QrCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					createQR();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		pinScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		chargeBit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					okBitcard();
					// 잔고 업데이트
					((ActivityMain) getActivity()).getBalance();
					pinText.setText("");

				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
		});

		return view;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			if (resultCode == -1) {
				// 추가한 부분
				try {
					mOrderContent = intent.getStringExtra("SCAN_RESULT");

					// string 가공
					StringTokenizer st, st2, st3;

					// 비트코인 보낼 정보 파싱
					st = new StringTokenizer(mOrderContent, ":");
					st2 = new StringTokenizer(mOrderContent, "&");

					if (st.nextToken() != null) {
						String temp = st.nextToken(); // bitcoin: 다음 부분

						// BTC량 파싱
						mSendAmount = st2.nextToken();
						mSendAmount = st2.nextToken();
						// amount = 0.0xxx 형태로 옴;
						st2 = new StringTokenizer(mSendAmount, "=");
						mSendAmount = st2.nextToken();
						mSendAmount = st2.nextToken();

						mAmount.setText(mSendAmount);

						// 주소 파싱
						Log.e("temp", temp);
						st3 = new StringTokenizer(temp, "?");
						mSendAddress = st3.nextToken();

						Log.e("address", mSendAddress);
						mSender.setText(mSendAddress);

						// 메시지 파싱
						st3 = new StringTokenizer(temp, "&");
						mSendMessage = st3.nextToken();
						mSendMessage = st3.nextToken();
						mSendMessage = st3.nextToken();
						st3 = new StringTokenizer(mSendMessage, "=");
						mSendMessage = st3.nextToken();
						mSendMessage = st3.nextToken();
						Log.e("message", mSendMessage);
						mMessage.setText(mSendMessage);
						order_id = mSendMessage;

					} // 제일 바깥 if
					else {
						mSender.setText(mOrderContent);
					}
				} // try
				catch (Exception e) {
					e.printStackTrace();
				}
			} else if (resultCode == 0) {
				
			}
		} // requestCode 0

		else if (requestCode == 1) {

			if (resultCode == -1) {

				pin = intent.getStringExtra("SCAN_RESULT");
				pinText.setText(pin);

			}
		}

	}

	private void sendBtc() {

		// 주소, 이메일 ,전화번호
		String sendAddress = mSender.getText().toString();
		Log.e("address", sendAddress);
		// 보내는 비트코인 량
		String amount = mAmount.getText().toString();
		
		if (amount == null || amount.length() < 1 || sendAddress.length() < 1) {
			// 로딩 다이얼로그 닫음
			dialog.dismiss();
			Toast.makeText(getActivity(), "QR코드를 스캔해주세요.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		double temp = Double.parseDouble(amount);
		amount = Double.toString(temp);
		Log.e("amount", amount);

		try {
			mWalletService.walletSendMoney(amount, sendAddress, "note", new CPWAmountListener() {

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
					// 로딩 다이얼로그 닫음
					dialog.dismiss();
					Toast.makeText(getActivity(), "입력 형식이 올바르지 않습니다." + throwable.toString() + " " + errorMsg,
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, CPCurrencyAmount amount) {
					// 로딩 다이얼로그 닫음
					dialog.dismiss();
					// 위의 액션바 잔고 업데이트
					((ActivityMain) getActivity()).getBalance();
					// 토스 메시지 띄움
					Toast.makeText(getActivity(), "보내기 성공", Toast.LENGTH_SHORT).show();
					// 한번 다 보내면 리셋
					mSender.setText("");
					mAmount.setText("");
					mMessage.setText("");

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost("http://203.246.112.131/user_simple_order_update.php");

						ArrayList<NameValuePair>params = new ArrayList<NameValuePair>(1);

						params.add(new BasicNameValuePair("order_id", order_id));
						Log.e("log_order_id", order_id);
						httppost.setEntity(new UrlEncodedFormEntity(params));
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String response = httpclient.execute(httppost, responseHandler);
						response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
						Log.e("log_order_",response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getAddress() {
		try {

			mWalletService.walletGetAddress(new CPWGetAddressListener() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, String address) {

					myAddress.setText(address);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
					Toast.makeText(getActivity(), "주소 가져오기 실패" + throwable.toString() + " " + errorMsg,
							Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createQR() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.dialog_qrcode, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(dialoglayout);

		final AlertDialog dialog = builder.create();

		dialog.show();

		// 이미지 연결
		ImageView addressImage = (ImageView) dialoglayout.findViewById(R.id.qrcode_image_address);

		TextView addressText = (TextView) dialoglayout.findViewById(R.id.qrcode_text_address);

		try {
			addressImage.setImageBitmap(mWalletService.walletCreateQRCode(myAddress.getText().toString()));

			addressText.setText(myAddress.getText().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getRate() {
		try {
			mWalletService.walletGetBalance(new CPWGetBalanceListener() {

				@SuppressLint("NewApi")
				@Override
				public void onSuccess(int statusCode, Header[] headers, CPCurrencyAmount currencyAmount) {
					try {
						btcInfo = ((ActivityMain) getActivity()).getBtcInfo();
						DecimalFormat commas = new DecimalFormat("#,###,###");
						String str = (String) commas.format((int) btcInfo.getBtc());
						mNowBtc.setText("1BTC = " + str + "원");
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
					Toast.makeText(getActivity(), "실패" + throwable.toString() + " " + errorMsg, Toast.LENGTH_SHORT)
							.show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void okBitcard() {
		String pin = pinText.getText().toString();
		try {
			mWalletService.walletRedeemBitcard(pin, new CPWAmountListener() {

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
					Toast.makeText(getActivity(), "충전 실패 : " + errorMsg, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, CPCurrencyAmount amount) {
					Toast.makeText(getActivity(), "충전 성공", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
