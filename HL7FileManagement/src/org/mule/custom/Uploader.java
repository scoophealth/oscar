package org.mule.custom;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*; 
import javax.crypto.spec.*;
import javax.xml.parsers.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.protocol.*;
//import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.codec.binary.Base64;

import org.w3c.dom.*;

import org.mule.impl.*;
import org.mule.umo.UMOEventContext;
import org.mule.providers.file.FileConnector;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

public class Uploader {
	private static final Log LOG = LogFactory.getLog(Uploader.class);

   	static int ConnectionRefused = 0;
   	static int Success = 1;
   	static int ValidationFailed = 2;
   	static int Exception = 3;
   	static int UploadFailed = 4;

	public void Upload(byte[] fileBytes) throws Exception{
		
		
		UMOEventContext context = RequestContext.getEventContext();
    	String fileName = context.getStringProperty( FileConnector.PROPERTY_ORIGINAL_FILENAME );
		String directory = context.getStringProperty( FileConnector.PROPERTY_DIRECTORY );
		String keyLocation = context.getStringProperty("keyLocation");
		String URL = context.getStringProperty("oscarURL");

		//String URL = "http://localhost:8084/oscar/lab/newLabUpload.do";            
		if (URL.endsWith("oscar")){
			URL = URL+"/lab/newLabUpload.do";
		}else if (URL.endsWith("oscar/")){	
			URL = URL+"lab/newLabUpload.do";
		}else if (URL.endsWith("/")){
			URL = URL+"oscar/lab/newLabUpload.do";
		}else{
			URL = URL+"/lab/newLabUpload.do";
		}
		LOG.debug("URL : " + URL );
		
		if (keyLocation.indexOf(":/") != -1){
			keyLocation = keyLocation.replaceAll("/", "\\\\");
		}else{
			keyLocation = "/"+keyLocation;
		}
		LOG.debug("KEY : " + keyLocation );
		// if returnCode is not set it is because of an exception
		int returnCode = Exception;
		try{
			InputStream is = new BufferedInputStream(new ByteArrayInputStream(fileBytes));
			is.mark(is.available()+1);

			// location of keyPair.key
			//String keyLocation = "/home/wrighd/sandbox/oscar_mcmaster/FileManagement/keys/keyPair.key";
			ArrayList keyPairInfo = parseKeyFile(keyLocation);
			String serviceName = (String) keyPairInfo.get(0);
			PrivateKey clientPrivKey = (PrivateKey) keyPairInfo.get(1);
			PublicKey serverPubKey = (PublicKey) keyPairInfo.get(2);
		
		   	// Sign the message       
			String signature = signMessage(is, clientPrivKey);
			is.reset();
	
			// Encrypt the message and the key used to encrypt it			
			SecretKey sKey = createSecretKey();
			File encryptedFile = encryptFile(is, sKey, fileName);
			is.close();

			String encryptedKey = encryptKey(sKey, serverPubKey);
		   	returnCode = sendFileToServer(URL, encryptedFile, encryptedKey, signature, serviceName, directory);
	      	encryptedFile.delete();

		}catch(Exception e){
			//System.out.println("Error: "+e);
			//e.printStackTrace();
			throw e;
		}		
		
		if(returnCode == Success){                             
			System.out.println("file: "+fileName+", Successfully Uploaded\n");                                              
      	}else if (returnCode == ConnectionRefused){                        
			throw new Exception("file: "+fileName+" could not be uploaded, Connection Refused : "+URL);
      	}else if (returnCode == ValidationFailed){                           
			throw new Exception("file: "+fileName+" could not be uploaded, Validation Failed");                           
      	}else if(returnCode == UploadFailed){
			throw new Exception("file: "+fileName+" could not be uploaded, Parsing Failed");                                                      
      	}else if (returnCode == Exception){                           
			throw new Exception("file: "+fileName+" corrupted");
      	}

	}

    
   	int sendFileToServer(String URL, File f1, String sKey, String sig, String serviceName, String dir) throws Exception{      
		
		int returnCode = this.ConnectionRefused;

	    try{
	         
		    Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", easyhttps);			   	      
		   	HttpClient client = new HttpClient();      	      
	   		MultipartPostMethod mPost = new MultipartPostMethod(URL);
	   		client.setConnectionTimeout(8000);
	      
		   	mPost.addParameter("importFile",f1.getName(),f1);
			mPost.addParameter("key", sKey);
			mPost.addParameter("signature", sig);
			mPost.addParameter("service", serviceName);
	      
		   	int statuscode = client.executeMethod(mPost);
		   	System.out.println("\nStatusLine>>>"+ mPost.getStatusLine());
	      
		   	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		   	factory.setValidating(false);
		   	Document doc = factory.newDocumentBuilder().parse(mPost.getResponseBodyAsStream());
	   	   	NodeList nl = doc.getElementsByTagName("outcome");
	   		String outcome = ( (Element) nl.item(0)  ).getFirstChild().getNodeValue();

		   	if (outcome == null){
		   		returnCode = this.Exception;
		   	}else if (outcome.equals("failed to validate")){
		    	returnCode = this.ValidationFailed;         
		    }else if (outcome.equals("upload failed")){
		        returnCode = this.UploadFailed;   
		    }else if (outcome.equals("uploaded")){
		        returnCode = this.Success;   
					
				nl = doc.getElementsByTagName("audit");
		   		String audit = ( (Element) nl.item(0)  ).getFirstChild().getNodeValue();
				
				// when uploaded successfully the only time audit will not be "success"
				// is with MDS messages when an audit of the message is returned					
				if (!audit.equals("success")){
                                        BufferedWriter out = new BufferedWriter(new FileWriter(dir+"/CURHST.0", true));
                                        out.write(audit);
                                        out.close();
				}
		    }else if (outcome.equals("exception")){
		        returnCode = this.Exception;   
			}
	      
	    	mPost.releaseConnection();
	      	
	    }catch(ConnectException connEx){
	         returnCode = ConnectionRefused;   
	    }catch(Exception e){
			 throw e;
	    }

	    return returnCode;
	}

	/*
	 *	Encrypts the message so it can be transfered securely
	 */
	File encryptFile(InputStream is, SecretKey skey, String fileName) throws Exception{

		fileName = fileName+".enc";
		//Encode file
		try{
			OutputStream fos = new FileOutputStream(fileName);
			byte[] buf = new byte[1024];

	      	SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			fos = new CipherOutputStream(fos, cipher);

			// Read in the cleartext bytes and write to out to encrypt
	        int numRead = 0;
	        while ((numRead = is.read(buf)) >= 0) {
	 	      	fos.write(buf, 0, numRead);
	        }
			fos.close();

		}catch(Exception e){
			throw e;
		}				

		return(new File(fileName));
			
	}

	/*
	 *	Encrypts the secret key so it can be transfered along with the message
	 */
	String encryptKey(SecretKey skey, PublicKey pubKey) throws Exception{

		Base64 base64 = new Base64();

		//Encode key string
		try{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherText = cipher.doFinal(skey.getEncoded());
			return(new String(base64.encode(cipherText), "ASCII"));
		}catch(Exception e){
			//return("failed");
			throw e;
		}
	}

	/*
	 *	Creates the secret key used to encrypt the message
	 */
	SecretKey createSecretKey() throws Exception {
		// Create key for ecryption
		try{
	 		KeyGenerator kgen = KeyGenerator.getInstance("AES");
	      	kgen.init(128);
	     	return(kgen.generateKey());
		}catch(Exception e){
			//return(null);
			throw e;		
		}
	}

	/*
	 *	Signs the message with the given private key and returns
	 * the signature
	 */
   	String signMessage(InputStream is, PrivateKey key) throws Exception{
		byte[] signature = null;		
		byte[] buf = new byte[1024];
		Base64 base64 = new Base64();

		try{    
			Signature sig = Signature.getInstance("MD5WithRSA");
	   		sig.initSign(key);

			// Read in the message bytes and update the signature 
	        int numRead = 0;
	        while ((numRead = is.read(buf)) >= 0) {
	        	sig.update(buf, 0, numRead);
	        }

	     	signature = sig.sign();
			return(new String(base64.encode(signature), "ASCII"));
		}catch(Exception e){
			//return("");
			throw e;		
		}
	}

	ArrayList parseKeyFile(String fileName) throws Exception{
		String serviceName = null;
		String privateKey = null;
		String publicKey = null;
		PrivateKey privKey = null;
		PublicKey pubKey = null;

		Base64 base64 = new Base64();
		ArrayList keyInfo = new ArrayList();

		try{
			InputStream input = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(input));

	        String line = null;		
			int lineCount = 0;
			while ((line = br.readLine()) != null) {
				if (lineCount == 1)
					serviceName = line;
				else if (lineCount == 4)
					privateKey = line;
				else if (lineCount == 7)
					publicKey = line;
			    lineCount++;
			}

			//create private key from string
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(base64.decode(privateKey.getBytes("ASCII")));
      		KeyFactory privKeyFactory = KeyFactory.getInstance("RSA");
			privKey = privKeyFactory.generatePrivate(privKeySpec);

			//create public key from string
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(base64.decode(publicKey.getBytes("ASCII")));
			KeyFactory pubKeyFactory = KeyFactory.getInstance("RSA");
			pubKey = pubKeyFactory.generatePublic(pubKeySpec);

			keyInfo.add(serviceName);
			keyInfo.add(privKey);
			keyInfo.add(pubKey);
		}catch(Exception e){
			System.out.println("Could not get key info from file '"+fileName+"': "+e);
			throw e;
		}

		return(keyInfo);

	}

}
