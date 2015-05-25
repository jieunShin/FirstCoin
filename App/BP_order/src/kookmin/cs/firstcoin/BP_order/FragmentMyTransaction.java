package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kookmin.cs.firstcoin.BP_order.TransactionListFragment.TransactionAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class FragmentMyTransaction extends Fragment implements View.OnClickListener {
	public final static int TO_TIME = 0;
	public final static int FROM_TIME = 1;
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	TransactionAdapter transactionAdapter;
	ListView mListView = null;
	ArrayList<Transaction> initData = null; // �ֱ� �ŷ������� ����
	ArrayList<Transaction> searchData = null; // �˻��� �ŷ������� ����
	
	private static final String TAB_NUMBER2 = "tab_number";
	private final static int REQUEST_TO_DATE = 0;
	private static final int REQUEST_FROM_DATE = 1;
	private final static int REQUEST_TO_TIME = 2;
	private final static int REQUEST_FROM_TIME = 3;

	private static final String DIALOG_DATE = "dialog";

	FragmentManager fm;
	// ��¥ ó��
	int mToYear;
	int mToMonth;
	int mToDay;
	int mFromYear;
	int mFromMonth;
	int mFromDay;
	// �ð� ó��
	int mToHour;
	int mFromHour;
	// ��ư
	Button mSearchButton;
	Button mToDateButton;
	Button mToTimeButton;
	Button mFromDateButton;
	Button mFromTimeButton;
	// view
	View rootView;
	// fragment
	private static FragmentMyTransaction fragment;
	// ���� ��¥�� �ð�
	final Calendar c = Calendar.getInstance();

	TransactionListFragment listFrg;
	ArrayList<Transaction> mTransactions;

	// �̱���
	public static FragmentMyTransaction newInstance() {
		fragment = new FragmentMyTransaction();

		return fragment;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("debug", "second-onCreate");
		mTransactions = TransactionData.get(getActivity()).getTransactions();
		// --------------- SecondFragment������ �� TransactionListFragment�� ���� �κ�
		fm = getActivity().getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.container_transaction_list);
		// container�ȿ� �����׸�Ʈ�� �������� �ʴ´ٸ� ���� ����
		// �̷��� if������ �������� ������ ���� �� �ݺ��ؼ� �����Ǽ� TransactionListFragment�� ������ �����ǰ�
		// ���ļ� ���̰� �ȴ�.
		if (fragment == null) {
			listFrg = new TransactionListFragment();
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.add(R.id.container_transaction_list, listFrg).commitAllowingStateLoss();
		}

	}// onCreate()

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_second_list_test, container, false);
		// ��¥ �ʱ�ȭ
		mToYear = c.get(Calendar.YEAR);
		mToMonth = c.get(Calendar.MONDAY);
		mToDay = c.get(Calendar.DAY_OF_MONTH) - 1;
		mToHour = c.get(Calendar.HOUR_OF_DAY);
		mFromYear = c.get(Calendar.YEAR);
		mFromMonth = c.get(Calendar.MONDAY);
		mFromDay = c.get(Calendar.DAY_OF_MONTH);
		mFromHour = c.get(Calendar.HOUR_OF_DAY);
		// search��ư
		mSearchButton = (Button) rootView.findViewById(R.id.button_search);
		mSearchButton.setOnClickListener(this);
		// To��ư�� ���� Ŭ��
		mToDateButton = (Button) rootView.findViewById(R.id.button_to_date);
		mToDateButton.setOnClickListener(this);
		mToDateButton.setText(String.format("%d/%d/%d", mToYear, mToMonth + 1, mToDay));
		mToTimeButton = (Button) rootView.findViewById(R.id.button_to_time);
		mToTimeButton.setOnClickListener(this);
		mToTimeButton.setText(String.format("%d��", mToHour));
		// from��ư
		mFromDateButton = (Button) rootView.findViewById(R.id.button_from_date);
		mFromDateButton.setOnClickListener(this);
		mFromDateButton.setText(String.format("%d/%d/%d", mFromYear, mFromMonth + 1, mFromDay));
		mFromTimeButton = (Button) rootView.findViewById(R.id.button_from_time);
		mFromTimeButton.setOnClickListener(this);
		mFromTimeButton.setText(String.format("%d��", mFromHour));

		return rootView;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_search:
			searchView();
			break;
		case R.id.button_to_date:
			DialogFragment toDateDialog = DatePickerFragment.newInstance(mToYear, mToMonth, mToDay);
			toDateDialog.setTargetFragment(FragmentMyTransaction.this, REQUEST_TO_DATE);
			toDateDialog.show(fm, DIALOG_DATE);
			break;
		case R.id.button_to_time:
			DialogFragment toTimeDialog = TimePickerFragment.newInstance(mToHour);
			toTimeDialog.setTargetFragment(FragmentMyTransaction.this, REQUEST_TO_TIME);
			toTimeDialog.show(fm, DIALOG_DATE);
			break;
		case R.id.button_from_date:
			DialogFragment fromDateDialog = DatePickerFragment.newInstance(mFromYear, mFromMonth, mFromDay);
			fromDateDialog.setTargetFragment(FragmentMyTransaction.this, REQUEST_FROM_DATE);
			fromDateDialog.show(fm, DIALOG_DATE);
			break;
		case R.id.button_from_time:
			DialogFragment fromTimeDialog = TimePickerFragment.newInstance(mFromHour);
			fromTimeDialog.setTargetFragment(FragmentMyTransaction.this, REQUEST_FROM_TIME);
			fromTimeDialog.show(fm, DIALOG_DATE);
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_TO_DATE) {

			mToYear = data.getIntExtra(DatePickerFragment.EXTRA_YEAR, 0);
			mToMonth = data.getIntExtra(DatePickerFragment.EXTRA_MONTH, 0);
			mToDay = data.getIntExtra(DatePickerFragment.EXTRA_DAY, 0);
			mToDateButton.setText(String.format("%d/%d/%d", mToYear, mToMonth + 1, mToDay));

		} else if (requestCode == REQUEST_TO_TIME) {
			mToHour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
			mToTimeButton.setText(String.format("%d��", mToHour));
		} else if (requestCode == REQUEST_FROM_DATE) {
			mFromYear = data.getIntExtra(DatePickerFragment.EXTRA_YEAR, 0);
			mFromMonth = data.getIntExtra(DatePickerFragment.EXTRA_MONTH, 0);
			mFromDay = data.getIntExtra(DatePickerFragment.EXTRA_DAY, 0);
			mFromDateButton.setText(String.format("%d/%d/%d", mFromYear, mFromMonth + 1, mFromDay));
		} else if (requestCode == REQUEST_FROM_TIME) {
			mFromHour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
			mFromTimeButton.setText(String.format("%d��", mFromHour));
		}

	}

	@SuppressLint("NewApi")
	public void searchView() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/user_transaction_search.php");

		// post�� ���� ���� ����
		String from = String.format("%04d-%02d-%02d %02d", mToYear, mToMonth + 1, mToDay, mToHour);
		String to = String.format("%04d-%02d-%02d %02d", mFromYear, mFromMonth + 1, mFromDay, mFromHour);

		// post�� ���� ���� �Է�
		params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("user_email", UserInfo.getEmail()));
		params.add(new BasicNameValuePair("from", from));
		params.add(new BasicNameValuePair("to", to));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / �����͸� �޾ƿ�
			// �޾ƿ� ������ ���� : ��¥,�ݾ�,�ֹ�����\n

			String[] data = response.split("\n");
			final ArrayList<Transaction> searchData = new ArrayList<Transaction>();
			if (data.length == 0)
				return;
			mTransactions.clear();
			for (int j = 0; j < data.length; j++) {
				Transaction trx = new Transaction(data[j]);
				mTransactions.add(trx);
			}
			listFrg.getTrxAdapter().notifyDataSetChanged();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
