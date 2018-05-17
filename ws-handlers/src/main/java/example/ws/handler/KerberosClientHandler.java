package example.ws.handler;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import pt.ulisboa.tecnico.sdis.kerby.Auth;
import pt.ulisboa.tecnico.sdis.kerby.BadTicketRequest_Exception;
import pt.ulisboa.tecnico.sdis.kerby.CipherClerk;
import pt.ulisboa.tecnico.sdis.kerby.CipheredView;
import pt.ulisboa.tecnico.sdis.kerby.KerbyException;
import pt.ulisboa.tecnico.sdis.kerby.RequestTime;
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
		CipherClerk cc = new CipherClerk();
		RequestTime time = null;
		QName svcn = (QName) context.get(MessageContext.WSDL_SERVICE);
		QName opn = (QName) context.get(MessageContext.WSDL_OPERATION);
	
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
					System.out.println("Writing header to OUTbound SOAP message...");
					
					
					// get SOAP envelope
					SOAPMessage msg = context.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					
					
					// Ticket ---- ------------------------------------------------------------------------
					
					
					// add header
					SOAPHeader shTicket = se.getHeader();
					if (shTicket == null)
						shTicket = se.addHeader();
	
					// add header element (name, namespace prefix, namespace)
					Name nameTicket = se.createName("ticket", svcn.getPrefix(), svcn.getNamespaceURI());
					SOAPElement elementTicket = shTicket.addHeaderElement(nameTicket);
					
					
					// add header element value
					elementTicket.addTextNode(DatatypeConverter.printBase64Binary(cc.cipherToXMLBytes(cipheredTicket, "ticket")));
					
					
					
					
					// Authenticator ------------------------------------------------------------------------
					
				
					// add header
					SOAPHeader shAuth = se.getHeader();
					if (shAuth == null)
						shAuth = se.addHeader();
	
					// add header element (name, namespace prefix, namespace)
					Name nameAuth = se.createName("authenticator", svcn.getPrefix(), svcn.getNamespaceURI());
					SOAPElement elementAuth = shAuth.addHeaderElement(nameAuth);
					
					// add header element value
					
					elementAuth.addTextNode(DatatypeConverter.printBase64Binary(cc.cipherToXMLBytes(cipher, "authenticator")));
					

					
					// put header in a property context
					
					context.put("time", auth.getTimeRequest());
					context.put("sessionKey", sessionKey);
					
				
		   		}
		   		
		   		else {
		   			System.out.println("[DEBUG] Inbound message");
		   			
		   		// get SOAP envelope header
					SOAPMessage msg = context.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					SOAPHeader sh = se.getHeader();
		
					// check header
					if (sh == null) {
						System.out.println("Header not found.");
						return true;
					}
					
					// get first header element
					Name nameTime = se.createName("reqtime", svcn.getPrefix(), svcn.getNamespaceURI());
					Iterator<?> it = sh.getChildElements(nameTime);
					// check header element
					if (!it.hasNext()) {
						System.out.println("Header element not found.");
						return true;
					}
					
					
					SOAPElement element = (SOAPElement) it.next();
					
		
					// get header element value
					String valueString = element.getValue();
					CipheredView cvTime = cc.cipherFromXMLBytes(DatatypeConverter.parseBase64Binary(valueString));
					Key sk = ((SessionKey) context.get("sessionKey")).getKeyXY();
					
					RequestTime rt = new RequestTime(cvTime, sk);
					

					
					if (rt.getTimeRequest().equals(context.get("time"))) System.out.println("Ticket date confirmed.");
					else throw new BadTicketRequest_Exception("Ticket date is not valid.", null);
		   			
		   			
		   		}
				
			} catch (KerbyClientException e) {
				System.out.println("There was an error while connecting to the KerbyClient.");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("The requested cryptographic algorithm is not available in the environment.");
			} catch (InvalidKeySpecException e) {
				System.out.println("There was an error while obtaining the client's key.");
			} catch (KerbyException e) {
				System.out.println("There was an error while connecting to Kerby.");
			} catch (BadTicketRequest_Exception e) {
				System.out.println("There was an error while requesting the ticket.");
			} catch (SOAPException e) {
				System.out.println("Ignoring SOAPException in handler: ");
				System.out.println(e);
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
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