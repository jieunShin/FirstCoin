/**
 * @brief ���Ÿ� ����Ʈ �����
 * @details ������ȣ, ��ǰ�̸�, ��ǰ����, ��ǰ����, �������¸� ����Ʈ�� ��Ÿ��.
 */

package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LongDistanceRequestAdapter extends BaseAdapter {

	public Context mContext = null;
	ArrayList<LongDistance> mLongdistance = null;
	LayoutInflater mLayoutInflater = null;

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	List<NameValuePair> params;

	FragmentManager fm;

	class ViewHolder {
		TextView namePd; // ��ǰ ����
		TextView pricePd; // ��ǰ ����
		TextView requestTime; // �ֹ���û �ð�
		TextView yes; // ��������
		TextView no;
	}

	public LongDistanceRequestAdapter(Context context, ArrayList<LongDistance> data) {
		mContext = context;
		mLongdistance = data;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLongdistance.size();
	}

	@Override
	public LongDistance getItem(int position) {
		// TODO Auto-generated method stub
		return mLongdistance.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View itemLayout = convertView;
		ViewHolder viewHolder = null;

		// ����ͺ䰡 ���� �� �並 �Ѱ����� ���� ��쿡�� ���ο� �� ����
		if (itemLayout == null) {
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_longdistance_request_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.requestTime = (TextView) itemLayout.findViewById(R.id.list_longdistance_requesttime);
			viewHolder.namePd = (TextView) itemLayout.findViewById(R.id.list_longdistance_name);
			viewHolder.pricePd = (TextView) itemLayout.findViewById(R.id.list_longdistance_price);
			viewHolder.yes = (TextView) itemLayout.findViewById(R.id.list_longdistance_yes);
			viewHolder.no = (TextView) itemLayout.findViewById(R.id.list_longdistance_no);

			itemLayout.setTag(viewHolder);

		} else // ������ ��� ��Ȧ�� ��ü�� ������ �ξ��� ������ ���� findViewById�Լ��� ������� �ʾƵ� ���ϴ� �並
				// ���� ����
		{
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.requestTime.setText(mLongdistance.get(position).getOrderTime());
		viewHolder.namePd.setText(mLongdistance.get(position).getName());
		viewHolder.pricePd.setText(mLongdistance.get(position).getPrice()+"��");

		// ���ι�ư ���� ��
		viewHolder.yes.setOnClickListener(

		new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(v.getContext(), ActivityMain.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
					// ���� ���̾�α� ����
					DialogFragmentAccept acceptDialog = DialogFragmentAccept.newInstance(position);
					acceptDialog.show(fm, "����");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		// ������ư ���� ��
		viewHolder.no.setOnClickListener(

		new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(v.getContext(), ActivityMain.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
					// ���� ���̾�α� ����
					DialogFragmentReject acceptDialog = DialogFragmentReject.newInstance(position);
					acceptDialog.show(fm, "����");


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		return itemLayout;
	}

	// ������ �߰� ��� ����
	public void add(int index, LongDistance addData) {

		mLongdistance.add(index, addData);
		notifyDataSetChanged();
	}

}