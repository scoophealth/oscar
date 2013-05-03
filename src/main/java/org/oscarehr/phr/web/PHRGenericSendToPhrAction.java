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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.web;

import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.CaseManagementEntryAction;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils;
import org.oscarehr.myoscar.client.ws_manager.MedicalDataManager;
import org.oscarehr.myoscar.client.ws_manager.MessageManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.myoscar_server.ws.MedicalDataType;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;

import oscar.dms.EDoc;
import oscar.dms.EDocFactory;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarPrevention.pageUtil.PreventionPrintPdf;

/**
 *
 * @author apavel
 */
public class PHRGenericSendToPhrAction extends DispatchAction {
    private static final Logger logger= MiscUtils.getLogger();
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


    public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

    	Integer demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
        String providerNo = (String) request.getSession().getAttribute("user");
        String module = request.getParameter("module");

        String labId = request.getParameter("labId");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        
        MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
        Long patientMyOscarUserId=MyOscarUtils.getMyOscarUserIdFromOscarDemographicId(myOscarLoggedInInfo, demographicNo);
        
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
       
        EDocFactory.Module docModule = EDocFactory.Module.demographic;
        String docModuleId = demographicNo.toString();

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

            String documentNo = EDocUtil.addDocumentSQL(newEDoc);

            EDoc eDoc = EDocUtil.getDoc(documentNo);

            //--- send to myoscar ---
			Document doc=XmlUtils.newDocument("BinaryDocument");
			XmlUtils.appendChildToRoot(doc, "Filename", eDoc.getFileName());
			XmlUtils.appendChildToRoot(doc, "FileDescription", eDoc.getDescription());
			XmlUtils.appendChildToRoot(doc, "MimeType", eDoc.getContentType());
			XmlUtils.appendChildToRoot(doc, "Data", eDoc.getFileBytes());
			String docAsString=XmlUtils.toString(doc, false);
			
			GregorianCalendar dateOfData=new GregorianCalendar();
			if (eDoc.getDateTimeStampAsDate()!=null) dateOfData.setTime(eDoc.getDateTimeStampAsDate());
			
			MedicalDataTransfer4 medicalDataTransfer=new MedicalDataTransfer4();
			medicalDataTransfer.setData(docAsString);
			medicalDataTransfer.setDateOfData(dateOfData);
			medicalDataTransfer.setMedicalDataType(MedicalDataType.BINARY_DOCUMENT.name());
			medicalDataTransfer.setObserverOfDataPersonId(myOscarLoggedInInfo.getLoggedInPersonId());

			LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
			medicalDataTransfer.setObserverOfDataPersonName(loggedInInfo.loggedInProvider.getFormattedName());
			medicalDataTransfer.setOriginalSourceId(loggedInInfo.currentFacility.getName()+":eDoc:"+eDoc.getDocId());
			medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);
						
			Long medicalDataId=MyOscarMedicalDataManagerUtils.addMedicalData(myOscarLoggedInInfo, medicalDataTransfer, "eDoc", eDoc.getDocId(), true, true);
			
			// log the send
			RemoteDataLogDao remoteDataLogDao=(RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
			RemoteDataLog remoteDataLog=new RemoteDataLog();
			remoteDataLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
			remoteDataLog.setDocumentId(MyOscarLoggedInInfo.getMyOscarServerBaseUrl(), "eDoc", eDoc.getDocId());
			remoteDataLog.setAction(RemoteDataLog.Action.SEND);
			remoteDataLog.setDocumentContents("id="+eDoc.getDocId()+", fileName="+eDoc.getFileName());
			remoteDataLogDao.persist(remoteDataLog);


			//--- send message ---
			MessageManager.sendMessage(myOscarLoggedInInfo, patientMyOscarUserId, subject, message);

			//--- send annotations ---
			MedicalDataManager.addMedicalDataAnnotation(myOscarLoggedInInfo, patientMyOscarUserId, medicalDataId, message);
			
			return mapping.findForward("loginPage");
            
        } catch (Exception e) {
            logger.error("Could not send document to PHR", e);
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
