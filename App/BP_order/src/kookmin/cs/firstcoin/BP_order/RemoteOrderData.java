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

public class RemoteOrderData {
	private ArrayList<RemoteOrder> mOrder;
	private Context context;
	// �ڽ��� �ν��Ͻ��� ����� ������.
	private static RemoteOrderData sRemoteOrderData;
	// ���
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	// --------------- ---------------//
	private RemoteOrderData(Context context) {
		this.context = context;
		mOrder = new ArrayList<RemoteOrder>();
	}

	// --------------- �ܺο��� LongDistanceDataȣ���� �� ����ϴ� �޼ҵ� ---------------//
	public static RemoteOrderData get(ContextWrapper c) {
		if (sRemoteOrderData == null) {
			sRemoteOrderData = new RemoteOrderData(c.getBaseContext());
			// sLongDistanceData = new ProductData(c.getApplicationContext());

		}
		return sRemoteOrderData;
	}

	// --------------- �ܺο��� ArrayList<LongDistance>�� ȣ���� �� ����ϴ� �޼ҵ�
	// ---------------//
	public ArrayList<RemoteOrder> getOrder() {
		return mOrder;
	}

	// --------------- �������� ����Ÿ �޾ƿ� ---------------//
	private void setLongDistanceList() {
		// TODO Auto-generated method stub
		

	}

}
