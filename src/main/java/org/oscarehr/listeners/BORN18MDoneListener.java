/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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

package org.oscarehr.listeners;

import org.apache.log4j.Logger;
import org.oscarehr.event.EFormDataCreateEvent;
import org.oscarehr.integration.born.BORN18MConnector;
import org.oscarehr.util.MiscUtils;
import org.springframework.context.ApplicationListener;

import oscar.OscarProperties;

/**
 * Listens for EFormDataCreateEvents and triggers sending BORN 18-month Enhanced Well Baby Visit data
 */
public class BORN18MDoneListener implements ApplicationListener<EFormDataCreateEvent> {
	
	String rourkeFormName = null;
	String nddsFormName = null;
	String report18mFormName = null;
	
	Logger logger = MiscUtils.getLogger();
	OscarProperties oscarProperties = OscarProperties.getInstance();

	/**
	 * Create an instance of a BORN18MDoneListener object.
	 */
	public BORN18MDoneListener() {
		super();
		rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
		nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
		report18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit"); 
	}

	@Override
	public void onApplicationEvent(EFormDataCreateEvent event) {
		Integer fdid = event.getEformDataId();
		Integer demographicNo = event.getDemographicNo();
		String eformName = event.getEformName();
		if (!eformName.equals(rourkeFormName) && !eformName.equals(nddsFormName) && !eformName.equals(report18mFormName))
			return;
		
		BORN18MConnector c = new BORN18MConnector(demographicNo);
		if (c.checkBabyNotYet18m()) return;

		Integer rourkeFdid = null, nddsFdid = null, report18mFdid = null;
		
		if (eformName.equalsIgnoreCase(rourkeFormName)) c.setRourkeFdid(fdid);
		else if (eformName.equalsIgnoreCase(nddsFormName)) c.setNddsFdid(fdid);
		else if (eformName.equalsIgnoreCase(report18mFormName)) c.setReport18mFdid(fdid);

		rourkeFdid = c.checkRourkeDone(rourkeFormName);
		nddsFdid = c.checkNddsDone(nddsFormName);
		report18mFdid = c.checkReport18mDone(report18mFormName);
		
		if (rourkeFdid!=null && nddsFdid!=null && report18mFdid!=null) {
			logger.info("starting BORN18M upload job");
			
			try {
	            c.updateBorn();
            } catch (Exception e) {
	            logger.error("Error", e);
            }
			
			logger.info("done BORN18M upload job");
		}
	}
}
