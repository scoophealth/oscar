package com.indivica.olis;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import ca.ssha.www._2005.hial.OLISStub;
import ca.ssha.www._2005.hial.OLISStub.HIALRequest;
import ca.ssha.www._2005.hial.OLISStub.HIALRequestSignedRequest;
import ca.ssha.www._2005.hial.OLISStub.OLISRequest;
import ca.ssha.www._2005.hial.OLISStub.OLISRequestResponse;

import com.indivica.olis.queries.Query;

public class Driver {

	public static String submitOLISQuery(HttpServletRequest request, Query query) {
	    try {
	    	OLISMessage message = new OLISMessage(query);

	    	System.setProperty("javax.net.ssl.keyStore",
	    			OscarProperties.getInstance().getProperty("olis_ssl_keystore").trim());
	    	System.setProperty("javax.net.ssl.keyStorePassword", 
	    			OscarProperties.getInstance().getProperty("olis_ssl_keystore_password").trim());
	    	System.setProperty("javax.net.ssl.trustStore",
	    			OscarProperties.getInstance().getProperty("olis_truststore").trim());
	    	System.setProperty("javax.net.ssl.trustStorePassword", 
	    			OscarProperties.getInstance().getProperty("olis_truststore_password").trim());

	    	OLISRequest olisRequest = new OLISRequest();
	    	olisRequest.setHIALRequest(new HIALRequest());
	    	OLISStub olis = new OLISStub();

	    	olisRequest.getHIALRequest().setClientTransactionID(message.getTransactionId());
	    	olisRequest.getHIALRequest().setSignedRequest(new HIALRequestSignedRequest());

	    	String msgInXML = String
	    	.format("<Request xmlns=\"http://www.ssha.ca/2005/HIAL\"><Content><![CDATA[%s]]></Content></Request>",
	    			message.getOlisHL7String());

	    	String signedRequest = Driver.signData(msgInXML);
	    	olisRequest.getHIALRequest().getSignedRequest().setSignedData(signedRequest);

	    	OLISRequestResponse olisResponse = olis.oLISRequest(olisRequest);

	    	String signedData = olisResponse.getHIALResponse().getSignedResponse().getSignedData();
	    	String unsignedData = Driver.unsignData(signedData);

	    	if (request != null) { 
		    	request.setAttribute("msgInXML", msgInXML);
		    	request.setAttribute("signedRequest", signedRequest);
		    	request.setAttribute("signedData", signedData);
		    	request.setAttribute("unsignedResponse", unsignedData);
	    	}
	    	return unsignedData;

	    } catch (Exception e) {
	    	MiscUtils.getLogger().error("Can't perform OLIS query due to exception.", e);
	    	return "";
	    }
    }
    
	public static String unsignData(String data) {
		
			byte[] dataBytes = Base64.decode(data);
		
			try {
				
		        CMSSignedData s = new CMSSignedData(dataBytes);
		        CertStore certs = s.getCertificatesAndCRLs("Collection", "BC");
		        SignerInformationStore signers = s.getSignerInfos();
		        @SuppressWarnings("unchecked")
		        Collection<SignerInformation> c = signers.getSigners();
		        Iterator<SignerInformation> it = c.iterator();
		        while (it.hasNext())
		        {
		                X509Certificate cert = null;
		                SignerInformation signer = it.next();
		                Collection certCollection = certs.getCertificates(signer.getSID());
		                @SuppressWarnings("unchecked")
		                Iterator<X509Certificate> certIt = certCollection.iterator();
		                cert = certIt.next();
		                if ( !signer.verify(cert.getPublicKey(), "BC")) throw new Exception("Doesn't verify");
		        }
		        
		        CMSProcessableByteArray cpb = (CMSProcessableByteArray) s.getSignedContent();
		        byte[] signedContent = (byte[]) cpb.getContent();
		        String content = new String(signedContent);
		        return content;
			} catch (Exception e) {
				MiscUtils.getLogger().error("error",e);
			}
			return null;
		
	}
	
	public static String signData(String data) {
		X509Certificate cert = null;
		PublicKey pub = null;
		PrivateKey priv = null;
		KeyStore keystore = null;
		String pwd = "Olis2011";
		String result = null;
		try {
			Security.addProvider(new BouncyCastleProvider());

			keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
			// Load the keystore
			keystore.load(new FileInputStream(OscarProperties.getInstance().getProperty("olis_keystore")), pwd.toCharArray());

			Enumeration e = keystore.aliases();
			String name = "";

			if (e != null) {
				while (e.hasMoreElements()) {
					String n = (String) e.nextElement();
					if (keystore.isKeyEntry(n)) {
						name = n;
					}
				}
			}

			// Get the private key and the certificate
			priv = (PrivateKey) keystore.getKey(name, pwd.toCharArray());
			cert = (X509Certificate) keystore.getCertificate(name);

			// I'm not sure if this is necessary
			Certificate[] certChain = keystore.getCertificateChain(name);
			ArrayList<Certificate> certList = new ArrayList<Certificate>();
			CertStore certs = null;
			for (int i = 0; i < certChain.length; i++) {
				certList.add(certChain[i]);
			}

			certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");

			// Encrypt data
			CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();

			// What digest algorithm i must use? SHA1? MD5? RSA?...
			sgen.addSigner(priv, (X509Certificate) cert, CMSSignedDataGenerator.DIGEST_MD5);

			// I'm not sure this is necessary
			sgen.addCertificatesAndCRLs(certs);

			// I think that the 2nd parameter need to be false (detached form)
			CMSSignedData csd = sgen.generate(new CMSProcessableByteArray(data.getBytes()), false, "BC");
			byte[] signedData = csd.getEncoded();
			byte[] signedDataB64 = Base64.encode(signedData);
			
			result = new String(signedDataB64);
			
			

		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't sign HL7 message for OLIS", e);
		}
		return result;
	}
}
