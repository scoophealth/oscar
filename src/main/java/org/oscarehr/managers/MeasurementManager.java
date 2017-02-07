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

package org.oscarehr.managers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementGroupStyleDao;
import org.oscarehr.common.dao.MeasurementMapDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementGroupStyle;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;

@Service
public class MeasurementManager {
	@Autowired
	private MeasurementDao measurementDao;

	@Autowired
	private MeasurementMapDao measurementMapDao;
	
	public List<Measurement> getCreatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Measurement> results = measurementDao.findByCreateDate(updatedAfterThisDateExclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getCreatedAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

		return (results);
	}

	public Measurement getMeasurement(LoggedInInfo loggedInInfo, Integer id) {
		Measurement result = measurementDao.find(id);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getMeasurement", "id=" + id);
		}

		return (result);
	}

	public List<Measurement> getMeasurementByType(LoggedInInfo loggedInInfo, Integer id, List<String> types) {
		List<Measurement> results = measurementDao.findByType(id, types);
		if (results.size() > 0) {
			LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getMeasurementByType", "id=" + id);
		}
		return results;
	}

	public List<MeasurementMap> getMeasurementMaps() {
		// should be safe to get all as they're a defined set of loinic codes or human entered entries
		List<MeasurementMap> results = measurementMapDao.getAllMaps();

		// not logging the read, this is not medicalData

		return (results);
	}

	public Measurement addMeasurement(LoggedInInfo loggedInInfo, Measurement measurement) {
		measurementDao.persist(measurement);
		LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.addMeasurement", "id=" + measurement.getId());
		return(measurement);
	}

	/**
	 * ProgramId is not available in oscar right now but the method signature is correct for when it is available.
	 */
	public List<Measurement> getMeasurementsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Measurement> results = measurementDao.findByProviderDemographicLastUpdateDate(providerNo, demographicId, updatedAfterThisDateExclusive.getTime(), itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getMeasurementsByProgramProviderDemographicDate", "programId=" + programId + ", providerNo=" + providerNo + ", demographicId=" + demographicId + ", updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive.getTime());

		return (results);
    }
	
	public static List<String> getFlowsheetDsHTML(){
		List<String> dsHtml = new ArrayList<String>();
		String path_set_by_property = OscarProperties.getInstance().getProperty("MEASUREMENT_DS_HTML_DIRECTORY");
		
		if( path_set_by_property != null ){
			File[] files1 = new File(path_set_by_property).listFiles();
			
			for (File file1 : files1) {
			    if (file1.isFile()) {
			    	dsHtml.add(file1.getName());
			    }
			}
		}
		
		URL path_of_resource = MeasurementFlowSheet.class.getClassLoader().getResource("/oscar/oscarEncounter/oscarMeasurements/flowsheets/html/");
		File[] files2 = new File(path_of_resource.getPath()).listFiles();
		
		for (File file2 : files2) {
		    if (file2.isFile()) {
		    	dsHtml.add(file2.getName());
		    }
		}
		
		
		return dsHtml;
	}
	
	public String getDShtml(String groupName){
		
    	String groupId = null;
    	String propKey = null;
    	
    	groupId = findGroupId(groupName);
    	propKey = "mgroup.ds.html."+groupId;

    	String dsHTML = null;
    	
    	PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean("propertyDao");
		Property p = propertyDao.checkByName(propKey);
		
		if(p!=null){
		dsHTML = p.getValue();
		return MeasurementFlowSheet.getDSHTMLStream(dsHTML);
		}
		
		return "";
	}
	
	public boolean isProperty(String prop) {
		PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean("propertyDao");
		Property props = propertyDao.checkByName(prop);
		if(props!=null){
			return true;
		}		
		return false;
	}
	
	public String findGroupId(String groupName){
		String id = null;
		MeasurementGroupStyleDao measurementGroupStyleDao = (MeasurementGroupStyleDao)SpringUtils.getBean("measurementGroupStyleDao");
		List<MeasurementGroupStyle> results = measurementGroupStyleDao.findByGroupName(groupName);
		
		if(results.size()>0){			
			for(MeasurementGroupStyle result:results){
				id = result.getId().toString();
			}
		}
		
		return id;
	}

	public void addMeasurementGroupDS(String groupName, String dsHTML){
		PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean("propertyDao");
		String id = findGroupId(groupName);
		boolean propertyExists = isProperty("mgroup.ds.html."+id);
		if(propertyExists){
			Property p = propertyDao.checkByName("mgroup.ds.html."+id);
			p.setValue(dsHTML);
			propertyDao.merge(p);
		}else{
			Property x = new Property();
			x.setName("mgroup.ds.html."+id);
			x.setValue(dsHTML);
			propertyDao.persist(x);
		}
	}
	
	public void removeMeasurementGroupDS(String propKey){
		PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean("propertyDao");
		boolean propertyExists = isProperty(propKey);
		if(propertyExists){
			Property p = propertyDao.checkByName(propKey);
			Integer value = p.getId();
			
			propertyDao.remove(value);
		}
	}
	
	
	public static String getPropertyValue(String prop){
		PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean("propertyDao");
		Property p = propertyDao.checkByName(prop);
		String value = p.getValue();
		
		return value;
	}
	
}
