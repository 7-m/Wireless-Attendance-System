package org.mfd.attendancesystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import org.mfd.was.core.Communicator;
import org.mfd.was.core.Message;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by mfd on 18/4/18.
 */

public class WasService
		extends Service {
	private static final String       TAG       = "org.mfd.service";
	static private final String       HOST      = "192.168.43.231";
	static private final int          PORT      = 52000;
	private              Communicator comm;
	private              Thread       requestServerThread;
	// used by the make shift attendance system
	//
	private              boolean      isPresent = false;

	@Override
	public void onCreate() {
		Log.e(TAG, "Was service started");

		Toast.makeText(this, "Created Service", Toast.LENGTH_SHORT).show();
		Notification.Builder builder =
				new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Coonected to WAS");
		startForeground(44, builder.build());


		//now once the service is started establish a connection with the server and store the
		//socket
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
					} catch (IOException e) {
						throw new RuntimeException("Successfully connected to server but couldnt create Communicator");
					}
				}
			}
		}, "MakeSocketConnectionThread");
		makeConnectionThread.start();


		try {
			//wait for the connection to be made
			makeConnectionThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//start the request server on a thread

		requestServerThread = new Thread(new RequestServer(comm, new MessageHandleImpl()), "RequestServerThread");
		requestServerThread.start();
		Log.e(TAG, "Started request server");


	}


	@Override
	public void onDestroy() {

		try {
			comm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		requestServerThread.interrupt();
		Log.e(TAG, "Service destroyed, requestServerThreadStopped");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new BinderImpl();
	}

	/*Implementation to handle messages sent from the Attendance Server*/
	private class MessageHandleImpl
			implements MessageHandler {
		private static final int     NOT_ID = 0xDEADBEEF;
		private              boolean isPresent;

		@Override
		public Message handle(Message message) {
			switch (message.getType()) {


				case ATTENDANCE_GET:


					WasService.this.isPresent = false;//reset the variable

					//post a  notification for 30s to give attendance
					//clicking the notification starts an activity to give attendance
					//send result of attendance from there
					Intent intent = new Intent(WasService.this, AttendanceTaker.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					PendingIntent pendingIntent = PendingIntent.getActivity(WasService.this, NOT_ID, intent, 0);


					Notification.Builder attendaceNot = new Notification.Builder(WasService.this)
							.setContentTitle("Give me your attendance")
							.setContentText("or else attendance shortage lol!")
							.setSmallIcon(R.mipmap.ic_launcher)
							.setContentIntent(pendingIntent)
							.setAutoCancel(true);

					final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					nm.notify(NOT_ID, attendaceNot.build());
					Log.e(TAG, "Sent Notification");

					//after 30 seconds cancel notification and post results on another thread

					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(1000 * 30);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}


							nm.cancel(NOT_ID);
							Log.e(TAG, "cancelled notification");

							Message reply = new Message(Message.MessageType.ATTENDANCE_GET);
							reply.putExtra(Message.ExtraType.RESULT, WasService.this.isPresent);
							try {
								comm.sendMessage(reply);
							} catch (IOException e) {
								e.printStackTrace();
							}
							Log.e(TAG, "Posted result: " + WasService.this.isPresent + " to server");

						}
					}, "PostResultToServerThread").start();
					//no immediate reply
					return null;

				case RETRIEVE_MAC:
					String deviceMac = Utils.retrieveWNICMac();

					Message reply = new Message(Message.MessageType.RETRIEVE_MAC);
					reply.putExtra(Message.ExtraType.MAC, deviceMac);
					return reply;

				default:
					throw new RuntimeException("Illegal message request recieved");

			}


		}


	}

	public class BinderImpl
			extends Binder {
		public void sendAttendanceResult(final boolean isPresent) {
			WasService.this.isPresent = isPresent;
		}

	}
}
