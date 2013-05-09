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

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DemographicMergedDao;
import org.oscarehr.common.dao.PHRVerificationDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Demographic.PatientStatus;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.DemographicMerged;
import org.oscarehr.common.model.PHRVerification;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

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
	private DemographicArchiveDao demographicArchiveDao;

	@Autowired
	private DemographicMergedDao demographicMergedDao;

	@Autowired
	private PHRVerificationDao phrVerificationDao;
	

	public Demographic getDemographic(Integer demographicId) {
		Demographic result = demographicDao.getDemographicById(demographicId);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous("DemographicManager.getDemographic", "demographicId=" + demographicId);
		}

		return (result);
	}

	public Demographic getDemographicByMyOscarUserName(String myOscarUserName) {
		Demographic result = demographicDao.getDemographicByMyOscarUserName(myOscarUserName);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous("DemographicManager.getDemographic", "demographicId=" + result.getDemographicNo());
		}

		return (result);
	}

	public List<Demographic> searchDemographicByName(String searchString, int startIndex, int itemsToReturn) {
		
		List<Demographic> results = demographicDao.searchDemographicByNameString(searchString, startIndex, itemsToReturn);
		
		if(logger.isDebugEnabled()) {
			logger.debug("searchDemographicByName, searchString="+searchString+", result.size="+results.size());
		}
		
		//--- log action ---
		for (Demographic demographic : results) {
			LogAction.addLogSynchronous("DemographicManager.searchDemographicByName result", "demographicId=" + demographic.getDemographicNo());
		}

		return (results);
	}
	
	public List<DemographicExt> getDemographicExts(Integer id) {
		
		List<DemographicExt> result = null;
		
		result = demographicExtDao.getDemographicExtByDemographicNo(id);
		
		//--- log action ---
		if (result != null) {
			for(DemographicExt item:result) {
				LogAction.addLogSynchronous("DemographicManager.getDemographicExts", "id="+item.getId() + "(" + id.toString() +")");
			}
		}

		return result;
	}

	public List<Demographic> getDemographicsByProvider(Provider provider) {
		List<Demographic> result = demographicDao.getDemographicByProvider(provider.getProviderNo(), true);
		
		//--- log action ---
		if (result != null) {
			for (Demographic demographic : result) {
				LogAction.addLogSynchronous("DemographicManager.getDemographicsByProvider result", "demographicId=" + demographic.getDemographicNo());
			}
		}
		
		return result;
	}

	public void createDemographic(Demographic demographic) {
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() 
					+ ": " + demographic.getBirthDayAsString());
		}
		
		demographic.setPatientStatus(PatientStatus.AC.name());
		demographicDao.save(demographic);

		if (demographic.getExtras() != null) {
			for(DemographicExt ext : demographic.getExtras()) {
				createExtension(ext);
			}
		}
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.createDemographic", "new id is =" + demographic.getDemographicNo());
		
	}
	
	public void updateDemographic(Demographic demographic) {
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() 
					+ ": " + demographic.getBirthDayAsString());
		}
		
		demographicDao.save(demographic);
		
		// TODO What needs to be done with extras - delete first, then save?!?, or build another service? 
		if (demographic.getExtras() != null) {
			for(DemographicExt ext : demographic.getExtras()) {
				LogAction.addLogSynchronous("DemographicManager.updateDemographic ext", "id=" + ext.getId() + "(" +  ext.toString() + ")");
				updateExtension(ext);
			}
		}
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.updateDemographic", "demographicNo=" + demographic.getDemographicNo());
				
	}

	public void createExtension(DemographicExt ext) {
		demographicExtDao.saveEntity(ext);
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.createExtension", "id=" + ext.getId());
	}
	
	public void updateExtension(DemographicExt ext) {
		demographicExtDao.saveEntity(ext);
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.updateExtension", "id=" + ext.getId());
	}

	public void deleteDemographic(Demographic demographic) {
		demographicArchiveDao.archiveRecord(demographic);
		demographic.setPatientStatus(Demographic.PatientStatus.DE.name());
		demographicDao.save(demographic);
		
		for(DemographicExt ext : getDemographicExts(demographic.getDemographicNo())) {
			LogAction.addLogSynchronous("DemographicManager.deleteDemographic ext", "id=" + ext.getId() + "(" + ext.toString() +")");
			deleteExtension(ext);
		}
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.deleteDemographic", "demographicNo=" + demographic.getDemographicNo());
	}

	public void deleteExtension(DemographicExt ext) {
		demographicExtDao.removeDemographicExt(ext.getId());
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.removeDemographicExt", "id=" + ext.getId());
	}

	public void mergeDemographics(Integer parentId, List<Integer> children) {
		for (Integer child : children) {
			DemographicMerged dm = new DemographicMerged();
			dm.setDemographicNo(child);
			dm.setMergedTo(parentId);
			demographicMergedDao.persist(dm);
			
			//--- log action ---
			LogAction.addLogSynchronous("DemographicManager.mergeDemographics", "id=" + dm.getId());
		}
		
	}

	public void unmergeDemographics(Integer parentId, List<Integer> children) {
		for (Integer childId : children) {
			List<DemographicMerged> dms = demographicMergedDao.findByParentAndChildIds(parentId, childId);
			if (dms.isEmpty()) {
				throw new IllegalArgumentException("Unable to find merge record for parent " + parentId + " and child " + childId);
			}
			for(DemographicMerged dm : demographicMergedDao.findByParentAndChildIds(parentId, childId)) {
				dm.setDeleted(1);
				demographicMergedDao.merge(dm);
				
				//--- log action ---
				LogAction.addLogSynchronous("DemographicManager.unmergeDemographics", "id=" + dm.getId());
			}
		}
	}



	public Long getActiveDemographicCount() {
		Long count =  demographicDao.getActiveDemographicCount();
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.getActiveDemographicCount", "");
		
		return count;
	}
	
	public List<Demographic> getActiveDemographics(int offset, int limit) {
	    List<Demographic>  result = demographicDao.getActiveDemographics(offset, limit);
	    
	    if(result != null) {
	    	for(Demographic d:result) {
		    	//--- log action ---
				LogAction.addLogSynchronous("DemographicManager.getActiveDemographics result", "demographicNo="+d.getDemographicNo());
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
	public List<DemographicMerged> getMergedDemographics(Integer parentId) {
	    List<DemographicMerged> result = demographicMergedDao.findCurrentByMergedTo(parentId);
	    
	    if(result != null) {
	    	for(DemographicMerged d:result) {
		    	//--- log action ---
				LogAction.addLogSynchronous("DemographicManager.getMergedDemogrpaphics result", "demographicNo="+d.getDemographicNo());
	    	}
	    	
	    }
	    
	    return result;
    }

	
	public PHRVerification getLatestPhrVerificationByDemographicId(Integer demographicId)
	{
		PHRVerification result=phrVerificationDao.findLatestByDemographicId(demographicId);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous("DemographicManager.getLatestPhrVerificationByDemographicId", "demographicId=" + demographicId);
		}
		
		return(result);
	}
	
	public String getPhrVerificationLevelByDemographicId(Integer demographicId)
	{
		PHRVerification phrVerification=getLatestPhrVerificationByDemographicId(demographicId);
		
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
	public boolean isPhrVerifiedToSendMessages(Integer demographicId)
	{
		String level=getPhrVerificationLevelByDemographicId(demographicId);
		// hard coded to 3 until some one tells me how to configure/check this
		if (PHR_VERIFICATION_LEVEL_3.equals(level)) return(true);
		else return(false);
	}

	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account medicalData.
	 */
	public boolean isPhrVerifiedToSendMedicalData(Integer demographicId)
	{
		String level=getPhrVerificationLevelByDemographicId(demographicId);
		// hard coded to 3 until some one tells me how to configure/check this
		if (PHR_VERIFICATION_LEVEL_3.equals(level)) return(true);
		else return(false);		
	}
}
