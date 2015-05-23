/**
 * @brief 원거리 리스트 어댑터
 * @details 접수번호, 상품이름, 상품가격, 상품수량, 접수상태를 리스트에 나타냄.
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
		TextView namePd; // 상품 내용
		TextView pricePd; // 상품 가격
		TextView requestTime; // 주문요청 시간
		TextView yes; // 접수상태
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

		// 어댑터뷰가 재사용 할 뷰를 넘겨주지 않은 경우에만 새로운 뷰 생성
		if (itemLayout == null) {
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_longdistance_request_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.requestTime = (TextView) itemLayout.findViewById(R.id.list_longdistance_requesttime);
			viewHolder.namePd = (TextView) itemLayout.findViewById(R.id.list_longdistance_name);
			viewHolder.pricePd = (TextView) itemLayout.findViewById(R.id.list_longdistance_price);
			viewHolder.yes = (TextView) itemLayout.findViewById(R.id.list_longdistance_yes);
			viewHolder.no = (TextView) itemLayout.findViewById(R.id.list_longdistance_no);

			itemLayout.setTag(viewHolder);

		} else // 재사용의 경우 뷰홀더 객체를 설정해 두었기 때문에 굳이 findViewById함수를 사용하지 않아도 원하는 뷰를
				// 참조 가능
		{
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.requestTime.setText(mLongdistance.get(position).getOrderTime());
		viewHolder.namePd.setText(mLongdistance.get(position).getName());
		viewHolder.pricePd.setText(mLongdistance.get(position).getPrice()+"원");

		// 승인버튼 누를 시
		viewHolder.yes.setOnClickListener(

		new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(v.getContext(), ActivityMain.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
					// 승인 다이얼로그 생성
					DialogFragmentAccept acceptDialog = DialogFragmentAccept.newInstance(position);
					acceptDialog.show(fm, "승인");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		// 거절버튼 누를 시
		viewHolder.no.setOnClickListener(

		new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(v.getContext(), ActivityMain.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
					// 승인 다이얼로그 생성
					DialogFragmentReject acceptDialog = DialogFragmentReject.newInstance(position);
					acceptDialog.show(fm, "거절");


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		return itemLayout;
	}

	// 데이터 추가 기능 구현
	public void add(int index, LongDistance addData) {

		mLongdistance.add(index, addData);
		notifyDataSetChanged();
	}

}