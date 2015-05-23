package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import kookmin.cs.firstcoin.order.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreAdapter extends BaseAdapter {

	public Context mContext = null;
	ArrayList<StoreInfo> mData = null;
	LayoutInflater mLayoutInflater = null;

	class ViewHolder {
		TextView storeName;
		TextView storeAddress;
		TextView storePhone;
	}

	public StoreAdapter(Context context, ArrayList<StoreInfo> data) {
		mContext = context;
		mData = data;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		Log.v("debug", "" + mData.size());
		return mData.size();
	}

	public StoreInfo getItem(int position) {
		return mData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View itemLayout = convertView;
		ViewHolder viewHolder = null;

		if (itemLayout == null) {
			itemLayout = mLayoutInflater.inflate(R.layout.list_view_store_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.storeName = (TextView) itemLayout.findViewById(R.id.list_store_name);
			viewHolder.storeAddress = (TextView) itemLayout.findViewById(R.id.list_store_address);
			viewHolder.storePhone = (TextView) itemLayout.findViewById(R.id.list_store_phone);

			itemLayout.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.storeName.setText(mData.get(position).getName());
		viewHolder.storeAddress.setText(mData.get(position).getAddress());
		viewHolder.storePhone.setText(mData.get(position).getPhone());

		return itemLayout;
	}

	public void add(int index, StoreInfo addData) {
		mData.add(index, addData);
		notifyDataSetChanged();
	}
}
