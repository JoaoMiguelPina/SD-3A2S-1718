package org.binas.ws.it;

import org.binas.domain.User;
import org.binas.station.ws.cli.StationClient;
import org.binas.station.ws.cli.StationClientException;
import org.binas.ws.FullStation_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.NoBinaRented_Exception;
import org.binas.ws.NoSlotAvail_Exception;
import org.binas.ws.UserNotExists_Exception;
import org.binas.ws.UserView;
import org.junit.Test;

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
			client.rentBina(sv1.getId(), userMail);
			
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
		}catch (StationClientException e) {
			System.out.println("There was an error while calling the StationClient. Check output: " + e);
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (NoSlotAvail_Exception e) {
			System.out.println("There is no slot available at this station.");
		}catch (UserNotExists_Exception e) {
			System.out.println("The user: " + e + "doesnt exists.");
		}

	}
	
	@Test(expected = FullStation_Exception.class)
	public void FullStationException() {
		
		int bonus1 = 1;
		client.testInitStation(stationId1, 10, 10, 1, bonus1);
		StationView sv1 = client.getInfoStation(stationId1);
		assertNotNull(sv1);
		
		client.activateUser(userMail);
		client.activateUser("pedro@tecnico");
		client.rentBina(sv1.getId(), userMail);
		client.rentBina(sv1.getId(), "pedro@tecnico");
	}
	
	@Test(expected=InvalidStation_Exception)
	public void InvalidStationException() {
		client.testInitStation("CXX_Station1", 10, 10, 1, 2);
	}
	
	@Test(expected=NoBinaRented_Exception)
	public void NoBinaRentedException() {
		//STATION 1
		int bonus1 = 1;
		client.testInitStation(stationId1, 10, 10, 5, bonus1);
		StationView sv1 = client.getInfoStation(stationId1);
		assertNotNull(sv1);
		
		client.activateUser(userMail);
		
		int value1 = client.getCredit(userMail);
		client.returnBina(sv1.getId(), userMail);

	}
	
	@Test(expected=UserNotExists_Exception)
	public void UserNotExistsException() {
		int bonus1 = 1;
		client.testInitStation(stationId1, 10, 10, 5, bonus1);
		StationView sv1 = client.getInfoStation(stationId1);
		assertNotNull(sv1);
		
		client.rentBina(sv1.getId(), userMail);
		
		
	}
}

//public void returnBina(String stationId, String email)
//		throws FullStation_Exception, InvalidStation_Exception, NoBinaRented_Exception, UserNotExists_Exception {
//	
//	StationClient s;
//	try {
//		s = new StationClient(this.endpointManager.getUddiUrl(), stationId);
//		org.binas.station.ws.StationView sv = s.getInfo();
//		
//		User user = null;
//		user = user.getUser(email);
//		UserView uv = user.getUserView();
//		
//		if (user.doesHaveBina()) {
//			if (sv.getFreeDocks() == 0) {
//				throw new FullStation_Exception("This station is full", null);
//			}
//			else{
//				int bonus = s.returnBina();
//				user.setHasBina(false);
//				user.addCredit(bonus);
//			}
//		}
//	} catch (StationClientException e) {
//		System.out.println("There was an error while calling the StationClient. Check output: " + e);
//	} catch (InvalidEmail_Exception e) {
//		System.out.println("The provided email (" + e + ") is invalid.");
//	} catch (NoSlotAvail_Exception e) {
//		System.out.println("There is no slot available at this station.");
//	}
//	
//	throw new NoBinaRented_Exception(email, null);
//	
//	
//	
//}