package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;
import java.util.Calendar;
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class FragmentTrx extends Fragment implements View.OnClickListener {
	public final static int TO_TIME = 0;
	public final static int FROM_TIME = 1;
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	TransactionAdapter transactionAdapter;
	ListView mListView = null;
	ArrayList<Transaction> initData = null; // 최근 거래내역을 저장
	ArrayList<Transaction> searchData = null; // 검색한 거래내역을 저장
	//
	private static final String TAB_NUMBER2 = "tab_number";
	private final static int REQUEST_TO_DATE = 0;
	private static final int REQUEST_FROM_DATE = 1;
	private final static int REQUEST_TO_TIME = 2;
	private final static int REQUEST_FROM_TIME = 3;

	private static final String DIALOG_DATE = "dialog";

	FragmentManager fm;
	// 날짜 처음
	int mToYear;
	int mToMonth;
	int mToDay;
	int mFromYear;
	int mFromMonth;
	int mFromDay;
	// 시간 처음
	int mToHour;
	int mFromHour;
	// 버튼
	Button mSearchButton;
	Button mToDateButton;
	Button mToTimeButton;
	Button mFromDateButton;
	Button mFromTimeButton;
	// view
	View rootView;
	// fragment
	private static FragmentTrx fragment;
	// 현재 날짜와 시간
	final Calendar c = Calendar.getInstance();

	ListFragmentTransaction listFrg;
	ArrayList<Transaction> mTransactions;

	// 싱글톤
	public static FragmentTrx newInstance() {
	
		fragment = new FragmentTrx();
		return fragment;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("debug", "second-onCreate");
		mTransactions = DataTransaction.get(getActivity()).getTransactions();
		fm = getActivity().getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.container_transaction_list);
		// container안에 프래그먼트가 존재하지 않는다면 새로 생성
		// 이렇게 if문으로 감싸주지 않으면 여러 번 반복해서 생성되서 TransactionListFragment가 여러번 생성되고
		// 횻캬 보이게 된다.
		if (fragment == null) {
			listFrg = new ListFragmentTransaction();
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.add(R.id.container_transaction_list, listFrg).commitAllowingStateLoss();
		}

	}// onCreate()

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_trx, container, false);
		// 날짜 초기화
		mToYear = c.get(Calendar.YEAR);
		mToMonth = c.get(Calendar.MONDAY);
		mToDay = c.get(Calendar.DAY_OF_MONTH) - 1;
		mToHour = c.get(Calendar.HOUR_OF_DAY);
		mFromYear = c.get(Calendar.YEAR);
		mFromMonth = c.get(Calendar.MONDAY);
		mFromDay = c.get(Calendar.DAY_OF_MONTH);
		mFromHour = c.get(Calendar.HOUR_OF_DAY);
		// search버튼
		mSearchButton = (Button) rootView.findViewById(R.id.button_search);
		mSearchButton.setOnClickListener(this);
		// To버튼에 대한 클릭
		mToDateButton = (Button) rootView.findViewById(R.id.button_to_date);
		mToDateButton.setOnClickListener(this);
		mToDateButton.setText(String.format("%d/%d/%d", mToYear, mToMonth + 1, mToDay));
		mToTimeButton = (Button) rootView.findViewById(R.id.button_to_time);
		mToTimeButton.setOnClickListener(this);
		mToTimeButton.setText(String.format("%d시", mToHour));
		// from버튼
		mFromDateButton = (Button) rootView.findViewById(R.id.button_from_date);
		mFromDateButton.setOnClickListener(this);
		mFromDateButton.setText(String.format("%d/%d/%d", mFromYear, mFromMonth + 1, mFromDay));
		mFromTimeButton = (Button) rootView.findViewById(R.id.button_from_time);
		mFromTimeButton.setOnClickListener(this);
		mFromTimeButton.setText(String.format("%d시", mFromHour));

		return rootView;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_search:
			searchView();
			break;
		case R.id.button_to_date:
			DialogFragment toDateDialog = DialogFragmentDate.newInstance(mToYear, mToMonth, mToDay);
			toDateDialog.setTargetFragment(FragmentTrx.this, REQUEST_TO_DATE);
			toDateDialog.show(fm, DIALOG_DATE);
			break;
		case R.id.button_to_time:
			DialogFragment toTimeDialog = DialogFragmentTime.newInstance(mToHour);
			toTimeDialog.setTargetFragment(FragmentTrx.this, REQUEST_TO_TIME);
			toTimeDialog.show(fm, DIALOG_DATE);
			break;
		case R.id.button_from_date:
			DialogFragment fromDateDialog = DialogFragmentDate.newInstance(mFromYear, mFromMonth, mFromDay);
			fromDateDialog.setTargetFragment(FragmentTrx.this, REQUEST_FROM_DATE);
			fromDateDialog.show(fm, DIALOG_DATE);
			break;
		case R.id.button_from_time:
			DialogFragment fromTimeDialog = DialogFragmentTime.newInstance(mFromHour);
			fromTimeDialog.setTargetFragment(FragmentTrx.this, REQUEST_FROM_TIME);
			fromTimeDialog.show(fm, DIALOG_DATE);
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_TO_DATE) {

			mToYear = data.getIntExtra(DialogFragmentDate.EXTRA_YEAR, 0);
			mToMonth = data.getIntExtra(DialogFragmentDate.EXTRA_MONTH, 0);
			mToDay = data.getIntExtra(DialogFragmentDate.EXTRA_DAY, 0);
			mToDateButton.setText(String.format("%d/%d/%d", mToYear, mToMonth + 1, mToDay));

		} else if (requestCode == REQUEST_TO_TIME) {
			mToHour = data.getIntExtra(DialogFragmentTime.EXTRA_HOUR, 0);
			mToTimeButton.setText(String.format("%d시", mToHour));
		} else if (requestCode == REQUEST_FROM_DATE) {
			mFromYear = data.getIntExtra(DialogFragmentDate.EXTRA_YEAR, 0);
			mFromMonth = data.getIntExtra(DialogFragmentDate.EXTRA_MONTH, 0);
			mFromDay = data.getIntExtra(DialogFragmentDate.EXTRA_DAY, 0);
			mFromDateButton.setText(String.format("%d/%d/%d", mFromYear, mFromMonth + 1, mFromDay));
		} else if (requestCode == REQUEST_FROM_TIME) {
			mFromHour = data.getIntExtra(DialogFragmentTime.EXTRA_HOUR, 0);
			mFromTimeButton.setText(String.format("%d시", mFromHour));
		}

	}

	public void searchView() {
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/pos_transaction_search.php");

		// post로 보낼 정보 가공
		String from = String.format("%04d-%02d-%02d %02d", mToYear, mToMonth + 1, mToDay, mToHour);
		String to = String.format("%04d-%02d-%02d %02d", mFromYear, mFromMonth + 1, mFromDay, mFromHour);

		// post로 보낼 정보 입력
		params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));
		params.add(new BasicNameValuePair("from", from));
		params.add(new BasicNameValuePair("to", to));

		Log.e("log_search", from);
		Log.e("log_search", to);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / 데이터를 받아옴
			// 받아온 데이터 형태 : 날짜,금액,주문내역\n

			String[] data = response.split("\n");
			Log.e("log_search : data_length", Integer.toString(data.length));
			final ArrayList<Transaction> searchData = new ArrayList<Transaction>();
			if (data[0].contains("not found")) {
				mTransactions.clear();
				return;
			}
		
			mTransactions.clear();
			for (int j = 0; j < data.length; j++) {
				Transaction trx = new Transaction(data[j]);
				mTransactions.add(trx);
			}
			listFrg.adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}