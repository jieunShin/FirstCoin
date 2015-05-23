/**
 * @brief ���Ÿ� �ֹ� �����׸�Ʈ
 * @details ���Ÿ� �ֹ��� ���� �ֹ���û�� ���� ����Ʈ�� �ֹ������� ���� ����Ʈ�� ��
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
      // �ʹݿ� �ֹ� ���� �ǿ� ���� ����Ʈ�� �̸� �Ѹ�.
      transaction = getChildFragmentManager().beginTransaction();
      transaction.replace(R.id.longdistance_list_view, listFrgRe);
      transaction.commit();

      //��ư �� ����
      request.setBackgroundColor(Color.parseColor("#f8981d"));
      accept.setBackgroundColor(Color.parseColor("#EAEAEA"));
      
      request.setTextColor(Color.parseColor("#ffffff"));
      accept.setTextColor(Color.parseColor("#000000"));

      // �������� ������ �������� �Լ�
      // ���⼭ 0�� �����ʹ� ����,������ ���θ� ��ٸ��� ������
      //
      updateRequestRemoteOrder();

      // �ֹ� ��û ��
      request.setOnClickListener(

      new View.OnClickListener() {

         @Override
         public void onClick(View v) {

            listFrgRe = new LongDistanceRequestFragment();
            // ������ �ٸ� ����͸� ����Ʈ�� ���� ������ ��� �ٸ� ����͸� ����� �ش� ����͸� ����
            transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.longdistance_list_view, listFrgRe);
            transaction.commit();

            //��ư �� ����
            request.setBackgroundColor(Color.parseColor("#f8981d"));
            accept.setBackgroundColor(Color.parseColor("#EAEAEA"));

            request.setTextColor(Color.parseColor("#ffffff"));
              accept.setTextColor(Color.parseColor("#000000"));
            // �������� ������ �������� �Լ�
            // ���⼭ 0�� �����ʹ� ����,������ ���θ� ��ٸ��� ������
            //
            updateRequestRemoteOrder();
         }
      });
      // �ֹ� ���� ��
      accept.setOnClickListener(

      new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            listFrgAc = new LongDistanceAcceptFragment();
            // ������ �ٸ� ����͸� ����Ʈ�� ���� ������ ��� �ٸ� ����͸� ����� �ش� ����͸� ����
            transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.longdistance_list_view, listFrgAc);
            transaction.commit();

            // ��ư�� ����
            request.setBackgroundColor(Color.parseColor("#EAEAEA"));
            accept.setBackgroundColor(Color.parseColor("#f8981d"));
            
            request.setTextColor(Color.parseColor("#000000"));
              accept.setTextColor(Color.parseColor("#ffffff"));

            updateAcceptRemoteOrder();
         }
      });

      return rootView;
      //
   }// onCreateView�� ��

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

         // / �����͸� �޾ƿ�
         // �޾ƿ� ������ ���� : order_id,����,�ֹ��ð�,order_status,�ѱݾ�,�ֹ��� �̸��� ��ȭ��ȣ\n
         String[] data = response.split("\n");

         // ���� ����Ʈ�� �ִ� ������ �ʱ�ȭ
         mLongDistance.clear();

         for (int i = 0; i < data.length; i++) {
            Log.e("third_data", "no." + i + ": " + data[i]);

            // ���� �����Ͱ� ��� �ƹ��͵� �������� ���� ���� for���� ��������
            if (data[i] == "") {
               break;
            }

            // �켱 �����͸� �����ͼ� ���� �Ŀ�
            LongDistance trx = new LongDistance(data[i]);
            // Order_status�� ���¸� ���� �ش��ϴ� ������ ���
            // ���⼭�� 0�� �ǹ̰� �ֹ���û ����/���� ���ε� �� �����͸� �߰� ��Ŵ
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

         // / �����͸� �޾ƿ�
         // �޾ƿ� ������ ���� : order_id,����,�ֹ��ð�,order_status,�ѱݾ�,�ֹ��� �̸��� ��ȭ��ȣ\n
         String[] data = response.split("\n");

         // ���� ����Ʈ�� �ֶ� ������ �ʱ�ȭ
         mLongDistance.clear();

         for (int i = 0; i < data.length; i++) {
            Log.e("third_data", "no." + i + ": " + data[i]);

            // ���� �����Ͱ� ��� �ƹ��͵� �������� ���� ���� for���� ��������
            if (data[i] == "") {
               break;
            }

            // �켱 �����͸� �����ͼ� ���� �Ŀ�
            LongDistance trx = new LongDistance(data[i]);
            // Order_status�� ���¸� ���� �ش��ϴ� ������ ���
            // ���⼭�� 1�� �ǹ̰� �ֹ���û ���� ���ε� �� �����͸� �߰� ��Ŵ
            // -1�� �����̹Ƿ� ���� �߰���Ű�� ����
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