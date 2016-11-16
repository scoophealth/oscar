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
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.AllergyManager;
import org.oscarehr.managers.PrescriptionManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;

public class ConsultationClinicalDataAction extends DispatchAction  {
	
	private static Logger logger = MiscUtils.getLogger();
	private PrescriptionManager prescriptionManager = SpringUtils.getBean(PrescriptionManager.class);
	private CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
	private AllergyManager allergyManager = SpringUtils.getBean( AllergyManager.class ); 
	
	public ConsultationClinicalDataAction() {
		// Default
	}
	
	@SuppressWarnings("unused")
	public ActionForward fetchMedications(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String demographicNo = request.getParameter("demographicNo");

		List<Drug> medications = prescriptionManager.getActiveMedications( loggedInInfo, demographicNo );
		
		if( medications != null ) {
			medicationToJson( response, medications, "Medications" );
		}
		
        return null;
	}
	
	@SuppressWarnings("unused")
	public ActionForward fetchLongTermMedications(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String demographicNo = request.getParameter("demographicNo");
		List<Drug> medications = prescriptionManager.getLongTermDrugs( loggedInInfo, Integer.parseInt( demographicNo ) );
		
		if( medications != null ) {
			medicationToJson( response, medications, "LongTermMedications" );
		}
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public ActionForward fetchAllergies(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);		
		String demographicNo = request.getParameter("demographicNo");
		
		List<Allergy> allergies = allergyManager.getActiveAllergies( loggedInInfo, Integer.parseInt( demographicNo ) );

		JSONObject json = new JSONObject();
		json.put("noteType", "Allergies");
		StringBuilder stringBuilder = new StringBuilder();
		String allergin = null;
		String reaction = null;
		
		for( Allergy allergy : allergies ) {
			
			allergin = allergy.getDescription();
			reaction = allergy.getReaction();
			
			if( allergin != null ) {
				stringBuilder.append( WordUtils.capitalizeFully( allergin ) );
			}
			
			if(reaction != null ) {				
				reaction = reaction.replace("\n", " ").replace("\r", " ");
				stringBuilder.append(" Reaction: ");
				stringBuilder.append( reaction );
			}
			
			stringBuilder.append( "\n" );
		}
		
		json.put("note", stringBuilder.toString());
	
		response.setContentType("text/javascript");
		
        try {
			response.getWriter().write( json.toString() );
		} catch (IOException e) {
			logger.error("Authentication error: ", e);
		}
				
		return null;
	}
	
	@SuppressWarnings("unused")
	public ActionForward fetchRiskFactors(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String demographicNo = request.getParameter("demographicNo");
		String noteType = "RiskFactors";
		
		Issue issue = caseManagementManager.getIssueByCode( noteType );
		List<CaseManagementNote> riskFactors = caseManagementManager.getNotes( loggedInInfo, demographicNo, new String[]{ issue.getId()+"" } );
		
		JSONObject json = new JSONObject();
		json.put( "noteType", noteType );
		StringBuilder stringBuilder = new StringBuilder();
		
		if( riskFactors != null ) {
			for(CaseManagementNote riskFactor : riskFactors) {
				stringBuilder.append( riskFactor.getNote() );
				stringBuilder.append("\n");
			}
		}
		
		json.put("note", stringBuilder.toString());
		
		response.setContentType("text/javascript");
        try {
			response.getWriter().write( json.toString() );
		} catch (IOException e) {
			logger.error("Authentication error: ", e);
		}
        
		return null;
	}
	
	
	private void medicationToJson( HttpServletResponse response, List<Drug> medications, String notetype ) {
		
		JSONObject json = new JSONObject();
		json.put("noteType", notetype);
		StringBuilder stringBuilder = new StringBuilder();
		String prescription = null;
		
		for( Drug medication : medications ) {
			
			if( medication.isCurrent() ) {
			
				prescription = medication.getSpecial();
				
				if( prescription != null ) {
					prescription = prescription.replace("\n", " ").replace("\r", " ");
					stringBuilder.append( WordUtils.capitalizeFully( prescription ) + "\n" );
				}
			
			}
		}
		
		json.put("note", stringBuilder.toString());
	
		response.setContentType("text/javascript");
        try {
			response.getWriter().write( json.toString() );
		} catch (IOException e) {
			logger.error("Authentication error: ", e);
		}
	}

}
