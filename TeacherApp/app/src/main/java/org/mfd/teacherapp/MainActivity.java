package org.mfd.teacherapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.mfd.was.core.Communicator;
import org.mfd.was.core.Message;

import java.io.IOException;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
	private static final String HOST = "192.168.0.199";
	private static final int PORT = 52000;
	private static final String TAG = "WASMainActivity";
	Communicator comm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//create a socket and
		Thread makeConnectionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				//the delay gives time for the h/w to warm up and prevents
				// java.net.SocketException: Software caused connection abort
				//errors
				Socket sock = null;
				for (int tries = 0; sock == null && tries < 3; tries++) {
					try {
						//dont bash the system, wait it out
						Thread.sleep(1000 * 5);

						sock = new Socket(HOST, PORT);

					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (sock == null)
					throw new RuntimeException("Couldnt connect to server");
				else {
					Log.e(TAG, "Connected successfully");
					try {
						comm = new Communicator(sock);
						comm.readMessage(); //server will ask for mac and thats the only reuquest itll send
						Message reply = new Message(Message.MessageType.RETRIEVE_MAC);
						reply.putExtra(Message.ExtraType.MAC, Utils.retrieveWlanMac());
						comm.sendMessage(reply);
					} catch (IOException | ClassNotFoundException e) {
						throw new RuntimeException("Successfully connected to server but couldnt create Communicator");
					}
				}
			}
		}, "MakeSocketConnectionThread");
		makeConnectionThread.start();
	}

	public void getMac(View v) {
		TextView tv = findViewById(R.id.macDisplay);
		tv.setText(Utils.retrieveWlanMac());
	}

	public void sendAttendanceMessage(View v) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message m = new Message(Message.MessageType.ATTENDANCE_START);
				try {
					comm.sendMessage(m);
				} catch (IOException e) {
					e.printStackTrace();
				}0

			}
		}).start();

	}
}
