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
				String wsURL = args[0];
				String uddiURL = args[1];
				String wsName = args[2];

				// Create server implementation object
				BinasManager endpoint = new BinasManager(wsURL,uddiURL,wsName);
				try {
					endpoint.start();
					endpoint.awaitConnections();
				} finally {
					endpoint.stop();
				}
	}

}