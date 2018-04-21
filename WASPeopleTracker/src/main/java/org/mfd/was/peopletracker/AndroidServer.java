package org.mfd.was.peopletracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;

import org.mfd.was.core.Communicator;

/**
 * 
 * 
 */

public class AndroidServer implements Runnable, Closeable {

	Vector<Client> clients;
	ServerSocket server;

	static private String retrieveMacAddress(Socket sock) {
		//TODO:send a message to fetch the mac address and assign it
		return null;
	}

	public AndroidServer(int port, Vector<Client> clients) throws IOException {
		server = new ServerSocket(port);
		server.setSoTimeout(500);
		this.clients = clients;
	}

	private void addClient(Socket sock) {

		String mac = retrieveMacAddress(sock);
		try {
			clients.add(new Client(mac, new Communicator(sock), System.currentTimeMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	synchronized public void close() {

		try {
			server.close();
		} catch (IOException e) {
			// this exception shouldnt occur cause the run() and close() are synced and hence no concurrent 
			// blocking and closing of serversocket should occur
			e.printStackTrace();
		}

	}

	@Override
	synchronized public void run() {

		while (!Thread.interrupted()) {
			try {
				Socket sock = server.accept();

				//add the client to connected clients list				
				addClient(sock);

			} catch (SocketTimeoutException to) {
				//do nothing
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
