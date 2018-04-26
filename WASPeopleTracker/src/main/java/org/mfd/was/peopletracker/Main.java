package org.mfd.was.peopletracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws IOException {
		//parse arguments and start attendacne manager
		Map<String, List<Boolean>> studentAttendanceDb=new HashMap<>();
		studentAttendanceDb.put("e4:46:da:18:4e:bb", new ArrayList<>());
		
		
		new AttendanceManager(52000, new Identifier() {

			@Override
			public Type identify(Client client) {
				String mac_mufaddal = "e4:46:da:18:4e:bb";
				String mac_manish="34:e9:11:bd:39:a3";
				String mac_madhu="e4:46:da:85:2a:b5";
				if (client.mac.equalsIgnoreCase(mac_mufaddal))
					return Type.TEACHER;
				if (client.mac.equalsIgnoreCase(mac_manish) || client.mac.equalsIgnoreCase(mac_madhu))
					return Type.STUDENT;
				
				throw new AssertionError();
			}
		},studentAttendanceDb).startTracking();
		
		
	}

}
