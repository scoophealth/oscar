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
package org.oscarehr.integration.excelleris.com.colcamex.www.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Level;
import org.oscarehr.integration.excelleris.com.colcamex.www.main.*;
import org.oscarehr.integration.excelleris.com.colcamex.www.security.SSLSocket;
import org.w3c.dom.Document;

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
public abstract class AbstractConnectionController implements Runnable {

	protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("ExpediusConnectionController");
	protected static Logger customLogger = null;
	
	public static final int DOWNLOAD_MODE = 1;
	public static final int LOGIN_MODE = 2;
	protected static final int ERROR = 1;
	protected static final int DISMISSABLE_ERROR = 2;
	
	protected static String  TRUSTSTORE_URL ;
	protected static String  STORE_TYPE ;
	protected static String  STORE_PASS ;
	protected static String  HTTPS_PROTOCOL ;
	protected static String  KEYSTORE_URL ;
	protected static String  USER;
	protected static String  PASS;	
	protected static String  URI;
	protected static String  LOGIN;	
	protected static String  FETCH;
	protected static String  ACKNOWLEDGE;
	protected static String  LOGOUT;
	protected static String  ACKNOWLEDGE_DOWNLOADS;	
	
	protected boolean controllerStatus;
    protected Connect connection;
    protected SSLSocket socket;
    protected HL7LabHandler labHandler;
    protected ExcellerisConfigurationBean configurationBean;
    protected String serviceName;
    protected Properties properties;
    protected MessageHandler messageHandler;
    protected int lastFileCount;
    protected W3CDocumentHandler documentHandler;
    protected String labType;

    protected abstract boolean hostLogin();
    protected abstract void setLastFileCount(int lastFileCount);
    public abstract boolean start();
    public abstract void setConnection(Connect connection); 
    public abstract void setServiceName(String serviceName);
    public abstract String getServiceName(); 
    public abstract void setMessageHandler(MessageHandler messageHandler);
    public abstract void setDocumentHandler(W3CDocumentHandler documentHandler);
      
    public AbstractConnectionController(Properties properties, ExcellerisConfigurationBean configurationBean) {
    	
    	// contains dynamic properties
    	if(configurationBean != null) {
    		setConfigurationBean(configurationBean);
    	}
    	
    	// contains static properties
    	if(properties != null) {

    		this.properties = properties;
    		
			TRUSTSTORE_URL = properties.getProperty("TRUSTSTORE_URL").trim();
			STORE_TYPE = properties.getProperty("STORE_TYPE").trim();
			STORE_PASS = properties.getProperty("STORE_PASS").trim();
			HTTPS_PROTOCOL = properties.getProperty("HTTPS_PROTOCOL").trim();
			KEYSTORE_URL = properties.getProperty("KEYSTORE_URL").trim();
			ACKNOWLEDGE_DOWNLOADS = properties.getProperty("ACKNOWLEDGE_DOWNLOADS").trim();
			
			setSocket(SSLSocket.getInstance(
					TRUSTSTORE_URL, 
					STORE_TYPE, 
					STORE_PASS, 
					HTTPS_PROTOCOL, 
					KEYSTORE_URL));
        } 
    	
    	_init();
    }
    
    /**
     * Initialize connection parameters from configuration bean.
     */
	protected void _init() {
		
		if(configurationBean != null) {
			
			USER = configurationBean.getUserName();
			PASS = configurationBean.getPassword();		
			URI = configurationBean.getServicePath();
			LOGIN = configurationBean.getLoginPath();	
			FETCH = configurationBean.getFetchPath();
			ACKNOWLEDGE = configurationBean.getAcknowledgePath();
			LOGOUT = configurationBean.getLogoutPath();    	

		} else {
			logger.error("Missing configuration information.");
			return;
		}
	}
	
    public void setConfigurationBean(ExcellerisConfigurationBean configurationBean) {
		logger.info("Setting configuration info.");
		this.configurationBean = configurationBean;		
	}
 
    public ExcellerisConfigurationBean getConfigurationBean() {
    	return configurationBean;
    }

	protected Connect getLabConnection() {
		return connection;
	}

	protected HL7LabHandler getLabHandler() {
		return labHandler;
	}

	public void setLabHandler(HL7LabHandler labHandler) {
		this.labHandler = labHandler;
	}
	
	public MessageHandler getMessageHandler() {
		return this.messageHandler;
	}
	
	public SSLSocket getSSLSocket() {
		return this.socket;
	}
	
	public void setSocket(SSLSocket socket) {
		if(socket != null) {
			this.socket = socket;
		}
	}
	
	/**
	 * The number of labs last retrieved.
	 * @return number of labs.
	 */
	public int getLastFileCount() {
		return lastFileCount;
	}
	
	public W3CDocumentHandler getDocumentHandler() {
		return this.documentHandler;
	}

    /**
     * Connection controllers start method executes the run method in 
     * all service controllers.
     * @param mode
     * @return
     */
    public boolean start(int mode) {

		boolean status = false;
		
		if(mode == LOGIN_MODE) {
			status = hostLogin();
			close();
		} else if (mode == DOWNLOAD_MODE) {
			run();
			status = true;
		}
		
		return status;
	}
    
    /**
     * close all streams and objects.
     */
    public void close() {
			
    	try {
    		if(getLabConnection() != null) {
	    		
				if( LOGOUT != null ){				
					getLabConnection().logout(LOGOUT);			
				}

				getLabConnection().close();	
    		}
  
		} catch (IOException e) {	
			handleError("Error closing connections during log-out. ", e, ERROR);				
		}
			
	}

    /**
	 * SSL Socket Factor for secure connection socket.
	 * @return
	 */
	protected SSLSocketFactory getSSLSocketFactory() {

		SSLSocketFactory socketFactory = null;
		String message = "Socket creation error during handshake. Failed to build custom Socket Factory "; 
		
		try {
			socketFactory = this.getSSLSocket().getSocketFactory();
		} catch (UnrecoverableKeyException e) {
			handleError(message, e, ERROR);
		} catch (KeyManagementException e) {
			handleError(message, e, ERROR);
		} catch (KeyStoreException e) {
			handleError(message, e, ERROR);
		} catch (NoSuchAlgorithmException e) {
			handleError(message, e, ERROR);
		} catch (CertificateException e) {
			handleError(message, e, ERROR);
		} catch (FileNotFoundException e) {
			handleError(message, e, ERROR);
		} catch (NoSuchProviderException e) {
			handleError(message, e, ERROR);
		} catch (IOException e) {
			handleError(message, e, ERROR);
		} finally {
			message = null;
		}

		return socketFactory;
	}
	
	/**
	 * SSL Context for secure server connections.
	 * @return
	 */
	protected SSLContext getSSLContext() {
		
		SSLContext socket = null;
		String message = "Socket creation error during handshake. The security certificate could be expired or corrupted. "; 
		
		try {
			socket = getSSLSocket().getSSlContext();
		} catch (UnrecoverableKeyException e) {
			handleError(message, e, ERROR);
		} catch (KeyManagementException e) {
			handleError(message, e, ERROR);
		} catch (KeyStoreException e) {
			handleError(message, e, ERROR);
		} catch (NoSuchAlgorithmException e) {
			handleError(message, e, ERROR);
		} catch (CertificateException e) {
			handleError(message, e, ERROR);
		} catch (IOException e) {
			handleError(message, e, ERROR);
		} catch (NoSuchProviderException e) {
			handleError(message, e, ERROR);
		} finally {
			message = null;
		}
		
		return socket;
		
	}
	
	/**
	 * Interpret server responses and trigger errors.
	 * Catches uncaught exceptions by reading server return status'
	 * @param serverResponse
	 */
	protected void processServerResponse(int serverResponse) {
		
		if(serverResponse == HttpsURLConnection.HTTP_GATEWAY_TIMEOUT) {
			handleError("A gateway timeout occurred while attempting to connect. Try again later. ", null, ERROR);
		}
		
		else if(serverResponse == HttpsURLConnection.HTTP_NOT_FOUND) {
			handleError("The server cannot be found. Ensure that the server address is correct. ", null, ERROR);
		}
		
		else if(serverResponse == HttpsURLConnection.HTTP_UNAUTHORIZED) {
			handleError("Failed authentication for this service. Ensure login data is correct. ", null, ERROR);				
		}
		
		else if(serverResponse == HttpsURLConnection.HTTP_INTERNAL_ERROR) {
			handleError("An internal error in Expedius server ceased operation. ", null, ERROR);	
		}
		
		else if(serverResponse == HttpsURLConnection.HTTP_FORBIDDEN) {			
			handleError("Failed authentication for this service. Access was forbidden. Ensure login data and security certificates are correct.", null, ERROR);			
		}
		
		else if(serverResponse == HttpsURLConnection.HTTP_UNAVAILABLE) {	
			handleError("Server for lab service is unavailable. Check network status. ", null, ERROR);			
		}
		
		else if(serverResponse == HL7LabHandler.HTTP_WEBSERVICE_ERROR) {
			handleError("The server process has been halted due to an unknown communication error with Oscar. ", null, DISMISSABLE_ERROR);
		}
		
		else if(serverResponse > HttpsURLConnection.HTTP_NO_CONTENT) {
			handleError("The server process has been halted due to communication error. ", null, ERROR);
		}

	}
	
	protected void processResults() {
		processResults(getDocumentHandler().getDocument(), getLabType()); 
	}
	
	protected void processResults(String labType) {
		setLabType(labType);
		processResults(getDocumentHandler().getDocument(), getLabType()); 
	}
	
	/**
	 * Do all actions to get HL7 files into Oscar.
	 * This method changes the HTTP status to ExpediusConnectionController.HTTP_ERROR_THROWN [-1] 
	 * so that 
	 * @param results
	 */
	protected void processResults(Document results, String labType) {

		setLastFileCount( getDocumentHandler().getMessageCount() );
		setLabType(labType);
		
		logger.info( getLastFileCount()  + " " + getLabType() + " lab files found." );
		
		String savePath = null;
		
		// exit if no lab files were downloaded.
		if ( getLastFileCount() > 0 ) {

			if( getLabHandler() != null ) {
				
				logger.info("Saving and parsing lab files.");
				
				// set the local file system save path for the hl7 file.
				getLabHandler().setFileName(getConfigurationBean().getServiceName() + "." + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				savePath = getLabHandler().getSavePath();
				savePath = savePath + getConfigurationBean().getServiceName(); 
				
				// point lab results to the handler.
				getLabHandler().setHl7labs(results);
				
				// save the down loaded lab file	
				try {					 
					getLabHandler().saveFile(savePath); 	
				} catch (IOException e) {
					handleError("Expedius has failed to save downloaded lab files. They will not be aknowledged. Contact support. ", e, ERROR);
				} catch (TransformerException e) {								
					handleError("There was a problem parsing the lab files into XML. They have not been saved and will not be aknowledged. ", e, ERROR);				
				} finally {
					if( getLabHandler().getResponseCode() == HL7LabHandler.OK ) {	
						logger.info(getServiceName() + " lab files have been saved to local file system: " + savePath);			
					}
				}
						
				// parse and persist HL7 file to Oscars database.
				parseAndPersist();
				
			} else {
				handleError("A software error prevented the lab file from being saved ", null, DISMISSABLE_ERROR);
			}
			
		} 
	}
	
	public void parseAndPersist(String labType) {
		setLabType(labType);
		parseAndPersist();
	}
	
	private void parseAndPersist() {
		
		if( getLabHandler().getResponseCode() == HttpsURLConnection.HTTP_OK ) {
			
			// tell the lab handler what type of labs to send to Oscar.
			getLabHandler().setLabType(getLabType());

			// step one
			getLabHandler().saveHL7();
			// step two
			getLabHandler().parseHL7();

			if(getLabHandler().getResponseCode() == HL7LabHandler.OK) {					
				logger.info("All " + getServiceName() + " lab Files Transfered and Parsed to Oscar successfully.");
				// reset lab handler.
				getLabHandler().reset();
			}
		
		}
	}

	/**
	 * Processes errors and exceptions by creating a log entry, sending a user GUI message, 
	 * and alerting user by email.
	 * 
	 * @param message
	 * @param exception
	 * @param errorLevel
	 * @param sendEmail
	 */
	protected void handleError(String message, Exception exception, int errorLevel) {
	
		String fileLocation = getLabHandler().getSavePath();
		Integer oscarFileId = getLabHandler().getFileId();
		String fileName = getLabHandler().getFileName();
		String serviceName = getServiceName();
		
		StringBuilder finalMessage = new StringBuilder();
		
		if(serviceName != null) {
			finalMessage.append(" Service " + serviceName + " has failed. ");
		}
		
		if(exception != null) {
			finalMessage.append(" Due to a fatal exception error " + exception.getMessage() + ". ");
		}
		
		if(message != null) {
			finalMessage.append(message);
		}
		
		if(fileName != null) {
			finalMessage.append(" File location: " + fileLocation);
			finalMessage.append(fileName);
		}
		
		if(oscarFileId > 0) {
			finalMessage.append(" Oscar file id: " + oscarFileId);
		}

		logger.log(Level.ERROR, finalMessage.toString(), exception);
		logger.log(Level.INFO, "HTTP connection status " + getLabConnection().getResponseCode());
		logger.log(Level.INFO, "Web Service status " + getLabHandler().getResponseCode());
		
		if(errorLevel == ERROR) {
			getMessageHandler().addErrorMessage(finalMessage.toString());
		}
		
		if(errorLevel == DISMISSABLE_ERROR) {
			getMessageHandler().addErrorMessage(finalMessage.toString());
		}

	}
	
	public String getLabType() {
		return labType;
	}
	
	public void setLabType(String labType) {
		this.labType = labType;
	}

}
