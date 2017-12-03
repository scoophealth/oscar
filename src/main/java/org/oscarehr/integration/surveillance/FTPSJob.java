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
package org.oscarehr.integration.surveillance;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.SurveillanceDataDao;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.SurveillanceData;
import org.oscarehr.util.MiscUtils;
//import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;
import oscar.oscarSurveillance.ProcessSurveyFile;


//import oscar.oscarSurveillance.ProcessSurveyFile;

public class FTPSJob implements OscarRunnable {

	private Logger logger = MiscUtils.getLogger();

	private Provider provider;
	private Security security;
	private String config = null;

	private SurveillanceDataDao surveillanceDataDao = SpringUtils.getBean(SurveillanceDataDao.class);
	/*
	 config is stored as json document
	 {domain:localhost, port: 21, username: "text", password= "",type="FTPS",surveyId=""}
	  
	 */

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
	
		//Parse Extra Data
		logger.debug("config: "+config);
		JSONObject json = JSONObject.fromObject(config);
		String surveyId = json.getString("surveyId");
		String domain = json.getString("domain");
		int port = json.getInt("port");
		String username = json.getString("username");
		String password = json.getString("password");
		
		ProcessSurveyFile.processSurveyFile(surveyId); //Run file generator
		
		//Check if there is anything to send
		List<SurveillanceData> listToSend = surveillanceDataDao.findUnSentBySurveyId(surveyId);
		if(listToSend.size() > 0) {
			try{
				//Login to FTPS server
				String protocol = "SSL";    // SSL/TLS
		        FTPSClient ftps = new FTPSClient(protocol);
		        ftps.setDefaultPort(port);
		        ftps.connect(domain);
		        
		        int reply = ftps.getReplyCode();
		        logger.error("reply code "+FTPReply.isPositiveCompletion(reply));
		        
		        boolean loggedIn  = ftps.login(username, password);
				if(loggedIn) {
					for(SurveillanceData data: listToSend) {
						ByteArrayInputStream is = new ByteArrayInputStream(data.getData());
						boolean sent = ftps.storeFile(data.getId()+"-"+data.getCreateDate().getTime(),is);
						if(sent) {//Mark what was sent
							data.setTransmissionDate(new Date());
							data.setSent(true);
							surveillanceDataDao.merge(data);
						}
						is.close();
						is = null;
					}
				}else{
					logger.error("ERROR : failed to login to domain "+domain+ " port "+port);
				}
				
				ftps.disconnect();
			}catch(Exception e) {
				logger.error("FTPS ERROR",e);
			}
			
		}
		
		
		
		
		
	}

	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;
	}

	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}


	public void setConfig(String string) {
		config = string;
		
	}

}
