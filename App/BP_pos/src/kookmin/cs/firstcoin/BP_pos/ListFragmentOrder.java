/**
 * @brief 주문 리스트 프래그먼트
 * @details 주문탭에서의 상품 리스트입니다.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//---------------  주문 탭의 리스트   ---------------//
public class ListFragmentOrder extends ListFragment {
	public final static int REQUEST_QUANTITY = 0;
	ArrayList<Product> mProducts;
	FragmentManager fm;
	private static ListFragmentOrder fragment = null;
	private static OrderAdapter adapter;

	public static ListFragmentOrder newInstance() {
		fragment = new ListFragmentOrder();
		return fragment;
	}

	// --------------- ---------------//
	public static OrderAdapter getOrderAdapter() {
		return adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		mProducts = DataProduct.get(getActivity()).getProducts();
		adapter = new OrderAdapter(mProducts);
		setListAdapter(adapter);
	}// onCreate

	public void onResume() {
		super.onResume();
	}// onResume

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// --------------- 롱 클릭 ---------------//
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// 삭제 다이얼로그 띄우기
				DialogFragmentDelete deleteDialog = DialogFragmentDelete.newInstance(position);
				deleteDialog.show(fm, "delete");
				return true;
			}
		});
	}

	// --------------- 일반 클릭 ---------------//
	public void onListItemClick(ListView l, View v, int position, long id) {
		// 다시 활성화
		DialogFragmentQuantity quantityDialog = new DialogFragmentQuantity(position);
		quantityDialog.setTargetFragment(ListFragmentOrder.this, REQUEST_QUANTITY);
		quantityDialog.show(fm, "quantitiy");
	}// onListItemClick

	// --------------- 수량 다이얼로그에서 수량이 반영된 가격을 받아옴. ---------------//
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_QUANTITY) {
				// 수량에 따른 변경된 가격을 p에 저장
				int price = (Integer) data.getSerializableExtra("quantity_value");
				// 수량에 따른 변경된 가격을 주문탭 Fragment인 FirstFragment에
				// 넘긴다(FirstFragment에서는 받은 다이얼로그에서 받은 가격을 총 금액에 반영
				if (getTargetFragment() != null) {
					Intent intent = new Intent();
					intent.putExtra("list_price", price);
					// 여기서 getTargetFragment()는 FirstFragment다. FirstFragment로
					// 수량에 따른 가격 price를 건낸다.
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

				}
			}
		}
	}// onActivityResult
		// 어댑터

	public class OrderAdapter extends ArrayAdapter<Product> {

		public OrderAdapter(ArrayList<Product> products) {
			super(getActivity(), 0, products);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_view_product_layout, null);
			}

			Product p = getItem(position);
			// 상품명
			TextView mName = (TextView) convertView.findViewById(R.id.list_product_name);
			mName.setText(p.getName());
			// 가격
			TextView mPrice = (TextView) convertView.findViewById(R.id.list_product_price);
			mPrice.setText(p.getPrice()+"원");
			// 수량
			TextView mNumber = (TextView) convertView.findViewById(R.id.list_product_quantity);
			mNumber.setText(p.getNumber());

			return convertView;
		}
	}// 어댑터
}
