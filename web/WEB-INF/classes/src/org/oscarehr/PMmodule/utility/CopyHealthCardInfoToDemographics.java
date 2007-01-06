/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CopyHealthCardInfoToDemographics {
	protected final Log log = LogFactory.getLog(getClass());
	
    protected ApplicationContext ctx = null;
    protected DataSource ds;
    protected Connection conn;
    
    
    public CopyHealthCardInfoToDemographics() throws Exception {
    	int totalUpdated = 0;
    	int totalWarnings = 0;
    	
    	String[] paths = {"/WEB-INF/applicationContext.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    
    	IntakeAManager intakeMgr = (IntakeAManager)ctx.getBean("intakeAManager");
    	ClientManager clientMgr = (ClientManager)ctx.getBean("clientManager");
    	
    	ds = (DataSource)ctx.getBean("dataSource");
    	conn = ds.getConnection();
 
    	Statement stmt = conn.createStatement();
    	stmt.execute("SELECT demographic_no FROM demographic");
    	ResultSet rs = stmt.getResultSet();
    	while(rs.next()) {
    		int demographicNo = rs.getInt("demographic_no");
    		Formintakea intake = intakeMgr.getCurrIntakeAByDemographicNo(String.valueOf(demographicNo));
    		if(intake != null) {
    			Demographic client = clientMgr.getClientByDemographicNo(String.valueOf(demographicNo));
    			boolean saveRequired=false;
    			if(intake.getHealthCardNum() != null && intake.getHealthCardNum().length()>0){
    				client.setHin(intake.getHealthCardNum());
    				log.info("updating " + demographicNo + " with hin " + client.getHin());
    				saveRequired=true;
    			}
    			if(intake.getHealthCardVer() != null && intake.getHealthCardVer().length()>0){
    				client.setVer(intake.getHealthCardVer());
    				log.info("updating " + demographicNo + " with ver " + client.getVer());
    				saveRequired=true;
    			}
    			if(saveRequired) {
    				clientMgr.saveClient(client);
    				totalUpdated++;
    			}
    		} else {
    			log.warn("No intake found for " + demographicNo);
    			totalWarnings++;
    		}
    		
    	}
    	
    	log.info("total updates=" + totalUpdated);
    	log.info("total warnings=" + totalWarnings);
    
    }
    
    public static void main(String args[]) throws Exception {
    	new CopyHealthCardInfoToDemographics();
    }
}
