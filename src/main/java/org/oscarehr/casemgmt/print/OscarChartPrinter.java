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


package org.oscarehr.casemgmt.print;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.CustomFilter;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.SxmlMisc;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.DemographicRelationship;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This will create a PDF + assemble e-forms,documents,labs into a package
 *
 * @author Marc Dumontier
 *
 */
public class OscarChartPrinter {

    public final int LINESPACING = 1;
    public final float LEADING = 12;
    public final float FONTSIZE = 10;
    public final int NUMCOLS = 2;

    private Demographic demographic;
    private String signingProvider;

    private PdfWriter writer;

	private HttpServletRequest request;
    private OutputStream os;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private Document document;
    private BaseFont bf;
    private Font font, boldFont;
    boolean newPage;
    private PdfContentByte cb;

    private ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    private DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
    private DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
    private OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
    private PreventionDao preventionDao = (PreventionDao)SpringUtils.getBean("preventionDao");
    private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
    private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
    

	public OscarChartPrinter(HttpServletRequest request, OutputStream os) throws DocumentException,IOException {
		this.request = request;
		this.os = os;

		document = new Document();
		// writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);
		
	    writer = PdfWriter.getInstance(document,os);
		writer.setPageEvent(new EndPage());
		document.setPageSize(PageSize.LETTER);
		document.open();
		//Create the font we are going to print to
        bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, FONTSIZE, Font.NORMAL);
        boldFont = new Font(bf,FONTSIZE,Font.BOLD);
	}


    public HttpServletRequest getRequest() {
    	return request;
    }

    public OutputStream getOutputStream() {
    	return os;
    }

    public void setDemographic(Demographic demographic) {
    	this.demographic=demographic;
    }

    public Font getFont() {
    	return font;
    }
    public SimpleDateFormat getFormatter() {
    	return formatter;
    }

    public Document getDocument() {
    	return document;
    }

    public boolean getNewPage() {
    	return newPage;
    }
    public void setNewPage(boolean b) {
    	this.newPage = b;
    }

    public BaseFont getBaseFont() {
    	return bf;
    }
    private Paragraph getParagraph(String value) {
    	Paragraph p = new Paragraph(value,font);
    	return p;
    }

    public void finish() {
        document.close();
    }


	public void printDocHeaderFooter() throws DocumentException {
    	String headerTitle = demographic.getFormattedName() + " " + demographic.getAge() + " " + demographic.getSex() + " DOB:" + demographic.getFormattedDob();

    	//set up document title and header
        ResourceBundle propResource = ResourceBundle.getBundle("oscarResources");
        String title = propResource.getString("oscarEncounter.pdfPrint.title") + " " + (String)request.getAttribute("demoName") + "\n";
        String gender = propResource.getString("oscarEncounter.pdfPrint.gender") + " " + (String)request.getAttribute("demoSex") + "\n";
        String dob = propResource.getString("oscarEncounter.pdfPrint.dob") + " " + (String)request.getAttribute("demoDOB") + "\n";
        String age = propResource.getString("oscarEncounter.pdfPrint.age") + " " + (String)request.getAttribute("demoAge") + "\n";
        String mrp = propResource.getString("oscarEncounter.pdfPrint.mrp") + " " + (String)request.getAttribute("mrp") + "\n";
        String[] info = new String[] { title, gender, dob, age, mrp };

        ClinicData clinicData = new ClinicData();
        clinicData.refreshClinicData();
        String[] clinic = new String[] {clinicData.getClinicName(), clinicData.getClinicAddress(),
        clinicData.getClinicCity() + ", " + clinicData.getClinicProvince(),
        clinicData.getClinicPostal(), clinicData.getClinicPhone()};

        if( newPage ) {
            document.newPage();
            newPage=false;
        }

        //Header will be printed at top of every page beginning with p2
        Phrase headerPhrase = new Phrase(LEADING, headerTitle, boldFont);
        HeaderFooter header = new HeaderFooter(headerPhrase,false);
        header.setAlignment(HeaderFooter.ALIGN_CENTER);
        document.setHeader(header);

        getDocument().add(headerPhrase);
        getDocument().add(new Phrase("\n"));

        Paragraph p = new Paragraph("Tel:"+demographic.getPhone(),getFont());
        p.setAlignment(Paragraph.ALIGN_LEFT);
       // getDocument().add(p);

        Paragraph p2 = new Paragraph("Date of Visit: ",getFont());
        p2.setAlignment(Paragraph.ALIGN_RIGHT);
       // getDocument().add(p);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        PdfPCell cell1 = new PdfPCell(p);
        cell1.setBorder(PdfPCell.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell cell2 = new PdfPCell(p2);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);

        getDocument().add(table);

        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        cell1 = new PdfPCell(getParagraph("Signed Provider:" + ((signingProvider!=null)?signingProvider:"")));
        cell1.setBorder(PdfPCell.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2 = new PdfPCell(getParagraph("RFR:" ));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cell3 = new PdfPCell(getParagraph("Ref:"));
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);

        getDocument().add(table);

        //Write title with top and bottom borders on p1
        cb = writer.getDirectContent();
        cb.setColorStroke(new Color(0,0,0));
        cb.setLineWidth(0.5f);

        cb.moveTo(document.left(), document.top() - (font.getCalculatedLeading(LINESPACING)*5f));
        cb.lineTo(document.right(), document.top() - (font.getCalculatedLeading(LINESPACING)*5f));
        cb.stroke();
    }


	public void printMasterRecord() throws DocumentException {
		if( newPage ) {
            document.newPage();
            newPage=false;
        }

        Paragraph p = null;
        Phrase phrase = null;


        p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase = new Phrase(LEADING, "Patient Information", boldFont);
        phrase.add("\n");
        p.add(phrase);
        document.add(p);

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        phrase.add("Title: " + demographic.getTitle() + "\n");
        phrase.add("Last Name: " + demographic.getLastName() + "\n");
        phrase.add("First Name: " + demographic.getFirstName() + "\n");
        phrase.add("Gender: " + demographic.getSex() + "\n");
        phrase.add("Date of Birth: " + demographic.getFormattedDob() + "\n");
        phrase.add("Offical Language: " + demographic.getOfficialLanguage()  + "\n");
        phrase.add("Spoken Language: " + demographic.getSpokenLanguage() + "\n");
        phrase.add("\n");
        p.add(phrase);
        document.add(p);

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        phrase.add("Roster Status: " + demographic.getRosterStatus()+ "\n");
        phrase.add("Date Rostered: " + "\n");
        phrase.add("Patient Status: " + demographic.getPatientStatus() + "\n");
        phrase.add("Chart No (MRN): " + demographic.getChartNo() + "\n");
        if(demographic.getDateJoined()!=null) {
        	phrase.add("Date Joined: " + formatter.format(demographic.getDateJoined())  + "\n");
        }
        if(demographic.getEndDate() != null) {
        	phrase.add("End Date: " + formatter.format(demographic.getEndDate()) + "\n");
        }
        phrase.add("\n");
        p.add(phrase);
        document.add(p);

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        phrase.add("Address: " + demographic.getAddress() + "\n");
        phrase.add("City: " + demographic.getCity() + "\n");
        phrase.add("Province: " + demographic.getProvince() + "\n");
        phrase.add("Postal Code: " + demographic.getPostal() + "\n");
        phrase.add("Email: " + demographic.getEmail()  + "\n");
        phrase.add("Phone: " +  demographic.getPhone() + "\n");

        List<DemographicExt> exts = demographicExtDao.getDemographicExtByDemographicNo(demographic.getDemographicNo());
        String phoneExt = null;
        String cell = null;
        for(DemographicExt ext:exts) {
        	if(ext.getKey().equals("wPhoneExt")) {
        		phoneExt = ext.getValue();
        	}
        	if(ext.getKey().equals("demo_cell")) {
        		cell = ext.getValue();
        	}
        }

        phrase.add("Work Phone: " +  demographic.getPhone2());
        if(phoneExt != null) {
        	phrase.add(" ext:" + phoneExt + "\n");
        }else {
        	phrase.add("\n");
        }

        if(cell!=null) {
        	phrase.add("Cell Phone: " +  cell +  "\n");
        }

        phrase.add("\n");
        p.add(phrase);
        document.add(p);

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        phrase.add("Health Insurance #: " + demographic.getHin() + "\n");
        phrase.add("HC Type: " + demographic.getHcType() + "\n");
        if(demographic.getEffDate() != null) {
        	phrase.add("Eff Date: " + formatter.format(demographic.getEffDate())  + "\n");
        }
        phrase.add("\n");
        p.add(phrase);
        document.add(p);

        DemographicCust demographicCust = demographicCustDao.find(demographic.getDemographicNo());

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        phrase.add("Physician: " + getProviderName(demographic.getProviderNo()) + "\n");
        if(demographicCust != null) {
	        phrase.add("Nurse: " + getProviderName(demographicCust.getNurse()) + "\n");
	        phrase.add("Midwife: " + getProviderName(demographicCust.getMidwife()) + "\n");
	        phrase.add("Resident: " + getProviderName(demographicCust.getResident()) + "\n");
        }
        phrase.add("Referral Doctor: " + getReferralDoctor(demographic.getFamilyDoctor()) + "\n");
        phrase.add("\n");
        p.add(phrase);
        document.add(p);

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        //alerts & notes
        if(demographicCust != null) {
	        phrase.add("Alerts: " + demographicCust.getAlert() + "\n");
	        if(demographicCust.getNotes().length()>0) {
	        	phrase.add("Notes: " + SxmlMisc.getXmlContent(demographicCust.getNotes(),"unotes") + "\n");
	        }
        }
        phrase.add("\n");
        p.add(phrase);
        document.add(p);


        //relationships
        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);

        DemographicRelationship demoRel = new DemographicRelationship();
        
		List<Map<String,String>> demoR = demoRel.getDemographicRelationships(String.valueOf(demographic.getDemographicNo()));
		for (int j=0; j<demoR.size(); j++) {
		    Map<String,String> r = demoR.get(j);
		    String relationDemographicNo = r.get("demographic_no");
		    Demographic relationDemographic = demographicDao.getClientByDemographicNo(Integer.parseInt(relationDemographicNo));
		    String relation = r.get("relation");
		    String subDecisionMaker = r.get("sub_decision_maker");
		    String emergencyContact = r.get("emergency_contact");
		    String notes = r.get("notes");

		    phrase.add(relation + " - " + relationDemographic.getFormattedName() +  " - " + subDecisionMaker + " - " + emergencyContact + " - " + notes+ "\n");
		}
        phrase.add("\n");
        p.add(phrase);
        document.add(p);

	}

	private String getReferralDoctor(String field) {
		if(field!=null&&field.length()>0) {
			return SxmlMisc.getXmlContent(field,"rd") ;
		}
		return "";
	}

	private String getProviderName(String providerNo) {
		if(providerNo == null || providerNo.length()==0) {
			return "";
		}
		Provider p = providerDao.getProvider(providerNo);
		if(p != null) {
			return p.getFormattedName();
		}
		return "";
	}


	public void printAppointmentHistory() throws DocumentException {
		if( newPage ) {
            document.newPage();
            newPage=false;
        }

		List<Appointment> appts = appointmentDao.getAppointmentHistory(demographic.getDemographicNo());

		PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.addCell("Date");
        table.addCell("From");
        table.addCell("To");
        table.addCell("Reason");
        table.addCell("Provider");
        table.addCell("Notes");

        for(Appointment appt:appts) {
	        table.addCell(generalCellForApptHistory(formatter.format(appt.getAppointmentDate())));
	        table.addCell(generalCellForApptHistory(timeFormatter.format(appt.getStartTime())));
	        table.addCell(generalCellForApptHistory(timeFormatter.format(appt.getEndTime())));
	        table.addCell(generalCellForApptHistory(appt.getReason()));
	        table.addCell(generalCellForApptHistory(providerDao.getProvider(appt.getProviderNo()).getFormattedName()));
	        table.addCell(generalCellForApptHistory(appt.getNotes()));
        }

        getDocument().add(table);
	}


    public void printCPPItem(String heading, Collection<CaseManagementNote> notes) throws DocumentException {
        if( newPage )
            document.newPage();
      //  else
      //      newPage = true;

        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);

        Paragraph p = null;
        Phrase phrase = null;


        p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase = new Phrase(LEADING, heading, obsfont);
        p.add(phrase);
        document.add(p);
        newPage = false;
        this.printNotes(notes,true);


        cb.endText();

    }




    public void printNotes(Collection<CaseManagementNote>notes, boolean compact) throws DocumentException{

        CaseManagementNote note;
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Paragraph p;
        Phrase phrase;
        Chunk chunk;

        //if( newPage )
       //     document.newPage();
       // else
       //     newPage = true;

        //Print notes
        Iterator<CaseManagementNote> notesIter = notes.iterator();
        while(notesIter.hasNext()) {
        	note = notesIter.next();
            p = new Paragraph();
            //p.setSpacingBefore(font.leading(LINESPACING)*2f);
            phrase = new Phrase(LEADING, "", font);

            if(compact) {
            	phrase.add(new Chunk(formatter.format(note.getObservation_date()) + ":"));
            } else {
            	chunk = new Chunk("Impression/Plan: (" + formatter.format(note.getObservation_date()) + ")\n", obsfont);
            	phrase.add(chunk);
            }
            if(compact) {
            	phrase.add(note.getNote() + "\n");
            } else {
            	phrase.add(note.getNote() + "\n\n");
            }
            p.add(phrase);
            document.add(p);
        }
    }

    public void printBlankLine() throws DocumentException {
    	Paragraph p = new Paragraph();
    	p.add(new Phrase("\n"));
    	document.add(p);

    }

	private PdfPCell generalCellForApptHistory(String text) {
		Paragraph p = new Paragraph(text,getFont());
        p.setAlignment(Paragraph.ALIGN_LEFT);
        PdfPCell cell1 = new PdfPCell(p);
        cell1.setBorder(PdfPCell.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell1;

	}


    public void printAllergies(List<Allergy> allergies) throws DocumentException {

        Font obsfont = new Font(getBaseFont(), FONTSIZE, Font.UNDERLINE);

        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        Phrase phrase = new Phrase(LEADING, "\n", getFont());
        p.add(phrase);
        phrase = new Phrase(LEADING, "Allergies", obsfont);
        p.add(phrase);
        getDocument().add(p);

        for(Allergy allergy:allergies) {
        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk(allergy.getDescription());
    		phrase.add(chunk);
    		p.add(phrase);
    		getDocument().add(p);
        }
        getDocument().add(new Phrase("\n",getFont()));
    }




   public void printRx(String demoNo) throws DocumentException {
        printRx(demoNo,null);
    }
    public void printRx(String demoNo,List<CaseManagementNote> cpp) throws DocumentException {
        if( demoNo == null )
            return;
        /*
        if( newPage )
            document.newPage();
        else
            newPage = true;
        */
        oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
        oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
        arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));

        if(arr.length==0) {
        	return;
        }

        Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Prescriptions");
        p.add(phrase);
        document.add(p);

        Font normal = new Font(bf, FONTSIZE, Font.NORMAL);



        Font curFont;
        for(int idx = 0; idx < arr.length; ++idx ) {
            oscar.oscarRx.data.RxPrescriptionData.Prescription drug = arr[idx];
            p = new Paragraph();
            p.setAlignment(Paragraph.ALIGN_LEFT);
            if(drug.isCurrent() && !drug.isArchived() ){
                curFont = normal;
                phrase = new Phrase(LEADING, "", curFont);
                phrase.add(formatter.format(drug.getRxDate()) + " - ");
                phrase.add(drug.getFullOutLine().replaceAll(";", " "));
                p.add(phrase);
                document.add(p);
            }
        }

    }


    public void printPreventions() throws DocumentException {
        List<Prevention> preventions = preventionDao.findNotDeletedByDemographicId(demographic.getDemographicNo());

        if(preventions.size()==0) {
        	return;
        }

        Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Preventions");
        p.add(phrase);
        document.add(p);

        for(Prevention prevention:preventions) {
        	String type = prevention.getPreventionType();
        	Date date = prevention.getPreventionDate();

        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk(type + " " + formatter.format(date)+"\n");
    		phrase.add(chunk);
    		p.add(phrase);
    		getDocument().add(p);
        }

    }

    public void printTicklers(LoggedInInfo loggedInInfo) throws DocumentException {
    	CustomFilter filter = new CustomFilter();
    	filter.setDemographicNo(String.valueOf(demographic.getDemographicNo()));
    	List<Tickler> ticklers = ticklerManager.getTicklers(loggedInInfo, filter);

    	if(ticklers.size()==0) {
    		return;
    	}

    	Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Ticklers");
        p.add(phrase);
        document.add(p);

        for(Tickler tickler:ticklers) {
        	String providerName = tickler.getProvider().getFormattedName();
        	String assigneeName = tickler.getAssignee().getFormattedName();
        	String serviceDate = tickler.getServiceDateWeb();
        	String priority = tickler.getPriority().toString();
        	char status = tickler.getStatus().toString().charAt(0);
        	String message = tickler.getMessage();


        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk("Provider:" + providerName+ "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Assignee:" + assigneeName+ "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Service Date:" + serviceDate+ "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Priority:" + priority+ "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Status:" + status+ "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Message:" + message+ "\n\n");
    		phrase.add(chunk);

    		p.add(phrase);
    		getDocument().add(p);
        }

    }

    public void printDiseaseRegistry() throws DocumentException {
    	DxresearchDAO dxDao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");
    	IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    	List<Dxresearch> dxs = dxDao.getDxResearchItemsByPatient(demographic.getDemographicNo());

    	if(dxs.size()==0) {
    		return;
    	}
    	Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Disease Registry");
        p.add(phrase);
        document.add(p);

        for(Dxresearch dx:dxs) {
        	String codingSystem = dx.getCodingSystem();
        	String code = dx.getDxresearchCode();
        	Date startDate = dx.getStartDate();
        	char status = dx.getStatus();

        	Issue issue = issueDao.findIssueByTypeAndCode(codingSystem, code);


        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk("Start Date:" + formatter.format(startDate) + "\n");
    		phrase.add(chunk);
    		if(issue != null) {
    			chunk = new Chunk("Issue:" + issue.getDescription()+ "\n");
    			phrase.add(chunk);
    		} else {
    			chunk = new Chunk("Issue: <Unknown>"+ "\n");
    			phrase.add(chunk);
    		}
    		chunk = new Chunk("Status:" + status+ "\n\n");
			phrase.add(chunk);

    		p.add(phrase);
    		getDocument().add(p);
        }

    }

    public void printCurrentAdmissions() throws DocumentException {
    	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean("admissionDao");
    	ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");

    	List<Admission> admissions = admissionDao.getCurrentAdmissions(demographic.getDemographicNo());

    	if(admissions.size()==0) {
    		return;
    	}
    	Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Current Admissions");
        p.add(phrase);
        document.add(p);

        for(Admission admission:admissions) {
        	String admissionDate =admission.getAdmissionDate("yyyy-MM-dd");
        	String admissionNotes = admission.getAdmissionNotes();
        	String programName = programDao.getProgramName(admission.getProgramId());
        	String programType = admission.getProgramType();
        	String providerName = providerDao.getProvider(admission.getProviderNo()).getFormattedName();

        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk("Summary:" + programName + "(" + programType + ")" + " by " + providerName + " on " + admissionDate + "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Admission Notes:" + admissionNotes+ "\n\n");
			phrase.add(chunk);

    		p.add(phrase);
    		getDocument().add(p);
        }

    }

    public void printPastAdmissions() throws DocumentException {
    	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean("admissionDao");
    	ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    	List<Admission> admissions = admissionDao.getAdmissions(demographic.getDemographicNo());
    	admissions = filterOutCurrentAdmissions(admissions);

    	if(admissions.size()==0) {
    		return;
    	}
    	Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Past Admissions");
        p.add(phrase);
        document.add(p);

        for(Admission admission:admissions) {
        	String admissionDate =admission.getAdmissionDate("yyyy-MM-dd");
        	String admissionNotes = admission.getAdmissionNotes();
        	String programName = programDao.getProgramName(admission.getProgramId());
        	String programType = admission.getProgramType();
        	String providerName = providerDao.getProvider(admission.getProviderNo()).getFormattedName();
        	String dischargeDate = formatter.format(admission.getDischargeDate());
        	String dischargeNotes = admission.getDischargeNotes();

        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk("Summary:" + programName + "(" + programType + ")" + " by " + providerName + " on " + admissionDate + "\n");
    		phrase.add(chunk);
    		chunk = new Chunk("Admission Notes:" + admissionNotes+ "\n\n");
			phrase.add(chunk);
			chunk = new Chunk("Discharged on " + dischargeDate + ", Notes:" + dischargeNotes+ "\n\n");
			phrase.add(chunk);

    		p.add(phrase);
    		getDocument().add(p);
        }

    }

    public void printCurrentIssues() throws  DocumentException {
    	CaseManagementIssueDAO cmIssueDao = (CaseManagementIssueDAO)SpringUtils.getBean("CaseManagementIssueDAO");

    	List<CaseManagementIssue> issues = cmIssueDao.getIssuesByDemographic(String.valueOf(demographic.getDemographicNo()));

    	Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        phrase.add("Current Issues");
        p.add(phrase);
        document.add(p);

        for(CaseManagementIssue issue:issues) {
        	String description = issue.getIssue().getDescription();

        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		Chunk chunk = new Chunk(description + "\n\n");
    		phrase.add(chunk);

    		p.add(phrase);
    		getDocument().add(p);
        }

    }

    private List<Admission> filterOutCurrentAdmissions(List<Admission> admissions) {
    	List<Admission> results = new ArrayList<Admission>();
    	for(Admission a:admissions) {
    		if(!a.getAdmissionStatus().equals("current")) {
    			results.add(a);
    		}
    	}
    	return results;
    }

    /*
     *Used to print footers on each page
     */
    class EndPage extends PdfPageEventHelper {
        private Date now;
        private String promoTxt;

        public EndPage() {
            now = new Date();
            promoTxt = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
            if( promoTxt == null ) {
                promoTxt = new String();
            }
        }

        public void onEndPage( PdfWriter writer, Document document ) {
            //Footer contains page numbers and date printed on all pages
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();

            String strFooter = promoTxt + " " + formatter.format(now);

            float textBase = document.bottom();
            cb.beginText();
            cb.setFontAndSize(font.getBaseFont(),FONTSIZE);
            Rectangle page = document.getPageSize();
            float width = page.getWidth();

            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, strFooter, (width/2.0f), textBase - 20, 0);

            strFooter = "-" + writer.getPageNumber() + "-";
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, strFooter, (width/2.0f), textBase-10, 0);

            cb.endText();
            cb.restoreState();
        }
    }


}
