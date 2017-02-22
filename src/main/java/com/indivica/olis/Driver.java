/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.common.model.Provider;
import org.oscarehr.olis.OLISProtocolSocketFactory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.xml.sax.InputSource;

import oscar.OscarProperties;
import oscar.oscarMessenger.data.MsgProviderData;
import ca.ssha._2005.hial.ArrayOfError;
import ca.ssha._2005.hial.ArrayOfString;
import ca.ssha._2005.hial.Response;
import ca.ssha.www._2005.hial.OLISStub;
import ca.ssha.www._2005.hial.OLISStub.HIALRequest;
import ca.ssha.www._2005.hial.OLISStub.HIALRequestSignedRequest;
import ca.ssha.www._2005.hial.OLISStub.OLISRequest;
import ca.ssha.www._2005.hial.OLISStub.OLISRequestResponse;

import com.indivica.olis.queries.Query;
import org.oscarehr.common.model.OscarMsgType;

public class Driver {

	private static OscarLogDao logDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");

	public static String submitOLISQuery(HttpServletRequest request, Query query) {
		try {
			OLISMessage message = new OLISMessage(query);

			System.setProperty("javax.net.ssl.trustStore", OscarProperties.getInstance().getProperty("olis_truststore").trim());
			System.setProperty("javax.net.ssl.trustStorePassword", OscarProperties.getInstance().getProperty("olis_truststore_password").trim());
			
			OLISRequest olisRequest = new OLISRequest();
			olisRequest.setHIALRequest(new HIALRequest());
			String olisRequestURL = OscarProperties.getInstance().getProperty("olis_request_url", "https://olis.ssha.ca/ssha.olis.webservices.ER7/OLIS.asmx");
			OLISStub olis = new OLISStub(olisRequestURL);
			olis._getServiceClient().getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, new Protocol("https",(ProtocolSocketFactory)  new OLISProtocolSocketFactory(),443));
			
			olisRequest.getHIALRequest().setClientTransactionID(message.getTransactionId());
			olisRequest.getHIALRequest().setSignedRequest(new HIALRequestSignedRequest());

			String olisHL7String = message.getOlisHL7String().replaceAll("\n", "\r");
			String msgInXML = String.format("<Request xmlns=\"http://www.ssha.ca/2005/HIAL\"><Content><![CDATA[%s]]></Content></Request>", olisHL7String);

			String signedRequest = null;

			if (OscarProperties.getInstance().getProperty("olis_returned_cert") != null) {
				signedRequest = Driver.signData2(msgInXML);
			} else {
				signedRequest = Driver.signData(msgInXML);
			}

			olisRequest.getHIALRequest().getSignedRequest().setSignedData(signedRequest);

			try {
				OscarLog logItem = new OscarLog();
				logItem.setAction("OLIS");
				logItem.setContent("query");
				logItem.setData(olisHL7String);

				logItem.setProviderNo("-1");

				logDao.persist(logItem);

			} catch (Exception e) {
				MiscUtils.getLogger().error("Couldn't write log message for OLIS query", e);
			}

			if (OscarProperties.getInstance().getProperty("olis_simulate", "no").equals("yes")) {
				String response = (String) request.getSession().getAttribute("olisResponseContent");
				request.setAttribute("olisResponseContent", response);
				request.getSession().setAttribute("olisResponseContent", null);
				return response;
			} else {
				OLISRequestResponse olisResponse = olis.oLISRequest(olisRequest);

				String signedData = olisResponse.getHIALResponse().getSignedResponse().getSignedData();
				String unsignedData = Driver.unsignData(signedData);
				//MiscUtils.getLogger().info(msgInXML);
				//MiscUtils.getLogger().info("---------------------------------");			
				//MiscUtils.getLogger().info(unsignedData);

				if (request != null) {
					request.setAttribute("msgInXML", msgInXML);
					request.setAttribute("signedRequest", signedRequest);
					request.setAttribute("signedData", signedData);
					request.setAttribute("unsignedResponse", unsignedData);
				}

				writeToFile(unsignedData);
				readResponseFromXML(request, unsignedData);

				return unsignedData;

			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't perform OLIS query due to exception.", e);
			if (request != null) {
				request.setAttribute("searchException", e);
			}

			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
			notifyOlisError(loggedInInfo.getLoggedInProvider(), e.getMessage());
			return "";
		}
	}

	public static void readResponseFromXML(HttpServletRequest request, String olisResponse) {

		olisResponse = olisResponse.replaceAll("<Content", "<Content xmlns=\"\" ");
		olisResponse = olisResponse.replaceAll("<Errors", "<Errors xmlns=\"\" ");

		try {
			DocumentBuilderFactory.newInstance().newDocumentBuilder();
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

			Source schemaFile = new StreamSource(new File(OscarProperties.getInstance().getProperty("olis_response_schema")));
			factory.newSchema(schemaFile);

			JAXBContext jc = JAXBContext.newInstance("ca.ssha._2005.hial");
			Unmarshaller u = jc.createUnmarshaller();
			@SuppressWarnings("unchecked")
			Response root = ((JAXBElement<Response>) u.unmarshal(new InputSource(new StringReader(olisResponse)))).getValue();

			if (root.getErrors() != null) {
				List<String> errorStringList = new LinkedList<String>();

				// Read all the errors
				ArrayOfError errors = root.getErrors();
				List<ca.ssha._2005.hial.Error> errorList = errors.getError();

				for (ca.ssha._2005.hial.Error error : errorList) {
					String errorString = "";
					errorString += "ERROR " + error.getNumber() + " (" + error.getSeverity() + ") : " + error.getMessage();
					MiscUtils.getLogger().debug(errorString);

					ArrayOfString details = error.getDetails();
                                        if (details != null) {
                                            List<String> detailList = details.getString();
                                            for (String detail : detailList) {
                                                    errorString += "\n" + detail;
                                            }
                                        }

					errorStringList.add(errorString);
				}
				if (request != null) request.setAttribute("errors", errorStringList);
			} else if (root.getContent() != null) {
				if (request != null) request.setAttribute("olisResponseContent", root.getContent());
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't read XML from OLIS response.", e);
			
			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
			notifyOlisError(loggedInInfo.getLoggedInProvider(), "Couldn't read XML from OLIS response." + "\n" + e);
		}
	}

	public static String unsignData(String data) {

		byte[] dataBytes = Base64.decode(data);

		try {

			CMSSignedData s = new CMSSignedData(dataBytes);
			Store certs = s.getCertificates();
			SignerInformationStore signers = s.getSignerInfos();
			@SuppressWarnings("unchecked")
			Collection<SignerInformation> c = signers.getSigners();
			Iterator<SignerInformation> it = c.iterator();
			while (it.hasNext()) {
				X509CertificateHolder cert = null;
				SignerInformation signer = it.next();
				Collection certCollection = certs.getMatches(signer.getSID());
				@SuppressWarnings("unchecked")
				Iterator<X509CertificateHolder> certIt = certCollection.iterator();
				cert = certIt.next();

				if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) throw new Exception("Doesn't verify");
			}

			CMSProcessableByteArray cpb = (CMSProcessableByteArray) s.getSignedContent();
			byte[] signedContent = (byte[]) cpb.getContent();
			String content = new String(signedContent);
			return content;
		} catch (Exception e) {
			MiscUtils.getLogger().error("error", e);
		}
		return null;

	}

	//Method uses a jks and a returned cert separately instead of needing to 
	//import the cert into PKCS12 file.
	public static String signData2(String data) {
		X509Certificate cert = null;
		PrivateKey priv = null;
		KeyStore keystore = null;
		String pwd = OscarProperties.getInstance().getProperty("olis_ssl_keystore_password","changeit");
		String result = null;
		try {
			Security.addProvider(new BouncyCastleProvider());

			keystore = KeyStore.getInstance("JKS");
			// Load the keystore
			keystore.load(new FileInputStream(OscarProperties.getInstance().getProperty("olis_keystore")), pwd.toCharArray());

			//Enumeration e = keystore.aliases();
			String name = "olis";

			// Get the private key and the certificate
			priv = (PrivateKey) keystore.getKey(name, pwd.toCharArray());

			FileInputStream is = new FileInputStream(OscarProperties.getInstance().getProperty("olis_returned_cert"));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) cf.generateCertificate(is);

			// I'm not sure if this is necessary

			ArrayList<Certificate> certList = new ArrayList<Certificate>();
			certList.add(cert);

			Store certs = new JcaCertStore(certList);
			
			// Encrypt data
			CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();

			// What digest algorithm i must use? SHA1? MD5? RSA?...
			ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(priv);			
			sgen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
	                     .build(sha1Signer, cert));
			
			// I'm not sure this is necessary
			sgen.addCertificates(certs);
			
			// I think that the 2nd parameter need to be false (detached form)
			CMSSignedData csd = sgen.generate(new CMSProcessableByteArray(data.getBytes()), true);
			
			byte[] signedData = csd.getEncoded();
			byte[] signedDataB64 = Base64.encode(signedData);

			result = new String(signedDataB64);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't sign HL7 message for OLIS", e);
		}
		return result;
	}

	public static String signData(String data) {
		X509Certificate cert = null;
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

			ArrayList<Certificate> certList = new ArrayList<Certificate>();
			certList.add(cert);
			
			Store certs = new JcaCertStore(certList);

			// Encrypt data
			CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();

			// What digest algorithm i must use? SHA1? MD5? RSA?...
			ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(priv);
			sgen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
	                     .build(sha1Signer, cert));
			

			// I'm not sure this is necessary
			sgen.addCertificates(certs);
			
			// I think that the 2nd parameter need to be false (detached form)
			CMSSignedData csd = sgen.generate(new CMSProcessableByteArray(data.getBytes()), true);
			
			byte[] signedData = csd.getEncoded();
			byte[] signedDataB64 = Base64.encode(signedData);

			result = new String(signedDataB64);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't sign HL7 message for OLIS", e);
		}
		return result;
	}

	private static void notifyOlisError(Provider provider, String errorMsg) {
		HashSet<String> sendToProviderList = new HashSet<String>();

		String providerNoTemp = "999998";
		sendToProviderList.add(providerNoTemp);

		if (provider != null) {
			// manual prompts always send to admin
			sendToProviderList.add(providerNoTemp);

			providerNoTemp = provider.getProviderNo();
			sendToProviderList.add(providerNoTemp);
		}

		// no one wants to hear about the problem
		if (sendToProviderList.size() == 0) return;

		String message = "OSCAR attempted to perform a fetch of OLIS data at " + new Date() + " but there was an error during the task.\n\nSee below for the error message:\n" + errorMsg;

		oscar.oscarMessenger.data.MsgMessageData messageData = new oscar.oscarMessenger.data.MsgMessageData();

		ArrayList<MsgProviderData> sendToProviderListData = new ArrayList<MsgProviderData>();
		for (String providerNo : sendToProviderList) {
			MsgProviderData mpd = new MsgProviderData();
			mpd.providerNo = providerNo;
			mpd.locationId = "145";
			sendToProviderListData.add(mpd);
		}

		String sentToString = messageData.createSentToString(sendToProviderListData);
		messageData.sendMessage2(message, "OLIS Retrieval Error", "System", sentToString, "-1", sendToProviderListData, null, null, OscarMsgType.GENERAL_TYPE);
	}

	static void writeToFile(String data) {
		try {
			File tempFile = new File(System.getProperty("java.io.tmpdir") + (Math.random() * 100) + ".xml");
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			pw.println(data);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}
}
