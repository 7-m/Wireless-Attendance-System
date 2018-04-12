package org.mfd.was.peopletracker;

import java.net.Socket;

public interface ClientHandler {
	
	void handleClient(Socket socket);

}
