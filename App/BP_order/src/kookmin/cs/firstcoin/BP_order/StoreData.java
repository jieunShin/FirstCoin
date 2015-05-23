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

	// --------------- �ܺο��� StoreDataȣ���� �� ����ϴ� �޼ҵ� ---------------//
	public static StoreData get(Context c) {
		if (sStoreData == null) {
			sStoreData = new StoreData(c.getApplicationContext());
		}
		return sStoreData;
	}

	// --------------- �ܺο��� ArrayList<Store>�� ȣ���� �� ����ϴ� �޼ҵ�
	// ---------------//
	public ArrayList<Store> getStores() {
		return mStores;
	}

}
