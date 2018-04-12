package org.binas.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Properties;

<<<<<<< HEAD
import org.binas.ws.it.BaseIT;
=======
//import org.binas.station.ws.cli.StationClient;
//import org.binas.station.ws.it.BaseIT;
>>>>>>> 13db0a1d9123f4248529e161a4fb2886f6e3e908
import org.binas.ws.AlreadyHasBina_Exception;
import org.binas.ws.BadInit_Exception;
import org.binas.ws.EmailExists_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.NoBinaAvail_Exception;
import org.binas.ws.NoCredit_Exception;
import org.binas.ws.UserNotExists_Exception;
import org.binas.ws.cli.BinasClient;
<<<<<<< HEAD
import org.junit.After;
=======
import org.binas.ws.cli.BinasClientException;
>>>>>>> 13db0a1d9123f4248529e161a4fb2886f6e3e908
import org.junit.Before;
import org.junit.Test;

public class RentBinaTest extends BaseIT{

	String stationId1 = "A46_Station1";
	String stationId2 = "A46_Station2";
	String stationId3 = "A46_Station3";
	
	String userMail = "sd.rent@tecnico.pt";
	
<<<<<<< HEAD
	public void success() {
=======
	/*
	@Before
	public void setup() throws BinasClientException, IOException {
		testProps = new Properties();
>>>>>>> 13db0a1d9123f4248529e161a4fb2886f6e3e908
		try {
			
			//STATION 1
			int bonus1 = 1;
			client.testInitStation(stationId1, 10, 10, 5, bonus1);
			org.binas.ws.StationView sv1 = client.getInfoStation(stationId1);
			assertNotNull(sv1);
			
			client.activateUser(userMail);
			client.rentBina(sv1.getId(), userMail);
			
			assertEquals(1, client.getInfoStation(sv1.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv1.getId()).getTotalGets());
			
			//STATION 2
			int bonus2 = 2;
			client.testInitStation(stationId2, 17, 17, 4, bonus2);
			org.binas.ws.StationView sv2 = client.getInfoStation(stationId2);
			assertNotNull(sv2);
			
			client.activateUser(userMail);
			client.rentBina(sv2.getId(), userMail);
			
			assertEquals(1, client.getInfoStation(sv2.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv2.getId()).getTotalGets());

			
			//STATION 3
			int bonus3 = 3;
			client.testInitStation(stationId3, 7, 7, 22, bonus3);
			org.binas.ws.StationView sv3 = client.getInfoStation(stationId3);
			assertNotNull(sv3);
			
			client.activateUser(userMail);
			client.rentBina(sv3.getId(), userMail);
			
			assertEquals(1, client.getInfoStation(sv3.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv3.getId()).getTotalGets());
			
		}catch(BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		}catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidStation_Exception e) {
			System.out.println("The provided station (" + e + ") is invalid.");
		} catch (AlreadyHasBina_Exception e) {
			System.out.println("The provided user (" + e + ") already had rent a bina.");
		} catch (NoBinaAvail_Exception e) {
			System.out.println("The provided station (" + e + ") doesnt have any bina.");
		} catch (NoCredit_Exception e) {
			System.out.println("The provided user (" + e + ") doesnt have credit.");
		} 

	}
	
	@Test(expected = AlreadyHasBina_Exception.class)
	public void AlreadyHasBinaException() {
		
		int bonus1 = 1;
		try {
			client.testInitStation(stationId1, 10, 10, 5, bonus1);
			client.activateUser(userMail);
			client.rentBina(stationId1, userMail);
			client.rentBina(stationId1, userMail);
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (InvalidStation_Exception e) {
			System.out.println("The provided station (" + e + ") is invalid.");
		} catch (AlreadyHasBina_Exception e) {
			System.out.println("The provided user (" + e + ") already had rent a bina.");
		} catch (NoBinaAvail_Exception e) {
			System.out.println("The provided station (" + e + ") doesnt have any bina.");
		} catch (NoCredit_Exception e) {
			System.out.println("The provided user (" + e + ") doesnt have credit.");
		} catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		}	
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void InvalidEmailException() {
		
		try {
			client.testInitStation(stationId1, 10, 10, 5, 1);
			client.activateUser("mail_invalido");
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} 
	}
	
	@Test(expected = EmailExists_Exception.class)
	public void EmailExistsException() {
		
		try {
			client.testInitStation(stationId1, 10, 10, 5, 1);
			client.activateUser(userMail);
			client.activateUser(userMail);
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		}
	}
	
	@Test(expected = BadInit_Exception.class)
	public void BadInitException() {
		
		int bonus1 = 1;
		try {
			client.testInitStation(null, 10, 10, 5, bonus1);
			client.activateUser(userMail);
			client.rentBina(stationId1, userMail);
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (InvalidStation_Exception e) {
			System.out.println("The provided station (" + e + ") is invalid.");
		} catch (AlreadyHasBina_Exception e) {
			System.out.println("The provided user (" + e + ") already had rent a bina.");
		} catch (NoBinaAvail_Exception e) {
			System.out.println("The provided station (" + e + ") doesnt have any bina.");
		} catch (NoCredit_Exception e) {
			System.out.println("The provided user (" + e + ") doesnt have credit.");
		} catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		}	
	}
	
	@Test (expected = InvalidStation_Exception.class)
	public void InvalidStationException()  {
		try {
			client.testInitStation("CXX_Station1", 10, 10, 1, 2);
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		}
	}
	
	@Test(expected = NoBinaAvail_Exception.class)
	public void NoBinaAvailableException() {
		try {
			client.testInitStation(stationId1, 10, 10, 0, 1);
			org.binas.ws.StationView sv1 = client.getInfoStation(stationId1);
			client.activateUser(userMail);
			
			client.rentBina(stationId1, userMail);
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (InvalidStation_Exception e) {
			System.out.println("The provided station (" + e + ") is invalid.");
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (AlreadyHasBina_Exception e) {
			System.out.println("The provided user (" + e + ") already had rent a bina.");
		} catch (NoBinaAvail_Exception e) {
			System.out.println("The provided station (" + e + ") doesnt have any bina.");
		} catch (NoCredit_Exception e) {
			System.out.println("The provided user (" + e + ") doesnt have credit.");
		} catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		}
		
	}
	
	@Test(expected = NoCredit_Exception.class)
	public void NoCreditException() {
		try {
			
			
			client.testInitStation(stationId1, 10, 10, 5, 2);
			client.activateUser(userMail);
			client.testInit(0);
			client.rentBina(stationId1, userMail);
			
			
		} catch (BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (EmailExists_Exception e) {
			System.out.println("The provided email (" + e + ") already exists.");
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (AlreadyHasBina_Exception e) {
			System.out.println("The provided user (" + e + ") already had rent a bina.");
		} catch (InvalidStation_Exception e) {
			System.out.println("The provided station (" + e + ") is invalid.");
		} catch (NoBinaAvail_Exception e) {
			System.out.println("The provided station (" + e + ") doesnt have any bina.");
		} catch (NoCredit_Exception e) {
			System.out.println("The provided user (" + e + ") doesnt have credit.");
		} catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		}
		

	}
	
	@Test(expected = UserNotExists_Exception.class)
	public void UserNotExistsException() {
		
		try {
			//client.testInitStation(stationId1, 10, 10, 5, 2);
			client.rentBina(stationId1, userMail);
		//} catch (BadInit_Exception e) {
			//System.out.println("There was an error while creating station. Check output: " + e);
		} catch (AlreadyHasBina_Exception e) {
			System.out.println("The provided user (" + e + ") already had rent a bina.");
		} catch (InvalidStation_Exception e) {
			System.out.println("The provided station (" + e + ") is invalid.");
		} catch (NoBinaAvail_Exception e) {
			System.out.println("The provided station (" + e + ") doesnt have any bina.");
		} catch (NoCredit_Exception e) {
			System.out.println("The provided user (" + e + ") doesnt have credit.");
		} catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		}
		
	}
	
	@After
	public void tearDown() {
		client.testClear();
	}
}
