/**
 * @brief ���Ÿ� ������ ��
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
//---------------  �̱��� ����   ---------------//
//���������� �������� ����ϴ� �Լ��� �����͸� �����ϰ� ������� ���
import android.content.ContextWrapper;
import android.os.StrictMode;

public class DataLongDistance {
	private ArrayList<LongDistance> mLongDistance;
	private Context context;
	//�ڽ��� �ν��Ͻ��� ����� ������.
	private static DataLongDistance sLongDistanceData;
	//���
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;
	
	private DataLongDistance(Context context){
		this.context = context;
		mLongDistance = new ArrayList<LongDistance>();
	}
	//---------------  �ܺο��� LongDistanceDataȣ���� �� ����ϴ� �޼ҵ�   ---------------//
	public static DataLongDistance get(ContextWrapper c){
		if(sLongDistanceData == null){
			sLongDistanceData = new DataLongDistance(c.getBaseContext());
			//sLongDistanceData = new ProductData(c.getApplicationContext());
			
		}
		return sLongDistanceData;
	}
	//--------------- �ܺο��� ArrayList<LongDistance>�� ȣ���� �� ����ϴ� �޼ҵ� ---------------//
	public ArrayList<LongDistance> getLongDistances(){
		return mLongDistance;
	}
	
	//---------------  �������� ����Ÿ �޾ƿ�   ---------------//
	private void setLongDistanceList() {

	}

}
