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
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.model.IntakeNodeJavascript;
import org.oscarehr.common.model.JointAdmission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.service.impl.SurveyManagerImpl;
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
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.match.IMatchManager;
import org.oscarehr.match.MatchManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;

import oscar.OscarProperties;

public class GenericIntakeEditAction extends BaseGenericIntakeAction {

	private static Logger LOG = MiscUtils.getLogger();
	// Forwards
	private static final String EDIT = "edit";
	private static final String PRINT = "print";
	private static final String CLIENT_EDIT = "clientEdit";
	private static final String EFORM_ADD = "eformAdd";
	private static final String APPT = "appointment";
	
	private ClientImageDAO clientImageDAO = null;
	private SurveyManager surveyManager = new SurveyManagerImpl();
	private IMatchManager matchManager = new MatchManager();
	
	public void setOscarSurveyManager(SurveyManager mgr) {
		this.surveyManager = mgr;
	}

	public void setClientImageDAO(ClientImageDAO clientImageDAO) {
		this.clientImageDAO = clientImageDAO;
	}

	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

		// [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
		List genders = GenericIntakeSearchAction.getGenders();
		formBean.setGenders(genders);
		// end of change

		String intakeType = getType(request);
		String providerNo = getProviderNo(request);

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
		
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		Integer defaultCommunityProgramId = null;
		
		if(org.oscarehr.common.IsPropertiesOn.propertiesOn("oscarClinic")) {
			defaultCommunityProgramId = getOscarClinicDefaultCommunityProgramId(oscar.OscarProperties.getInstance().getProperty("oscarClinicDefaultProgram"));
		}
						
		setBeanProperties(formBean, intake, getClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
				.areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), defaultCommunityProgramId, null, null, loggedInInfo.currentFacility.getId(), null,jsLocation);

		request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);

		request.getSession().setAttribute("intakeCurrentBedCommunityId", null);
		
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

		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		Integer facilityId = loggedInInfo.currentFacility.getId();

		// [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
		List genders = GenericIntakeSearchAction.getGenders();
		formBean.setGenders(genders);
		// end of change

		String intakeType = getType(request);
		//Integer clientId = getClientIdAsInteger(request);
		String providerNo = getProviderNo(request);

		Integer nodeId = null;
		try {
			String strNodeId = getParameter(request,"nodeId");
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
		

		setBeanProperties(formBean, intake, client, providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
				.areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), null,
				null,null, facilityId, nodeId, jsLocation);

		// UCF -- intake accessment : please don't remove the following lines
		List allForms = surveyManager.getAllFormsForCurrentProviderAndCurrentFacility();
		request.getSession().setAttribute("survey_list", allForms);

		String oldBedProgramId = null;
		request.getSession().setAttribute("intakeCurrentBedCommunityId", oldBedProgramId);

		request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
				

		ProgramUtils.addProgramRestrictions(request);

		return mapping.findForward(EDIT);

	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		Integer facilityId = loggedInInfo.currentFacility.getId();

		// [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
		List genders = GenericIntakeSearchAction.getGenders();
		formBean.setGenders(genders);
		// end of change

		String intakeType = getType(request);
		Integer clientId = getClientIdAsInteger(request);
		String providerNo = getProviderNo(request);

		Integer nodeId = null;
		try {
			String strNodeId = getParameter(request,"nodeId");
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
		
		

		setBeanProperties(formBean, intake, getClient(clientId), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
				.areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), getCurrentBedCommunityProgramId(clientId),
				getCurrentServiceProgramIds(clientId), getCurrentExternalProgramId(clientId), facilityId, nodeId, jsLocation);

		// UCF -- intake accessment : please don't remove the following lines
		List allForms = surveyManager.getAllFormsForCurrentProviderAndCurrentFacility();
		request.getSession().setAttribute("survey_list", allForms);

		String oldBedProgramId = String.valueOf(getCurrentBedCommunityProgramId(clientId));
		request.getSession().setAttribute("intakeCurrentBedCommunityId", oldBedProgramId);

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

		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		Integer facilityId = loggedInInfo.currentFacility.getId();

		String intakeType = getType(request);
		String providerNo = getProviderNo(request);
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
		
		
		
		setBeanProperties(formBean, intake, getClient(clientId), providerNo, false, false, false, null, null, null, facilityId,null,jsLocation);

		return mapping.findForward(PRINT);
	}

	public ActionForward save_all(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String saveWhich) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		Intake intake = formBean.getIntake();
		String intakeType = intake.getType();
		Demographic client = formBean.getClient();
		String providerNo = getProviderNo(request);
		Integer nodeId = formBean.getNodeId();
		Integer oldId = null;
		
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
			
			// save client information.
			saveClient(client, providerNo);

			
					// for RFQ:
					if (OscarProperties.getInstance().isTorontoRFQ()) {
						Integer clientId = client.getDemographicNo();
						if (clientId != null && !"".equals(clientId)) {
							oldId = getCurrentBedCommunityProgramId(client.getDemographicNo());

							// Save 'external' program for RFQ.
							admitExternalProgram(client.getDemographicNo(), providerNo, formBean.getSelectedExternalProgramId());
						}
						// get and set intake location
						// client.setChildren(formBean.getProgramInDomainId());
						Integer intakeLocationId = 0;
						String intakeLocationStr = formBean.getProgramInDomainId();
						if (intakeLocationStr == null || "".equals(intakeLocationStr)) {
							Integer selectedBedCommunityProgramId = formBean.getSelectedBedCommunityProgramId();
							if ("RFQ_admit".equals(saveWhich)) {
								if (programManager.isBedProgram(selectedBedCommunityProgramId.toString())) {
									intakeLocationId = selectedBedCommunityProgramId;
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

					intake.setFacilityId(loggedInInfo.currentFacility.getId());

					String admissionText = null;
					String remoteReferralId = StringUtils.trimToNull(request.getParameter("remoteReferralId"));
					if (remoteReferralId != null) {
						admissionText = getAdmissionText(admissionText, remoteReferralId);
					}

					admitBedCommunityProgram(client.getDemographicNo(), providerNo, formBean.getSelectedBedCommunityProgramId(), saveWhich, admissionText);

					if (remoteReferralId != null) {
						// doing this after the admit is about as transactional as this is going to get for now.
						ReferralWs referralWs;
						try {
							referralWs = CaisiIntegratorManager.getReferralWs();
							referralWs.removeReferral(Integer.parseInt(remoteReferralId));
						}
						catch (Exception e) {
							LOG.error("Unexpected error", e);
						}
					}

					// if (!formBean.getSelectedServiceProgramIds().isEmpty() && "RFQ_admit".endsWith(saveWhich)) {
					//if (!formBean.getSelectedServiceProgramIds().isEmpty()) { //should be able to discharge from all service programs.
						admitServicePrograms(client.getDemographicNo(), providerNo, formBean.getSelectedServiceProgramIds(), null);
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
				
				
				setBeanProperties(formBean, intake, client, providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(
						intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), getCurrentBedCommunityProgramId(client.getDemographicNo()),
						getCurrentServiceProgramIds(client.getDemographicNo()), getCurrentExternalProgramId(client.getDemographicNo()), loggedInInfo.currentFacility.getId(), nodeId, jsLocation);

				String oldBedProgramId = String.valueOf(getCurrentBedCommunityProgramId(client.getDemographicNo()));
				request.getSession().setAttribute("intakeCurrentBedCommunityId", oldBedProgramId);

				String remoteFacilityIdStr = StringUtils.trimToNull(request.getParameter("remoteFacilityId"));
				String remoteDemographicIdStr = StringUtils.trimToNull(request.getParameter("remoteDemographicId"));
				if (remoteFacilityIdStr != null && remoteDemographicIdStr != null) {
					try {
						int remoteFacilityId = Integer.parseInt(remoteFacilityIdStr);
						int remoteDemographicId = Integer.parseInt(remoteDemographicIdStr);
						DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();

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

	private String getAdmissionText(String admissionText, String remoteReferralId) {
		try {
			ReferralWs referralWs = CaisiIntegratorManager.getReferralWs();
			Referral remoteReferral = referralWs.getReferral(Integer.parseInt(remoteReferralId));
			StringBuilder sb = new StringBuilder();

			CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(remoteReferral.getSourceIntegratorFacilityId());
			sb.append("Referred from : ");
			sb.append(cachedFacility.getName());

			FacilityIdStringCompositePk facilityProviderPrimaryKey = new FacilityIdStringCompositePk();
			facilityProviderPrimaryKey.setIntegratorFacilityId(remoteReferral.getSourceIntegratorFacilityId());
			facilityProviderPrimaryKey.setCaisiItemId(remoteReferral.getSourceCaisiProviderId());
			CachedProvider cachedProvider = CaisiIntegratorManager.getProvider(facilityProviderPrimaryKey);
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

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
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
	
	public ActionForward save_draft(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return save_all(mapping, form, request, response, "draft");
	}

	public ActionForward save_temp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return save_all(mapping, form, request, response, "RFQ_temp");
	}

	public ActionForward save_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return save_all(mapping, form, request, response, "RFQ_admit");
	}

	public ActionForward save_notAdmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return save_all(mapping, form, request, response, "RFQ_notAdmit");
	}

	public ActionForward clientEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;
		String forward = null;
		Integer clientEditId = formBean.getClient().getDemographicNo();

		StringBuilder parameters = new StringBuilder(PARAM_START);

		Set<Integer> serviceProgramIds = formBean.getSelectedServiceProgramIds();
		if (!serviceProgramIds.isEmpty()) {
			Integer serviceProgramId = serviceProgramIds.iterator().next(); //assumption: there is only one item in this set at a time. Take the 1st one.
			if (serviceProgramId == Integer.parseInt(OscarProperties.getIntakeProgramAccessServiceId())) {
				parameters.append(FORM_ID).append(PARAM_EQUALS).append(OscarProperties.getIntakeProgramAccessFId());
				forward = EFORM_ADD;
			} else if (serviceProgramId == Integer.parseInt(OscarProperties.getIntakeProgramCashServiceId())) {
				parameters.append(FORM_ID).append(PARAM_EQUALS).append(OscarProperties.getIntakeProgramCashFId());
				forward = EFORM_ADD;
			}
		}
		if (EFORM_ADD.equals(forward)) {
			parameters.append(PARAM_AND);
			parameters.append(DEMOGRAPHIC_NO).append(PARAM_EQUALS).append(clientEditId);
			parameters.append(PARAM_AND);
			parameters.append(APPOINTMENT).append(PARAM_EQUALS).append(0); // appointment code is always 0
		} else {
			parameters.append(CLIENT_EDIT_ID).append(PARAM_EQUALS).append(clientEditId);
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
        }

		return null;
	}

	// Adapt

	private Demographic getClient(Integer clientId) {
		return clientManager.getClientByDemographicNo(clientId.toString());
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

	private List<Program> getBedPrograms(Set<Program> providerPrograms, String providerNo) {
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

	private List<Program> getCommunityPrograms() {
		List<Program> communityPrograms = new ArrayList<Program>();

		for (Program program : programManager.getCommunityPrograms()) {
			communityPrograms.add(program);
		}

		return communityPrograms;
	}

	private List<Program> getServicePrograms(Set<Program> providerPrograms, String providerNo) {
		List<Program> servicePrograms = new ArrayList<Program>();

		for (Object o : programManager.getServicePrograms()) {
			Program program = (Program) o;

			if (providerPrograms.contains(program)) {
				if (OscarProperties.getInstance().isTorontoRFQ()) {
					if (caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, "", String.valueOf(program.getId()))) {
						servicePrograms.add(program);
					}
				}
				else {
					servicePrograms.add(program);
				}
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

	private Integer getCurrentExternalProgramId(Integer clientId) {
		Integer currentProgramId = null;

		Admission externalProgramAdmission = admissionManager.getCurrentExternalProgramAdmission(clientId);

		if (externalProgramAdmission != null) {
			currentProgramId = externalProgramAdmission.getProgramId();
		}

		return currentProgramId;
	}

	private SortedSet<Integer> getCurrentServiceProgramIds(Integer clientId) {
		SortedSet<Integer> currentProgramIds = new TreeSet<Integer>();

		List<?> admissions = admissionManager.getCurrentServiceProgramAdmission(clientId);
		if (admissions != null) {
			for (Object o : admissions) {
				Admission serviceProgramAdmission = (Admission) o;
				currentProgramIds.add(serviceProgramAdmission.getProgramId());
			}
		}

		return currentProgramIds;
	}

	private void saveClient(Demographic client, String providerNo) {
		client.setProviderNo(providerNo);

		clientManager.saveClient(client);
		try {
			log.info("Processing client creation event with MatchManager..." + 
					matchManager.<Demographic>processEvent(client, IMatchManager.Event.CLIENT_CREATED));
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error while processing MatchManager.processEvent(Client)",e);
		}
	}

	private void admitExternalProgram(Integer clientId, String providerNo, Integer externalProgramId) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		Program externalProgram = null;
		Integer currentExternalProgramId = getCurrentExternalProgramId(clientId);

		if (externalProgramId != null) {
			externalProgram = programManager.getProgram(externalProgramId);
		}

		if (externalProgram != null) {
			if (currentExternalProgramId == null) {
				admissionManager.processAdmission(clientId, providerNo, externalProgram, "intake external discharge", "intake external admit");
			}
			else if (!currentExternalProgramId.equals(externalProgramId)) {
				/*
				 * if (programManager.getProgram(externalProgramId).isExternal()) { if (externalProgram.isExternal()) { admissionManager.processAdmission(clientId, providerNo,
				 * externalProgram, "intake external discharge", "intake external admit"); } }
				 */
				admissionManager.processDischarge(currentExternalProgramId, clientId, "intake external discharge", "0");
				admissionManager.processAdmission(clientId, providerNo, externalProgram, "intake external discharge", "intake external admit");
			}
		}
	}

	private void admitBedCommunityProgram(Integer clientId, String providerNo, Integer bedCommunityProgramId, String saveWhich, String admissionText) throws ProgramFullException,
			AdmissionException, ServiceRestrictionException {
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
						admissionManager.processAdmission(familyId, providerNo, bedCommunityProgram, "intake discharge", admissionText);
					}
					else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
						if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
							if (bedCommunityProgram.isBed()) {
								admissionManager.processAdmission(familyId, providerNo, bedCommunityProgram, "intake discharge", admissionText);
							}
							else {
								admissionManager.processDischargeToCommunity(bedCommunityProgramId, familyId, providerNo, "intake discharge", "0");
							}
						}
						else {
							if (bedCommunityProgram.isCommunity()) {
								admissionManager.processDischargeToCommunity(bedCommunityProgramId, familyId, providerNo, "intake discharge", "0");
							}
							else {
								admissionManager.processDischarge(currentBedCommunityProgramId, familyId, "intake discharge", "0");
								admissionManager.processAdmission(familyId, providerNo, bedCommunityProgram, "intake discharge", admissionText);
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
					admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", admissionText);
				}
				else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
					if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
						if (bedCommunityProgram.isBed()) {
							// automatic discharge from one bed program to another bed program.
							admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", admissionText);
						}
						else {
							admissionManager.processDischargeToCommunity(bedCommunityProgramId, clientId, providerNo, "intake discharge", "0");
						}
					}
					else {
						if (bedCommunityProgram.isCommunity()) {
							admissionManager.processDischargeToCommunity(bedCommunityProgramId, clientId, providerNo, "intake discharge", "0");
						}
						else {
							admissionManager.processDischarge(currentBedCommunityProgramId, clientId, "intake discharge", "0");
							admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", admissionText);
						}
					}
				}
			}
		}
	}

	private void admitServicePrograms(Integer clientId, String providerNo, Set<Integer> serviceProgramIds, String admissionText) throws ProgramFullException, AdmissionException,
			ServiceRestrictionException {
		SortedSet<Integer> currentServicePrograms = getCurrentServiceProgramIds(clientId);

		if (admissionText == null) admissionText = "intake admit";
		
		if( serviceProgramIds.isEmpty()) {
			for(Object programId : currentServicePrograms) {
				admissionManager.processDischarge((Integer) programId, clientId, "intake discharge", "0");
			}
			return;
		}
		
		Collection<?> discharge = CollectionUtils.subtract(currentServicePrograms, serviceProgramIds);

		for (Object programId : discharge) {
			admissionManager.processDischarge((Integer) programId, clientId, "intake discharge", "0");
		}

		Collection<?> admit = CollectionUtils.subtract(serviceProgramIds, currentServicePrograms);

		for (Object programId : admit) {
			Program program = programManager.getProgram((Integer) programId);
			admissionManager.processAdmission(clientId, providerNo, program, "intake discharge", admissionText);
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

	private Set<Program> getActiveProviderProgramsInFacility(String providerNo, Integer facilityId) {
		Set<Program> programs = new HashSet<Program>();
		Set<Program> programsInDomain = getActiveProviderPrograms(providerNo);
		if (facilityId == null) return programs;

		for (Program p : programManager.getProgramDomainInCurrentFacilityForCurrentProvider(false)) {
			if (programsInDomain.contains(p)) {
				programs.add(p);
			}
		}

		return programs;
	}

	// Bean

	private void setBeanProperties(GenericIntakeEditFormBean formBean, Intake intake, Demographic client, String providerNo, boolean bedCommunityProgramsVisible,
			boolean serviceProgramsVisible, boolean externalProgramsVisible, Integer currentBedCommunityProgramId, SortedSet<Integer> currentServiceProgramIds,
			Integer currentExternalProgramId, Integer facilityId, Integer nodeId, List<IntakeNodeJavascript> javascriptLocation) {
		formBean.setIntake(intake);
		formBean.setClient(client);
		formBean.setNodeId(nodeId);
		formBean.setJsLocation(javascriptLocation);
		
		if (bedCommunityProgramsVisible || serviceProgramsVisible || externalProgramsVisible) {
			Set<Program> providerPrograms = getActiveProviderProgramsInFacility(providerNo, facilityId);

			if (bedCommunityProgramsVisible) {
				formBean.setBedCommunityPrograms(getBedPrograms(providerPrograms, providerNo), getCommunityPrograms());
				formBean.setSelectedBedCommunityProgramId(currentBedCommunityProgramId);
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

}
