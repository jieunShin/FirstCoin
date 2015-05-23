package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;

import android.content.Context;

public class StoreData {
	private ArrayList<Store> mStores;
	private Context context;
	private static StoreData sStoreData;

	private StoreData(Context context) {
		this.context = context;
		mStores = new ArrayList<Store>();
	}

	// --------------- 외부에서 StoreData호출할 때 사용하는 메소드 ---------------//
	public static StoreData get(Context c) {
		if (sStoreData == null) {
			sStoreData = new StoreData(c.getApplicationContext());
		}
		return sStoreData;
	}

	// --------------- 외부에서 ArrayList<Store>를 호출할 때 사용하는 메소드
	// ---------------//
	public ArrayList<Store> getStores() {
		return mStores;
	}

}
