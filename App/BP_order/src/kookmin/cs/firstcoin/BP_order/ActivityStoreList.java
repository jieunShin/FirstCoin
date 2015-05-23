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
	// ���۸�
	GoogleMap map;
	LocationManager manager;
	String provider = LocationManager.NETWORK_PROVIDER;
	double ltNW;
	LatLng mCurLoc;
	private Toolbar toolbar;
	int storeId; // ��Ŀ�� Ŭ���Ͽ� �ֹ��� ������ �� �̿��� ���������� ����

	MarkerOptions marker;
	// � �з��� �����ߴ����� ��Ÿ���� ����
	String storenum = "";

	// �������� ����� ���� ����
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpClient httpclient;
	List<NameValuePair> params;

	// list�� ���� ����
	StoreAdapter storeAdapter;
	ListView mListView;
	ArrayList<StoreInfo> mData = null;
	StoreInfo store;

	private SlidingDrawer sd;

	// GPS ���� �б� ���� ����
	// �տ� ���� activity�� �����ϸ� ���⼭ gps ���� ���ص� ���� activity���� ����,�浵 ���� �����͵� �ɵ�!
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
		storenum = in.getExtras().get("storeNum").toString(); // ī�װ� ������
		// list����, adapter����
		mListView = (ListView) findViewById(R.id.store_list_view);
		mData = new ArrayList<StoreInfo>();
		storeAdapter = new StoreAdapter(this, mData);
		mListView.setAdapter(storeAdapter);

		// ���۸� ����
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// ������� ���� ��ġ�� �̵���Ű�� �κ�
		map.setMyLocationEnabled(true);
		// ���۸� �����
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			// ��Ŀ�� Ŭ������ �� �ߴ� ��ǳ��
			@Override
			public View getInfoContents(Marker arg0) {
				View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);

				double marker_x = arg0.getPosition().latitude;
				double marker_y = arg0.getPosition().longitude;

				storename = (TextView) v.findViewById(R.id.text_custominfo_storeid);
				// ��ġ ���� �̿��Ͽ� ����� �̸��� �����´�.

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
		// ��Ŀ ��ǳ�� Ŭ������ ��
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
		
		// ���۸� ��Ŀ Ŭ������ ��
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
		
			}
		});

		// ī�޶� �̵��� �� �߻��ϴ� ������
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

		// ��ġ������
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// gps
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// gps���������� ���̾�α� ���
			createGpsDisabledAlert();
		} else {
			// ���� �ش� ��ġ ������Ʈ
			updateLocation(manager.getLastKnownLocation(provider));
		}

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, final View view, final int position, long arg) {
				// Ŭ������ ���� ���� ó��
				// ���� activity�� �Ѿ��
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

	// //////////GPS ����////////////////
	// GPS üŷ
	private void createGpsDisabledAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("GPS�� �������� �ʽ��ϴ�. GPS�� �ѽðڽ��ϱ�?").setCancelable(false)
				.setNegativeButton("������", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						provider = LocationManager.NETWORK_PROVIDER;
						dialog.cancel();
						overridePendingTransition(0, 0);
					}
				}).setPositiveButton("�ѱ�", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						provider = LocationManager.GPS_PROVIDER;
						showGpsOptions();
						overridePendingTransition(0, 0);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}// createGpsDisabledAlert

	// GPS ����â
	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);
	}

	// ��ġ ������
	LocationListener listener = new LocationListener() {
		// ���°� ���� ��
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// ��ġ���ι��̴��� �ϵ���� ���°� ����Ȱ�� ������Ʈ�Ѵ�.
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Log.i("TAG", "[-2] �������");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i("TAG", "[-1] �Ͻ��� �Ҵ�");
				break;
			case LocationProvider.AVAILABLE:
				Log.i("TAG", "[1] ���� ��ġ ã����");
				break;
			}// end switch
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.i("MyTag", "[3] ���� ��ġ�� ã����");

			updateLocation(location);
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("MyTag", "[2] ���� ��� ����");
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("MyTag", "[-3] ���� ��� �Ұ�");
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