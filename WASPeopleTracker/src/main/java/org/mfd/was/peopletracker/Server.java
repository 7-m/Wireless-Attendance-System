package org.mfd.was.peopletracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private boolean stopped = false;
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

	public void start() {
		//listen for connections on a new  thread and handle them on new threads 
		//managed by an executor service.
		new Thread(() -> {
			while (!stopped) {
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
		},"PeopleTrackerServer").start();//dont hardcode shit
	}

	public void stop() {
		stopped = true;
	}

	public static Server getServer(int port,ClientHandler clientHandler) throws IOException {
		if (INSTANCE != null)
			return INSTANCE;

		INSTANCE = new Server(port, clientHandler);
		return INSTANCE;
	}

}
