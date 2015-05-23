/**
 * @brief ���� ��Ƽ��Ƽ
 * @details �����κ��� ���� ���� content�� �޾ƿͼ� �ͽ������ ����Ʈ�� ������ ����ݴϴ�.
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

public class DataStat {
	private ArrayList<Stat> mStat;
	private Context context;
	//�ڽ��� �ν��Ͻ��� ����� ������.
	private static DataStat sStatData;
	//���
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	
	//---------------     ---------------//
	private DataStat(Context context){
		this.context = context;
		mStat = new ArrayList<Stat>();
	}
	//---------------  �ܺο��� ProductDataȣ���� �� ����ϴ� �޼ҵ�   ---------------//
	public static DataStat get(ContextWrapper c){
		if(sStatData == null){
			sStatData = new DataStat(c.getBaseContext());
			
		}
		return sStatData;
	}
	//--------------- �ܺο��� ArrayList<Product>�� ȣ���� �� ����ϴ� �޼ҵ� ---------------//
	public ArrayList<Stat> getStat(){
		return mStat;
	}
	
	// ���⼭ ������ �޴°� �ƴ�. ThirdFragment.java�� stat(int month)���� ������ ����!!! - 2015.04.04 ����
	//---------------  �������� ����Ÿ �޾ƿ�   ---------------//
	private void setStatList() {
		// TODO Auto-generated method stub
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://203.246.112.131/pos_statistic_month.php");
///		httppost = new HttpPost("http://203.246.112.131/pos_statistic_day.php");

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
			// �޾ƿ� ������ ���� : ��¥,�ݾ�\n
			String[] data = response.split("\n");
			Log.e("log_data_length", Integer.toString(data.length));
			for(int i=0; i<data.length; i++) {
				Stat trx= new Stat(data[i]);
				mStat.add(trx);
			}

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
