package org.binas.ws.it;

import static org.junit.Assert.assertEquals;

import org.binas.ws.BadInit_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.StationView;
import org.junit.After;
import org.junit.Test;

public class GetInfoStationIT extends BaseIT{
	
	private StationView station;
	
	@Test
	public void success() {
		
		try {
			client.testInitStation("A46_Station1", 22, 7, 6, 2);
			station = client.getInfoStation("A46_Station1");
			
			assertEquals(22, (int) station.getCoordinate().getX());
			assertEquals(7, (int) station.getCoordinate().getY());
			assertEquals(6, station.getCapacity());
			assertEquals(6, station.getAvailableBinas());
			assertEquals(0, station.getFreeDocks());
			assertEquals(0, station.getTotalGets());
			assertEquals(0, station.getTotalReturns());
			
			
		} catch (BadInit_Exception e) {
			e.printStackTrace();
		} catch (InvalidStation_Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test(expected = InvalidStation_Exception.class)
	public void invalidStationId() throws InvalidStation_Exception {
		station = client.getInfoStation("CXX_Station");
	}	
	
	@Test(expected = InvalidStation_Exception.class)
	public void nullStation() throws InvalidStation_Exception {
		station = client.getInfoStation(null);
	}
	
	@Test(expected = InvalidStation_Exception.class)
	public void emptyStation() throws InvalidStation_Exception {
		station = client.getInfoStation("");
	}
	
	@Test(expected = InvalidStation_Exception.class)
	public void spaceStation() throws InvalidStation_Exception {
		station = client.getInfoStation(" ");
	}
	
	@After
	public void tearDown() {
		client.testClear();
	}
}