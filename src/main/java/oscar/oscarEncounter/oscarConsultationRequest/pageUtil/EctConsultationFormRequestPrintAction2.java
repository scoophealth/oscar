/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

/*
 * EctConsultationFormRequestPrintAction.java
 *
 * Created on November 19, 2007, 4:05 PM
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.hospitalReportManager.HRMPDFCreator;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

import com.lowagie.text.DocumentException;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.eform.EFormUtil;
import oscar.eform.actions.PrintAction;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.all.pageUtil.OLISLabPDFCreator;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

/**
 *
 * Convert submitted preventions into pdf and return file
 */
public class EctConsultationFormRequestPrintAction2 extends Action {
    
    private static final Logger logger = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public EctConsultationFormRequestPrintAction2() {
    }
    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
    	String reqId = (String) request.getAttribute("reqId");
    	if (request.getParameter("reqId")!=null) reqId = request.getParameter("reqId");
    	
		String demoNo = request.getParameter("demographicNo");
		ArrayList<EDoc> docs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		ArrayList<Object> alist = new ArrayList<Object>();
		
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<InputStream> streams = new ArrayList<InputStream>();
		ArrayList<File> filesToDelete = new ArrayList<File>();

		ArrayList<LabResultData> labs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		HRMDocumentToDemographicDao hrmDocumentToDemographicDao = SpringUtils.getBean(HRMDocumentToDemographicDao.class);
		List<HRMDocumentToDemographic> attachedHRMReports = hrmDocumentToDemographicDao.findHRMDocumentsAttachedToConsultation(reqId);
		String error = "";
		Exception exception = null;
		try {

			File f = File.createTempFile("consult"+reqId,"pdf");
	        FileOutputStream fos = new FileOutputStream(f);
			
			ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(request,fos);
			cpdfc.printPdf(loggedInInfo);
			
			fos.close();
			
			FileInputStream fis = new FileInputStream(f);
			alist.add(fis);
			streams.add(fis);
			filesToDelete.add(f);
			
			for (int i = 0; i < docs.size(); i++) {
				EDoc doc = docs.get(i);  
				if (doc.isPrintable()) {
					if (doc.isImage()) {
						File f2 = File.createTempFile("image"+doc.getDocId(),"pdf");
						FileOutputStream fos2 = new FileOutputStream(f2);
						
						request.setAttribute("imagePath", path + doc.getFileName());
						request.setAttribute("imageTitle", doc.getDescription());

						ImagePDFCreator ipdfc = new ImagePDFCreator(request, fos2);
						ipdfc.printPdf();
						
						fos2.close();
						
						FileInputStream fis2 = new FileInputStream(f2);
						alist.add(fis2);
						streams.add(fis2);
						filesToDelete.add(f2);
					}
					else if (doc.isPDF()) {
						alist.add(path + doc.getFileName());
					}
					else {
						logger.error("EctConsultationFormRequestPrintAction: " + doc.getType() + " is marked as printable but no means have been established to print it.");	
					}
				}
			}

			
			// Iterating over requested labs.
			for (int i = 0; labs != null && i < labs.size(); i++) {
				// Storing the lab in PDF format inside a byte stream.
				request.setAttribute("segmentID", labs.get(i).segmentID);

				request.setAttribute("providerNo",LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
				File f2 = File.createTempFile("lab"+request.getAttribute("segmentID"),"pdf");

				FileOutputStream fos2 = new FileOutputStream(f2);
				FileInputStream fis2 = null;

				MessageHandler messageHandler = Factory.getHandler(labs.get(i).getSegmentID()); 
				if (messageHandler instanceof OLISHL7Handler){
					//If the lab is HL7, use the OLISLabPDFCreator to print the lab
					OLISLabPDFCreator olisLabPdfCreator = new OLISLabPDFCreator(request, fos2);
					olisLabPdfCreator.printPdf();
					fis2 = new FileInputStream(f2);
				}
				else {
					File f3 = File.createTempFile("lab"+request.getAttribute("segmentID") + "-complete","pdf");
					FileOutputStream fos3 = new FileOutputStream(f3);

					//If it isn't HL7, use the normal LabPDFCreator to print
					LabPDFCreator lpdfc = new LabPDFCreator(request, fos2);
					lpdfc.printPdf();
					lpdfc.addEmbeddedDocuments(f2,fos3);
					fos3.close();
					fis2 = new FileInputStream(f3);

					filesToDelete.add(f3);
				}

				fos2.close();
				alist.add(fis2);
				streams.add(fis2);

				filesToDelete.add(f2);
			}

			for (HRMDocumentToDemographic attachedHRM : attachedHRMReports) {
				
				File f2 = File.createTempFile("hrm"+attachedHRM.getHrmDocumentId(),"pdf");
				FileOutputStream fos2 = new FileOutputStream(f2);
			
				HRMPDFCreator hrmPdfCreator = new HRMPDFCreator(fos2, attachedHRM.getHrmDocumentId(), loggedInInfo);
				hrmPdfCreator.printPdf();

				fos2.close();
				
				FileInputStream fis2 = new FileInputStream(f2);

				alist.add(fis2);
				streams.add(fis2);
				filesToDelete.add(f2);
				
			}
			
            //Get attached eForms
            List<EFormData> eForms = EFormUtil.listPatientEformsCurrentAttachedToConsult(reqId);
            for (EFormData eForm : eForms) {
                String localUri = PrintAction.getEformRequestUrl(request);
                byte[] buffer = WKHtmlToPdfUtils.convertToPdf(localUri + eForm.getId());
                File f2 = File.createTempFile("eform-" + eForm.getFormId(),"pdf");
				FileOutputStream fos2 = new FileOutputStream(f2);
				fos2.write(buffer);
				fos2.close();
				
				FileInputStream fis2 = new FileInputStream(f2);

				alist.add(fis2);
				streams.add(fis2);
				filesToDelete.add(f2);
				
            }
            
			if (alist.size() > 0) {
				response.setContentType("application/pdf"); // octet-stream
				response.setHeader(
						"Content-Disposition",
						"inline; filename=\"combinedPDF-"
								+ UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
								+ ".pdf\"");
				
				ConcatPDF.concat(alist, response.getOutputStream());
				
			}

		} catch (DocumentException de) {
			error = "DocumentException";
			exception = de;
		} catch (IOException ioe) {
			error = "IOException";
			exception = ioe;
		} finally { 
			// Cleaning up InputStreams created for concatenation.
			
			for (InputStream is : streams) {
				try {
					is.close();
				} catch (IOException e) {
					error = "IOException";
				}
			}
			
			for(File f:filesToDelete) {
				f.delete();
			}
		}
		if (!error.equals("")) {
			logger.error(error + " occurred inside ConsultationPrintAction", exception);
			request.setAttribute("printError", exception.getMessage());
			request.setAttribute("de", demoNo);
			return mapping.findForward("error");
		}
		return null;
		
    }
}
