/**
 * @brief �ֹ� ������ ��ǰ ������ ���� ��Ƽ��Ƽ
 * @details �����κ��� �ֹ� ������ ��ǰ �����͸� �޾ƿͼ� �����մϴ�.
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
//---------------  �̱��� ����   ---------------//
//���������� �������� ����ϴ� �Լ��� �����͸� �����ϰ� ������� ���
import android.content.ContextWrapper;
import android.os.StrictMode;
import android.util.Log;

public class DataProduct {
	private ArrayList<Product> mProducts;
	private Context context;
	//�ڽ��� �ν��Ͻ��� ����� ������.
	private static DataProduct sProductData;
	//���
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
	//---------------  �ܺο��� ProductDataȣ���� �� ����ϴ� �޼ҵ�   ---------------//
	public static DataProduct get(Context c){
		if(sProductData == null){
			sProductData = new DataProduct(c.getApplicationContext());		
		}

		return sProductData;
	}
	//--------------- �ܺο��� ArrayList<Product>�� ȣ���� �� ����ϴ� �޼ҵ� ---------------//
	public ArrayList<Product> getProducts(){
		return mProducts;
	}
	
	//---------------  �������� ����Ÿ �޾ƿ�   ---------------//
	private void setProductList() {
		// TODO Auto-generated method stub
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/pos_showMenuList.php");

		Log.e("log_id", UserInfo.getMerchantId());
		// post�� ���� ���� �Է�
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");  

			/// �����͸� �޾ƿ�
			// �޾ƿ� ������ ���� : �޴��̸�,����\n
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
