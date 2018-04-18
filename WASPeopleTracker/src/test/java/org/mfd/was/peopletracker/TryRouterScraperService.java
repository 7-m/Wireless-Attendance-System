package org.mfd.was.peopletracker;

public class TryRouterScraperService {
	public static void main(String[] args) throws InterruptedException {
		
		RouterScraper rs=RouterScraper.getScraper(0);
		Thread t=new Thread(rs);
		t.start();
		Thread.sleep(1000*2);
		
		//t.interrupt();
	}

}
