package org.binas.ws;

import org.binas.domain.BinasManager;

public class BinasApp {

	public static void main(String[] args) throws Exception {

		BinasManager binasManager = BinasManager.getInstance();
		// Check arguments

		if (args.length == 3) {
			String wsURL = args[0];
			String uddiURL = args[1];
			String wsName = args[2];

			BinasManager.getInstance().setBinas(uddiURL, wsName, wsURL);
		}

		else {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + BinasApp.class.getName() + " wsURL");
			return;
		}

		try {
			binasManager.start();
			binasManager.awaitConnections();
		} finally {
			binasManager.stop();
		}
	}

	// Create server implementation object

}