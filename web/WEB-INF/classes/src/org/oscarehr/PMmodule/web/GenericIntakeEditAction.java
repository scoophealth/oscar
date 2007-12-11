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
import org.apache.struts.util.LabelValueBean;
import org.caisi.model.CaisiEditor;
import org.caisi.service.CaisiEditorManager;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean;

import oscar.OscarProperties;

public class GenericIntakeEditAction extends BaseGenericIntakeAction {

    private static Log LOG = LogFactory.getLog(GenericIntakeEditAction.class);

    // Forwards
    private static final String EDIT = "edit";
    private static final String PRINT = "print";
    private static final String CLIENT_EDIT = "clientEdit";

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        // [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
        List lGenders = caisiEditorManager.getActiveLabelCaisiEditor("gender");
        int nSize = lGenders.size();
        LabelValueBean[] genders = new LabelValueBean[nSize];
        for (int i = 0; i < nSize; i++) {
            CaisiEditor ce = (CaisiEditor) lGenders.get(i);
            genders[i] = new LabelValueBean(ce.getLabelValue(), ce.getLabelCode());
        }
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

        setBeanProperties(formBean, intake, getClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(
                intakeType), null, null, null);

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

        setBeanProperties(formBean, intake, getRemoteClient(request), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency()
                .areExternalProgramsVisible(intakeType), null, null, null);
        formBean.setRemoteAgency(getRemoteAgency(request));
        formBean.setRemoteAgencyDemographicNo(getRemoteAgencyDemographicNo(request));

        return mapping.findForward(EDIT);
    }

    private Demographic getRemoteClient(HttpServletRequest request) {
        return integratorManager.getDemographic(getRemoteAgency(request), getRemoteAgencyDemographicNo(request));
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeEditFormBean formBean = (GenericIntakeEditFormBean) form;

        // [ 1842774 ] RFQ Feature: link reg intake gender to list editor table;
        List lGenders = caisiEditorManager.getActiveLabelCaisiEditor("gender");
        int nSize = lGenders.size();
        LabelValueBean[] genders = new LabelValueBean[nSize];
        for (int i = 0; i < nSize; i++) {
            CaisiEditor ce = (CaisiEditor) lGenders.get(i);
            genders[i] = new LabelValueBean(ce.getLabelValue(), ce.getLabelCode());
        }
        formBean.setGenders(genders);
        // end of change

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

        setBeanProperties(formBean, intake, getClient(clientId), providerNo, Agency.getLocalAgency().areHousingProgramsVisible(intakeType), Agency.getLocalAgency().areServiceProgramsVisible(intakeType), Agency.getLocalAgency().areExternalProgramsVisible(
                intakeType), getCurrentBedCommunityProgramId(clientId), getCurrentServiceProgramIds(clientId), getCurrentExternalProgramId(clientId));

        // UCF -- intake accessment : please don't remove the following line
        request.getSession().setAttribute("survey_list", surveyManager.getAllForms());

        ProgramUtils.addProgramRestrictions(request);

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

            // for RFQ: to save 'external' program.
            if (OscarProperties.getInstance().isTorontoRFQ()) {
                admitExternalProgram(client.getDemographicNo(), providerNo, formBean.getSelectedExternalProgramId());
            }

            admitBedCommunityProgram(client.getDemographicNo(), providerNo, formBean.getSelectedBedCommunityProgramId());

            if (!formBean.getSelectedServiceProgramIds().isEmpty()) admitServicePrograms(client.getDemographicNo(), providerNo, formBean.getSelectedServiceProgramIds());
            saveIntake(intake, client.getDemographicNo());

            // don't move updateRemote up...this method has the problem needs to be fixed
            if (integratorManager.isEnabled()) updateRemote(client, formBean.getRemoteAgency(), formBean.getRemoteAgencyDemographicNo());
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
                getCurrentBedCommunityProgramId(client.getDemographicNo()), getCurrentServiceProgramIds(client.getDemographicNo()), getCurrentExternalProgramId(client.getDemographicNo()));

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

    private void setBeanProperties(GenericIntakeEditFormBean formBean, Intake intake, Demographic client, String providerNo, boolean bedCommunityProgramsVisible, boolean serviceProgramsVisible, boolean externalProgramsVisible,
            Integer currentBedCommunityProgramId, SortedSet<Integer> currentServiceProgramIds, Integer currentExternalProgramId) {
        formBean.setIntake(intake);
        formBean.setClient(client);

        if (bedCommunityProgramsVisible || serviceProgramsVisible || externalProgramsVisible) {
            Set<Program> providerPrograms = getActiveProviderPrograms(providerNo);

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
        }
    }

}