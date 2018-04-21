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
		ois = new ObjectInputStream(socket.getInputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
	}

	synchronized public Message sendAndRecieveMessage(Message message) throws IOException, ClassNotFoundException {

		oos.writeObject(message);
		return (Message) ois.readObject();

	}

}
