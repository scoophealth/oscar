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
package org.oscarehr.ws.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicParameters;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicTransferScore;
import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.WaitingListDao;
import org.oscarehr.common.dao.WaitingListNameDao;
import org.oscarehr.common.exception.PatientDirectiveException;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.WaitingList;
import org.oscarehr.common.model.WaitingListName;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.web.DemographicSearchHelper;
import org.oscarehr.ws.rest.conversion.DemographicContactFewConverter;
import org.oscarehr.ws.rest.conversion.DemographicConverter;
import org.oscarehr.ws.rest.conversion.ProfessionalSpecialistConverter;
import org.oscarehr.ws.rest.conversion.ProviderConverter;
import org.oscarehr.ws.rest.conversion.WaitingListNameConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.DemographicArchiveResponse;
import org.oscarehr.ws.rest.to.OscarSearchResponse;
import org.oscarehr.ws.rest.to.model.DemographicArchiveMeta;
import org.oscarehr.ws.rest.to.model.DemographicContactFewTo1;
import org.oscarehr.ws.rest.to.model.DemographicSearchRequest;
import org.oscarehr.ws.rest.to.model.DemographicSearchRequest.SEARCHMODE;
import org.oscarehr.ws.rest.to.model.DemographicSearchRequest.SORTDIR;
import org.oscarehr.ws.rest.to.model.DemographicSearchRequest.SORTMODE;
import org.oscarehr.ws.rest.to.model.DemographicSearchResult;
import org.oscarehr.ws.rest.to.model.DemographicTo1;
import org.oscarehr.ws.rest.to.model.StatusValueTo1;
import org.oscarehr.ws.rest.to.model.WaitingListNameTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.OscarProperties;
import oscar.oscarWaitingList.util.WLWaitingListUtil;


/**
 * Defines a service contract for main operations on demographic. 
 */
@Path("/demographics")
@Component("demographicService")
public class DemographicService extends AbstractServiceImpl {
	
	
	@Autowired
	private DemographicManager demographicManager;
	
	@Autowired
	private ContactDao contactDao;
	
	@Autowired
	private WaitingListDao waitingListDao;
	
	@Autowired
	private WaitingListNameDao waitingListNameDao;
	
	@Autowired
	private ProviderDao providerDao;
	
	@Autowired
	private SecUserRoleDao secUserRoleDao;
	
	@Autowired
	private ProfessionalSpecialistDao specialistDao;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	@Autowired
	private ProviderManager2 providerManager;
	
	
	private DemographicConverter demoConverter = new DemographicConverter();
	private DemographicContactFewConverter demoContactFewConverter = new DemographicContactFewConverter();
	private WaitingListNameConverter waitingListNameConverter = new WaitingListNameConverter();
	private ProviderConverter providerConverter = new ProviderConverter();
	private ProfessionalSpecialistConverter specialistConverter = new ProfessionalSpecialistConverter();
	
	
	/**
	 * Finds all demographics.
	 * <p/>
	 * In case limit or offset parameters are set to null or zero, the entire result set is returned.
	 * 
	 * @param offset
	 * 		First record in the entire result set to be returned
	 * @param limit
	 * 		Maximum total number of records that should be returned
	 * @return
	 * 		Returns all demographics.
	 */
	@GET
	public OscarSearchResponse<DemographicTo1> getAllDemographics(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
		OscarSearchResponse<DemographicTo1> result = new OscarSearchResponse<DemographicTo1>();
		
		if (offset == null) {
			offset = 0;
		}
		if (limit == null) {
			limit = 0;
		}
		
		result.setLimit(limit);
		result.setOffset(offset);
		result.setTotal(demographicManager.getActiveDemographicCount(getLoggedInInfo()).intValue());
		
		for(Demographic demo : demographicManager.getActiveDemographics(getLoggedInInfo(), offset, limit)) {
			result.getContent().add(demoConverter.getAsTransferObject(getLoggedInInfo(),demo));
		}
		
		return result;
	}

	/**
	 * Gets detailed demographic data.
	 * 
	 * @param id
	 * 		Id of the demographic to get data for 
	 * @return
	 * 		Returns data for the demographic provided 
	 */
	@GET
	@Path("/{dataId}")
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public DemographicTo1 getDemographicData(@PathParam("dataId") Integer id) throws PatientDirectiveException {		
		Demographic demo = demographicManager.getDemographic(getLoggedInInfo(),id);
		if (demo == null) return null;
		
		List<DemographicExt> demoExts = demographicManager.getDemographicExts(getLoggedInInfo(),id);
		if (demoExts!=null && !demoExts.isEmpty()) {
			DemographicExt[] demoExtArray = demoExts.toArray(new DemographicExt[demoExts.size()]);
			demo.setExtras(demoExtArray);
		}

		DemographicTo1 result = demoConverter.getAsTransferObject(getLoggedInInfo(),demo);
		
		DemographicCust demoCust = demographicManager.getDemographicCust(getLoggedInInfo(),id);
		if (demoCust!=null) {
			result.setNurse(demoCust.getNurse());
			result.setResident(demoCust.getResident());
			result.setAlert(demoCust.getAlert());
			result.setMidwife(demoCust.getMidwife());
			result.setNotes(demoCust.getNotes());
		}
		
		List<WaitingList> waitingList = waitingListDao.search_wlstatus(id);
		if (waitingList!=null && !waitingList.isEmpty()) {
			WaitingList wl = waitingList.get(0);
			result.setWaitingListID(wl.getListId());
			result.setWaitingListNote(wl.getNote());
			result.setOnWaitingListSinceDate(wl.getOnListSince());
		}
		
		List<WaitingListName> waitingListNames = waitingListNameDao.findAll(null, null);
		if (waitingListNames!=null) {
			for (WaitingListName waitingListName : waitingListNames) {
				if (waitingListName.getIsHistory().equals("Y")) continue;
				
				WaitingListNameTo1 waitingListNameTo1 = waitingListNameConverter.getAsTransferObject(getLoggedInInfo(),waitingListName);
				result.getWaitingListNames().add(waitingListNameTo1);
			}
		}
		
		List<ProfessionalSpecialist> referralDocs = specialistDao.findAll();
		if (referralDocs!=null) {
			for (ProfessionalSpecialist referralDoc : referralDocs) {
				if(referralDoc != null) {
					result.getReferralDoctors().add(specialistConverter.getAsTransferObject(getLoggedInInfo(),referralDoc));
				}
			}
		}
		
		List<SecUserRole> doctorRoles = secUserRoleDao.getSecUserRolesByRoleName("doctor");
		if (doctorRoles!=null) {
			for (SecUserRole doctor : doctorRoles) {
				Provider provider = providerDao.getProvider(doctor.getProviderNo());
				if(provider != null) {
					result.getDoctors().add(providerConverter.getAsTransferObject(getLoggedInInfo(),provider));
				}
			}
		}
		
		List<SecUserRole> nurseRoles = secUserRoleDao.getSecUserRolesByRoleName("nurse");
		if (nurseRoles!=null) {
			for (SecUserRole nurse : nurseRoles) {
				Provider provider = providerDao.getProvider(nurse.getProviderNo());
				if(provider != null) {
					result.getNurses().add(providerConverter.getAsTransferObject(getLoggedInInfo(),provider));
				}
			}
		}
		
		List<SecUserRole> midwifeRoles = secUserRoleDao.getSecUserRolesByRoleName("midwife");
		if (midwifeRoles!=null) {
			for (SecUserRole midwife : midwifeRoles) {
				Provider provider = providerDao.getProvider(midwife.getProviderNo());
				if(provider != null) {
					result.getMidwives().add(providerConverter.getAsTransferObject(getLoggedInInfo(),provider));
				}
			}
		}
		
		List<DemographicContact> demoContacts = demographicManager.getDemographicContacts(getLoggedInInfo(),id);
		if (demoContacts!=null) {
			for (DemographicContact demoContact : demoContacts) {
				Integer contactId = Integer.valueOf(demoContact.getContactId());
				DemographicContactFewTo1 demoContactTo1 = new DemographicContactFewTo1();
				
				if (demoContact.getCategory().equals(DemographicContact.CATEGORY_PERSONAL)) {
					if (demoContact.getType()==DemographicContact.TYPE_DEMOGRAPHIC) {
						Demographic contactD = demographicManager.getDemographic(getLoggedInInfo(),contactId);
						demoContactTo1 = demoContactFewConverter.getAsTransferObject(demoContact, contactD);
						if (demoContactTo1.getPhone()==null || demoContactTo1.getPhone().equals("")) {
							DemographicExt ext = demographicManager.getDemographicExt(getLoggedInInfo(), id, "demo_cell");
							if (ext!=null) demoContactTo1.setPhone(ext.getValue());
						}
					}
					else if (demoContact.getType()==DemographicContact.TYPE_CONTACT) {
						Contact contactC = contactDao.find(contactId);
						demoContactTo1 = demoContactFewConverter.getAsTransferObject(demoContact, contactC);
					}
					result.getDemoContacts().add(demoContactTo1);
				}
				else if (demoContact.getCategory().equals(DemographicContact.CATEGORY_PROFESSIONAL)) {
					if (demoContact.getType()==DemographicContact.TYPE_PROVIDER) {
						Provider contactP = providerDao.getProvider(contactId.toString());
						demoContactTo1 = demoContactFewConverter.getAsTransferObject(demoContact, contactP);
					}
					else if (demoContact.getType()==DemographicContact.TYPE_PROFESSIONALSPECIALIST) {
						ProfessionalSpecialist contactS = specialistDao.find(contactId);
						demoContactTo1 = demoContactFewConverter.getAsTransferObject(demoContact, contactS);
					}
					result.getDemoContactPros().add(demoContactTo1);
				}
			}
		}
		
		List<String> patientStatusList = demographicManager.getPatientStatusList();
		List<String> rosterStatusList = demographicManager.getRosterStatusList();
		if (patientStatusList!=null) {
			for (String ps : patientStatusList) {
				StatusValueTo1 value = new StatusValueTo1(ps);
				result.getPatientStatusList().add(value);
			}
		}
		if (rosterStatusList!=null) {
			for (String rs : rosterStatusList) {
				StatusValueTo1 value = new StatusValueTo1(rs);
				result.getRosterStatusList().add(value);
			}
		}
		return result;
	}

	/**
	 * Saves demographic information. 
	 * 
	 * @param data
	 * 		Detailed demographic data to be saved
	 * @return
	 * 		Returns the saved demographic data
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public DemographicTo1 createDemographicData(DemographicTo1 data) {
		Demographic demographic = demoConverter.getAsDomainObject(getLoggedInInfo(), data);
		demographicManager.createDemographic(getLoggedInInfo(), demographic, data.getAdmissionProgramId());
	    return demoConverter.getAsTransferObject(getLoggedInInfo(), demographic);
	}

	/**
	 * Updates demographic information. 
	 * 
	 * @param data
	 * 		Detailed demographic data to be updated
	 * @return
	 * 		Returns the updated demographic data
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public DemographicTo1 updateDemographicData(DemographicTo1 data) {
		//update demographiccust
		if (data.getNurse()!=null || data.getResident()!=null || data.getAlert()!=null || data.getMidwife()!=null || data.getNotes()!=null) {
			DemographicCust demoCust = demographicManager.getDemographicCust(getLoggedInInfo(),data.getDemographicNo());
			if (demoCust==null) {
				demoCust = new DemographicCust();
				demoCust.setId(data.getDemographicNo());
			}
			demoCust.setNurse(data.getNurse());
			demoCust.setResident(data.getResident());
			demoCust.setAlert(data.getAlert());
			demoCust.setMidwife(data.getMidwife());
			demoCust.setNotes(data.getNotes());
			demographicManager.createUpdateDemographicCust(getLoggedInInfo(),demoCust);
		}
		
		//update waitingList
		if (data.getWaitingListID()!=null) {
			WLWaitingListUtil.updateWaitingListRecord(data.getWaitingListID().toString(), data.getWaitingListNote(), data.getDemographicNo().toString(), null);
		}
		
		Demographic demographic = demoConverter.getAsDomainObject(getLoggedInInfo(),data);
	    demographicManager.updateDemographic(getLoggedInInfo(),demographic);
	    
	    return demoConverter.getAsTransferObject(getLoggedInInfo(),demographic);
	}

	/**
	 * Deletes demographic information. 
	 * 
	 * @param id
	 * 		Id of the demographic data to be deleted
	 * @return
	 * 		Returns the deleted demographic data
	 */
	@DELETE
	@Path("/{dataId}")
	public DemographicTo1 deleteDemographicData(@PathParam("dataId") Integer id) {
		Demographic demo = demographicManager.getDemographic(getLoggedInInfo(),id);
    	DemographicTo1 result = getDemographicData(id);
    	if (demo == null) {
    		throw new IllegalArgumentException("Unable to find demographic record with ID " + id);
    	}
    	
		demographicManager.deleteDemographic(getLoggedInInfo(),demo);
	    return result;
	}

	/**
	 * Search demographics - used by navigation of OSCAR webapp
	 * 
	 * Currently supports LastName[,FirstName] and address searches.
	 * 
	 * @param id
	 * 		Id of the demographic to get data for 
	 * @return
	 * 		Returns data for the demographic provided 
	 */
	@GET
	@Path("/quickSearch")
	@Produces("application/json")
	public AbstractSearchResponse<DemographicSearchResult> search(@QueryParam("query") String query) {		

		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_demographic", "r", null)) {
				throw new RuntimeException("Access Denied");
		}
		
		AbstractSearchResponse<DemographicSearchResult> response = new AbstractSearchResponse<DemographicSearchResult>();
		
		List<DemographicSearchResult> results = new ArrayList<DemographicSearchResult>();
		
		if(query == null ) {
			return response;
		}
		
		DemographicSearchRequest req = new DemographicSearchRequest();
		req.setActive(true);
		req.setIntegrator(false); //this should be configurable by persona
		
		//caisi
		boolean outOfDomain = true;
		if(OscarProperties.getInstance().getProperty("ModuleNames","").indexOf("Caisi") != -1) {
			outOfDomain=false;
		}
		req.setOutOfDomain(outOfDomain);
		
		

		if(query.startsWith("addr:")) {
			req.setMode(SEARCHMODE.Address);
			req.setKeyword(query.substring("addr:".length()));
		} else if(query.startsWith("chartNo:")) {
			req.setMode(SEARCHMODE.ChartNo);
			req.setKeyword(query.substring("chartNo:".length()));
		} else {
			req.setMode(SEARCHMODE.Name);
			req.setKeyword(query);
		}
		
				
		int count = demographicManager.searchPatientsCount(getLoggedInInfo(),req);

		if(count>0) {
			results = demographicManager.searchPatients(getLoggedInInfo(),req, 0, 10);
			response.setContent(results);
			response.setTotal(count);
			response.setQuery(query);
			
		}
		
		
		return response;
	}
	
	@POST
	@Path("/search")
	@Produces("application/json")
	@Consumes("application/json")
	public AbstractSearchResponse<DemographicSearchResult> search(JSONObject json,@QueryParam("startIndex") Integer startIndex,@QueryParam("itemsToReturn") Integer itemsToReturn ) {
		AbstractSearchResponse<DemographicSearchResult> response = new AbstractSearchResponse<DemographicSearchResult>();
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_demographic", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		DemographicSearchRequest req = convertFromJSON(json);
		//caisi
		boolean outOfDomain = true;
		if(OscarProperties.getInstance().getProperty("ModuleNames","").indexOf("Caisi") != -1) {
			outOfDomain=false;
		}
		req.setOutOfDomain(outOfDomain);
				
		
		List<DemographicSearchResult> results = new ArrayList<DemographicSearchResult>();
		
		if(json.getString("term").length() >= 1) {
				
			int count = 0;
			
			// this version of JPA cannot build join queries. This pre-query will not work with Demographic Extended data.
			if( DemographicSearchRequest.SEARCHMODE.BandNumber.equals( req.getMode() ) ) {
				count = 1;
			} else {
				count = demographicManager.searchPatientsCount(getLoggedInInfo(), req);
			}
			
			if(count>0) {
				
				results = demographicManager.searchPatients(getLoggedInInfo(), req, startIndex, itemsToReturn);
				
				if( DemographicSearchRequest.SEARCHMODE.BandNumber.equals( req.getMode() ) ) {
					count = results.size();
				}
				
				response.setContent(results);
				response.setTotal(count);
			}
		}
		
		return response;
	}
	
	@POST
	@Path("/searchIntegrator")
	@Produces("application/json")
	@Consumes("application/json")
	public AbstractSearchResponse<DemographicSearchResult> searchIntegrator(JSONObject json, @QueryParam("itemsToReturn") Integer itemsToReturn ) {
		AbstractSearchResponse<DemographicSearchResult> response = new AbstractSearchResponse<DemographicSearchResult>();
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_demographic", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		List<DemographicSearchResult> results = new ArrayList<DemographicSearchResult>();
		
		if(json.getString("term").length() >= 1) {
			
			MatchingDemographicParameters matches = CaisiIntegratorManager.getMatchingDemographicParameters(getLoggedInInfo(), convertFromJSON(json));
			List<MatchingDemographicTransferScore> integratorSearchResults = null;
			try {			
				matches.setMaxEntriesToReturn(itemsToReturn);
				matches.setMinScore(7); 
				integratorSearchResults = DemographicSearchHelper.getIntegratedSearchResults(getLoggedInInfo(), matches);
				MiscUtils.getLogger().info("Integrator search results : "+(integratorSearchResults==null?"null":String.valueOf(integratorSearchResults.size())));
			}catch(Exception e){
				MiscUtils.getLogger().error("error searching integrator", e);
			}
			
			if(integratorSearchResults != null) {
				 for (MatchingDemographicTransferScore matchingDemographicTransferScore : integratorSearchResults) {
					if( isLocal(matchingDemographicTransferScore)) { 	
						MiscUtils.getLogger().warn("ignoring remote demographic since we already have them locally");
						 continue;     
					}
					 if(matchingDemographicTransferScore.getDemographicTransfer() != null) {
						 DemographicTransfer obj = matchingDemographicTransferScore.getDemographicTransfer();
						 DemographicSearchResult item = new DemographicSearchResult();
						 item.setLastName(obj.getLastName());
						 item.setFirstName(obj.getFirstName());
						 item.setSex(obj.getGender().toString());
						 item.setDob(obj.getBirthDate().getTime());
						 item.setRemoteFacilityId(obj.getIntegratorFacilityId());
						 item.setDemographicNo(obj.getCaisiDemographicId());
						 results.add(item);
					 }
					 
				 }
				 
			}
			
		}
		
		 response.setContent(results);	
		 response.setTotal((response.getContent()!=null)?response.getContent().size():0);
		
		return response;
	}
	
	private DemographicSearchRequest convertFromJSON(JSONObject json) {
		if(json ==null)return null;
		
		String searchType = json.getString("type");
				
		DemographicSearchRequest req = new DemographicSearchRequest();
		
		req.setMode(SEARCHMODE.valueOf(searchType));
		if(req.getMode() == null) {
			req.setMode(SEARCHMODE.Name);
		}
		
		req.setKeyword(json.getString("term"));
		req.setActive(Boolean.valueOf(json.getString("active")));
		req.setIntegrator(Boolean.valueOf(json.getString("integrator")));
		req.setOutOfDomain(Boolean.valueOf(json.getString("outofdomain")));
		
		Pattern namePtrn = Pattern.compile("sorting\\[(\\w+)\\]");
		
		JSONObject params = json.getJSONObject("params");
		if(params != null) {
			for(Object key:params.keySet()) {
				Matcher nameMtchr = namePtrn.matcher((String)key);
				if (nameMtchr.find()) {
				   String var = nameMtchr.group(1);
				   req.setSortMode(SORTMODE.valueOf(var));
				   req.setSortDir(SORTDIR.valueOf(params.getString((String)key)));
				}
			}
		}
		return req;
	}
	
	private boolean isLocal(MatchingDemographicTransferScore matchingDemographicTransferScore) {
	    String hin = matchingDemographicTransferScore.getDemographicTransfer().getHin(); 
	    
	    if(hin != null && !hin.isEmpty()) {
		    DemographicSearchRequest dsr = new DemographicSearchRequest();
		    dsr.setActive(true);
		    dsr.setKeyword(hin);
		    dsr.setMode(SEARCHMODE.HIN);
		    dsr.setOutOfDomain(true);
		    dsr.setSortMode(SORTMODE.Name);
		    dsr.setSortDir(SORTDIR.asc);

		    if(demographicManager.searchPatientsCount(getLoggedInInfo(), dsr) > 0) {
		    	return true;
		    }
	    }

	    return false;
	    
	}
	
	@POST
	@Path("/historyList")
	@Produces("application/json")
	@Consumes("application/json")
	public DemographicArchiveResponse getHistoryList(JSONObject json) {
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_demographic", "r", null)) {
			throw new SecurityException("Access Denied");
		}
		
		Integer demographicNo = null;
		if(json.containsKey("demographicNo")) {
			demographicNo = json.getInt("demographicNo");
		}
		
		if(demographicNo == null) {
			throw new RuntimeException("demographicNo must be present in json");
		}
		
		List<Object[]> archiveMeta = demographicManager.getArchiveMeta(getLoggedInInfo(), demographicNo);
		
		DemographicArchiveResponse response = new DemographicArchiveResponse();
		for(Object[] meta: archiveMeta) {
			DemographicArchiveMeta m = new DemographicArchiveMeta();
			m.setId((Long)meta[0]);
			m.setDemographicNo((Integer)meta[1]);
			m.setLastUpdateUser((String)meta[2]);
			m.setLastUpdateDate((Date)meta[3]);
			Provider p = providerManager.getProvider(getLoggedInInfo(), (String)meta[2]);
			if(p != null) {
				m.setLastUpdateUserName(p.getFormattedName());
			}
			response.getMetadata().add(m);
		}
		
		return response;
	}
}
