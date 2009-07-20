/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.web;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.phr.model.PHRBinaryData;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;
import oscar.dms.EDoc;
import oscar.dms.EDocFactory;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author apavel
 */
public class PHRGenericSendToPhrAction extends Action {
    private static Log _log = LogFactory.getLog(PHRGenericSendToPhrAction.class);
    private PHRService phrService;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String demographicNo = request.getParameter("demographic_no");
        String providerNo = (String) request.getSession().getAttribute("user");
        String labId = request.getParameter("labId");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        String errorMessage = null;
        DemographicData demographicData = new DemographicData();
        String recipientPhrId = demographicData.getDemographic(demographicNo).getIndivoId();
        int recipientType = PHRDocument.TYPE_DEMOGRAPHIC;

        EDocFactory eDocFactory = new EDocFactory();
        String description = "";
        String type = "document";
        String fileName = "document";
        String contentType = "application/pdf";
        String html = null;
        String creatorId = providerNo;
        String responsibleId = "";
        String source = null;
        EDocFactory.Status status = EDocFactory.Status.SENT;
        Date observationDate = new Date();
        String reviewerId = null;
        String reviewDateTime = null;
        EDocFactory.Module module = EDocFactory.Module.demographic;
        String moduleId = demographicNo;
        
        try {
            if (labId != null) {
                request.setAttribute("segmentID", labId);
                request.setAttribute("providerNo", providerNo);

                type = "lab";
                fileName =  "labtophr";
                description = subject;

                EDoc newEDoc = eDocFactory.createEDoc(description, type, fileName, contentType, html, creatorId, responsibleId, source, status, observationDate, reviewerId, observationDate, module, moduleId);
                OutputStream os = newEDoc.getFileOutputStream();
                LabPDFCreator labPDFCreator = new LabPDFCreator(request, os);
                labPDFCreator.printPdf();
                EDocUtil eDocUtil = new EDocUtil();

                String documentNo = eDocUtil.addDocumentSQL(newEDoc);

                ProviderData senderProviderData = new ProviderData(providerNo);

                EDoc eDoc = EDocUtil.getDoc(documentNo);
                PHRBinaryData phrBinaryData = new PHRBinaryData();
                String actionIndex = phrService.sendAddBinaryData(senderProviderData, demographicNo, recipientType, recipientPhrId, eDoc) + "";

                ArrayList<String> eDocAttachmentActionId = new ArrayList();
                eDocAttachmentActionId.add(actionIndex);
                phrService.sendAddMessage(subject, null, message, senderProviderData, demographicNo, recipientType, recipientPhrId, eDocAttachmentActionId);
                
                
                phrService.sendAddAnnotation(senderProviderData, demographicNo, recipientPhrId, actionIndex, message);
                return mapping.findForward("loginPage");
            }
        } catch (Exception e) {
            _log.error("Could not send document to PHR", e);
            request.setAttribute("error_msg", "Error: " + e.getMessage());
            return mapping.findForward("loginPage");
        }
        
        
        response.getWriter().append("object ID is unrecognized or is not set");
        return null;
    }

    /**
     * @return the phrService
     */
    public PHRService getPhrService() {
        return phrService;
    }

    /**
     * @param phrService the phrService to set
     */
    public void setPhrService(PHRService phrService) {
        this.phrService = phrService;
    }

}
