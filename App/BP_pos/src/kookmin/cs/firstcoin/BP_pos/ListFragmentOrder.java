/**
 * @brief �ֹ� ����Ʈ �����׸�Ʈ
 * @details �ֹ��ǿ����� ��ǰ ����Ʈ�Դϴ�.
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

//---------------  �ֹ� ���� ����Ʈ   ---------------//
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
		// --------------- �� Ŭ�� ---------------//
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// ���� ���̾�α� ����
				DialogFragmentDelete deleteDialog = DialogFragmentDelete.newInstance(position);
				deleteDialog.show(fm, "delete");
				return true;
			}
		});
	}

	// --------------- �Ϲ� Ŭ�� ---------------//
	public void onListItemClick(ListView l, View v, int position, long id) {
		// �ٽ� Ȱ��ȭ
		DialogFragmentQuantity quantityDialog = new DialogFragmentQuantity(position);
		quantityDialog.setTargetFragment(ListFragmentOrder.this, REQUEST_QUANTITY);
		quantityDialog.show(fm, "quantitiy");
	}// onListItemClick

	// --------------- ���� ���̾�α׿��� ������ �ݿ��� ������ �޾ƿ�. ---------------//
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_QUANTITY) {
				// ������ ���� ����� ������ p�� ����
				int price = (Integer) data.getSerializableExtra("quantity_value");
				// ������ ���� ����� ������ �ֹ��� Fragment�� FirstFragment��
				// �ѱ��(FirstFragment������ ���� ���̾�α׿��� ���� ������ �� �ݾ׿� �ݿ�
				if (getTargetFragment() != null) {
					Intent intent = new Intent();
					intent.putExtra("list_price", price);
					// ���⼭ getTargetFragment()�� FirstFragment��. FirstFragment��
					// ������ ���� ���� price�� �ǳ���.
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

				}
			}
		}
	}// onActivityResult
		// �����

	public class OrderAdapter extends ArrayAdapter<Product> {

		public OrderAdapter(ArrayList<Product> products) {
			super(getActivity(), 0, products);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_view_product_layout, null);
			}

			Product p = getItem(position);
			// ��ǰ��
			TextView mName = (TextView) convertView.findViewById(R.id.list_product_name);
			mName.setText(p.getName());
			// ����
			TextView mPrice = (TextView) convertView.findViewById(R.id.list_product_price);
			mPrice.setText(p.getPrice()+"��");
			// ����
			TextView mNumber = (TextView) convertView.findViewById(R.id.list_product_quantity);
			mNumber.setText(p.getNumber());

			return convertView;
		}
	}// �����
}
