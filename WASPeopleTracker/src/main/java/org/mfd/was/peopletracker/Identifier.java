package org.mfd.was.peopletracker;

public interface Identifier {
	//identifies and categorizes  a client bases on mac
	enum Type {
		STUDENT, TEACHER
	}

	Type identify(Client client);

}
