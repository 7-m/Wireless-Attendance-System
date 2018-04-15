package org.mfd.was.peopletracker;

import java.util.ArrayList;
import java.util.List;

public class MockProvider extends RouterScraperService {

	@Override
	public List<Connection> getConnectedClients() {
		
		return new ArrayList<>();
	}

}
