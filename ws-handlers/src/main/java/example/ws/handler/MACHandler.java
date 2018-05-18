package example.ws.handler;

import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Mac;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.kerby.CipherClerk;


public class MACHandler implements SOAPHandler<SOAPMessageContext> {
	
	/** Plain text to protect with the message authentication code. */
	final String plainText = "This is the plain text!";
	/** Plain text bytes. */
	final byte[] plainBytes = plainText.getBytes();

	/** Message authentication code algorithm. */
	private static final String MAC_ALGO = "HmacSHA256";

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outboundElement = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		QName svcn = (QName) context.get(MessageContext.WSDL_SERVICE);
		context.get(MessageContext.WSDL_OPERATION);
		new CipherClerk();
		
		try {
			if (outboundElement.booleanValue()) {
				
				// Outbound
				
				System.out.println("[MAC Handler] Outbound message");
			
				// get SOAP envelope
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				se.getBody();
				
				// add header
				SOAPHeader shMAC = se.getHeader();
				if (shMAC == null)
					shMAC = se.addHeader();
				

				// add header element (name, namespace prefix, namespace)
				Name mac = se.createName("mac", svcn.getPrefix(), svcn.getNamespaceURI());
				SOAPElement elementMAC = shMAC.addHeaderElement(mac);
			

				// generate AES secret key
				Key key = (Key) context.get("sessionKey");
			
				
			
				
				context.toString();
				
				DOMSource source = new DOMSource(context.getMessage().getSOAPBody());
				StringWriter stringResult = new StringWriter();
				
				
				 TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
				 String message = stringResult.toString();
				 byte[] teste = message.getBytes();

				// make MAC
				byte[] cipherDigest = makeMAC(teste, key);
				
				// add header element value
				elementMAC.addTextNode(DatatypeConverter.printBase64Binary(cipherDigest));


			}
			
			else {
				
//				// Inbound

				// get SOAP envelope header
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();
				se.getBody();
			
				// check header
				if (sh == null) {
					System.out.println("[MAC Handler] [Inbound] Header not found.");
					return true;
				}
				
				
				
				// get first header element
				Name nameMAC = se.createName("mac", svcn.getPrefix(), svcn.getNamespaceURI());
				Iterator<?> it = sh.getChildElements(nameMAC);
				// check header element
				if (!it.hasNext()) {
					System.out.println("[MAC Handler] [Inbound] Header element not found.");
					return true;
				}
				
				
				SOAPElement element = (SOAPElement) it.next();
				

				Key keyInbound = (Key) context.get("sessionKey");
	
				// get header element value
				String valueString = element.getValue();
				
				byte[] valueByteArray = DatatypeConverter.parseBase64Binary(valueString);
				
				DOMSource source = new DOMSource(context.getMessage().getSOAPBody());
				StringWriter stringResult = new StringWriter();
				

				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
				String message = stringResult.toString();
				byte[] test = message.getBytes();
				

				// make MAC
				byte[] cipherDigest = makeMAC(test, keyInbound);
				
				
				if (Arrays.equals(valueByteArray, cipherDigest)) System.out.println("[MACHandler] The MACs match!");
				else throw new RuntimeException("[MACHandler] The MACs don't match!");
				
			}
			
		} catch (TransformerConfigurationException e) {
			System.out.println("[MACHandler] There as an error while configuring the MAC transformer into a byte array.");
		} catch (TransformerException e) {
			System.out.println("[MACHandler] There as an error while using the Transformer to create a byte array.");
		} catch (TransformerFactoryConfigurationError e) {
			System.out.println("[MACHandler] There as an error while transforming the MAC into a byte array.");
		} catch (SOAPException e) {
			System.out.print("Ignoring SOAPException in handler: ");
			System.out.println(e);
		} catch (InvalidKeyException e) {
			System.out.println("[MACHandler] The key inserted while generating MAC is not valid.");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("The requested cryptographic algorithm is not available in the environment.");
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
	
	/** Makes a message authentication code. 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException */
	private static byte[] makeMAC(byte[] bytes, Key key) throws NoSuchAlgorithmException, InvalidKeyException {

		Mac cipher = Mac.getInstance(MAC_ALGO);
		cipher.init(key);
		byte[] cipherDigest = cipher.doFinal(bytes);

		return cipherDigest;
	}




}