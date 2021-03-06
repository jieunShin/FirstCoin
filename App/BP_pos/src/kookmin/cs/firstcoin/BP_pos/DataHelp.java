/**
 * @brief 도움말 데이터 클래스
 * @details 서버로부터 도움말 데이터를 받아오고 도움말 데이터를 저장합니다.
 */


package kookmin.cs.firstcoin.BP_pos;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;

public class DataHelp{
   int cnt;
   HttpPost httppost;
   HttpResponse httpresponse;
   HttpClient httpclient;
   String allNotifStr="";
   String[] notifStr;
   String[] notifContent;
   public DataHelp(){
      cnt = 0;
      notifContent = new String[100];
      try {

         httpclient = new DefaultHttpClient();
         StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
         httppost = new HttpPost("http://203.246.112.131/pos_help_title.php");
         ResponseHandler<String> responseHandler = new BasicResponseHandler();

         String response = httpclient.execute(httppost, responseHandler);
         response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
         final String[] str_token = response.split("\n");
         cnt = str_token.length;
         StringTokenizer st = new StringTokenizer(response,"\n");//토큰
         notifStr = new String[cnt];
         for(int i=0;i<cnt;i++){
            notifStr[i] = str_token[i];
         }
         
         
      } catch (Exception e) {
      }
      
   }
   public int getCnt(){
      return cnt;
   }
   String getNotifications(){
      return allNotifStr;
   }
   public String getNotification(int index){
      return notifStr[index];
   }
   
   String getNotifContent(int index){
      return notifContent[index];
   }
   

}