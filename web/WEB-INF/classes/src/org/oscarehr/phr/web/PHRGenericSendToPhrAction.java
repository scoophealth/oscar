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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.CaseManagementEntryAction;
import org.oscarehr.phr.model.PHRBinaryData;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.SpringUtils;
import oscar.dms.EDoc;
import oscar.dms.EDocFactory;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarPrevention.pageUtil.PreventionPrintPdf;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author apavel
 */
public class PHRGenericSendToPhrAction extends DispatchAction {
    private static Log _log = LogFactory.getLog(PHRGenericSendToPhrAction.class);
    private PHRService phrService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       if (request.getParameter("SendToPhrPreview") == null) return mapping.findForward("inputJsp");
       return super.execute(mapping, form, request, response);
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
        return send(mapping,form,request,response);
    }

    public ActionForward documentPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String labId = request.getParameter("labId");
        String module = request.getParameter("module");
        String providerNo = (String) request.getSession().getAttribute("user");

        response.setContentType("application/pdf");  //octet-stream
        response.setHeader("Content-Disposition", "attachment; filename=\"pdfPreview.pdf\"");

        if (labId != null) {
            request.setAttribute("segmentID", labId);
            request.setAttribute("providerNo", providerNo);
            LabPDFCreator labPdfCreator = new LabPDFCreator(request, response.getOutputStream());
            labPdfCreator.printPdf();
            return null;
        }
        if (module != null && module.equals("prevention")) {
            PreventionPrintPdf preventionPrintPdf = new PreventionPrintPdf();
            String[] headerIds = request.getParameterValues("printHP");
            preventionPrintPdf.printPdf(headerIds, request, response.getOutputStream());
        }
        if (module != null && module.equals("echart")) {
            CaseManagementEntryAction action = new CaseManagementEntryAction();
            action.setCaseManagementManager((CaseManagementManager) SpringUtils.getBean("caseManagementManager"));
            action.setProviderManager((ProviderManager) SpringUtils.getBean("providerManager"));
            action.doPrint(request, response.getOutputStream());
        }
        return null;
    }


    public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String demographicNo = request.getParameter("demographic_no");
        String providerNo = (String) request.getSession().getAttribute("user");
        String module = request.getParameter("module");

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
        String fileName = "phrdocument";
        String contentType = "application/pdf";
        String html = null;
        String creatorId = providerNo;
        String responsibleId = "";
        String source = null;
        EDocFactory.Status status = EDocFactory.Status.SENT;
        Date observationDate = new Date();
        String reviewerId = null;
        String reviewDateTime = null;
        EDocFactory.Module docModule = EDocFactory.Module.demographic;
        String docModuleId = demographicNo;

        EDoc newEDoc = null; //get nullpointer if something goes wrong
        try {
            if (labId != null) {
                request.setAttribute("segmentID", labId);

                type = "lab";
                fileName =  "labtophr";
                description = subject;
                newEDoc = eDocFactory.createEDoc(description, type, fileName, contentType, html, creatorId, responsibleId, source, status, observationDate, reviewerId, observationDate, docModule, docModuleId);
                OutputStream os = newEDoc.getFileOutputStream();
                LabPDFCreator labPDFCreator = new LabPDFCreator(request, os);
                labPDFCreator.printPdf();
            }
            else if (module != null && module.equals("prevention")) {
                String[] headerIds = request.getParameterValues("printHP");

                type = "others";
                fileName =  "immunizationtophr";
                description = subject;
                newEDoc = eDocFactory.createEDoc(description, type, fileName, contentType, html, creatorId, responsibleId, source, status, observationDate, reviewerId, observationDate, docModule, docModuleId);

                OutputStream os = newEDoc.getFileOutputStream();
                PreventionPrintPdf preventionPrintPdf = new PreventionPrintPdf();
                preventionPrintPdf.printPdf(headerIds, request, os);
            } else if (module != null && module.equals("echart")) {
                type = "others";
                fileName =  "echarttophr";
                description = subject;
                newEDoc = eDocFactory.createEDoc(description, type, fileName, contentType, html, creatorId, responsibleId, source, status, observationDate, reviewerId, observationDate, docModule, docModuleId);

                OutputStream os = newEDoc.getFileOutputStream();

                CaseManagementEntryAction action = new CaseManagementEntryAction();
                action.setCaseManagementManager((CaseManagementManager) SpringUtils.getBean("caseManagementManager"));
                action.setProviderManager((ProviderManager) SpringUtils.getBean("providerManager"));
                action.doPrint(request, os);
            } else {
                response.getWriter().append("object ID is unrecognized or is not set");
                return null;
            }
            request.setAttribute("providerNo", providerNo);

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
            
        } catch (Exception e) {
            _log.error("Could not send document to PHR", e);
            request.setAttribute("error_msg", "Error: " + e.getMessage());
            return mapping.findForward("loginPage");
        }
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
