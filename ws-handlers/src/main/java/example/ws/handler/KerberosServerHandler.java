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
					System.out.println("[Debug] Outbound message");
					
					// get SOAP envelope
					SOAPMessage msg = context.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();
					
					System.out.println("[Debug] DE PUTA MADRE");
					
					// add header
					SOAPHeader shTime = se.getHeader();
					if (shTime == null)
						shTime = se.addHeader();
					
					System.out.println("[Debug] HELIO PARABENS");
	
					// add header element (name, namespace prefix, namespace)
					Name nameTime = se.createName("reqtime", svcn.getPrefix(), svcn.getNamespaceURI());
					System.out.println("[Debug] ANTES DO SABAO");
					SOAPElement elementTime = shTime.addHeaderElement(nameTime);
					
					sessionKey = (Key) context.get("sessionKey");
					time = (RequestTime) context.get("time");
					System.out.println("[Debug] DEPOIS DO SABAO");
					
					System.out.println("[Debug] TIME NO OUTBOUND: " + context.get("time"));
					System.out.println("[Debug] SESSIONKEY NO OUTBOUND: " + context.get("sessionKey"));
					
					// add header element value
					elementTime.addTextNode(DatatypeConverter.printBase64Binary(cc.cipherToXMLBytes(time.cipher(sessionKey), "reqtime")));
					
					System.out.println("[Debug] BRUNO DE CARALHO");
					
					
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
				        
				    Auth authServer = new Auth(cvAuth, sessionKey);
				    authServer.validate();
				        
				    time = new RequestTime(cvAuth, sessionKey);
					System.out.println("JORGE JESUS" + time.toString());

					
					// put header in a property context
					
					context.put("time", time);
					System.out.println("VAI PO CARALHO PINA");
					context.put("sessionKey", sessionKey);
					System.out.println("VAI PO CARALHO HELIO");
						
	
				}
			
			} catch (KerbyClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
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