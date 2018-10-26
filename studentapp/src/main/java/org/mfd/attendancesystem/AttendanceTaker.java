package org.mfd.attendancesystem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mfd on 23/4/18.
 */

public  class AttendanceTaker extends Activity {


	private static final String TAG = "AttendanceTaker";
	private WasService.BinderImpl serviceRemote;
	private AlertDialog alertDialog;

	/*for fingerprint authentication callbacks*/
	FingerprintManager.AuthenticationCallback fingerPrintCb = new FingerprintManager.AuthenticationCallback() {


		@Override
		public void onAuthenticationError(int errorCode, CharSequence errString) {

			Log.e(TAG, errString.toString());

			serviceRemote.sendAttendanceResult(false);
			alertDialog.dismiss();
			Toast.makeText(AttendanceTaker.this, errString.toString() + " No attendance", Toast.LENGTH_LONG).show();
			AttendanceTaker.this.finish();
		}

		@Override
		public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
			//send the result
			serviceRemote.sendAttendanceResult(true);
			alertDialog.dismiss();
			Toast.makeText(AttendanceTaker.this, "Attendance granted", Toast.LENGTH_LONG).show();
			AttendanceTaker.this.finish();
		}

		@Override
		public void onAuthenticationFailed() {
			serviceRemote.sendAttendanceResult(false);
			alertDialog.dismiss();
			Toast.makeText(AttendanceTaker.this, "No Attendance for you lol!", Toast.LENGTH_LONG).show();
			AttendanceTaker.this.finish();
		}

	};

	/*for onBind() callbacks*/
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			serviceRemote = (WasService.BinderImpl) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceRemote = null;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//use this time to bind to service
		Intent serviceIntent = new Intent(this, WasService.class);
		bindService(serviceIntent, connection, BIND_AUTO_CREATE);
		Log.e(TAG, "called bindService()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.attendance_taker);

		//heres how it works, verify finger, bind to service, send result.

		//now start scanning for finger
		FingerprintManager fm = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
		Log.e(TAG, "finger me baby");
		if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    Activity#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
		fm.authenticate(null, null, 0, fingerPrintCb, null);

		//post an alert dialog
		alertDialog = new AlertDialog.Builder(this).setTitle("Scan Finger").create();
		alertDialog.show();


	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(connection);
	}
}
