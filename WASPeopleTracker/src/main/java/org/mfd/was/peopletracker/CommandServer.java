package org.mfd.was.peopletracker;

import java.io.IOException;

import org.mfd.was.core.Message;

public class CommandServer implements Runnable {

	private static final String TAG = "CommandServer";
	Client teacher;
	Command command;

	public CommandServer(Client teacher, Command command) {
		super();
		this.teacher = teacher;
		this.command = command;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Message m = teacher.communicator.readMessage();
				Utils.log(TAG, "Receieved message: " + m.getType() + " from " + teacher);
				switch (m.getType()) {
				case ATTENDANCE_START:
					command.startAttendance();
					break;
				default:
					throw new RuntimeException("Unexpected type :" + m.getType());
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		}

	}
}
