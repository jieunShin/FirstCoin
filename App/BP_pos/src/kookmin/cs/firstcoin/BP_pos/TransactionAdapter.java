/**
 * @brief �ŷ����� �����
 * @details �ŷ����� ����Ʈ ����ͷμ� �ŷ����� ��¥�� ����, ������ �����͸� ����.
 */


package kookmin.cs.firstcoin.BP_pos;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TransactionAdapter extends BaseAdapter {

	Context mContext=null;
	ArrayList<Transaction> mData=null;
	LayoutInflater mLayoutInflater=null;

	class ViewHolder
	{
		TextView transactionDate;
		TextView transactionPrice;
		TextView transactionContent;		
	}

	public TransactionAdapter (Context context,ArrayList<Transaction> data)
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
	public Transaction getItem(int position) {
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
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_transaction_layout,null);
			viewHolder= new ViewHolder();

			viewHolder.transactionDate = (TextView)itemLayout.findViewById(R.id.list_transaction_date);
			viewHolder.transactionPrice = (TextView)itemLayout.findViewById(R.id.list_transaction_price);
			viewHolder.transactionContent=(TextView)itemLayout.findViewById(R.id.list_transaction_content);

			itemLayout.setTag(viewHolder);
		}
		else // ������ ��� ��Ȧ�� ��ü�� ������ �ξ��� ������ ���� findViewById�Լ��� ������� �ʾƵ� ���ϴ� �並 ���� ����
		{
			viewHolder=(ViewHolder)itemLayout.getTag();
		}

		viewHolder.transactionContent.setText(mData.get(position).getContent());
		viewHolder.transactionDate.setText(mData.get(position).getDate());
		viewHolder.transactionPrice.setText(mData.get(position).getPrice()+"��");
		viewHolder = (ViewHolder)itemLayout.getTag();

		return itemLayout;
	}

	//������ �߰� ��� ����
	public void add(int index,Transaction addData)
	{
		mData.add(index,addData);
		notifyDataSetChanged();
	}

}
