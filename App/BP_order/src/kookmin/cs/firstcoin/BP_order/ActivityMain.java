package kookmin.cs.firstcoin.BP_order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coinplug.lib.wallet.CPUserService;
import com.coinplug.lib.wallet.CPWalletService;
import com.coinplug.lib.wallet.ICPUser;
import com.coinplug.lib.wallet.ICPWallet;
import com.coinplug.lib.wallet.listener.CPUGetUserinfoListener;
import com.coinplug.lib.wallet.listener.CPWGetBalanceListener;
import com.coinplug.lib.wallet.model.CPAccountInfo;
import com.coinplug.lib.wallet.model.CPCurrencyAmount;

public class ActivityMain extends ActionBarActivity {
   // Store데이터들
   ArrayList<Store> mStores;
   // Drawer관련 변수들
   private DrawerLayout mDrawerLayout;
   private ActionBarDrawerToggle drawerToggle;
   private ListView mDrawerList;
   private Toolbar toolbar;

   // Tab관련 변수들
   ViewPager pager;
   private String titles[] = new String[] { "상점 주문", "나의 주문 상태", "간편 결제", "주문 내역" };
   SlidingTabLayout slidingTabLayout;

   // 잔고 출력 관련
   private ICPWallet mWalletService;
   private ICPUser mUserService;
   private TextView mBtcAmount, mKrwAmount;
   DecimalFormat commas;
   String BTCstr;

   private BtcInfo btcInfo;
   public UserInfo userInfo;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      // Store데이터를 가져옴
      mStores = StoreData.get(this).getStores();
      // 코인플러그 완련
      try {
         mUserService = CPUserService.getInstance(this);
         mWalletService = CPWalletService.getInstance(this);
         btcInfo = new BtcInfo();
         btcInfo.setBtcInfo();

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      setData();
      // 드로워 기초 설정 및 액션바(툴바) 설정
      mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      mDrawerList = (ListView) findViewById(R.id.navdrawer);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbar.setTitle("곧 잔고가 출력됩니다");
      toolbar.setTitleTextColor(Color.WHITE);
      setSupportActionBar(toolbar);
      toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);

      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

         @Override
         public boolean onMenuItemClick(MenuItem arg0) {

            // TODO Auto-generated method stub
            return false;
         }
      });
      // 지갑 잔고 불러오는 함수
      getBalance();

      // 뷰페이저 설졍
      pager = (ViewPager) findViewById(R.id.viewpager);
      slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
      pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

      slidingTabLayout.setViewPager(pager);
      slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
         @Override
         public int getIndicatorColor(int position) {
            return Color.YELLOW;
         }
      });

      // 드로워 설정
      drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
      mDrawerLayout.setDrawerListener(drawerToggle);
      String[] values = new String[] { "공지사항", "도움말", "앱정보", "Coin-plug 페이지", "로그아웃", "종료" };
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
            android.R.id.text1, values);
      mDrawerList.setAdapter(adapter);
      mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
            case 0:
               startActivity(new Intent(ActivityMain.this, ActivityNotify.class));
               mDrawerLayout.closeDrawer(Gravity.START);
               break;
            case 1:
               startActivity(new Intent(ActivityMain.this, ActivityHelp.class));
               mDrawerLayout.closeDrawer(Gravity.START);
               break;
            case 2:
               startActivity(new Intent(ActivityMain.this, ActivityAppInfo.class));
               mDrawerLayout.closeDrawer(Gravity.START);
               break;
            case 3:
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coinplug.com")));
               mDrawerLayout.closeDrawer(Gravity.START);
               break;
            case 4:
               Toast.makeText(ActivityMain.this, "로그아웃 되었습니다",
                           Toast.LENGTH_SHORT).show();
               SharedPreferences pref = getSharedPreferences(
                           "auto_login", MODE_PRIVATE);
               SharedPreferences.Editor editor = pref.edit();
               editor.clear();
               editor.putString("id", UserInfo.getEmail());
               editor.commit();
               
               
               startActivity(new Intent(ActivityMain.this, ActivityLogin.class));
               mDrawerLayout.closeDrawer(Gravity.START);
               break;
            case 5:
               mDrawerLayout.closeDrawer(Gravity.START);
               finish();
               break;
            }
         }
      });

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.searchview, menu); //

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

         MenuItem searchItem = menu.findItem(R.id.menu_search);
         MenuItem reset = menu.findItem(R.id.menu_reset);
         reset.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               // TODO Auto-generated method stub
               Toast.makeText(ActivityMain.this, "잔고 업데이트", Toast.LENGTH_LONG).show();
               getBalance();
               return false;
            }
         });
         SearchView searchView = (SearchView) searchItem.getActionView();

         if (null != searchView) {

            searchView.setQueryHint("상점명을 입력하시오.");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

               @Override
               public boolean onQueryTextSubmit(String s) {
                  // 검색어 완료시

                  HttpPost httppost;
                  HttpClient httpclient;

                  List<NameValuePair> nameList;

                  String keyword = s;
                  Log.e("log_key", keyword);

                  try {

                     httpclient = new DefaultHttpClient();
                     StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
                     httppost = new HttpPost("http://203.246.112.131/user_find_store.php");

                     nameList = new ArrayList<NameValuePair>(1);
                     nameList.add(new BasicNameValuePair("keyword", keyword));

                     httppost.setEntity(new UrlEncodedFormEntity(nameList, "utf-8"));

                     ResponseHandler<String> responseHandler = new BasicResponseHandler();
                     String response = httpclient.execute(httppost, responseHandler);
                     response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                     Log.e("log_res", response);
                     String[] store_data = response.split("\n");

                     // 리스트 갱신
                     mStores.clear();

                     // 분류된 상점 데이터를 각각 저장
                     for (int i = 0; i < store_data.length; i++) {
                        Store store = new Store(store_data[i]);
                        mStores.add(store);
                     }

                     // ActivityStore 액티비티로 이동(상점 리스트를 보여줌)
                     Intent i = new Intent(ActivityMain.this, ActivityStore.class);
                     startActivity(i);

                  } catch (Exception e) {
                     // TODO Auto-generated catch block

                     e.printStackTrace();
                  }

                  return true;
               }

               @Override
               public boolean onQueryTextChange(String s) {
                  // 검색어 입력시
                  return true;
               }
            });

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            if (null != searchManager) {
               searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
            searchView.setIconifiedByDefault(true);
         }
      }

      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if (drawerToggle.onOptionsItemSelected(item)) {
         return true;
      }

      switch (item.getItemId()) {
      case android.R.id.home:
         mDrawerLayout.openDrawer(Gravity.START);
         return true;
      }

      return super.onOptionsItemSelected(item);
   }

   @Override
   protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      drawerToggle.syncState();
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      drawerToggle.onConfigurationChanged(newConfig);
   }

   public void onBackPressed() {
      // TODO Auto-generated method stub
      if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
         mDrawerLayout.closeDrawer(Gravity.LEFT);
      } else {
         super.onBackPressed();

      }
   }

   // --------------- back버튼 눌렀을 시에 ---------------//
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      // TODO Auto-generated method stub
      if (keyCode == KeyEvent.KEYCODE_BACK) {
         if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
         } else {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle("안내");
            d.setMessage("종료하시겠습니까?");
            d.setNegativeButton("아니오", new DialogInterface.OnClickListener() {

               @Override
               public void onClick(DialogInterface dialog, int which) {
                  // TODO Auto-generated method stub

               }
            });
            d.setPositiveButton("예", new DialogInterface.OnClickListener() {

               @Override
               public void onClick(DialogInterface dialog, int which) {
                  // TODO Auto-generated method stub
                  finishApp();

               }
            }).show();
         }
         return true;
      }
      return super.onKeyDown(keyCode, event);
   }

   void finishApp() {
      moveTaskToBack(true);
      finish();
      android.os.Process.killProcess(android.os.Process.myPid());
   }

   public void getBalance() {
      try {
         mWalletService.walletGetBalance(new CPWGetBalanceListener() {

            @SuppressLint("NewApi")
            @Override
            public void onSuccess(int statusCode, Header[] headers, CPCurrencyAmount currencyAmount) {
               // TODO Auto-generated method stub
               try {
                  toolbar.setTitle(currencyAmount.get(CPCurrencyAmount.BTC) + "BTC");
                  toolbar.setSubtitle("나의 지갑 잔고");
                  toolbar.setSubtitleTextColor(Color.WHITE);

               } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
               // TODO Auto-generated method stub
               Toast.makeText(ActivityMain.this, "실패" + throwable.toString() + " " + errorMsg, Toast.LENGTH_SHORT)
                     .show();
            }
         });
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public BtcInfo getBtcInfo() {
      return btcInfo;
   }

   private void setData() {

      try {
         mUserService.userGetUserinfo(new CPUGetUserinfoListener() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, CPAccountInfo userInfomation) {
               // TODO Auto-generated method stub
               userInfo.setEmail(userInfomation.getEmail());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorMsg) {
               // TODO Auto-generated method stub

            }
         });
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}