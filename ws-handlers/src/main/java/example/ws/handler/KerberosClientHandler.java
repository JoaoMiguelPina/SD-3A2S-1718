package example.ws.handler;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.kerby.Auth;
import pt.ulisboa.tecnico.sdis.kerby.BadTicketRequest_Exception;
import pt.ulisboa.tecnico.sdis.kerby.CipheredView;
import pt.ulisboa.tecnico.sdis.kerby.KerbyException;
import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;
import pt.ulisboa.tecnico.sdis.kerby.SessionKey;
import pt.ulisboa.tecnico.sdis.kerby.SessionKeyAndTicketView;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;

public class KerberosClientHandler implements SOAPHandler<SOAPMessageContext> {
	
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

    /**
     * Gets the names of the header blocks that can be processed by this Handler instance.
     * If null, processes all.
     */
    public Set getHeaders() {
        return null;
    }

    /**
     * The handleMessage method is invoked for normal processing of inbound and
     * outbound messages.
     */

    /**
     * Called at the conclusion of a message exchange pattern just prior to the
     * JAX-WS runtime dispatching a message, fault or exception.
     */
    public void close(MessageContext messageContext) {

    }

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		Boolean outboundElement = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	
		   	try {
		   		
		   		if (outboundElement.booleanValue()) {
					client = new KerbyClient("http://sec.sd.rnl.tecnico.ulisboa.pt:8888/kerby");
					
					final Key clientKey = getKey(VALID_CLIENT_PASSWORD); // Kc
			        
			        long nounce = randomGenerator.nextLong();
			        
			        SessionKeyAndTicketView result = client.requestTicket(VALID_CLIENT_NAME, VALID_SERVER_NAME, nounce, VALID_DURATION);
			        
			        CipheredView cipheredSessionKey = result.getSessionKey(); // Kcs
					CipheredView cipheredTicket = result.getTicket();
					
					SessionKey sessionKey = new SessionKey(cipheredSessionKey, clientKey); 
					
					Auth auth = new Auth(VALID_CLIENT_NAME, new Date());
					CipheredView cipher = auth.cipher(sessionKey.getKeyXY());
					
					
					// writing Ticket SOAP message
					System.out.println("Writing header to OUTbound SOAP messagnkjbhbvhe...");
					
					QName svcn = (QName) context.get(MessageContext.WSDL_SERVICE);
					QName opn = (QName) context.get(MessageContext.WSDL_OPERATION);
					
					
					// get SOAP envelope
					SOAPMessage msg = context.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					
					// add header
					SOAPHeader shTicket = se.getHeader();
					if (shTicket == null)
						shTicket = se.addHeader();
	
					// add header element (name, namespace prefix, namespace)
					System.out.println("Namespace: " + svcn.getNamespaceURI());
					Name nameTicket = se.createName("ticket", svcn.getPrefix(), svcn.getNamespaceURI());
					SOAPHeaderElement elementTicket = shTicket.addHeaderElement(nameTicket);
	
					// add header element value
					elementTicket.addTextNode(cipheredTicket.toString());
					
					
					// add header
					SOAPHeader shAuth = se.getHeader();
					if (shAuth == null)
						shAuth = se.addHeader();
	
					// add header element (name, namespace prefix, namespace)
					Name nameAuth = se.createName("authenticator", svcn.getPrefix(), svcn.getNamespaceURI());
					SOAPHeaderElement elementAuth = shAuth.addHeaderElement(nameAuth);
	
					// add header element value
					elementAuth.addTextNode(cipher.toString());
					
					
					System.out.println("CIPHER: " + cipher.toString());
					System.out.println(sessionKey.toString());
			        
	
			        System.out.println();
		   		}
		   		
		   		else {
		   			System.out.println("MENSAGEM DE INBOUND LOL");
		   		}
				
			} catch (KerbyClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KerbyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadTicketRequest_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	        
	        
		return true;
	}


	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

}