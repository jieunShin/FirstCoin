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
		TextView requestTime; // 주문요청 시간
		TextView namePd; // 상품 내용
		TextView pricePd; // 상품 가격
		Button State; // 접수상태
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

		// 어댑터뷰가 재사용 할 뷰를 넘겨주지 않은 경우에만 새로운 뷰 생성
		if (itemLayout == null) {
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_remoteorder_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.requestTime = (TextView) itemLayout.findViewById(R.id.list_remote_requesttime);
			viewHolder.namePd = (TextView) itemLayout.findViewById(R.id.list_remote_name);
			viewHolder.pricePd = (TextView) itemLayout.findViewById(R.id.list_remote_price);
			viewHolder.State = (Button) itemLayout.findViewById(R.id.list_remote_state);

			itemLayout.setTag(viewHolder);

		} else // 재사용의 경우 뷰홀더 객체를 설정해 두었기 때문에 굳이 findViewById함수를 사용하지 않아도 원하는 뷰를
				// 참조 가능
		{
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.requestTime.setText(mOrder.get(position).getOrderTime());
		viewHolder.namePd.setText(mOrder.get(position).getContent());
		viewHolder.pricePd.setText(mOrder.get(position).getPrice());
		viewHolder.State.setText("주문 승인");

		return itemLayout;
	}

	// 데이터 추가 기능 구현
	public void add(int index, RemoteOrder addData) {

		mOrder.add(index, addData);
		notifyDataSetChanged();
	}

}
