package org.mfd.was.peopletracker;

import java.util.List;

public class RouterScraper implements ConnectionInfoQueryable, Runnable, AutoCloseable {
	private static RouterScraper INSTANCE;
	RouterScraperService rss;

	private RouterScraper() {
		rss = RouterScraperService.getScraper();
	}

	@Override
	public List<TimedConnection> getConnectionInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	static RouterScraper getScraper() {
		if (INSTANCE == null)
			INSTANCE = new RouterScraper();

		return INSTANCE;

	}

	/*
	 * implement this on a seperate thread which continuosly polls the
	 */

}
