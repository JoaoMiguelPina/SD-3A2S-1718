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


public class EvilHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public boolean handleMessage(SOAPMessageContext context) {  
		
		Boolean outboundElement = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		QName svcn = (QName) context.get(MessageContext.WSDL_SERVICE);
		try {
		
			if (outboundElement.booleanValue()) {
				// Outbound
				

				// get SOAP envelope
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPBody sb = se.getBody();
				
				
				if (sb == null)
					sb = se.addBody();

				// add body element (name, namespace prefix, namespace)
				Name cambridgeAnalytica = se.createName("cambridgeAnalytica", svcn.getPrefix(), svcn.getNamespaceURI());
				SOAPElement element = (SOAPElement) sb.getFirstChild();
				
				SOAPElement analytica = element.addChildElement(cambridgeAnalytica);
				analytica.addTextNode("ZUCC must SUCC");
		
			
			} 
			
			else {
				
				// Inbound
						
				
			
			}
		
		} catch (Exception e) {
			
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