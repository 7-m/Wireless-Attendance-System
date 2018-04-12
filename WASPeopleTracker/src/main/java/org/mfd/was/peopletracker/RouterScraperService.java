package org.mfd.was.peopletracker;

import java.util.List;
import java.util.ServiceLoader;


//refer to java service loader for service implementation details
public abstract class RouterScraperService {
	private static ServiceLoader<RouterScraperService> loader = ServiceLoader.load(RouterScraperService.class);

	abstract public List<Client> getConnectedClients();

	static public RouterScraperService getScraper() {
		for (RouterScraperService rss : loader)
			return rss;
		return null;
	}

}
