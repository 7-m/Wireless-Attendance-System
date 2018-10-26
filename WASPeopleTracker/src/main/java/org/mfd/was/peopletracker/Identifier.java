package org.mfd.was.peopletracker;

/**
 Isdentifies and categorizes  a client bases on mac.
 */
public interface Identifier {

	Type identify(Client client);

	enum Type {
		STUDENT, TEACHER
	}

}
