package org.mfd.was.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Allows 2 way communication using messages across a socket. Follows the
 * protocol described in Message.
 * 
 * @author mfd
 *
 */
public class Communicator {
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public Communicator(Socket socket) throws IOException {
		super();
		this.socket = socket;
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());

	}

	public Message sendAndRecieveMessage(Message message) throws IOException, ClassNotFoundException {

		oos.writeObject(message);
		oos.flush();
		return (Message) ois.readObject();

	}

	public Message readMessage() throws ClassNotFoundException, IOException {
		synchronized (ois) {
			return (Message) ois.readObject();
		}
	}

	public void sendMessage(Message m) throws IOException {
		synchronized (oos) {
			oos.writeObject(m);
			oos.flush();
		}
	}

	public boolean dataAvailible() throws IOException {
		return ois.available() > 0;
	}

	public void close() throws IOException {
		socket.close();
	}

}
