package kookmin.cs.firstcoin.BP_order;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GpsInfo extends Service implements LocationListener {
	private final Context mContext;

	// ���� GPS ��� ����
	boolean isGPSEnabled = false;

	// ��Ʈ��ũ ��� ����
	boolean isNetworkEnabled = false;

	// GPS ���� ��
	boolean isGetLocation = false;

	Location location;
	double lat; // ����
	double lon; // �浵

	// GPS ���� ������Ʈ �Ÿ� 10����
	private static final long MIN_DISTANCE_UPDATES = 10;

	// GPS ���� ������Ʈ �ð� 1/1000
	private static final long MIN_TIME_UPDATES = 1000 * 60 * 1;

	protected LocationManager locationManager;

	public GpsInfo(Context context) {
		this.mContext = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
			} else {
				this.isGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES,
							MIN_DISTANCE_UPDATES, this);

					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							// ���� �浵 ����
							lat = location.getLatitude();
							lon = location.getLongitude();
						}
					}
				}

				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES,
								MIN_DISTANCE_UPDATES, this);
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lon = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	// GPS ����
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GpsInfo.this);
		}
	}

	// ������
	public double getLatitude() {
		if (location != null) {
			lat = location.getLatitude();
		}
		return lat;
	}

	// �浵��
	public double getLongitude() {
		if (location != null) {
			lon = location.getLongitude();
		}
		return lon;
	}

	public boolean isGetLocation() {
		return this.isGetLocation;
	}

	// GPS ������ �������� ������ �� ���������� ���� ����� alert â
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle("GPS �����������");
		alertDialog.setMessage("GPS ������ ���� �ʾ��� ���� �ֽ��ϴ�.\n ����â���� ���ðڽ��ϱ�?");
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onLocationChanged(Location location) {

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public void onProviderEnabled(String provider) {

	}

	public void onProviderDisabled(String provider) {

	}
}
