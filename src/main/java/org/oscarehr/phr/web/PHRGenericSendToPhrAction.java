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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.casemgmt.service.CaseManagementPrint;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils;
import org.oscarehr.myoscar.client.ws_manager.MedicalDataManager;
import org.oscarehr.myoscar.client.ws_manager.MessageManager;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
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
        	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        	String demono = request.getParameter("demographicNo");
        	if (demono == null)  demono = request.getParameter("demographic_no");
    		if (demono == null || "".equals(demono)) {
    			demono = (String) request.getAttribute("casemgmt_DemoNo");
    		}
			Integer demographicNo = Integer.parseInt(demono);
			String ids = request.getParameter("notes2print");
			boolean printAllNotes = "ALL_NOTES".equals(ids);
			String[] noteIds;
			if (ids.length() > 0) {
				noteIds = ids.split(",");
			} else {
				noteIds = new String[] {};
			}
			boolean printCPP  = request.getParameter("printCPP") != null && request.getParameter("printCPP").equalsIgnoreCase("true");
			boolean printRx   = request.getParameter("printRx") != null && request.getParameter("printRx").equalsIgnoreCase("true");
			boolean printLabs = request.getParameter("printLabs") != null && request.getParameter("printLabs").equalsIgnoreCase("true");		
		
			CaseManagementPrint cmp = new CaseManagementPrint();
			cmp.doPrint(loggedInInfo,demographicNo, printAllNotes,noteIds,printCPP,printRx,printLabs,null,null,request, response.getOutputStream());
        	
        }
        return null;
    }


    public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	String providerNo=loggedInInfo.getLoggedInProviderNo();

    	Integer demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
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
        String documentNo = null;
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

    			String ids = request.getParameter("notes2print");
    			boolean printAllNotes = "ALL_NOTES".equals(ids);
    			String[] noteIds;
    			if (ids.length() > 0) {
    				noteIds = ids.split(",");
    			} else {
    				noteIds = new String[] {};
    			}
    			boolean printCPP  = request.getParameter("printCPP") != null && request.getParameter("printCPP").equalsIgnoreCase("true");
    			boolean printRx   = request.getParameter("printRx") != null && request.getParameter("printRx").equalsIgnoreCase("true");
    			boolean printLabs = request.getParameter("printLabs") != null && request.getParameter("printLabs").equalsIgnoreCase("true");
    			Calendar startDate = null;
    			try{
    				startDate = Calendar.getInstance();
    				startDate.setTimeInMillis(Long.parseLong(request.getParameter("startDate")));
    			}catch(Exception e){
    				startDate = null;
    			}
    			
    			Calendar endDate = null;
    			try{
    				endDate = Calendar.getInstance();
    				endDate.setTimeInMillis(Long.parseLong(request.getParameter("endDate")));
    			}catch(Exception e){
    				endDate = null;
    			}
    		
    			CaseManagementPrint cmp = new CaseManagementPrint();
    			cmp.doPrint(loggedInInfo,demographicNo, printAllNotes,noteIds,printCPP,printRx,printLabs,startDate,endDate,request, os);
            	
            } else if (module != null && module.equals("document")) {
            	documentNo =  request.getParameter("documentNo");
            } else {
                response.getWriter().append("object ID is unrecognized or is not set");
                return null;
            }
            request.setAttribute("providerNo", providerNo);
            
            if(newEDoc != null){ // This should be null except for when it's a document
            	documentNo = EDocUtil.addDocumentSQL(newEDoc);
            }
            EDoc eDoc = EDocUtil.getDoc(documentNo);
            
            if (module != null && module.equals("document")) {
            	eDoc.setDescription(subject);
            }

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

			medicalDataTransfer.setObserverOfDataPersonName(loggedInInfo.getLoggedInProvider().getFormattedName());
			medicalDataTransfer.setOriginalSourceId(loggedInInfo.getCurrentFacility().getName()+":eDoc:"+eDoc.getDocId());
			medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);
						
			Long medicalDataId=MyOscarMedicalDataManagerUtils.addMedicalData(providerNo, myOscarLoggedInInfo, medicalDataTransfer, "eDoc", eDoc.getDocId(), true, true);
			
			// log the send
			RemoteDataLogDao remoteDataLogDao=(RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
			RemoteDataLog remoteDataLog=new RemoteDataLog();
			remoteDataLog.setProviderNo(providerNo);
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
