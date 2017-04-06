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


package oscar.eform.actions;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.EformDataManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.match.IMatchManager;
import org.oscarehr.match.MatchManager;
import org.oscarehr.match.MatchManagerException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormLoader;
import oscar.eform.EFormUtil;
import oscar.eform.data.DatabaseAP;
import oscar.eform.data.EForm;
import oscar.oscarEncounter.data.EctProgram;
import oscar.util.StringUtils;


public class AddEFormAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private EformDataManager eformDataManager = SpringUtils.getBean( EformDataManager.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
		
		logger.debug("==================SAVING ==============");
		HttpSession se = request.getSession();

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		boolean fax = "true".equals(request.getParameter("faxEForm"));
		boolean print = "true".equals(request.getParameter("print"));
		boolean saveAsEdoc = "true".equals( request.getParameter("saveAsEdoc") );

		@SuppressWarnings("unchecked")
		Enumeration<String> paramNamesE = request.getParameterNames();
		//for each name="fieldname" value="myval"
		ArrayList<String> paramNames = new ArrayList<String>();  //holds "fieldname, ...."
		ArrayList<String> paramValues = new ArrayList<String>(); //holds "myval, ...."
		String fid = request.getParameter("efmfid");
		String demographic_no = request.getParameter("efmdemographic_no");
		String eform_link = request.getParameter("eform_link");
		String subject = request.getParameter("subject");

		boolean doDatabaseUpdate = false;

		List<String> oscarUpdateFields = new ArrayList<String>();

		if (request.getParameter("_oscardodatabaseupdate") != null && request.getParameter("_oscardodatabaseupdate").equalsIgnoreCase("on"))
			doDatabaseUpdate = true;

		ActionMessages updateErrors = new ActionMessages();

		// The fields in the _oscarupdatefields parameter are separated by %s.
		if (!print && !fax && doDatabaseUpdate && request.getParameter("_oscarupdatefields") != null) {

			oscarUpdateFields = Arrays.asList(request.getParameter("_oscarupdatefields").split("%"));

			boolean validationError = false;

			for (String field : oscarUpdateFields) {
				EFormLoader.getInstance();
				// Check for existence of appropriate databaseap
				DatabaseAP currentAP = EFormLoader.getAP(field);
				if (currentAP != null) {
					if (!currentAP.isInputField()) {
						// Abort! This field can't be updated
						updateErrors.add(field, new ActionMessage("errors.richeForms.noInputMethodError", field));
						validationError = true;
					}
				} else {
					// Field doesn't exit
					updateErrors.add(field, new ActionMessage("errors.richeForms.noSuchFieldError", field));
					validationError = true;
				}
			}

			if (!validationError) {
				for (String field : oscarUpdateFields) {
					EFormLoader.getInstance();
					DatabaseAP currentAP = EFormLoader.getAP(field);
					// We can add more of these later...
					if (currentAP != null) {
						String inSQL = currentAP.getApInSQL();

						inSQL = DatabaseAP.parserReplace("demographic", demographic_no, inSQL);
						inSQL = DatabaseAP.parserReplace("provider", providerNo, inSQL);
						inSQL = DatabaseAP.parserReplace("fid", fid, inSQL);

						inSQL = DatabaseAP.parserReplace("value", request.getParameter(field), inSQL);

						//if(currentAP.getArchive() != null && currentAP.getArchive().equals("demographic")) {
						//	demographicArchiveDao.archiveRecord(demographicManager.getDemographic(loggedInInfo,demographic_no));
						//}

						// Run the SQL query against the database
						//TODO: do this a different way.
						MiscUtils.getLogger().error("Error",new Exception("EForm is using disabled functionality for updating fields..update not performed"));
					}
				}
			}
		}

		if (subject == null) subject="";
		String curField = "";
		while (paramNamesE.hasMoreElements()) {
			curField = paramNamesE.nextElement();
			if( curField.equalsIgnoreCase("parentAjaxId"))
				continue;
			if(request.getParameter(curField) != null && (!request.getParameter(curField).trim().equals("")) )
			{
				paramNames.add(curField);
				paramValues.add(request.getParameter(curField));
			}
			
		}

		EForm curForm = new EForm(fid, demographic_no, providerNo);

		//add eform_link value from session attribute
		ArrayList<String> openerNames = curForm.getOpenerNames();
		ArrayList<String> openerValues = new ArrayList<String>();
		for (String name : openerNames) {
			String lnk = providerNo+"_"+demographic_no+"_"+fid+"_"+name;
			String val = (String)se.getAttribute(lnk);
			openerValues.add(val);
			if (val!=null) se.removeAttribute(lnk);
		}

		//----names parsed
		ActionMessages errors = curForm.setMeasurements(paramNames, paramValues);
		curForm.setFormSubject(subject);
		curForm.setValues(paramNames, paramValues);
		if (!openerNames.isEmpty()) curForm.setOpenerValues(openerNames, openerValues);
		if (eform_link!=null) curForm.setEformLink(eform_link);
		curForm.setImagePath();
		curForm.setAction();
		curForm.setNowDateTime();
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			request.setAttribute("curform", curForm);
			request.setAttribute("page_errors", "true");
			return mapping.getInputForward();
		}

		//Check if eform same as previous, if same -> not saved
		String prev_fdid = (String)se.getAttribute("eform_data_id");
		se.removeAttribute("eform_data_id");
		boolean sameform = false;
		if (StringUtils.filled(prev_fdid)) {
			EForm prevForm = new EForm(prev_fdid);
			if (prevForm!=null) {
				sameform = curForm.getFormHtml().equals(prevForm.getFormHtml());
			}			
		}
		if (!sameform) { //save eform data

			String fdid = eformDataManager.saveEformData( loggedInInfo, curForm ) + "";

			if( saveAsEdoc ) {
				eformDataManager.saveEformDataAsEDoc( loggedInInfo, fdid ); 
			}

			EFormUtil.addEFormValues(paramNames, paramValues, new Integer(fdid), new Integer(fid), new Integer(demographic_no)); //adds parsed values

			//post fdid to {eform_link} attribute
			if (eform_link!=null) {
				se.setAttribute(eform_link, fdid);
			}
			
			if (fax) {
				request.setAttribute("fdid", fdid);
				return(mapping.findForward("fax"));
			}
			
			else if (print) {
				request.setAttribute("fdid", fdid);
				return(mapping.findForward("print"));
			}

			else {
				//write template message to echart
				String program_no = new EctProgram(se).getProgram(providerNo);
				String path = request.getRequestURL().toString();
				String uri = request.getRequestURI();
				path = path.substring(0, path.indexOf(uri));
				path += request.getContextPath();
	
				EFormUtil.writeEformTemplate(LoggedInInfo.getLoggedInInfoFromSession(request),paramNames, paramValues, curForm, fdid, program_no, path);
			}
			
		}
		else {
			logger.debug("Warning! Form HTML exactly the same, new form data not saved.");
			if (fax) {
				request.setAttribute("fdid", prev_fdid);
				return(mapping.findForward("fax"));
			}
			
			else if (print) {
				request.setAttribute("fdid", prev_fdid);
				return(mapping.findForward("print"));
			}
			
			if( saveAsEdoc ) {
				eformDataManager.saveEformDataAsEDoc( loggedInInfo, prev_fdid ); 
			}
		}
		
		if (demographic_no != null) {
			IMatchManager matchManager = new MatchManager();
			DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
			Demographic client = demographicManager.getDemographic(loggedInInfo,demographic_no);
			try {
	            matchManager.<Demographic>processEvent(client, IMatchManager.Event.CLIENT_CREATED);
            } catch (MatchManagerException e) {
            	MiscUtils.getLogger().error("Error while processing MatchManager.processEvent(Client)",e);
            }
		}
		

		return(mapping.findForward("close"));
	}

	// MOVED TO MANAGER.
//	private EFormData toEFormData(EForm eForm) {
//		EFormData eFormData=new EFormData();
//		eFormData.setFormId(Integer.parseInt(eForm.getFid()));
//		eFormData.setFormName(eForm.getFormName());
//		eFormData.setSubject(eForm.getFormSubject());
//		eFormData.setDemographicId(Integer.parseInt(eForm.getDemographicNo()));
//		eFormData.setCurrent(true);
//		eFormData.setFormDate(new Date());
//		eFormData.setFormTime(eFormData.getFormDate());
//		eFormData.setProviderNo(eForm.getProviderNo());
//		eFormData.setFormData(eForm.getFormHtml());
//		eFormData.setShowLatestFormOnly(eForm.isShowLatestFormOnly());
//		eFormData.setPatientIndependent(eForm.isPatientIndependent());
//		eFormData.setRoleType(eForm.getRoleType());
//
//		return(eFormData);
//	}
}
