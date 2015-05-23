package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import kookmin.cs.firstcoin.order.R;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LongDistanceAdapter extends BaseAdapter {
	public Context mContext=null;
	ArrayList<LongDistance> mLongdistance=null;
	LayoutInflater mLayoutInflater=null;
	
	FragmentManager fm;

	class ViewHolder
	{
		TextView storeName; // �ֹ� ���� �̸�
		TextView requestTime; // �ֹ���û �ð�
		TextView pricePd; //��ǰ ����
		TextView State; // ��������
	}

	public LongDistanceAdapter (Context context, ArrayList<LongDistance> data)
	{
		mContext = context;
		mLongdistance=data;
		mLayoutInflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return mLongdistance.size();
	}

	@Override
	public LongDistance getItem(int position) {
		return mLongdistance.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View itemLayout =convertView;
		ViewHolder viewHolder=null;

		//����ͺ䰡 ���� �� �並 �Ѱ����� ���� ��쿡�� ���ο� �� ����
		if(itemLayout==null)
		{
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_longdistance_layout,null);

			viewHolder= new ViewHolder();
			viewHolder.storeName=(TextView)itemLayout.findViewById(R.id.list_longdistance_storename);
			viewHolder.requestTime=(TextView)itemLayout.findViewById(R.id.list_longdistance_requesttime);
			viewHolder.pricePd=(TextView)itemLayout.findViewById(R.id.list_longdistance_price);
			viewHolder.State=(TextView)itemLayout.findViewById(R.id.list_longdistance_state);

			itemLayout.setTag(viewHolder);

		}
		else // ������ ��� ��Ȧ�� ��ü�� ������ �ξ��� ������ ���� findViewById�Լ��� ������� �ʾƵ� ���ϴ� �並 ���� ����
		{
			viewHolder=(ViewHolder)itemLayout.getTag();
		}
		
		viewHolder.storeName.setText(mLongdistance.get(position).getStoreName());
		viewHolder.requestTime.setText(mLongdistance.get(position).getOrderTime());
		viewHolder.pricePd.setText(mLongdistance.get(position).getPrice()+"��");
		switch(Integer.parseInt(mLongdistance.get(position).getOrderStatus()))
		{
			case 0: // �ֹ� ��û�� �� ����
				viewHolder.State.setText("���� ���");
				break;
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
			case 4: // pos���� ���� �ϷḦ Ȯ���ϰ� ��ǰ �غ� ���� ������Ʈ �� ����
				viewHolder.State.setText("��ǰ �غ� ��");
				break;
			case 5: // �Ⱦ� ����� ����
				viewHolder.State.setText("�غ� �Ϸ�");
				break;
		}
		
		//�ֹ� ���� ��ư Ŭ�� ������
		viewHolder.State.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							//���� ������� ������ ���� �������� ���̾�α� ������
							if(Integer.parseInt(mLongdistance.get(position).getOrderStatus())==2)
							{
								// Adapter���� Fragment �Ŵ����� ���� ���� ����
								// Fragment �Ŵ����� ���� ���̾�α� ������ ������.
								Intent intent = new Intent(v.getContext(), ActivityMain.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
								// �������� ���̾�α� ���� 
								DialogFragmentRemotePay remotepayDialog = DialogFragmentRemotePay.newInstance(position);
								remotepayDialog.show(fm,"����");
							} //if�� ��
							

						}
						catch (Exception e) {
							e.printStackTrace();
						}

					}
				}); // ������ ����
		
		return itemLayout;
	}

	//������ �߰� ��� ����
	public void add(int index,LongDistance addData)
	{
		mLongdistance.add(index,addData);
		notifyDataSetChanged();
	}
	
	

}
