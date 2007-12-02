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
 * You should have received a copy of the GNU General Public License
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
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class GenericIntakeEditAction extends BaseGenericIntakeAction {

    private static Log LOG = LogFactory.getLog(GenericIntakeEditAction.class);

    private ProgramDao programDao=(ProgramDao)SpringUtils.beanFactory.getBean("programDao");
    
    // Forwards
    private static final String EDIT = "edit";
    private static final String PRINT = "print";
    private static final String CLIENT_EDIT = "clientEdit";

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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

        setBeanProperties(formBean, intake, getClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency
                .getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), null, null, null);

        //--- program gender issues ---
        
        StringBuilder programMaleOnly=new StringBuilder("[");
        StringBuilder programFemaleOnly=new StringBuilder("[");
        StringBuilder programTransgenderOnly=new StringBuilder("[");
        
        for (Program program : programDao.getProgramByGenderType("Man"))
        {
            if (programMaleOnly.length()>1) programMaleOnly.append(',');
            programMaleOnly.append(program.getId());
        }
        for (Program program : programDao.getProgramByGenderType("Woman"))
        {
            if (programFemaleOnly.length()>1) programFemaleOnly.append(',');
            programFemaleOnly.append(program.getId());
        }
        for (Program program : programDao.getProgramByGenderType("Transgender"))
        {
            if (programTransgenderOnly.length()>1) programTransgenderOnly.append(',');
            programTransgenderOnly.append(program.getId());
        }
        
        programMaleOnly.append(']');
        programFemaleOnly.append(']');
        programTransgenderOnly.append(']');
        
        // yeah I know we shouldn't set it in the session but I can't set it in the request because struts isn't being used properly and this method isn't actually called before render, it's called in a prior request method. 
        // considering no one cares about the quality of this code anymore it's simpler for me to continue on as is and use the session space knowing it'll cause the session / shared variable issues. Sorry but management says quality is not a priority and speed is instead. So, here's proliferating a bad practice in the name of speed.
        request.getSession().setAttribute("programMaleOnly", programMaleOnly.toString());
        request.getSession().setAttribute("programFemaleOnly", programFemaleOnly.toString());
        request.getSession().setAttribute("programTransgenderOnly", programTransgenderOnly.toString());
               
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

        setBeanProperties(formBean, intake, getRemoteClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency
                .getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType), null, null, null);
        formBean.setRemoteAgency(getRemoteAgency(request));
        formBean.setRemoteAgencyDemographicNo(getRemoteAgencyDemographicNo(request));

        return mapping.findForward(EDIT);
    }

    private Demographic getRemoteClient(HttpServletRequest request) {
        return integratorManager.getDemographic(getRemoteAgency(request), getRemoteAgencyDemographicNo(request));
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        String intakeType = getType(request);
        Integer clientId = getClientId(request);
        String providerNo = getProviderNo(request);

        Intake intake = null;

        if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.copyQuickIntake(clientId, providerNo);
        }
        else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.copyIndepthIntake(clientId, providerNo);
        }
        else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.copyProgramIntake(clientId, getProgramId(request), providerNo);
        }

        setBeanProperties(formBean, intake, getClient(clientId), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency
                .getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType),getCurrentBedCommunityProgramId(clientId), getCurrentServiceProgramIds(clientId),getCurrentExternalProgramId(clientId));

        return mapping.findForward(EDIT);
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        String intakeType = getType(request);
        Integer clientId = getClientId(request);
        String providerNo = getProviderNo(request);

        Intake intake = null;

        if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.getMostRecentQuickIntake(clientId);
        }
        else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.getMostRecentIndepthIntake(clientId);
        }
        else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
            intake = genericIntakeManager.getMostRecentProgramIntake(clientId, getProgramId(request));
        }

        setBeanProperties(formBean, intake, getClient(clientId), providerNo, false, false, false, null, null, null);

        return mapping.findForward(PRINT);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        Intake intake = formBean.getIntake();
        String intakeType = intake.getType();
        Demographic client = formBean.getClient();
        String providerNo = getProviderNo(request);

        try {
            saveClient(client, providerNo);
            admitBedCommunityProgram(client.getDemographicNo(), providerNo, formBean.getSelectedBedCommunityProgramId());
            if (!formBean.getSelectedServiceProgramIds().isEmpty()) admitServicePrograms(client.getDemographicNo(), providerNo, formBean
                    .getSelectedServiceProgramIds());
            saveIntake(intake, client.getDemographicNo());
            // don't move updateRemote up...this method has the problem needs to be fixed
            updateRemote(client, formBean.getRemoteAgency(), formBean.getRemoteAgencyDemographicNo());

        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.error(e);

            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message", e.getMessage()));
            saveErrors(request, messages);
        }

        setBeanProperties(formBean, intake, client, providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency()
                .areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(intakeType),getCurrentBedCommunityProgramId(client.getDemographicNo()), getCurrentServiceProgramIds(client
                .getDemographicNo()),getCurrentExternalProgramId(client.getDemographicNo()));

        return mapping.findForward(EDIT);
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

    private List<Program> getBedPrograms(Set<Program> providerPrograms) {
        List<Program> bedPrograms = new ArrayList<Program>();

        for (Program program : programManager.getBedPrograms()) {
            if (providerPrograms.contains(program)) {
                bedPrograms.add(program);
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

    private List<Program> getServicePrograms(Set<Program> providerPrograms) {
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

        for (Object o : programManager.getExternalPrograms()) {
            Program program = (Program) o;

           /* if (providerPrograms.contains(program)) {
                externalPrograms.add(program);
            }*/
            externalPrograms.add(program);
        }

        return externalPrograms;
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

        Demographic.OptingStatus defaultOptingStatus = null;

        String optingDefaultString = OscarProperties.getInstance().getProperty("DATA_SHARING_OPTING_DEFAULT");
        if (optingDefaultString != null) optingDefaultString = optingDefaultString.trim();
        // yes I know this step is silly since I convert it back to a string later but it ensures it's a valid option.
        if (optingDefaultString != null) defaultOptingStatus = Demographic.OptingStatus.valueOf(optingDefaultString);

        // local save
        clientManager.saveClient(client);

        if (defaultOptingStatus != null) clientManager
                .saveDemographicExt(client.getDemographicNo(), Demographic.SHARING_OPTING_KEY, defaultOptingStatus.name());

    }

    private void admitBedCommunityProgram(Integer clientId, String providerNo, Integer bedCommunityProgramId) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
        Program bedCommunityProgram = null;
        Integer currentBedCommunityProgramId = getCurrentBedCommunityProgramId(clientId);

        if (bedCommunityProgramId == null && currentBedCommunityProgramId == null) {
            bedCommunityProgram = programManager.getHoldingTankProgram();
        }
        else if (bedCommunityProgramId != null) {
            bedCommunityProgram = programManager.getProgram(bedCommunityProgramId);
        }

        if (bedCommunityProgram != null) {
            if (currentBedCommunityProgramId == null) {
                admissionManager.processAdmission(clientId, providerNo, bedCommunityProgram, "intake discharge", "intake admit");
            }
            else if (!currentBedCommunityProgramId.equals(bedCommunityProgramId)) {
                if (programManager.getProgram(currentBedCommunityProgramId).isBed()) {
                    if (bedCommunityProgram.isBed()) {
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

    // Bean

    private void setBeanProperties(GenericIntakeEditFormBean formBean, Intake intake, Demographic client, String providerNo,
            boolean bedCommunityProgramsVisible, boolean serviceProgramsVisible, boolean externalProgramsVisible, Integer currentBedCommunityProgramId,
            SortedSet<Integer> currentServiceProgramIds,Integer currentExternalProgramId) {
        formBean.setIntake(intake);
        formBean.setClient(client);

        if (bedCommunityProgramsVisible || serviceProgramsVisible || externalProgramsVisible) {
            Set<Program> providerPrograms = getActiveProviderPrograms(providerNo);

            if (bedCommunityProgramsVisible) {
                formBean.setBedCommunityPrograms(getBedPrograms(providerPrograms), getCommunityPrograms());
                formBean.setSelectedBedCommunityProgramId(currentBedCommunityProgramId);
            }

            if (serviceProgramsVisible) {
                formBean.setServicePrograms(getServicePrograms(providerPrograms));
                formBean.setSelectedServiceProgramIds(currentServiceProgramIds);
            }
            
            if (externalProgramsVisible) {
                formBean.setExternalPrograms(getExternalPrograms(providerPrograms));
                formBean.setSelectedExternalProgramId(currentExternalProgramId);
            }
        }
    }


}