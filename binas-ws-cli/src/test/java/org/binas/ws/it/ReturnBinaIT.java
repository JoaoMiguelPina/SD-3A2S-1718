package org.binas.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.binas.ws.AlreadyHasBina_Exception;
import org.binas.ws.BadInit_Exception;
import org.binas.ws.EmailExists_Exception;
import org.binas.ws.FullStation_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.NoBinaAvail_Exception;
import org.binas.ws.NoBinaRented_Exception;
import org.binas.ws.NoCredit_Exception;
import org.binas.ws.StationView;
import org.binas.ws.UserNotExists_Exception;
import org.binas.ws.UserView;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;

public class ReturnBinaIT extends BaseIT {

	String stationId1 = "A46_Station1";
	String stationId2 = "A46_Station2";
	String stationId3 = "A46_Station3";

	String userMail1 = "sd1@tecnico.pt";
	String userMail2 = "sd2@tecnico.pt";
	String userMail3 = "sd3@tecnico.pt";

	@Test
	public void success() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception,
			InvalidEmail_Exception, AlreadyHasBina_Exception, NoBinaAvail_Exception, NoCredit_Exception,
			UserNotExists_Exception, FullStation_Exception, NoBinaRented_Exception {

		// STATION 1
		client.testInitStation(stationId1, 22, 7, 6, 2);
		StationView sv1 = client.getInfoStation(stationId1);
		assertNotNull(sv1);

		client.activateUser(userMail1);
		client.rentBina(stationId1, userMail1);

		assertEquals(1, client.getInfoStation(sv1.getId()).getFreeDocks());
		assertEquals(1, client.getInfoStation(sv1.getId()).getTotalGets());

		int value1 = client.getCredit(userMail1);
		client.returnBina(sv1.getId(), userMail1);

		assertEquals(0, client.getInfoStation(sv1.getId()).getFreeDocks());
		assertEquals(1, client.getInfoStation(sv1.getId()).getTotalReturns());
		assertEquals(11, client.getCredit(userMail1));

		// STATION 2
		client.testInitStation(stationId2, 80, 20, 12, 1);
		StationView sv2 = client.getInfoStation(stationId2);
		assertNotNull(sv2);

		client.activateUser(userMail2);
		client.rentBina(sv2.getId(), userMail2);

		assertEquals(1, client.getInfoStation(sv2.getId()).getFreeDocks());
		assertEquals(1, client.getInfoStation(sv2.getId()).getTotalGets());

		int value2 = client.getCredit(userMail2);
		client.returnBina(sv2.getId(), userMail2);

		assertEquals(0, client.getInfoStation(sv2.getId()).getFreeDocks());
		assertEquals(1, client.getInfoStation(sv2.getId()).getTotalReturns());
		assertEquals(10, client.getCredit(userMail2));

		// STATION 3
		client.testInitStation(stationId3, 50, 50, 20, 0);
		StationView sv3 = client.getInfoStation(stationId3);
		assertNotNull(sv3);

		client.activateUser(userMail3);
		client.rentBina(sv3.getId(), userMail3);

		assertEquals(1, client.getInfoStation(sv3.getId()).getFreeDocks());
		assertEquals(1, client.getInfoStation(sv3.getId()).getTotalGets());

		int value3 = client.getCredit(userMail3);
		client.returnBina(sv3.getId(), userMail3);

		assertEquals(0, client.getInfoStation(sv3.getId()).getFreeDocks());
		assertEquals(1, client.getInfoStation(sv3.getId()).getTotalReturns());
		assertEquals(9, client.getCredit(userMail3));

	}

	@Test(expected = FullStation_Exception.class)
	public void FullStationException() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception,
			InvalidEmail_Exception, AlreadyHasBina_Exception, NoBinaAvail_Exception, NoCredit_Exception,
			UserNotExists_Exception, FullStation_Exception, NoBinaRented_Exception {

		client.testInitStation(stationId1, 22, 7, 6, 2);
		client.testInitStation(stationId2, 80, 20, 12, 1);
		StationView sv1 = client.getInfoStation(stationId1);
		StationView sv2 = client.getInfoStation(stationId2);
		assertNotNull(sv1);
		assertNotNull(sv2);

		client.activateUser(userMail1);
		client.rentBina(sv1.getId(), userMail1);
		client.returnBina(sv2.getId(), userMail1);
	}

	@Test(expected = InvalidStation_Exception.class)
	public void InvalidStationException() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception,
			InvalidEmail_Exception, AlreadyHasBina_Exception, NoBinaAvail_Exception, NoCredit_Exception,
			UserNotExists_Exception, FullStation_Exception, NoBinaRented_Exception {

		client.testInitStation(stationId2, 80, 20, 12, 1);
		StationView sv1 = client.getInfoStation(stationId2);
		assertNotNull(sv1);

		client.activateUser(userMail1);
		client.rentBina(sv1.getId(), userMail1);
		client.returnBina("CXX_Station1", userMail1);
	}

	@Test(expected = InvalidStation_Exception.class)
	public void InvalidStationExceptionEmptyStation() throws BadInit_Exception, EmailExists_Exception,
			InvalidEmail_Exception, InvalidStation_Exception, AlreadyHasBina_Exception, NoBinaAvail_Exception,
			NoCredit_Exception, UserNotExists_Exception, FullStation_Exception, NoBinaRented_Exception {
		client.testInitStation(stationId2, 80, 20, 12, 1);
		StationView sv1 = client.getInfoStation(stationId2);
		assertNotNull(sv1);

		client.activateUser(userMail1);
		client.rentBina(sv1.getId(), userMail1);
		client.returnBina("", userMail1);
	}

	@Test(expected = InvalidStation_Exception.class)
	public void InvalidStationExceptionNullStation() throws BadInit_Exception, InvalidStation_Exception,
			EmailExists_Exception, InvalidEmail_Exception, FullStation_Exception, NoBinaRented_Exception,
			UserNotExists_Exception, AlreadyHasBina_Exception, NoBinaAvail_Exception, NoCredit_Exception {
		client.testInitStation(stationId2, 80, 20, 12, 1);
		StationView sv1 = client.getInfoStation(stationId2);
		assertNotNull(sv1);

		client.activateUser(userMail1);
		client.rentBina(sv1.getId(), userMail1);
		client.returnBina(null, userMail1);
	}

	@Test(expected = NoBinaRented_Exception.class)
	public void NoBinaRentedException() throws BadInit_Exception, UserNotExists_Exception, EmailExists_Exception,
			InvalidEmail_Exception, InvalidStation_Exception, FullStation_Exception, NoBinaRented_Exception {
		client.testInitStation(stationId1, 22, 7, 6, 2);
		StationView sv1 = client.getInfoStation(stationId1);
		assertNotNull(sv1);

		client.activateUser(userMail1);

		client.returnBina(sv1.getId(), userMail1);

	}

	@Test(expected = UserNotExists_Exception.class)
	public void UserNotExistsException() throws BadInit_Exception, InvalidStation_Exception, FullStation_Exception,
			NoBinaRented_Exception, UserNotExists_Exception {
		client.testInitStation(stationId1, 22, 7, 6, 2);
		StationView sv1 = client.getInfoStation(stationId1);
		assertNotNull(sv1);

		client.returnBina(sv1.getId(), userMail1);

	}

	@After
	public void tearDown() {
		client.testClear();
	}
}
