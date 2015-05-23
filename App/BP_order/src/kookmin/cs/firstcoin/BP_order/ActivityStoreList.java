package kookmin.cs.firstcoin.BP_order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kookmin.cs.firstcoin.order.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class ActivityStoreList extends FragmentActivity {
	// 구글맵
	GoogleMap map;
	LocationManager manager;
	String provider = LocationManager.NETWORK_PROVIDER;
	double ltNW;
	LatLng mCurLoc;
	private Toolbar toolbar;
	int storeId; // 마커를 클릭하여 주문을 진행할 때 이용할 상점정보를 저장

	MarkerOptions marker;
	// 어떤 분류를 선택했는지를 나타내는 숫자
	String storenum = "";

	// 서버와의 통신을 위한 변수
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	// list를 위한 변수
	StoreAdapter storeAdapter;
	ListView mListView;
	ArrayList<StoreInfo> mData = null;
	StoreInfo store;

	private SlidingDrawer sd;

	// GPS 값을 읽기 위한 변수
	// 앞에 지도 activity를 포함하면 여기서 gps 설정 안해도 앞의 activity에서 위도,경도 값을 가져와도 될듯!
	GpsInfo gps = new GpsInfo(ActivityStoreList.this);
	double latitude;
	double longitude;
	TextView storename;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storelist);

		sd = (SlidingDrawer) findViewById(R.id.slidingDrawer1);

		Intent in = getIntent();
		storenum = in.getExtras().get("storeNum").toString(); // 카테고리 정보값
		// list선언, adapter설정
		mListView = (ListView) findViewById(R.id.store_list_view);
		mData = new ArrayList<StoreInfo>();
		storeAdapter = new StoreAdapter(this, mData);
		mListView.setAdapter(storeAdapter);

		// 구글맵 시작
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// 사용자의 현재 위치로 이동시키는 부분
		map.setMyLocationEnabled(true);
		// 구글맵 어댑터
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			// 마커를 클릭했을 때 뜨는 말풍선
			@Override
			public View getInfoContents(Marker arg0) {
				View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);

				double marker_x = arg0.getPosition().latitude;
				double marker_y = arg0.getPosition().longitude;

				storename = (TextView) v.findViewById(R.id.text_custominfo_storeid);
				// 위치 값을 이용하여 스토어 이름을 가져온다.

				for (int i = 0; i < storeAdapter.getCount(); i++) {
					double x = storeAdapter.getItem(i).getX();
					double y = storeAdapter.getItem(i).getY();

					if (marker_x == x && marker_y == y) {
						storeId = storeAdapter.getItem(i).getId();
						arg0.setSnippet(Integer.toString(storeId));
						storename.setText(storeAdapter.getItem(i).getName());
						break;

					}
				}

				return v;
			}
		});
		// 마커 말풍선 클릭했을 때
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker arg0) {
				int merchant_id = Integer.parseInt(arg0.getSnippet());
				String store_name = storename.getText().toString();
				Intent in = new Intent(ActivityStoreList.this, ActivityOrder.class);
				in.putExtra("merchant_id", merchant_id + "");
				in.putExtra("store_name", store_name);
				finish();
				startActivity(in);
			}
		});
		
		// 구글맵 마커 클릭했을 때
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
		
			}
		});

		// 카메라가 이동할 때 발생하는 리스너
		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub
				double lat = arg0.target.latitude;
				double log = arg0.target.longitude;
				double ltNW = map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude;
				double lgNW = map.getProjection().getVisibleRegion().latLngBounds.northeast.longitude;
				double ltSW = map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude;
				double lgSW = map.getProjection().getVisibleRegion().latLngBounds.southwest.longitude;

				connect(ltNW, lgNW, ltSW, lgSW);
				storeAdapter.notifyDataSetChanged();

				for (int i = 0; i < storeAdapter.getCount(); i++) {
					TextView storename = (TextView) findViewById(R.id.text_custominfo_storeid);
					LatLng geoPoint = new LatLng(storeAdapter.getItem(i).getX(), storeAdapter.getItem(i).getY());
					marker = new MarkerOptions();
					marker.position(geoPoint);
					map.addMarker(marker);
				}

			}
		});

		// 위치관리자
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// gps
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// gps꺼져있으면 다이얼로그 띄움
			createGpsDisabledAlert();
		} else {
			// 지도 해당 위치 업데이트
			updateLocation(manager.getLastKnownLocation(provider));
		}

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, final View view, final int position, long arg) {
				// 클릭했을 때에 대한 처리
				// 다음 activity로 넘어가기
				int merchant_id = mData.get(position).getId();
				String store_name = mData.get(position).getName();
				Intent in = new Intent(ActivityStoreList.this, ActivityOrder.class);
				in.putExtra("merchant_id", merchant_id + "");
				in.putExtra("store_name", store_name);
				finish();
				startActivity(in);
			}
		});

		sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {

			}

		});

		sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				
			}

		});

	}// onCreate

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		manager.removeUpdates(listener);

	}// onPause

	@Override
	protected void onResume() {
		super.onResume();
	}// onResume

	public void updateLocation(Location myLoc) {
		Geocoder coder = new Geocoder(getApplicationContext());
		double lat = myLoc.getLatitude();
		double log = myLoc.getLongitude();

		try {
			List<Address> xxx = coder.getFromLocation(lat, log, 5);
			Address addr = xxx.get(0);
			String fullAddress = addr.getAddressLine(addr.getMaxAddressLineIndex());

			Toast.makeText(getApplicationContext(), fullAddress, Toast.LENGTH_LONG).show();
		} catch (IOException e) {

			e.printStackTrace();
		}
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 15));

	}

	// //////////GPS 설정////////////////
	// GPS 체킹
	private void createGpsDisabledAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("GPS가 켜져있지 않습니다. GPS를 켜시겠습니까?").setCancelable(false)
				.setNegativeButton("사용안함", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						provider = LocationManager.NETWORK_PROVIDER;
						dialog.cancel();
						overridePendingTransition(0, 0);
					}
				}).setPositiveButton("켜기", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						provider = LocationManager.GPS_PROVIDER;
						showGpsOptions();
						overridePendingTransition(0, 0);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}// createGpsDisabledAlert

	// GPS 설정창
	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);
	}

	// 위치 리스너
	LocationListener listener = new LocationListener() {
		// 상태가 변할 때
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// 위치프로바이더의 하드웨어 상태가 변경된경우 업데이트한다.
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Log.i("TAG", "[-2] 범위벗어남");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i("TAG", "[-1] 일시적 불능");
				break;
			case LocationProvider.AVAILABLE:
				Log.i("TAG", "[1] 현재 위치 찾는중");
				break;
			}// end switch
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.i("MyTag", "[3] 현재 위치를 찾았음");

			updateLocation(location);
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("MyTag", "[2] 서비스 사용 가능");
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("MyTag", "[-3] 서비스 사용 불가");
		}
	};

	public void connect(double x1, double y1, double x2, double y2) {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			String url = new String("http://203.246.112.131/user_getStoreInfo_category.php?" + "category=" + storenum
					+ "&x1=" + x1 + "&x2=" + x2 + "&y1=" + y1 + "&y2=" + y2);

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				String res = EntityUtils.toString(resEntity);
				res = new String(res.getBytes("ISO-8859-1"), "UTF-8");

				final String[] str_token = res.split("\n");
				int cnt = str_token.length;

				mData.clear();
				for (int i = 0; i < cnt; i++) {
					store = new StoreInfo(str_token[i]);
					mData.add(store);
				}
			}
		} catch (Exception e) {
			Log.e("log_e", e.toString());
		}
	}
}