package org.mfd.was.peopletracker;

import org.mfd.was.core.Communicator;

public class Client {
	final String mac;
	final Communicator communicator;
	final long timeAdded; // records the time when the client connected

	public Client(String mac, Communicator comm, long timeAdded) {
		this.mac = mac;
		this.communicator = comm;
		this.timeAdded = timeAdded;
	}

}
