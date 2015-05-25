package kookmin.cs.firstcoin.BP_order;

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

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityOrder extends ActionBarActivity {

	ListView mListView = null;
	TextView mTextView = null;
	Button mButton = null;
	EditText mEditPrice;
	private Toolbar toolbar;
	ArrayList<Product> mData = null;
	OrderAdapter orderAdapter;
	ListDialog listDialog = null;
	int TotalPrice;
	String BtcInfo;

	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	String merchant_id;
	String store_name;

	// 메뉴리스트 정보를 저장
	ArrayList<Transaction> menuListData = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);

		Intent in = getIntent();
		merchant_id = in.getExtras().get("merchant_id").toString();
		store_name = in.getExtras().get("store_name").toString();

		// 액션바 ( 툴바) 설정 부분
		toolbar = (Toolbar) findViewById(R.id.order_toolbar);
		toolbar.setTitle("주문 하기 : " + store_name);

		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		TotalPrice = 0;
		// 텍스트 뷰 영역
		mTextView = (TextView) findViewById(R.id.main_total_price);
		mTextView.setText("총금액 : 0KRW ");

		// 버튼 영역
		mButton = (Button) findViewById(R.id.main_btn_payment);
		mButton.setText("결제하기");

		// 리스트 뷰 영역
		mData = new ArrayList<Product>();

		// 기본 예제 데이터 세팅
		setProductList();

		// 에디트
		mEditPrice = (EditText) findViewById(R.id.list_product_price);

		orderAdapter = new OrderAdapter(this, mData);
		mListView = (ListView) findViewById(R.id.order_list_view);
		mListView.setAdapter(orderAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, final View view, final int position, long arg) {
				listDialog = new ListDialog(orderAdapter.mContext);
				listDialog.setTitle("수량 선택");
				listDialog.show();

				// 선택된 리스트 의 정보를 다이얼로그로 전달
				String name = mData.get(position).getName();
				final String price = mData.get(position).getPrice();
				String quantity = mData.get(position).getNumber();
				listDialog.setInfo(name, price, quantity);

				// list 다이얼로그에서 확인버튼 눌렀을 시
				listDialog.setOkClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						TextView setColor1 = ((TextView) view.findViewById(R.id.list_product_name));
						TextView setColor2 = ((TextView) view.findViewById(R.id.list_product_price));
						TextView setColor3 = ((TextView) view.findViewById(R.id.list_product_quantity));
						String prevNumber = mData.get(position).getNumber();
						if (!prevNumber.equals("0")) {
							TotalPrice -= Integer.parseInt(price) * Integer.parseInt(prevNumber);
						}

						String str_quantity = listDialog.spinner.getSelectedItem().toString();
						mData.get(position).setNumber(str_quantity);

						// 최종 총금액 합산
						TotalPrice += Integer.parseInt(price) * Integer.parseInt(str_quantity);
						// GetRages();
						mTextView.setText("총금액 : " + TotalPrice + "KRW");

						TextView listTextView = ((TextView) view.findViewById(R.id.list_product_quantity));
						listTextView.setText(str_quantity);

						// 추가한부분
						mData.get(position).setNumber(str_quantity + "");

						
						Toast.makeText(ActivityOrder.this, "상품 수량이 반영되었습니다.", Toast.LENGTH_SHORT).show();
						listDialog.dismiss(); // 다이얼로그 창 닫기
					}

				});

				// list 다이얼로그에서 취소버튼 눌렀을 시
				listDialog.setCancleClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(ActivityOrder.this, "수량 선택 취소", Toast.LENGTH_SHORT).show();
						listDialog.cancel(); // 다이얼로그 창 종료
					}
				});

				// 리스트 다이얼로그 모든 취소 처리 리스너 -> 이전키 , 다이얼로그 밖의 영역 터치, calcel함수
				// 호출 등에 따른 처리를 이 함수에서 모두함
				listDialog.setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						Toast.makeText(ActivityOrder.this, "취소", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});// setOnItemClickListener
	}// onCreate

	private void setProductList() {
		// TODO Auto-generated method stub
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/user_showMenuList.php");

		// post로 보낼 정보 입력
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("merchant_id", merchant_id));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / 데이터를 받아옴
			// 받아온 데이터 형태 : 메뉴이름,가격\n
			String[] data = response.split("\n");

			for (int i = 0; i < data.length; i++) {
				Product menu = new Product(data[i]);
				mData.add(menu);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void payClick(View v) {
		int count = 0;
		// db연결
		MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());

		for (int i = 0; i < mData.size(); i++) {
			if (!mData.get(i).getNumber().equals("0")) {
				count++;
				handler.insert(mData.get(i).getName(), mData.get(i).getPrice(), mData.get(i).getNumber());
			}
		}

		if (count == 0) {
			Toast.makeText(ActivityOrder.this, "물품을 선택 해 주세요", Toast.LENGTH_SHORT).show();
			return;
		}

		// 내가 수정한 부분
		Intent in = new Intent(ActivityOrder.this, ActivityPayment.class);
		in.putExtra("merchant_id", merchant_id);
		in.putExtra("TotalSum", TotalPrice);
		startActivity(in);
	}

	public void resetClick(View v) {
		for (int i = 0; i < mData.size(); i++) { // 기존 데이터 들의 number 값을 0으로 바꾸고
			mData.get(i).setNumber("0");
		}
		TotalPrice = 0;
		mListView.setAdapter(orderAdapter);
		mTextView.setText("총금액 : " + TotalPrice + "KRW");
		Toast.makeText(ActivityOrder.this, "주문이 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
	}
}
