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
package org.oscarehr.sharingcenter.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.marc.shic.core.CodeValue;
import org.marc.shic.core.MappingCodeType;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.pix.PixApplicationException;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.sharingcenter.AffinityDomainDocuments;
import org.oscarehr.sharingcenter.SharedDocumentsModel;
import org.oscarehr.sharingcenter.SharingCenterUtil;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.CodeMappingDataObject;
import org.oscarehr.sharingcenter.model.CodeValueDataObject;
import org.oscarehr.sharingcenter.model.PatientDocument;
import org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class SharedDocumentsServlet extends Action {

    private static final AffinityDomainDao affinityDomainDao = SpringUtils.getBean(AffinityDomainDao.class);
    private static final DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SharedDocumentsModel model = new SharedDocumentsModel();

        int parsedDemographicId = Integer.parseInt(request.getParameter("demographic_no"));

        model.setDemographic(demographicDao.getDemographicById(parsedDemographicId));
        model.setDecisionMakers(SharingCenterUtil.getRelationships(parsedDemographicId));

        String providerId = (String) request.getSession().getAttribute("user");

        // Get all policies for the affinity domain.
        model.setSharedAffinityDomains(SharingCenterUtil.getSharedNetworks(parsedDemographicId));

        AffinityDomainDataObject selectedAffinityDomain = null;

        // Change the affinity domain that is currently being viewed.
        if (request.getParameter("viewAffinityDomain") != null) {
            int postedAffinityDomainId = Integer.parseInt(request.getParameter("targetAffinityDomain"));
            selectedAffinityDomain = affinityDomainDao.getAffinityDomain(postedAffinityDomainId);

        } else {
            // No selected affinity domain, use the first shared network found.
            if (request.getParameter("selectedAffinityDomain") == null) {
                if (model.getSharedAffinityDomains().size() > 0) {
                    selectedAffinityDomain = model.getSharedAffinityDomains().get(0);
                }
            } else {
                int postedAffinityDomainId = Integer.parseInt(request.getParameter("selectedAffinityDomain"));
                selectedAffinityDomain = affinityDomainDao.getAffinityDomain(postedAffinityDomainId);
            }
        }
        
        // redirect to allow the patient to share with an affinity domain
        if (selectedAffinityDomain == null) {
            response.sendRedirect(String.format(request.getContextPath() + "/sharingcenter/networks/sharingnetworks.jsp?demographic_no=%s", parsedDemographicId));
            return null;
        }

        // Set the affinity domain the model represents.
        model.setSelectedAffinityDomain(selectedAffinityDomain);

        // get elevation codes
        CodeMappingDataObject codeMappings = selectedAffinityDomain.getCodeMapping(MappingCodeType.ConfidentialityCode.toString());

        // Populate the model with the codemappings for the selected affinity domain.
        model.setConsentCodeMappings(codeMappings);

        model.setConsentGiven(request.getParameter("confirmElevate") != null);

        // Elevate
        CodeValue purposeOfUse = null;
        if (model.isConsentGiven()) {
            // get the purpose of use (intent)
            for (CodeValueDataObject code : codeMappings.getCodeValues()) {
                if (request.getParameter("purposeOfUse").equals(code.getCode())) {
                    purposeOfUse = code.createCodeValue();
                    break;
                }
            }
        }

        List<Integer> policies = new ArrayList<Integer>();
        if (request.getParameter("confirmConsent") != null) {
            int authenticatorId = Integer.parseInt(request.getParameter("authenticatorSelect"));

            for (PolicyDefinitionDataObject policy : SharingCenterUtil.getAllUnconsentedPolicies(parsedDemographicId, selectedAffinityDomain.getId())) {
                policies.add(policy.getId());
            }

            // Send BPPC
            Boolean bppcResult = SharingCenterUtil.giveConsentAndAcknowledgement(parsedDemographicId, Integer.valueOf(providerId), authenticatorId, selectedAffinityDomain, policies);

        }

        AffinityDomainDocuments documents = new AffinityDomainDocuments(selectedAffinityDomain.getId());

        if (request.getParameter("downloadDocuments") != null && request.getParameter("docsToDownload") != null) {
            try {
                for (String patientDocumentId : request.getParameterValues("docsToDownload")) {
                    PatientDocument downloadedDocument = SharingCenterUtil.downloadSharedDocument(Integer.parseInt(patientDocumentId), request.getParameter("demographic_no"), providerId);
                    model.getDownloadedDocumentNames().add(downloadedDocument.getTitle());
                }

            } catch (CommunicationsException e) {
            }
        }

        try {

            if (request.getParameter("folder") == null) {
                // No folder was selected, load root documents.
                documents.getDocuments().addAll(SharingCenterUtil.findAffinityDomainRootDocuments(parsedDemographicId, selectedAffinityDomain.getId(), providerId, purposeOfUse));
            } else {
                // Load the documents within the selected folder.
                documents.getDocuments().addAll(SharingCenterUtil.getFolderContents(request.getParameter("folder"), parsedDemographicId, selectedAffinityDomain.getId(), providerId, purposeOfUse));
            }

            model.getAffinityDomainSharedDocuments().add(documents);

            model.getFolders().addAll(SharingCenterUtil.findFoldersForAffinityDomain(parsedDemographicId, selectedAffinityDomain.getId(), purposeOfUse));
        } catch (CommunicationsException ex) {
            model.getErrors().add("Unable to connect to endpoint.");
            MiscUtils.getLogger().error("Unable to connect to endpoint.", ex);
        } catch (PixApplicationException ex) {
            model.getErrors().add("Unable to resolve the patient's identity.");
            MiscUtils.getLogger().error("Unable to resolve the patient's identity.", ex);
        }

        model.getUnconsentedPolicies().addAll(SharingCenterUtil.getAllUnconsentedPolicies(parsedDemographicId, selectedAffinityDomain.getId()));
        model.getElevatePolicies().addAll(SharingCenterUtil.getAllPolicies(selectedAffinityDomain.getId()));
        request.setAttribute("model", model);

        request.getRequestDispatcher("shared.jsp").forward(request, response);
        
        return null;
    }
}
