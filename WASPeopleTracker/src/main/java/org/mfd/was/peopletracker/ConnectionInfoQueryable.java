package org.mfd.was.peopletracker;

import java.util.List;

public interface ConnectionInfoQueryable {
	//use a class which also has info about the connection duration
	List<TimedConnection> getConnectionInfo();

}
