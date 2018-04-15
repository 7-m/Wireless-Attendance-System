package org.mfd.was.peopletracker;

import java.util.ArrayList;
import java.util.List;

public class RouterScraper implements ConnectedClientsQueryable, Runnable, AutoCloseable {
	private final int DELAY_IN_SECONDS;
	private static RouterScraper INSTANCE;
	RouterScraperService rss;
	List<TimedConnection> connectedClients;

	private RouterScraper(int scraperDelay) {
		DELAY_IN_SECONDS = scraperDelay;
		rss = RouterScraperService.getScraper();
		connectedClients = new ArrayList<>();
	}

	@Override
	public List<TimedConnection> getConnectedClients() {
		return connectedClients;
	}

	@Override
	synchronized public void run() {
		while (!Thread.currentThread().isInterrupted()) {

			/*
			 * business logic:
			 * scrape the router every DELAY seconds
			 * increment the timed connections as required
			 * 
			 */

			try {

				List<Connection> currentlyConnectedClients = rss.getConnectedClients();
				//
				List<TimedConnection> updatedList = new ArrayList<>();
				for (Connection c : currentlyConnectedClients) {
					TimedConnection e = new TimedConnection(c.getIp(), c.getMac());
					if (connectedClients.contains(e)) {
						//increment the duration of existing client
						//TODO:make this code readable 
						e.setDuration(connectedClients.get(connectedClients.indexOf(e)).getConnectionDuration()
								+ DELAY_IN_SECONDS);
					}
					updatedList.add(e);
				}
				//update the list
				connectedClients = updatedList;

				Thread.sleep(DELAY_IN_SECONDS * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	synchronized public void close() {
		// TODO Auto-generated method stub

	}

	static RouterScraper getScraper(int scraperDelay) {
		if (INSTANCE == null)
			INSTANCE = new RouterScraper(scraperDelay);

		return INSTANCE;

	}

}
