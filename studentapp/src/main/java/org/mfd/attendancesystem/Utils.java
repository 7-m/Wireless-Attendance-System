package org.mfd.attendancesystem;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by mfd on 22/4/18.
 */

public class Utils {
	static public String retrieveWNICMac() {

		try {
			for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
				NetworkInterface ni = nis.nextElement();
				if (ni.getDisplayName().equalsIgnoreCase("wlan0"))
					return strigifyMac(ni.getHardwareAddress());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("No wlan hardware found!");

	}

	//the mac address returned by this is in the correct order
	static public String strigifyMac(byte[] hardwareAddress) {
		if (hardwareAddress == null)
			throw new RuntimeException("No mac address associated with hardware!");

		StringBuilder sb = new StringBuilder();

		for (byte b : hardwareAddress)
			sb.append(Integer.toHexString(b & 0xff)).append(':'); //0xff to mask the sign extension after casting

		sb.deleteCharAt(sb.length() - 1); //delete the last ':'
		return sb.toString();

	}
}
