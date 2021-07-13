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


import java.io.*;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import ca.uvic.leadlab.obibconnector.facades.datatypes.AttachmentType;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.parser.RtfParser;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.integration.cdx.dao.CdxAttachmentDao;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.integration.cdx.model.CdxAttachment;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.DocumentException;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 *
 * Convert submitted preventions into pdf and return file
 */
public class EctConsultationFormRequestPrintAction2 extends Action {
    
    private static final Logger logger = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private String contentRoute;
    
    public EctConsultationFormRequestPrintAction2() {
    }
    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		contentRoute = request.getSession().getServletContext().getRealPath("/");

    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
    	String reqId = (String) request.getAttribute("reqId");
    	if (request.getParameter("reqId")!=null) reqId = request.getParameter("reqId");
    	
		String demoNo = request.getParameter("demographicNo");
		ArrayList<EDoc> docs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		ArrayList<Object> alist = new ArrayList<Object>();
		byte[] buffer;
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<InputStream> streams = new ArrayList<InputStream>();
		ArrayList<File> filesToDelete = new ArrayList<File>();

		ArrayList<LabResultData> labs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		String error = "";
		Exception exception = null;
		try {

			File f = File.createTempFile("consult"+reqId,"pdf");
	        FileOutputStream fos = new FileOutputStream(f);
			
			ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(request,fos);
			cpdfc.printPdf(loggedInInfo);
			
			fos.close();
			
			FileInputStream fis = new FileInputStream(f);
			ByteInputStream bis;
			ByteOutputStream bos = null;

			alist.add(fis);
			streams.add(fis);
			filesToDelete.add(f);
			boolean success = false;
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
					else if (doc.isCDX()) {
						success = false;
						 bos = new ByteOutputStream();
						try {
							cdxToPdf(doc,bos);
							success = true;
						} catch (OBIBException e) {
							MiscUtils.getLogger().error(e.getMessage());

						}if (success) {
							buffer = bos.getBytes();
							bis = new ByteInputStream(buffer, bos.getCount());
							bos.close();
							streams.add(bis);
							alist.add(bis);
						}
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
				File f3 = File.createTempFile("lab"+request.getAttribute("segmentID") + "-complete","pdf");
		        
				FileOutputStream fos2 = new FileOutputStream(f2);
				FileOutputStream fos3 = new FileOutputStream(f3);
		            
				LabPDFCreator lpdfc = new LabPDFCreator(request, fos2);
				lpdfc.printPdf();
				lpdfc.addEmbeddedDocuments(f2,fos3);

				fos2.close();
				fos3.close();
				
				FileInputStream fis2 = new FileInputStream(f3);
				
				alist.add(fis2);
				streams.add(fis2);
				filesToDelete.add(f2);
				filesToDelete.add(f3);
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
			logger.error(error + " occured insided ConsultationPrintAction", exception);
			request.setAttribute("printError", new Boolean(true));
			return mapping.findForward("error");
		}
		return null;
		
    }

	/**
	 * Converts attached CDX document in the consultation request to PDF.
	 *
	 * @param os the output stream where the PDF will be written
	 * @throws IOException       when an error with the output stream occurs
	 * @throws DocumentException when an error in document construction occurs
	 */

	private void cdxToPdf(EDoc doc, ByteOutputStream os) throws OBIBException {

		CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
		CdxProvenance provDoc = provenanceDao.findByDocumentNo(Integer.parseInt(doc.getDocId()));
		CdxAttachmentDao attachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);

		ArrayList<Object> streamList = new ArrayList<Object>();


		// transform main document from XML to HTML
		try {
			StringReader cdaReader = new StringReader(provDoc.getPayload());
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new StreamSource(contentRoute + "/share/xslt/CDA_to_HTML.xsl"));

			ByteOutputStream bos_html = new ByteOutputStream();
			transformer.transform(new StreamSource(cdaReader), new StreamResult(bos_html));

			String html = new String(bos_html.getBytes(), UTF_8);

			// transform main document from HTML to PDF

			HtmlToPdf htmlToPdf = HtmlToPdf.create()
					.object(HtmlToPdfObject.forHtml(html));

			InputStream mainDoc = htmlToPdf.convert();
			streamList.add(mainDoc);
			mainDoc.close();

			// transform attachments to CDX document

			for (CdxAttachment att : attachmentDao.findByDocNo(provDoc.getId())) {
				InputStream attDoc = null;
				if (att.getAttachmentType().equals(AttachmentType.PDF.mediaType)) {
					attDoc = new ByteArrayInputStream(att.getContent());
				} else if (att.getAttachmentType().equals(AttachmentType.JPEG.mediaType)
						|| att.getAttachmentType().equals(AttachmentType.PNG.mediaType)
						|| att.getAttachmentType().equals(AttachmentType.TIFF.mediaType)) {
					ByteOutputStream aos = new ByteOutputStream();
					imageToPdf(att.getContent(), aos);
					aos.close();
					byte[] buffer = aos.getBytes();
					attDoc = new ByteArrayInputStream(buffer);
				} else if (att.getAttachmentType().equals(AttachmentType.RTF.mediaType)) {
					Document document = new Document();
					ByteOutputStream aos = new ByteOutputStream();
					PdfWriter writer = PdfWriter.getInstance(document, aos);
					document.open();
					RtfParser parser = new RtfParser(null);
					parser.convertRtfDocument(new ByteArrayInputStream(att.getContent()), document);
					document.close();
					aos.close();
					byte[] buffer = aos.getBytes();
					attDoc = new ByteArrayInputStream(buffer);
				} else throw new OBIBException("Unknown attachment type of CDX document ("
						+ att.getAttachmentType() + ")");
				streamList.add(attDoc);
			}
			ConcatPDF.concat(streamList, os);
		} catch (Exception e) {
			throw new OBIBException("Attachment document to PDF automatically. The document has *not* been sent.");
		}
	}
	/**
	 * Converts attached image in the consultation request to PDF.
	 *
	 * @param os the output stream where the PDF will be written
	 * @throws IOException       when an error with the output stream occurs
	 * @throws DocumentException when an error in document construction occurs
	 */
	private void imageToPdf(byte[] content, OutputStream os) throws IOException, DocumentException {
		Image image = Image.getInstance(content);

		// Create the document we are going to write to
		Document document = new Document();
		// PdfWriter writer = PdfWriter.getInstance(document, os);
		PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_6PT);


		document.setPageSize(PageSize.LETTER);
		document.addCreator("OSCAR");
		document.open();

		PdfContentByte cb = writer.getDirectContent();
		if (image.getScaledWidth() > 500 || image.getScaledHeight() > 700) {
			image.scaleToFit(500, 700);
		}
		image.setAbsolutePosition(20, 20);
		cb.addImage(image);
		document.close();
	}
}
