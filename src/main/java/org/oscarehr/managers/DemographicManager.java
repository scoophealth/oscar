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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.common.Gender;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicCustArchiveDao;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtArchiveDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DemographicMergedDao;
import org.oscarehr.common.dao.PHRVerificationDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Demographic.PatientStatus;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.DemographicMerged;
import org.oscarehr.common.model.PHRVerification;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.DemographicSearchRequest;
import org.oscarehr.ws.rest.to.model.DemographicSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;
import oscar.util.StringUtils;

/**
 * Will provide access to demographic data, as well as closely related data such as 
 * extensions (DemographicExt), merge data, archive data, etc.
 * 
 * Future Use: Add privacy, security, and consent profiles
 * 
 *
 */
@Service
public class DemographicManager {
	public static final String PHR_VERIFICATION_LEVEL_3 = "+3";
	public static final String PHR_VERIFICATION_LEVEL_2 = "+2";
	public static final String PHR_VERIFICATION_LEVEL_1 = "+1";

	private static Logger logger=MiscUtils.getLogger();
	
	@Autowired
	private DemographicDao demographicDao;
	@Autowired
	private DemographicExtDao demographicExtDao;
	@Autowired
	private DemographicCustDao demographicCustDao;
	@Autowired
	private DemographicContactDao demographicContactDao;
	
	@Autowired
	private DemographicArchiveDao demographicArchiveDao;
	@Autowired
	private DemographicExtArchiveDao demographicExtArchiveDao;
	@Autowired
	private DemographicCustArchiveDao demographicCustArchiveDao;

	@Autowired
	private DemographicMergedDao demographicMergedDao;

	@Autowired
	private PHRVerificationDao phrVerificationDao;
	
	@Autowired
	private AdmissionDao admissionDao;
	@Autowired
	private ProgramDao programDao;
	

	public Demographic getDemographic(LoggedInInfo loggedInInfo, Integer demographicId) {
		Demographic result = demographicDao.getDemographicById(demographicId);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographic", "demographicId=" + demographicId);
		}

		return (result);
	}
	
	public String getDemographicFormattedName(LoggedInInfo loggedInInfo, Integer demographicId) {
		Demographic result = getDemographic(loggedInInfo, demographicId);
		String name = null;
		if(result != null) {
			name = result.getLastName() + ", " + result.getFirstName();
		}

		return (name);
	}

	public Demographic getDemographicByMyOscarUserName(LoggedInInfo loggedInInfo, String myOscarUserName) {
		Demographic result = demographicDao.getDemographicByMyOscarUserName(myOscarUserName);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographic", "demographicId=" + result.getDemographicNo());
		}

		return (result);
	}

	public List<Demographic> searchDemographicByName(LoggedInInfo loggedInInfo, String searchString, int startIndex, int itemsToReturn) {
		
		List<Demographic> results = demographicDao.searchDemographicByNameString(searchString, startIndex, itemsToReturn);
		
		if(logger.isDebugEnabled()) {
			logger.debug("searchDemographicByName, searchString="+searchString+", result.size="+results.size());
		}
		
		//--- log action ---
		for (Demographic demographic : results) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchDemographicByName result", "demographicId=" + demographic.getDemographicNo());
		}

		return (results);
	}
	
	public List<DemographicExt> getDemographicExts(LoggedInInfo loggedInInfo, Integer id) {
		
		List<DemographicExt> result = null;
		
		result = demographicExtDao.getDemographicExtByDemographicNo(id);
		
		//--- log action ---
		if (result != null) {
			for(DemographicExt item:result) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicExts", "id="+item.getId() + "(" + id.toString() +")");
			}
		}

		return result;
	}
	
	public DemographicExt getDemographicExt(LoggedInInfo loggedInInfo, Integer demographicNo, String key) {
		
		DemographicExt result = null;
		result = demographicExtDao.getDemographicExt(demographicNo, key);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicExt(demographicNo, key)", "id="+result.getId() + "(" + demographicNo +")");
		}
		return result;
	}
	
	public DemographicCust getDemographicCust(LoggedInInfo loggedInInfo, Integer id) {
		DemographicCust result = null;
		result = demographicCustDao.find(id);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicCust", "id="+ id.toString());
		}
		return result;
	}
	
	public void createUpdateDemographicCust(LoggedInInfo loggedInInfo, DemographicCust demoCust) {
		if (demoCust!=null) {
			//Archive previous demoCust
			DemographicCust prevCust = demographicCustDao.find(demoCust.getId());
			if (prevCust!=null) {
				if (!(StringUtils.nullSafeEquals(prevCust.getAlert(), demoCust.getAlert()) &&
					  StringUtils.nullSafeEquals(prevCust.getMidwife(), demoCust.getMidwife()) &&
					  StringUtils.nullSafeEquals(prevCust.getNurse(), demoCust.getNurse()) &&
					  StringUtils.nullSafeEquals(prevCust.getResident(), demoCust.getResident()) &&
					  StringUtils.nullSafeEquals(prevCust.getNotes(), demoCust.getNotes())))
				{
					demographicCustArchiveDao.archiveDemographicCust(prevCust);
				}
			}
			
			demographicCustDao.merge(demoCust);
		}
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createUpdateDemographicCust", "id="+ demoCust.getId());
	}
	
	public List<DemographicContact> getDemographicContacts(LoggedInInfo loggedInInfo, Integer id) {
		List<DemographicContact> result = null;
		result = demographicContactDao.findActiveByDemographicNo(id);
		
		//--- log action ---
		if (result != null) {
			for(DemographicContact item:result) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicContacts", "id="+item.getId() + "(" + id.toString() +")");
			}
		}
		return result;
	}

	public List<Demographic> getDemographicsByProvider(LoggedInInfo loggedInInfo, Provider provider) {
		List<Demographic> result = demographicDao.getDemographicByProvider(provider.getProviderNo(), true);
		
		//--- log action ---
		if (result != null) {
			for (Demographic demographic : result) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicsByProvider result", "demographicId=" + demographic.getDemographicNo());
			}
		}
		
		return result;
	}

	public void createDemographic(LoggedInInfo loggedInInfo, Demographic demographic, Integer admissionProgramId) {
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() 
					+ ": " + demographic.getBirthDayAsString());
		}
		
		demographic.setPatientStatus(PatientStatus.AC.name());
		demographicDao.save(demographic);
		
		Admission admission = new Admission();
		admission.setClientId(demographic.getDemographicNo());
		admission.setProgramId(admissionProgramId);
		admission.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		admission.setAdmissionDate(new Date());
		admission.setAdmissionStatus(Admission.STATUS_CURRENT);
		admission.setAdmissionNotes("");
		
		admissionDao.saveAdmission(admission);
		
		if (demographic.getExtras() != null) {
			for(DemographicExt ext : demographic.getExtras()) {
				createExtension(loggedInInfo, ext);
			}
		}
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createDemographic", "new id is =" + demographic.getDemographicNo());
		
	}
	
	public void updateDemographic(LoggedInInfo loggedInInfo, Demographic demographic) {
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() 
					+ ": " + demographic.getBirthDayAsString());
		}
		
		//Archive previous demo
		Demographic prevDemo = demographicDao.getDemographicById(demographic.getDemographicNo());
		demographicArchiveDao.archiveRecord(prevDemo);
		
		//save current demo
		demographicDao.save(demographic);
		
		if (demographic.getExtras() != null) {
			for(DemographicExt ext : demographic.getExtras()) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.updateDemographic ext", "id=" + ext.getId() + "(" +  ext.toString() + ")");
				updateExtension(loggedInInfo, ext);
			}
		}
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.updateDemographic", "demographicNo=" + demographic.getDemographicNo());
				
	}

	public void createExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		demographicExtDao.saveEntity(ext);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createExtension", "id=" + ext.getId());
	}
	
	public void updateExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		archiveExtension(ext);
		demographicExtDao.saveEntity(ext);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.updateExtension", "id=" + ext.getId());
	}
	
	public void archiveExtension(DemographicExt ext) {
		if (ext!=null && ext.getId()!=null) {
			DemographicExt prevExt = demographicExtDao.find(ext.getId());
			if (!(ext.getKey().equals(prevExt.getKey()) && ext.getValue().equals(prevExt.getValue()))) {
				demographicExtArchiveDao.archiveDemographicExt(prevExt);
			}
		}
	}
	
	public void createUpdateDemographicContact(LoggedInInfo loggedInInfo, DemographicContact demoContact) {
		demographicContactDao.merge(demoContact);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createUpdateDemographicContact", "id=" + demoContact.getId());
	}

	public void deleteDemographic(LoggedInInfo loggedInInfo, Demographic demographic) {
		demographicArchiveDao.archiveRecord(demographic);
		demographic.setPatientStatus(Demographic.PatientStatus.DE.name());
		demographicDao.save(demographic);
		
		for(DemographicExt ext : getDemographicExts(loggedInInfo, demographic.getDemographicNo())) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.deleteDemographic ext", "id=" + ext.getId() + "(" + ext.toString() +")");
			deleteExtension(loggedInInfo, ext);
		}
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.deleteDemographic", "demographicNo=" + demographic.getDemographicNo());
	}

	public void deleteExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		archiveExtension(ext);
		demographicExtDao.removeDemographicExt(ext.getId());
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.removeDemographicExt", "id=" + ext.getId());
	}

	public void mergeDemographics(LoggedInInfo loggedInInfo, Integer parentId, List<Integer> children) {
		for (Integer child : children) {
			DemographicMerged dm = new DemographicMerged();
			dm.setDemographicNo(child);
			dm.setMergedTo(parentId);
			demographicMergedDao.persist(dm);
			
			//--- log action ---
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.mergeDemographics", "id=" + dm.getId());
		}
		
	}

	public void unmergeDemographics(LoggedInInfo loggedInInfo, Integer parentId, List<Integer> children) {
		for (Integer childId : children) {
			List<DemographicMerged> dms = demographicMergedDao.findByParentAndChildIds(parentId, childId);
			if (dms.isEmpty()) {
				throw new IllegalArgumentException("Unable to find merge record for parent " + parentId + " and child " + childId);
			}
			for(DemographicMerged dm : demographicMergedDao.findByParentAndChildIds(parentId, childId)) {
				dm.setDeleted(1);
				demographicMergedDao.merge(dm);
				
				//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.unmergeDemographics", "id=" + dm.getId());
			}
		}
	}



	public Long getActiveDemographicCount(LoggedInInfo loggedInInfo) {
		Long count =  demographicDao.getActiveDemographicCount();
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getActiveDemographicCount", "");
		
		return count;
	}
	
	public List<Demographic> getActiveDemographics(LoggedInInfo loggedInInfo, int offset, int limit) {
	    List<Demographic>  result = demographicDao.getActiveDemographics(offset, limit);
	    
	    if(result != null) {
	    	for(Demographic d:result) {
		    	//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getActiveDemographics result", "demographicNo="+d.getDemographicNo());
	    	}
	    }
	    
	    return result;
    }

	/**
	 * Gets all merged demographic for the specified parent record ID 
	 * 
	 * @param parentId
	 * 		ID of the parent demographic record 
	 * @return
	 * 		Returns all merged demographic records for the specified parent id.
	 */
	public List<DemographicMerged> getMergedDemographics(LoggedInInfo loggedInInfo, Integer parentId) {
	    List<DemographicMerged> result = demographicMergedDao.findCurrentByMergedTo(parentId);
	    
	    if(result != null) {
	    	for(DemographicMerged d:result) {
		    	//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getMergedDemogrpaphics result", "demographicNo="+d.getDemographicNo());
	    	}
	    	
	    }
	    
	    return result;
    }

	
	public PHRVerification getLatestPhrVerificationByDemographicId(LoggedInInfo loggedInInfo, Integer demographicId)
	{
		PHRVerification result=phrVerificationDao.findLatestByDemographicId(demographicId);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getLatestPhrVerificationByDemographicId", "demographicId=" + demographicId);
		}
		
		return(result);
	}
	
	public String getPhrVerificationLevelByDemographicId(LoggedInInfo loggedInInfo, Integer demographicId)
	{
		PHRVerification phrVerification=getLatestPhrVerificationByDemographicId(loggedInInfo, demographicId);
		
        if (phrVerification!=null){
        	String authLevel =phrVerification.getVerificationLevel();
        	if ( PHRVerification.VERIFICATION_METHOD_FAX.equals(authLevel) || PHRVerification.VERIFICATION_METHOD_MAIL.equals(authLevel)  || PHRVerification.VERIFICATION_METHOD_EMAIL.equals(authLevel)){
        		return PHR_VERIFICATION_LEVEL_1;
        	}else if (PHRVerification.VERIFICATION_METHOD_TEL.equals(authLevel) || PHRVerification.VERIFICATION_METHOD_VIDEOPHONE.equals(authLevel)){
        		return PHR_VERIFICATION_LEVEL_2;
        	}else if (PHRVerification.VERIFICATION_METHOD_INPERSON.equals(authLevel)){
        		return PHR_VERIFICATION_LEVEL_3;
        	}
        }
        
        // blank string because preserving existing behaviour moved from PHRVerificationDao, I would have preferred returnning null on a new method...
        return("");
	}
	
	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account messages.
	 */
	public boolean isPhrVerifiedToSendMessages(LoggedInInfo loggedInInfo, Integer demographicId)
	{
		String level=getPhrVerificationLevelByDemographicId(loggedInInfo, demographicId);
		// hard coded to 3 until some one tells me how to configure/check this
		if (PHR_VERIFICATION_LEVEL_3.equals(level)) return(true);
		else return(false);
	}

	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account medicalData.
	 */
	public boolean isPhrVerifiedToSendMedicalData(LoggedInInfo loggedInInfo, Integer demographicId)
	{
		String level=getPhrVerificationLevelByDemographicId(loggedInInfo, demographicId);
		// hard coded to 3 until some one tells me how to configure/check this
		if (PHR_VERIFICATION_LEVEL_3.equals(level)) return(true);
		else return(false);		
	}

	/**
	 * @deprecated there should be a generic call for getDemographicExt(Integer demoId, String key) instead. Then the caller should assemble what it needs from the demographic and ext call itself.
	 */
	public String getDemographicWorkPhoneAndExtension(LoggedInInfo loggedInInfo, Integer demographicNo)
	{
		Demographic result=demographicDao.getDemographicById(demographicNo);
		String workPhone = result.getPhone2();
		if(workPhone != null && workPhone.length()>0) {
			String value = demographicExtDao.getValueForDemoKey(demographicNo, "wPhoneExt");
			if(value != null && value.length()>0) {
				workPhone += "x" + value;
			}
		}
		
		//--- log action ---
		if (result!=null)
		{
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicWorkPhoneAndExtension", "demographicId="+result.getDemographicNo() + "result=" + workPhone);
		}
		
		return(workPhone);
	}
	
	/**
	 * @see DemographicDao.findByAttributes for parameter details
	 */
	public List<Demographic> searchDemographicsByAttributes(LoggedInInfo loggedInInfo, String hin, String firstName, String lastName, Gender gender, Calendar dateOfBirth, String city, String province, String phone, String email, String alias, int startIndex, int itemsToReturn) {
		List<Demographic> results=demographicDao.findByAttributes(hin, firstName, lastName, gender, dateOfBirth, city, province, phone, email, alias, startIndex, itemsToReturn);

		// log all items read
		for(Demographic d : results) {
	    	LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchDemographicsByAttributes result", "demographicNo="+d.getDemographicNo());
    	}
		
		return(results);
	}
	
	public List<String> getPatientStatusList() {
		return demographicDao.search_ptstatus();
	}
	
	public List<String> getRosterStatusList() {
		return demographicDao.getRosterStatuses();
	}
	
	
	public List<DemographicSearchResult> searchPatients(LoggedInInfo loggedInInfo, DemographicSearchRequest searchRequest, int startIndex, int itemsToReturn) {
		List<DemographicSearchResult> results =  demographicDao.searchPatients(loggedInInfo, searchRequest, startIndex, itemsToReturn);
		
		for (DemographicSearchResult demographic : results) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchPatients result", "demographicId=" + demographic.getDemographicNo());
		}
		
		return results;
	}
	
	public int searchPatientsCount(LoggedInInfo loggedInInfo, DemographicSearchRequest searchRequest) {
		return demographicDao.searchPatientCount(loggedInInfo, searchRequest);
	}
	

}
