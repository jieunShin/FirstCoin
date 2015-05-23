/**
 * @brief 주문 가능한 상품 데이터 모음 액티비티
 * @details 서버로부터 주문 가능한 상품 데이터를 받아와서 저장합니다.
 */


package kookmin.cs.firstcoin.BP_pos;

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
import android.util.Log;

public class DataProduct {
	private ArrayList<Product> mProducts;
	private Context context;
	//자신의 인스턴스를 멤버로 가진다.
	private static DataProduct sProductData;
	//통신
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	
	//---------------     ---------------//
	private DataProduct(){
		mProducts = new ArrayList<Product>();
		setProductList();	
	}
	private DataProduct(Context context){
		this.context = context;
		mProducts = new ArrayList<Product>();
		setProductList();
	}
	//---------------  외부에서 ProductData호출할 때 사용하는 메소드   ---------------//
	public static DataProduct get(Context c){
		if(sProductData == null){
			sProductData = new DataProduct(c.getApplicationContext());		
		}

		return sProductData;
	}
	//--------------- 외부에서 ArrayList<Product>를 호출할 때 사용하는 메소드 ---------------//
	public ArrayList<Product> getProducts(){
		return mProducts;
	}
	
	//---------------  서버에서 데이타 받아옴   ---------------//
	private void setProductList() {
		// TODO Auto-generated method stub
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/pos_showMenuList.php");

		Log.e("log_id", UserInfo.getMerchantId());
		// post로 보낼 정보 입력
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");  

			/// 데이터를 받아옴
			// 받아온 데이터 형태 : 메뉴이름,가격\n
			String[] data = response.split("\n");

			for(int i=0; i<data.length; i++) {
				Product menu = new Product(data[i]);
				mProducts.add(menu);
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
