package org.mfd.attendancesystem;

import android.util.Log;

import org.mfd.was.core.Communicator;
import org.mfd.was.core.Message;

import java.io.IOException;

/**
 * Created by mfd on 21/4/18.
 */

public class RequestServer implements Runnable {
	private static final String TAG = "WASRequestServer";
	Communicator communicator;
	MessageHandler messageHandler;

	public RequestServer(Communicator communicator, MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		this.communicator = communicator;

	}

	//start the request handler message loop
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Log.e(TAG,"waiting for messages");
				Message message = communicator.readMessage();
				Log.e(TAG, "recieved message : "+message.getType());

				Message reply = messageHandler.handle(message);
				if (reply != null)
					communicator.sendMessage(reply);

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
