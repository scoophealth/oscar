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
package org.oscarehr.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tools.ant.util.DateUtils;
import org.hibernate.transform.ResultTransformer;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.ws.rest.to.model.DemographicSearchResult;

import oscar.oscarDemographic.data.DemographicMerged;

public class DemographicSearchResultTransformer implements ResultTransformer{

	private DemographicDao demographicDao;
	private SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.ISO8601_DATE_PATTERN);
	private DemographicMerged dm = new DemographicMerged();
	
	public DemographicSearchResultTransformer() {
	}
	
	
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Integer demographicNo = (Integer) tuple[0];
	    String lastName = (String) tuple[1];
	    String firstName = (String) tuple[2];
	    String chartNo = (String)tuple[3];
	    String sex = String.valueOf(tuple[4]);
	    String providerNo = (String)tuple[5];
	    String rosterStatus = (String)tuple[6];
	    String patientStatus = (String)tuple[7];
	    String phone = (String)tuple[8];
	    String year = (String)tuple[9];
	    String month = (String)tuple[10];
	    String day = (String)tuple[11];
	    String providerLastName = (String)tuple[12];
		String providerFirstName = (String)tuple[13];
		String hin = (String)tuple[14];
	    Integer mergedTo = (Integer)tuple[15];
	    
	    //more about this @ SF Bug #3575
	    if(mergedTo != null) {
	    	//find and replace with the HEAD record info
	    	Integer headDemographicNo = dm.getHead(demographicNo);
	    	Demographic d = demographicDao.getDemographicById(headDemographicNo);
	    	
	    	if(d != null) {
	    		demographicNo = d.getDemographicNo();
	    		lastName = d.getLastName();
	    		firstName = d.getFirstName();
	    		chartNo = d.getChartNo();
	    		sex = d.getSex();
	    		providerNo = d.getProviderNo();
	    		rosterStatus = d.getRosterStatus();
	    		patientStatus = d.getPatientStatus();
	    		phone = d.getPhone();
	    		year = d.getYearOfBirth();
	    		month = d.getMonthOfBirth();
	    		day = d.getDateOfBirth();
	    		hin = d.getHin();
	    		providerLastName = (d.getProvider()!=null)?d.getProvider().getLastName():"";
	    		providerFirstName = (d.getProvider()!=null)?d.getProvider().getFirstName():"";
	    		mergedTo = null;
	    	}
	    }
	    
	    Date dob = null;
		try {
			dob = sdf.parse(year + "-" + month + "-" + day);
		} catch(ParseException e) {
			//logger.warn("Demographic " + demographicNo + " has a bad DOB ",e);
		}
		
			    
	    DemographicSearchResult result =  
	    		new DemographicSearchResult(demographicNo, lastName, firstName, chartNo, sex, providerNo, rosterStatus, 
	    				patientStatus, phone, dob, providerLastName, providerFirstName,hin);
	    return result;
	}
	
	@Override
	public List transformList(List collection) {
		
		return collection;
	}

	public DemographicDao getDemographicDao() {
		return demographicDao;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}

	
}
