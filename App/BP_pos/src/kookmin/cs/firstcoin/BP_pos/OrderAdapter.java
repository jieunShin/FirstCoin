/**
 * @brief �ֹ� ����Ʈ�� �����
 * @details ����Ʈ�� ���Ǵ� �����
 */


package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {


	public Context mContext=null;
	ArrayList<Product> mData=null;
	LayoutInflater mLayoutInflater=null;

	class ViewHolder
	{
		TextView namePd;
		TextView pricePd;
		TextView numberPd;
	}

	public OrderAdapter (Context context,ArrayList<Product> data)
	{
		mContext = context;
		mData=data;
		mLayoutInflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Product getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View itemLayout =convertView;
		ViewHolder viewHolder=null;

		//����ͺ䰡 ���� �� �並 �Ѱ����� ���� ��쿡�� ���ο� �� ����
		if(itemLayout==null)
		{
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_product_layout,null);


			viewHolder= new ViewHolder();
			viewHolder.namePd=(TextView)itemLayout.findViewById(R.id.list_product_name);
			viewHolder.pricePd=(TextView)itemLayout.findViewById(R.id.list_product_price);
			viewHolder.numberPd=(TextView)itemLayout.findViewById(R.id.list_product_quantity);

			itemLayout.setTag(viewHolder);

		}
		else // ������ ��� ��Ȧ�� ��ü�� ������ �ξ��� ������ ���� findViewById�Լ��� ������� �ʾƵ� ���ϴ� �並 ���� ����
		{
			viewHolder=(ViewHolder)itemLayout.getTag();
		}

		viewHolder.namePd.setText(mData.get(position).getName());
		viewHolder.pricePd.setText(mData.get(position).getPrice()+"��");
		viewHolder.numberPd.setText(mData.get(position).getNumber());


		return itemLayout;
	}

	//������ �߰� ��� ����
	public void add(int index,Product addData)
	{
		
		mData.add(index,addData);
		notifyDataSetChanged();
	}
}
