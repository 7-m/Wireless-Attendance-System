package org.mfd.was.peopletracker;

import java.io.IOException;
import java.util.Vector;

public class StudentTracker {
	private Vector<Client> clients;

	AndroidServer androidServer;

	Thread androidServerThread;

	public StudentTracker(int port) throws IOException {
		clients = new Vector<>();

		androidServer = new AndroidServer(port, clients);
		androidServerThread = new Thread(androidServer, "AndroidServerThread");

	}

	public void startTracking() {
		androidServerThread.start();

	}

}
