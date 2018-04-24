package org.mfd.was.peopletracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;

import org.mfd.was.core.Communicator;
import org.mfd.was.core.Message;
import org.mfd.was.core.Message.ExtraType;
import org.mfd.was.core.Message.MessageType;

/**
 * 
 * 
 */

public class AndroidServer implements Runnable, Closeable {

	private static final String TAG = "AndroidServer";
	ServerSocket server;
	ClientHandler clientHandler;

	static private String retrieveMacAddress(Communicator comm) throws ClassNotFoundException, IOException {
		Message macMessage = new Message(MessageType.RETRIEVE_MAC);

		Message reply = comm.sendAndRecieveMessage(macMessage);
		return (String) reply.getExtra(ExtraType.MAC);
	}

	public AndroidServer(int port, Vector<Client> clients, ClientHandler clientHandler) throws IOException {
		server = new ServerSocket(port);
		server.setSoTimeout(500);
		this.clientHandler = clientHandler;
	}

	private void makeClient(Socket sock) {

		//, run it on a different thread or it might cause issues
		new Thread(() -> {
			try {
				Communicator comm = new Communicator(sock);
				Client client = new Client(retrieveMacAddress(comm), comm, System.currentTimeMillis());
				Utils.Log(TAG, "Created client "+client);

				clientHandler.handle(client);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}, "AddClientThread").start();

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

				//construct the client and handle it			
				makeClient(sock);

			} catch (SocketTimeoutException to) {
				//do nothing
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
