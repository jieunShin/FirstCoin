/**
 * @brief 통계탭 프래그먼트
 * @details 통계탭에서 일별과 월별 거래된 총금액 정보를 리스트로 나타냅니다.
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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentStat extends Fragment {

   ListView mListView = null;
   View rootView;
   private Button day;
   private Button month;
   private TextView date;

   private HttpPost httppost;
   private HttpResponse httpresponse;
   private HttpClient httpclient;
   List<NameValuePair> params;
   boolean dayflag = false;
   boolean monthflag = false;

   TransactionAdapter transactionAdapter;
   FragmentManager fm;
   ListFragmentStat listFrg;
   ArrayList<Stat> mStat;

   public static FragmentStat newInstance() {
      FragmentStat fragment = new FragmentStat();

      return fragment;
   }

   public FragmentStat() {
      // TODO Auto-generated constructor stub
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);

      mStat = DataStat.get(getActivity()).getStat();
      fm = getActivity().getSupportFragmentManager();
      Fragment fragment = fm.findFragmentById(R.id.stat_list_view);
      // container안에 프래그먼트가 존재하지 않는다면 새로 생성
      // 이렇게 if문으로 감싸주지 않으면 여러 번 반복해서 생성되서 StatListFragment가 여러번 생성되고 곂쳐서 보이게
      // 된다.
      if (fragment == null) {
         listFrg = new ListFragmentStat();
         FragmentTransaction transaction = getChildFragmentManager()
               .beginTransaction();
         transaction.add(R.id.stat_list_view, listFrg)
               .commitAllowingStateLoss();
      }

   } // onCreate

   public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_stat, container,
            false);

      day = (Button) rootView.findViewById(R.id.stat_btn_day);
      month = (Button) rootView.findViewById(R.id.stat_btn_month);

      date = (TextView) rootView.findViewById(R.id.stat_text_date);
      
      month.setBackgroundColor(Color.parseColor("#EAEAEA"));
      day.setBackgroundColor(Color.parseColor("#f8981d"));
      
      day.setTextColor(Color.parseColor("#ffffff"));
      month.setTextColor(Color.parseColor("#000000"));
      
      date.setText("날짜(월-일)");
      
      stat(0);

      // 일별
      day.setOnClickListener(new View.OnClickListener() {

        @SuppressLint("ResourceAsColor")
      @Override
         public void onClick(View v) {
            // 버튼 색 변경
           month.setBackgroundColor(Color.parseColor("#EAEAEA"));
         day.setBackgroundColor(Color.parseColor("#f8981d"));
         
         day.setTextColor(Color.parseColor("#ffffff"));
           month.setTextColor(Color.parseColor("#000000"));
               
            // 글자 변경
            date.setText("날짜(월-일)");
          

            stat(0);
         }
      });

      month.setOnClickListener(new View.OnClickListener() {

        @SuppressLint("ResourceAsColor")
      @Override
         public void onClick(View v) {

            // 버튼색 변경
           month.setBackgroundColor(Color.parseColor("#f8981d"));
         day.setBackgroundColor(Color.parseColor("#EAEAEA"));
         
         day.setTextColor(Color.parseColor("#000000"));
            month.setTextColor(Color.parseColor("#ffffff"));
           

            // 글자 변경
            date.setText("날짜(년-월)");

            stat(1);
         }
      });

      return rootView;
      //
   }// onCreateView의 끝

   void stat(int isMonth) {

      try {
         httpclient = new DefaultHttpClient();
         if (isMonth == 1)
            httppost = new HttpPost(
                  "http://203.246.112.131/pos_statistic_month.php");
         else
            httppost = new HttpPost(
                  "http://203.246.112.131/pos_statistic_day.php");

         params = new ArrayList<NameValuePair>(1);
         params.add(new BasicNameValuePair("merchant_id", UserInfo
               .getMerchantId()));

         httppost.setEntity(new UrlEncodedFormEntity(params));
         httpresponse = httpclient.execute(httppost);
         ResponseHandler<String> responseHandler = new BasicResponseHandler();

         String response = httpclient.execute(httppost, responseHandler);
         response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

         // / 데이터를 받아옴
         // 받아온 데이터 형태 : 날짜,금액\n
         String[] data = response.split("\n");
         mStat.clear();
         Log.e("log_data_length", Integer.toString(data.length));
         for (int i = 0; i < data.length; i++) {
            Log.e("third_data", "no."+i+": " + data[i]);
            Stat trx = new Stat(data[i]);
            
            mStat.add(trx);
         }
         
         listFrg.getStatAdapter().notifyDataSetChanged();
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}