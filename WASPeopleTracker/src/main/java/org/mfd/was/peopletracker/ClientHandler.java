package org.mfd.was.peopletracker;

/**
 * Defines whats to be done when a client connects. Later extend this to include
 * things to do when a client disconnects.
 */
public interface ClientHandler {
	void handle(Client client);

}
