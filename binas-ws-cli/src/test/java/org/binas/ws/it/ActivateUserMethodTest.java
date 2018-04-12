package org.binas.ws.it;

import static org.junit.Assert.assertNotNull;

import org.binas.ws.BadInit_Exception;
import org.binas.ws.EmailExists_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.StationView;
import org.binas.ws.UserView;
import org.junit.Test;

import junit.framework.Assert;

public class ActivateUserMethodTest extends BaseIT {
	@Test
	public void success() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste@teste");
		
	}
	
//	@Test(expected = InvalidEmail_Exception.class)
//	public void invalidEmail1() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
//		int bonus1 = 2;
//		client.testInitStation("A46_Station1", 32, 45, 26, bonus1);
//		StationView station1 = client.getInfoStation("A46_Station1");
//		
//		client.activateUser("teste.@teste");
//		
//	}
//	
//	@Test(expected = InvalidEmail_Exception.class)
//	public void invalidEmail2() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
//		int bonus1 = 2;
//		client.testInitStation("A46_Station1", 32, 45, 26, bonus1);
//		StationView station1 = client.getInfoStation("A46_Station1");
//		
//		client.activateUser("teste@teste.");
//		
//	}
//	
//	@Test(expected = InvalidEmail_Exception.class)
//	public void invalidEmail3() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
//		int bonus1 = 2;
//		client.testInitStation("A46_Station1", 32, 45, 26, bonus1);
//		StationView station1 = client.getInfoStation("A46_Station1");
//		
//		client.activateUser("teste.@teste.");
//		
//	}
//	
//	@Test
//	public void invalidEmail4() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
//		int bonus1 = 2;
//		client.testInitStation("A46_Station1", 32, 45, 26, bonus1);
//		StationView station1 = client.getInfoStation("A46_Station1");
//		
//		client.activateUser("teste.teste@teste");
//		
//	}
}
