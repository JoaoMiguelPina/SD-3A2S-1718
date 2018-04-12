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
import org.junit.Test;

import junit.framework.Assert;

public class ReturnBinaIT extends BaseIT{
	
	String stationId1 = "A46_Station1";
	String stationId2 = "A46_Station2";
	String stationId3 = "A46_Station3";
	
	String userMail = "sd@tecnico.pt";
	
	@Test 
	public void success() {
		try {
			
			//STATION 1
			int bonus1 = 1;
			client.testInitStation(stationId1, 10, 10, 5, bonus1);
			StationView sv1 = client.getInfoStation(stationId1);
			assertNotNull(sv1);
			
			client.activateUser(userMail);
			client.rentBina(stationId1, userMail);
			
			assertEquals(1, client.getInfoStation(sv1.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv1.getId()).getTotalGets());
			
			int value1 = client.getCredit(userMail);
			client.returnBina(sv1.getId(), userMail);
			
			assertEquals(0, client.getInfoStation(sv1.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv1.getId()).getTotalReturns());
			
			//STATION 2
			int bonus2 = 2;
			client.testInitStation(stationId2, 17, 17, 4, bonus2);
			StationView sv2 = client.getInfoStation(stationId2);
			assertNotNull(sv2);
			
			client.activateUser(userMail);
			client.rentBina(sv2.getId(), userMail);
			
			assertEquals(1, client.getInfoStation(sv2.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv2.getId()).getTotalGets());
			
			int value2 = client.getCredit(userMail);
			client.returnBina(sv2.getId(), userMail);
			
			assertEquals(0, client.getInfoStation(sv2.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv2.getId()).getTotalReturns());
			
			//STATION 3
			int bonus3 = 3;
			client.testInitStation(stationId3, 7, 7, 22, bonus3);
			StationView sv3 = client.getInfoStation(stationId3);
			assertNotNull(sv3);
			
			client.activateUser(userMail);
			client.rentBina(sv3.getId(), userMail);
			
			assertEquals(1, client.getInfoStation(sv3.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv3.getId()).getTotalGets());
			
			int value3 = client.getCredit(userMail);
			client.returnBina(sv3.getId(), userMail);
			
			assertEquals(0, client.getInfoStation(sv3.getId()).getFreeDocks());
			assertEquals(1, client.getInfoStation(sv3.getId()).getTotalReturns());
			
		}catch(BadInit_Exception e) {
			System.out.println("There was an error while creating station. Check output: " + e);
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		}catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		} catch (InvalidStation_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FullStation_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBinaRented_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyHasBina_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBinaAvail_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoCredit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test(expected = FullStation_Exception.class)
	public void FullStationException() {
		
		int bonus1 = 1;
		try {
			client.testInitStation(stationId1, 10, 10, 1, bonus1);
			StationView sv1 = client.getInfoStation(stationId1);
			assertNotNull(sv1);
			
			client.activateUser(userMail);
			client.activateUser("pedro@tecnico");
			client.rentBina(sv1.getId(), userMail);
			client.rentBina(sv1.getId(), "pedro@tecnico");
		} catch (BadInit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStation_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEmail_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyHasBina_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBinaAvail_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoCredit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test(expected=InvalidStation_Exception.class)
	public void InvalidStationException() {
		try {
			client.testInitStation("CXX_Station1", 10, 10, 1, 2);
		} catch (BadInit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected=NoBinaRented_Exception.class)
	public void NoBinaRentedException() {
		//STATION 1
		int bonus1 = 1;
		try {
			client.testInitStation(stationId1, 10, 10, 5, bonus1);
			StationView sv1 = client.getInfoStation(stationId1);
			assertNotNull(sv1);
			
			client.activateUser(userMail);
			
			int value1 = client.getCredit(userMail);
			client.returnBina(sv1.getId(), userMail);
		} catch (BadInit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStation_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEmail_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FullStation_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBinaRented_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	@Test(expected=UserNotExists_Exception.class)
	public void UserNotExistsException() {
		int bonus1 = 1;
		try {
			client.testInitStation(stationId1, 10, 10, 5, bonus1);
			StationView sv1 = client.getInfoStation(stationId1);
			assertNotNull(sv1);
			
			client.rentBina(sv1.getId(), userMail);
		} catch (BadInit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStation_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyHasBina_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBinaAvail_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoCredit_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
