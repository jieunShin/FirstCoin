package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RemoteOrderAdapter extends BaseAdapter {

	public Context mContext = null;
	ArrayList<RemoteOrder> mOrder = null;
	LayoutInflater mLayoutInflater = null;

	class ViewHolder {
		TextView requestTime; // �ֹ���û �ð�
		TextView namePd; // ��ǰ ����
		TextView pricePd; // ��ǰ ����
		Button State; // ��������
	}

	public RemoteOrderAdapter(Context context, ArrayList<RemoteOrder> data) {
		mContext = context;
		mOrder = data;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mOrder.size();
	}

	@Override
	public RemoteOrder getItem(int position) {
		// TODO Auto-generated method stub
		return mOrder.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View itemLayout = convertView;
		ViewHolder viewHolder = null;

		// ����ͺ䰡 ���� �� �並 �Ѱ����� ���� ��쿡�� ���ο� �� ����
		if (itemLayout == null) {
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_remoteorder_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.requestTime = (TextView) itemLayout.findViewById(R.id.list_remote_requesttime);
			viewHolder.namePd = (TextView) itemLayout.findViewById(R.id.list_remote_name);
			viewHolder.pricePd = (TextView) itemLayout.findViewById(R.id.list_remote_price);
			viewHolder.State = (Button) itemLayout.findViewById(R.id.list_remote_state);

			itemLayout.setTag(viewHolder);

		} else // ������ ��� ��Ȧ�� ��ü�� ������ �ξ��� ������ ���� findViewById�Լ��� ������� �ʾƵ� ���ϴ� �並
				// ���� ����
		{
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.requestTime.setText(mOrder.get(position).getOrderTime());
		viewHolder.namePd.setText(mOrder.get(position).getContent());
		viewHolder.pricePd.setText(mOrder.get(position).getPrice());
		viewHolder.State.setText("�ֹ� ����");

		return itemLayout;
	}

	// ������ �߰� ��� ����
	public void add(int index, RemoteOrder addData) {

		mOrder.add(index, addData);
		notifyDataSetChanged();
	}

}
