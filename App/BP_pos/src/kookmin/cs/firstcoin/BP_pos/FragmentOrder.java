/**
 * @brief �ֹ� �� �����׸�Ʈ
 * @details 
 * ���� ��Ƽ��Ƽ���� �ֹ����� ������ �� �ߴ� ȭ���� ������ �κ��Դϴ�.
 * �ֹ� ������ ��ǰ����Ʈ�� ���ϴ�.
 * ����Ʈ�� click�ϸ� ��ǰ ���� ���� ���̾�αװ� ��ϴ�.
 * ����Ʈ�� long click�ϸ� ��ǰ ���� ���̾�αװ� ��ϴ�.
 * �ֹ��ʱ�ȭ ��ư�� ������ ������ ��� 0���� �ʱ�ȭ�� �˴ϴ�.
 * ��ǰ�߰� ��ư�� ������ ��ǰ �߰� ���̾�αװ� ��ϴ�.
 * ��ǰ������ �����ϸ� ������ ���� ������ �ѱݾ׿� �ջ��� �˴ϴ�. 
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
	// ��ư����
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
		// container�ȿ� �����׸�Ʈ�� �������� �ʴ´ٸ� ���� ����
		// �̷��� if������ �������� ������ ���� �� �ݺ��ؼ� �����Ǽ� OrderListFragment�� ������ �����ǰ� ���ļ� ���̰�
		// �ȴ�.
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
		// �ؽ�Ʈ �� ����
		mTextTotalPrice = (TextView) rootView.findViewById(R.id.main_total_price);
		mTextTotalPrice.setText("�ѱݾ� : 0KRW ");

		mPaymentButton = (Button) rootView.findViewById(R.id.main_btn_payment);
		mPaymentButton.setOnClickListener(this);
		mPaymentButton.setText("�����ϱ�");

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

	// --------------- ��ư�� Ŭ������ �� ---------------//
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
				Toast.makeText(getActivity(), "��ǰ�� ���� �� �ּ���", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(getActivity(), "�ֹ��� �ʱ�ȭ �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
			break;
		}// switch
	}// onClick
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_PRICE) {
				int priceFromList = (Integer) data.getSerializableExtra("list_price");
				totalPrice += priceFromList;
				mTextTotalPrice.setText("�ѱݾ� : " + totalPrice + "KRW ");
				listFrg.getOrderAdapter().notifyDataSetChanged();
			}
		}
	}// onActivityResult

	public void OrderReset(){
		for (int i = 0; i < mProducts.size(); i++) { // ���� ������ ���� number ����
			// 0���� �ٲٰ�
			mProducts.get(i).setNumber("0");
		}
		totalPrice = 0;
		mTextTotalPrice.setText("�ѱݾ� : " + totalPrice + "KRW");
		
	}
	
}
