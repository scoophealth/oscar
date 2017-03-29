/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tika.io.IOUtils;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.fax.util.PdfCoverPageCreator;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;

import com.itextpdf.text.pdf.PdfReader;
import com.lowagie.text.DocumentException;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

public class EctConsultationFormFaxAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public EctConsultationFormFaxAction() {
	}
	    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
    	String reqId = request.getParameter("reqId");
		String demoNo = request.getParameter("demographicNo");
		String faxNumber = request.getParameter("letterheadFax");
		String consultResponsePage = request.getParameter("consultResponsePage");
		boolean doCoverPage = request.getParameter("coverpage").equalsIgnoreCase("true");
		
		ArrayList<EDoc> docs;
		if (consultResponsePage==null) {
			docs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		} else {
			docs = EDocUtil.listResponseDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		}
		
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		ArrayList<Object> alist = new ArrayList<Object>();
		byte[] buffer;
		ByteInputStream bis;
		ByteOutputStream bos;
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<InputStream> streams = new ArrayList<InputStream>();
		String provider_no = loggedInInfo.getLoggedInProviderNo();		

		ArrayList<LabResultData> labs;
		if (consultResponsePage==null) {
			labs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		} else {
			labs = consultLabs.populateLabResultsDataConsultResponse(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		}
		
		String error = "";
		Exception exception = null;
		try {
			
			if( doCoverPage ) {
				String note = request.getParameter("note") == null ? "" : request.getParameter("note");
				
				PdfCoverPageCreator pdfCoverPageCreator = new PdfCoverPageCreator(note);
				
				buffer = pdfCoverPageCreator.createCoverPage();
				bis = new ByteInputStream(buffer, buffer.length);
				streams.add(bis);
				alist.add(bis);
				
			}

			if (consultResponsePage==null) { //fax for consultation request
				bos = new ByteOutputStream();
				ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(request, bos);
				cpdfc.printPdf(loggedInInfo);
				
				buffer = bos.getBytes();
				bis = new ByteInputStream(buffer, bos.getCount());
				bos.close();
				streams.add(bis);
				alist.add(bis);
			}
			else { //fax for consultation response
				String consultRespoonsePDF = ConsultResponsePDFCreator.create(consultResponsePage);
				alist.add(consultRespoonsePDF);
			}

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
			
			if (alist.size() > 0) {
				
				String referralFax = request.getParameter("fax");
				
				// Retrieving additional fax recipients.
				String[] tmpRecipients = request.getParameterValues("faxRecipients");
				
				// Removing all non digit characters from fax numbers.
				for (int i = 0; tmpRecipients != null && i < tmpRecipients.length; i++) { 
					tmpRecipients[i] = tmpRecipients[i].trim().replaceAll("\\D", "");
				}
				ArrayList<String> recipients = tmpRecipients == null ? new ArrayList<String>() : new ArrayList<String>(Arrays.asList(tmpRecipients));
				
				// Including consultant fax number if appropriate.
				if (referralFax != null && !referralFax.equals("")) {
					recipients.add(referralFax.trim().replaceAll("\\D", ""));					
				}
				
				// Removing duplicate phone numbers.
				recipients = new ArrayList<String>(new HashSet<String>(recipients));
				
				// Writing consultation request to disk as a pdf.
				String faxPath = path;
				String filename = "Consult_" + reqId + System.currentTimeMillis() + ".pdf";
				String faxPdf = String.format("%s%s%s", faxPath, File.separator, filename);
				
				FileOutputStream fos = null;
				
				try {
					fos = new FileOutputStream(faxPdf);		
					ConcatPDF.concat(alist, fos);
				} finally {
					IOUtils.closeQuietly(fos);
				}
				
				String tempPath = OscarProperties.getInstance().getProperty(
					"fax_file_location", System.getProperty("java.io.tmpdir"));
                String faxClinicId = OscarProperties.getInstance().getProperty("fax_clinic_id","");
				
				PdfReader pdfReader = new PdfReader(faxPdf);
				int numPages = pdfReader.getNumberOfPages();
				pdfReader.close();
				
				FaxJob faxJob = null;
				FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);				
				FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
				
				List<FaxConfig> faxConfigs = faxConfigDao.findAll(null, null);
				boolean validFaxNumber;
				
				for (int i = 0; i < recipients.size(); i++) {					
				    String faxNo = recipients.get(i).replaceAll("\\D", "");
				    if (faxNo.length() < 7) { throw new DocumentException("Document target fax number '"+faxNo+"' is invalid."); }
				    String tempName = "CRF-" + faxClinicId + reqId + "." + System.currentTimeMillis();
					
					String tempPdf = String.format("%s%s%s.pdf", tempPath, File.separator, tempName);
					String tempTxt = String.format("%s%s%s.txt", tempPath, File.separator, tempName);
					
					// Copying the fax pdf.
					FileUtils.copyFile(new File(faxPdf), new File(tempPdf));
					
					// Creating text file with the specialists fax number.
					PrintWriter pw = null;
					
					try {
						fos = new FileOutputStream(tempTxt);
						pw = new PrintWriter(fos);
						pw.println(faxNo);
					} finally {
						IOUtils.closeQuietly(pw);
						IOUtils.closeQuietly(fos);
					}

					// A little sanity check to ensure both files exist.
					if (!new File(tempPdf).exists() || !new File(tempTxt).exists()) {
						throw new DocumentException("Unable to create files for fax of consultation request " + reqId + ".");
					}
				    validFaxNumber = false;
				    
				    faxJob = new FaxJob();
		    		faxJob.setDestination(faxNo);
		    		faxJob.setFile_name(filename);
		    		faxJob.setNumPages(numPages);
		    		faxJob.setFax_line(faxNumber);
		    		faxJob.setStamp(new Date());
		    		faxJob.setOscarUser(provider_no);
		    		faxJob.setDemographicNo(Integer.parseInt(demoNo));
				    
				    for( FaxConfig faxConfig : faxConfigs ) {
				    	
				    	if( faxConfig.getFaxNumber().equals(faxNumber) ) {
				    						    		
				    		faxJob.setStatus(FaxJob.STATUS.SENT);
				    		faxJob.setUser(faxConfig.getFaxUser());
				    
				    		validFaxNumber = true;
				    		break;
				    	}
				    }
				    
				    if( !validFaxNumber ) {
				    	
				    	faxJob.setStatus(FaxJob.STATUS.ERROR);
				    	logger.error("PROBLEM CREATING FAX JOB", new DocumentException("Document outgoing fax number '"+faxNumber+"' is invalid."));
				    }
				    else {
				    	
				    	faxJob.setStatus(FaxJob.STATUS.SENT);
				    }
				    				    
				    faxJobDao.persist(faxJob);
				}

				LogAction.addLog(provider_no, LogConst.SENT, LogConst.CON_FAX, "CONSULT "+ reqId);
				request.setAttribute("faxSuccessful", true);
				return mapping.findForward("success");

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
		}
		if (!error.equals("")) {
			logger.error(error + " occured insided ConsultationPrintAction", exception);
			request.setAttribute("printError", new Boolean(true));
			return mapping.findForward("error");
		}
		return null;		
    }   
}
