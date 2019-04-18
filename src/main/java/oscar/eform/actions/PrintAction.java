/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package oscar.eform.actions;

import java.io.File;
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
import org.oscarehr.common.dao.EFormDataDao;
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
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ImagePDFCreator;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

public class PrintAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();

	private String localUri = null;
	
//	private boolean skipSave = false;
	
	private HttpServletResponse response;
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
		
		localUri = getEformRequestUrl(request);
		this.response = response;
		String id  = (String)request.getAttribute("fdid");
		String providerId = request.getParameter("providerId");
	//	skipSave = "true".equals(request.getParameter("skipSave"));
		try {
			printForm(request, LoggedInInfo.getLoggedInInfoFromSession(request), id, providerId);
		} catch (Exception e) {
			MiscUtils.getLogger().error("",e);
			return mapping.findForward("error");
		}
		return mapping.findForward("success");
	}
	
	/**
	 * This method is a copy of Apache Tomcat's ApplicationHttpRequest getRequestURL method with the exception that the uri is removed and replaced with our eform viewing uri. Note that this requires that the remote url is valid for local access. i.e. the
	 * host name from outside needs to resolve inside as well. The result needs to look something like this : https://127.0.0.1:8443/oscar/eformViewForPdfGenerationServlet?fdid=2&parentAjaxId=eforms
	 */
    public static String getEformRequestUrl(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		Integer port;
		try { port = new Integer(OscarProperties.getInstance().getProperty("oscar_port")); }
	    catch (Exception e) { port = 8443; }
		if (port < 0) port = 80; // Work around java.net.URL bug

		url.append(scheme);
		url.append("://");
		//url.append(request.getServerName());
		url.append("127.0.0.1");
		
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		url.append("/EFormViewForPdfGenerationServlet?parentAjaxId=eforms&providerId=");
		url.append(request.getParameter("providerId"));
		url.append("&fdid=");

		return (url.toString());
	}

	/**
	 * This method will take eforms and send them to a PHR.
	 */
	public void printForm(HttpServletRequest request, LoggedInInfo loggedInInfo, String formId, String providerId) {
		
		File tempFile = null;

		byte[] buffer;
		ByteInputStream bis;
		ByteOutputStream bos;
		ArrayList<InputStream> streams = new ArrayList<InputStream>();
		ArrayList<Object> alist = new ArrayList<Object>();
		
		EFormDataDao efmDataDao = SpringUtils.getBean(EFormDataDao.class);
		EFormData eformData = efmDataDao.find(Integer.parseInt(formId));
		ArrayList<EDoc> docs = EDocUtil.listDocsAttachedToEForm(loggedInInfo, String.valueOf(eformData.getDemographicId()), formId, EDocUtil.ATTACHED);
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<LabResultData> labs = consultLabs.populateLabResultsDataEForm(loggedInInfo, String.valueOf(eformData.getDemographicId()), formId, CommonLabResultData.ATTACHED);
		HRMDocumentToDemographicDao hrmDocumentToDemographicDao = SpringUtils.getBean(HRMDocumentToDemographicDao.class);
		List<HRMDocumentToDemographic> attachedHRMReports = hrmDocumentToDemographicDao.findHRMDocumentsAttachedToEForm(formId);
		
		try {
			
			bos = new ByteOutputStream();
			//ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(request,bos);
			//cpdfc.printPdf(loggedInInfo);
			
			buffer = WKHtmlToPdfUtils.convertToPdf(localUri + formId);
			 bis = new ByteInputStream(buffer, buffer.length);
             streams.add(bis);
             alist.add(bis);
			
			
			for (int i = 0; i < docs.size(); i++) {
				EDoc doc = docs.get(i);  
				if (doc.isPrintable()) {
					if (doc.isImage()) {
						bos = new ByteOutputStream();
						request.setAttribute("imagePath", path + doc.getFileName());
						request.setAttribute("imageTitle", doc.getDescription());
						ImagePDFCreator ipdfc = new ImagePDFCreator(request, bos);
						ipdfc.printPdf();
						
						buffer = bos.getBytes();
						bis = new ByteInputStream(buffer, bos.getCount());
						bos.close();
						streams.add(bis);
						alist.add(bis);
						
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
				bos = new ByteOutputStream();
				request.setAttribute("segmentID", labs.get(i).segmentID);
				LabPDFCreator lpdfc = new LabPDFCreator(request, bos);
				lpdfc.printPdf();

				// Transferring PDF to an input stream to be concatenated with
				// the rest of the documents.
				buffer = bos.getBytes();
				bis = new ByteInputStream(buffer, bos.getCount());
				bos.close();
				streams.add(bis);
				alist.add(bis);

			}

			for (HRMDocumentToDemographic attachedHRM : attachedHRMReports) {
				bos = new ByteOutputStream();
				HRMPDFCreator hrmPdfCreator = new HRMPDFCreator(bos, attachedHRM.getHrmDocumentId(), loggedInInfo);
				hrmPdfCreator.printPdf();

				buffer = bos.getBytes();
				bis = new ByteInputStream(buffer, bos.getCount());
				bos.close();
				streams.add(bis);
				alist.add(bis);
			}
			
			List<EFormData> eForms = EFormUtil.listPatientEformsCurrentAttachedToEForm(formId);
            for (EFormData eForm : eForms) {
                String localUri = PrintAction.getEformRequestUrl(request);
                buffer = WKHtmlToPdfUtils.convertToPdf(localUri + eForm.getId());
                bis = new ByteInputStream(buffer, buffer.length);
                streams.add(bis);
                alist.add(bis);
            }
            
			if (alist.size() > 0) {
				
				bos = new ByteOutputStream();
				ConcatPDF.concat(alist, bos);
				response.setContentType("application/pdf"); // octet-stream
				response.setHeader(
						"Content-Disposition",
						"inline; filename=\"combinedPDF-"
								+ UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
								+ ".pdf\"");
				response.getOutputStream().write(bos.getBytes(), 0, bos.getCount());
			}
			
			/*
			
			logger.info("Generating PDF for eform with fdid = " + formId);

			tempFile = File.createTempFile("EFormPrint." + formId, ".pdf");
			//tempFile.deleteOnExit();

			// convert to PDF
			String viewUri = localUri + formId;
			WKHtmlToPdfUtils.convertToPdf(viewUri, tempFile);
			logger.info("Writing pdf to : "+tempFile.getCanonicalPath());
			
			
			InputStream is = new BufferedInputStream(new FileInputStream(tempFile));
			ByteOutputStream bos = new ByteOutputStream();
			byte buffer[] = new byte[1024];
			int read;
			while (is.available() != 0) {
				read = is.read(buffer,0,1024);
				bos.write(buffer,0, read);
			}
			
			bos.flush();
			// byte[] pdf = HtmlToPdfServlet.appendFooter(bos.getBytes());
			byte[] pdf;
            try {
	            pdf = HtmlToPdfServlet.stamp(bos.getBytes());
            } catch (Exception e) {
            	throw new RuntimeException(e);
            }
			
			//while (fos.read() != -1)
			response.setContentType("application/pdf");  //octet-stream
            response.setHeader("Content-Disposition", "attachment; filename=\"EForm-"
            				+ formId + "-"
							+ UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
							+ ".pdf\"");
			// response.getOutputStream().write(bos.getBytes(), 0, bos.getCount());
            HtmlToPdfServlet.stream(response, pdf, false);
            // response.getOutputStream().write(pdf);
			
			// Removing the consulation pdf.
			tempFile.delete();	
			
			// Removing the eform
			if (skipSave) {
	        	 EFormDataDao eFormDataDao=(EFormDataDao) SpringUtils.getBean("EFormDataDao");
	        	 EFormData eFormData=eFormDataDao.find(Integer.parseInt(formId));
	        	 eFormData.setCurrent(false);
	        	 eFormDataDao.merge(eFormData);
			}
			*/
		} catch (DocumentException e) {
			//logger.error("Error converting and sending eform. id=" + eFormId, e);
			MiscUtils.getLogger().error("",e);
		} catch (IOException e) {
			//logger.error("Error converting and sending eform. id=" + eFormId, e);
			MiscUtils.getLogger().error("",e);
		}
	}

	
}
