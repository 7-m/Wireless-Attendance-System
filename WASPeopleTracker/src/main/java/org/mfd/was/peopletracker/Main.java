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
				String mac_mom="ac:c3:3a:87:31:99";
				if (client.mac.equalsIgnoreCase(mac_mufaddal))
					return Type.STUDENT;
				if (client.mac.equalsIgnoreCase(mac_mom))
					return Type.TEACHER;
				
				throw new AssertionError();
			}
		},studentAttendanceDb).startTracking();
		
		
	}

}
