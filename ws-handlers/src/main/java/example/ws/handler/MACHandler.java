package example.ws.handler;

import static javax.xml.bind.DatatypeConverter.printHexBinary;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.NodeList;
import pt.ulisboa.tecnico.sdis.kerby.CipherClerk;
import pt.ulisboa.tecnico.sdis.kerby.CipheredView;
import pt.ulisboa.tecnico.sdis.kerby.SessionKey;


public class MACHandler implements SOAPHandler<SOAPMessageContext> {
	
	/** Plain text to protect with the message authentication code. */
	final String plainText = "This is the plain text!";
	/** Plain text bytes. */
	final byte[] plainBytes = plainText.getBytes();

	/** Symmetric cryptography algorithm. */
	private static final String SYM_ALGO = "AES";
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
				
				System.out.println("OUBOUND");
			
				// get SOAP envelope
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPBody sb = se.getBody();
				
				System.out.println("QUAU FOI");
				
				// add header
				SOAPHeader shMAC = se.getHeader();
				if (shMAC == null)
					shMAC = se.addHeader();
				
				System.out.println("A EMPRESA");
				

				// add header element (name, namespace prefix, namespace)
				Name mac = se.createName("mac", svcn.getPrefix(), svcn.getNamespaceURI());
				SOAPElement elementMAC = shMAC.addHeaderElement(mac);
				
				System.out.println("QUE DESENVULVEU");

				// generate AES secret key
				
				
				System.out.println("MAPA DO CONTEXT" + context.toString());
				Key key = (Key) context.get("sessionKey");
			
//				Key key1 = (Key) context.get("sessionKey");
				System.out.println("gaja chata caralho" );
				
			
				
				context.toString();
				
				System.out.println("U IPHONE");
				
				NodeList list = sb.getChildNodes();
				
				System.out.println("TAMANHO DO LIST" + list.getLength());
				
				ByteArrayOutputStream sw = new ByteArrayOutputStream();
				 TransformerFactory.newInstance().newTransformer().transform(
		                    new DOMSource(sb.getFirstChild()),
		                    new StreamResult(sw));
				 
				 byte[] teste = sw.toByteArray();

				// make MAC
				System.out.println("SOAP BODY: " + sb.toString());
				byte[] cipherDigest = makeMAC(teste, key);
				System.out.println("A NOKIA");
				System.out.println(printHexBinary(cipherDigest));
				
				// add header element value
				
				 
				
				elementMAC.addTextNode(DatatypeConverter.printBase64Binary(cipherDigest));

				System.out.println("OU O APPLE " + DatatypeConverter.printBase64Binary(cipherDigest));

			}
			
			else {
				
				System.out.println("LOL");
//				// Inbound
//				
//
//				
				// get SOAP envelope header
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();
				SOAPBody sb = se.getBody();
			
//	
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
				System.out.println("NESPRESSO GET VALUE: " + valueString);
				
				byte[] valueByteArray = DatatypeConverter.parseBase64Binary(valueString);
				System.out.println("DELTA");
				
				ByteArrayOutputStream sw = new ByteArrayOutputStream();
				 TransformerFactory.newInstance().newTransformer().transform(
		                    new DOMSource(sb.getFirstChild()),
		                    new StreamResult(sw));
				 
				 byte[] teste = sw.toByteArray();

				// make MAC
				System.out.println("SOAP BODY: " + sb.toString());
				byte[] cipherDigest = makeMAC(teste, keyInbound);
				
				if (Arrays.equals(valueByteArray, cipherDigest)) System.out.println("MESMO MAC! UAU BUE BONS");
				else System.out.println("FODE A TUA MAE E FAZ FILHOS DA PUTA");
//				
				
////				
////				
//////				System.out.println("ANANAS " + context.get("sessionKey").toString());
//////			
//				// verify the MAC
//				System.out.println("Verifying ...");
//				boolean result = verifyMAC(valueByteArray, plainBytes, keyInbound);
//				if (result) System.out.println("MESMO MAC! UAU BUE BONS");
//				else System.out.println("FODE A TUA MAE E FAZ FILHOS DA PUTA");
				
			}
			
		} catch(Exception e) {
			
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
	
	/** Makes a message authentication code. */
	private static byte[] makeMAC(byte[] bytes, Key key) throws Exception {

		Mac cipher = Mac.getInstance(MAC_ALGO);
		cipher.init(key);
		byte[] cipherDigest = cipher.doFinal(bytes);

		return cipherDigest;
	}

	/**
	 * Calculates new digest from text and compare it to the to deciphered
	 * digest.
	 */
	private static boolean verifyMAC(byte[] cipherDigest, byte[] bytes, Key key) throws Exception {

		Mac cipher = Mac.getInstance(MAC_ALGO);
		cipher.init(key);
		byte[] cipheredBytes = cipher.doFinal(bytes);
		return Arrays.equals(cipherDigest, cipheredBytes);
	}



}