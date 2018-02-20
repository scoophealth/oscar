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
package org.oscarehr.fax.core;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.pdf.codec.Base64;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;

public class FaxImporter {
	
	private static String PATH = "/fax";	
	private static String DOCUMENT_DIR = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
	private static String DEFAULT_USER = "-1";
	private FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
	private FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
	private QueueDocumentLinkDao queueDocumentLinkDao = SpringUtils.getBean(QueueDocumentLinkDao.class);
	private ProviderLabRoutingDao providerLabRoutingDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
	private Logger log = MiscUtils.getLogger();
	
	public void poll() {
		
		log.info( "CHECKING REMOTE FOR INCOMING FAXES" );
		
		List<FaxConfig> faxConfigList = faxConfigDao.findAll(null,null);
		DefaultHttpClient client = new DefaultHttpClient();
		
		for( FaxConfig faxConfig : faxConfigList ) {
			if( faxConfig.isActive() ) {

				Credentials credentials = new UsernamePasswordCredentials( faxConfig.getSiteUser(), faxConfig.getPasswd() );
				client.getCredentialsProvider().setCredentials( new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), credentials );

				HttpGet mGet = null;
				HttpResponse response = null;
				int status = HttpStatus.SC_OK;
				
				try {

					log.debug( "Auth: " + faxConfig.getSiteUser() + ":" + faxConfig.getPasswd() );					
					log.debug( "Service Path: " + faxConfig.getUrl() + PATH + "/" + URLEncoder.encode( faxConfig.getFaxUser(), "UTF-8" ) );
					
					mGet = new HttpGet( faxConfig.getUrl() + PATH + "/" + URLEncoder.encode( faxConfig.getFaxUser(), "UTF-8" ) );
					mGet.setHeader("accept", "application/json");
					mGet.setHeader("user", faxConfig.getFaxUser());
					mGet.setHeader("passwd", faxConfig.getFaxPasswd());

					response = client.execute(mGet);
			
					if( response != null ) {
						status = response.getStatusLine().getStatusCode();
					}				
			
					if( status == HttpStatus.SC_OK ) {
		
						HttpEntity httpEntity = response.getEntity();
	                	String content = EntityUtils.toString(httpEntity);
	
	                	log.debug("CONTENT: " + content);
	                	
	                	ObjectMapper mapper = new ObjectMapper();
	                	
	                	List<FaxJob> faxList =  mapper.readValue(content, new TypeReference<List<FaxJob>>(){});

	                	for( FaxJob receivedFax : faxList ) {	
	                		
	                		String fileName = null;
	                		EDoc edoc = null;
	                		FaxJob faxFile = null;
	  
	                		// if this recievedFax Object contains an error 
	                		// skip the download step there is no file to download.
	                		if( ! FaxJob.STATUS.ERROR.equals( receivedFax.getStatus() ) ) {
	                			faxFile = downloadFax( client, faxConfig, receivedFax );
	                		}
	                		
	                		// save the received fax to the file system and assign to an inbox Queue 
	                		if( faxFile != null ) {	 	                				                			 
	                			edoc = saveAndInsertIntoQueue( faxConfig, receivedFax, faxFile );
	                		}
	                		
	                		if( edoc != null ) {
	                			fileName = edoc.getFileName();
	                		}

	                		// The fileName variable will be NULL if the saveAndInsertIntoQueue methods fails
	                		// to fully complete. If NULL, the file will not be deleted from the Host server. 
	                		if( fileName != null ) {
	   
	                			// set the new fax into provider lab routing for tracking it's route.
	                			providerRouting( Integer.parseInt( edoc.getDocId() ) );
	                			
	                			// delete the fax on the sever.
	                			deleteFax( client, faxConfig, receivedFax );
	                			
	                		} else {
	                			fileName = FaxJob.STATUS.ERROR.name();
	                		}
	                		
	                		// this received fax may contain status errors that the 
	                		// end user needs to see. So the job should be saved to the database anyway.
                			receivedFax.setFile_name( fileName );

                			// save the receivedFax Object regardless of status or fileName.
                			saveFaxJob( new FaxJob( receivedFax ) );
	                	}
	                	
					} else {
						log.error( "HTTP Status error with HTTP code: " + status );					
					}
					
				} catch (IOException e) {
					log.error("HTTP WS CLIENT ERROR", e);

				} finally {
					if(mGet != null) {
						mGet.reset();
					}
				}
			}
		} 
		
	}
		
	private FaxJob downloadFax( DefaultHttpClient client, FaxConfig faxConfig, FaxJob fax ) {

		FaxJob downloadedFax = null;
		HttpGet mGet = null;
		
		try {
			mGet = new HttpGet(faxConfig.getUrl() + PATH + "/" 
					+ URLEncoder.encode(faxConfig.getFaxUser(),"UTF-8") + "/" 
					+ URLEncoder.encode(fax.getFile_name(),"UTF-8") );
			mGet.setHeader("accept", "application/json");
			mGet.setHeader("user", faxConfig.getFaxUser());
			mGet.setHeader("passwd", faxConfig.getFaxPasswd());
		
			HttpResponse response = client.execute(mGet);

			if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
        	
				HttpEntity httpEntity = response.getEntity();
				String content = EntityUtils.toString(httpEntity);
				
				ObjectMapper mapper = new ObjectMapper();
				
				downloadedFax = mapper.readValue( content, FaxJob.class );   
				
				fax.setStatus( downloadedFax.getStatus() );
				fax.setStatusString( downloadedFax.getStatusString() );
				
				// the fileName will be null if there is an error
				// will need to modify the receivedFax header appropriately. 
    			if( FaxJob.STATUS.ERROR.equals( downloadedFax.getStatus() ) ) {   				
    				downloadedFax = null;   				    				
    			}
			}
			
	      } catch (ClientProtocolException e) {
          	log.error("HTTP WS CLIENT ERROR", e);         
          } catch (IOException e) {
          	log.error("IO ERROR", e);
          } finally {
			if(mGet != null) {
				mGet.reset();
			}
          }

		return downloadedFax;
	}
	
	private void deleteFax( DefaultHttpClient client, FaxConfig faxConfig, FaxJob fax ) 
			throws ClientProtocolException, IOException {
		HttpDelete mDelete = new HttpDelete(faxConfig.getUrl() + PATH + "/" 
				+ URLEncoder.encode(faxConfig.getFaxUser(),"UTF-8") + "/" 
				+ URLEncoder.encode(fax.getFile_name(),"UTF-8") );
		
		mDelete.setHeader("accept", "application/json");
		mDelete.setHeader("user", faxConfig.getFaxUser());
		mDelete.setHeader("passwd", faxConfig.getFaxPasswd());
		
		log.info("Deleting Fax file " + fax.getFile_name() + " from the host server.");
		
		HttpResponse response = client.execute(mDelete);
		mDelete.reset();
	       
		if( !(response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) ) {
			log.debug("Failed to delete Fax file " + fax.getFile_name() + " from the host server.");
			throw new ClientProtocolException("CANNOT DELETE " + fax.getFile_name());
		} else {
			log.info("Fax file " + fax.getFile_name() + " has been deleted from the host server.");
		}
	}
	
	private EDoc saveAndInsertIntoQueue( FaxConfig faxConfig, FaxJob receivedFax, FaxJob faxFile ) {		 		
	
		String filename = receivedFax.getFile_name();
		
		filename = filename.replace("|", "-");
		
		if( filename.isEmpty() ) {
			filename = System.currentTimeMillis() + ".pdf";
		}
		
		filename = filename.replace(".tif", ".pdf");

		if( ! filename.endsWith(".pdf") || ! filename.endsWith(".PDF") ) {
			filename = filename + ".pdf";
		}
		
		filename = filename.trim();	

		EDoc newDoc = new EDoc("Recieved Fax", "Recieved Fax", filename, "", 
				DEFAULT_USER, DEFAULT_USER, "", 'A', 
				DateFormatUtils.format(receivedFax.getStamp(), "yyyy-MM-dd"), 
				"", "", "demographic", DEFAULT_USER, receivedFax.getNumPages() );
		
		newDoc.setDocPublic("0");
		
		filename = newDoc.getFileName();

		if( Base64.decodeToFile( faxFile.getDocument(), DOCUMENT_DIR + "/" + filename) ) {
		
			newDoc.setContentType( "application/pdf" );
			newDoc.setNumberOfPages( receivedFax.getNumPages() );
			String doc_no = EDocUtil.addDocumentSQL( newDoc );
		
			Integer queueId = faxConfig.getQueue();
			Integer docNum = Integer.parseInt( doc_no );
			
			queueDocumentLinkDao.addActiveQueueDocumentLink(queueId, docNum);
			
			log.info( "Saved file " + filename + " to filesystem at " + DOCUMENT_DIR + " as document ID " + docNum );
			
			newDoc.setDocId( doc_no );
			
			return newDoc;
		}
		
		log.debug( "Failed to save file " + filename + " to filesystem at " + DOCUMENT_DIR );
		
		return null;
		
	}

	private Integer saveFaxJob( FaxJob saveFax ) {				
		saveFax.setUser(DEFAULT_USER);
		faxJobDao.persist(saveFax);
		return saveFax.getId();
	}
	
	
	private void providerRouting( Integer labNo ) {
		ProviderLabRoutingModel providerLabRouting = new ProviderLabRoutingModel();
		providerLabRouting.setLabNo(labNo);
		providerRouting( providerLabRouting );
	}
	
	/**
	 * Put an entry in Provider Lab Routing that will cause the unclaimed lab indicator
	 * to light up next to the inbox.
	 * @return
	 */
	private void providerRouting( ProviderLabRoutingModel providerLabRouting ) {
		
		providerLabRouting.setLabType( ProviderLabRoutingDao.LAB_TYPE.DOC.name() );
		providerLabRouting.setProviderNo( ProviderLabRoutingDao.UNCLAIMED_PROVIDER );
		providerLabRouting.setStatus( ProviderLabRoutingDao.STATUS.N.name() );
		providerLabRouting.setTimestamp( new Date( System.currentTimeMillis() ) );
		providerLabRoutingDao.persist( providerLabRouting );
		
		Integer id = providerLabRouting.getId();
		if( id == null || id < 1 ) {
			log.warn("Failed to add Fax document id " + providerLabRouting.getLabNo() + " to provider lab routing.");
		}
	}

}
