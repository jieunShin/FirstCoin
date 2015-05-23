/**
 * @brief 원거리 데이터 모델
 * @details 
 */


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

public class DataLongDistance {
	private ArrayList<LongDistance> mLongDistance;
	private Context context;
	//자신의 인스턴스를 멤버로 가진다.
	private static DataLongDistance sLongDistanceData;
	//통신
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	
	private DataLongDistance(Context context){
		this.context = context;
		mLongDistance = new ArrayList<LongDistance>();
	}
	//---------------  외부에서 LongDistanceData호출할 때 사용하는 메소드   ---------------//
	public static DataLongDistance get(ContextWrapper c){
		if(sLongDistanceData == null){
			sLongDistanceData = new DataLongDistance(c.getBaseContext());
			//sLongDistanceData = new ProductData(c.getApplicationContext());
			
		}
		return sLongDistanceData;
	}
	//--------------- 외부에서 ArrayList<LongDistance>를 호출할 때 사용하는 메소드 ---------------//
	public ArrayList<LongDistance> getLongDistances(){
		return mLongDistance;
	}
	
	//---------------  서버에서 데이타 받아옴   ---------------//
	private void setLongDistanceList() {

	}

}
