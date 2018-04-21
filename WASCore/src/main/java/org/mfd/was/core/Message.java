package org.mfd.was.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6316350315218172934L;

	public enum MessageType {
		PING, PONG, ATTENDANCE_START, RETRIEVE_MAC, ATTENDANCE_RESULT
	}

	public enum ExtraType {
		MAC,RESULT
	}

	private MessageType type;
	private Map<ExtraType, Object> extra;

	public Message(MessageType type) {
		this.type = type;
		extra = new HashMap<ExtraType, Object>();

	}

	public MessageType getType() {
		return type;
	}

	public void putExtra(ExtraType key, Object value) {
		extra.put(key, value);
	}

	public Object getExtra(ExtraType key) {
		return extra.get(key);
	}
}
