/**
 * @brief 주문 탭 프래그먼트
 * @details 
 * 메인 액티비티에서 주문탭을 눌렀을 때 뜨는 화면을 구현한 부분입니다.
 * 주문 가능한 상품리스트를 띄웁니다.
 * 리스트를 click하면 상품 수량 선택 다이얼로그가 뜹니다.
 * 리스트를 long click하면 상품 삭제 다이얼로그가 뜹니다.
 * 주문초기화 버튼을 누르면 수량이 모두 0으로 초기화가 됩니다.
 * 상품추가 버튼을 누르면 상품 추가 다이얼로그가 뜹니다.
 * 상품수량을 선택하면 수량에 따른 가격이 총금액에 합산이 됩니다. 
 */

package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentOrder extends Fragment implements View.OnClickListener {
	private final static int REQUEST_QUANTITY = 0;
	private final static int REQUEST_PRICE = 1;
	FragmentManager fm;
	ListFragmentOrder listFrg = null;
	
	private static final String TAB_NUMBER1 = "tab_number";
	View rootView;
	ListView mListView = null;
	TextView mTextTotalPrice = null;
	Intent i;
	EditText mEditPrice;
	ArrayList<Product> mProducts = null;
	OrderAdapter orderAdapter;
	private int totalPrice;
	String BtcInfo;

	String addName = "";
	String addPrice = "";
	String delName = "";
	String delPrice = "";
	// 버튼모음
	Button mOrderButton;
	Button mResetButton;
	Button mPaymentButton;
	boolean isLongClickClicked;

	// test
	private static FragmentOrder fragment = null;

	public static FragmentOrder newInstance() {
		
		fragment = new FragmentOrder();
		return fragment;
	}

	public FragmentOrder() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("debug", "onCreate");

	
		fm = getActivity().getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.container_list_fragment);
		// container안에 프래그먼트가 존재하지 않는다면 새로 생성
		// 이렇게 if문으로 감싸주지 않으면 여러 번 반복해서 생성되서 OrderListFragment가 여러번 생성되고 곂쳐서 보이게
		// 된다.
		if (fragment == null) {
			listFrg = new ListFragmentOrder();
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.add(R.id.container_list_fragment, listFrg).commitAllowingStateLoss();
			listFrg.setTargetFragment(FragmentOrder.this, REQUEST_PRICE);
		}

	}// onCreate()

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v("debuf", "onCreateView");
		rootView = inflater.inflate(R.layout.fragment_order, container, false);

		try {
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		totalPrice = 0;
		// 텍스트 뷰 영역
		mTextTotalPrice = (TextView) rootView.findViewById(R.id.main_total_price);
		mTextTotalPrice.setText("총금액 : 0KRW ");

		mPaymentButton = (Button) rootView.findViewById(R.id.main_btn_payment);
		mPaymentButton.setOnClickListener(this);
		mPaymentButton.setText("결제하기");

		mOrderButton = (Button) rootView.findViewById(R.id.order_btn_add);
		mOrderButton.setOnClickListener(this);

		mResetButton = (Button) rootView.findViewById(R.id.order_btn_reset);
		mResetButton.setOnClickListener(this);

		mProducts = DataProduct.get(getActivity()).getProducts();
		OrderReset();
		
		return rootView;
	}// onCreateView

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("debug", "onActivityCreated");
	}// onActivityCreated

	public void onResume() {
		Log.v("debug", "onResume");
		super.onResume();
	}// onResume

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("debug", "onAttach");
	}

	// --------------- 버튼을 클릭했을 때 ---------------//
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_btn_payment:

			int count = 0;

			MySQLiteHandler handler = MySQLiteHandler.open(getActivity().getApplicationContext());

			for (int i = 0; i < mProducts.size(); i++) {
				if (!mProducts.get(i).getNumber().equals("0")) {
					count++;
					handler.insert(mProducts.get(i).getName(), mProducts.get(i).getPrice(), mProducts.get(i)
							.getNumber());
				}
			}

			if (count == 0) {
				Toast.makeText(getActivity(), "물품을 선택 해 주세요", Toast.LENGTH_SHORT).show();
				break;
			}

			Intent in = new Intent(getActivity(), ActivityPayment.class);
			in.putExtra("TotalSum", totalPrice);
			startActivity(in);
			break;
		case R.id.order_btn_add:
			DialogFragmentAdd addDialog = new DialogFragmentAdd();
			addDialog.setTargetFragment(FragmentOrder.this, REQUEST_QUANTITY);
			addDialog.show(fm, "dialog");
			break;
		case R.id.order_btn_reset:
			OrderReset();
			listFrg.getOrderAdapter().notifyDataSetChanged();
			Toast.makeText(getActivity(), "주문이 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
			break;
		}// switch
	}// onClick
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_PRICE) {
				int priceFromList = (Integer) data.getSerializableExtra("list_price");
				totalPrice += priceFromList;
				mTextTotalPrice.setText("총금액 : " + totalPrice + "KRW ");
				listFrg.getOrderAdapter().notifyDataSetChanged();
			}
		}
	}// onActivityResult

	public void OrderReset(){
		for (int i = 0; i < mProducts.size(); i++) { // 기존 데이터 들의 number 값을
			// 0으로 바꾸고
			mProducts.get(i).setNumber("0");
		}
		totalPrice = 0;
		mTextTotalPrice.setText("총금액 : " + totalPrice + "KRW");
		
	}
	
}
