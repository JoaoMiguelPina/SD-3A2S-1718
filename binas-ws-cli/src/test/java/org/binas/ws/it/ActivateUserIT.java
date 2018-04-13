package org.binas.ws.it;

import static org.junit.Assert.assertNotNull;

import org.binas.ws.BadInit_Exception;
import org.binas.ws.EmailExists_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.InvalidStation_Exception;
import org.binas.ws.StationView;
import org.binas.ws.UserView;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;

public class ActivateUserIT extends BaseIT {
	@Test
	public void success() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("regouga@regouga");
		Assert.assertEquals("regouga@regouga", userView.getEmail());
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail1() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste.@teste");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail2() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste@teste.");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail3() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("@teste");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail4() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste@");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail5() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("@");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail6() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste.@teste.");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail7() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste.teste.@teste");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail8() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste@teste.teste.");
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail9() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView = client.activateUser("teste@teste.teste.");
	}
	
	@Test(expected = EmailExists_Exception.class)
	public void invalidEmail10() throws BadInit_Exception, InvalidStation_Exception, EmailExists_Exception, InvalidEmail_Exception {
		UserView userView1 = client.activateUser("teste1@teste1");
		UserView userView2 = client.activateUser("teste1@teste1");
	}
	
	@After
	public void teardown() {
		client.testClear();
	}
	
}
