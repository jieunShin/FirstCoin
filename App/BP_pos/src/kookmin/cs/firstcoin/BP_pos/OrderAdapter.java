/**
 * @brief 주문 리스트의 어댑터
 * @details 리스트에 사용되는 어댑터
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

		//어댑터뷰가 재사용 할 뷰를 넘겨주지 않은 경우에만 새로운 뷰 생성
		if(itemLayout==null)
		{
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_product_layout,null);


			viewHolder= new ViewHolder();
			viewHolder.namePd=(TextView)itemLayout.findViewById(R.id.list_product_name);
			viewHolder.pricePd=(TextView)itemLayout.findViewById(R.id.list_product_price);
			viewHolder.numberPd=(TextView)itemLayout.findViewById(R.id.list_product_quantity);

			itemLayout.setTag(viewHolder);

		}
		else // 재사용의 경우 뷰홀더 객체를 설정해 두었기 때문에 굳이 findViewById함수를 사용하지 않아도 원하는 뷰를 참조 가능
		{
			viewHolder=(ViewHolder)itemLayout.getTag();
		}

		viewHolder.namePd.setText(mData.get(position).getName());
		viewHolder.pricePd.setText(mData.get(position).getPrice()+"원");
		viewHolder.numberPd.setText(mData.get(position).getNumber());


		return itemLayout;
	}

	//데이터 추가 기능 구현
	public void add(int index,Product addData)
	{
		
		mData.add(index,addData);
		notifyDataSetChanged();
	}
}
