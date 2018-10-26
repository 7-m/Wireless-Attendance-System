package org.mfd.attendancesystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;

/**
 * Created by mfd on 15/4/18.
 * <p>
 * <p>
 * dont forget to set bssid and ssid values
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {
	public static String TAG = "org.mfd.WBR";


	public static String WAS_SSID = "\"Y-5\""; //you need to put it in quotes
	public static String WAS_BSSID = "cc:b2:55:e1:83:33";


	@Override
	public void onReceive(Context context, Intent intent) {

		NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		Intent in = new Intent(context, WasService.class);


		Log.e(TAG, info.toString());

		if (info.isConnected()) {
			WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			Log.e(TAG, wm.getConnectionInfo().toString());

			WifiInfo wifiInfo = wm.getConnectionInfo();
			if (wifiInfo.getSSID().equalsIgnoreCase(WAS_SSID)) {
				//we have got the WAS router, start the service
				context.startService(in);
				Log.e(TAG, "Started Was service");


			}

		} else { //stop the service as we aint no more connected to it
			context.stopService(in);
			Log.e(TAG,"Stopped Was Service");
		}
	}
}
