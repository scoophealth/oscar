/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.RedirectingActionForward;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.FunctionalCentreDischargeException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.model.IntakeNodeJavascript;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.DemographicExtra;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.Referral;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Demographic.PatientStatus;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.JointAdmission;
//import org.oscarehr.match.IMatchManager;
//import org.oscarehr.match.MatchManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;


public class GenericIntakeEditAction extends DispatchAction {

	private static Logger LOG = MiscUtils.getLogger();
	// Forwards
	private static final String EDIT = "edit";
	private static final String PRINT = "print";
	private static final String CLIENT_EDIT = "clientEdit";
	private static final String EFORM_ADD = "eformAdd";
	private static final String APPT = "appointment";
	protected static final String CLIENT_EDIT_ID = "id";
	
	private ClientImageDAO clientImageDAO = null;
	private SurveyManager surveyManager = (SurveyManager)SpringUtils.getBean("surveyManager2");
	private OcanStaffFormDao ocanStaffFormDao;
	
	protected static final String PROGRAM_ID = "programId";
	protected static final String TYPE = "type";
	protected static final String CLIENT = "client";
	protected static final String CLIENT_EXTRA = "clientExtra";
	protected static final String APPOINTMENT = "appointment";
	protected static final String DEMOGRAPHIC_NO = "demographic_no";
	protected static final String FORM_ID = "fid";
	protected static final String CLIENT_ID = "clientId";
	protected static final String INTAKE_ID = "intakeId";
	
	
	private GenericIntakeManager genericIntakeManager;
	protected ClientManager clientManager;
	protected ProgramManager programManager;
	protected AdmissionManager admissionManager;
	protected CaseManagementManager caseManagementManager;

	
	 public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
	        this.genericIntakeManager = genericIntakeManager;
	    }
	 
	    protected Integer getProgramId(HttpServletRequest request) {
			Integer programId = null;
			
			String programIdParam = request.getParameter(PROGRAM_ID);
			
			if (programIdParam != null) {
				try {
					programId = Integer.valueOf(programIdParam);
				} catch (NumberFormatException e) {
					LOG.error("Error", e);
				}
			}
			
			return programId;
		}
	public void setOscarSurveyManager(SurveyManager mgr) {
		this.surveyManager = mgr;
	}

	public void setClientImageDAO(ClientImageDAO clientImageDAO) {
		this.clientImageDAO = clientImageDAO;
	}
	
	public void setOcanStaffFormDao(OcanStaffFormDao ocanStaffFormDao) {
    	this.ocanStaffFormDao = ocanStaffFormDao;
    }

	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

		// [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
		List genders = GenericIntakeSearchAction.getGenders();
		formBean.setGenders(genders);
		// end of change

		String intakeType = request.getParameter(TYPE);
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		Intake intake = null;
		MiscUtils.getLogger().debug("INTAKE TYPE " + intakeType);
		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.createQuickIntake(providerNo);
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.createIndepthIntake(providerNo);
		}
		else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
		}

		MiscUtils.getLogger().debug("INTAKE IS NULL " + String.valueOf(intake == null));
		List<IntakeNodeJavascript> jsLocation = genericIntakeManager.getIntakeNodeJavascriptLocation(intake.getNode().getQuestionId());
		
		Integer defaultCommunityProgramId = null;
		
		if(org.oscarehr.common.IsPropertiesOn.propertiesOn("oscarClinic")) {
			defaultCommunityProgramId = getOscarClinicDefaultCommunityProgramId(oscar.OscarProperties.getInstance().getProperty("oscarClinicDefaultProgram"));
		}
						
		setBeanProperties(loggedInInfo,formBean, intake, getClientExtra(request), getClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
				.areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), defaultCommunityProgramId, null, null, loggedInInfo.getCurrentFacility().getId(), null,jsLocation, Agency.getLocalAgency().areCommunityProgramsVisible(intakeType), null);

		request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);

		request.getSession().setAttribute("intakeCurrentBedCommunityId", null);
		request.getSession().setAttribute("intakeCurrentCommunityId", null);
		
		//set up appointment's attributes here: request.getSession().getAttribute("appointment_date");

		ProgramUtils.addProgramRestrictions(request);

		ActionForward forward = mapping.findForward(EDIT);
		StringBuilder path = new StringBuilder(forward.getPath());		
		String fromAppt = request.getParameter("fromAppt");
		
		if( fromAppt!=null && "1".equals(fromAppt)) {
			
	      //  String originalPage2 = request.getParameter("originalPage");
	        String provider_no2 = request.getParameter("provider_no");
	        String bFirstDisp2 = request.getParameter("bFirstDisp");
	        String year2 = request.getParameter("year");
	        String month2 = request.getParameter("month");
	        String day2 = request.getParameter("day");
	        String start_time2 = request.getParameter("start_time");
	        String end_time2 = request.getParameter("end_time");
	        String duration2 = request.getParameter("duration");
	        String bufName = "";
	        String demographicNo = "";
	        String bufDoctorNo = providerNo;
	        String bufChart = "";
			String addAppt = "?user_id="+providerNo+"&provider_no="+provider_no2+"&bFirstDisp="+bFirstDisp2+"&appointment_date="+request.getParameter("appointment_date")+"&year="+year2+"&month="+month2+"&day="+day2+"&start_time="+start_time2+"&end_time="+end_time2+"&duration="+duration2+"&name="+URLEncoder.encode(bufName.toString())+"&chart_no="+URLEncoder.encode(bufChart.toString())+"&bFirstDisp=false&demographic_no="+demographicNo+"&messageID="+request.getParameter("messageId")+"&doctor_no="+bufDoctorNo.toString()+"&notes="+request.getParameter("notes")+"&reason="+request.getParameter("reason")+"&location="+request.getParameter("location")+"&resources="+request.getParameter("resources")+"&apptType="+request.getParameter("apptType")+"&style="+request.getParameter("style")+"&billing="+request.getParameter("billing")+"&status="+request.getParameter("status")+"&createdatetime="+request.getParameter("createdatetime")+"&creator="+request.getParameter("creator")+"&remarks="+request.getParameter("remarks");
			
			path.append(addAppt);
		}	
			
		return new ActionForward(path.toString());
		//return mapping.findForward(EDIT);
	}

	public ActionForward blank(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Integer facilityId = loggedInInfo.getCurrentFacility().getId();

		// [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
		List genders = GenericIntakeSearchAction.getGenders();
		formBean.setGenders(genders);
		// end of change

		String intakeType = request.getParameter(TYPE);

		Integer nodeId = null;
		try {
			String strNodeId = request.getParameter("nodeId");
			if(strNodeId != null)
				nodeId = Integer.valueOf(strNodeId);
		} catch(NumberFormatException e) {
			MiscUtils.getLogger().warn("error",e);
		}
		
		
		Intake intake = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			if (intake == null) {
				intake = genericIntakeManager.createQuickIntake(providerNo);
				//intake.setClientId(clientId);
				intake.setFacilityId(facilityId);
			}
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			if (intake == null) {
				intake = genericIntakeManager.createIndepthIntake(providerNo);
				//intake.setClientId(clientId);
				intake.setFacilityId(facilityId);
			}
		}
		else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			if (intake == null) {
				intake = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
				//intake.setClientId(clientId);
				intake.setFacilityId(facilityId);
			}
		}
		else {
			IntakeNode in = genericIntakeManager.getIntakeNode(nodeId);
			if(intake == null) {
				intake= genericIntakeManager.createIntake(in,providerNo);
				//intake.setClientId(clientId);
				intake.setFacilityId(facilityId);						
			}
		}
		
		List<IntakeNodeJavascript> jsLocation = genericIntakeManager.getIntakeNodeJavascriptLocation(intake.getNode().getQuestionId());

		Demographic client = new Demographic();
		DemographicExtra clientExtra = new DemographicExtra();
		

		setBeanProperties(loggedInInfo,formBean, intake, clientExtra, client, providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
				.areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), null,
				null,null, facilityId, nodeId, jsLocation, Agency.getLocalAgency().areCommunityProgramsVisible(intakeType), null);

		// UCF -- intake accessment : please don't remove the following lines
		List allForms = surveyManager.getAllFormsForCurrentProviderAndCurrentFacility(loggedInInfo);
		request.getSession().setAttribute("survey_list", allForms);

		String oldBedProgramId = null;
		request.getSession().setAttribute("intakeCurrentBedId", oldBedProgramId);
		request.getSession().setAttribute("intakeCurrentCommunityId", oldBedProgramId);

		request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
				

		ProgramUtils.addProgramRestrictions(request);

		return mapping.findForward(EDIT);

	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Integer facilityId = loggedInInfo.getCurrentFacility().getId();

		// [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
		List genders = GenericIntakeSearchAction.getGenders();
		formBean.setGenders(genders);
		// end of change

		String intakeType = request.getParameter(TYPE);
		Integer clientId = getClientIdAsInteger(request);

		Integer nodeId = null;
		try {
			String strNodeId = request.getParameter("nodeId");
			if(strNodeId != null)
				nodeId = Integer.valueOf(strNodeId);
		} catch(NumberFormatException e) {
			MiscUtils.getLogger().warn("error",e);
		}
		
		
		Intake intake = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.copyRegIntake(clientId, providerNo, facilityId);
			if (intake == null) {
				intake = genericIntakeManager.createQuickIntake(providerNo);
				intake.setClientId(clientId);
				intake.setFacilityId(facilityId);
			}
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.copyIndepthIntake(clientId, providerNo, facilityId);
			if (intake == null) {
				intake = genericIntakeManager.createIndepthIntake(providerNo);
				intake.setClientId(clientId);
				intake.setFacilityId(facilityId);
			}
		}
		else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.copyProgramIntake(clientId, getProgramId(request), providerNo, facilityId);
			if (intake == null) {
				intake = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
				intake.setClientId(clientId);
				intake.setFacilityId(facilityId);
			}
		}
		else {
			IntakeNode in = genericIntakeManager.getIntakeNode(nodeId);
			intake = genericIntakeManager.copyIntakeWithId(in, clientId, null, providerNo, facilityId);
			if(intake == null) {
				intake= genericIntakeManager.createIntake(in,providerNo);
				intake.setClientId(clientId);
				intake.setFacilityId(facilityId);						
			}
		}
		
		List<IntakeNodeJavascript> jsLocation = genericIntakeManager.getIntakeNodeJavascriptLocation(intake.getNode().getQuestionId());
		
		

		setBeanProperties(loggedInInfo, formBean, intake, getClientExtra(clientId), getClient(clientId), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
				.areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), getCurrentBedProgramId(clientId),
				getCurrentServiceProgramIds(loggedInInfo, clientId, providerNo, facilityId), getCurrentExternalProgramId(clientId), facilityId, nodeId, jsLocation, Agency.getLocalAgency().areCommunityProgramsVisible(intakeType), getCurrentCommunityProgramId(clientId));

		// UCF -- intake accessment : please don't remove the following lines
		List allForms = surveyManager.getAllFormsForCurrentProviderAndCurrentFacility(loggedInInfo);
		request.getSession().setAttribute("survey_list", allForms);

		String oldBedProgramId = String.valueOf(getCurrentBedProgramId(clientId));
		request.getSession().setAttribute("intakeCurrentBedId", oldBedProgramId);

		String oldCommunityProgramId = String.valueOf(getCurrentCommunityProgramId(clientId));
		request.getSession().setAttribute("intakeCurrentCommunityId", oldCommunityProgramId);

		if (clientManager.isClientFamilyHead(clientId)) {
			request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
		}
		else {
			if (clientManager.isClientDependentOfFamily(clientId)) request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, true);
			else request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
		}

		ProgramUtils.addProgramRestrictions(request);

		return mapping.findForward(EDIT);
	}

	public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Integer facilityId = loggedInInfo.getCurrentFacility().getId();

		String intakeType = request.getParameter(TYPE);
		Integer clientId = getClientIdAsInteger(request);
		Integer intakeId = getIntakeId(request);

		Intake intake = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			if (intakeId > -1) {
				intake = genericIntakeManager.getRegIntakeById(intakeId, facilityId);
			}
			else {
				intake = genericIntakeManager.getMostRecentQuickIntake(clientId, facilityId);
			}
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.getMostRecentIndepthIntake(clientId, facilityId);
		}
		else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			intake = genericIntakeManager.getMostRecentProgramIntake(clientId, getProgramId(request), facilityId);
		}

		List<IntakeNodeJavascript> jsLocation = genericIntakeManager.getIntakeNodeJavascriptLocation(intake.getNode().getQuestionId());
		
		
		
		setBeanProperties(loggedInInfo, formBean, intake, getClientExtra(clientId), getClient(clientId), providerNo, false, false, false, null, null, null, facilityId,null,jsLocation, false, null);

		return mapping.findForward(PRINT);
	}

	public ActionForward save_all(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String saveWhich) throws FunctionalCentreDischargeException {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Intake intake = formBean.getIntake();
		String intakeType = intake.getType();
		Demographic client = formBean.getClient();
		DemographicExtra clientExtra = formBean.getClientExtra();
		Integer nodeId = formBean.getNodeId();
		Integer oldId = null;
		LogAction.addLog((String)request.getSession().getAttribute("user"), LogConst.UPDATE, LogConst.CON_CAISI_INTAKE, String.valueOf(intake.getId()), request.getRemoteAddr(), String.valueOf(client.getDemographicNo()), "");
		String formattedAdmissionDate = request.getParameter("admissionDate");
		Date admissionDate;
		if(StringUtils.isBlank(formattedAdmissionDate)) {
			admissionDate = new Date();
		} else {      
			admissionDate = oscar.util.DateUtils.toDate(formattedAdmissionDate);
		}	

		/* for repeating elements */
		String[] repeatSizes= request.getParameterValues("repeat_size");
		if(repeatSizes != null) {
			for(String rs:repeatSizes) {
				String[] vals = rs.split("-");
				String rNodeId = vals[0];
				String rSize = vals[1];
				intake.cleanRepeatingAnswers(Integer.parseInt(rNodeId),Integer.parseInt(rSize));
			}
		}
		try {
			try {
				GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(request.getParameter("eff_year")), Integer.parseInt(request.getParameter("eff_month")), Integer.parseInt(request.getParameter("eff_day")));
				client.setEffDate(cal.getTime());
			} catch (Exception e) {
				LOG.debug("date parse exception on eff date", e);
				// that's fine ignore it, probably an invalid date or no date set.
			}

			String anonymous=StringUtils.trimToNull(request.getParameter("anonymous"));
			client.setAnonymous(anonymous);

			//check to see if HIN already exists in the system
			if(client.getDemographicNo() == null) {
				String hin = client.getHin();
				String hcType = client.getHcType();
				if(hin.length()>0 && clientManager.checkHealthCardExists(hin, hcType)) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hin.duplicate"));
					saveErrors(request, messages);
					return mapping.findForward(EDIT);
				}
			}			
			
			if(StringUtils.isBlank(client.getPatientStatus())) {
				client.setPatientStatus(PatientStatus.AC.name());
			}

			// save client information.
			saveClient(client, providerNo);
			saveClientExtra(clientExtra, client.getDemographicNo());		
			saveCbiForm(loggedInInfo, client, clientExtra);
			
					// for RFQ:
					if (OscarProperties.getInstance().isTorontoRFQ()) {
						Integer clientId = client.getDemographicNo();
						if (clientId != null && !"".equals(clientId)) {
							oldId = getCurrentBedCommunityProgramId(client.getDemographicNo());

							// Save 'external' program for RFQ.
							admitExternalProgram(loggedInInfo, client.getDemographicNo(), providerNo, formBean.getSelectedExternalProgramId());
						}
						// get and set intake location
						// client.setChildren(formBean.getProgramInDomainId());
						Integer intakeLocationId = 0;
						String intakeLocationStr = formBean.getProgramInDomainId();
						if (intakeLocationStr == null || "".equals(intakeLocationStr)) {
							Integer selectedBedProgramId = formBean.getSelectedBedProgramId();
							if ("RFQ_admit".equals(saveWhich)) {
								if (programManager.isBedProgram(selectedBedProgramId.toString())) {
									intakeLocationId = selectedBedProgramId;
								} 
								else {
									if (formBean.getProgramInDomainId() != null && formBean.getProgramInDomainId().trim().length() > 0)
										intakeLocationId = Integer.valueOf(formBean.getProgramInDomainId());
								}
							}
						}
						else {
							intakeLocationId = Integer.valueOf(intakeLocationStr);
						}

						intake.setIntakeLocation(intakeLocationId);
					}

					intake.setFacilityId(loggedInInfo.getCurrentFacility().getId());

					String admissionText = null;
					String remoteReferralId = StringUtils.trimToNull(request.getParameter("remoteReferralId"));
					if (remoteReferralId != null) {
						admissionText = getAdmissionText(loggedInInfo,admissionText, remoteReferralId);
					}

					admitBedCommunityProgram(loggedInInfo, client.getDemographicNo(), providerNo, formBean.getSelectedBedProgramId(), saveWhich, admissionText, admissionDate);
					admitBedCommunityProgram(loggedInInfo, client.getDemographicNo(), providerNo, formBean.getSelectedCommunityProgramId(), saveWhich, admissionText, admissionDate);

					if (remoteReferralId != null) {
						// doing this after the admit is about as transactional as this is going to get for now.
						ReferralWs referralWs;
						try {
							referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
							referralWs.removeReferral(Integer.parseInt(remoteReferralId));
						}
						catch (Exception e) {
							LOG.error("Unexpected error", e);
						}
					}

					// if (!formBean.getSelectedServiceProgramIds().isEmpty() && "RFQ_admit".endsWith(saveWhich)) {
					//if (!formBean.getSelectedServiceProgramIds().isEmpty()) { //should be able to discharge from all service programs.
						admitServicePrograms(loggedInInfo, client.getDemographicNo(), providerNo, formBean.getSelectedServiceProgramIds(), null, admissionDate, loggedInInfo.getCurrentFacility().getId());
					//}

					if ("normal".equals(saveWhich) || "appointment".equals(saveWhich)) {
						// normal intake saving . eg. seaton house
						intake.setIntakeStatus("Signed");
						intake.setId(null);
						saveIntake(intake, client.getDemographicNo());
					} else if("draft".equals(saveWhich)) {
						intake.setIntakeStatus("Draft");
						intake.setId(null);
						saveIntake(intake, client.getDemographicNo());
					}
					else {
						// RFQ intake saving...
						if ("RFQ_temp".equals(saveWhich)) {
							intake.setIntakeStatus("Unsigned");
							saveUpdateIntake(intake, client.getDemographicNo());
						}
						else if ("RFQ_admit".equals(saveWhich)) {
							intake.setIntakeStatus("Signed");
							intake.setId(null);
							saveIntake(intake, client.getDemographicNo());
						}
						else if ("RFQ_notAdmit".equals(saveWhich)) {
							intake.setIntakeStatus("Signed");
							intake.setId(null);
							saveIntake(intake, client.getDemographicNo());
						}
					}
				}
				catch (ProgramFullException e) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
					saveErrors(request, messages);
				}
				catch (AdmissionException e) {
					MiscUtils.getLogger().error("Error", e);
					LOG.error("Error", e);

					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message", e.getMessage()));
					saveErrors(request, messages);
				}
				catch (ServiceRestrictionException e) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.service_restricted", e.getRestriction().getComments(), e.getRestriction().getProvider()
							.getFormattedName()));
					saveErrors(request, messages);
				}

				List<IntakeNodeJavascript> jsLocation = genericIntakeManager.getIntakeNodeJavascriptLocation(intake.getNode().getQuestionId());
				
				
				setBeanProperties(loggedInInfo,formBean, intake, clientExtra, client, providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(
						intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), getCurrentBedProgramId(client.getDemographicNo()),
						getCurrentServiceProgramIds(loggedInInfo, client.getDemographicNo(), providerNo, loggedInInfo.getCurrentFacility().getId()), getCurrentExternalProgramId(client.getDemographicNo()), loggedInInfo.getCurrentFacility().getId(), nodeId, jsLocation, Agency.getLocalAgency().areCommunityProgramsVisible(intakeType), getCurrentCommunityProgramId(client.getDemographicNo()));

				String oldBedProgramId = String.valueOf(getCurrentBedProgramId(client.getDemographicNo()));
				request.getSession().setAttribute("intakeCurrentBedId", oldBedProgramId);

				String oldCommunityProgramId = String.valueOf(getCurrentCommunityProgramId(client.getDemographicNo()));
				request.getSession().setAttribute("intakeCurrentCommunityId", oldCommunityProgramId);

				String remoteFacilityIdStr = StringUtils.trimToNull(request.getParameter("remoteFacilityId"));
				String remoteDemographicIdStr = StringUtils.trimToNull(request.getParameter("remoteDemographicId"));
				if (remoteFacilityIdStr != null && remoteDemographicIdStr != null) {
					try {
						int remoteFacilityId = Integer.parseInt(remoteFacilityIdStr);
						int remoteDemographicId = Integer.parseInt(remoteDemographicIdStr);
						DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());

						// link the clients
						demographicWs.linkDemographics(providerNo, client.getDemographicNo(), remoteFacilityId, remoteDemographicId);

						// copy image if exists
						{
							DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteFacilityId, remoteDemographicId);

							if (demographicTransfer.getPhoto() != null) {
								ClientImage clientImage = new ClientImage();
								clientImage.setDemographic_no(client.getDemographicNo());
								clientImage.setImage_data(demographicTransfer.getPhoto());
								clientImage.setImage_type("jpg");
								clientImageDAO.saveClientImage(clientImage);
							}
						}
					}
					catch (MalformedURLException e) {
						LOG.error("Error", e);
					}
					catch (WebServiceException e) {
						LOG.error("Error", e);
					}
				}
		/*
				GenericIntakeEditFormBean formBean2 = new GenericIntakeEditFormBean();
				request.getSession().setAttribute("genericIntakeEditForm", formBean2);
		*/		
				if (("RFQ_admit".equals(saveWhich) || "RFQ_notAdmit".equals(saveWhich)) && oldId != null) {
					return clientEdit(mapping, form, request, response);
				}
				else if (request.getAttribute(Globals.ERROR_KEY) != null) {
					return mapping.findForward(EDIT);
				}
				else if( "appointment".equals(saveWhich)) {
					ActionForward forward = mapping.findForward(APPT);
					StringBuilder path = new StringBuilder(forward.getPath());
					//String fromAppt = request.getParameter("fromAppt");
			        //String originalPage2 = request.getParameter("originalPage");
			        String provider_no2 = request.getParameter("provider_no");
			        String bFirstDisp2 = request.getParameter("bFirstDisp");
			        String year2 = request.getParameter("year");
			        String month2 = request.getParameter("month");
			        String day2 = request.getParameter("day");
			        String start_time2 = request.getParameter("start_time");
			        String end_time2 = request.getParameter("end_time");
			        String duration2 = request.getParameter("duration");
			        String bufName = client.getDisplayName();
			        Integer dem = client.getDemographicNo();
			        String bufDoctorNo = client.getProviderNo();
			        String bufChart = client.getChartNo();
					String addAppt = "?user_id="+request.getParameter("creator")+"&provider_no="+provider_no2+"&bFirstDisp="+bFirstDisp2+"&appointment_date="+request.getParameter("appointment_date")+"&year="+year2+"&month="+month2+"&day="+day2+"&start_time="+start_time2+"&end_time="+end_time2+"&duration="+duration2+"&name="+URLEncoder.encode(bufName.toString())+"&chart_no="+URLEncoder.encode(bufChart.toString())+"&bFirstDisp=false&demographic_no="+dem.toString()+"&messageID="+request.getParameter("messageId")+"&doctor_no="+bufDoctorNo.toString()+"&notes="+request.getParameter("notes")+"&reason="+request.getParameter("reason")+"&location="+request.getParameter("location")+"&resources="+request.getParameter("resources")+"&type="+request.getParameter("apptType")+"&style="+request.getParameter("style")+"&billing="+request.getParameter("billing")+"&status="+request.getParameter("status")+"&createdatetime="+request.getParameter("createdatetime")+"&creator="+request.getParameter("creator")+"&remarks="+request.getParameter("remarks");
					
					path.append(addAppt);					
					return new RedirectingActionForward(path.toString());

				}
				else {
					return clientEdit(mapping, form, request, response);
				}			
	}

	private String getAdmissionText(LoggedInInfo loggedInInfo, String admissionText, String remoteReferralId) {
		try {
			ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			Referral remoteReferral = referralWs.getReferral(Integer.parseInt(remoteReferralId));
			StringBuilder sb = new StringBuilder();

			CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),remoteReferral.getSourceIntegratorFacilityId());
			sb.append("Referred from : ");
			sb.append(cachedFacility.getName());

			FacilityIdStringCompositePk facilityProviderPrimaryKey = new FacilityIdStringCompositePk();
			facilityProviderPrimaryKey.setIntegratorFacilityId(remoteReferral.getSourceIntegratorFacilityId());
			facilityProviderPrimaryKey.setCaisiItemId(remoteReferral.getSourceCaisiProviderId());
			CachedProvider cachedProvider = CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(),facilityProviderPrimaryKey);
			sb.append(" by ");
			sb.append(cachedProvider.getLastName());
			sb.append(", ");
			sb.append(cachedProvider.getFirstName());
			if (cachedProvider.getWorkPhone() != null) {
				sb.append("(");
				sb.append(cachedProvider.getWorkPhone());
				sb.append(")");
			}

			sb.append(". ");
			sb.append("Reason for Referral : ");
			sb.append(remoteReferral.getReasonForReferral());

			sb.append(". ");
			sb.append("Presenting Problem : ");
			sb.append(remoteReferral.getPresentingProblem());

			admissionText = sb.toString();
		}
		catch (Exception e) {
			LOG.error("Unexpected error.", e);
		}
		return admissionText;
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FunctionalCentreDischargeException {
		
		String fromAppt = request.getParameter("fromAppt");
		/*
        String originalPage2 = request.getParameter("originalPage");
        String provider_no2 = request.getParameter("provider_no");
        String bFirstDisp2 = request.getParameter("bFirstDisp");
        String year2 = request.getParameter("year");
        String month2 = request.getParameter("month");
        String day2 = request.getParameter("day");
        String start_time2 = request.getParameter("start_time");
        String end_time2 = request.getParameter("end_time");
        String duration2 = request.getParameter("duration");
        */
		if(fromAppt!=null && fromAppt.equals("1")) {
			return save_all(mapping, form, request, response, "appointment");
		} else {
			return save_all(mapping, form, request, response, "normal");
		}
	}
	
	public ActionForward save_draft(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FunctionalCentreDischargeException {
		return save_all(mapping, form, request, response, "draft");
	}

	public ActionForward save_temp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FunctionalCentreDischargeException {

		return save_all(mapping, form, request, response, "RFQ_temp");
	}

	public ActionForward save_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FunctionalCentreDischargeException {
		return save_all(mapping, form, request, response, "RFQ_admit");
	}

	public ActionForward save_notAdmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FunctionalCentreDischargeException {
		return save_all(mapping, form, request, response, "RFQ_notAdmit");
	}

	public ActionForward clientEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;
		String forward = null;
		Integer clientEditId = formBean.getClient().getDemographicNo();

		StringBuilder parameters = new StringBuilder("?");

		Set<Integer> serviceProgramIds = formBean.getSelectedServiceProgramIds();
		if (!serviceProgramIds.isEmpty()) {
			Integer serviceProgramId = serviceProgramIds.iterator().next(); //assumption: there is only one item in this set at a time. Take the 1st one.
			if (serviceProgramId == Integer.parseInt(OscarProperties.getIntakeProgramAccessServiceId())) {
				parameters.append(FORM_ID).append("=").append(OscarProperties.getIntakeProgramAccessFId());
				forward = EFORM_ADD;
			} else if (serviceProgramId == Integer.parseInt(OscarProperties.getIntakeProgramCashServiceId())) {
				parameters.append(FORM_ID).append("=").append(OscarProperties.getIntakeProgramCashFId());
				forward = EFORM_ADD;
			}
		}
		if (EFORM_ADD.equals(forward)) {
			parameters.append("&");
			parameters.append(DEMOGRAPHIC_NO).append("=").append(clientEditId);
			parameters.append("&");
			parameters.append(APPOINTMENT).append("=").append(0); // appointment code is always 0
		} else {
			parameters.append(CLIENT_EDIT_ID).append("=").append(clientEditId);
			forward = CLIENT_EDIT;
		}

		return createRedirectForward(mapping, forward, parameters);
	}

	
	public ActionForward save_proxy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;
		try {
			save(mapping, formBean, request, response);
			int clientEditId = formBean.getClient().getDemographicNo();
			OutputStream os = response.getOutputStream();
			os.write((""+clientEditId).getBytes());
        } catch (IOException e) {
	        //ignore
	        LOG.error(e.getMessage(), e);
        } catch (FunctionalCentreDischargeException e2) {
        	LOG.error(e2.getMessage(), e2);
        }

		return null;
	}

	// Adapt

	private Demographic getClient(Integer clientId) {
		return clientManager.getClientByDemographicNo(clientId.toString());
	}
	
	private DemographicExtra getClientExtra(Integer clientId) {
		return clientManager.getClientExtraByDemographicNo(clientId.toString());
	}
	
	private Set<Program> getActiveProviderPrograms(String providerNo) {
		Set<Program> activeProviderPrograms = new HashSet<Program>();

		for (Program providerProgram : programManager.getProgramDomain(providerNo)) {
			if (providerProgram != null && providerProgram.isActive()) {
				activeProviderPrograms.add(providerProgram);
			}
		}

		return activeProviderPrograms;
	}

	public List<Program> getBedPrograms(Set<Program> providerPrograms, String providerNo) {
		List<Program> bedPrograms = new ArrayList<Program>();

		for (Program program : programManager.getBedPrograms()) {
			if (providerPrograms.contains(program)) {
				if (OscarProperties.getInstance().isTorontoRFQ()) {
					if (caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, "", String.valueOf(program.getId()))) {
						bedPrograms.add(program);
					}
				}
				else {
					bedPrograms.add(program);
				}
			}
		}

		return bedPrograms;
	}

	public List<Program> getCommunityPrograms() {
		List<Program> communityPrograms = new ArrayList<Program>();

		for (Program program : programManager.getCommunityPrograms()) {
			communityPrograms.add(program);
		}

		return communityPrograms;
	}

	public List<Program> getServicePrograms(Set<Program> providerPrograms, String providerNo) {
		List<Program> servicePrograms = new ArrayList<Program>();

		for (Object o : programManager.getServicePrograms()) {
			Program program = (Program) o;

			if (providerPrograms.contains(program)) {
				servicePrograms.add(program);
			}
		}

		return servicePrograms;
	}


	
	private List<Program> getExternalPrograms(Set<Program> providerPrograms) {
		List<Program> externalPrograms = new ArrayList<Program>();

		for (Program program : programManager.getExternalPrograms()) {
			externalPrograms.add(program);
		}
		return externalPrograms;
	}

	private List<Program> getProgramsInDomain(Set<Program> providerPrograms) {
		List<Program> programsInDomain = new ArrayList<Program>();

		for (Program program : providerPrograms) {
			programsInDomain.add(program);
		}
		return programsInDomain;
	}
	
	private Integer getOscarClinicDefaultCommunityProgramId (String communityProgram){
		Integer communityProgramId = null;
		communityProgramId=programManager.getProgramIdByProgramName(communityProgram);
		return communityProgramId;
	}
	
	private Integer getCurrentBedCommunityProgramId(Integer clientId) {
		Integer currentProgramId = null;

		Admission bedProgramAdmission = admissionManager.getCurrentBedProgramAdmission(clientId);
		Admission communityProgramAdmission = admissionManager.getCurrentCommunityProgramAdmission(clientId);

		if (bedProgramAdmission != null) {
			currentProgramId = bedProgramAdmission.getProgramId();
		}
		else if (communityProgramAdmission != null) {
			currentProgramId = communityProgramAdmission.getProgramId();
		}

		return currentProgramId;
	}

	private Integer getCurrentBedProgramId(Integer clientId) {
		Integer currentProgramId = null;

		Admission bedProgramAdmission = admissionManager.getCurrentBedProgramAdmission(clientId);
		
		if (bedProgramAdmission != null) {
			currentProgramId = bedProgramAdmission.getProgramId();
		}
		
		return currentProgramId;
	}

	private Integer getCurrentCommunityProgramId(Integer clientId) {
		Integer currentProgramId = null;

		Admission communityProgramAdmission = admissionManager.getCurrentCommunityProgramAdmission(clientId);

		if (communityProgramAdmission != null) {
			currentProgramId = communityProgramAdmission.getProgramId();
		}

		return currentProgramId;
	}

	private Integer getCurrentExternalProgramId(Integer clientId) {
		Integer currentProgramId = null;

		Admission externalProgramAdmission = admissionManager.getCurrentExternalProgramAdmission(clientId);

		if (externalProgramAdmission != null) {
			currentProgramId = externalProgramAdmission.getProgramId();
		}

		return currentProgramId;
	}

	private SortedSet<Integer> getCurrentServiceProgramIds(LoggedInInfo loggedInInfo, Integer clientId, String providerNo, Integer facilityId) {
		SortedSet<Integer> currentProgramIds = new TreeSet<Integer>();
		Set<Program> providerPrograms = getActiveProviderProgramsInFacility(loggedInInfo, providerNo, facilityId);
		List<Program> programsInDomain = getServicePrograms(providerPrograms, providerNo);
		List<?> admissions = admissionManager.getCurrentServiceProgramAdmission(clientId);
		if (admissions != null) {
			for (Object o : admissions) {
				Admission serviceProgramAdmission = (Admission) o;
				for(Program p : programsInDomain) {
					if(p.getId().intValue() == serviceProgramAdmission.getProgramId().intValue())				
						currentProgramIds.add(serviceProgramAdmission.getProgramId());
				}
			}
		}

		return currentProgramIds;
	}

	private void saveClient(Demographic client, String providerNo) {
		
		String strSaveMrp = OscarProperties.getInstance().getProperty("caisi.registration_intake.updateMRPOnSave", "true");
		if("true".equals(strSaveMrp)) {
			client.setProviderNo(providerNo);
		}

		clientManager.saveClient(client);
		//this is slowing things down, and AFAIK waitlist isn't being used anywhere
		/*
		try {
			log.info("Processing client creation event with MatchManager..." + 
					matchManager.<Demographic>processEvent(client, IMatchManager.Event.CLIENT_CREATED));
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error while processing MatchManager.processEvent(Client)",e);
		}
		*/
	}

	private void saveClientExtra(DemographicExtra clientExtra, Integer demographicNo) {
		
		clientManager.saveDemographicExt(demographicNo.intValue(), "middleName", clientExtra.getMiddleName()==null?"":clientExtra.getMiddleName());
		clientManager.saveDemographicExt(demographicNo.intValue(), "preferredName",clientExtra.getPreferredName()==null?"":clientExtra.getPreferredName());
		clientManager.saveDemographicExt(demographicNo.intValue(), "lastNameAtBirth",clientExtra.getLastNameAtBirth()==null?"":clientExtra.getLastNameAtBirth());
		clientManager.saveDemographicExt(demographicNo.intValue(), "maritalStatus",clientExtra.getMaritalStatus()==null?"":clientExtra.getMaritalStatus());
		clientManager.saveDemographicExt(demographicNo.intValue(), "recipientLocation",clientExtra.getRecipientLocation()==null?"":clientExtra.getRecipientLocation());
		clientManager.saveDemographicExt(demographicNo.intValue(), "lhinConsumerResides",clientExtra.getLhinConsumerResides()==null?"":clientExtra.getLhinConsumerResides());
		clientManager.saveDemographicExt(demographicNo.intValue(), "address2",clientExtra.getAddress2()==null?"":clientExtra.getAddress2());
				
	}
	
	private void saveCbiForm(LoggedInInfo loggedInInfo, Demographic client, DemographicExtra clientExtra) {
		
		//LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		Integer facilityId = loggedInInfo.getCurrentFacility().getId();
		  
		//When update registration intake, update demographic info in existing cbi form.
		OcanStaffForm cbiForm = ocanStaffFormDao.findLatestByFacilityClient(facilityId, client.getDemographicNo(), "CBI");
		if(cbiForm == null) {
			return;
		}
		cbiForm.setAddressLine1(client.getAddress()==null?"":client.getAddress());
		cbiForm.setAddressLine2(clientExtra.getAddress2()==null?"":clientExtra.getAddress2());
		cbiForm.setCity(client.getCity()==null?"":client.getCity());
		cbiForm.setClientDateOfBirth(client.getDateOfBirth());
		cbiForm.setEmail(client.getEmail()==null?"":client.getEmail());
		cbiForm.setFirstName(client.getFirstName());
		cbiForm.setGender(client.getSex());
		cbiForm.setHcNumber(client.getHin()==null?"":client.getHin());
		cbiForm.setHcVersion(client.getVer()==null?"":client.getVer());
		cbiForm.setLastName(client.getLastName());		
		cbiForm.setLastNameAtBirth(clientExtra.getLastNameAtBirth()==null?"":clientExtra.getLastNameAtBirth());
		cbiForm.setPhoneNumber(client.getPhone()==null?"":client.getPhone());
		cbiForm.setPostalCode(client.getPostal()==null?"":client.getPostal());
		cbiForm.setProvince(client.getProvince()==null?"":client.getProvince());
		cbiForm.setEstimatedAge(client.getAge()==null?"":client.getAge());
		ocanStaffFormDao.merge(cbiForm);
		
		OcanFormAction.addOcanStaffFormData(cbiForm.getId(), "middle" , clientExtra.getMiddleName()==null?"":clientExtra.getMiddleName());
		OcanFormAction.addOcanStaffFormData(cbiForm.getId(), "preferred" , clientExtra.getPreferredName()==null?"":clientExtra.getPreferredName());
		OcanFormAction.addOcanStaffFormData(cbiForm.getId(), "lastNameAtBirth" , clientExtra.getLastNameAtBirth()==null?"":clientExtra.getLastNameAtBirth());
		OcanFormAction.addOcanStaffFormData(cbiForm.getId(), "marital_status" , clientExtra.getMaritalStatus()==null?"":clientExtra.getMaritalStatus());
		OcanFormAction.addOcanStaffFormData(cbiForm.getId(), "service_recipient_location" , clientExtra.getRecipientLocation()==null?"":clientExtra.getRecipientLocation());
		OcanFormAction.addOcanStaffFormData(cbiForm.getId(), "service_recipient_lhin" , clientExtra.getLhinConsumerResides()==null?"":clientExtra.getLhinConsumerResides());
		
	}
	
	

	private void admitExternalProgram(LoggedInInfo loggedInInfo, Integer clientId, String providerNo, Integer externalProgramId) throws ProgramFullException, AdmissionException, ServiceRestrictionException, FunctionalCentreDischargeException {
		Program externalProgram = null;
		Integer currentExternalProgramId = getCurrentExternalProgramId(clientId);

		if (externalProgramId != null) {
			externalProgram = programManager.getProgram(externalProgramId);
		}

		if (externalProgram != null) {
			if (currentExternalProgramId == null) {
				admissionManager.processAdmission(loggedInInfo, clientId, providerNo, externalProgram, "intake external discharge", "intake external admit");
			}
			else if (!currentExternalProgramId.equals(externalProgramId)) {
				/*
				 * if (programManager.getProgram(externalProgramId).isExternal()) { if (externalProgram.isExternal()) { admissionManager.processAdmission(clientId, providerNo,
				 * externalProgram, "intake external discharge", "intake external admit"); } }
				 */
				admissionManager.processDischarge(loggedInInfo, currentExternalProgramId, clientId, "intake external discharge", "0");
				admissionManager.processAdmission(loggedInInfo, clientId, providerNo, externalProgram, "intake external discharge", "intake external admit");
			}
		}
	}

	public void admitBedCommunityProgram(LoggedInInfo loggedInInfo, Integer clientId, String providerNo, Integer bedCommunityProgramId, String saveWhich, String admissionText, Date admissionDate) throws ProgramFullException,
			AdmissionException, ServiceRestrictionException, FunctionalCentreDischargeException {
		Program bedCommunityProgram = null;
		Integer currentBedCommunityProgramId = getCurrentBedCommunityProgramId(clientId);

		if (admissionText == null) admissionText = "intake admit";

		if ("RFQ_notAdmit".equals(saveWhich) && bedCommunityProgramId == null && currentBedCommunityProgramId == null) {
			return;
		}
		if (bedCommunityProgramId == null && currentBedCommunityProgramId == null) {
			bedCommunityProgram = programManager.getHoldingTankProgram();
		}
		else if (bedCommunityProgramId != null) {
			bedCommunityProgram = programManager.getProgram(bedCommunityProgramId);
		}

		boolean isFamilyHead = false;
		boolean isFamilyDependent = false;
		JointAdmission clientsJadm = null;
		List<JointAdmission> dependentList = null;
		Integer[] dependentIds = null;

		if (clientManager != null && clientId != null) {
			dependentList = clientManager.getDependents(Integer.valueOf(clientId));
			clientsJadm = clientManager.getJointAdmission(Integer.valueOf(clientId));
		}
		if (clientsJadm != null && clientsJadm.getHeadClientId() != null) {
			isFamilyDependent = true;
		}
		if (dependentList != null && dependentList.size() > 0) {
			isFamilyHead = true;
		}
		if (dependentList != null) {
			dependentIds = new Integer[dependentList.size()];
			for (int i = 0; i < dependentList.size(); i++) {
				dependentIds[i] = new Integer(dependentList.get(i).getClientId().intValue());
			}
		}

		if (isFamilyDependent) {
			throw new AdmissionException("you cannot admit a dependent family/group member, you must remove the dependent status or admit the family head");

		}
		else if (isFamilyHead && dependentIds != null && dependentIds.length >= 1) {
			Integer[] familyIds = new Integer[dependentIds.length + 1];
			familyIds[0] = clientId;
			for (int i = 0; i < dependentIds.length; i++) {
				familyIds[i + 1] = dependentIds[i];
			}
			for (int i = 0; i < familyIds.length; i++) {
				Integer familyId = familyIds[i];
				if (bedCommunityProgram != null) {
					if (currentBedCommunityProgramId == null) {
						admissionManager.processAdmission(loggedInInfo, familyId, providerNo, bedCommunityProgram, "intake discharge", admissionText, admissionDate);
					}
					else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
						if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
							if (bedCommunityProgram.isBed()) {
								admissionManager.processAdmission(loggedInInfo, familyId, providerNo, bedCommunityProgram, "intake discharge", admissionText, admissionDate);
							}
							else {
								admissionManager.processDischargeToCommunity(loggedInInfo,bedCommunityProgramId, familyId, providerNo, "intake discharge", "0", admissionDate, false);
							}
						}
						else {
							if (bedCommunityProgram.isCommunity()) {
								admissionManager.processDischargeToCommunity(loggedInInfo,bedCommunityProgramId, familyId, providerNo, "intake discharge", "0", admissionDate, false);
							}
							else {
								admissionManager.processDischarge(loggedInInfo, currentBedCommunityProgramId, familyId, "intake discharge", "0", admissionDate);
								admissionManager.processAdmission(loggedInInfo, familyId, providerNo, bedCommunityProgram, "intake discharge", admissionText, admissionDate);
							}
						}
					}
				}
			}

			// throw new AdmissionException(
			// "If you admit the family head, all dependents will also be admitted to this program and discharged from their current programs. Are you sure you wish to proceed?");

		}
		else {

			if (bedCommunityProgram != null) {
				if (currentBedCommunityProgramId == null) {
					admissionManager.processAdmission(loggedInInfo, clientId, providerNo, bedCommunityProgram, "intake discharge", admissionText, admissionDate);
				}
				else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
					if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
						if (bedCommunityProgram.isBed()) {
							// automatic discharge from one bed program to another bed program.
							admissionManager.processAdmission(loggedInInfo, clientId, providerNo, bedCommunityProgram, "intake discharge", admissionText, admissionDate);
						}
						else {
							admissionManager.processDischargeToCommunity(loggedInInfo,bedCommunityProgramId, clientId, providerNo, "intake discharge", "0", admissionDate, false);
						}
					}
					else {
						if (bedCommunityProgram.isCommunity()) {
							admissionManager.processDischargeToCommunity(loggedInInfo,bedCommunityProgramId, clientId, providerNo, "intake discharge", "0", admissionDate, false);
						}
						else {
							admissionManager.processDischarge(loggedInInfo, currentBedCommunityProgramId, clientId, "intake discharge", "0", admissionDate);
							admissionManager.processAdmission(loggedInInfo, clientId, providerNo, bedCommunityProgram, "intake discharge", admissionText, admissionDate);
						}
					}
				}
			}
		}
	}

	public void admitServicePrograms(LoggedInInfo loggedInInfo, Integer clientId, String providerNo, Set<Integer> serviceProgramIds, String admissionText, Date admissionDate, Integer facilityId) throws ProgramFullException, AdmissionException,
			ServiceRestrictionException, FunctionalCentreDischargeException {
		SortedSet<Integer> currentServicePrograms = getCurrentServiceProgramIds(loggedInInfo, clientId, providerNo, facilityId);

		if (admissionText == null) admissionText = "intake admit";
		
		//only allow to discharge the programs for which you are staff of.
		Set<Program> programsInDomain = getActiveProviderPrograms(providerNo);
		List<Integer> programDomainIds = new ArrayList<Integer>();
		for(Program p:programsInDomain) {
			programDomainIds.add(p.getId());
		}
		
		//discharge from all
		if( serviceProgramIds.isEmpty()) {
			for(Object programId : currentServicePrograms) {
				if(programDomainIds.contains(programId)) {
					admissionManager.processDischarge(loggedInInfo, (Integer) programId, clientId, "intake discharge", "0", admissionDate);
				} 
			}
			return;
		}
		
		//remove the ones selected, and discharge the ones not selected
		Collection<?> discharge = CollectionUtils.subtract(currentServicePrograms, serviceProgramIds);

		for (Object programId : discharge) {
			if(programDomainIds.contains(programId)) {
				admissionManager.processDischarge(loggedInInfo, (Integer) programId, clientId, "intake discharge", "0", admissionDate);
			}
		}
		

		Collection<?> admit = CollectionUtils.subtract(serviceProgramIds, currentServicePrograms);

		for (Object programId : admit) {
			Program program = programManager.getProgram((Integer) programId);
			admissionManager.processAdmission(loggedInInfo, clientId, providerNo, program, "intake discharge", admissionText, admissionDate);
		}
	}

	private void saveIntake(Intake intake, Integer clientId) {
		intake.setClientId(clientId);
		genericIntakeManager.saveIntake(intake);
	}

	private void saveUpdateIntake(Intake intake, Integer clientId) {
		intake.setClientId(clientId);

		genericIntakeManager.saveUpdateIntake(intake);
	}

	public Set<Program> getActiveProviderProgramsInFacility(LoggedInInfo loggedInInfo, String providerNo, Integer facilityId) {
		Set<Program> programs = new HashSet<Program>();
		Set<Program> programsInDomain = getActiveProviderPrograms(providerNo);
		if (facilityId == null) return programs;

		for (Program p : programManager.getProgramDomainInCurrentFacilityForCurrentProvider(loggedInInfo, false)) {
			if (programsInDomain.contains(p)) {
				programs.add(p);
			}
		}

		return programs;
	}

	// Bean

	private void setBeanProperties(LoggedInInfo loggedInInfo, GenericIntakeEditFormBean formBean, Intake intake, DemographicExtra clientExtra, Demographic client, String providerNo, boolean bedProgramsVisible,
			boolean serviceProgramsVisible, boolean externalProgramsVisible, Integer currentBedProgramId, SortedSet<Integer> currentServiceProgramIds,
			Integer currentExternalProgramId, Integer facilityId, Integer nodeId, List<IntakeNodeJavascript> javascriptLocation, boolean communityProgramsVisible, Integer currentCommunityProgramId) {
		formBean.setIntake(intake);
		formBean.setClient(client);
		formBean.setClientExtra(clientExtra);
		formBean.setNodeId(nodeId);
		formBean.setJsLocation(javascriptLocation);
		
		if (bedProgramsVisible || communityProgramsVisible || serviceProgramsVisible || externalProgramsVisible) {
			Set<Program> providerPrograms = getActiveProviderProgramsInFacility(loggedInInfo, providerNo, facilityId);

			if (bedProgramsVisible) {
				formBean.setBedPrograms(getBedPrograms(providerPrograms, providerNo));
				formBean.setSelectedBedProgramId(currentBedProgramId);
			}

			if (communityProgramsVisible) {
				formBean.setCommunityPrograms(getCommunityPrograms());
				formBean.setSelectedCommunityProgramId(currentCommunityProgramId);
			}
			
			if (serviceProgramsVisible) {
				formBean.setServicePrograms(getServicePrograms(providerPrograms, providerNo));
				formBean.setSelectedServiceProgramIds(currentServiceProgramIds);
			}

			if (externalProgramsVisible) {
				formBean.setExternalPrograms(getExternalPrograms(providerPrograms));
				formBean.setSelectedExternalProgramId(currentExternalProgramId);
			}

			formBean.setProgramsInDomain(getProgramsInDomain(providerPrograms));

			String intakeLocation = "";
			if (intake != null) {
				intakeLocation = String.valueOf(intake.getIntakeLocation());
			}
			if (intakeLocation == null || "".equals(intakeLocation) || "null".equals(intakeLocation)) {
				formBean.setSelectedProgramInDomainId(0);
			}
			else {
				formBean.setSelectedProgramInDomainId(Integer.valueOf(intakeLocation));
			}
		}

	}
	
    protected ActionForward createRedirectForward(ActionMapping mapping, String forwardName, StringBuffer parameters) {
        ActionForward forward = mapping.findForward(forwardName);
        StringBuilder path = new StringBuilder(forward.getPath());
        path.append(parameters);
        
        return new RedirectingActionForward(path.toString());
    }

    protected ActionForward createRedirectForward(ActionMapping mapping, 
        String forwardName, StringBuilder parameters) {
	    ActionForward forward = mapping.findForward(forwardName);
	    StringBuilder path = new StringBuilder(forward.getPath());
	    path.append(parameters);
	
	    return new RedirectingActionForward(path.toString());
    }

    protected Demographic getClient(HttpServletRequest request) {
		Demographic client = (Demographic) getSessionAttribute(request,CLIENT);
		return (client != null) ? client : new Demographic();
	}
    
    protected DemographicExtra getClientExtra(HttpServletRequest request) {
		DemographicExtra client = (DemographicExtra) getSessionAttribute(request,CLIENT_EXTRA);
		return (client != null) ? client : new DemographicExtra();
	}
    
    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }
    
    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
    	this.admissionManager = mgr;
    }
    
    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
    	this.caseManagementManager = caseManagementManager;
    }
    
	protected Integer getClientIdAsInteger(HttpServletRequest request) {
		Integer clientId = null;
		String clientId_str = request.getParameter(CLIENT_ID);
		if(clientId_str!=null) {
			try {
				clientId = Integer.valueOf(clientId_str);
			} catch (NumberFormatException e) {
				LOG.error("Error", e);
			}
		}
		return clientId;
		
	}

	protected Integer getIntakeId(HttpServletRequest request) {
		return Integer.valueOf(request.getParameter(INTAKE_ID));
	}
	
	protected Object getSessionAttribute(HttpServletRequest request, String attributeName) {
		Object attribute = request.getSession().getAttribute(attributeName);
		
		if (attribute != null) {
			request.getSession().removeAttribute(attributeName);
		}
		
		return attribute;
	}
}
