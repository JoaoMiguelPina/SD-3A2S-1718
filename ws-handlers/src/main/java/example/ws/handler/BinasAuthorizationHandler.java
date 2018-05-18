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
import javax.xml.soap.SOAPBody;
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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pt.ulisboa.tecnico.sdis.kerby.Auth;
import pt.ulisboa.tecnico.sdis.kerby.CipherClerk;
import pt.ulisboa.tecnico.sdis.kerby.CipheredView;
import pt.ulisboa.tecnico.sdis.kerby.KerbyException;
import pt.ulisboa.tecnico.sdis.kerby.RequestTime;
import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;
import pt.ulisboa.tecnico.sdis.kerby.Ticket;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sdis.kerby.cli.KerbyClientException;


public class BinasAuthorizationHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public boolean handleMessage(SOAPMessageContext context) {  
		
		Boolean outboundElement = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		QName svcn = (QName) context.get(MessageContext.WSDL_SERVICE);
		QName opn = (QName) context.get(MessageContext.WSDL_OPERATION);
		String ticketEmail = (String) context.get("ticketEmail");
		String authEmail = (String) context.get("authEmail");
		
		try {
		
			if (outboundElement.booleanValue()) {
				// Outbound
			
			} 
			
			else {
				
				// Inbound
				
				System.out.println("Processing inbound message at BinasAuthorizationHandler...");

				// get SOAP envelope header
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();
				SOAPBody sb = se.getBody();
				
				SOAPElement element = (SOAPElement) sb.getFirstChild();
				Name nameEmail = se.createName("email");
			
				
				Iterator<?> it = element.getChildElements(nameEmail);
				
				if (!it.hasNext()) {
					System.out.println("[BinasAuthorizationHandler] Element not found");
					return true;
				}
				
				SOAPElement email = (SOAPElement) it.next();
				String emailStr = email.getTextContent();				
				
				if (!emailStr.equals(authEmail) || !emailStr.equals(ticketEmail) || !authEmail.equals(ticketEmail)) throw new RuntimeException("Authorization denied - the provided emails don't match!");
				else System.out.println("[BinasAuthorizationHandler] Authorization granted - the emails match!");
			

			
				
			
			}
		
		} catch (SOAPException e) {
			throw new RuntimeException("[BinasAuthorizationHandler] There was an error while getting a SOAP Message.");
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