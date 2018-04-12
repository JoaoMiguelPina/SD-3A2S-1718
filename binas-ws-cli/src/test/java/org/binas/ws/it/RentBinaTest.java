package org.binas.ws.it;

import java.io.IOException;
import java.util.Properties;

//import org.binas.station.ws.cli.StationClient;
//import org.binas.station.ws.it.BaseIT;
import org.binas.ws.AlreadyHasBina_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.NoBinaAvail_Exception;
import org.binas.ws.NoCredit_Exception;
import org.binas.ws.UserNotExists_Exception;
import org.binas.ws.cli.BinasClient;
import org.binas.ws.cli.BinasClientException;
import org.junit.Before;
import org.junit.Test;

public class RentBinaTest {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;
	
//	BinasClient
	protected static BinasClient client;
	
	/*
	@Before
	public void setup() throws BinasClientException, IOException {
		testProps = new Properties();
		try {
			testProps.load(RentBinaTest.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new BinasClient(uddiURL, wsName);
		} else {
			client = new BinasClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
		
	}
	
	@Test
	public void success(){
		
	}
	
	@Test(expected = AlreadyHasBina_Exception.class)
	public void AlreadyHasBinaException() {}
	
	@Test (expected = InvalidStation_Exception.class)
	public void InvalidStationException() {}
	
	@Test(expected = NoBinaAvail_Exception.class)
	public void NoBinaAvailableException() {}
	
	@Test(expected = NoCredit_Exception.class)
	public void NoCreditException() {}
	
	@Test(expected = UserNotExists_Exception.class)
	public void UserNotExistsException() {}*/
}
