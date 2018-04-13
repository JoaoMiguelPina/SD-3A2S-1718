package org.binas.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.binas.ws.AlreadyHasBina_Exception;
import org.binas.ws.BadInit_Exception;
import org.binas.ws.CoordinatesView;
import org.binas.ws.EmailExists_Exception;
import org.binas.ws.FullStation_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.NoBinaAvail_Exception;
import org.binas.ws.NoBinaRented_Exception;
import org.binas.ws.NoCredit_Exception;
import org.binas.ws.StationView;
import org.binas.ws.UserNotExists_Exception;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class ListStationsIT extends BaseIT {

	String stationId1 = "A46_Station1";
	String stationId2 = "A46_Station2";
	String stationId3 = "A46_Station3";

	String userMail1 = "sd1@tecnico.pt";
	String userMail2 = "sd2@tecnico.pt";
	String userMail3 = "sd3@tecnico.pt";
	
	StationView sv1;
	StationView sv2;
	StationView sv3;

	@Before
	public void setup() throws BadInit_Exception, EmailExists_Exception, InvalidEmail_Exception,
			InvalidStation_Exception, UserNotExists_Exception, FullStation_Exception, NoBinaRented_Exception,
			AlreadyHasBina_Exception, NoBinaAvail_Exception, NoCredit_Exception {
		
		// STATION 1
		client.testInitStation(stationId1, 22, 7, 6, 2);
		sv1 = client.getInfoStation(stationId1);

		// STATION 2
		client.testInitStation(stationId2, 80, 20, 12, 1);
		sv2 = client.getInfoStation(stationId2);

		// STATION 3
		client.testInitStation(stationId3, 50, 50, 20, 0);
		sv3 = client.getInfoStation(stationId3);

	}

	@Test
	public void success1() {
		CoordinatesView coord = new CoordinatesView();
		coord.setX(10);
		coord.setY(10);
		
		List<StationView> stations = (List<StationView>) client.listStations(1, coord);
		
		assertEquals(1, stations.size());
		assertEquals(stations.get(0).getId(), sv1.getId());
		assertEquals(stations.get(0).getCapacity(), sv1.getCapacity());
		assertEquals(stations.get(0).getAvailableBinas(), sv1.getAvailableBinas());
		assertEquals(stations.get(0).getFreeDocks(), sv1.getFreeDocks());
	}
	
	@Test
	public void success2() {
		CoordinatesView coord = new CoordinatesView();
		coord.setX(100);
		coord.setY(20);
		
		List<StationView> stations = (List<StationView>) client.listStations(2, coord);
		
		assertEquals(2, stations.size());
		assertEquals(stations.get(0).getId(), sv2.getId());
		assertEquals(stations.get(0).getCapacity(), sv2.getCapacity());
		assertEquals(stations.get(0).getAvailableBinas(), sv2.getAvailableBinas());
		assertEquals(stations.get(0).getFreeDocks(), sv2.getFreeDocks());
		
		assertEquals(stations.get(1).getId(), sv3.getId());
		assertEquals(stations.get(1).getCapacity(), sv3.getCapacity());
		assertEquals(stations.get(1).getAvailableBinas(), sv3.getAvailableBinas());
		assertEquals(stations.get(1).getFreeDocks(), sv3.getFreeDocks());
	}
	
	@Test
	public void success3() {
		CoordinatesView coord = new CoordinatesView();
		coord.setX(50);
		coord.setY(100);
		
		List<StationView> stations = (List<StationView>) client.listStations(3, coord);
		
		assertEquals(3, stations.size());
		assertEquals(stations.get(0).getId(), sv3.getId());
		assertEquals(stations.get(0).getCapacity(), sv3.getCapacity());
		assertEquals(stations.get(0).getAvailableBinas(), sv3.getAvailableBinas());
		assertEquals(stations.get(0).getFreeDocks(), sv3.getFreeDocks());
		
		assertEquals(stations.get(1).getId(), sv2.getId());
		assertEquals(stations.get(1).getCapacity(), sv2.getCapacity());
		assertEquals(stations.get(1).getAvailableBinas(), sv2.getAvailableBinas());
		assertEquals(stations.get(1).getFreeDocks(), sv2.getFreeDocks());
		
		assertEquals(stations.get(2).getId(), sv1.getId());
		assertEquals(stations.get(2).getCapacity(), sv1.getCapacity());
		assertEquals(stations.get(2).getAvailableBinas(), sv1.getAvailableBinas());
		assertEquals(stations.get(2).getFreeDocks(), sv1.getFreeDocks());
	}
	
	@Test
	public void success4() {
		CoordinatesView coord = new CoordinatesView();
		coord.setX(20);
		coord.setY(10);
		
		List<StationView> stations = (List<StationView>) client.listStations(2, coord);
		
		assertEquals(2, stations.size());
		assertEquals(stations.get(0).getId(), sv1.getId());
		assertEquals(stations.get(0).getCapacity(), sv1.getCapacity());
		assertEquals(stations.get(0).getAvailableBinas(), sv1.getAvailableBinas());
		assertEquals(stations.get(0).getFreeDocks(), sv1.getFreeDocks());
		
		assertEquals(stations.get(1).getId(), sv3.getId());
		assertEquals(stations.get(1).getCapacity(), sv3.getCapacity());
		assertEquals(stations.get(1).getAvailableBinas(), sv3.getAvailableBinas());
		assertEquals(stations.get(1).getFreeDocks(), sv3.getFreeDocks());
	}
		
	@After
	public void tearDown() {
		client.testClear();
	}
}
