/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.form.pdfservlet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.web.PrescriptionQrCodeUIBean;

import oscar.OscarProperties;

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
			baosPDF = generatePDFDocumentBytes(req, this.getServletContext());

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
		} catch (DocumentException dex) {
			res.setContentType("text/html");
			PrintWriter writer = res.getWriter();
			writer.println("Exception from: " + this.getClass().getName() + " " + dex.getClass().getName() + "<br>");
			writer.println("<pre>");
			dex.printStackTrace(writer);
			writer.println("</pre>");
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
		private String sigDoctorName;
		private String rxDate;
		private String promoText;
		private String origPrintDate = null;
		private String numPrint = null;

		public EndPage() {
		}

        public EndPage(String clinicName, String clinicTel, String clinicFax, String patientPhone, String patientCityPostal, String patientAddress,
                String patientName,String patientDOB, String sigDoctorName, String rxDate,String origPrintDate,String numPrint) {
			this.clinicName = clinicName;
			this.clinicTel = clinicTel;
			this.clinicFax = clinicFax;
			this.patientPhone = patientPhone;
			this.patientCityPostal = patientCityPostal;
			this.patientAddress = patientAddress;
			this.patientName = patientName;
                        this.patientDOB=patientDOB;
			this.sigDoctorName = sigDoctorName;
			this.rxDate = rxDate;
			this.promoText = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
			this.origPrintDate = origPrintDate;
			this.numPrint = numPrint;
			if (promoText == null) {
				promoText = "";
			}
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

		public void renderPage(PdfWriter writer, Document document) {
			Rectangle page = document.getPageSize();
			PdfContentByte cb = writer.getDirectContent();

			try {


				float height = page.height();
                boolean showPatientDOB=false;
                //head.writeSelectedRows(0, 1,document.leftMargin(), page.height() - document.topMargin()+ head.getTotalHeight(),writer.getDirectContent());
                if(this.patientDOB!=null && this.patientDOB.length()>0){
                    showPatientDOB=true;
                }
                //header table for patient's information.
				PdfPTable head = new PdfPTable(1);
				String newline = System.getProperty("line.separator");
                String hStr=this.patientName;
                if(showPatientDOB){
                     hStr+="   DOB:"+this.patientDOB+"                               " + this.rxDate + newline;}
                else{hStr+="                            " + this.rxDate + newline;}

                hStr+=this.patientAddress + newline + this.patientCityPostal + newline + this.patientPhone;
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				Phrase hPhrase = new Phrase(hStr, new Font(bf, 10));
				head.addCell(hPhrase);
				head.setTotalWidth(272f);
				head.writeSelectedRows(0, -1, 13f, height - 90f, cb);

				// draw R
				writeDirectContent(cb, bf, 50, PdfContentByte.ALIGN_LEFT, "R", 20, page.height() - 53, 0);

				// draw X
				writeDirectContent(cb, bf, 43, PdfContentByte.ALIGN_LEFT, "X", 40, page.height() - 71, 0);

				// render clinicName;
				bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				// p("render clinicName");
				int fontFlags = Font.NORMAL;
				Font font = new Font(bf, 10, fontFlags);
				ColumnText ct = new ColumnText(cb);
				ct.setSimpleColumn(80, (page.height() - 15), 280, (page.height() - 100), 11, Element.ALIGN_LEFT);
				// p("value of clinic name", this.clinicName);
				ct.setText(new Phrase(12, this.clinicName, font));
				ct.go();
				// render clnicaTel;
				// bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				if (this.clinicTel.length() <= 13) {
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, "Tel:" + this.clinicTel, 188, (page.height() - 70), 0);
					// render clinicFax;
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, "Fax:" + this.clinicFax, 188, (page.height() - 80), 0);
				} else {
					String str1 = this.clinicTel.substring(0, 13);
					String str2 = this.clinicTel.substring(13);
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, "Tel:" + str1, 188, (page.height() - 70), 0);
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, str2, 188, (page.height() - 80), 0);
					writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, "Fax:" + this.clinicFax, 188, (page.height() - 88), 0);
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
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, "Signature:", 20f, endPara - 30f, 0);
				// Render line for Signature 75, 55, 280, 55, 0.5
				cb.setRGBColorStrokeF(0f, 0f, 0f);
				cb.setLineWidth(0.5f);
				// cb.moveTo(75f, 50f);
				// cb.lineTo(280f, 50f);
				cb.moveTo(75f, endPara - 30f);
				cb.lineTo(280f, endPara - 30f);
				cb.stroke();
				// Render doctor name
				writeDirectContent(cb, bf, 10, PdfContentByte.ALIGN_LEFT, this.sigDoctorName, 90, endPara - 40f, 0);
				// public void writeDirectContent(PdfContentByte cb, BaseFont bf, float fontSize, int alignment, String text, float x, float y, float rotation)
				// render reprint origPrintDate and numPrint
				if (origPrintDate != null && numPrint != null) {
					String rePrintStr = "Reprint by " + this.sigDoctorName + "; Original Printed: " + origPrintDate + "; Times Printed: " + numPrint;
					writeDirectContent(cb, bf, 6, PdfContentByte.ALIGN_LEFT, rePrintStr, 50, endPara - 48, 0);
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

	private HashMap parseSCAddress(String s) {
		HashMap hm = new HashMap();
		String[] ar = s.split("</b>");
		String[] ar2 = ar[1].split("<br>");
		ArrayList<String> lst = new ArrayList(Arrays.asList(ar2));
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

	protected ByteArrayOutputStream generatePDFDocumentBytes(final HttpServletRequest req, final ServletContext ctx) throws DocumentException, java.io.IOException {
		logger.debug("***in generatePDFDocumentBytes2 FrmCustomedPDFServlet.java***");
		// added by vic, hsfo
		Enumeration em = req.getParameterNames();
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
		String useSatelliteClinic = (String) req.getParameter("useSC");
		logger.debug(useSatelliteClinic);
		if (useSatelliteClinic != null && useSatelliteClinic.equalsIgnoreCase("true")) {
			String scAddress = (String) req.getParameter("scAddress");
			logger.debug("clinic detail" + "=" + scAddress);
			HashMap hm = parseSCAddress(scAddress);
			clinicName = (String) hm.get("clinicName");
			clinicTel = (String) hm.get("clinicTel");
			clinicFax = (String) hm.get("clinicFax");
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
		List<String> listRx = new ArrayList();
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
		for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
			temp = new StringBuilder(e.nextElement().toString());
			props.setProperty(temp.toString(), req.getParameter(temp.toString()));
		}

		for (Enumeration e = req.getAttributeNames(); e.hasMoreElements();) {
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
			ArrayList graphicPageArray = new ArrayList();
			if (graphicPage != null) {
				// for (String s : graphicPage) {
				// p("graphicPage", s);
				// }
				graphicPageArray = new ArrayList(Arrays.asList(graphicPage));
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
			document.setMargins(15, pageSize.width() - 285f + 5f, 140, 60);// left, right, top , bottom

			writer = PdfWriter.getInstance(document, baosPDF);
			writer.setPageEvent(new EndPage(clinicName, clinicTel, clinicFax, patientPhone, patientCityPostal, patientAddress, patientName,patientDOB, sigDoctorName, rxDate, origPrintDate, numPrint));
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
			// render prescriptions
			for (String rxStr : listRx) {
				bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
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
			if (PrescriptionQrCodeUIBean.isPrescriptionQrCodeEnabledForCurrentProvider())
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
