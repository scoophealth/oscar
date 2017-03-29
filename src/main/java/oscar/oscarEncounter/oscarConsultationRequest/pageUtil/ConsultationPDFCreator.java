/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DigitalSignatureDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DigitalSignature;
import org.oscarehr.common.model.Site;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;

import oscar.OscarProperties;
import oscar.oscarClinic.ClinicData;
import oscar.oscarRx.data.RxProviderData;
import oscar.oscarRx.data.RxProviderData.Provider;

public class ConsultationPDFCreator extends PdfPageEventHelper {

	private static Logger logger = MiscUtils.getLogger();
	private OutputStream os;

	private Document document;

	private BaseFont bf;
	private Font font;
	private Font boldFont;
	private Font bigBoldFont;
	private Font headerFont;
	private Font infoFont;

	private EctConsultationFormRequestUtil reqFrm;
	private OscarProperties props;
	private ClinicData clinic;
	private ResourceBundle oscarR;

	/**
	 * Prepares a ConsultationPDFCreator instance to print a consultation request to PDF.
	 * @param request contains the information necessary to construct the consultation request
	 * @param os the output stream where the PDF will be written
	 */
	public ConsultationPDFCreator(HttpServletRequest request, OutputStream os) {
		this.os = os;
	    reqFrm = new EctConsultationFormRequestUtil ();
	    reqFrm.estRequestFromId(LoggedInInfo.getLoggedInInfoFromSession(request), request.getParameter("reqId") == null ? (String)request.getAttribute("reqId") : request.getParameter("reqId"));
	    props = OscarProperties.getInstance();
	    clinic = new ClinicData();
		oscarR = ResourceBundle.getBundle("oscarResources",request.getLocale());
	}

	/**
	 * Prints the consultation request.
	 * @throws IOException when an error with the output stream occurs
	 * @throws DocumentException when an error in document construction occurs
	 */
	public void printPdf(LoggedInInfo loggedInInfo) throws IOException, DocumentException {

		// Create the document we are going to write to
		document = new Document();
		// PdfWriter.getInstance(document, os);
		PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);

		document.setPageSize(PageSize.LETTER);
		document.addTitle(getResource("msgConsReq"));
		document.addCreator("OSCAR");
		document.open();

		// Create the fonts that we are going to use
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		headerFont = new Font(bf, 14, Font.BOLD);
		infoFont = new Font(bf, 12, Font.NORMAL);
		font = new Font(bf, 9, Font.NORMAL);
		boldFont = new Font(bf, 10, Font.BOLD);
		bigBoldFont = new Font(bf, 12, Font.BOLD);

		createConsultationRequest(loggedInInfo);

		document.close();
	}

	/**
	 * Creates and adds the table at the top of the document
	 * which contains the consultation request.
	 */
	private void createConsultationRequest(LoggedInInfo loggedInInfo) throws DocumentException {

		float[] tableWidths = { 1f, 1f };
		PdfPTable table = new PdfPTable(1);
//		PdfPCell cell;
		PdfPTable border, border2, border1;
		table.setWidthPercentage(95);

		// Creating a border for the entire request.
		border = new PdfPTable(1);
		border.setSplitLate(false);
		addToTable(table, border, true);

		if(props.getProperty("faxLogoInConsultation")!=null) {
			// Creating container for logo and clinic information table.
			border1 = new PdfPTable(tableWidths);
			addTable(border, border1);
			
			// Adding fax logo
			PdfPTable infoTable = createLogoHeader();
			addToTable(border1, infoTable, false);
			
			// Adding clinic information to the border.
			infoTable = createClinicInfoHeader();			
			addToTable(border1, infoTable, false);
			
		} else {
			// Adding clinic information to the border.
			PdfPTable infoTable = createClinicInfoHeader();			
			addTable(border, infoTable);
		}
		
		// Add reply info 
		PdfPTable infoTable = createReplyHeader();
		addTable(border, infoTable);

		// Creating container for specialist and patient table.
		border2 = new PdfPTable(tableWidths);
		addTable(border, border2);

		// Adding specialist info to container.
		infoTable = createSpecialistTable();
		addToTable(border2, infoTable, true);

		// Adding patient info to main table.
		infoTable = createPatientTable();
		addToTable(border2, infoTable, true);

		// Creating a table with details for the consultation request.
		infoTable = createConsultDetailTable(loggedInInfo);

//		// Adding promotional information if appropriate.
//		if (props.getProperty("FORMS_PROMOTEXT") != null){
//			cell = new PdfPCell(new Phrase(props.getProperty(""), font));
//			cell.setBorder(0);
//			infoTable.addCell(cell);
//			cell.setPhrase(new Phrase(props.getProperty("FORMS_PROMOTEXT"), font));
//			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			infoTable.addCell(cell);
//		}

		// Adding details and promotional information.
		addTable(border, infoTable);

		document.add(table);
	}

	/**
	 * Add's the table 'add' to the table 'main' (with no border surrounding it.)
	 * @param main the host table
	 * @param add the table being added
	 * @return the cell containing the table being added to the main table.
	 */
	private PdfPCell addTable(PdfPTable main, PdfPTable add) {
		return addToTable(main, add, false);
	}

	/**
	 * Add's the table 'add' to the table 'main'.
	 * @param main the host table
	 * @param add the table being added
	 * @param border true if a border should surround the table being added
	 * @return the cell containing the table being added to the main table.	 *
	 */
	private PdfPCell addToTable(PdfPTable main, PdfPTable add, boolean border) {
		PdfPCell cell;
		cell = new PdfPCell(add);
		if (!border) { cell.setBorder(0); }
		cell.setPadding(3);
		cell.setColspan(1);
		main.addCell(cell);
		return cell;
	}
	
	private PdfPTable createLogoHeader() {
		float[] tableWidths;
		PdfPCell cell = new PdfPCell();
		//tableWidths = new float[]{ 1.5f, 2.5f };
		//PdfPTable infoTable = new PdfPTable(tableWidths);
		PdfPTable infoTable = new PdfPTable(1);
		try{
			String filename = "";
			if(props.getProperty("multisites")!=null && "on".equalsIgnoreCase(props.getProperty("multisites"))) {
				DocumentDao documentDao = (DocumentDao) SpringUtils.getBean("documentDao");
				SiteDao siteDao = (SiteDao) SpringUtils.getBean("siteDao");
				Site site = siteDao.getById(Integer.valueOf(reqFrm.siteName));
				if(site!=null) {
					if(site.getSiteLogoId()!=null) {
						org.oscarehr.common.model.Document d = documentDao.getDocument(String.valueOf(site.getSiteLogoId()));
						String dir = props.getProperty("DOCUMENT_DIR");
						filename = dir.concat(d.getDocfilename());
					} else {
						//If no logo file uploaded for this site, use the default one defined in oscar properties file.
						filename = props.getProperty("faxLogoInConsultation");	
					}
				}			
			} else {
				filename = props.getProperty("faxLogoInConsultation");	
			}
		
			FileInputStream fileInputStream = new FileInputStream(filename);
			byte[] faxLogImage = new byte[1024 * 256];		
			fileInputStream.read(faxLogImage);
			Image image = Image.getInstance(faxLogImage);
			//image.scalePercent(80f);
			
			// only half table width
			image.scaleToFit(PageSize.LETTER.getWidth() * 0.95f * 0.5f - 10, 50f);
			image.setBorder(0);
			cell = new PdfPCell(image);
			cell.setBorder(0);
			infoTable.addCell(cell);
		} catch (Exception e) {
					logger.error("Unexpected error.", e);
		}		
		
		// The last cell in the table is extended to the maximum available height;
				// inserting a blank cell here prevents the last border used to underline text from
				// being displaced to the bottom of this table.
				cell.setPhrase(new Phrase(" ", font));
				cell.setBorder(0);
				cell.setColspan(2);
				infoTable.addCell(cell);
		return infoTable;		
				
	}
	
	private PdfPTable createReplyHeader() {
		
		PdfPCell cell;
		PdfPTable infoTable = new PdfPTable(1);

		cell = new PdfPCell(new Phrase("", headerFont));
		cell.setBorder(0);
		cell.setPaddingLeft(25);
		infoTable.addCell(cell);
		
		cell.setPhrase(new Phrase(getResource("msgConsReq"), bigBoldFont));
		cell.setPadding(0);
		cell.setBorder(0);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		infoTable.addCell(cell);

		if ( "1".equals(reqFrm.pwb) ){
			cell.setPhrase(new Phrase(getResource("msgPleaseReplyPatient"), boldFont));
		}
		
		// If not set to Patient Will Book then maybe a Custom Appointment Instruction is used.
		else if( OscarProperties.getInstance().getBooleanProperty("CONSULTATION_APPOINTMENT_INSTRUCTIONS_LOOKUP", "true") ) {
			cell.setPhrase( new Phrase( reqFrm.getAppointmentInstructionsLabel(), boldFont ));
 		}

		else if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
			cell.setPhrase(new Phrase("", boldFont));
		}
		else {
			cell.setPhrase(new Phrase(
					String.format("%s %s %s", getResource("msgPleaseReplyPart1"),
											  clinic.getClinicName(),
											  getResource("msgPleaseReplyPart2")), boldFont));
		}
		infoTable.addCell(cell);
		
		// The last cell in the table is extended to the maximum available height;
				// inserting a blank cell here prevents the last border used to underline text from
				// being displaced to the bottom of this table.
				cell.setPhrase(new Phrase(" ", font));
				cell.setBorder(0);
				cell.setColspan(2);
				infoTable.addCell(cell);
				
		return infoTable;
	}

	/**
	 * Creates a table and populates it with the clinic information for the header.
	 * @return the table produced
	 */
	private PdfPTable createClinicInfoHeader() {

		String letterheadName = null;
		if (reqFrm.letterheadName != null && reqFrm.letterheadName.startsWith("prog_")) {
			ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
			Integer programNo = Integer.parseInt(reqFrm.letterheadName.substring(5));
			letterheadName = programDao.getProgramName(programNo);
		} else if (reqFrm.letterheadName != null && !reqFrm.letterheadName.equals("-1") && clinic != null && !reqFrm.letterheadName.equals(clinic.getClinicName())) {
			Provider letterheadNameProvider = (reqFrm.letterheadName != null ? new RxProviderData().getProvider(reqFrm.letterheadName) : null);
			if (letterheadNameProvider != null && letterheadNameProvider.getSurname() != null){			
				String firstName = "";
				if(reqFrm.letterheadTitle!=null && reqFrm.letterheadTitle.equals("Dr")){
					firstName = letterheadNameProvider.getFirstName();
				}else{
					firstName = letterheadNameProvider.getFirstName().replace("Dr. ", "");
				}
				
				letterheadName = firstName + " " + letterheadNameProvider.getSurname();
			}else{
				letterheadName = clinic.getClinicName();
			}
		} else {
			letterheadName = clinic.getClinicName();
		}

		PdfPCell cell;
		PdfPTable infoTable = new PdfPTable(1);

		cell = new PdfPCell(new Phrase(letterheadName, headerFont));

		cell.setBorder(0);
		cell.setPaddingLeft(25);
		infoTable.addCell(cell);

		cell.setPhrase(new Phrase(
				(reqFrm.letterheadAddress != null && reqFrm.letterheadAddress.trim().length() > 0 ?
						String.format("%s", reqFrm.letterheadAddress)
					  : String.format("%s, %s, %s %s",
						 	   clinic.getClinicAddress(),  clinic.getClinicCity(),
							   clinic.getClinicProvince(), clinic.getClinicPostal())), font));
		infoTable.addCell(cell);

		cell.setPhrase(new Phrase(String.format("Tel: %s Fax: %s",
				(reqFrm.letterheadPhone != null && reqFrm.letterheadPhone.trim().length() > 0 ? reqFrm.letterheadPhone : clinic.getClinicPhone()),
				(reqFrm.letterheadFax != null && reqFrm.letterheadFax.trim().length() > 0 ? reqFrm.letterheadFax : clinic.getClinicFax())), font));
		infoTable.addCell(cell);

		cell.setPadding(0);
		cell.setPhrase(new Phrase(getResource("msgConsReq"), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		infoTable.addCell(cell);
	
		// Use a Custom Appointment Instruction - if it is set up.
		if( OscarProperties.getInstance().getBooleanProperty("CONSULTATION_APPOINTMENT_INSTRUCTIONS_LOOKUP", "true") ) {
			cell.setPhrase( new Phrase( reqFrm.getAppointmentInstructionsLabel(), boldFont ));	
		} 
		
		else if ( "1".equals(reqFrm.pwb) ){
			//cell.setPhrase(new Phrase(getResource("msgPleaseReplyPatient"), boldFont));
			// msgPleaseReplyPatient does not exist. Using Part1 and Part2 method instead
			cell.setPhrase(new Phrase(
					String.format("%s %s %s", getResource("msgPleaseReplyPart1"),
											  clinic.getClinicName(),
											  getResource("msgPleaseReplyPart2")), boldFont));
		}

		else if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
			cell.setPhrase(new Phrase("Please reply", boldFont));
		}
		
		// REDUNDANT CODE commented out.
//		else {
//			cell.setPhrase(new Phrase(
//					String.format("%s %s %s", getResource("msgPleaseReplyPart1"),
//											  clinic.getClinicName(),
//											  getResource("msgPleaseReplyPart2")), boldFont));
//		}
		
		infoTable.addCell(cell);

		return infoTable;
	}

	/**
	 * Creates the table containing information about the specialist.
	 * @return the table produced
	 */
	private PdfPTable createSpecialistTable() {
		float[] tableWidths;
		PdfPCell cell = new PdfPCell();
		tableWidths = new float[]{ 1.5f, 2.5f };
		PdfPTable infoTable = new PdfPTable(tableWidths);

		infoTable.addCell(setInfoCell(cell, getResource("msgDate")));
		infoTable.addCell(setDataCell(cell, reqFrm.pwb.equals("1") ? getResource("pwb") : reqFrm.referalDate));

		infoTable.addCell(setInfoCell(cell, getResource("msgStatus")));
		infoTable.addCell(setDataCell(cell, (reqFrm.urgency.equals("1") ?  getResource("msgUrgent") :
											 (reqFrm.urgency.equals("2") ?  getResource("msgNUrgent") :
										     (reqFrm.urgency.equals("3")) ? getResource("msgReturn")
										     : "  "))));

		infoTable.addCell(setInfoCell(cell, getResource("msgService")));
		infoTable.addCell(setDataCell(cell, reqFrm.getServiceName(reqFrm.service)));

		infoTable.addCell(setInfoCell(cell, getResource("msgConsultant")));
		infoTable.addCell(setDataCell(cell, reqFrm.getSpecailistsName(reqFrm.specialist)));



		infoTable.addCell(setInfoCell(cell, getResource("msgPhone")));
		if ((reqFrm.getSpecailistsName(reqFrm.specialist) == null) ||(reqFrm.getSpecailistsName(reqFrm.specialist).equals("-1"))||(reqFrm.getSpecailistsName(reqFrm.specialist).equals(""))) {
			infoTable.addCell(setDataCell(cell, ""));
		} else {
			infoTable.addCell(setDataCell(cell, reqFrm.specPhone));
		}


		infoTable.addCell(setInfoCell(cell, getResource("msgFax")));
		if ((reqFrm.getSpecailistsName(reqFrm.specialist) == null) || (reqFrm.getSpecailistsName(reqFrm.specialist).equals("-1"))||(reqFrm.getSpecailistsName(reqFrm.specialist).equals(""))) {
			infoTable.addCell(setDataCell(cell, ""));
		} else {
			infoTable.addCell(setDataCell(cell, reqFrm.specFax));
		}

		infoTable.addCell(setInfoCell(cell, getResource("msgAddr")));
		if ((reqFrm.getSpecailistsName(reqFrm.specialist) == null)||(reqFrm.getSpecailistsName(reqFrm.specialist).equals("-1"))||(reqFrm.getSpecailistsName(reqFrm.specialist).equals(""))) {
			infoTable.addCell(setDataCell(cell, ""));
		} else {
			infoTable.addCell(setDataCell(cell, divy(reqFrm.specAddr)));
		}

		// The last cell in the table is extended to the maximum available height;
		// inserting a blank cell here prevents the last border used to underline text from
		// being displaced to the bottom of this table.
		cell.setPhrase(new Phrase(" ", font));
		cell.setBorder(0);
		cell.setColspan(2);
		infoTable.addCell(cell);

		return infoTable;
	}

	/**
	 * Creates the table containing information about the patient.
	 * @return the table produced
	 */
	private PdfPTable createPatientTable() {
		float[] tableWidths;
		PdfPCell cell;
		tableWidths = new float[]{ 2, 2.5f };
		PdfPTable infoTable = new PdfPTable(tableWidths);
		cell = new PdfPCell();
		infoTable.addCell(setInfoCell(cell, getResource("msgPat")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientName));

		infoTable.addCell(setInfoCell(cell, getResource("msgAddr")));
		infoTable.addCell(setDataCell(cell, divy(reqFrm.patientAddress)));

		infoTable.addCell(setInfoCell(cell, getResource("msgPhone")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientPhone));

		infoTable.addCell(setInfoCell(cell, getResource("msgWPhone")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientWPhone));
                
                infoTable.addCell(setInfoCell(cell, getResource("msgEmail")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientEmail));

		infoTable.addCell(setInfoCell(cell, getResource("msgBirth")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientDOB + " (y/m/d)"));
		
		infoTable.addCell(setInfoCell(cell, getResource("msgSex")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientSex));

		infoTable.addCell(setInfoCell(cell, getResource("msgCard")));
		infoTable.addCell(setDataCell(cell, String.format("(%s) %s %s", reqFrm.patientHealthCardType,
														    reqFrm.patientHealthNum,
														    reqFrm.patientHealthCardVersionCode)));

		if (!reqFrm.pwb.equals("1")) {
			infoTable.addCell(setInfoCell(cell, getResource("msgappDate")));
			infoTable.addCell(setDataCell(cell, reqFrm.pwb.equals("1") ? getResource("pwb") : reqFrm.appointmentDate));
			infoTable.addCell(setInfoCell(cell, getResource("msgTime")));
			infoTable.addCell(setDataCell(cell, String.format("%s%s%s %s", reqFrm.appointmentHour,
					 !reqFrm.appointmentMinute.equals("") ? ":" : "",
					 reqFrm.appointmentMinute,
					 reqFrm.appointmentPm)));
		}

		infoTable.addCell(setInfoCell(cell, getResource("msgChart")));
		infoTable.addCell(setDataCell(cell, reqFrm.patientChartNo));

		// The last cell in the table is extended to the maximum available height;
		// inserting a blank cell here prevents the last border used to underline text from
		// being displaced to the bottom of this table.
		cell.setPhrase(new Phrase(" ", font));
		cell.setBorder(0);
		cell.setColspan(2);
		infoTable.addCell(cell);

		return infoTable;
	}

	/**
	 * Creates the table containing additional information
	 * about the reason for the consultation request.
	 * @return the table produced
	 */
	private PdfPTable createConsultDetailTable(LoggedInInfo loggedInInfo) {
		PdfPTable infoTable;
		PdfPCell cell;
		infoTable = new PdfPTable(1);
		cell = new PdfPCell();

		infoTable.addCell(setInfoCell(cell, getResource("msgReason")));
		infoTable.addCell(setDataCell(cell, reqFrm.reasonForConsultation));

		if(getlen(reqFrm.clinicalInformation) > 1) {
			infoTable.addCell(setInfoCell(cell, getResource("msgClinicalInfom")));
			infoTable.addCell(setDataCell(cell, divy(reqFrm.clinicalInformation)));
		}

		if(getlen(reqFrm.concurrentProblems) > 1) {
			if (props.getProperty("significantConcurrentProblemsTitle", "")
					.length() > 1) {
				infoTable.addCell(setInfoCell(cell, props.getProperty("significantConcurrentProblemsTitle", "")));
			} else {
				infoTable.addCell(setInfoCell(cell,getResource("msgSigProb")));
			}
			infoTable.addCell(setDataCell(cell, divy(reqFrm.concurrentProblems)));
		}

		if(getlen(reqFrm.currentMedications) > 1) {
			if (props.getProperty("currentMedicationsTitle", "").length() > 1) {
				infoTable.addCell(setInfoCell(cell, props.getProperty("currentMedicationsTitle", "")));
			} else {
				infoTable.addCell(setInfoCell(cell, getResource("msgCurrMed")));
			}
			infoTable.addCell(setDataCell(cell, divy(reqFrm.currentMedications)));
		}

		if(getlen(reqFrm.allergies) > 1) {
			infoTable.addCell(setInfoCell(cell, getResource("msgAllergies")));
			infoTable.addCell(setDataCell(cell, divy(reqFrm.allergies)));
		}

		ProviderDao proDAO = (ProviderDao) SpringUtils.getBean("providerDao");
		org.oscarehr.common.model.Provider pro = proDAO.getProvider(reqFrm.providerNo);
		String ohipNo = pro.getOhipNo();
		
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		Demographic demo = demographicManager.getDemographic(loggedInInfo, reqFrm.demoNo);

		String famDocOhipNo = "";
		if(demo.getProviderNo()!=null && !demo.getProviderNo().equals("")) {
			pro = proDAO.getProvider(demo.getProviderNo());
			famDocOhipNo = pro.getOhipNo();
		}

		if (OscarProperties.getInstance().getBooleanProperty("printPDF_referring_prac", "yes")) {
		infoTable.addCell(setFooterCell(cell, getResource("msgAssociated2"), reqFrm.getProviderName(reqFrm.providerNo) + ((getlen(ohipNo) > 0) ? " (" + ohipNo + ")" : "")));
		}

                if (OscarProperties.getInstance().getBooleanProperty("mrp_model", "yes")) {
  		    infoTable.addCell(setFooterCell(cell, getResource("msgFamilyDoc2"), reqFrm.getFamilyDoctor() + ((getlen(famDocOhipNo) > 0) ? " (" + famDocOhipNo + ")" : "")));
                }
		if (getlen(reqFrm.signatureImg) > 0) {
			addSignature(infoTable);
		}
		return infoTable;
	}

private void addSignature(PdfPTable infoTable) {
		float[] tableWidths;
		PdfPCell cell;
		tableWidths = new float[]{ 0.55f, 2.75f };
		PdfPTable table = new PdfPTable(tableWidths);
		cell = new PdfPCell(new Phrase(getResource("msgSignature") + ":", infoFont));
		cell.setBorder(0);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_BOTTOM);
		table.addCell(cell);
		try {
			DigitalSignatureDao digitalSignatureDao = (DigitalSignatureDao) SpringUtils.getBean("digitalSignatureDao");
			DigitalSignature digitalSignature = digitalSignatureDao.find(Integer.parseInt(reqFrm.signatureImg));
			if (digitalSignature != null) {
				Image image = Image.getInstance(digitalSignature.getSignatureImage());
				image.scalePercent(80f);
				image.setBorder(0);
				cell = new PdfPCell(image);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(table);
				cell.setBorder(0);
				cell.setPadding(0);
				cell.setColspan(1);
				infoTable.addCell(cell);

				return;
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}
	/**
	 * Formats a cell to display information provided in a regular font with an underline.
	 * @param cell the cell to format
	 * @param phrase the information to display
	 * @return the formatted cell
	 */
	private PdfPCell setDataCell(PdfPCell cell, String phrase) {
		cell.setPhrase(new Phrase(phrase, font));
		cell.setBorderWidthBottom(0.75f);
		return cell;
	}

	/**
	 * Formats a cell to display information provided in a slightly larger font and followed by a colon.
	 * @param cell the cell to format
	 * @param phrase the information to display
	 * @return the formatted cell
	 */
	private PdfPCell setInfoCell(PdfPCell cell, String phrase) {
		cell.setPhrase(new Phrase(phrase + ":", infoFont));
		cell.setBorder(0);
		return cell;
	}

	/**
	 * Formats a cell to display on the same line both the information style (larger text followed by colon) and
	 * the data in a normal font on the same line.
	 * @param cell the cell to format
	 * @param info the information heading
	 * @param data the data value
	 * @return the formatted cell
	 */
	private PdfPCell setFooterCell(PdfPCell cell, String info, String data) {
		cell.setPhrase(new Phrase(String.format("%s %s %s", info, getlen(data) > 0 ? ":" : "", data)));
		cell.setBorder(0);
		return cell;
	}

	/**
	 * Returns a Consultation Request localized value for the key provided.
	 * @param key the key to reference
	 * @return the value for the key provided
	 */
	private String getResource(String key) {
		return oscarR.getString("oscarEncounter.oscarConsultationRequest.consultationFormPrint." + key);
	}


	/**
	 * Returns the length of the string provided and 0 if the string is null.
	 * @param str the string to check
	 * @return the length of str
	 */
	private int getlen (String str){
		if (str == null)
	            return 0;
		return str.length();
	}

	/**
	 * Converts breaking lines and non-breaking spaces to their
	 * appropriate equivalents for displaying text in the PDF.
	 * @param str the string to modify
	 * @return the original string with all breaking lines replaced by '\n' and all non-breaking spaces replaced by ' '
	 */
	private String divy (String str){
		if (str == null) { return ""; }
	    str = str.replaceAll("<\\s*br\\s*/?>", "\n");
	    str = str.replaceAll("&nbsp;", " ");
		return str;
	}
}
