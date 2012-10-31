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


package oscar.oscarLab.ca.on;

import java.util.Date;

import org.oscarehr.common.dao.LabPatientPhysicianInfoDao;
import org.oscarehr.common.dao.LabReportInformationDao;
import org.oscarehr.common.dao.LabTestResultsDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.LabReportInformation;
import org.oscarehr.common.model.LabTestResults;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class LabResultImport {
   
	private static LabTestResultsDao labTestResultsDao = SpringUtils.getBean(LabTestResultsDao.class);
	private static LabPatientPhysicianInfoDao labPatientPhysicianInfoDao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);
	private static LabReportInformationDao labReportInformationDao = SpringUtils.getBean(LabReportInformationDao.class);
	private static PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
	private static  ProviderLabRoutingDao providerLabRoutingDao = SpringUtils.getBean(ProviderLabRoutingDao.class);


	
    public static void SaveLabDesc(String description, String ppId)  {
    	LabTestResults l = new LabTestResults();
    	l.setDescription(description);
    	l.setLabPatientPhysicianInfoId(Integer.parseInt(ppId));
    	l.setLineType("D");
    	labTestResultsDao.persist(l);
    }
    
    public static String saveLabPatientPhysicianInfo(String labReportInfo_id, String accession_num, String collDate, String firstname, String lastname, String sex, String hin, String birthdate, String phone) {
    	LabPatientPhysicianInfo l = new LabPatientPhysicianInfo();
    	l.setLabReportInfoId(Integer.parseInt(labReportInfo_id));
    	l.setAccessionNum(accession_num);
    	l.setPatientFirstName(firstname);
    	l.setPatientLastName(lastname);
    	l.setPatientSex(sex);
    	l.setPatientHin(hin);
    	l.setPatientDob(birthdate);
    	l.setPatientPhone(phone);
    	l.setCollectionDate(collDate);
    	l.setServiceDate(collDate);
    	l.setLabStatus("F");
    	labPatientPhysicianInfoDao.persist(l);
    	
    	return l.getId().toString();
    }
    
    public static String saveLabReportInfo(String location_id)  {
    	LabReportInformation l = new LabReportInformation();
    	l.setLocationId(location_id);
    	l.setPrintDate(UtilDateUtilities.DateToString(new Date(),"yyyy-MM-dd"));
    	l.setPrintTime(UtilDateUtilities.DateToString(new Date(),"HH:mm:ss"));
    	labReportInformationDao.persist(l);
		
    	return l.getId().toString();
    }
    
    public static String saveLabTestResults(String title, String testName, String abn, String minimum, String maximum, String result, String units, String description, String location, String ppId, String linetype, String last)  {
    	LabTestResults l = new LabTestResults();
    	l.setTitle(title);
    	l.setTestName(testName);
    	l.setAbn(abn);
    	l.setMinimum(minimum);
    	l.setMaximum(maximum);
    	l.setResult(result);
    	l.setUnits(units);
    	l.setDescription(description);
    	l.setLocationId(location);
    	l.setLabPatientPhysicianInfoId(Integer.parseInt(ppId));
    	l.setLineType(linetype);
    	l.setLast(last);
    	
    	labTestResultsDao.persist(l);
    	
    	return l.getId().toString();
    }
    
    public static Long savePatientLabRouting(String demo_no, String lab_no) {
    	PatientLabRouting plr = new PatientLabRouting();
    	plr.setDemographicNo(Integer.parseInt(demo_no));
    	plr.setLabNo(Integer.parseInt(lab_no));
    	plr.setLabType("CML");
    	patientLabRoutingDao.persist(plr);
    	
    	return plr.getId().longValue();
    }
    
    public static Long saveProviderLabRouting(String provider_no, String lab_no, String status, String comment, String timestamp)  {
		Long id = null;
		if (timestamp==null || ("").equals(timestamp)) 
			timestamp=null;
		
		Date ts = null;
		if(timestamp != null) {
			ts = ConversionUtils.fromTimestampString(timestamp);
		}
		
		
		ProviderLabRoutingModel plr = new ProviderLabRoutingModel();
		plr.setProviderNo(provider_no);
		plr.setLabNo(Integer.parseInt(lab_no));
		plr.setStatus(status);
		plr.setComment(comment);
		plr.setTimestamp(ts);
		plr.setLabType("CML");
		providerLabRoutingDao.persist(plr);
		
		return plr.getId().longValue();
    }
}
