package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LongDistanceAcceptAdapter extends BaseAdapter {

	public Context mContext = null;
	ArrayList<LongDistance> mLongdistance = null;
	LayoutInflater mLayoutInflater = null;

	FragmentManager fm;

	class ViewHolder {
		TextView requestTime; // �ֹ���û �ð�
		TextView namePd; // ��ǰ ����
		TextView pricePd; // ��ǰ ����
		TextView State; // ��������
	}

	public LongDistanceAcceptAdapter(Context context, ArrayList<LongDistance> data) {
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
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_longdistance_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.requestTime = (TextView) itemLayout.findViewById(R.id.list_longdistance_requesttime);
			viewHolder.namePd = (TextView) itemLayout.findViewById(R.id.list_longdistance_name);
			viewHolder.pricePd = (TextView) itemLayout.findViewById(R.id.list_longdistance_price);
			viewHolder.State = (TextView) itemLayout.findViewById(R.id.list_longdistance_state);

			itemLayout.setTag(viewHolder);

		} else // ������ ��� ��Ȧ�� ��ü�� ������ �ξ��� ������ ���� findViewById�Լ��� ������� �ʾƵ� ���ϴ� �並
				// ���� ����
		{
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.requestTime.setText(mLongdistance.get(position).getOrderTime());
		viewHolder.namePd.setText(mLongdistance.get(position).getName());
		viewHolder.pricePd.setText(mLongdistance.get(position).getPrice()+"��");
		switch (Integer.parseInt(mLongdistance.get(position).getOrderStatus())) {

		case -1: // �ֹ� ��û�� ������ ����
			viewHolder.State.setText("�ֹ� ����");
			break;
		case 1: // �ֹ� ������ ������ qrcode�� ������Ʈ ���� ���� ����
			viewHolder.State.setText("���� ����");
			break;
		case 2: // �ֹ� ���α��� �� ����
			viewHolder.State.setText("���� ���");
			break;
		case 3: // ���� �ϷḸ ���� ����
			viewHolder.State.setText("���� �Ϸ�");
			break;
		case 4: // ��ǰ �غ� ���� ������Ʈ �� ����
			viewHolder.State.setText("��ǰ �غ� ��");
			break;
		case 5: // �Ⱦ� ����� ����
			viewHolder.State.setText("�غ� �Ϸ�");
			break;
		}

		// �ֹ� ���� ��ư Ŭ�� ������
		viewHolder.State.setOnClickListener(

		new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					// ��������, ���� ������� ���¸� ������ ��� ���¿� ���� ������Ʈ �����ϵ��� ��
					if (Integer.parseInt(mLongdistance.get(position).getOrderStatus()) >= 3) {
						// Adapter���� Fragment �Ŵ����� ���� ���� ����
						// Fragment �Ŵ����� ���� ���̾�α� ������ ������.
						Intent intent = new Intent(v.getContext(), ActivityMain.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
						// �������� ���̾�α� ����
						DialogFragmentStateUpdate stateUpdateDialog = DialogFragmentStateUpdate.newInstance(position);
						stateUpdateDialog.show(fm, "������Ʈ");
					} // if�� ��

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}); // ������ ����

		return itemLayout;
	}

	// ������ �߰� ��� ����
	public void add(int index, LongDistance addData) {

		mLongdistance.add(index, addData);
		notifyDataSetChanged();
	}

}
