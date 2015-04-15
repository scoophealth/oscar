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
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.marc.shic.core.DocumentContainerMetaData;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MappingCodeType;
import org.marc.shic.core.SubmissionType;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.sharingcenter.DocumentType;
import org.oscarehr.sharingcenter.SharingCenterUtil;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.dao.ExportedDocumentDao;
import org.oscarehr.sharingcenter.dao.PatientSharingNetworkDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.sharingcenter.model.ExportedDocument;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class DocumentExportServlet extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // clinic info
        ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        // patient
        int patientId = Integer.parseInt(request.getParameter("demographic_no"));

        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
        
        // authenticator
        int authenticatorId = Integer.parseInt(request.getParameter("authenticatorSelect"));

        // domain
        int networkId = Integer.parseInt(request.getParameter("network_id"));
        AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
        AffinityDomainDataObject network = null;
        network = affDao.getAffinityDomain(networkId);

        // results page
        String forward = String.format("exportresult.jsp?demographic_no=%s&domain=%s", patientId, networkId);

        // author
        int authorId = Integer.parseInt(request.getParameter("provider_no"));

        // is patient already sharing?
        PatientSharingNetworkDao patientSharingNetworkDao = SpringUtils.getBean(PatientSharingNetworkDao.class);

        boolean sharingEnabled = patientSharingNetworkDao.isSharingEnabled(network.getId(), patientId);
        if (sharingEnabled == false) {
            // no point in continuing if the patient isn't shared
            response.sendRedirect(String.format("exportresult.jsp?demographic_no=%s&patient=0", patientId));
            return null;
        }

        // Send BPPC documents based on consentGiven and policySelect[]
        String[] policiesArray = {};
        String bppcStatus = null;
        String consentGiven = request.getParameter("consentGiven");

        List<Integer> policies = new ArrayList<Integer>();

        if (consentGiven.equalsIgnoreCase("yes")) {

            // policies
            if (request.getParameterValues("policySelect").length > 0) {
                policiesArray = request.getParameterValues("policySelect");

                for (String policy : policiesArray) {
                    policies.add(Integer.valueOf(policy));
                }
            }

            // verify consent
            Boolean bppcResult = SharingCenterUtil.giveConsentAndAcknowledgement(patientId, authorId, authenticatorId, network, policies);

            // check status (true if sent, false if an error occurred, null if already consented)
            if (Boolean.TRUE.equals(bppcResult)) {
                bppcStatus = "1";
            } else if (Boolean.FALSE.equals(bppcResult)) {
                bppcStatus = "0";
            }
        }

        // get the documents
        String[] documentsArray = {};
        if (request.getParameterValues("documents").length > 0) {
            documentsArray = request.getParameterValues("documents");
        }

        // folders
        ExportedDocumentDao exportedDocumentDao = SpringUtils.getBean(ExportedDocumentDao.class);
        FolderMetaData folder = null;
        if (request.getParameter("folderSelect") != null) {
            // a folder was supplied
            if (request.getParameter("folderSelect").equalsIgnoreCase("new")) {
                if (!request.getParameter("newFolderTitle").isEmpty()) {
                    // create a new folder and add documents
                    folder = new FolderMetaData();
                    folder.setPatient(SharingCenterUtil.resolvePatient(patientId, network));
                    folder.setTitle(request.getParameter("newFolderTitle"));
                    folder.setDescription(request.getParameter("newFolderTitle"));
                    folder.setSourceId(clinicData.getSourceId());
                    folder.setAuthor(SharingCenterUtil.createAuthor(authorId));
                    folder.setCreationTime(new GregorianCalendar());
                    folder.setCodeList(SharingCenterUtil.generateCodeValue(MappingCodeType.FolderCodeList, network.getId(), request.getParameter("documentsType"), documentsArray[0]));
                    folder.setContentType(SharingCenterUtil.generateCodeValue(MappingCodeType.ContentTypeCode, network.getId(), request.getParameter("documentsType"), documentsArray[0]));
                }

            } else if (!request.getParameter("folderSelect").isEmpty()) {
                // add document(s) to an old folder
                folder = SharingCenterUtil.getFolder(request.getParameter("folderSelect"), network);
                folder.setSubmissionType(SubmissionType.EXISTING);
                folder.setSourceId(clinicData.getSourceId());
                folder.setPatient(SharingCenterUtil.resolvePatient(patientId, network));
                folder.setAuthor(SharingCenterUtil.createAuthor(authorId));
                folder.setCreationTime(new GregorianCalendar());
                folder.setCodeList(SharingCenterUtil.generateCodeValue(MappingCodeType.FolderCodeList, network.getId(), request.getParameter("documentsType"), documentsArray[0]));
                folder.setContentType(SharingCenterUtil.generateCodeValue(MappingCodeType.ContentTypeCode, network.getId(), request.getParameter("documentsType"), documentsArray[0]));
            }
        }

        boolean submissionResult = false;
        List<DocumentMetaData> documents = null;

        // create documents based on the type parameter
        if (request.getParameter("documentsType").equalsIgnoreCase("edocs")) {
            documents = SharingCenterUtil.createDocumentsFromEdocs(patientId, authorId, authenticatorId, network, documentsArray, policiesArray);

        } else if (request.getParameter("documentsType").equalsIgnoreCase("eforms")) {
            documents = SharingCenterUtil.createDocumentsFromEforms(patientId, authorId, authenticatorId, network, documentsArray, policiesArray);

        } else if (request.getParameter("documentsType").equalsIgnoreCase(DocumentType.CDS.name())) {
            documents = SharingCenterUtil.createDocumentsFromCDSExport(patientId, authorId, authenticatorId, network, documentsArray, policiesArray);

        } else if (request.getParameter("documentsType").equalsIgnoreCase(DocumentType.XPHR.name())) {
            documents = SharingCenterUtil.createDocumentsFromXPHR(patientId, authorId, authenticatorId, network, documentsArray, policiesArray);

        } else if (request.getParameter("documentsType").equalsIgnoreCase(DocumentType.NEXJ.name())) {
            documents = SharingCenterUtil.createDocumentsFromNEXJ(patientId, authorId, authenticatorId, network, documentsArray, policiesArray);
        }

        if (folder != null) {
            // folder submission
            folder.getDocuments().addAll(documents);

            // submit folder
            submissionResult = SharingCenterUtil.submitFolder(folder, network);

            // persist the XDS IDs of the submitted documents (if successful)
            if (submissionResult) {
                for (DocumentMetaData doc : folder.getDocuments()) {
                    ExportedDocument exported = new ExportedDocument();
                    exported.setAffinityDomain(network.getId());
                    exported.setDemographicNo(patientId);
                    exported.setDocumentType(request.getParameter("documentsType"));
                    exported.setLocalDocId(Integer.valueOf(documentsArray[folder.getDocuments().indexOf(doc)]));
                    exported.setDocumentUID(doc.getUniqueId());
                    exported.setDocumentUUID(doc.getId());
                    exportedDocumentDao.persist(exported);
                }
            }

        } else if (documents.size() > 1) {
            // document container submission
            DocumentContainerMetaData container = new DocumentContainerMetaData();
            container.setPatient(SharingCenterUtil.resolvePatient(patientId, network));
            container.setSourceId(clinicData.getOid());
            container.setAuthor(SharingCenterUtil.createAuthor(authorId));
            container.setCreationTime(new GregorianCalendar());
            container.setContentType(SharingCenterUtil.generateCodeValue(MappingCodeType.ContentTypeCode, network.getId(), request.getParameter("documentsType"), documentsArray[0]));
            container.addExtendedAttribute("authorInstitution", clinicData.getName());

            container.getDocuments().addAll(documents);

            // submit container
            submissionResult = SharingCenterUtil.submitDocumentContainer(container, network);

            // persist the XDS IDs of the submitted documents (if successful)
            if (submissionResult) {
                for (DocumentMetaData doc : container.getDocuments()) {
                    ExportedDocument exported = new ExportedDocument();
                    exported.setAffinityDomain(network.getId());
                    exported.setDemographicNo(patientId);
                    exported.setDocumentType(request.getParameter("documentsType"));
                    exported.setLocalDocId(Integer.valueOf(documentsArray[container.getDocuments().indexOf(doc)]));
                    exported.setDocumentUID(doc.getUniqueId());
                    exported.setDocumentUUID(doc.getId());
                    exportedDocumentDao.persist(exported);
                }
            }

        } else if (documents.size() == 1) {
            // single document submission
            DocumentMetaData document = documents.get(0);

            // submit document
            submissionResult = SharingCenterUtil.submitSingleDocument(document, network);

            // persist the XDS IDs of the submitted document (if successful)
            if (submissionResult) {
                ExportedDocument exported = new ExportedDocument();
                exported.setAffinityDomain(network.getId());
                exported.setDemographicNo(patientId);
                exported.setDocumentType(request.getParameter("documentsType"));
                exported.setLocalDocId(Integer.valueOf(documentsArray[0]));
                exported.setDocumentUID(document.getUniqueId());
                exported.setDocumentUUID(document.getId());
                exportedDocumentDao.persist(exported);
            }
        }

        String submissionStatus = (submissionResult) ? "1" : "0";

        if (bppcStatus != null) {
            forward += "&bppc=" + bppcStatus;
        }

        if (submissionStatus != null) {
            forward += "&submission=" + submissionStatus;
        }

        response.sendRedirect(forward);
        
        return null;
    }

}
