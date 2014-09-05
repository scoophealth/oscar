/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.CxfClientUtilsOld;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.log.LogAction;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;

public final class SendingUtils {

	private static final Logger logger = MiscUtils.getLogger();
	private static final Integer CONNECTION_TIME_OUT = 10000;
	
	public static int send(LoggedInInfo loggedInInfo, AbstractMessage message, ProfessionalSpecialist professionalSpecialist) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, HL7Exception
	{
		return(send(loggedInInfo,message, professionalSpecialist.geteDataUrl(), professionalSpecialist.geteDataOscarKey(), professionalSpecialist.geteDataServiceKey(), professionalSpecialist.geteDataServiceName()));
	}
	
	public static int send(LoggedInInfo loggedInInfo, AbstractMessage message, String url, String publicOscarKeyString, String publicServiceKeyString, String serviceName) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, HL7Exception {
		PrivateKey publicServiceKey = getPublicServiceKey(publicServiceKeyString);
		PublicKey publicOscarKey = getPublicOscarKey(publicOscarKeyString);

		byte[] dataBytes=OscarToOscarUtils.pipeParser.encode(message).getBytes();
		
		return (send(loggedInInfo,dataBytes, url, publicOscarKey, publicServiceKey, serviceName));
	}

	public static int send(LoggedInInfo loggedInInfo, byte[] dataBytes, String url, PublicKey receiverOscarKey, PrivateKey publicServiceKey, String serviceName) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		byte[] signature = getSignature(dataBytes, publicServiceKey);
		SecretKey senderSecretKey = createSecretKey();
		byte[] encryptedBytes = encryptData(dataBytes, senderSecretKey);
		byte[] encryptedSecretKey = encryptEncryptionKey(senderSecretKey, receiverOscarKey);

		if (loggedInInfo != null) {
			Provider provider = loggedInInfo.getLoggedInProvider();
			LogAction.addLog(provider.getProviderNo(), SendingUtils.class.getSimpleName(), "HL7", new String(dataBytes, MiscUtils.DEFAULT_UTF8_ENCODING));
		} else {
			throw new IllegalStateException("Unable to post data outside authentication context. Please make sure LoggedInInfo is configured to contain non-null provider.");
		}
		
		return (postData(url, encryptedBytes, encryptedSecretKey, signature, serviceName));
	}

	private static int postData(String url, byte[] encryptedBytes, byte[] encryptedSecretKey, byte[] signature, String serviceName) throws IOException {
		MultipartEntity multipartEntity = new MultipartEntity();
		
		String filename=serviceName+'_'+System.currentTimeMillis()+".hl7";
		multipartEntity.addPart("importFile", new ByteArrayBody(encryptedBytes, filename));		
		multipartEntity.addPart("key", new StringBody(new String(Base64.encodeBase64(encryptedSecretKey), MiscUtils.DEFAULT_UTF8_ENCODING)));
		multipartEntity.addPart("signature", new StringBody(new String(Base64.encodeBase64(signature), MiscUtils.DEFAULT_UTF8_ENCODING)));
		multipartEntity.addPart("service", new StringBody(serviceName));
		multipartEntity.addPart("use_http_response_code", new StringBody("true"));

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(multipartEntity);

		HttpClient httpClient = getTrustAllHttpClient();
		httpClient.getParams().setParameter("http.connection.timeout", CONNECTION_TIME_OUT);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int statusCode=httpResponse.getStatusLine().getStatusCode();
		logger.debug("StatusCode:" + statusCode);
		return (statusCode);
	}

	private static HttpClient getTrustAllHttpClient()
	{
		try {
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        TrustManager[] temp =new TrustManager[1];
	        temp[0]=new CxfClientUtilsOld.TrustAllManager();
	        sslContext.init(null, temp, null);
	        
	        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext);
	        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        
	        HttpClient httpClient=new DefaultHttpClient();
	        ClientConnectionManager connectionManager = httpClient.getConnectionManager();
	        SchemeRegistry schemeRegistry = connectionManager.getSchemeRegistry();
	        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
	        return(new DefaultHttpClient(connectionManager, httpClient.getParams()));
        } catch (Exception e) {
	        logger.error("Unexpected error", e);
	        return(null);
        }
	}
	
	private static byte[] encryptEncryptionKey(SecretKey senderSecretKey, PublicKey receiverOscarKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, receiverOscarKey);
		return (cipher.doFinal(senderSecretKey.getEncoded()));
	}

	private static byte[] encryptData(byte[] dataBytes, SecretKey senderSecretKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(senderSecretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return (cipher.doFinal(dataBytes));
	}

	private static SecretKey createSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		return (keyGenerator.generateKey());
	}

	private static byte[] getSignature(byte[] bytes, PrivateKey receiverServiceKey) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
		Signature signature = Signature.getInstance("MD5WithRSA");
		signature.initSign(receiverServiceKey);
		signature.update(bytes);
		return (signature.sign());
	}

	public static PublicKey getPublicOscarKey(String publicOscarKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
	    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicOscarKeyString));
		KeyFactory pubKeyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicOscarKey = pubKeyFactory.generatePublic(pubKeySpec);
	    return publicOscarKey;
    }

	/**
	 * I know it returns a "private key" object but in reality it's a public key
	 * because it's a key we give out to other people.
	 */
	public static PrivateKey getPublicServiceKey(String publicServiceKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
	    PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(publicServiceKeyString));
		KeyFactory privKeyFactory = KeyFactory.getInstance("RSA");
		PrivateKey publicServiceKey = privKeyFactory.generatePrivate(privKeySpec);
	    return publicServiceKey;
    }

	public static void main(String... argv) throws Exception {
		// String url = "http://localhost:49898/oscar/lab/newLabUpload.do";
		String url = "http://localhost:8080/oscar/lab/newLabUpload.do";
		String publicOscarKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCYf7j1EXWHdbtZgn7e28Yc4a/0Mznx1irA0NW1yknJU9TScpFUVJ9LKmo3+pqAqaGkWmZgz4bn0XZQ/PJNw9z24dRwaVzOgjJ9h1ci/cmei80UK7uL7soS3c1Hj6lddkZbAJ5+F9amasRsaabFI+Gvevq0EYMIaETFjZiEkDNUwIDAQAB";
		String publicServiceKeyString = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI61yqsLH352cc+Ij3X/WzUKrFD6izRCwRqdS7RobiKmRqp6Ol1fiFJEOGGot1deNpIWJP05SNvx1qhMb9r6g8JamQvORR4QgUfKVOxwauOqm9myvSr8kPjAbQVHG8QcCBs+2qPfQU2whf+dpJFlx2P/Gx42W7S6HhK9awu9ZKeBAgMBAAECgYBRhwmBLZmQZZofNaS/hGJWqwJGQNvFv10SF0pohkBlCxjTy4AMV8dJOC/9mqUjBG+ohX4cK92zyTUYcJJ2Ryd9veIrVKM/3oUAXeHBaAHyaamFb8s6tZMHuJNJipV5igod/7nkRVGFa1RzamnMzrcnBLhqVZacwkN2F+BFzMTAAQJBAPxkQTT46O4DCbuNNxnIvMcpzIT17mhXNE+ZUOL1R2LMFM0bSyItkeiTaWQ1zgK4BPT3iAYiyvUq7fZOVGuGP9ECQQCQwBsubAM8R1STJERefMZRGAUg+UVTXatq9BK1xU9vQQCwKXBf78a+JLONQN/h8F8RXQduyyrNe0qpo7vTVAixAkA2zjJWpWI3JNO9NTns0Gkluk7d5GVjpOQIENu+nNJmgrhVnYKgJlMTtMbi6sgUUQ9KfmG8K1v1BuBrZrDwNFOxAkA9JSlWPsJPIEKVtWg8EbEkaGUiPKoQQS08DMYqiqK3eFn2EEsr+3mUsKQ4MwNfyc4e45FUN/ZovoAXkNayunjBAkAzhYBxLxjJTf7SBCjwcKus/Z0G1+mYWaKQuYWhyXhVJ7w8oNZ0KqoXECDYdeSAMEwGUkLHJjRtIFBHyJzR2vU6";

		PublicKey publicOscarKey = getPublicOscarKey(publicOscarKeyString); 
		PrivateKey publicServiceKey = getPublicServiceKey(publicServiceKeyString);
		
		byte[] bytes = "foo bar was here".getBytes(); 
		send(null, bytes, url, publicOscarKey, publicServiceKey, "server1");
	}
}
