package org.mfd.was.peopletracker;

import java.sql.Date;
import java.time.Instant;

import org.mfd.was.core.Communicator;

public class Client {
	final String mac;
	final Communicator communicator;
	final long timeAdded; // time when the client connected

	public Client(String mac, Communicator comm, long timeAdded) {
		this.mac = mac;
		this.communicator = comm;
		this.timeAdded = timeAdded;
	}

	@Override
	public String toString() {

		return "[ " + mac + " , " + Date.from(Instant.ofEpochMilli(timeAdded)) +" ]";
	}

}
