package org.binas.ws;

import org.binas.domain.BinasManager;


public class BinasApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
				if (args.length < 1) {
					System.err.println("Argument(s) missing!");
					System.err.println("Usage: java " + BinasApp.class.getName() + " wsURL");
					return;
				}
				
				
				
				if (args.length == 3) {
					String wsURL = args[0];
					String uddiURL = args[1];
					String wsName = args[2];
					
					System.out.println(wsURL);
					System.out.println(uddiURL);
					System.out.println(wsName);
					BinasManager endpoint = new BinasManager(uddiURL, wsName, wsURL);
					
					try {
						endpoint.start();
						endpoint.awaitConnections();
					} finally {
						endpoint.stop();
					}
				}
				

				// Create server implementation object
				
				
				
	}

}