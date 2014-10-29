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
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.MessagingManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.ws.rest.conversion.ProgramProviderConverter;
import org.oscarehr.ws.rest.conversion.SecobjprivilegeConverter;
import org.oscarehr.ws.rest.conversion.SecuserroleConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.NavbarResponse;
import org.oscarehr.ws.rest.to.PatientList;
import org.oscarehr.ws.rest.to.PersonaResponse;
import org.oscarehr.ws.rest.to.PersonaRightsResponse;
import org.oscarehr.ws.rest.to.PrimitiveResponseWrapper;
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
		
		MenuTo1 menu = new MenuTo1()
				.addWithState(0,bundle.getString("navbar.menu.inbox"),null,"inbox")
				.addWithState(1,bundle.getString("navbar.menu.consults"),null,"consults")
				.addWithState(2,bundle.getString("navbar.menu.billing"),null,"billing")
				.addWithState(3,bundle.getString("navbar.menu.tickler"),null,"ticklers")
				.addWithState(4,bundle.getString("navbar.menu.schedule"),null,"schedule")
				//.add(0,"K2A",null,"#/k2a")
				.addWithState(5,bundle.getString("navbar.menu.admin"),null,"admin");
		navBarMenu.setMenu(menu);
	/*	
		MenuTo1 moreMenu = new MenuTo1()
		.add(1,"Caseload",null,"#/caseload")
		.add(2,"Resources",null,"#/resources")
		navBarMenu.setMoreMenu(moreMenu);
		*/
		MenuTo1 moreMenu2 = new MenuTo1()
		.addWithState(0,bundle.getString("navbar.menu.reports"),null,"reports")
		.addWithState(1,bundle.getString("navbar.menu.documents"),null,"documents");
		navBarMenu.setMoreMenu(moreMenu2);
		
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
		
		PersonaResponse response = new PersonaResponse();

		response.getPatientListTabItems().add(new PatientList(0,"Appts.","../ws/rs/schedule/day/today","patientlist/patientList1.jsp","GET"));
		response.getPatientListTabItems().add(new PatientList(1,"Recent","../ws/rs/providerService/getRecentDemographicsViewed?startIndex=0&itemsToReturn=100","patientlist/recent.jsp","GET"));
		
		response.getPatientListMoreTabItems().add(new PatientList(0,"Patient Sets","../ws/rs/reporting/demographicSets/patientList","patientlist/demographicSets.html","POST"));
		response.getPatientListMoreTabItems().add(new PatientList(1,"Caseload",null,"patientlist/program.jsp",null));
		
		return response;
	}
	
}

