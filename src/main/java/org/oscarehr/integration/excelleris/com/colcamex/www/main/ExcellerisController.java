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
package org.oscarehr.integration.excelleris.com.colcamex.www.main; 

import java.io.IOException; 
import java.net.SocketTimeoutException;
import java.util.Properties;
import javax.net.ssl.*;
import org.oscarehr.integration.excelleris.com.colcamex.www.core.*;
import org.w3c.dom.Node;


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
public class ExcellerisController extends AbstractConnectionController {

	private static final String DEFAULT_EXCELLERIS_LAB_TYPE = "PATHHL7";
	//private static final String NODE_AUTHENTICATED = "authentication";
	private static final String ACK_RETURN_CODE = "0";
	private static final String RESPONSE_ACCESSGRANTED = "accessgranted";
	private static final String NODE_HL7MESSAGES = "hl7messages";
	private static final String NODE_MESSAGECOUNT = "MessageCount";
	private static final String NODE_MESSAGEFORMAT = "MessageFormat";
	private static final String NODE_VERSION = "Version";
	private static final String NODE_RETURNCODE = "ReturnCode";
	private static final String EXCELLERIS_LAB_TYPE = "PATHL7";
	
	private String labType;

	public ExcellerisController(Properties properties, ExcellerisConfigurationBean configurationBean) {		
		super(properties, configurationBean);
		
		if(super.properties.containsKey("EXCELLERIS_LAB_TYPE")) {
			this.labType = properties.getProperty("EXCELLERIS_LAB_TYPE").trim();
		} else {
			this.labType = DEFAULT_EXCELLERIS_LAB_TYPE;
		}
    }
	
	@Override
	public void run() {
		_init();
		Thread thread = Thread.currentThread();
		thread.setName("ExcellerisController"+ "[" + thread.getId() + "]");
		
		logger.debug("Running [" + thread.getName() + "]");	
		
		Node rootTag = null;
		String messageCount = null;
		String messageFormat = null;
		String messageVersion = null;
		//NamedNodeMap nodemap = null;
		//Document response = null;	
		//Node code = null;
		String ackReturnCode = null;
		setLastFileCount(0);
		
		if(hostLogin()){
			
			try {	
				// fetch data
				logger.info("Fetching Excelleris labs from " + FETCH);
				getLabConnection().fetch(FETCH);
			} catch (SocketTimeoutException e) {				
				handleError("Connection timeout occured while attempting to fetch lab files. Check internet connectivity. ", e, ERROR);
			} catch (IOException e) {				
				handleError("Expedius has failed to fetch lab files. Contact support. ", e, ERROR);
			} /*catch (ParserConfigurationException e) {				
				handleError("There was a problem with parsing the server response while fetching lab files.", e, ERROR);
			}  */ finally {
				// for maintenance.
				close();									
			}
				

			// save data
			if( getLabConnection().getResponseCode() == HttpsURLConnection.HTTP_OK ) {
					
				if(getLabConnection().hasResponse()) {
					
					rootTag = getDocumentHandler().getRoot();
					
					if( NODE_HL7MESSAGES.equalsIgnoreCase(rootTag.getNodeName()) ) {
						messageCount = getDocumentHandler().getNodeAttributeValue(NODE_MESSAGECOUNT, rootTag);
						messageFormat = getDocumentHandler().getNodeAttributeValue(NODE_MESSAGEFORMAT, rootTag);
						messageVersion = getDocumentHandler().getNodeAttributeValue(NODE_VERSION, rootTag);
						
						logger.info(NODE_MESSAGECOUNT + ": " + messageCount);
						logger.info(NODE_MESSAGEFORMAT + ": " + messageFormat);
						logger.info(NODE_VERSION + ": " + messageVersion);
					}
					
				}
				
				if( getLastFileCount() > 0 ) {
					getLabHandler().setLabType(EXCELLERIS_LAB_TYPE);
					super.processResults(getDocumentHandler().getDocument(), labType);
				}
			}
						
//						response = getLabConnection().getResponse();
//						rootTag = response.getDocumentElement();
//						
//						if(rootTag.getNodeName().equalsIgnoreCase(NODE_HL7MESSAGES)) {
//							
//							nodemap = rootTag.getAttributes();
//							
//							if(nodemap.getLength() > 0) {
//								
//								messageCount = nodemap.getNamedItem(NODE_MESSAGECOUNT).getNodeValue();
//								messageFormat = nodemap.getNamedItem(NODE_MESSAGEFORMAT).getNodeValue();
//								messageVersion = nodemap.getNamedItem(NODE_VERSION).getNodeValue();
//								
//								logger.info(NODE_MESSAGECOUNT + ": " + messageCount);
//								logger.info(NODE_MESSAGEFORMAT + ": " + messageFormat);
//								logger.info(NODE_VERSION + ": " + messageVersion);
//								
//								if(messageCount != null) {
//									setLastFileCount(Integer.parseInt(messageCount));									
//									logger.info( messageCount + " Excelleris labs downloaded");
//								}
//
//							}
//						
//						} else {
//							logger.error("Error: No, or incorrect, content from server. Root Tag: "+rootTag.toString());
//						}
//					}
					
					
							
			//}

			// CAUTION - disable acknowledge for testing. You will loose all your test labs.
			if( (ACKNOWLEDGE_DOWNLOADS.equalsIgnoreCase("true")) && (getLabHandler().getResponseCode() == HttpsURLConnection.HTTP_OK) ) {
				try {
					getLabConnection().acknowledge(ACKNOWLEDGE);
				} catch (SocketTimeoutException e) {				
					handleError("Connection timeout occured while attempting to fetch lab files. Check internet connectivity. ", e, ERROR);
				} catch (IOException e) {				
					handleError("Expedius has failed to fetch lab files. Contact support. ", e, ERROR);
				} 
				
				if(getLabConnection().hasResponse()) {	
					
					ackReturnCode = getDocumentHandler().getNodeAttributeValue(NODE_RETURNCODE, getDocumentHandler().getRoot());
					
					if( ACK_RETURN_CODE.equalsIgnoreCase(ackReturnCode) ) {
						logger.info("All Labs Acknowledged: " + ackReturnCode);
					} else {
						handleError("Expedius has failed to acknowledge the last lab download. "
								+ "This could result in multiple copies of the same lab. Excelleris server acknowledge return code was "
								+ ackReturnCode + "If this error does not resolve in 24 hours, contact support.",
								null, DISMISSABLE_ERROR);
					}
				}
			}
//						
//							rootTag = getLabConnection().getResponse().getDocumentElement();		
//							nodemap = rootTag.getAttributes();
//							
//							if(nodemap.getLength() > 0) {
//								
//								code = nodemap.getNamedItem(NODE_RETURNCODE);
//								
//								if(Integer.parseInt(code.getNodeValue().toString()) == 0) {								
//									
//								} else {								
//									logger.error(rootTag.toString() + " Invalid response code");								
//								}
//								
//							} else {							
//								
//							}
//						}
//						
//					} else {
//						
//					}
//					
//				} else {
//					logger.info("Acknowledge not enabled. This lab file will be downloaded again.");
//				}
				
			// catch all server response codes.
			processServerResponse(getLabConnection().getResponseCode());
															
//			} catch (SocketTimeoutException e) {				
//				handleError("Connection timeout occured while attempting to fetch lab files. Check internet connectivity. ", e, ERROR);
//			} catch (IOException e) {				
//				handleError("Expedius has failed to fetch lab files. Contact support. ", e, ERROR);
//			} catch (ParserConfigurationException e) {				
//				handleError("There was a problem with parsing the server response while fetching lab files.", e, ERROR);
//			}  finally {
//				close();									
//			}		
		}
	}	
	
	/**
	 * Login method. Used on every connection and whenever the server needs to be verified.
	 */
	@Override
	protected boolean hostLogin() {

		boolean success = Boolean.FALSE;
		//Document response = null;
		//String method = null;
		String result = null;
		//Node rootTag = null;		
		SSLSocketFactory socketFactory = super.getSSLSocketFactory();
		
		if(socketFactory != null) {
			
			if(getLabConnection() == null) {
				handleError("Missing connection information. Service not run.", null, ERROR);	
			}

			// connect - sets up the security keys and session cookie. A handshake.
			try {
				getLabConnection().connect(URI, socketFactory);
			} catch (SocketTimeoutException e1) {
				handleError("Connection timeout occured during handshake. Check internet connectivity and try again. ", e1, ERROR);
			} catch (IOException e2) { 										
				handleError("Server is not recognized or cannot be reached during handshake. Check connection links or if server is accessable. ", e2, ERROR);			
			}
						
			// authenticate - verify login information and open portal.
			// but only after a handshake success.
			if(getLabConnection().getResponseCode() == HttpsURLConnection.HTTP_OK) {
				
				try {
					getLabConnection().login(USER, PASS, LOGIN);
				} catch (SocketTimeoutException e3) {					
					handleError("Connection timeout occured while logging-in. Check internet connectivity and try again. ", e3, ERROR);
				} catch (IOException e4) {					
					handleError("Connection failure while logging-in. Check internet connectivity and try again. ", e4, ERROR);
				}
				
			}

			if(getLabConnection().getResponseCode() == HttpsURLConnection.HTTP_OK) {
				
				if(getLabConnection().hasResponse()) {
					
					result = getDocumentHandler().getRoot().getFirstChild().getNodeValue();
					
//					response = getLabConnection().getResponse();					
//					rootTag = response.getDocumentElement();
//					method = rootTag.getNodeName();
//					result = rootTag.getFirstChild().getNodeValue();
//					
					logger.info("Excelleris login response: " + result);
									
					if(result.equalsIgnoreCase(RESPONSE_ACCESSGRANTED)) {
						success = Boolean.TRUE;
						getLabConnection().setLoggedIn(true);
					} else {
						logger.info("Failed to log into Excelleris.");
					}
					
				}								
			}
			
			if(getLabConnection().getResponseCode() > HttpsURLConnection.HTTP_OK) {
				processServerResponse(getLabConnection().getResponseCode());
			}
	
		} 
		
		return success;
		
	}

	public boolean start() {		
		return super.start(DOWNLOAD_MODE);
	}
	
	@Override
	public void setConnection(Connect connection) {
		this.connection = connection;		
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;		
	}

	@Override
	protected void setLastFileCount(int lastFileCount) {
		this.lastFileCount = lastFileCount;
		
	}

	@Override
	public void setDocumentHandler(W3CDocumentHandler documentHandler) {
		this.documentHandler = documentHandler;			
	}		   
}
