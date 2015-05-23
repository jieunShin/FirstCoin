/**
 * @brief Gcm메시지를 받아 처리하는 클래스
 * @details 오픈소스로 제공된 클래스로, GCM(Google Cloud Message)를 받았을 때 안드로이드에서 행해지는 모든 함수를 설정
 */

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kookmin.cs.firstcoin.BP_order;

import java.util.HashMap;
import java.util.Map;

import kookmin.cs.firstcoin.order.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

				sendNotification("Deleted messages on server: " + extras.toString());

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

				sendNotification("Received: " + extras.toString());
				Log.i(TAG, "Received: " + extras.toString());
			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// push가 도착했을 때
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Map<String, String> msgMap = new HashMap<String, String>();
		String dataPair[] = msg.split(",");

		for (int i = 0; i < dataPair.length; i++) {
			String data = dataPair[i];
			String[] keyValue = data.split("=");
			keyValue[0] = keyValue[0].trim();
			msgMap.put(keyValue[0], keyValue[1]);
			Log.e("log_gcm_map", keyValue[0] + "," + keyValue[1]);
		}

		String title = msgMap.get("title");
		String message = msgMap.get("message");

		Intent intent = new Intent(GcmIntentService.this, ActivityStart.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis() / 1000, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setVibrate(new long[] { 200, 1000, 200, 1000 })	// manifest에 권한줘야함
				.setSmallIcon(R.drawable.ic_launcher_order_app_logo)
				.setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setTicker(title)
				.setContentText(message)
				.setContentIntent(contentIntent)
				.setDefaults(0)
				.setLights(Color.GREEN, 1000, 100)
				.setAutoCancel(true);

		//화면 깨우기
		PushWakeLock.acquireCpuWakeLock(this);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		PushWakeLock.releaseCpuLock();

	}
}
