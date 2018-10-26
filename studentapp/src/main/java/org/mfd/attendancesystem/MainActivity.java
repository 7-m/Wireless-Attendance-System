package org.mfd.attendancesystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "org.mfd.as.MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);


	}

	@Override
	protected void onStart() {
		super.onStart();


	}

	public void displayMac(View v) {
		TextView tv=(TextView) findViewById(R.id.macDisplay);
		tv.setText(Utils.retrieveWNICMac());
	}

}
