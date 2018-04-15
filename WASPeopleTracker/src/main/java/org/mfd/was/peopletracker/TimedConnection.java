package org.mfd.was.peopletracker;

import java.net.Inet4Address;

public class TimedConnection extends Connection {

	public TimedConnection(Inet4Address ip, String mac) {
		super(ip, mac);
	}

	public void setDuration(int newDuration) {
		connectionDuration = newDuration;
	}
	public int getConnectionDuration() {
		return connectionDuration;
	}

	int connectionDuration;//in minutes

}
