package org.binas.domain;

import java.io.IOException;

import javax.xml.ws.Endpoint;

import org.binas.ws.BinasPortImpl;
import org.binas.ws.BinasPortType;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

public class BinasManager {
	
	/** Web Service location to publish */
	private String wsURL;
	
	/** UDDI naming server location */
	private String uddiURL = null;
	/** Web Service name */
	private String wsName = null;
	
	/** Web Service end point */
	private Endpoint endpoint = null;
	
	/** Port implementation */
	private BinasPortImpl portImpl = new BinasPortImpl(this);

	 /** Obtain Port implementation */
	 public BinasPortType getPort() {
		 return portImpl;
	 }
	 
	 /** UDDI Naming instance for contacting UDDI server */
	 private UDDINaming uddiNaming = null;
	 
	 /** Get UDDI Naming instance for contacting UDDI server */
	 public synchronized UDDINaming getUddiNaming() throws UDDINamingException {
		 
		 if (uddiNaming == null) {
			 this.uddiNaming = new UDDINaming(uddiURL);
		 }
		 
		 return this.uddiNaming;
	 }
	 
		/** output option */
		private boolean verbose = true;

		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}
	

	// Singleton -------------------------------------------------------------

	private BinasManager() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final BinasManager INSTANCE = new BinasManager();
	}

	public static synchronized BinasManager getInstance() {
		return SingletonHolder.INSTANCE;
	}


	
	
	/** constructor with provided UDDI location, WS name, and WS URL */
	public BinasManager(String uddiURL, String wsName, String wsURL) {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		this.wsURL = wsURL;
		System.out.println(this.wsURL + "Manager");
		System.out.println(this.uddiURL + "Manager");
		System.out.println(this.wsName + "Manager");
		System.out.println(this.getUddiUrl() + "ManagerGETTER");
	}

	/** constructor with provided web service URL */
	public BinasManager(String wsName, String wsURL) {
		this.wsName = wsName;
		this.wsURL = wsURL;
	}

	/** Get Web Service UDDI publication name */
	public String getWsName() {
		return wsName;
	}
	
	public String getUddiUrl() {
		System.out.println(this.uddiURL + "GETTER");
		return uddiURL;
	}








	




	

	/* end point management */

	public void start() throws Exception {
		try {
			// publish end point
			endpoint = Endpoint.create(this.portImpl);
			if (verbose) {
				System.out.printf("Starting %s%n", wsURL);
			}
			endpoint.publish(wsURL);
		} catch (Exception e) {
			endpoint = null;
			if (verbose) {
				System.out.printf("Caught exception when starting: %s%n", e);
				e.printStackTrace();
			}
			throw e;
		}
		//publishToUDDI();
	}

	public void awaitConnections() {
		if (verbose) {
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
		}
		try {
			System.in.read();
		} catch (IOException e) {
			if (verbose) {
				System.out.printf("Caught i/o exception when awaiting requests: %s%n", e);
			}
		}
	}

	public void stop() throws Exception {
		try {
			if (endpoint != null) {
				// stop end point
				endpoint.stop();
				if (verbose) {
					System.out.printf("Stopped %s%n", wsURL);
				}
			}
		} catch (Exception e) {
			if (verbose) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
		}
		this.portImpl = null;
		//unpublishFromUDDI();
	}

	/* UDDI */

	void publishToUDDI() throws Exception {
		// TODO
	}

	void unpublishFromUDDI() {
		// TODO
	}

}
