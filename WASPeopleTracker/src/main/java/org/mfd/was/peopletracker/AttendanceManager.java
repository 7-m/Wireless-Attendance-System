package org.mfd.was.peopletracker;

import org.mfd.was.core.Message;
import org.mfd.was.core.Message.ExtraType;
import org.mfd.was.core.Message.MessageType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 Manages Android Server and CommandServer

 */
public class AttendanceManager {

	private static final String TAG = "AttendnaceManager";
	AndroidServer androidServer;
	Thread        androidServerThread;

	// true at index n represents attendance granted for period n+1.
	// map should be supplied pre initialized
	private Map<String, List<Boolean>> attendance;
	private Vector<Client>             students=new Vector<>();
	private ExecutorService            commandServerThreads;
	// exclusively used to handle commandServer threads
	private ExecutorService            attendanceThreads;

	public AttendanceManager(int port, Identifier identifier, Map<String, List<Boolean>> attendance)
			throws IOException {
		this.attendance = attendance;

		androidServer = new AndroidServer(port, students, new ClientHandlerImpl(identifier));
		androidServerThread = new Thread(androidServer, "AndroidServerThread");

		attendanceThreads = Executors.newCachedThreadPool();
		commandServerThreads = Executors.newCachedThreadPool();

	}

	public void startTracking() {
		//start listening for people
		androidServerThread.start();
		Utils.log(TAG, "AndroidServer Started");

	}

	/*to define whats to be done when a client connects*/
	class ClientHandlerImpl
			implements ClientHandler {

		public Command command = new Command() {
			final private String TAG = AttendanceManager.TAG + "-startAttendance()";

			@Override
			public void startAttendance() {
				//send every client attendance start code on seperate threads using future
				// algorithm
				// >send the commands on seperate threads to each client
				// >sleep for 30 seconds
				// >check the results
				Utils.log(TAG, "Taking attendance");
				for (Client c : students)
					attendanceThreads.submit(() -> {
						boolean result = false;
						try {
							Utils.log(TAG, "Asking " + c + " to retrieve their attendance");
							//send command
							c.communicator.sendMessage(new Message(MessageType.ATTENDANCE_GET));

							//wait for 30+5 seconds for response else mark absent
							Thread.sleep(1000 * 35);//wait it out

							//check if message available
							//if (c.communicator.dataAvailible()) {
							Message recieved = c.communicator.readMessage();
							result = (Boolean) recieved.getExtra(ExtraType.RESULT);
							Utils.log(TAG, c + " replied ");
							//}

						} catch (Exception e) {
							//better print stack trace and log it rather than halting the system
							//why jeopardize the whole system for one misbehaving mobile phone
							//causing i-o exceptions
							e.printStackTrace();

						}
						Utils.log(TAG, c + " -result: " + result);
						attendance.get(c.mac).add(result);

					});

			}
		};



		Identifier identifier;

		public ClientHandlerImpl(Identifier identifier) {
			this.identifier = identifier;

		}

		@Override
		public void handle(Client client) {
			//check if its a student that belongs to this class
			switch (identifier.identify(client)) {
				case STUDENT://add it o students list
					students.add(client);
					Utils.log(TAG, "Added " + client + " as student");
					break;
				case TEACHER://monitor its request on a thread, allow multiple teachers to connect
					commandServerThreads.submit(new CommandServer(client, command));
					Utils.log(TAG, "Added " + client + " as teacher");
					break;
				default://close socket and perhaps blacklist the clown

			}
		}

	}

}
