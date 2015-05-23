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
		TextView storeName; // 주문 상점 이름
		TextView requestTime; // 주문요청 시간
		TextView pricePd; //상품 가격
		TextView State; // 접수상태
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

		//어댑터뷰가 재사용 할 뷰를 넘겨주지 않은 경우에만 새로운 뷰 생성
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
		else // 재사용의 경우 뷰홀더 객체를 설정해 두었기 때문에 굳이 findViewById함수를 사용하지 않아도 원하는 뷰를 참조 가능
		{
			viewHolder=(ViewHolder)itemLayout.getTag();
		}
		
		viewHolder.storeName.setText(mLongdistance.get(position).getStoreName());
		viewHolder.requestTime.setText(mLongdistance.get(position).getOrderTime());
		viewHolder.pricePd.setText(mLongdistance.get(position).getPrice()+"원");
		switch(Integer.parseInt(mLongdistance.get(position).getOrderStatus()))
		{
			case 0: // 주문 요청만 들어간 상태
				viewHolder.State.setText("승인 대기");
				break;
			case -1: // 주문 요청이 거절된 상태
				viewHolder.State.setText("주문 거절");
				break;
			case 1: // 주문 승인은 했으나 qrcode가 업데이트 되지 않은 상태
				viewHolder.State.setText("현장 결제");
				break;
			case 2: // 주문 승인까지 된 상태
				viewHolder.State.setText("결제 대기");
				break;
			case 3: // 결제 완료만 끝난 상태
				viewHolder.State.setText("결제 완료");
				break;
			case 4: // pos에서 결제 완료를 확인하고 상품 준비 중을 업데이트 한 상태
				viewHolder.State.setText("상품 준비 중");
				break;
			case 5: // 픽업 대기인 상태
				viewHolder.State.setText("준비 완료");
				break;
		}
		
		//주문 상태 버튼 클릭 리스터
		viewHolder.State.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							//결제 대기중인 상태일 때만 결제진행 다이얼로그 생성함
							if(Integer.parseInt(mLongdistance.get(position).getOrderStatus())==2)
							{
								// Adapter에서 Fragment 매니저를 쓰기 위한 설정
								// Fragment 매니저를 통해 다이얼로그 생성이 가능함.
								Intent intent = new Intent(v.getContext(), ActivityMain.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
								// 결제진행 다이얼로그 생성 
								DialogFragmentRemotePay remotepayDialog = DialogFragmentRemotePay.newInstance(position);
								remotepayDialog.show(fm,"결제");
							} //if문 끝
							

						}
						catch (Exception e) {
							e.printStackTrace();
						}

					}
				}); // 리스너 종료
		
		return itemLayout;
	}

	//데이터 추가 기능 구현
	public void add(int index,LongDistance addData)
	{
		mLongdistance.add(index,addData);
		notifyDataSetChanged();
	}
	
	

}
