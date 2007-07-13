/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabFileManagement
 * 
 * testClient2.java
 *
 * Created on September 22, 2005, 9:52 AM
 */

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import java.util.logging.*;
import javax.xml.parsers.*;
import org.apache.commons.httpclient.protocol.*;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.w3c.dom.*;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*; 
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Base64;


public class testClient2 {
   
   static int ConnectionRefused = 0;
   static int Success = 1;
   static int ValidationFailed = 2;
   static int Exception = 3;
   static int UploadFailed = 3;
   
   static Logger logger;
   

   /** Creates a new instance of testClient2 */
   public testClient2() {
   }
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      String URL = "http://localhost:8084/oscar/lab/newLabUpload.do";            
      // TODO code application logic here
      //load properties file
      //   should have username , password , url for uploading into OSCAR
      
      initLogger();

      String filename = "";
		String type = "";
		testClient2 client = new testClient2();

		// location of keyPair.key
		String keyLocation = "keys/keyPair.key";
		ArrayList keyPairInfo = client.parseKeyFile(keyLocation);
		String serviceName = (String) keyPairInfo.get(0);
		PrivateKey clientPrivKey = (PrivateKey) keyPairInfo.get(1);
		PublicKey serverPubKey = (PublicKey) keyPairInfo.get(2);

      try{
      	if ( args[0] == null ){ throw new Exception("Usage Exception"); }
      	   logger.info("Loading Properties file "+args[0]);
      	   filename = args[0];
			if ( args.length < 2 ){
				System.out.println("No message type specified, sending message anyways\nUsage: testClient <input file> <message type>");
	  	 	}else{
		 		type = args[1];
		 	}
           
      }catch(Exception loadingEx){
         logger.severe("Error loading properties file"+loadingEx);
         System.out.println("Usage: testClient <input file> <message type>");
         System.exit(2);
      }
         
		
		// Sign the message       
	   String signature = client.signMessage(filename, clientPrivKey);
       
		// encrypt the message and the key used to encrypt it
		SecretKey sKey = client.createSecretKey();
		File encryptedFile = client.encryptFile(filename, sKey);
		String encryptedKey = client.encryptKey(sKey, serverPubKey);
		      
      int returnCode = client.sendFileToServer(URL, encryptedFile, encryptedKey, signature, type, serviceName);
      
		if(returnCode == Success){                             
			System.out.println("SUCCESSFULLY UPLOADED");                                              
      }else if (returnCode == ConnectionRefused){                        
      	logger.severe("file :"+filename+" could not be uploaded, Connection Refused : "+URL);
      }else if (returnCode == ValidationFailed){                           
      	logger.severe("file :"+filename+" could not be uploaded, Validation Failed");                            
      }else if(returnCode == UploadFailed){
      	logger.severe("file :"+filename+" could not be uploaded");                                                        
      }else if (returnCode == Exception){                           
      	logger.severe("file :"+filename+" corrupted");
      }

   }
     
   int sendFileToServer(String URL, File f1, String sKey, String sig, String type, String serviceName){      
      int returnCode = this.ConnectionRefused;
      try{
         
	      Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
   	   Protocol.registerProtocol("https", easyhttps);
   	      
   	   HttpClient client = new HttpClient();
         
      
   	   MultipartPostMethod mPost = new MultipartPostMethod(URL);
   	   client.setConnectionTimeout(8000);
      
   	   //File f1 = new File("/home/jay/documents/labs/Apr271133");      
   	   mPost.addParameter("importFile",f1.getName(),f1);
			mPost.addParameter("key", sKey);
			mPost.addParameter("signature", sig);
   	   mPost.addParameter("type",type);
			mPost.addParameter("service", serviceName);
      
   	   int statuscode = client.executeMethod(mPost);
   	   System.out.println("StatusLine>>>"+ mPost.getStatusLine());
   	   String xml  = mPost.getResponseBodyAsString();
   	   xml = xml.trim();
      
   	   System.out.println("RESPONSE: "+xml);//mPost.getResponseBodyAsString());
   	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   	   factory.setValidating(false);
   	   Document doc = factory.newDocumentBuilder().parse(mPost.getResponseBodyAsStream());
   	   //Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
      
   	   NodeList nl = doc.getElementsByTagName("outcome");
   	   String outcome = ( (Element) nl.item(0)  ).getFirstChild().getNodeValue();
   	   System.out.println("outcome "+outcome);

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
					FileOutputStream out = new FileOutputStream("CURHST.0");
					out.write(audit.getBytes("UTF-8"));
					out.close();
				}
      	}else if (outcome.equals("exception")){
      	   returnCode = this.Exception;   
			}

			

      
      	mPost.releaseConnection();
      	
      }catch(ConnectException connEx){
         returnCode = ConnectionRefused;   
      }catch(Exception e){
         e.printStackTrace();
      }
      return returnCode;
   }

	/*
	 *	Encrypts the message so it can be transfered securely
	 */
	File encryptFile(String fileName, SecretKey skey){
		String file = fileName+".enc";		

		//Encode file
		try{
			InputStream fis = new FileInputStream(fileName);
			OutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];

      	SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			fos = new CipherOutputStream(fos, cipher);

			// Read in the cleartext bytes and write to out to encrypt
         int numRead = 0;
         while ((numRead = fis.read(buf)) >= 0) {
         	fos.write(buf, 0, numRead);
         }
			fos.close();
			fis.close();

		}catch(Exception e){
			System.out.println("Encoding error: "+e);
		}				

		return(new File(file));
		
	}

	/*
	 *	Encrypts the secret key so it can be transfered along with the message
	 */
	String encryptKey(SecretKey skey, PublicKey pubKey){

		Base64 base64 = new Base64();

		//Encode key string
		try{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherText = cipher.doFinal(skey.getEncoded());
			return(new String(base64.encode(cipherText), "ASCII"));
		}catch(Exception e){
			System.out.println("Encoding error: "+e);
			return("failed");
		}
	}

	/*
	 *	Creates the secret key used to encrypt the message
	 */
	SecretKey createSecretKey() {
		// Create key for ecryption
		try{
	 		KeyGenerator kgen = KeyGenerator.getInstance("AES");
      	kgen.init(128);
      	return(kgen.generateKey());
		}catch(Exception e){
			System.out.println("Could not create secret key: "+e);
			e.printStackTrace();
			return(null);
		}
	}

	/*
	 *	Signs the message with the given private key and returns
	 * the signature
	 */
   String signMessage(String filename, PrivateKey key){
		byte[] signature = null;		
		byte[] buf = new byte[1024];
		//byte[] fileString = FileToByteArray(filename);
		Base64 base64 = new Base64();

		try{    
			InputStream is = new FileInputStream(filename);
			Signature sig = Signature.getInstance("MD5WithRSA");
     		sig.initSign(key);
			//sig.update(fileString);	
			// Read in the message bytes and update the signature 

         int numRead = 0;
         while ((numRead = is.read(buf)) >= 0) {
            sig.update(buf, 0, numRead);
         }
			is.close();

     		signature = sig.sign();
			return(new String(base64.encode(signature), "ASCII"));
		}catch(Exception e){
			logger.severe("Could not create signature: "+e);
			return("");
		}

	   	
   }

	ArrayList parseKeyFile(String fileName){
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
			e.printStackTrace();
		}

		return(keyInfo);

	}

	byte[] FileToByteArray(String name){
		try{
			File file = new File(name);
			InputStream is = new FileInputStream(name);
			long length = file.length();

			if (length > Integer.MAX_VALUE) {
	      	System.out.println("File is too large to process");
	      	return null;
	      }

		   // Create the byte array to hold the data
	      byte[] bytes = new byte[(int)length];

	      // Read in the bytes
	      int offset = 0;
	      int numRead = 0;
	      while ( (offset < bytes.length) && ( (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) ) {
	         offset += numRead;
	      }

	      // Ensure all the bytes have been read in
	      if (offset < bytes.length) {
	         //throw new IOException("Could not completely read file " + file.getName());
				System.out.println("Could not completely read file "+file.getName());      
			}

	      is.close();
	      return bytes;
		}catch(Exception e){
			System.out.println("Could not retrieve data from file: "+e);
			e.printStackTrace();
			return null;
		}

	}
   
   static void initLogger(){
      logger = Logger.getLogger("testClient2");
        try {
            // Create an appending file handler
            boolean append = true;
            //FileHandler handler = new FileHandler("c:/Data/LabManagement.log", append);
            FileHandler handler = new FileHandler("/home/wrighd/sandbox/oscar_mcmaster/FileManagement/logs/LabManagement.log", append);
            handler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.ALL);             
            // Add to the desired logger
            //ConsoleHandler handler2 = new ConsoleHandler();                       
            logger.addHandler(handler);
            //logger.addHandler(handler2);
        } catch (IOException e) {}         
        
    }
}
