package kookmin.cs.firstcoin.BP_order;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
//---------------  싱글톤 패턴   ---------------//
//여러곳에서 공통으로 사용하는 함수나 데이터를 공유하고 싶을경우 사용
import android.content.ContextWrapper;
import android.os.StrictMode;

public class RemoteOrderData {
	private ArrayList<RemoteOrder> mOrder;
	private Context context;
	// 자신의 인스턴스를 멤버로 가진다.
	private static RemoteOrderData sRemoteOrderData;
	// 통신
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	// --------------- ---------------//
	private RemoteOrderData(Context context) {
		this.context = context;
		mOrder = new ArrayList<RemoteOrder>();
	}

	// --------------- 외부에서 LongDistanceData호출할 때 사용하는 메소드 ---------------//
	public static RemoteOrderData get(ContextWrapper c) {
		if (sRemoteOrderData == null) {
			sRemoteOrderData = new RemoteOrderData(c.getBaseContext());
			// sLongDistanceData = new ProductData(c.getApplicationContext());

		}
		return sRemoteOrderData;
	}

	// --------------- 외부에서 ArrayList<LongDistance>를 호출할 때 사용하는 메소드
	// ---------------//
	public ArrayList<RemoteOrder> getOrder() {
		return mOrder;
	}

	// --------------- 서버에서 데이타 받아옴 ---------------//
	private void setLongDistanceList() {
		// TODO Auto-generated method stub
		

	}

}
