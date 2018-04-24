package org.mfd.was.peopletracker;

import java.io.IOException;
import java.util.Vector;

public class AndroidServerTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		Vector<Client> clients = new Vector<>();
		AndroidServer as = new AndroidServer(52000, clients);
		Thread ast=new Thread(as);
		ast.start();
		
		while(true) {
			for(Client c:clients)
				System.out.println(c);
			Thread.sleep(1000*2);
		}

	}

}
