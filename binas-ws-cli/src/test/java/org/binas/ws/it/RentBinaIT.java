package org.binas.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Properties;

import org.binas.ws.it.BaseIT;
import org.binas.ws.AlreadyHasBina_Exception;
import org.binas.ws.BadInit_Exception;
import org.binas.ws.EmailExists_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.NoBinaAvail_Exception;
import org.binas.ws.NoCredit_Exception;
import org.binas.ws.StationView;
import org.binas.ws.UserNotExists_Exception;
import org.binas.ws.cli.BinasClient;
import org.junit.After;
import org.binas.ws.cli.BinasClientException;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class RentBinaIT extends BaseIT{

	String stationId1 = "A46_Station1";
	String stationId2 = "A46_Station2";
	String stationId3 = "A46_Station3";
	
	String userMail = "binas@tecnico";
	String userMail2 = "gira@binas";
	String userMail3 = "drivenow@istoeumacopia";
	
	int valor;
	
	@Test
	public void success() throws EmailExists_Exception, InvalidEmail_Exception, AlreadyHasBina_Exception, InvalidStation_Exception, NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception, BadInit_Exception {
			
			int bonus1 = 1;
			client.activateUser(userMail);
			client.testInitStation(stationId1, 10, 10, 2, bonus1);
			client.rentBina(stationId1, userMail);
	
			
			StationView sv = client.getInfoStation(stationId1);
			
			
			assertEquals(1, sv.getAvailableBinas());
			assertEquals(1, sv.getFreeDocks());
			assertEquals(9, client.getCredit(userMail));

	}
	
	@Test(expected = AlreadyHasBina_Exception.class)
	public void AlreadyHasBinaException() throws BadInit_Exception, EmailExists_Exception, InvalidEmail_Exception, AlreadyHasBina_Exception, InvalidStation_Exception, NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception {
		
		int bonus1 = 1;

			client.testInitStation(stationId1, 10, 10, 5, bonus1);
			client.activateUser(userMail);
			client.rentBina(stationId1, userMail);
			client.rentBina(stationId1, userMail);
			
		
	}
	
	@Test (expected = InvalidEmail_Exception.class)
	public void InvalidEmailException() throws BadInit_Exception, EmailExists_Exception, InvalidEmail_Exception {
		
		client.testInitStation(stationId1, 10, 10, 5, 1);
		client.activateUser("mail_invalido");

	}
	
	@Test(expected = EmailExists_Exception.class)
	public void EmailExistsException() throws BadInit_Exception, EmailExists_Exception, InvalidEmail_Exception {
		
		client.testInitStation(stationId1, 10, 10, 5, 1);
		client.activateUser(userMail);
		client.activateUser(userMail);

	}
	
	
	@Test (expected = InvalidStation_Exception.class)
	public void InvalidStationException() throws BadInit_Exception, AlreadyHasBina_Exception, InvalidStation_Exception, NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception, EmailExists_Exception, InvalidEmail_Exception  {
		client.activateUser(userMail);
		client.rentBina("CXX_Station1", userMail);

	}
	
	@Test(expected = NoBinaAvail_Exception.class)
	public void NoBinaAvailableException() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception, AlreadyHasBina_Exception, NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception {
			client.testInitStation(stationId1, 10, 10, 0, 1);
			client.activateUser(userMail);
			client.rentBina(stationId1, userMail);
		
	}
	
	@Test(expected = NoCredit_Exception.class)
	public void NoCreditException() throws BadInit_Exception, EmailExists_Exception, InvalidEmail_Exception, AlreadyHasBina_Exception, InvalidStation_Exception, NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception {
		client.testInitStation(stationId1, 10, 10, 5, 2);
		client.testInit(0);
		client.activateUser(userMail2);
		client.rentBina(stationId1, userMail2);

	}
	
	@Test(expected = UserNotExists_Exception.class)
	public void UserNotExistsException() throws AlreadyHasBina_Exception, InvalidStation_Exception, NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception {
		
		client.rentBina(stationId1, userMail);

	}
	
	@After
	public void tearDown() {
		client.testClear();
	}
}
