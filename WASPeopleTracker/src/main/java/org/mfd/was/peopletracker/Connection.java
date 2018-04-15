package org.mfd.was.peopletracker;

import java.net.Inet4Address;

public class Connection {
	// reconsider the stuff in this class
	
	public Connection(Inet4Address ip, String mac) {
		super();
		this.ip = ip;
		this.mac = mac;
	}
	Inet4Address ip;
	String mac;// macs will be scraped and hence we stick to a string representation
	
	public Inet4Address getIp() {
		return ip;
	}
	
	public String getMac() {
		return mac;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Connection)
			return mac.equals(((Connection) obj).mac);
		else
			return false;
	}
}
