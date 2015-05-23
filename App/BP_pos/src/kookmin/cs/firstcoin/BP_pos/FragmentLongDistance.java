/**
 * @brief 원거리 주문 프래그먼트
 * @details 원거리 주문에 대한 주문요청에 대한 리스트와 주문접수에 대한 리스트를 서
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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentLongDistance extends Fragment {

   ListView mListView = null;
   View rootView;
   private Button request;
   private Button accept;

   private HttpPost httppost;
   private HttpResponse httpresponse;
   private HttpClient httpclient;
   List<NameValuePair> params;

   Fragment fragment;
   FragmentManager fm;
   FragmentTransaction transaction;
   LongDistanceAcceptFragment listFrgAc;
   LongDistanceRequestFragment listFrgRe;
   ArrayList<LongDistance> mLongDistance;

   FragmentEmpty em;
   int listCount = 0;

   public static FragmentLongDistance newInstance() {
      FragmentLongDistance fragment = new FragmentLongDistance();

      return fragment;
   }

   public FragmentLongDistance() {
      // TODO Auto-generated constructor stub
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);

      mLongDistance = DataLongDistance.get(getActivity()).getLongDistances();
      fm = getActivity().getSupportFragmentManager();
      fragment = fm.findFragmentById(R.id.longdistance_list_view);
      
   } // onCreate

   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_longdistance, container, false);

      request = (Button) rootView.findViewById(R.id.longdistance_btn_request);
      accept = (Button) rootView.findViewById(R.id.longdistance_btn_accept);

      listFrgRe = new LongDistanceRequestFragment();
      // 초반에 주문 승인 건에 대한 리스트를 미리 뿌림.
      transaction = getChildFragmentManager().beginTransaction();
      transaction.replace(R.id.longdistance_list_view, listFrgRe);
      transaction.commit();

      //버튼 색 변경
      request.setBackgroundColor(Color.parseColor("#f8981d"));
      accept.setBackgroundColor(Color.parseColor("#EAEAEA"));
      
      request.setTextColor(Color.parseColor("#ffffff"));
      accept.setTextColor(Color.parseColor("#000000"));

      // 서버에서 데이터 가져오는 함수
      // 여기서 0인 데이터는 승인,거절의 여부를 기다리는 데이터
      //
      updateRequestRemoteOrder();

      // 주문 요청 건
      request.setOnClickListener(

      new View.OnClickListener() {

         @Override
         public void onClick(View v) {

            listFrgRe = new LongDistanceRequestFragment();
            // 이전에 다른 어댑터를 리스트에 먼저 설정한 경우 다른 어댑터를 지우고 해당 어댑터를 설정
            transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.longdistance_list_view, listFrgRe);
            transaction.commit();

            //버튼 색 변경
            request.setBackgroundColor(Color.parseColor("#f8981d"));
            accept.setBackgroundColor(Color.parseColor("#EAEAEA"));

            request.setTextColor(Color.parseColor("#ffffff"));
              accept.setTextColor(Color.parseColor("#000000"));
            // 서버에서 데이터 가져오는 함수
            // 여기서 0인 데이터는 승인,거절의 여부를 기다리는 데이터
            //
            updateRequestRemoteOrder();
         }
      });
      // 주문 접수 건
      accept.setOnClickListener(

      new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            listFrgAc = new LongDistanceAcceptFragment();
            // 이전에 다른 어댑터를 리스트에 먼저 설정한 경우 다른 어댑터를 지우고 해당 어댑터를 설정
            transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.longdistance_list_view, listFrgAc);
            transaction.commit();

            // 버튼색 변경
            request.setBackgroundColor(Color.parseColor("#EAEAEA"));
            accept.setBackgroundColor(Color.parseColor("#f8981d"));
            
            request.setTextColor(Color.parseColor("#000000"));
              accept.setTextColor(Color.parseColor("#ffffff"));

            updateAcceptRemoteOrder();
         }
      });

      return rootView;
      //
   }// onCreateView의 끝

   void updateRequestRemoteOrder() {

      try {
         listCount = 0;
         httpclient = new DefaultHttpClient();
         httppost = new HttpPost("http://203.246.112.131/pos_get_remote_order.php");

         params = new ArrayList<NameValuePair>(1);
         params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));

         httppost.setEntity(new UrlEncodedFormEntity(params));
         httpresponse = httpclient.execute(httppost);
         ResponseHandler<String> responseHandler = new BasicResponseHandler();

         String response = httpclient.execute(httppost, responseHandler);
         response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

         // / 데이터를 받아옴
         // 받아온 데이터 형태 : order_id,메일,주문시간,order_status,총금액,주문자 이름과 전화번호\n
         String[] data = response.split("\n");

         // 기존 리스트에 있던 데이터 초기화
         mLongDistance.clear();

         for (int i = 0; i < data.length; i++) {
            Log.e("third_data", "no." + i + ": " + data[i]);

            // 만약 데이터가 없어서 아무것도 가져오지 않은 경우는 for문을 빠져나감
            if (data[i] == "") {
               break;
            }

            // 우선 데이터를 가져와서 생성 후에
            LongDistance trx = new LongDistance(data[i]);
            // Order_status의 상태를 보고 해당하는 내역을 골라냄
            // 여기서는 0의 의미가 주문요청 승인/거절 전인데 이 데이터만 추가 시킴
            if (Integer.parseInt(trx.getOrderStatus()) == 0) {
               listCount++;
               mLongDistance.add(trx);
            }
    
         }

         if (listCount == 0) {
            em = new FragmentEmpty();
            transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.longdistance_list_view, em);
            transaction.commit();
         }

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   void updateAcceptRemoteOrder() {

      try {
         listCount = 0;
         httpclient = new DefaultHttpClient();
         httppost = new HttpPost("http://203.246.112.131/pos_get_remote_order.php");

         params = new ArrayList<NameValuePair>(1);
         params.add(new BasicNameValuePair("merchant_id", UserInfo.getMerchantId()));

         httppost.setEntity(new UrlEncodedFormEntity(params));
         httpresponse = httpclient.execute(httppost);
         ResponseHandler<String> responseHandler = new BasicResponseHandler();

         String response = httpclient.execute(httppost, responseHandler);
         response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

         // / 데이터를 받아옴
         // 받아온 데이터 형태 : order_id,메일,주문시간,order_status,총금액,주문자 이름과 전화번호\n
         String[] data = response.split("\n");

         // 기존 리스트에 있떤 데이터 초기화
         mLongDistance.clear();

         for (int i = 0; i < data.length; i++) {
            Log.e("third_data", "no." + i + ": " + data[i]);

            // 만약 데이터가 없어서 아무것도 가져오지 않은 경우는 for문을 빠져나감
            if (data[i] == "") {
               break;
            }

            // 우선 데이터를 가져와서 생성 후에
            LongDistance trx = new LongDistance(data[i]);
            // Order_status의 상태를 보고 해당하는 내역을 골라냄
            // 여기서는 1의 의미가 주문요청 승인 후인데 이 데이터만 추가 시킴
            // -1은 거절이므로 따로 추가시키지 않음
            if (Integer.parseInt(trx.getOrderStatus()) != 0 && Integer.parseInt(trx.getOrderStatus()) != 6) {
               listCount++;
               mLongDistance.add(trx);
            }

         }

         if (listCount == 0) {
            em = new FragmentEmpty();
            transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.longdistance_list_view, em);
            transaction.commit();

         }

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}