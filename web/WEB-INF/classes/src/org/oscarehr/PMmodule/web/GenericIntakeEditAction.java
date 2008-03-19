
/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Gengeneral Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.JointAdmission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean;
import org.oscarehr.util.SessionConstants;

import oscar.OscarProperties;

public class GenericIntakeEditAction extends BaseGenericIntakeAction {

    private static Log LOG = LogFactory.getLog(GenericIntakeEditAction.class);
    // Forwards
    private static final String EDIT = "edit";
    private static final String PRINT = "print";
    private static final String CLIENT_EDIT = "clientEdit";

    protected SurveyManager surveyManager;

    public void setSurveyManager(SurveyManager mgr) {
        this.surveyManager = mgr;
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        // [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
        List genders =  lookupManager.LoadCodeList("GEN", true, null,null);
        formBean.setGenders(genders);
        // end of change

        String intakeType = getType(request);
        String providerNo = getProviderNo(request);

        Intake intake = null;

        if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.createQuickIntake(providerNo);
        }
        else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.createIndepthIntake(providerNo);
        }
        else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
        }
        
		Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        
        setBeanProperties(formBean, intake, getClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(
                intakeType), null, null, null,facilityId);
        
        request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
        
        request.getSession().setAttribute("intakeCurrentBedCommunityId",null);
        
        ProgramUtils.addProgramRestrictions(request);
        
        return mapping.findForward(EDIT);
    }

    public ActionForward create_remote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        String intakeType = getType(request);
        String providerNo = getProviderNo(request);

        Intake intake = null;

        if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.createQuickIntake(providerNo);
        }
        else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.createIndepthIntake(providerNo);
        }
        else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
        }


        Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        
        setBeanProperties(formBean, intake, getRemoteClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency()
                .areExternalProgramsVisible(intakeType), null, null, null,facilityId);
        formBean.setRemoteAgency(getRemoteAgency(request));
        formBean.setRemoteAgencyDemographicNo(getRemoteAgencyDemographicNo(request));

        
        return mapping.findForward(EDIT);
    }

    private Demographic getRemoteClient(HttpServletRequest request) {
        return integratorManager.getDemographic(getRemoteAgency(request), getRemoteAgencyDemographicNo(request));
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        
        // [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
        List genders = lookupManager.LoadCodeList("GEN",true,null,null);
        formBean.setGenders(genders);
        // end of change

        String intakeType = getType(request);
        Integer clientId = getClientId(request);
        String providerNo = getProviderNo(request);

        Intake intake = null;

        if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.copyQuickIntake(clientId, providerNo, facilityId);
            if (intake==null)
            {
                intake = genericIntakeManager.createQuickIntake(providerNo);;
                intake.setClientId(clientId);
                intake.setFacilityId(facilityId);
            }
        }
        else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.copyIndepthIntake(clientId, providerNo, facilityId);
            if (intake==null)
            {
                intake = genericIntakeManager.createIndepthIntake(providerNo);;
                intake.setClientId(clientId);
                intake.setFacilityId(facilityId);
            }
        }
        else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.copyProgramIntake(clientId, getProgramId(request), providerNo, facilityId);
            if (intake==null)
            {
                intake = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
                intake.setClientId(clientId);
                intake.setFacilityId(facilityId);
            }
        }
      
        setBeanProperties(formBean, intake, getClient(clientId), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(
                intakeType), getCurrentBedCommunityProgramId(clientId), getCurrentServiceProgramIds(clientId), getCurrentExternalProgramId(clientId),facilityId);

        // UCF -- intake accessment : please don't remove the following line
        request.getSession().setAttribute("survey_list", surveyManager.getAllForms());
        
        String oldBedProgramId = String.valueOf(getCurrentBedCommunityProgramId(clientId));
        request.getSession().setAttribute("intakeCurrentBedCommunityId",oldBedProgramId);
        
        if(clientManager.isClientFamilyHead(clientId)) {
        	request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
        } else {
        	if(clientManager.isClientDependentOfFamily(clientId))
        		request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, true);
        	else 
        		request.getSession().setAttribute(SessionConstants.INTAKE_CLIENT_IS_DEPENDENT_OF_FAMILY, false);
        } 
        
        ProgramUtils.addProgramRestrictions(request);
        
        return mapping.findForward(EDIT);
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        
        String intakeType = getType(request);
        Integer clientId = getClientId(request);
        String providerNo = getProviderNo(request);

        Intake intake = null;

        if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.getMostRecentQuickIntake(clientId, facilityId);
        }
        else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.getMostRecentIndepthIntake(clientId, facilityId);
        }
        else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.getMostRecentProgramIntake(clientId, getProgramId(request), facilityId);
        }
        
        setBeanProperties(formBean, intake, getClient(clientId), providerNo, false, false, false, null, null, null,facilityId);

        return mapping.findForward(PRINT);
    }

    public ActionForward save_all(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String saveWhich) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;
        Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        
        Intake intake = formBean.getIntake();
        String intakeType = intake.getType();
        Demographic client = formBean.getClient();
        String providerNo = getProviderNo(request);
        
        Integer oldId=null ;
        Integer newId=null;
        try { 
        	// save client information.
            saveClient(client, providerNo);
            
            // for RFQ: 
            if (OscarProperties.getInstance().isTorontoRFQ()) {
            	Integer clientId = client.getDemographicNo();
            	if(clientId !=null && !"".equals(clientId)) {
            		oldId = getCurrentBedCommunityProgramId(client.getDemographicNo());
            		newId = formBean.getSelectedBedCommunityProgramId();
        		
            	            	
            	//Save 'external' program for RFQ.
            	admitExternalProgram(client.getDemographicNo(), providerNo, formBean.getSelectedExternalProgramId());
            	}
            	//get and set intake location
            	//client.setChildren(formBean.getProgramInDomainId());
            	Integer intakeLocationId = 0;
            	String intakeLocationStr = formBean.getProgramInDomainId();
            	if(intakeLocationStr ==null || "".equals(intakeLocationStr)) {
            		Integer selectedBedCommunityProgramId = formBean.getSelectedBedCommunityProgramId();
            		if("RFQ_admit".equals(saveWhich)) {
            			if(programManager.isBedProgram(selectedBedCommunityProgramId.toString())) {           			
            				intakeLocationId = selectedBedCommunityProgramId;
            			} else {
            				intakeLocationId = Integer.valueOf(formBean.getProgramInDomainId());
            			}
            		}
            	} else {
            		intakeLocationId = Integer.valueOf(intakeLocationStr);
            	}
            	
            	intake.setIntakeLocation(intakeLocationId); 
            	
            	Facility currentFacility = (Facility)request.getSession().getAttribute("currentFacility");
            	Integer currentFacilityId = currentFacility.getId();
            	intake.setFacilityId(currentFacilityId);
            }
            
           
            	admitBedCommunityProgram(client.getDemographicNo(), providerNo, formBean.getSelectedBedCommunityProgramId(), saveWhich);
            
            	if (!formBean.getSelectedServiceProgramIds().isEmpty() && "RFQ_admit".endsWith(saveWhich)) {
            		admitServicePrograms(client.getDemographicNo(), providerNo, formBean.getSelectedServiceProgramIds());
            	}
         
            
            if("normal".equals(saveWhich)) {
            	//normal intake saving . eg. seaton house
            	intake.setIntakeStatus("Signed");
            	intake.setId(null);
            	saveIntake(intake, client.getDemographicNo());
            } else {
            	//RFQ intake saving...
            	if ("RFQ_temp".equals(saveWhich)) {            
            		intake.setIntakeStatus("Unsigned");            	
            		saveUpdateIntake(intake, client.getDemographicNo());
            	} else if("RFQ_admit".equals(saveWhich)) {
            		intake.setIntakeStatus("Signed");
            		intake.setId(null);
            		saveIntake(intake, client.getDemographicNo());
            	} else if("RFQ_notAdmit".equals(saveWhich)) {
            		intake.setIntakeStatus("Signed");
            		intake.setId(null);
            		saveIntake(intake, client.getDemographicNo());
            	}
            }
            
            
            // don't move updateRemote up...this method has the problem needs to be fixed
            if (integratorManager.isEnabled()) {
            	updateRemote(client, formBean.getRemoteAgency(), formBean.getRemoteAgencyDemographicNo());
            }
        }
        catch (ProgramFullException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
            saveErrors(request, messages);
        }
        catch (AdmissionException e) {
            e.printStackTrace();
            LOG.error(e);

            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message", e.getMessage()));
            saveErrors(request, messages);
        }
        catch (ServiceRestrictionException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.service_restricted", e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()));
            saveErrors(request, messages);
        }

        setBeanProperties(formBean, intake, client, providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType),
                getCurrentBedCommunityProgramId(client.getDemographicNo()), getCurrentServiceProgramIds(client.getDemographicNo()), getCurrentExternalProgramId(client.getDemographicNo()),facilityId);
        
        String oldBedProgramId = String.valueOf(getCurrentBedCommunityProgramId(client.getDemographicNo()));
        request.getSession().setAttribute("intakeCurrentBedCommunityId",oldBedProgramId);
        
        if(("RFQ_admit".equals(saveWhich) || "RFQ_notAdmit".equals(saveWhich)) && oldId!=null) {
        	return clientEdit(mapping, form, request,response);
        } else {
        	return mapping.findForward(EDIT);
        }
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	return save_all(mapping,form,request,response, "normal");
    }
    
    public ActionForward save_temp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	return save_all(mapping,form,request,response,"RFQ_temp");
    }
    
    public ActionForward save_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	return save_all(mapping,form,request,response,"RFQ_admit");
    }
    
    public ActionForward save_notAdmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	return save_all(mapping,form,request,response,"RFQ_notAdmit");
    }
    
    private void updateRemote(Demographic client, String remoteAgency, Long remoteAgencyDemographicNo) {
        integratorManager.saveClient(client, remoteAgency, remoteAgencyDemographicNo);
    }

    public ActionForward clientEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        Integer clientEditId = formBean.getClient().getDemographicNo();

        StringBuilder parameters = new StringBuilder(PARAM_START);
        parameters.append(CLIENT_EDIT_ID).append(PARAM_EQUALS).append(clientEditId);

        return createRedirectForward(mapping, CLIENT_EDIT, parameters);
    }

    // Adapt

    private Demographic getClient(Integer clientId) {
        return clientManager.getClientByDemographicNo(clientId.toString());
    }

    private Set<Program> getActiveProviderPrograms(String providerNo) {
        Set<Program> activeProviderPrograms = new HashSet<Program>();

        for (Program providerProgram : programManager.getProgramDomain(providerNo)) {
            if (providerProgram.isActive()) {
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

        Demographic.ConsentGiven defaultConsentStatus = null;

        String consentDefaultString = OscarProperties.getInstance().getProperty("CONSENT_DEFAULT");
        if (consentDefaultString != null) consentDefaultString = consentDefaultString.trim();
        // yes I know this step is silly since I convert it back to a string later but it ensures it's a valid option.
        if (consentDefaultString != null) defaultConsentStatus = Demographic.ConsentGiven.valueOf(consentDefaultString);

        // local save
        clientManager.saveClient(client);

        if (defaultConsentStatus != null){
            clientManager.saveDemographicExt(client.getDemographicNo(), Demographic.CONSENT_GIVEN_KEY, defaultConsentStatus.name());
            clientManager.saveDemographicExt(client.getDemographicNo(), Demographic.METHOD_OBTAINED_KEY, Demographic.MethodObtained.IMPLICIT.name());
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
                 * if (programManager.getProgram(externalProgramId).isExternal()) { if (externalProgram.isExternal()) { admissionManager.processAdmission(clientId, providerNo, externalProgram, "intake external discharge", "intake external admit"); } }
                 */
                admissionManager.processDischarge(currentExternalProgramId, clientId, "intake external discharge", "0");
                admissionManager.processAdmission(clientId, providerNo, externalProgram, "intake external discharge", "intake external admit");
            }
        }
    }

    private void admitBedCommunityProgram(Integer clientId, String providerNo, Integer bedCommunityProgramId, String saveWhich) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
        Program bedCommunityProgram = null;
        Integer currentBedCommunityProgramId = getCurrentBedCommunityProgramId(clientId);
        
        if("RFQ_notAdmit".equals(saveWhich) && bedCommunityProgramId == null && currentBedCommunityProgramId == null) {
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

		if(clientManager != null  &&  clientId != null){
			dependentList = clientManager.getDependents(Long.valueOf(clientId));
			clientsJadm = clientManager.getJointAdmission(Long.valueOf(clientId));
		}
		if (clientsJadm != null  &&  clientsJadm.getHeadClientId() != null) {
			isFamilyDependent = true;
		}
		if(dependentList != null  &&  dependentList.size() > 0){
			isFamilyHead = true;
		}
        if(dependentList != null){
        	dependentIds = new Integer[dependentList.size()];
			for(int i=0; i < dependentList.size(); i++ ){
				dependentIds[i] = new Integer(((JointAdmission)dependentList.get(i)).getClientId().intValue());
			}
        }
        
        if(isFamilyDependent){
        	throw new AdmissionException("you cannot admit a dependent family/group member, you must remove the dependent status or admit the family head");
        
        }else if(isFamilyHead &&  dependentIds != null  &&  dependentIds.length >= 1){
        	Integer[] familyIds = new Integer[dependentIds.length + 1];
        	familyIds[0] = clientId;
        	for(int i=0; i < dependentIds.length; i++ ){
        		familyIds[i+1] = dependentIds[i];
        	}
        	for(int i=0; i < familyIds.length; i++ ){
        		Integer familyId = familyIds[i];
		        if (bedCommunityProgram != null) {
		            if (currentBedCommunityProgramId == null) {
		                admissionManager.processAdmission(familyId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
		            }
		            else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
		                if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
		                    if (bedCommunityProgram.isBed()) {
		                        admissionManager.processAdmission(familyId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
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
		                        admissionManager.processAdmission(familyId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
		                    }
		                }
		            }
		        }
        	}
        	
        	//throw new AdmissionException("If you admit the family head, all dependents will also be admitted to this program and discharged from their current programs. Are you sure you wish to proceed?");
        
        }else{
        
	        if (bedCommunityProgram != null) {
	            if (currentBedCommunityProgramId == null) {	            	
	                admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
	            }
	            else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
	                if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
	                    if (bedCommunityProgram.isBed()) {
	                        //automatic discharge from one bed program to another bed program.
	                    	admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
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
	                        admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
	                    }
	                }
	            }
	        }
        }
    }

    private void admitServicePrograms(Integer clientId, String providerNo, Set<Integer> serviceProgramIds) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
        SortedSet<Integer> currentServicePrograms = getCurrentServiceProgramIds(clientId);

        Collection<?> discharge = CollectionUtils.subtract(currentServicePrograms, serviceProgramIds);

        for (Object programId : discharge) {
            admissionManager.processDischarge((Integer) programId, clientId, "intake discharge", "0");
        }

        Collection<?> admit = CollectionUtils.subtract(serviceProgramIds, currentServicePrograms);

        for (Object programId : admit) {
            Program program = programManager.getProgram((Integer) programId);
            admissionManager.processAdmission(clientId, providerNo, program, "intake discharge", "intake admit");
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
        if(facilityId==null) 
        	return programs;
        
        for(Program p : programManager.getProgramDomainInFacility(providerNo, Long.valueOf(facilityId)))
        		 {
        	if(programsInDomain.contains(p)) {
        		programs.add(p);
        	}
        }
        
        return programs;
    }
    
    // Bean

    private void setBeanProperties(GenericIntakeEditFormBean formBean, Intake intake, Demographic client, String providerNo, boolean bedCommunityProgramsVisible, boolean serviceProgramsVisible, boolean externalProgramsVisible,
            Integer currentBedCommunityProgramId, SortedSet<Integer> currentServiceProgramIds, Integer currentExternalProgramId, Integer facilityId) {
        formBean.setIntake(intake);
        formBean.setClient(client);

        if (bedCommunityProgramsVisible || serviceProgramsVisible || externalProgramsVisible) {
            Set<Program> providerPrograms = getActiveProviderPrograms(providerNo);
            if (OscarProperties.getInstance().isTorontoRFQ()) {        		
        		providerPrograms = getActiveProviderProgramsInFacility(providerNo,facilityId);
        	} else {
        		providerPrograms = getActiveProviderPrograms(providerNo);
        	}
        	
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
           
            String intakeLocation="";
            if(intake!=null) {
            	intakeLocation= String.valueOf(intake.getIntakeLocation());
            }
            if(intakeLocation ==null || "".equals(intakeLocation) || "null".equals(intakeLocation)) {
            	formBean.setSelectedProgramInDomainId(0);
            } else {
            	formBean.setSelectedProgramInDomainId(Integer.valueOf(intakeLocation));
            }            
        }
       
    }

}