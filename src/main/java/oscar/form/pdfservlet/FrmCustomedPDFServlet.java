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


package oscar.form.pdfservlet;

import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.tika.io.IOUtils;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.util.LocaleUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.web.PrescriptionQrCodeUIBean;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;

import com.itextpdf.text.pdf.PdfReader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class FrmCustomedPDFServlet extends HttpServlet {

	public static final String HSFO_RX_DATA_KEY = "hsfo.rx.data";
	private static Logger logger = MiscUtils.getLogger();

	@Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException, java.io.IOException {

		ByteArrayOutputStream baosPDF = null;

		try {
			String method = req.getParameter("__method");
			boolean isFax = method.equals("oscarRxFax");
			baosPDF = generatePDFDocumentBytes(req, this.getServletContext());
			if (isFax) {
				res.setContentType("text/html");
				PrintWriter writer = res.getWriter();
				String faxNo = req.getParameter("pharmaFax").trim().replaceAll("\\D", "");
			    if (faxNo.length() < 7) {
					writer.println("<script>alert('Error: No fax number found!');window.close();</script>");
				} else {
		                	// write to file
		                	String pdfFile = "prescription_"+Integer.parseInt(req.getParameter("pdfId"))+".pdf";
		                	String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/";

		                	FileOutputStream fos = null;
		                	try {
		                		fos = new FileOutputStream(path+pdfFile);
		                		baosPDF.writeTo(fos);
		                	} finally {
		                		IOUtils.closeQuietly(fos);
		                	}

					String tempPath = OscarProperties.getInstance().getProperty(
								"fax_file_location", System.getProperty("java.io.tmpdir"));
		                	// write to file
		                	String tempPdf = tempPath + "/prescription_" + req.getParameter("pdfId") + ".pdf";
		                	// Copying the fax pdf.
							FileUtils.copyFile(new File(path+pdfFile), new File(tempPdf));

			                String txtFile = tempPath + "/prescription_" + req.getParameter("pdfId") + ".txt";
		                	FileWriter fstream = new FileWriter(txtFile);
		                	BufferedWriter out = new BufferedWriter(fstream);
			                try {
			                	out.write(faxNo);
		                    } finally {
		                    	if (out != null) out.close();
		                	}		                	
		                	
		                	
			                String faxNumber = req.getParameter("clinicFax");
			                String demo = req.getParameter("demographic_no");
			                FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
			                FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
			                List<FaxConfig> faxConfigs = faxConfigDao.findAll(null, null);
			                String provider_no = LoggedInInfo.getLoggedInInfoFromSession(req).getLoggedInProviderNo();
			                FaxJob faxJob;
			                boolean validFaxNumber = false;
			                
			                for( FaxConfig faxConfig : faxConfigs ) {
			                	
			                	if( faxConfig.getFaxNumber().equals(faxNumber) ) {
			                		
			                		PdfReader pdfReader = new PdfReader(path+pdfFile);
			                		
			                		faxJob = new FaxJob();
			                		faxJob.setDestination(faxNo);
			                		faxJob.setFax_line(faxNumber);
			                		faxJob.setFile_name(pdfFile);
			                		faxJob.setUser(faxConfig.getFaxUser());
			                		faxJob.setNumPages(pdfReader.getNumberOfPages());
			                		faxJob.setStamp(new Date());
			                		faxJob.setStatus(FaxJob.STATUS.SENT);
			                		faxJob.setOscarUser(provider_no);
			                		faxJob.setDemographicNo(Integer.parseInt(demo));
			                		
			                		faxJobDao.persist(faxJob);
			                		validFaxNumber = true;
			                		break;
			                		
			                	}
			                }
			                
			        if( validFaxNumber ) {
			        	
			        	LogAction.addLog(provider_no, LogConst.SENT, LogConst.CON_FAX, "PRESCRIPTION " + pdfFile );
			        	writer.println("<script>alert('Fax sent to: " + StringEscapeUtils.escapeJavaScript(req.getParameter("pharmaName")) + " (" + faxNo + ")');window.close();</script>");
			        	
			        }
				}
			} else {
				StringBuilder sbFilename = new StringBuilder();
				sbFilename.append("filename_");
				sbFilename.append(".pdf");

				// set the Cache-Control header
				res.setHeader("Cache-Control", "max-age=0");
				res.setDateHeader("Expires", 0);

				res.setContentType("application/pdf");

				// The Content-disposition value will be inline
				StringBuilder sbContentDispValue = new StringBuilder();
				sbContentDispValue.append("inline; filename="); // inline - display
				// the pdf file
				// directly rather
				// than open/save
				// selection
				// sbContentDispValue.append("; filename=");
				sbContentDispValue.append(sbFilename);

				res.setHeader("Content-disposition", sbContentDispValue.toString());

				res.setContentLength(baosPDF.size());

				ServletOutputStream sos;

				sos = res.getOutputStream();

				baosPDF.writeTo(sos);
				
				sos.flush();
			}
		} catch (DocumentException dex) {
			res.setContentType("text/html");
			PrintWriter writer = res.getWriter();
			writer.println("Exception from: " + this.getClass().getName() + " " + dex.getClass().getName() + "<br>");
			writer.println("<pre>");
			writer.println(dex.getMessage());
			writer.println("</pre>");
		} catch (java.io.FileNotFoundException dex) {
		    res.setContentType("text/html");
		    PrintWriter writer = res.getWriter();
		    writer.println("<script>alert('Signature not found. Please sign the prescription.');</script>");
	    } finally {
			if (baosPDF != null) {
				baosPDF.reset();
			}
		}

	}

	// added by vic, hsfo
	private ByteArrayOutputStream generateHsfoRxPDF(HttpServletRequest req) {

		HsfoRxDataHolder rx = (HsfoRxDataHolder) req.getSession().getAttribute(HSFO_RX_DATA_KEY);

		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(rx.getOutlines());
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/oscar/form/prop/Hsfo_Rx.jasper");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			JasperRunManager.runReportToPdfStream(is, baos, rx.getParams(), ds);
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
		return baos;
	}

	/**
	 * the form txt file has lines in the form: For Checkboxes: ie. ohip : left, 76, 193, 0, BaseFont.ZAPFDINGBATS, 8, \u2713 requestParamName : alignment, Xcoord, Ycoord, 0, font, fontSize, textToPrint[if empty, prints the value of the request param]
	 * NOTE: the Xcoord and Ycoord refer to the bottom-left corner of the element For single-line text: ie. patientCity : left, 242, 261, 0, BaseFont.HELVETICA, 12 See checkbox explanation For multi-line text (textarea) ie. aci : left, 20, 308, 0,
	 * BaseFont.HELVETICA, 8, _, 238, 222, 10 requestParamName : alignment, bottomLeftXcoord, bottomLeftYcoord, 0, font, fontSize, _, topRightXcoord, topRightYcoord, spacingBtwnLines NOTE: When working on these forms in linux, it helps to load the PDF file
	 * into gimp, switch to pt. coordinate system and use the mouse to find the coordinates. Prepare to be bored!
	 */

	class EndPage extends PdfPageEventHelper {

		private String clinicName;
		private String clinicTel;
		private String clinicFax;
		private String patientPhone;
		private String patientCityPostal;
		private String patientAddress;
		private String patientName;
        private String patientDOB;
        private String patientHIN;
        private String patientChartNo;
        private String bandNumber;
        private String pracNo;
		private String sigDoctorName;
		private String rxDate;
		private String promoText;
		private String origPrintDate = null;
		private String numPrint = null;
		private String imgPath;
                Locale locale = null;
                
		public EndPage() {
		}


		public EndPage(String clinicName, String clinicTel, String clinicFax, String patientPhone, String patientCityPostal, String patientAddress,
                String patientName,String patientDOB, String sigDoctorName, String rxDate,String origPrintDate,String numPrint, String imgPath, 
                String patientHIN, String patientChartNo, String bandNumber, String pracNo, Locale locale) {

			this.clinicName = clinicName==null ? "" : clinicName;
			this.clinicTel = clinicTel==null ? "" : clinicTel;
			this.clinicFax = clinicFax==null ? "" : clinicFax;
			this.patientPhone = patientPhone==null ? "" : patientPhone;
			this.patientCityPostal = patientCityPostal==null ? "" : patientCityPostal;
			this.patientAddress = patientAddress==null ? "" : patientAddress;	
			this.patientName = patientName;
            this.patientDOB=patientDOB;
			this.sigDoctorName = sigDoctorName==null ? "" : sigDoctorName;
			this.rxDate = rxDate;
			this.promoText = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
			this.origPrintDate = origPrintDate;
			this.numPrint = numPrint;
			if (promoText == null) {
				promoText = "";
			}
			this.imgPath = imgPath;
			this.patientHIN = patientHIN==null ? "" : patientHIN;
			this.patientChartNo = patientChartNo==null ? "" : patientChartNo;
			this.pracNo = pracNo==null ? "" : pracNo;
			this.locale = locale;
            this.bandNumber = bandNumber;            

		}

		@Override
        public void onEndPage(PdfWriter writer, Document document) {
			renderPage(writer, document);
		}

		public void writeDirectContent(PdfContentByte cb, BaseFont bf, float fontSize, int alignment, String text, float x, float y, float rotation) {
			cb.beginText();
			cb.setFontAndSize(bf, fontSize);
			cb.showTextAligned(alignment, text, x, y, rotation);
			cb.endText();
		}
		
		private String geti18nTagValue(Locale locale, String tag) {
			return LocaleUtils.getMessage(locale,tag);
		}
		
		public void renderPage(PdfWriter writer, Document document) {
			Rectangle page = document.getPageSize();
			PdfContentByte cb = writer.getDirectContent();

			try {


				float height = page.getHeight();
                                boolean showPatientDOB=false;
                                //head.writeSelectedRows(0, 1,document.leftMargin(), page.height() - document.topMargin()+ head.getTotalHeight(),writer.getDirectContent());
                                if(this.patientDOB!=null && this.patientDOB.length()>0){
                                    showPatientDOB=true;
                                }
                                //header table for patient's information.
                                                PdfPTable head = new PdfPTable(1);
                                                String newline = System.getProperty("line.separator");
                                StringBuilder hStr = new StringBuilder(this.patientName);
                                if(showPatientDOB){
                                     hStr.append("   "+geti18nTagValue(locale, "RxPreview.msgDOB")+":").append(this.patientDOB);
                                }
                                hStr.append(newline).append(this.patientAddress).append(newline).append(this.patientCityPostal).append(newline).append(this.patientPhone);
                                
                                if (patientHIN != null && patientHIN.trim().length() > 0) { 
                                    hStr.append(newline).append(geti18nTagValue(locale, "oscar.oscarRx.hin")+" ").append(patientHIN); 
                                }

                                if (patientChartNo != null && !patientChartNo.isEmpty()) {
                                    String chartNoTitle = geti18nTagValue(locale, "oscar.oscarRx.chartNo") ;
                                    hStr.append(newline).append(chartNoTitle).append(patientChartNo);
                                }
                                
                                if( bandNumber != null && ! bandNumber.isEmpty() ) {
                                	String bandNumberTitle = org.oscarehr.util.LocaleUtils.getMessage(locale, "oscar.oscarRx.bandNumber");
                                	 hStr.append(newline).append(bandNumberTitle).append(bandNumber);
                                }
                                
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				Phrase hPhrase = new Phrase(hStr.toString(), new Font(bf, 10));
				head.addCell(hPhrase);
				head.setTotalWidth(272f);
				head.writeSelectedRows(0, -1, 13f, height - 100f, cb);

				bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				writeDirectContent(cb, bf, 12, PdfContentByte.ALIGN_LEFT, "o s c a r", 21, page.getHeight() - 60, 90);
				// draw R
				writeDirectContent(cb, bf, 50, PdfContentByte.ALIGN_LEFT, "P", 24, page.getHeight() - 53, 0);

				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				// draw X
				writeDirectContent(cb, bf, 43, PdfContentByte.ALIGN_LEFT, "X", 38, page.getHeight() - 69, 0);

				bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, this.sigDoctorName, 80, (page.getHeight() - 25), 0);
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, this.rxDate, 188, (page.getHeight() - 90), 0);
				
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				int fontFlags = Font.NORMAL;
				Font font = new Font(bf, 10, fontFlags);
				ColumnText ct = new ColumnText(cb);
				ct.setSimpleColumn(80, (page.getHeight() - 25), 280, (page.getHeight() - 90), 11, Element.ALIGN_LEFT);
				// p("value of clinic name", this.clinicName);
				ct.setText(new Phrase(12, clinicName+(pracNo.trim().length()>0 ? "\r\n"+geti18nTagValue(locale, "RxPreview.PractNo")+": "+ pracNo : ""), font));ct.go();
				// render clnicaTel;
				if (this.clinicTel.length() <= 13) {
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, geti18nTagValue(locale, "RxPreview.msgTel")+":" + this.clinicTel, 188, (page.getHeight() - 70), 0);
					// render clinicFax;
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, geti18nTagValue(locale, "RxPreview.msgFax")+":" + this.clinicFax, 188, (page.getHeight() - 80), 0);
				} else {
					String str1 = this.clinicTel.substring(0, 13);
					String str2 = this.clinicTel.substring(13);
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, geti18nTagValue(locale, "RxPreview.msgTel")+":" + str1, 188, (page.getHeight() - 70), 0);
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, str2, 188, (page.getHeight() - 80), 0);
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, geti18nTagValue(locale, "RxPreview.msgFax")+":" + this.clinicFax, 188, (page.getHeight() - 88), 0);
				}

				// get the end of paragraph
				float endPara = writer.getVerticalPosition(true);
				// draw left line
				cb.setRGBColorStrokeF(0f, 0f, 0f);
				cb.setLineWidth(0.5f);
				// cb.moveTo(13f, 20f);
				cb.moveTo(13f, endPara - 60);
				cb.lineTo(13f, height - 15f);
				cb.stroke();

				// draw right line 285, 20, 285, 405, 0.5
				cb.setRGBColorStrokeF(0f, 0f, 0f);
				cb.setLineWidth(0.5f);
				// cb.moveTo(285f, 20f);
				cb.moveTo(285f, endPara - 60);
				cb.lineTo(285f, height - 15f);
				cb.stroke();
				// draw top line 10, 405, 285, 405, 0.5
				cb.setRGBColorStrokeF(0f, 0f, 0f);
				cb.setLineWidth(0.5f);
				cb.moveTo(13f, height - 15f);
				cb.lineTo(285f, height - 15f);
				cb.stroke();

				// draw bottom line 10, 20, 285, 20, 0.5
				cb.setRGBColorStrokeF(0f, 0f, 0f);
				cb.setLineWidth(0.5f);
				// cb.moveTo(13f, 20f);
				// cb.lineTo(285f, 20f);
				cb.moveTo(13f, endPara - 60);
				cb.lineTo(285f, endPara - 60);
				cb.stroke();
				// Render "Signature:"
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, geti18nTagValue(locale, "RxPreview.msgSignature"), 20f, endPara - 30f, 0);// Render line for Signature 75, 55, 280, 55, 0.5
				cb.setRGBColorStrokeF(0f, 0f, 0f);
				cb.setLineWidth(0.5f);
				// cb.moveTo(75f, 50f);
				// cb.lineTo(280f, 50f);
				cb.moveTo(75f, endPara - 30f);
				cb.lineTo(280f, endPara - 30f);
				cb.stroke();

				if (this.imgPath != null) {
					Image img = Image.getInstance(this.imgPath);
					// image, image_width, 0, 0, image_height, x, y
					//         131, 55, 375, 75, 0
					cb.addImage(img, 157, 0, 0, 40, 150f, endPara-30f);
				}

				// Render doctor name
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, this.sigDoctorName, 90, endPara - 40f, 0);
				// public void writeDirectContent(PdfContentByte cb, BaseFont bf, float fontSize, int alignment, String text, float x, float y, float rotation)
				// render reprint origPrintDate and numPrint
				if (origPrintDate != null && numPrint != null) {
					String rePrintStr = geti18nTagValue(locale, "RxPreview.msgReprintBy")+" " + this.sigDoctorName + "; "+geti18nTagValue(locale, "RxPreview.msgOrigPrinted")+": " + origPrintDate + "; "+geti18nTagValue(locale, "RxPreview.msgTimesPrinted") +": " + numPrint;writeDirectContent(cb, bf, 6, PdfContentByte.ALIGN_LEFT, rePrintStr, 50, endPara - 48, 0);
				}
				// print promoText
				writeDirectContent(cb, bf, 6, PdfContentByte.ALIGN_LEFT, this.promoText, 70, endPara - 57, 0);
				// print page number
				String footer = "" + writer.getPageNumber();
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_RIGHT, footer, 280, endPara - 57, 0);
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}
	}

	private HashMap<String,String> parseSCAddress(String s) {
		HashMap<String,String> hm = new HashMap<String,String>();
		String[] ar = s.split("</b>");
		String[] ar2 = ar[1].split("<br>");
		ArrayList<String> lst = new ArrayList<String>(Arrays.asList(ar2));
		lst.remove(0);
		String tel = lst.get(3);
		tel = tel.replace("Tel: ", "");
		String fax = lst.get(4);
		fax = fax.replace("Fax: ", "");
		String clinicName = lst.get(0) + "\n" + lst.get(1) + "\n" + lst.get(2);
		logger.debug(tel);
		logger.debug(fax);
		logger.debug(clinicName);
		hm.put("clinicName", clinicName);
		hm.put("clinicTel", tel);
		hm.put("clinicFax", fax);

		return hm;

	}

	protected ByteArrayOutputStream generatePDFDocumentBytes(final HttpServletRequest req, final ServletContext ctx) throws DocumentException {
		logger.debug("***in generatePDFDocumentBytes2 FrmCustomedPDFServlet.java***");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(req);
		
		// added by vic, hsfo
		Enumeration<String> em = req.getParameterNames();
		while (em.hasMoreElements()) {
			logger.debug("para=" + em.nextElement());
		}
		em = req.getAttributeNames();
		while (em.hasMoreElements())
			logger.debug("attr: " + em.nextElement());

		if (HSFO_RX_DATA_KEY.equals(req.getParameter("__title"))) {
			return generateHsfoRxPDF(req);
		}
		String newline = System.getProperty("line.separator");

		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
		PdfWriter writer = null;
		String method = req.getParameter("__method");
		String origPrintDate = null;
		String numPrint = null;
		if (method != null && method.equalsIgnoreCase("rePrint")) {
			origPrintDate = req.getParameter("origPrintDate");
			numPrint = req.getParameter("numPrints");
		}

		logger.debug("method in generatePDFDocumentBytes " + method);
		String clinicName;
		String clinicTel;
		String clinicFax;
		// check if satellite clinic is used
		String useSatelliteClinic = req.getParameter("useSC");
		logger.debug(useSatelliteClinic);
		if (useSatelliteClinic != null && useSatelliteClinic.equalsIgnoreCase("true")) {
			String scAddress = req.getParameter("scAddress");
			logger.debug("clinic detail" + "=" + scAddress);
			HashMap<String,String> hm = parseSCAddress(scAddress);
			clinicName =  hm.get("clinicName");
			clinicTel = hm.get("clinicTel");
			clinicFax = hm.get("clinicFax");
		} else {
			// parameters need to be passed to header and footer
			clinicName = req.getParameter("clinicName");
			logger.debug("clinicName" + "=" + clinicName);
			clinicTel = req.getParameter("clinicPhone");
			clinicFax = req.getParameter("clinicFax");
		}
		String patientPhone = req.getParameter("patientPhone");
		String patientCityPostal = req.getParameter("patientCityPostal");
		String patientAddress = req.getParameter("patientAddress");
		String patientName = req.getParameter("patientName");
		String sigDoctorName = req.getParameter("sigDoctorName");
		String rxDate = req.getParameter("rxDate");
		String rx = req.getParameter("rx");
        String patientDOB=req.getParameter("patientDOB");
        String showPatientDOB=req.getParameter("showPatientDOB");
        String imgFile=req.getParameter("imgFile");
        String patientHIN=req.getParameter("patientHIN");
        String patientChartNo = req.getParameter("patientChartNo");
        String patientBandNumber = req.getParameter("bandNumber");
        String pracNo=req.getParameter("pracNo");
        Locale locale = req.getLocale();
        
		if (clinicName==null) clinicName = "";
		if (clinicTel==null) clinicTel = "";
		if (clinicFax==null) clinicFax = "";
		if (patientPhone==null) patientPhone = "";
		if (patientCityPostal==null) patientCityPostal = "";
		if (patientAddress==null) patientAddress = "";
		if (sigDoctorName==null) sigDoctorName = "";
        if (patientHIN==null) patientHIN = "";
        if (patientChartNo==null) patientChartNo = "";
        if (pracNo==null) pracNo = "";
        
        boolean isShowDemoDOB=false;
        if(showPatientDOB!=null&&showPatientDOB.equalsIgnoreCase("true")){
            isShowDemoDOB=true;
        }
        if(!isShowDemoDOB)
            patientDOB="";
		if (rx == null) {
			rx = "";
		}

		String additNotes = req.getParameter("additNotes");
		String[] rxA = rx.split(newline);
		List<String> listRx = new ArrayList<String>();
		String listElem = "";
		// parse rx and put into a list of rx;
		for (String s : rxA) {

			if (s.equals("") || s.equals(newline) || s.length() == 1) {
				listRx.add(listElem);
				listElem = "";
			} else {
				listElem = listElem + s;
				listElem += newline;
			}

		}

		// get the print prop values
		Properties props = new Properties();
		StringBuilder temp = new StringBuilder();
		for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();) {
			temp = new StringBuilder(e.nextElement().toString());
			props.setProperty(temp.toString(), req.getParameter(temp.toString()));
		}

		for (Enumeration<String> e = req.getAttributeNames(); e.hasMoreElements();) {
			temp = new StringBuilder(e.nextElement().toString());
			props.setProperty(temp.toString(), req.getAttribute(temp.toString()).toString());
		}
		Document document = new Document();

		try {
			String title = req.getParameter("__title") != null ? req.getParameter("__title") : "Unknown";

			// specify the page of the picture using __graphicPage, it may be used multiple times to specify multiple pages
			// however the same graphic will be applied to all pages
			// ie. __graphicPage=2&__graphicPage=3
			String[] cfgGraphicFile = req.getParameterValues("__cfgGraphicFile");
			int cfgGraphicFileNo = cfgGraphicFile == null ? 0 : cfgGraphicFile.length;
			if (cfgGraphicFile != null) {
				// for (String s : cfgGraphicFile) {
				// p("cfgGraphicFile", s);
				// }
			}

			String[] graphicPage = req.getParameterValues("__graphicPage");
			ArrayList<String> graphicPageArray = new ArrayList<String>();
			if (graphicPage != null) {
				// for (String s : graphicPage) {
				// p("graphicPage", s);
				// }
				graphicPageArray = new ArrayList<String>(Arrays.asList(graphicPage));
			}

			// A0-A10, LEGAL, LETTER, HALFLETTER, _11x17, LEDGER, NOTE, B0-B5, ARCH_A-ARCH_E, FLSA
			// and FLSE
			// the following shows a temp way to get a print page size
			Rectangle pageSize = PageSize.LETTER;
			String pageSizeParameter = req.getParameter("rxPageSize");
			if (pageSizeParameter != null) {
				if ("PageSize.HALFLETTER".equals(pageSizeParameter)) {
					pageSize = PageSize.HALFLETTER;
				} else if ("PageSize.A6".equals(pageSizeParameter)) {
					pageSize = PageSize.A6;
				} else if ("PageSize.A4".equals(pageSizeParameter)) {
					pageSize = PageSize.A4;
				}
			}
			/*
			 * if ("PageSize.HALFLETTER".equals(props.getProperty(PAGESIZE))) { pageSize = PageSize.HALFLETTER; } else if ("PageSize.A6".equals(props.getProperty(PAGESIZE))) { pageSize = PageSize.A6; } else if
			 * ("PageSize.A4".equals(props.getProperty(PAGESIZE))) { pageSize = PageSize.A4; }
			 */
			// p("size of page ", props.getProperty(PAGESIZE));

			document.setPageSize(pageSize);
			// 285=left margin+width of box, 5f is space for looking nice
			document.setMargins(15, pageSize.getWidth() - 285f + 5f, 170, 60);// left, right, top , bottom

			// writer = PdfWriter.getInstance(document, baosPDF);
			writer = PdfWriterFactory.newInstance(document, baosPDF, FontSettings.HELVETICA_10PT);
			writer.setPageEvent(new EndPage(clinicName, clinicTel, clinicFax, patientPhone, patientCityPostal, 
					patientAddress, patientName,patientDOB, sigDoctorName, rxDate, origPrintDate, numPrint, 
					imgFile, patientHIN, patientChartNo, patientBandNumber, pracNo, 
					locale));

			document.addTitle(title);
			document.addSubject("");
			document.addKeywords("pdf, itext");
			document.addCreator("OSCAR");
			document.addAuthor("");
			document.addHeader("Expires", "0");

			document.open();
			document.newPage();

			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf; // = normFont;

			cb.setRGBColorStroke(0, 0, 255);
			// add water mark
			if (OscarProperties.getInstance().getBooleanProperty("enable_rx_watermark", "true")) {
				Image image = null;
				if (!"oscarRxFax".equals(req.getParameter("__method"))) {
					if(OscarProperties.getInstance().getProperty("rx_watermark_file_name") !=null ) {
						image = Image.getInstance(req.getSession().getServletContext().getRealPath("/") + "images/" + OscarProperties.getInstance().getProperty("rx_watermark_file_name"));
					} else {
						image = Image.getInstance(req.getSession().getServletContext().getRealPath("/") + "images/watermark.png");
					}
					image.setAbsolutePosition(0, 0);
					image.setAlignment(Image.MIDDLE | Image.UNDERLYING);
					image.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
					cb.addImage(image);
				}
			}
			
			// render prescriptions
			for (String rxStr : listRx) {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				//bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				Paragraph p = new Paragraph(new Phrase(rxStr, new Font(bf, 10)));
				p.setKeepTogether(true);
				p.setSpacingBefore(5f);
				document.add(p);
			}
			// render additional notes
			if (additNotes != null && !additNotes.equals("")) {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				Paragraph p = new Paragraph(new Phrase(additNotes, new Font(bf, 10)));
				p.setKeepTogether(true);
				p.setSpacingBefore(10f);
				document.add(p);
			}

			// render QrCode
			if (PrescriptionQrCodeUIBean.isPrescriptionQrCodeEnabledForProvider(loggedInInfo.getLoggedInProviderNo()))
			{
				Integer scriptId=Integer.parseInt(req.getParameter("scriptId"));
				byte[] qrCodeImage=PrescriptionQrCodeUIBean.getPrescriptionHl7QrCodeImage(scriptId);
				Image qrCode=Image.getInstance(qrCodeImage);
				document.add(qrCode);
			}
		}
		catch (DocumentException dex) {
			baosPDF.reset();
			throw dex;
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			if (document != null) {
				document.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
		logger.debug("***END in generatePDFDocumentBytes2 FrmCustomedPDFServlet.java***");
		return baosPDF;
	}
}
