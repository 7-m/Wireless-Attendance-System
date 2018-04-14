package org.mfd.was.peopletracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable, AutoCloseable {

	ServerSocket server;
	private static Server INSTANCE;
	ExecutorService executorService;
	ClientHandler clientHandler;

	private Server() {
	}

	private Server(int port, ClientHandler clientHandler) throws IOException {
		server = new ServerSocket(port);
		server.setSoTimeout(500);
		this.clientHandler = clientHandler;
		executorService = Executors.newCachedThreadPool();
	}

	@Override
	synchronized public void run() {
		//listen for connections and handle them on new threads 
		//managed by an executor service.

		while (!Thread.interrupted()) {
			try {
				Socket sock = server.accept();

				//handle the client on another thread
				executorService.execute(() -> clientHandler.handleClient(sock));

			} catch (SocketTimeoutException to) {
				//do nothing
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	synchronized public void close() {
		if (INSTANCE == null) // make it idempotent
			return;
		executorService.shutdown();
		try {
			server.close();
		} catch (IOException e) {
			// this exception shouldnt occur cause the run() and dispose() are synced and hence no socket exception should occur
			e.printStackTrace();
		}
		INSTANCE = null;

	}

	public static Server getServer(int port, ClientHandler clientHandler) throws IOException {
		if (INSTANCE == null)
			INSTANCE = new Server(port, clientHandler);

		return INSTANCE;
	}

}
