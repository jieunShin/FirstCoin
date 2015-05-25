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

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentOrderState extends Fragment {

	ListView mListView=null;
	View rootView;

	private HttpPost httppost;
	private HttpResponse httpresponse;
	private HttpClient httpclient;
	List<NameValuePair> params;

	Fragment fragment;
	FragmentManager fm;
	FragmentTransaction transaction;
	LongDistanceListFragment listFrg;
	ArrayList<LongDistance> mLongDistance;
	
	Button refresh;
	FragmentEmpty em;
	int listCount=0;

	public static FragmentOrderState newInstance(){
		FragmentOrderState fragment = new FragmentOrderState();

		return fragment;
	}
	public FragmentOrderState() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mLongDistance = DataLongDistance.get(getActivity()).getLongDistances();
		//--------------- FourthFragment생성될 때 LongDistanceListFragment도 띄우는 부분 ---------------//
		fm = getActivity().getSupportFragmentManager();
		fragment = fm.findFragmentById(R.id.state_list_view);

		//container안에 프래그먼트가 존재하지 않는다면 새로 생성
		if(fragment == null){
			listFrg = new LongDistanceListFragment();
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.add(R.id.state_list_view, listFrg).commitAllowingStateLoss();
		}

	} // onCreate


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_orderstate,container, false);
		refresh = (Button) rootView.findViewById(R.id.order_state_refresh);
		
		// 탭이 시작 되면서 부터 한번 업데이트
		updateRemoteOrder();
		
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
	         public void onClick(View v) {
	           
	        	updateRemoteOrder();
	        	((ActivityMain) getActivity()).getBalance();
	        	Toast.makeText(getActivity(), "업데이트 완료", Toast.LENGTH_SHORT).show();
	        	
	         }
	      });
		return rootView;
		//
	}//onCreateView의 끝
	
	
	void updateRemoteOrder(){
	
		try {
			
			listCount=0;
			
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			httpclient = new DefaultHttpClient();

			httppost = new HttpPost(
					"http://203.246.112.131/user_get_remote_order.php");

			params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("user_email", UserInfo.getEmail()));

			httppost.setEntity(new UrlEncodedFormEntity(params));
			httpresponse = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpclient.execute(httppost, responseHandler);
			response = new String(response.getBytes("ISO-8859-1"), "UTF-8");


			/// 데이터를 받아옴
			// 받아온 데이터 형태 : order_id , merchant_id , 상점 이름, 주문 시간, 주문 상태 , 가격 , Content
			String[] data = response.split("\n");
			
			//기존 리스트에 있던 데이터 초기화
			mLongDistance.clear();
			
			for (int i = 0; i < data.length; i++) {
				Log.e("third_data", "no."+i+": " + data[i]);
				
				// 만약 데이터가 없어서 아무것도 가져오지 않은 경우는 for문을 빠져나감
				if(data[0]==""){
					break;
				}
				
				//우선 데이터를 가져와서 생성 후에
				LongDistance trx = new LongDistance(data[i]);
				if(Integer.parseInt(trx.getOrderStatus())!=6){
					listCount++;
				mLongDistance.add(trx);
				}
				
			}
			
			listFrg.getLongDistanceAdapter().notifyDataSetChanged();
			
			if(listCount==0){
				em = new FragmentEmpty();
				transaction = getChildFragmentManager().beginTransaction();
				transaction.replace(R.id.state_list_view, em);
				transaction.commit();
				
			}

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
