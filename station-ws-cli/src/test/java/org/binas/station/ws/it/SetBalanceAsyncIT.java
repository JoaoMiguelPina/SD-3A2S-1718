package org.binas.station.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.Response;

import org.binas.station.ws.BadInit_Exception;
import org.binas.station.ws.BalanceView;
import org.binas.station.ws.SetBalanceResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SetBalanceAsyncIT extends BaseIT{
	private final static int X = 5;
	private final static int Y = 5;
	private final static int CAPACITY = 20;
	private final static int RETURN_PRIZE = 0;
	private final static int tag = 1;
	private final static int value = 2;
	private final static String email = "email@valido";
	
	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadInit_Exception {
		client.testClear();
		client.testInit(X, Y, CAPACITY, RETURN_PRIZE);

	}

	@After
	public void tearDown() {
	}

	// main tests

	@Test
	public void success() throws InterruptedException, ExecutionException{
		
		Response<SetBalanceResponse> response = client.setBalanceAsync(email, value, tag);	
		
		BalanceView bv = null;
		while(!response.isDone()) {
			bv = client.getBalance(email);
		}
		
		int tagOut = bv.getTag();
		int valueOut = bv.getValue();
		assertEquals(tag, tagOut);
		assertEquals(2, valueOut);
	}


}
