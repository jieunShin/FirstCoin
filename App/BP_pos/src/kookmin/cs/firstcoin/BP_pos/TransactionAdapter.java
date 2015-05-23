/**
 * @brief 거래내역 어댑터
 * @details 거래내역 리스트 어댑터로서 거래내역 날짜와 가격, 내용의 데이터를 지님.
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

		//어댑터뷰가 재사용 할 뷰를 넘겨주지 않은 경우에만 새로운 뷰 생성
		if(itemLayout==null)
		{
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_transaction_layout,null);
			viewHolder= new ViewHolder();

			viewHolder.transactionDate = (TextView)itemLayout.findViewById(R.id.list_transaction_date);
			viewHolder.transactionPrice = (TextView)itemLayout.findViewById(R.id.list_transaction_price);
			viewHolder.transactionContent=(TextView)itemLayout.findViewById(R.id.list_transaction_content);

			itemLayout.setTag(viewHolder);
		}
		else // 재사용의 경우 뷰홀더 객체를 설정해 두었기 때문에 굳이 findViewById함수를 사용하지 않아도 원하는 뷰를 참조 가능
		{
			viewHolder=(ViewHolder)itemLayout.getTag();
		}

		viewHolder.transactionContent.setText(mData.get(position).getContent());
		viewHolder.transactionDate.setText(mData.get(position).getDate());
		viewHolder.transactionPrice.setText(mData.get(position).getPrice()+"원");
		viewHolder = (ViewHolder)itemLayout.getTag();

		return itemLayout;
	}

	//데이터 추가 기능 구현
	public void add(int index,Transaction addData)
	{
		mData.add(index,addData);
		notifyDataSetChanged();
	}

}
