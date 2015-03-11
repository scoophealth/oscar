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
package org.oscarehr.integration.excelleris.com.colcamex.www.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;
/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class SSLSocket {
	
	private Logger logger = Logger.getLogger("SSLSocket");
	
	private final String DEFAULT_PROVIDER = "SunJSSE";
	private static final String DEFAULT_FACTORY_TYPE = "SunX509";
	private static final String DEFAULT_SSL_TYPE = "TLSv1";
	private final String DEFAULT_STORE_TYPE = "JKS";
	
	private String storeType; 
	private String pass; 
	private String keySource;
	private String trustSource;
	private String httpsProtocol;
	private String trustFactoryType;
	private static SSLSocket instance = null;
	
	/**
	 * Default constructor.
	 */
	protected SSLSocket(){
		// default constructor.
	}
	
	/**
	 * Constructor.
	 * @param trustSource
	 * @param trustStoreType
	 * @param pass
	 * @param httpsProtocol
	 * @param keySource
	 */
	protected SSLSocket(
			String trustSource, 
			String storeType, 
			String pass, 
			String httpsProtocol,
			String keySource
	){
		
		this.setStoreType(storeType);
		logger.info("Setting SSLSocket with security protocol: " + httpsProtocol);
		this.setHttpsProtocol(httpsProtocol);
		this.setPass(pass);
		logger.debug("Setting SSLSocket with key store source: " + keySource);
		this.setKeySource(keySource);
		logger.debug("Setting SSLSocket with trust store source: " + trustSource);
		this.setTrustSource(trustSource);

		//Use this if Expedius is the only SSL connection in this server instance.
		//This will reset the server defaults.
//		this.setPass(pass);
//		this.setKeySource(keySource);
//		System.setProperty("javax.net.ssl.trustStore", trustSource);
//		System.setProperty("javax.net.ssl.trustStoreType", DEFAULT_STORE_TYPE);
//		System.setProperty("javax.net.ssl.trustStorePassword", pass); 
//		logger.info("Setting SSLSocket with security protocol: " + httpsProtocol);
//		System.setProperty("https.protocols", httpsProtocol);	
	}
	
	public static synchronized SSLSocket getInstance(
			String trustSource, 
			String storeType, 
			String pass, 
			String httpsProtocol,
			String keySource) { 
		// instantiate
		if(instance == null) {		
			instance = new SSLSocket(trustSource, 
					storeType, 
					pass, 
					httpsProtocol,
					keySource);
		} else {
			instance = null;
			instance = new SSLSocket(trustSource, 
					storeType, 
					pass, 
					httpsProtocol,
					keySource);
		}
		return instance;
	}
	
	
	/**
	 * Creates a socket for binding to the HTTPS connection.
	 * 
	 * @return SSLContext 
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchProviderException 
	 */
	public synchronized SSLContext getSSlContext() throws UnrecoverableKeyException, 
		KeyStoreException, 
		NoSuchAlgorithmException, 
		CertificateException, 
		FileNotFoundException, 
		IOException, 
		KeyManagementException, NoSuchProviderException {
		
			if( (this.getHttpsProtocol() == null) || (this.getHttpsProtocol().isEmpty()) ) {
				this.setHttpsProtocol(DEFAULT_SSL_TYPE);
			}

			SSLContext sslContext = SSLContext.getInstance( this.getHttpsProtocol(), DEFAULT_PROVIDER ); 
            sslContext.init(getKeyManagers(), getTrustManagers(), new SecureRandom());

			return sslContext;		
	}
	
	public synchronized SSLSocketFactory getSocketFactory() throws UnrecoverableKeyException, 
		KeyManagementException, 
		KeyStoreException, 
		NoSuchAlgorithmException, 
		CertificateException, 
		FileNotFoundException, 
		NoSuchProviderException, 
		IOException {

		// remove all SSL cipher suites. Excelleris prefers TLSv1.
		SSLSocketFactory socketFactory = this.getSSlContext().getSocketFactory();
		String[] cipherSuites = socketFactory.getDefaultCipherSuites();
		ArrayList<String> cipherSuiteList = new ArrayList<String>();
		for(int i = 0; i < cipherSuites.length; i++) {
			if(! cipherSuites[i].contains("SSL")) {
				logger.debug("Adding cipher suite: " + cipherSuites[i]);
				cipherSuiteList.add(cipherSuites[i]);
			}
		}
		cipherSuites = new String[cipherSuiteList.size()];
		cipherSuites = cipherSuiteList.toArray(cipherSuites);		

		return new SSLSocketFactoryWrapper(socketFactory, new String[]{this.getHttpsProtocol()}, cipherSuites);
	}
	
	private KeyManager[] getKeyManagers() throws KeyStoreException, 
		NoSuchAlgorithmException, 
		CertificateException, IOException, 
		UnrecoverableKeyException {
		
		// keys
		KeyStore ks = KeyStore.getInstance(this.getStoreType());  					
		InputStream keySourceStream = new FileInputStream(this.getKeySource());		
		ks.load(keySourceStream, this.getPass().toCharArray());
		
		logger.debug("Setting SSLSocket with key store source: " + this.getKeySource());

		keySourceStream.close();
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(this.getTrustFactoryType());           
		kmf.init(ks, this.getPass().toCharArray());
		
		return kmf.getKeyManagers();
	}
	
	/**
	 * Create a custom trust store
	 * @return
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 */
	private TrustManager[] getTrustManagers() throws KeyStoreException, 
		NoSuchAlgorithmException, CertificateException, 
		FileNotFoundException, IOException {
		
		//trust 
		KeyStore trustKeyStore = KeyStore.getInstance(this.getStoreType());
		trustKeyStore.load(new FileInputStream(this.getTrustSource()), this.getPass().toCharArray());
        
        logger.debug("Setting SSLSocket with trust store source: " + this.getTrustSource());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(this.getTrustFactoryType());
        tmf.init(trustKeyStore);
        
        return tmf.getTrustManagers();
	}

	public String getStoreType() {
		if(this.storeType == null) {
			return DEFAULT_STORE_TYPE;
		}
		return storeType;
	}

	private void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getPass() {
		return pass;
	}

	private void setPass(String pass) {
		this.pass = pass;
	}

	public String getKeySource() {
		return keySource;
	}

	private void setKeySource(String keySource) {
		this.keySource = keySource;
	}

	public String getTrustSource() {
		return trustSource;
	}

	private void setTrustSource(String trustSource) {
		this.trustSource = trustSource;
	}

	public String getHttpsProtocol() {
		if(this.httpsProtocol == null) {
			return DEFAULT_SSL_TYPE;
		}
		return httpsProtocol;
	}

	private void setHttpsProtocol(String httpsProtocol) {
		this.httpsProtocol = httpsProtocol;
	}

	public String getTrustFactoryType() {
		if(this.trustFactoryType == null) {
			return DEFAULT_FACTORY_TYPE;
		}
		return trustFactoryType;
	}

	public void setTrustFactoryType(String trustFactoryType) {
		this.trustFactoryType = trustFactoryType;
	}

}
