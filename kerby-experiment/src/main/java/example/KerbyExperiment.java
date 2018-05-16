package example;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

import pt.ulisboa.tecnico.sdis.kerby.SessionKeyAndTicketView;


import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import pt.ulisboa.tecnico.sdis.kerby.Auth;
import pt.ulisboa.tecnico.sdis.kerby.BadTicketRequest_Exception;
import pt.ulisboa.tecnico.sdis.kerby.CipheredView;
import pt.ulisboa.tecnico.sdis.kerby.RequestTime;
import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;
import pt.ulisboa.tecnico.sdis.kerby.SessionKey;
import pt.ulisboa.tecnico.sdis.kerby.SessionKeyAndTicketView;
import pt.ulisboa.tecnico.sdis.kerby.SessionKeyView;
import pt.ulisboa.tecnico.sdis.kerby.Ticket;
import pt.ulisboa.tecnico.sdis.kerby.TicketView;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;


public class KerbyExperiment {
	
	private static SecureRandom randomGenerator = new SecureRandom();
	private static final String VALID_CLIENT_NAME = "alice@A46.binas.org";
	private static final String VALID_CLIENT_PASSWORD = "jrUiRXG";
	private static final String VALID_SERVER_NAME = "binas@A46.binas.org";
	private static final String VALID_SERVER_PASSWORD = "4N8v8vLt";
	private static final int VALID_DURATION = 30;
	
	protected static KerbyClient client;
	protected static KerbyClient server;
	
	private static Key getKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return SecurityHelper.generateKeyFromPassword(password);
	}
	

    public static void main(String[] args) throws Exception {
        System.out.println("Hi!");

        System.out.println();

        // receive arguments
        System.out.printf("Received %d arguments%n", args.length);

        System.out.println();

        // load configuration properties
        try {
            InputStream inputStream = KerbyExperiment.class.getClassLoader().getResourceAsStream("config.properties");
            // variant for non-static methods:
            // InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

            Properties properties = new Properties();
            properties.load(inputStream);

            System.out.printf("Loaded %d properties%n", properties.size());

        } catch (IOException e) {
            System.out.printf("Failed to load configuration: %s%n", e);
        }

        System.out.println();

		// client-side code experiments
        System.out.println("Experiment with Kerberos client-side processing");
        
        client = new KerbyClient("http://sec.sd.rnl.tecnico.ulisboa.pt:8888/kerby");
        
        final Key clientKey = getKey(VALID_CLIENT_PASSWORD);
        final Key serverKey = getKey(VALID_SERVER_PASSWORD);
        
        long nounce = randomGenerator.nextLong();
        
        SessionKeyAndTicketView result = client.requestTicket(VALID_CLIENT_NAME, VALID_SERVER_NAME, nounce, VALID_DURATION);
        
        CipheredView cipheredSessionKey = result.getSessionKey();
		CipheredView cipheredTicket = result.getTicket();
		
		SessionKey sessionKey = new SessionKey(cipheredSessionKey, clientKey);
		
		Auth auth = new Auth(VALID_CLIENT_NAME, new Date());
		CipheredView cipher = auth.cipher(sessionKey.getKeyXY());
		
		System.out.println("CIPHER: " + cipher.toString());
		System.out.println(sessionKey.toString());
        

        System.out.println();

		// server-side code experiments
        System.out.println("Experiment with Kerberos server-side processing");
		
        server = new KerbyClient("http://sec.sd.rnl.tecnico.ulisboa.pt:8888/kerby");
        
        Ticket ticket = new Ticket(cipheredTicket, serverKey);
        ticket.validate();
        System.out.println(ticket.toString());
        
        Auth authServer = new Auth(cipher, sessionKey.getKeyXY());
        authServer.validate();
        
        RequestTime time = new RequestTime(cipher, sessionKey.getKeyXY());

        System.out.println();
		
		System.out.println("Bye!");
    }
}
