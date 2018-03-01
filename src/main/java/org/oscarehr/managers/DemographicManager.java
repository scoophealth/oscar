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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.Gender;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.ContactSpecialtyDao;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicCustArchiveDao;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicDao.SearchType;
import org.oscarehr.common.dao.DemographicExtArchiveDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DemographicMergedDao;
import org.oscarehr.common.dao.PHRVerificationDao;
import org.oscarehr.common.exception.PatientDirectiveException;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.ContactSpecialty;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Demographic.PatientStatus;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.DemographicMerged;
import org.oscarehr.common.model.PHRVerification;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.HealthCareTeamCreator;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.DemographicSearchRequest;
import org.oscarehr.ws.rest.to.model.DemographicSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.OscarProperties;
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

	private static Logger logger = MiscUtils.getLogger();

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
	private SecurityInfoManager securityInfoManager;

	@Autowired
	private ContactSpecialtyDao contactSpecialtyDao;

	@Autowired
	private ProgramManager2 programManager2;
	

	public Demographic getDemographic(LoggedInInfo loggedInInfo, Integer demographicId) throws PatientDirectiveException {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ, (demographicId!=null)?demographicId:null );
		
		Demographic result = demographicDao.getDemographicById(demographicId);

		//--- log action ---
		if (result != null) {
			LogAction.addLog(loggedInInfo, "DemographicManager.getDemographic", null, null, ""+demographicId, null);
		}

		return (result);
	}
	public Demographic getDemographic(LoggedInInfo loggedInInfo, String demographicNo) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		Integer demographicId = null;
		try {
			demographicId = Integer.parseInt(demographicNo);
		} catch(NumberFormatException e) {
			return null;
		}
		return getDemographic(loggedInInfo,demographicId);

	}


	public Demographic getDemographicWithExt(LoggedInInfo loggedInInfo, Integer demographicId) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		Demographic result = getDemographic(loggedInInfo, demographicId);
		if (result!=null) {
			List<DemographicExt> demoExts = getDemographicExts(loggedInInfo,demographicId);
			if (demoExts!=null && !demoExts.isEmpty()) {
				DemographicExt[] demoExtArray = demoExts.toArray(new DemographicExt[demoExts.size()]);
				result.setExtras(demoExtArray);
			}
		}
		return result;
	}

	public String getDemographicFormattedName(LoggedInInfo loggedInInfo, Integer demographicId) {
		Demographic result = getDemographic(loggedInInfo, demographicId);
		String name = null;
		if (result != null) {
			name = result.getLastName() + ", " + result.getFirstName();
		}

		return (name);
	}

	public Demographic getDemographicByMyOscarUserName(LoggedInInfo loggedInInfo, String myOscarUserName) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		Demographic result = demographicDao.getDemographicByMyOscarUserName(myOscarUserName);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographic", "demographicId=" + result.getDemographicNo());
		}

		return (result);
	}
	

	public List<Demographic> searchDemographicByName(LoggedInInfo loggedInInfo, String searchString, int startIndex, int itemsToReturn) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		List<Demographic> results = demographicDao.searchDemographicByNameString(searchString, startIndex, itemsToReturn);

		if (logger.isDebugEnabled()) {
			logger.debug("searchDemographicByName, searchString=" + searchString + ", result.size=" + results.size());
		}

		//--- log action ---
		for (Demographic demographic : results) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchDemographicByName result", "demographicId=" + demographic.getDemographicNo());
		}

		return (results);
	}

	public List<DemographicExt> getDemographicExts(LoggedInInfo loggedInInfo, Integer id) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		List<DemographicExt> result = null;

		result = demographicExtDao.getDemographicExtByDemographicNo(id);

		//--- log action ---
		if (result != null) {
			for (DemographicExt item : result) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicExts", "id=" + item.getId() + "(" + id.toString() + ")");
			}
		}

		return result;
	}
	
	public List<Demographic> findInactiveDemographicByBandNumber( LoggedInInfo loggedInInfo, String bandNumber,  
			List<String> stati, int limit, int offset, String providerNo, Boolean outOfDomain ) {
		return findDemographicByBandNumber( loggedInInfo, bandNumber, stati, limit, offset, providerNo, outOfDomain, true);
	}
	
	public List<Demographic> findActiveDemographicByBandNumber( LoggedInInfo loggedInInfo, String bandNumber,  
			List<String> stati, int limit, int offset, String providerNo, Boolean outOfDomain ) {
		return findDemographicByBandNumber( loggedInInfo, bandNumber, stati, limit, offset, providerNo, outOfDomain, false);
	}
	
	public List<Demographic> findAllDemographicByBandNumber( LoggedInInfo loggedInInfo, String bandNumber, 
			int limit, int offset, String providerNo, Boolean outOfDomain) {
		return findDemographicByBandNumber( loggedInInfo, bandNumber, null, limit, offset, providerNo, outOfDomain, true);
	}
	
	/*
	 * A work-around/hack.
	 */
	public List<String> findDemographicNumbersByBandNumber( LoggedInInfo loggedInInfo, String searchStrs, 
			int limit, int offset, String providerNo, Boolean outOfDomain, boolean active, boolean inactive ) {
		
		List<Demographic> demographics = null;
		List<String> demographicNumbers = null;
		List<String> stati = new ArrayList<String>();
		stati.add( Demographic.PatientStatus.AC.name() );
		
		if( active && inactive ) {
			
			logger.debug( "Searching all demographics with band number " +  searchStrs );
			
			demographics = findAllDemographicByBandNumber( loggedInInfo, searchStrs, limit, offset, providerNo, outOfDomain );			
		} else if ( active ) {
			
			logger.debug( "Searching active demographics with band number " +  searchStrs );
			
			demographics = findActiveDemographicByBandNumber( loggedInInfo, searchStrs, stati, limit, offset, providerNo, outOfDomain );			
		} else if ( inactive ) {
			
			logger.debug( "Searching inactive demographics with band number " +  searchStrs );
			
			demographics = findInactiveDemographicByBandNumber( loggedInInfo, searchStrs, stati, limit, offset, providerNo, outOfDomain );
		}
		
		if( demographics != null ) {
			for( Demographic demographic : demographics ) {
				if( demographicNumbers == null ) {
					demographicNumbers = new ArrayList<String>();
				}
				String demographicNumber = demographic.getDemographicNo() + "";
				if( ! demographicNumbers.contains(demographicNumber) ) {
					demographicNumbers.add(demographicNumber);
				}
			}
		}

		return demographicNumbers;
	}
	
	public List<Demographic> findDemographicByBandNumber( LoggedInInfo loggedInInfo, String bandNumber,  
			List<String> statuses, int limit, int offset, String providerNo, Boolean outOfDomain, boolean ignoreStatuses ) {
		
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		
		// It is assumed that a band number search will be for the commonly used DemographicExt key "statusNum"
		List<DemographicExt> bandNumbers = null;		
		List<Demographic> demographicList = null;
		List<Demographic> demographics = null;

		List<DemographicExt> bandNumberList = demographicExtDao.searchDemographicExtByKeyAndValue( DemographicExt.FIRST_NATION_KEY.statusNum.toString(), bandNumber );
		
		if( bandNumberList != null ) {
			bandNumbers = new ArrayList<DemographicExt>();
			bandNumbers.addAll( bandNumberList );
		}

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.findDemographicByBandNumber", "Searching for band number " + bandNumber );
		
		if( bandNumbers != null ) {
			
			logger.debug( "Located " + bandNumbers.size() + " bandnumbers with search string " + bandNumber );

			for(DemographicExt demographicExt : bandNumbers) {
				if( demographicList == null ) {
					demographicList = new ArrayList<Demographic>();
				}
				demographicList.addAll( 
						demographicDao.findDemographicByDemographicNoAndStatus( demographicExt.getDemographicNo()+"", 
								statuses, limit, offset, providerNo, outOfDomain, ignoreStatuses ) 
				);
			}			
		}
 
		if( demographicList != null ) {
			
			for( Demographic demographic : demographicList ) {
				if( demographics == null ) {
					demographics = new ArrayList<Demographic>();
				}
				if( ! demographics.contains(demographic) ) {
					demographics.add( demographic );
				}
			}
			
			logger.debug( "Located " + demographics.size() + " demographics with search string " + bandNumber );
		}
		
		return demographics;
	}

	public DemographicExt getDemographicExt(LoggedInInfo loggedInInfo, Integer demographicNo, String key) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		DemographicExt result = null;
		result = demographicExtDao.getDemographicExt(demographicNo, key);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicExt(demographicNo, key)", "id=" + result.getId() + "(" + demographicNo + ")");
		}
		return result;
	}

	public DemographicCust getDemographicCust(LoggedInInfo loggedInInfo, Integer id) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		DemographicCust result = null;
		result = demographicCustDao.find(id);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicCust", "id=" + id.toString());
		}
		return result;
	}

	public void createUpdateDemographicCust(LoggedInInfo loggedInInfo, DemographicCust demoCust) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		if (demoCust != null) {
			//Archive previous demoCust
			DemographicCust prevCust = demographicCustDao.find(demoCust.getId());
			if (prevCust != null) {
				if (!(StringUtils.nullSafeEquals(prevCust.getAlert(), demoCust.getAlert()) && StringUtils.nullSafeEquals(prevCust.getMidwife(), demoCust.getMidwife()) && StringUtils.nullSafeEquals(prevCust.getNurse(), demoCust.getNurse()) && StringUtils.nullSafeEquals(prevCust.getResident(), demoCust.getResident()) && StringUtils.nullSafeEquals(prevCust.getNotes(), demoCust.getNotes()))) {
					demographicCustArchiveDao.archiveDemographicCust(prevCust);
				}
			}

			demographicCustDao.merge(demoCust);
		}

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createUpdateDemographicCust", "id=" + demoCust.getId());
	}

	public List<DemographicContact> getDemographicContacts(LoggedInInfo loggedInInfo, Integer demographicNo, String category) {

		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		List<Integer> programDomainIds = new ArrayList<Integer>();
		for(ProgramProvider pp:programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo())) {
			programDomainIds.add(pp.getProgramId().intValue());
		}
		
		List<DemographicContact> result = new ArrayList<DemographicContact>();
		
		for(DemographicContact dc:demographicContactDao.findByDemographicNoAndCategory( demographicNo, category )) {
			if(dc.getProgramNo() != null && dc.getProgramNo().intValue() > 0) {
				if(programDomainIds.contains(dc.getProgramNo())) {
					result.add(dc);
				}
			} else {
				result.add(dc);
			}
		}

		//--- log action ---
		if (result != null) {
			for (DemographicContact item : result) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicContacts", "id=" + item.getId() + "(" + demographicNo.toString() + ")");
			}
		}
		return result;
	}

	public List<DemographicContact> getDemographicContacts(LoggedInInfo loggedInInfo, Integer id) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		List<Integer> programDomainIds = new ArrayList<Integer>();
		for(ProgramProvider pp:programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo())) {
			programDomainIds.add(pp.getProgramId().intValue());
		}
		List<DemographicContact> result = new ArrayList<DemographicContact>();
		for(DemographicContact dc:demographicContactDao.findActiveByDemographicNo(id)) {
			if(dc.getProgramNo() != null && dc.getProgramNo().intValue() > 0) {
				if(programDomainIds.contains(dc.getProgramNo())) {
					result.add(dc);
				}
			} else {
				result.add(dc);
			}
		}
		
		//--- log action ---
		if (result != null) {
			for (DemographicContact item : result) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicContacts", "id=" + item.getId() + "(" + id.toString() + ")");
			}
		}
		return result;
	}

	public List<Demographic> getDemographicsByProvider(LoggedInInfo loggedInInfo, Provider provider) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
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
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() + ": " + demographic.getBirthDayAsString());
		}

		demographic.setPatientStatus(PatientStatus.AC.name());
		demographic.setFamilyDoctor("<rdohip></rdohip><rd></rd>");
		demographic.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
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
			for (DemographicExt ext : demographic.getExtras()) {
				createExtension(loggedInInfo, ext);
			}
		}

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createDemographic", "new id is =" + demographic.getDemographicNo());

	}

	public void updateDemographic(LoggedInInfo loggedInInfo, Demographic demographic) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.UPDATE);
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() + ": " + demographic.getBirthDayAsString());
		}

		//Archive previous demo
		Demographic prevDemo = demographicDao.getDemographicById(demographic.getDemographicNo());
		demographicArchiveDao.archiveRecord(prevDemo);

		//retain merge info
		demographic.setSubRecord(prevDemo.getSubRecord());
		//save current demo
		demographic.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		demographicDao.save(demographic);

		if (demographic.getExtras() != null) {
			for (DemographicExt ext : demographic.getExtras()) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.updateDemographic ext", "id=" + ext.getId() + "(" + ext.toString() + ")");
				updateExtension(loggedInInfo, ext);
			}
		}

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.updateDemographic", "demographicNo=" + demographic.getDemographicNo());

	}

	public void addDemographic(LoggedInInfo loggedInInfo, Demographic demographic) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		try {
			demographic.getBirthDay();
		} catch (Exception e) {
			throw new IllegalArgumentException("Birth date was specified for " + demographic.getFullName() + ": " + demographic.getBirthDayAsString());
		}

		//save current demo
		demographic.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		demographicDao.save(demographic);

		if (demographic.getExtras() != null) {
			for (DemographicExt ext : demographic.getExtras()) {
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.addDemographic ext", "id=" + ext.getId() + "(" + ext.toString() + ")");
				updateExtension(loggedInInfo, ext);
			}
		}

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.addDemographic", "demographicNo=" + demographic.getDemographicNo());

	}


	public void createExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		demographicExtDao.saveEntity(ext);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createExtension", "id=" + ext.getId());
	}

	public void updateExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.UPDATE);
		archiveExtension(loggedInInfo, ext);
		demographicExtDao.saveEntity(ext);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.updateExtension", "id=" + ext.getId());
	}

	/**
	 * Deprecated: use the new archiveExtension method with a LoggedInInfo parameter.
	 * archiveExtension(LoggedInInfo loggedInInfo, DemographicExt ext)
	 */
	@Deprecated
	public void archiveExtension(DemographicExt ext) {
		//TODO: this needs a loggedInInfo
		if (ext != null && ext.getId() != null) {
			DemographicExt prevExt = demographicExtDao.find(ext.getId());
			if (!(ext.getKey().equals(prevExt.getKey()) && Objects.equals(ext.getValue(),prevExt.getValue()))) {
				demographicExtArchiveDao.archiveDemographicExt(prevExt);
			}
		}
	}
	
	/**
	 * New ArchiveExtension method that includes the LoggedInInfo parameter.
	 */
	public void archiveExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		if (ext != null && ext.getId() != null) {
			DemographicExt prevExt = demographicExtDao.find(ext.getId());
			if (!(ext.getKey().equals(prevExt.getKey()) && Objects.equals(ext.getValue(),prevExt.getValue()))) {
				demographicExtArchiveDao.archiveDemographicExt(prevExt);
			}
		}
		
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.archiveExtension", " Extension ID: " + ext.getId());
	}

	public void createUpdateDemographicContact(LoggedInInfo loggedInInfo, DemographicContact demoContact) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		demographicContactDao.merge(demoContact);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.createUpdateDemographicContact", "id=" + demoContact.getId());
	}

	public void deleteDemographic(LoggedInInfo loggedInInfo, Demographic demographic) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		demographicArchiveDao.archiveRecord(demographic);
		demographic.setPatientStatus(Demographic.PatientStatus.DE.name());
		demographic.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		demographicDao.save(demographic);

		for (DemographicExt ext : getDemographicExts(loggedInInfo, demographic.getDemographicNo())) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.deleteDemographic ext", "id=" + ext.getId() + "(" + ext.toString() + ")");
			deleteExtension(loggedInInfo, ext);
		}

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.deleteDemographic", "demographicNo=" + demographic.getDemographicNo());
	}

	public void deleteExtension(LoggedInInfo loggedInInfo, DemographicExt ext) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.WRITE);
		archiveExtension(loggedInInfo, ext);
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
			for (DemographicMerged dm : demographicMergedDao.findByParentAndChildIds(parentId, childId)) {
				dm.setDeleted(1);
				demographicMergedDao.merge(dm);

				//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.unmergeDemographics", "id=" + dm.getId());
			}
		}
	}

	public Long getActiveDemographicCount(LoggedInInfo loggedInInfo) {
		Long count = demographicDao.getActiveDemographicCount();

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getActiveDemographicCount", "");

		return count;
	}

	public List<Demographic> getActiveDemographics(LoggedInInfo loggedInInfo, int offset, int limit) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		List<Demographic> result = demographicDao.getActiveDemographics(offset, limit);

		if (result != null) {
			for (Demographic d : result) {
				//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getActiveDemographics result", "demographicNo=" + d.getDemographicNo());
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

		if (result != null) {
			for (DemographicMerged d : result) {
				//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getMergedDemogrpaphics result", "demographicNo=" + d.getDemographicNo());
			}

		}

		return result;
	}

	public PHRVerification getLatestPhrVerificationByDemographicId(LoggedInInfo loggedInInfo, Integer demographicId) {
		PHRVerification result = phrVerificationDao.findLatestByDemographicId(demographicId);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getLatestPhrVerificationByDemographicId", "demographicId=" + demographicId);
		}

		return (result);
	}

	public String getPhrVerificationLevelByDemographicId(LoggedInInfo loggedInInfo, Integer demographicId) {
		PHRVerification phrVerification = getLatestPhrVerificationByDemographicId(loggedInInfo, demographicId);

		if (phrVerification != null) {
			String authLevel = phrVerification.getVerificationLevel();
			if (PHRVerification.VERIFICATION_METHOD_FAX.equals(authLevel) || PHRVerification.VERIFICATION_METHOD_MAIL.equals(authLevel) || PHRVerification.VERIFICATION_METHOD_EMAIL.equals(authLevel)) {
				return PHR_VERIFICATION_LEVEL_1;
			} else if (PHRVerification.VERIFICATION_METHOD_TEL.equals(authLevel) || PHRVerification.VERIFICATION_METHOD_VIDEOPHONE.equals(authLevel)) {
				return PHR_VERIFICATION_LEVEL_2;
			} else if (PHRVerification.VERIFICATION_METHOD_INPERSON.equals(authLevel)) {
				return PHR_VERIFICATION_LEVEL_3;
			}
		}

		// blank string because preserving existing behaviour moved from PHRVerificationDao, I would have preferred returnning null on a new method...
		return ("");
	}

	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account messages.
	 */
	public boolean isPhrVerifiedToSendMessages(LoggedInInfo loggedInInfo, Integer demographicId) {
		String level = getPhrVerificationLevelByDemographicId(loggedInInfo, demographicId);
		// hard coded to 3 until some one tells me how to configure/check this
		if (PHR_VERIFICATION_LEVEL_3.equals(level)) return (true);
		else return (false);
	}

	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account medicalData.
	 */
	public boolean isPhrVerifiedToSendMedicalData(LoggedInInfo loggedInInfo, Integer demographicId) {
		String level = getPhrVerificationLevelByDemographicId(loggedInInfo, demographicId);
		// hard coded to 3 until some one tells me how to configure/check this
		if (PHR_VERIFICATION_LEVEL_3.equals(level)) return (true);
		else return (false);
	}

	/**
	 * @deprecated there should be a generic call for getDemographicExt(Integer demoId, String key) instead. Then the caller should assemble what it needs from the demographic and ext call itself.
	 */
	public String getDemographicWorkPhoneAndExtension(LoggedInInfo loggedInInfo, Integer demographicNo) {
		Demographic result = demographicDao.getDemographicById(demographicNo);
		String workPhone = result.getPhone2();
		if (workPhone != null && workPhone.length() > 0) {
			String value = demographicExtDao.getValueForDemoKey(demographicNo, "wPhoneExt");
			if (value != null && value.length() > 0) {
				workPhone += "x" + value;
			}
		}

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicWorkPhoneAndExtension", "demographicId=" + result.getDemographicNo() + "result=" + workPhone);
		}

		return (workPhone);
	}

	/**
	 * @see DemographicDao.findByAttributes for parameter details
	 */
	public List<Demographic> searchDemographicsByAttributes(LoggedInInfo loggedInInfo, String hin, String firstName, String lastName, Gender gender, Calendar dateOfBirth, String city, String province, String phone, String email, String alias, int startIndex, int itemsToReturn) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		List<Demographic> results = demographicDao.findByAttributes(hin, firstName, lastName, gender, dateOfBirth, city, province, phone, email, alias, startIndex, itemsToReturn);

		// log all items read
		for (Demographic d : results) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchDemographicsByAttributes result", "demographicNo=" + d.getDemographicNo());
		}

		return (results);
	}

	public List<String> getPatientStatusList() {
		return demographicDao.search_ptstatus();
	}

	public List<String> getRosterStatusList() {
		return demographicDao.getRosterStatuses();
	}

	/**
	 * Search patients via DemographicSearchRequest transfer object.
	 * Used mostly with the REST web services.
	 */
	public List<DemographicSearchResult> searchPatients(LoggedInInfo loggedInInfo, DemographicSearchRequest searchRequest, int startIndex, int itemsToReturn) {
		List<DemographicSearchResult> results = null;
		
		// uncaught exception will be thrown here if the status' are not listed in the properties 
		// file as expected. 
		List<String> notActiveStatus = Arrays.asList( OscarProperties.getInstance().getProperty("inactive_statuses").split(",") );
		
		// For now, searching for a band number implies searching the demographicExt table for statusNum key:value 
		// The general searchPatients method is not robust enough to handle joins.
		// Hence the following:
		if( searchRequest != null && DemographicSearchRequest.SEARCHMODE.BandNumber.equals( searchRequest.getMode() ) ) { 

			List<Demographic> demographics = null;

			if( searchRequest.isActive() ) {
				
				// For future searches, the DemographicSearchRequest transfer class has been wired to transfer First Nation keys - as defined
				// in the DemographicExt Model class FIRST_NATION_KEY ENUM. ie: searchRequest.getFirstNationKey()
				// the findBandNumber methods will need to be modified.
				
				demographics = findActiveDemographicByBandNumber(loggedInInfo, searchRequest.getKeyword(), 
						notActiveStatus, itemsToReturn, startIndex, loggedInInfo.getLoggedInProviderNo(), Boolean.FALSE);
				
			} else {
				
				// Inactive statuses are now determined in a more dynamic modifier within the DemographicDao class.
				// So no need to include them in every call.
				
				demographics = findAllDemographicByBandNumber(loggedInInfo, searchRequest.getKeyword(), 
						itemsToReturn, startIndex, loggedInInfo.getLoggedInProviderNo(), Boolean.FALSE);
				
			}

			results = new ArrayList<DemographicSearchResult>();
			
			// Not too sure about the use of Hibernate (Oscar deprecated ??) Transfer tools in this method.
			// So this is the most neutral method for model class to transfer class 
			
			for (Demographic demographic : demographics) {
				
				DemographicSearchResult demographicSearchResult = new DemographicSearchResult();
				try {
					BeanUtils.copyProperties(demographicSearchResult, demographic);
				} catch (IllegalAccessException e) {
					// Not too sure how exceptions should be logged in this format.
					LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchPatients",  e.getMessage());
				} catch (InvocationTargetException e) {
					LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchPatients",  e.getMessage());
				}
				
				results.add(demographicSearchResult);
				
			}
			
		} else {
			results = demographicDao.searchPatients(loggedInInfo, searchRequest, startIndex, itemsToReturn);
		}

		for (DemographicSearchResult demographic : results) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchPatients result", "demographicId=" + demographic.getDemographicNo());
		}

		return results;
	}

	public int searchPatientsCount(LoggedInInfo loggedInInfo, DemographicSearchRequest searchRequest) {
		return demographicDao.searchPatientCount(loggedInInfo, searchRequest);
	}
	
	/**
	 * Redirecting the DemographicDao multi search through the manager. 
	 */
	public List<Demographic> doMultiSearch(LoggedInInfo loggedInInfo, List<String> searchTypes, List<String> searchStrs, 
			int limit, int offset, String providerNo, boolean outOfDomain, boolean active, boolean inactive, String relatedTo) {
		
		checkPrivilege( loggedInInfo, SecurityInfoManager.READ );
		
		// redirect to compensate for searching demographics by their First Nation's status number.
		if( searchTypes.contains( SearchType.SEARCH_BAND_NUMBER.name().toLowerCase() ) ) {
			
			logger.debug( "Searching by Band/Status number: " + searchStrs );
			
			List<String> demographicNumbers = null;
			// assuming that the (non-ordered list) list order for search types and search strings has not been modified.
			for( int i = 0; i < searchTypes.size(); i++ ) {
				if( SearchType.SEARCH_BAND_NUMBER.name().equalsIgnoreCase( searchTypes.get(i) ) ) {

					List<String> demographicsByBandNumber = this.findDemographicNumbersByBandNumber( loggedInInfo, searchStrs.get(i), limit, offset, providerNo, outOfDomain, active, inactive );
					
					if( demographicsByBandNumber != null ) {
						if( demographicNumbers == null ) {
							demographicNumbers = new ArrayList<String>();
						}
						demographicNumbers.addAll( demographicsByBandNumber );
					}
					
					// remove this band number switch - it confuses the multi-search.
					searchTypes.remove(i);
					searchStrs.remove(i);				
				}
			}
			
			if( demographicNumbers != null ) {
				
				logger.debug( "Adding demographic numbers " + demographicNumbers + " to search strings list." );
				
				int index = searchTypes.size();
				for( int i = 0; i < demographicNumbers.size(); i++ ) {
					searchTypes.add( index, SearchType.SEARCH_DEMOGRAPHIC_NO.name() );
					searchStrs.add( index, demographicNumbers.get(i) );
					index++;
				}
			}
		}

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.doMultiSearch", "Searching for " + searchTypes + " With criteria " + searchStrs );
		
		logger.debug( "Adding Search Types " + searchTypes + " and Search Strings " + searchStrs + " to multisearch method.");
		
		return demographicDao.doMultiSearch( searchTypes, searchStrs, limit, offset, providerNo, outOfDomain, active, inactive, relatedTo);	
	}

	/**
	 * @programId can be null for all/any program
	 */
	public List<Integer> getAdmittedDemographicIdsByProgramAndProvider(LoggedInInfo loggedInInfo, Integer programId, String providerNo) {
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Integer> demographicIds = admissionDao.getAdmittedDemographicIdByProgramAndProvider(programId, providerNo);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getAdmittedDemographicIdsByProgramAndProvider", "programId=" + programId + ", providerNo=" + providerNo);

		return (demographicIds);
	}

	public List<Integer> getDemographicIdsWithMyOscarAccounts(LoggedInInfo loggedInInfo, Integer startDemographicIdExclusive, int itemsToReturn) {
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Integer> demographicIds = demographicDao.getDemographicIdsWithMyOscarAccounts(startDemographicIdExclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicIdsWithMyOscarAccounts", null);

		return (demographicIds);
	}

	public List<Demographic> getDemographics(LoggedInInfo loggedInInfo, List<Integer> demographicIds) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Demographic> demographics = demographicDao.getDemographics(demographicIds);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographics", "demographicIds=" + demographicIds);

		return (demographics);
	}

	public List<Demographic> searchDemographic(LoggedInInfo loggedInInfo, String searchStr) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Demographic> demographics = demographicDao.searchDemographic(searchStr);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchDemographic", "searchStr=" + searchStr);

		return (demographics);
	}

	public List<Demographic> getActiveDemosByHealthCardNo(LoggedInInfo loggedInInfo, String hcn, String hcnType) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Demographic> demographics = demographicDao.getActiveDemosByHealthCardNo(hcn, hcnType);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getActiveDemosByHealthCardNo", "hcn=" + hcn + ",hcnType=" + hcnType);

		return (demographics);
	}

	public List<Integer> getMergedDemographicIds(LoggedInInfo loggedInInfo, Integer demographicNo) {    
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Integer> ids = demographicDao.getMergedDemographics(demographicNo);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getMergedDemographics", "demographicNo=" + demographicNo);

		return ids;
	}

	public List<Demographic> getDemosByChartNo(LoggedInInfo loggedInInfo, String chartNo) {
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));

		List<Demographic> demographics = demographicDao.getClientsByChartNo(chartNo);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getActiveDemosByChartNo", "chartNo=" + chartNo);

		return (demographics);
	}


	public List<Demographic> searchByHealthCard(LoggedInInfo loggedInInfo, String hin) {
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);

		List<Demographic> demographics = demographicDao.searchByHealthCard(hin);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.searchByHealthCard", "hin=" + hin);

		return (demographics);
	}


	public Demographic getDemographicByNamePhoneEmail(LoggedInInfo loggedInInfo, String firstName, String lastName, String hPhone, String wPhone, String email) {
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);

		Demographic demographic =  demographicDao.getDemographicByNamePhoneEmail(firstName, lastName, hPhone, wPhone, email);

		if(demographic != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicByNamePhoneEmail", "id found=" + demographic.getDemographicNo());
		}

		return (demographic);
	}

	public List<Demographic> getDemographicWithLastFirstDOB(LoggedInInfo loggedInInfo, String lastname, String firstname, String year_of_birth, String month_of_birth, String date_of_birth) {
		if (loggedInInfo == null) throw (new SecurityException("user not logged in?"));
		checkPrivilege(loggedInInfo, SecurityInfoManager.READ);

		List<Demographic> results = demographicDao.getDemographicWithLastFirstDOB(lastname, firstname, year_of_birth, month_of_birth, date_of_birth);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getDemographicWithLastFirstDOB", "");


		return (results);
	}

	
	public List<DemographicContact> getHealthCareTeam(LoggedInInfo loggedInInfo, Integer demographicNo) {
		return getHealthCareTeam(loggedInInfo,demographicNo,DemographicContact.CATEGORY_PROFESSIONAL);
	}


	/**
	 * Returns a list of type DemographicContacts that are associated to this demographic number.
	 * DemographicContact.details returns a Contact object that contains the detailed info of each contact.
	 * 
	 * @param loggedInInfo
	 * @param demographicNo
	 * @return DemographicContacts
	 */
	public List<DemographicContact> getHealthCareTeam(LoggedInInfo loggedInInfo, Integer demographicNo, String type) {
		if(demographicNo == null) {
			return null;
		}

		// only professional contacts here.
		List<DemographicContact> demographicContacts = getDemographicContacts( loggedInInfo, demographicNo, type);
		LogAction.addLogSynchronous(loggedInInfo,"DemographicManager.getHealthCareTeam", demographicNo+"");

		return HealthCareTeamCreator.addContactDetailsToDemographicContact( demographicContacts );
	}

	public List<Object[]> getArchiveMeta(LoggedInInfo loggedInInfo, Integer demographicNo) {

		List<Object[]> archiveMeta = demographicArchiveDao.findMetaByDemographicNo(demographicNo);

		LogAction.addLogSynchronous(loggedInInfo, "DemographicManager.getArchiveMeta", ""+demographicNo);

		return archiveMeta;
	}

	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", privilege, null)) {
			throw new RuntimeException("missing required security object (_demographic)");
		}
	}

	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege, int demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", privilege, demographicNo)) {
			throw new RuntimeException("missing required security object (_demographic)");
		}
	}

	/**
	 * Find the provider designated as the primary or most responsible practitioner for this patient.
	 * If the MRP is not indicated this method will return the first available internal provider.
	 * @param loggedInInfo
	 * @param demographicNo
	 * @return DemographicContact
	 */
	public DemographicContact getMostResponsibleProviderFromHealthCareTeam(LoggedInInfo loggedInInfo, Integer demographicNo) {

		if(demographicNo == null) {
			return null;
		}

		DemographicContact mrp = null;
		List<DemographicContact> demographicContacts = getHealthCareTeam(loggedInInfo, demographicNo);

		for(DemographicContact demographicContact : demographicContacts) {
			if( demographicContact.isMrp() ) {
				mrp = demographicContact;
			}
		}

		// can be removed if annoying. This is a back up for when the MRP is 
		// not indicated. If the MRP indicator is not checked this block will grab the
		// first internal provider assigned to the patient file.
		if(mrp == null) {
			for( DemographicContact demographicContact : demographicContacts ) {
				if( demographicContact.getType() == DemographicContact.TYPE_PROVIDER ) {
					mrp = demographicContact;
				}
			}
		}

		LogAction.addLogSynchronous(loggedInInfo,"DemographicManager.getMostResponsibleProviderFromHealthCareTeam", 
				"Retrieving MRP for Demographic " + demographicNo+"");

		return HealthCareTeamCreator.addContactDetailsToDemographicContact( mrp );
	}


	/**
	 * Get Health Care Team Member by a specific role.
	 * @param loggedInInfo
	 * @param demographicNo
	 * @param role (can be an numeric string or alpha string)
	 * @return
	 */
	public DemographicContact getHealthCareMemberbyRole( LoggedInInfo loggedInInfo, Integer demographicNo, String role ) {
		if(demographicNo == null) {
			return null;
		}

		if(role == null) {
			role = "";
		}

		ContactSpecialty contactSpecialty = null;
		String roleId = "";
		DemographicContact contact = null;
		String contactRole = "";
		Integer roleInteger;

		if( StringUtils.isNumeric( role ) ) {
			roleInteger = oscar.util.ConversionUtils.fromIntString(role);
			contactSpecialty = contactSpecialtyDao.find( roleInteger );
			if( contactSpecialty != null ) {
				role = contactSpecialty.getSpecialty();
				roleId = role; 
			}					
		} else {
			contactSpecialty = contactSpecialtyDao.findBySpecialty( role.trim() );
			if( contactSpecialty != null ) {
				roleId = contactSpecialty.getId()+"";
			}
		}

		List<DemographicContact> demographicContacts = getHealthCareTeam(loggedInInfo, demographicNo);			

		for( DemographicContact demographicContact : demographicContacts ) {
			contactRole = demographicContact.getRole();
			if( role.equalsIgnoreCase( contactRole ) || roleId.equalsIgnoreCase( contactRole ) ) {
				contact = demographicContact;
			}				
		}

		return HealthCareTeamCreator.addContactDetailsToDemographicContact( contact );
	}

	public DemographicContact getHealthCareMemberbyId(LoggedInInfo loggedInInfo, Integer demographicContactId) {
		if(demographicContactId == null) {
			return null;
		}

		DemographicContact contact = demographicContactDao.find(demographicContactId);			
		LogAction.addLogSynchronous(loggedInInfo,"DemographicManager.getHealthCareMemberbyId", demographicContactId+"");
		MiscUtils.getLogger().debug("Health Care Contact found." + contact);
		return HealthCareTeamCreator.addContactDetailsToDemographicContact( contact );
	}

}
