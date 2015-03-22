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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.w3c.dom.Document;
import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;

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
public class HL7LabHandler {

	public static Logger logger = Logger.getLogger("ExpediusHL7LabHandler");
	
	private static final String DEFAULT_SAVE_PATH = "/var/lib/expedius/labs/";
	private static final String DOCUMENT_FORMAT = "UTF-8";
	public static final int HTTP_WEBSERVICE_ERROR = 100;
	public static final int OK = HttpsURLConnection.HTTP_OK;
	
	private Document hl7labs;
	private String fileName;
	private String providerNumber;
	private ByteArrayInputStream byteArrayInputStream;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int fileId;
	private String savePath;
	private String serviceName;
	private String labType;
	private int responseCode;

	public HL7LabHandler(){
		// default constructor
	}
	

	public HL7LabHandler(Properties properties){
		
		if(properties != null) {
			
			if(properties.containsKey("SERVICE_NUMBER")) {
				providerNumber = properties.getProperty("SERVICE_NUMBER").trim();
			}
			
			if(properties.containsKey("SERVICE_NAME")) {
				serviceName = properties.getProperty("SERVICE_NAME").trim();
			}
			
			if(properties.containsKey("LAB_TYPE")) {
				labType = properties.getProperty("LAB_TYPE").trim();
			}
			
			if(properties.containsKey("HL7_SAVE_PATH")) {
				savePath = properties.getProperty("HL7_SAVE_PATH").trim();
			}
		}
	}

	public HL7LabHandler( 
			String providerNumber
	) {
		this.hl7labs = null;
		this.fileName = null;
		this.providerNumber = providerNumber;
	}
	
	/**
	 * 
	 * @param hl7labs
	 * @param fileName
	 * @param providerNumber
	 * @param webServiceEndpoint
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public HL7LabHandler(
			Document hl7labs, 
			String fileName, 
			String providerNumber
	) {
		this.hl7labs = hl7labs;
		this.fileName = fileName;
		this.providerNumber = providerNumber;
	}

	public int getResponseCode() {
		return responseCode;
	}

	private void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getFileId() {
		return fileId;
	}

	private void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public Document getHl7labs() {
		return hl7labs;
	}

	public void setHl7labs(Document hl7labs) {
		this.hl7labs = hl7labs;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setProviderNumber(String providerNumber) {
		this.providerNumber = providerNumber;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSavePath() {
		return savePath;
	}

	public String getLabType() {
		return labType;
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}
	
	public void reset() {	
		setHl7labs(null);
		setFileName(null);
		setFileId(0);
		setProviderNumber(null);
	}

	/**
	 * The server root directory where downloaded HL7 files will 
	 * be saved.
	 * @param savePath
	 */
	public void setSavePath(String savePath) {
		
		if( (savePath == null) || (savePath.equals(" ")) ) {
			
			if( ! confirmDirectory(DEFAULT_SAVE_PATH, null) ) {				
				createDirectory(DEFAULT_SAVE_PATH);				
			}
			
			this.savePath = DEFAULT_SAVE_PATH;

		} else {
	
			if( ! savePath.endsWith("/") ) {
				savePath = savePath + "/";
			}
			
			
			if( ! confirmDirectory(savePath, null) ) {			
				createDirectory(savePath);
			}
				
			logger.info("Setting file save path " + savePath);
			
			this.savePath = savePath;			
		}	
	}
	
	/** 
	 * Complementary to save path.
	 * @param directoryPath
	 */
	private void createDirectory(String directoryPath) {
		
		File newDirectory = new File(directoryPath);
		
		if( newDirectory.mkdir() ) {
			logger.info("New lab save directory created at " + newDirectory.getAbsolutePath());
		} else {
			logger.log(Level.ERROR, "Failed to create new save directory.");
		}
	}

	
	
	/**
	 * Overloaded super method to simplify trigger. 
	 * This overload returns an HTTP server status of 200 for OK 
	 * and 100 for error. The super method returns boolean.  
	 * @return ExpediusHL7LabHandler.HTTP_WEBSERVICE_ERROR [100] for error. ExpediusHL7LabHandler.OK [200] for success.
	 * @throws RemoteException
	 */
	public void parseHL7() {

		setResponseCode(HTTP_WEBSERVICE_ERROR);
		
		String serviceName = this.getServiceName();
		String savePath = this.getSavePath();
		int fileId = this.getFileId();
		String labType = this.getLabType();
		String fileName = this.getFileName();
		
		if( (serviceName != null) && 
				(savePath != null) && 
					(fileId > 0) && 
						(fileName != null) &&
							(labType != null) ) {

			MessageHandler msgHandler = HandlerClassFactory.getHandler(labType);
			LoggedInInfo loggedininfo = new LoggedInInfo();

			if(msgHandler.parse( loggedininfo, serviceName, savePath + fileName, fileId, "127.0.0.1") != null) {
				setResponseCode(OK);
			}

		}

	}

	/**
	 * Overloaded to allow quick save based on settings.
	 * @return
	 * @throws TransformerException
	 * @throws IOException
	 * @throws RemoteException
	 */
	public void saveHL7() {

		if(this.getSavePath() != null) {
			saveHL7(this.getSavePath());
		} else {
			saveHL7(DEFAULT_SAVE_PATH);
		}

	}

	/**
	 * Parse and persist HL7 file to Oscar database for viewing in 
	 * Oscars inbox.
	 * 
	 * @return False if file failed save. 
	 * @throws TransformerException 
	 * @throws IOException 
	 */
	public void saveHL7(String filePath) {

		setResponseCode(HTTP_WEBSERVICE_ERROR);
		String savedFilePath = null;
		int oscarFileId = 0;
		File file = null;

		// tell Oscar where the file is - check if it has been uploaded in the past.
		if( (this.getFileName() != null) && 
				(this.confirmDirectory(filePath, this.getFileName())) ) {

			savedFilePath = new String(filePath + this.getFileName());
			
			logger.debug("HL7 lab file Path: " + savedFilePath);
			logger.debug("Service Number (provider)" + providerNumber);
			
			file = new File(savedFilePath);
			try {
	            inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
            	logger.error("Failed to save file:  " + savedFilePath);
            }
			
			oscarFileId = FileUploadCheck.addFile(file.getName(), inputStream, providerNumber);

			logger.debug("OSCAR returned file id: " + oscarFileId);
			
			// Oscar returns a -1 for save failure.
			// 0 is returned when connection fails.
			if(oscarFileId > 0) {				
				setResponseCode(OK);
				setFileId(oscarFileId);
			} else {					
				logger.debug("Oscar Web Service Error while persisting HL7 lab files.");
			}
			
			close();
		} else { 
			logger.debug("Failed to locate saved file.");				
		}

	}
	
	/**
	 * Overloaded to accept default setting in this class.
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws TransformerException 
	 */
	public void saveFile() throws IOException, TransformerException {
		saveFile(this.getSavePath());
	}
	
	/**
	 * Save downloaded HL7 to local disk space. 
	 * Save path is determined in the properties file.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws TransformerException 
	 */
	public void saveFile(String filePath) throws IOException, TransformerException {

		setResponseCode(HTTP_WEBSERVICE_ERROR);
		
		TransformerFactory transformerFactory = null;
		Transformer transformer = null;
		DOMSource domSource = null;
		StringWriter xmlAsWriter = null;
		StreamResult streamResult = null;
		String incomingFileName = new String( getFileName() );

		setSavePath(filePath);
		
		if(incomingFileName != null) {
			
			incomingFileName = incomingFileName.trim();
			if(incomingFileName.contains("/")) {
				incomingFileName = fileName.replace("/", "").trim();
			} 
			
			incomingFileName = ( serviceName.toLowerCase() ) + "_" + ( incomingFileName.replaceAll(".enc", "") ).toLowerCase();
		}
		
		// convert Document into input stream for use with oscar utilities.
		transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		domSource = new DOMSource(this.getHl7labs());
		xmlAsWriter = new StringWriter();
		streamResult = new StreamResult(xmlAsWriter);  
		transformer.transform(domSource, streamResult); 

		logger.info("Saving lab file to: " + getSavePath() + incomingFileName);

		if(byteArrayInputStream == null) {				
			byteArrayInputStream = new ByteArrayInputStream(xmlAsWriter.toString().getBytes(DOCUMENT_FORMAT));	
		}
		
		outputStream = new FileOutputStream(getSavePath() + incomingFileName);		
		int bytesRead = 0;				
		while ((bytesRead = byteArrayInputStream.read()) != -1){
			outputStream.write(bytesRead);
		}

		// confirm the file was written.
		if( confirmDirectory(getSavePath(), incomingFileName) ) {
			// set the file name as saved.
			this.setFileName(incomingFileName);
			setResponseCode(OK);
			
			if(getResponseCode() == OK) {
				logger.info("New lab file saved.");
			}
			
		} else {
			
			this.setFileName(null);
		}
		
		close();

	}  
	

	/**
	 * Make sure the directory exists and is writable.
	 * 
	 * @param filePath
	 * @return
	 */
	private boolean confirmDirectory(String filePath, String savedFile) {		
		
		boolean allGood = Boolean.TRUE;
		
		if(filePath == null) {
			return Boolean.FALSE;
		}
		
		if(savedFile == null) {
			savedFile = "";
		}
	
		File directoryCheck = new File(filePath + savedFile);		
		if(directoryCheck.exists()) {
	
			if(! directoryCheck.canWrite()) {
				allGood = Boolean.FALSE;
			}
			
			if(! directoryCheck.canRead()) {
				allGood = Boolean.FALSE;
			}
			
		} else {
			allGood = Boolean.FALSE;
		}

		return allGood;
	}
	
	/**
	 * Close all input/output streams after every run.
	 */
	private void close() {
		
		if(byteArrayInputStream != null) {
			try {
				byteArrayInputStream.close();
			} catch (IOException e) {
				logger.log(Level.ERROR,"Closing outputstream failed", e);
			}
		}
		
		if(outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				logger.log(Level.ERROR, "Closing outputstream failed", e);
			}
		}
		
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.log(Level.ERROR, "Closing inputStream failed", e);
			}
		}
	}

}
