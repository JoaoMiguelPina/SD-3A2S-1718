package example.ws.handler;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
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
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.kerby.Auth;
import pt.ulisboa.tecnico.sdis.kerby.CipherClerk;
import pt.ulisboa.tecnico.sdis.kerby.CipheredView;
import pt.ulisboa.tecnico.sdis.kerby.KerbyException;
import pt.ulisboa.tecnico.sdis.kerby.RequestTime;
import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;
import pt.ulisboa.tecnico.sdis.kerby.Ticket;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;


public class KerberosServerHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static SecureRandom randomGenerator = new SecureRandom();
	private static final String VALID_CLIENT_NAME = "alice@A46.binas.org";
	private static final String VALID_CLIENT_PASSWORD = "jrUiRXG";
	private static final String VALID_SERVER_NAME = "binas@A46.binas.org";
	private static final String VALID_SERVER_PASSWORD = "4N8v8vLt";
	private static final int VALID_DURATION = 30;
	public static final String CONTEXT_PROPERTY = "my.property";
	
	protected static KerbyClient client;
	protected static KerbyClient server;
	
	private static Key getKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return SecurityHelper.generateKeyFromPassword(password);
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		
		Boolean outboundElement = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		CipherClerk cc = new CipherClerk();
		RequestTime time = null;
		QName svcn = (QName) context.get(MessageContext.WSDL_SERVICE);
		QName opn = (QName) context.get(MessageContext.WSDL_OPERATION);
		Key sessionKey = null;
		

			
			try {
				server = new KerbyClient("http://sec.sd.rnl.tecnico.ulisboa.pt:8888/kerby");
			
			
				if (outboundElement.booleanValue()) {
					System.out.println("OUTbound SOAP message...");
					
					// get SOAP envelope
					SOAPMessage msg = context.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					
					
					// add header
					SOAPHeader shTime = se.getHeader();
					if (shTime == null)
						shTime = se.addHeader();
	
					// add header element (name, namespace prefix, namespace)
					Name nameTime = se.createName("reqtime", svcn.getPrefix(), svcn.getNamespaceURI());
					SOAPElement elementTime = shTime.addHeaderElement(nameTime);
					
					sessionKey = (Key) context.get("sessionKey");
					time = (RequestTime) context.get("time");

					// add header element value
					elementTime.addTextNode(DatatypeConverter.printBase64Binary(cc.cipherToXMLBytes(time.cipher(sessionKey), "reqtime")));
					
					
					
				}
				
				else {
					
					
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
					Name nameTicket = se.createName("ticket", svcn.getPrefix(), svcn.getNamespaceURI());
					Name nameAuth = se.createName("authenticator", svcn.getPrefix(), svcn.getNamespaceURI());
					Iterator<?> it = sh.getChildElements(nameTicket);
					// check header element
					if (!it.hasNext()) {
						System.out.println("Header element not found.");
						return true;
					}
					
					
					SOAPElement element = (SOAPElement) it.next();
					
		
					// get header element value
					String valueString = element.getValue();
					CipheredView cvTicket = cc.cipherFromXMLBytes(DatatypeConverter.parseBase64Binary(valueString));
					
					it = sh.getChildElements(nameAuth);
	
					element = (SOAPElement) it.next();
					valueString = element.getValue();
					CipheredView cvAuth = cc.cipherFromXMLBytes(DatatypeConverter.parseBase64Binary(valueString));
					
					final Key serverKey = getKey(VALID_SERVER_PASSWORD);
					
				    Ticket ticket = new Ticket(cvTicket, serverKey);
				    sessionKey = ticket.getKeyXY();
				    ticket.validate();
				    context.put("ticketEmail", ticket.getX());
				        
				    Auth authServer = new Auth(cvAuth, sessionKey);
				    authServer.validate();
				    context.put("authEmail", authServer.getX());
				        
				    time = new RequestTime(cvAuth, sessionKey);

					
					// put header in a property context
					
					context.put("time", time);
					context.put("sessionKey", sessionKey);
	
				}
			
			} catch (KerbyClientException e) {
				System.out.println("There was an error while connecting to the KerbyClient.");
			} catch (SOAPException e) {
				System.out.println("Ignoring SOAPException in handler: ");
				System.out.println(e);
			} catch (JAXBException e) {
				System.out.println("There was an error while marshelling or unmarshelling the ticket.");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("The requested cryptographic algorithm is not available in the environment.");
			} catch (InvalidKeySpecException e) {
				System.out.println("There was an error while obtaining the client's key.");
			} catch (KerbyException e) {
				System.out.println("There was an error while connecting to Kerby.");
			}
	        
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}



}