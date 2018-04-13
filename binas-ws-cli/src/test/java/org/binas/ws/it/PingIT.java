package org.binas.ws.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * Test suite
 */
public class PingIT extends BaseIT {
	
    @Test
    public void success() {
		 assertNotNull(client.testPing("Um teste de ping!"));
    }
    
    @Test
    public void pingNullTest() {
		 assertNull(client.testPing(null));
    }
    
    @Test
    public void pingEmptyTest() {
		 assertNull(client.testPing(""));
    }
    
    
   
}
