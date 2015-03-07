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
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.ConsultationManager;
import org.oscarehr.managers.MessagingManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.conversion.ProgramProviderConverter;
import org.oscarehr.ws.rest.conversion.SecobjprivilegeConverter;
import org.oscarehr.ws.rest.conversion.SecuserroleConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.DashboardPreferences;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.NavbarResponse;
import org.oscarehr.ws.rest.to.PatientList;
import org.oscarehr.ws.rest.to.PersonaResponse;
import org.oscarehr.ws.rest.to.PersonaRightsResponse;
import org.oscarehr.ws.rest.to.PrimitiveResponseWrapper;
import org.oscarehr.ws.rest.to.model.MenuItemTo1;
import org.oscarehr.ws.rest.to.model.MenuTo1;
import org.oscarehr.ws.rest.to.model.NavBarMenuTo1;
import org.oscarehr.ws.rest.to.model.ProgramProviderTo1;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/persona")
public class PersonaService extends AbstractServiceImpl {

	
	@Autowired
	private ProgramManager2 programManager2;
	
	@Autowired
	private MessagingManager messagingManager;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	@Autowired
	private ConsultationManager consultationManager;
	
	
	@GET
	@Path("/rights")
	@Produces("application/json")
	public PersonaRightsResponse getMyRights() {
		PersonaRightsResponse response = new PersonaRightsResponse();

		SecuserroleConverter converter = new SecuserroleConverter();
		response.setRoles(converter.getAllAsTransferObjects(getLoggedInInfo(),securityInfoManager.getRoles(getLoggedInInfo())));
		
		SecobjprivilegeConverter converter2 = new SecobjprivilegeConverter();
		response.setPrivileges(converter2.getAllAsTransferObjects(getLoggedInInfo(),securityInfoManager.getSecurityObjects(getLoggedInInfo())));
			
		return response;
	}
	
	@GET
	@Path("/hasRight")
	@Produces("application/json")
	public PrimitiveResponseWrapper<Boolean> hasRight(@QueryParam("objectName") String objectName, @QueryParam("privilege") String privilege, @QueryParam("demographicNo") String demographicNo) {
		PrimitiveResponseWrapper<Boolean> response = new PrimitiveResponseWrapper<Boolean>();
		response.setValue(securityInfoManager.hasPrivilege(getLoggedInInfo(), objectName, privilege, demographicNo));
		
		return response;
	}
	
	@POST
	@Path("/hasRights")
	@Consumes("application/json")
	@Produces("application/json")
	public AbstractSearchResponse<Boolean> hasRights(JSONObject json) {
		AbstractSearchResponse<Boolean> response = new AbstractSearchResponse<Boolean>();
		
		JSONArray ja = json.getJSONArray("items");
		for(int x=0;x<ja.size();x++) {
			JSONObject o = (JSONObject)ja.get(x);
			String objectName = o.getString("objectName");
			String privilege = o.getString("privilege");
			Integer demographicNo = null;
			if(o.has("demographicNo")) {
				demographicNo = o.getInt("demographicNo");
			}
			response.getContent().add(securityInfoManager.hasPrivilege(getLoggedInInfo(), objectName, privilege, (demographicNo!=null)?demographicNo.toString():null));
		}
		response.setTotal(response.getContent().size());
		
		return response;
	}
	
	@GET
	@Path("/navbar")
	@Produces("application/json")
	public NavbarResponse getMyNavbar() {
		Provider provider = getCurrentProvider();
		ResourceBundle bundle = getResourceBundle();
		
		NavbarResponse result = new NavbarResponse();
		
		/* program domain, current program */
		List<ProgramProvider> ppList = programManager2.getProgramDomain(getLoggedInInfo(),provider.getProviderNo());
		ProgramProviderConverter ppConverter = new ProgramProviderConverter();
		List<ProgramProviderTo1> programDomain = new ArrayList<ProgramProviderTo1>();
		
		for(ProgramProvider pp:ppList) {
			programDomain.add(ppConverter.getAsTransferObject(getLoggedInInfo(),pp));
		}
		result.setProgramDomain(programDomain);
		
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(getLoggedInInfo(),provider.getProviderNo());
		if(pp != null) {
			ProgramProviderTo1 ppTo = ppConverter.getAsTransferObject(getLoggedInInfo(),pp);
			result.setCurrentProgram(ppTo);
		} else {
			if(result.getProgramDomain() != null && result.getProgramDomain().size()>0) {
				result.setCurrentProgram(result.getProgramDomain().get(0));
			}
		}
		
		/* counts */
		
		int messageCount = messagingManager.getMyInboxMessageCount(getLoggedInInfo(),provider.getProviderNo(), false);
		int ptMessageCount = messagingManager.getMyInboxMessageCount(getLoggedInInfo(),provider.getProviderNo(),true);
		result.setUnreadMessagesCount(messageCount);
		result.setUnreadPatientMessagesCount(ptMessageCount);
		
		
		/* this is manual right now. Need to have this generated from some kind
		 * of user data
		 */
		NavBarMenuTo1 navBarMenu = new NavBarMenuTo1();
		
		MenuTo1 patientSearchMenu = new MenuTo1().add(0,bundle.getString("navbar.menu.newPatient"),null,"#/newpatient")
				.add(1,bundle.getString("navbar.menu.advancedSearch"),null,"#/search");
		navBarMenu.setPatientSearchMenu(patientSearchMenu);
		
		int idCounter = 0;
		
		MenuTo1 menu = new MenuTo1()
		        .add(idCounter++,bundle.getString("navbar.menu.schedule"),null,"../provider/providercontrol.jsp")
				.addWithState(idCounter++,bundle.getString("navbar.menu.inbox"),null,"inbox");

		if (!consultationManager.isConsultResponseEnabled()) {
			menu.addWithState(idCounter++,bundle.getString("navbar.menu.consults"),null,"consultRequests");
		}
		else if (!consultationManager.isConsultRequestEnabled()) {
			menu.addWithState(idCounter++,bundle.getString("navbar.menu.consultResponses"),null,"consultResponses");
		}

		//consult menu
		if (consultationManager.isConsultRequestEnabled() && consultationManager.isConsultResponseEnabled()) {
			MenuItemTo1 consultMenu = new MenuItemTo1(idCounter++, bundle.getString("navbar.menu.consults"), null);
			consultMenu.setDropdown(true);
			MenuTo1 consultMenuList = new MenuTo1()
					.addWithState(idCounter++,bundle.getString("navbar.menu.consultRequests"),null,"consultRequests")
					.addWithState(idCounter++,bundle.getString("navbar.menu.consultResponses"),null,"consultResponses");
			consultMenu.setDropdownItems(consultMenuList.getItems());
			menu.getItems().add(consultMenu);
		}
		
		menu.addWithState(idCounter++,bundle.getString("navbar.menu.billing"),null,"billing")
			.addWithState(idCounter++,bundle.getString("navbar.menu.tickler"),null,"ticklers")
			
			//.add(0,"K2A",null,"#/k2a")
			.addWithState(idCounter++,bundle.getString("navbar.menu.admin"),null,"admin");

		MenuItemTo1 moreMenu = new MenuItemTo1(idCounter++, bundle.getString("navbar.menu.more"), null);
		moreMenu.setDropdown(true);
		
		MenuTo1 moreMenuList = new MenuTo1()
		.addWithState(idCounter++,bundle.getString("navbar.menu.reports"),null,"reports")
		.addWithState(idCounter++,bundle.getString("navbar.menu.documents"),null,"documents");
		moreMenu.setDropdownItems(moreMenuList.getItems());
		menu.getItems().add(moreMenu);
		navBarMenu.setMenu(menu);
	
		MenuTo1 userMenu = new MenuTo1()
		.addWithState(0,bundle.getString("navbar.menu.settings"),null,"settings")
		.addWithState(1,bundle.getString("navbar.menu.support"),null,"support")
		.addWithState(2,bundle.getString("navbar.menu.help"),null,"help");
		navBarMenu.setUserMenu(userMenu);
		
		result.setMenus(navBarMenu);
		
		return result;
	}

	@GET
	@Path("/setDefaultProgramInDomain")
	public GenericRESTResponse setDefaultProgram(@QueryParam("programId") Integer programId) {
		programManager2.setCurrentProgramInDomain(getLoggedInInfo().getLoggedInProviderNo(), programId);
		return new GenericRESTResponse();
	}
	
	@GET
	@Path("/patientLists")
	@Produces("application/json")
	public PersonaResponse getMyPatientLists() {
		Provider provider = getCurrentProvider();
		ResourceBundle bundle = getResourceBundle();
		
		
		PersonaResponse response = new PersonaResponse();

		response.getPatientListTabItems().add(new PatientList(0,bundle.getString("patientList.tab.appts"),"../ws/rs/schedule/day/today","patientlist/patientList1.jsp","GET"));
		response.getPatientListTabItems().add(new PatientList(1,bundle.getString("patientList.tab.recent"),"../ws/rs/providerService/getRecentDemographicsViewed?startIndex=0&itemsToReturn=100","patientlist/recent.jsp","GET"));
		
		response.getPatientListMoreTabItems().add(new PatientList(0,bundle.getString("patientList.tab.patientSets"),"../ws/rs/reporting/demographicSets/patientList","patientlist/demographicSets.jsp","POST"));
		response.getPatientListMoreTabItems().add(new PatientList(1,bundle.getString("patientList.tab.caseload"),null,"patientlist/program.jsp",null));
		
		return response;
	}
	
	/**
	 * This will be a REST based way to get access to groups of preferences. It's not fully implemented yet
	 * 
	 * @param obj
	 * @return
	 */
	@POST
	@Path("/preferences")
	@Produces("application/json")
	@Consumes("application/json")
	public PersonaResponse getPreferences(JSONObject obj) {
		Provider provider = getCurrentProvider();
		
		//not yet used..need a way to just load specific groups of properties.
		String type = obj.getString("type");
		
		PersonaResponse response = new PersonaResponse();
		DashboardPreferences prefs = new DashboardPreferences();
		
		//this needs to be more structured after the alpha. Create a manager a way to load with defaults
		UserPropertyDAO propDao =(UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
		String strVal = propDao.getStringValue(provider.getProviderNo(), "dashboard.expiredTicklersOnly");
		if(strVal == null) {
			prefs.setExpiredTicklersOnly(true);
		}
		else if(strVal != null && "true".equalsIgnoreCase(strVal)) {
			prefs.setExpiredTicklersOnly(true);
		}
		
		response.setDashboardPreferences(prefs);

		return response;
	}
	
	@POST
	@Path("/updatePreferences")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse updatePreferences(JSONObject json){
		Provider provider = getCurrentProvider();
		GenericRESTResponse response = new GenericRESTResponse();
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_pref", "u", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		Boolean value = null;
		
		if(json.has("expiredTicklersOnly")) {
			value = json.getBoolean("expiredTicklersOnly");
		}
		
		if(value != null) {

			UserPropertyDAO propDao =(UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
			UserProperty prop = propDao.getProp(provider.getProviderNo(), "dashboard.expiredTicklersOnly");
			if(prop != null) {
				prop.setValue(String.valueOf(value));
			} else {
				prop = new UserProperty();
				prop.setName("dashboard.expiredTicklersOnly");
				prop.setProviderNo(provider.getProviderNo());
				prop.setValue(String.valueOf(value));
			}
			
			propDao.saveProp(prop);
			
			response.setSucess(true);
		} else {
			response.setSucess(false);
		}
		
		
		return response;
	
	}
}

