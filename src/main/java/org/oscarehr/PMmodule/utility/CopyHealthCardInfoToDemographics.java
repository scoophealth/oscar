/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.utility;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CopyHealthCardInfoToDemographics {
	protected final Logger log = MiscUtils.getLogger();
	    
    public CopyHealthCardInfoToDemographics() throws Exception {
    	int totalUpdated = 0;
    	int totalWarnings = 0;
    	
    	IntakeAManager intakeMgr = (IntakeAManager)SpringUtils.getBean("intakeAManager");
    	ClientManager clientMgr = (ClientManager)SpringUtils.getBean("clientManager");
    	
    	Statement stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
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
