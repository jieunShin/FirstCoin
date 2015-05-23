package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import kookmin.cs.firstcoin.order.R;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionListFragment extends ListFragment {
	ArrayList<Transaction> mTransactions;
	FragmentManager fm;
	private static TransactionAdapter adapter;

	public static TransactionListFragment newInstance() {
		TransactionListFragment fragment = new TransactionListFragment();
		return fragment;
	}

	public TransactionListFragment() {
		// TODO Auto-generated constructor stub
	}

	public static TransactionAdapter getTrxAdapter() {
		return adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		mTransactions = TransactionData.get(getActivity()).getTransactions();
		adapter = new TransactionAdapter(mTransactions);
		setListAdapter(adapter);
	}// onCreate
		// 일바클릭

	public void onListItemClick(ListView l, View v, int position, long id) {
		// 다시 활성화
		TrxInforDialogFragment inforDialog = new TrxInforDialogFragment(position);
		inforDialog.show(fm, "quantitiy");
	}// onListItemClick

	public class TransactionAdapter extends ArrayAdapter<Transaction> {

		public TransactionAdapter(ArrayList<Transaction> transactions) {
			super(getActivity(), 0, transactions);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_view_transaction_layout, null);
			}

			Transaction t = getItem(position);
			// data
			TextView mData = (TextView) convertView.findViewById(R.id.list_transaction_date);
			mData.setText(t.getDate());
			// price
			TextView mPrice = (TextView) convertView.findViewById(R.id.list_transaction_price);
			mPrice.setText(t.getPrice()+"원");
			// content
			TextView mContent = (TextView) convertView.findViewById(R.id.list_transaction_content);
			mContent.setText(t.getContent());

			return convertView;
		}
	}// 어댑터

}
