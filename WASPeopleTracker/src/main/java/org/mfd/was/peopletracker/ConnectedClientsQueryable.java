package org.mfd.was.peopletracker;

import java.util.List;

public interface ConnectedClientsQueryable {
	//use a class which also has info about the connection duration
	List<TimedConnection> getConnectedClients();

}
