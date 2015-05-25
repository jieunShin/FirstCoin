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

	// �޴�����Ʈ ������ ����
	ArrayList<Transaction> menuListData = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);

		Intent in = getIntent();
		merchant_id = in.getExtras().get("merchant_id").toString();
		store_name = in.getExtras().get("store_name").toString();

		// �׼ǹ� ( ����) ���� �κ�
		toolbar = (Toolbar) findViewById(R.id.order_toolbar);
		toolbar.setTitle("�ֹ� �ϱ� : " + store_name);

		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		TotalPrice = 0;
		// �ؽ�Ʈ �� ����
		mTextView = (TextView) findViewById(R.id.main_total_price);
		mTextView.setText("�ѱݾ� : 0KRW ");

		// ��ư ����
		mButton = (Button) findViewById(R.id.main_btn_payment);
		mButton.setText("�����ϱ�");

		// ����Ʈ �� ����
		mData = new ArrayList<Product>();

		// �⺻ ���� ������ ����
		setProductList();

		// ����Ʈ
		mEditPrice = (EditText) findViewById(R.id.list_product_price);

		orderAdapter = new OrderAdapter(this, mData);
		mListView = (ListView) findViewById(R.id.order_list_view);
		mListView.setAdapter(orderAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, final View view, final int position, long arg) {
				listDialog = new ListDialog(orderAdapter.mContext);
				listDialog.setTitle("���� ����");
				listDialog.show();

				// ���õ� ����Ʈ �� ������ ���̾�α׷� ����
				String name = mData.get(position).getName();
				final String price = mData.get(position).getPrice();
				String quantity = mData.get(position).getNumber();
				listDialog.setInfo(name, price, quantity);

				// list ���̾�α׿��� Ȯ�ι�ư ������ ��
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

						// ���� �ѱݾ� �ջ�
						TotalPrice += Integer.parseInt(price) * Integer.parseInt(str_quantity);
						// GetRages();
						mTextView.setText("�ѱݾ� : " + TotalPrice + "KRW");

						TextView listTextView = ((TextView) view.findViewById(R.id.list_product_quantity));
						listTextView.setText(str_quantity);

						// �߰��Ѻκ�
						mData.get(position).setNumber(str_quantity + "");

						
						Toast.makeText(ActivityOrder.this, "��ǰ ������ �ݿ��Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
						listDialog.dismiss(); // ���̾�α� â �ݱ�
					}

				});

				// list ���̾�α׿��� ��ҹ�ư ������ ��
				listDialog.setCancleClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(ActivityOrder.this, "���� ���� ���", Toast.LENGTH_SHORT).show();
						listDialog.cancel(); // ���̾�α� â ����
					}
				});

				// ����Ʈ ���̾�α� ��� ��� ó�� ������ -> ����Ű , ���̾�α� ���� ���� ��ġ, calcel�Լ�
				// ȣ�� � ���� ó���� �� �Լ����� �����
				listDialog.setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						Toast.makeText(ActivityOrder.this, "���", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});// setOnItemClickListener
	}// onCreate

	private void setProductList() {
		// TODO Auto-generated method stub
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/user_showMenuList.php");

		// post�� ���� ���� �Է�
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("merchant_id", merchant_id));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

			// / �����͸� �޾ƿ�
			// �޾ƿ� ������ ���� : �޴��̸�,����\n
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
		// db����
		MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());

		for (int i = 0; i < mData.size(); i++) {
			if (!mData.get(i).getNumber().equals("0")) {
				count++;
				handler.insert(mData.get(i).getName(), mData.get(i).getPrice(), mData.get(i).getNumber());
			}
		}

		if (count == 0) {
			Toast.makeText(ActivityOrder.this, "��ǰ�� ���� �� �ּ���", Toast.LENGTH_SHORT).show();
			return;
		}

		// ���� ������ �κ�
		Intent in = new Intent(ActivityOrder.this, ActivityPayment.class);
		in.putExtra("merchant_id", merchant_id);
		in.putExtra("TotalSum", TotalPrice);
		startActivity(in);
	}

	public void resetClick(View v) {
		for (int i = 0; i < mData.size(); i++) { // ���� ������ ���� number ���� 0���� �ٲٰ�
			mData.get(i).setNumber("0");
		}
		TotalPrice = 0;
		mListView.setAdapter(orderAdapter);
		mTextView.setText("�ѱݾ� : " + TotalPrice + "KRW");
		Toast.makeText(ActivityOrder.this, "�ֹ��� �ʱ�ȭ �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
	}
}
