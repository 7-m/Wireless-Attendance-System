package org.mfd.was.peopletracker;

public class MainClass {
	
	public MainClass() {
		
		int port;
		Server server =Server.getServer(port);
		server.start();
		
		//the server is run on a seperate thread, throw in some controls to kill it
		//or perform other functions
	}

}
