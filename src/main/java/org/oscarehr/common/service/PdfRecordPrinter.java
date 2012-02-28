package org.oscarehr.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.eyeform.MeasurementFormatter;
import org.oscarehr.eyeform.model.EyeForm;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.eyeform.model.EyeformOcularProcedure;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.eyeform.model.EyeformSpecsHistory;
import org.oscarehr.eyeform.model.EyeformTestBook;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.eform.util.GraphicalCanvasToImage;
import oscar.oscarClinic.ClinicData;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;

import com.lowagie.text.Chunk;
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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfRecordPrinter {

    private static Logger logger = MiscUtils.getLogger();

    private HttpServletRequest request;
    private OutputStream os;

    private Document document;
    //private PdfContentByte cb;
    private BaseFont bf;
    private Font font, boldFont;
    private boolean newPage = false;

    private SimpleDateFormat formatter;

    public final int LINESPACING = 1;
    public final float LEADING = 12;
    public final float FONTSIZE = 10;
    public final int NUMCOLS = 2;

    private Demographic demographic;
    private Appointment appointment;
    private String signingProvider;

    private PdfWriter writer;

    public PdfRecordPrinter(HttpServletRequest request, OutputStream os) throws DocumentException,IOException {
        this.request = request;
        this.os = os;
        formatter = new SimpleDateFormat("dd-MMM-yyyy");

        //Create the font we are going to print to
        bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, FONTSIZE, Font.NORMAL);
        boldFont = new Font(bf,FONTSIZE,Font.BOLD);

        //Create the document we are going to write to
        document = new Document();
        writer = PdfWriter.getInstance(document,os);
        writer.setPageEvent(new EndPage());
        writer.setStrictImageSequence(true);

        document.setPageSize(PageSize.LETTER);

        /*
        HeaderFooter footer = new HeaderFooter(new Phrase("-",font),new Phrase("-",font));
        footer.setAlignment(HeaderFooter.ALIGN_CENTER);
        footer.setBorder(0);

        document.setFooter(footer);
*/
        document.open();


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

    public void setAppointment(Appointment appointment) {
    	this.appointment=appointment;
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

    public String getSigningProvider() {
    	return signingProvider;
    }

	public void setSigningProvider(String signingProvider) {
    	this.signingProvider = signingProvider;
    }

	public void footer() {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();

        Date now = new Date();
        String promoTxt = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
        if( promoTxt == null ) {
            promoTxt = new String();
        }

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
	public void printDocHeaderFooter() throws DocumentException {
		document.resetHeader();
		document.resetFooter();
		//document.resetPageCount();
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
/*
        HeaderFooter header = new HeaderFooter(headerPhrase,false);
        header.setAlignment(HeaderFooter.ALIGN_CENTER);
        document.setHeader(header);
*/
        getDocument().add(headerPhrase);
        getDocument().add(new Phrase("\n"));

        Paragraph p = new Paragraph("Tel:"+demographic.getPhone(),getFont());
        p.setAlignment(Paragraph.ALIGN_LEFT);
       // getDocument().add(p);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Paragraph p2 = new Paragraph("Date of Visit: " + sdf.format(appointment.getAppointmentDate()),getFont());
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
        cell1.setBorder(PdfPCell.BOTTOM);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2 = new PdfPCell(getParagraph("RFR:" + this.appointment.getReason()));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBorder(PdfPCell.BOTTOM);
        PdfPCell cell3 = new PdfPCell(getParagraph("Ref:" + this.getRefName(this.demographic)));
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setBorder(PdfPCell.BOTTOM);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        getDocument().add(table);

        /*
        HeaderFooter footer = new HeaderFooter(new Phrase("-",font),new Phrase("-",font));
        footer.setAlignment(HeaderFooter.ALIGN_CENTER);
        footer.setBorder(0);

        document.setFooter(footer);
*/

        //Write title with top and bottom borders on p1


       // cb = writer.getDirectContent();
        /*
        cb.setColorStroke(new Color(0,0,0));
        cb.setLineWidth(0.5f);
        * 
        cb.moveTo(document.left(), document.top() - (font.getCalculatedLeading(LINESPACING)*5f));
        cb.lineTo(document.right(), document.top() - (font.getCalculatedLeading(LINESPACING)*5f));
        cb.stroke();     
        */
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

        if (cpp != null ){
            List<CaseManagementNote>notes = cpp;
            if (notes != null && notes.size() > 0){
                p = new Paragraph();
                p.setAlignment(Paragraph.ALIGN_LEFT);
                phrase = new Phrase(LEADING, "\nOther Meds\n", obsfont); //TODO:Needs to be i18n
                p.add(phrase);
                document.add(p);
                newPage = false;
                this.printNotes(notes);
            }

        }
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


       // cb.endText();

    }

    public void printCPPItem(String heading, Measurements measurement) throws DocumentException {
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

        p = new Paragraph();
        phrase = new Phrase(LEADING, "", font);
        //phrase.add(new Chunk(formatter.format(measurement.getDateObserved()) + ":"));
        phrase.add(measurement.getDataField() + "\n");
        p.add(phrase);
        document.add(p);

       // cb.endText();

    }

    public void printBlankLine() throws  DocumentException {
    	document.add(new Phrase("\n"));
    }

    public void printCPP(HashMap<String,List<CaseManagementNote> >cpp) throws DocumentException {
        if( cpp == null )
            return;

        if( newPage )
            document.newPage();
        else
            newPage = true;

        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);




        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_CENTER);
        Phrase phrase = new Phrase(LEADING, "\n\n", font);
        p.add(phrase);
        phrase = new Phrase(LEADING, "Patient CPP", obsfont);
        p.add(phrase);
        document.add(p);
        //upperYcoord -= p.leading() * 2f;
        //lworkingYcoord = rworkingYcoord = upperYcoord;
        //ColumnText ct = new ColumnText(cb);
        String[] headings = {"Social History\n","Other Meds\n", "Medical History\n", "Ongoing Concerns\n", "Reminders\n", "Family History\n", "Risk Factors\n"};
        String[] issueCodes = {"SocHistory","OMeds","MedHistory","Concerns","Reminders","FamHistory","RiskFactors"};
        //String[] content = {cpp.getSocialHistory(), cpp.getFamilyHistory(), cpp.getMedicalHistory(), cpp.getOngoingConcerns(), cpp.getReminders()};

        //init column to left side of page
        //ct.setSimpleColumn(document.left(), document.bottomMargin()+25f, document.right()/2f, lworkingYcoord);

        //int column = 1;
        //Chunk chunk;
        //float bottom = document.bottomMargin()+25f;
        //float middle;
        //bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        //cb.beginText();
        //String headerContd;
        //while there are cpp headings to process

        for( int idx = 0; idx < headings.length; ++idx ) {
            p = new Paragraph();
            p.setAlignment(Paragraph.ALIGN_LEFT);
            phrase = new Phrase(LEADING, headings[idx], obsfont);
            p.add(phrase);
            document.add(p);
            newPage = false;
            this.printNotes(cpp.get(issueCodes[idx]));
        }
            //phrase.add(content[idx]);
            //ct.addText(phrase);

//            //do we need a page break?  check if we're within a fudge factor of the bottom
//            if( lworkingYcoord <= (bottom * 1.1) && rworkingYcoord <= (bottom*1.1) ) {
//                document.newPage();
//                rworkingYcoord = lworkingYcoord = document.top();
//            }
//
//            //Are we in right column?  if so, flip over to left column if there is room
//            if( column % 2 == 1 ) {
//                if( lworkingYcoord > bottom ) {
//                    ct.setSimpleColumn(document.left(), bottom, (document.right()/2f)-10f, lworkingYcoord);
//                    ++column;
//                }
//            }
//            //Are we in left column?  if so, flip over to right column only if text will fit
//            else {
//                ct.setSimpleColumn((document.right()/2f)+10f, bottom, document.right(), rworkingYcoord);
//
//                if( ct.go(true) == ColumnText.NO_MORE_COLUMN ) {
//                    ct.setSimpleColumn(document.left(), bottom, (document.right()/2f)-10f, lworkingYcoord);
//                }
//                else {
//                    ct.setYLine(rworkingYcoord);
//                    ++column;
//                }
//
//                //ct.go(true) consumes input so we reload
//                phrase = new Phrase(LEADING, "", font);
//                chunk = new Chunk(headings[idx], obsfont);
//                phrase.add(chunk);
//                phrase.add(content[idx]);
//                ct.setText(phrase);
//            }
//
//            //while there is text to write, fill columns/page break when page full
//            while( ct.go() == ColumnText.NO_MORE_COLUMN ) {
//                if( column % 2 == 0 ) {
//                    lworkingYcoord = bottom;
//                    middle = (document.right()/4f)*3f;
//                    headerContd = headings[idx] + " cont'd";
//                    cb.setFontAndSize(bf, FONTSIZE);
//                    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, headerContd, middle, rworkingYcoord-phrase.leading(), 0f);
//                    //cb.showTextAligned(PdfContentByte.ALIGN_CENTER, headings[idx] + " cont'd", middle, rworkingYcoord, 0f);
//                    rworkingYcoord -= phrase.leading();
//                    ct.setSimpleColumn((document.right()/2f)+10f, bottom, document.right(), rworkingYcoord);
//                }
//                else {
//                    document.newPage();
//                    rworkingYcoord = lworkingYcoord = document.top();
//                    middle = (document.right()/4f);
//                    headerContd = headings[idx] + " cont'd";
//                    cb.setFontAndSize(bf, FONTSIZE);
//                    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, headerContd, middle, lworkingYcoord-phrase.leading(), 0f);
//                    lworkingYcoord -= phrase.leading();
//                    ct.setSimpleColumn(document.left(), bottom, (document.right()/2f)-10f, lworkingYcoord);
//                }
//                ++column;
//            }
//
//            if( column % 2 == 0 )
//                lworkingYcoord -= (ct.getLinesWritten() * ct.getLeading() + (ct.getLeading() * 2f));
//            else
//                rworkingYcoord -= (ct.getLinesWritten() * ct.getLeading() + (ct.getLeading() * 2f));
//        }
//        cb.endText();
    }

    public void printNotes(Collection<CaseManagementNote>notes) throws DocumentException{
        printNotes(notes,false);
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

    public void finish() {
        document.close();
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

    public void printOcularProcedures(List<EyeformOcularProcedure> ocularProcedures) throws DocumentException {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

 /*
		if( getNewPage() )
            getDocument().newPage();
        else
            setNewPage(true);
*/

        Font obsfont = new Font(getBaseFont(), FONTSIZE, Font.UNDERLINE);


        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        Phrase phrase = new Phrase(LEADING, "\n", getFont());
        p.add(phrase);
        phrase = new Phrase(LEADING, "Ocular Procedures", obsfont);
        p.add(phrase);
        getDocument().add(p);

        for(EyeformOcularProcedure proc:ocularProcedures) {
        	p = new Paragraph();
    		phrase = new Phrase(LEADING, "", getFont());
    		//Chunk chunk = new Chunk("Documentation Date: " + getFormatter().format(proc.getDate()) + "\n", obsfont);
    		Chunk chunk = new Chunk(getFormatter().format(proc.getDate()) + " " + proc.getEye() + " " + proc.getProcedureName() + " at " + proc.getLocation() + " by " + providerDao.getProviderName(proc.getDoctor()) + " " + proc.getProcedureNote() + "\n",getFont());
    		phrase.add(chunk);
    		p.add(phrase);
    		getDocument().add(p);
        }
    }

    public void printSpecsHistory(List<EyeformSpecsHistory> specsHistory) throws DocumentException {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

 /*
		if( getNewPage() )
            getDocument().newPage();
        else
            setNewPage(true);
*/

        Font obsfont = new Font(getBaseFont(), FONTSIZE, Font.UNDERLINE);


        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        Phrase phrase = new Phrase(LEADING, "\n", getFont());
        p.add(phrase);
        phrase = new Phrase(LEADING, "Specs History", obsfont);
        p.add(phrase);
        getDocument().add(p);

        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
    	table.setTotalWidth(new float[]{10f,60f});
        table.setTotalWidth(5f);
    	table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);

        for(EyeformSpecsHistory specs:specsHistory) {
    		PdfPCell cell1 = new PdfPCell(new Phrase(getFormatter().format(specs.getDate()),getFont()));
            cell1.setBorder(PdfPCell.NO_BORDER);
            cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
    		table.addCell(cell1);

    		PdfPCell cell2 = new PdfPCell(new Phrase(specs.toString2(),getFont()));
            cell2.setBorder(PdfPCell.NO_BORDER);
            cell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
    		table.addCell(cell2);
        }

        getDocument().add(table);
    }

    public void printAllergies(List<Allergy> allergies) throws  DocumentException {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

 /*
		if( getNewPage() )
            getDocument().newPage();
        else
            setNewPage(true);
 */
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

    public void printEyeformPlan(List<EyeformFollowUp>followUps, List<EyeformProcedureBook> procedureBooks, List<EyeformTestBook>testBooks,EyeForm eyeform) throws DocumentException {

    	for(EyeformFollowUp followUp:followUps) {
        	Paragraph p = new Paragraph();
    		p.add(getFormatter().format(followUp.getDate()) + ":" + followUp.getType()+ " " + followUp.getTimespan() + " " + followUp.getTimeframe() + " " + followUp.getFollowupProvider() + " " + followUp.getUrgency() + "\n");
    		getDocument().add(p);
        }

    	for(EyeformProcedureBook proc:procedureBooks) {
        	Paragraph p = new Paragraph();
    		p.add(getFormatter().format(proc.getDate()) + ":" + proc.getProcedureName() + "\n");
    		getDocument().add(p);
        }

    	for(EyeformTestBook test:testBooks) {
        	Paragraph p = new Paragraph();
    		p.add(getFormatter().format(test.getDate()) + ":" + test.getTestname() + "\n");
    		getDocument().add(p);
        }

    	if(eyeform != null && eyeform.getDischarge()!=null && eyeform.getDischarge().equals("true")) {
    		Paragraph p = new Paragraph();
    		p.add("Patient is discharged from my active care.\n");
    		getDocument().add(p);
    	}

    	if(eyeform != null && eyeform.getOpt()!=null && eyeform.getOpt().equals("true")) {
    		Paragraph p = new Paragraph();
    		p.add("Routine eye care by an optometrist is recommended.\n");
    		getDocument().add(p);
    	}

    	if(eyeform != null && eyeform.getStat()!=null && eyeform.getStat().equals("true")) {
    		Paragraph p = new Paragraph();
    		p.add("Follow up as needed with me STAT or PRN if symptoms are worse.\n");
    		getDocument().add(p);
    	}
    	return;
    }

    public void printEyeformMeasurements(MeasurementFormatter mf) throws DocumentException {
    	/*
		if( getNewPage() )
            getDocument().newPage();
        else
            setNewPage(true);
        */

        Font obsfont = new Font(getBaseFont(), FONTSIZE, Font.UNDERLINE);


        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        Phrase phrase = new Phrase(LEADING, "\n", getFont());
       // p.add(phrase);
        phrase = new Phrase(LEADING, "Ocular Examination", obsfont);
        p.add(phrase);
        getDocument().add(p);


        p = new Paragraph();
        boolean addVisionAssessment = false;
        p.add(new Phrase("VISION ASSESSMENT:  ",getFont()));
        if(mf.getVisionAssessmentAutoRefraction().length()>0) {
        	p.add(new Phrase("Auto-refraction ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentAutoRefraction(),getFont()));
        	addVisionAssessment=true;
        }
        if(mf.getVisionAssessmentKeratometry().length()>0) {
        	p.add(new Phrase(" Keratometry ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentKeratometry(),getFont()));
        	addVisionAssessment=true;
        }
        if(mf.getVisionAssessmentVision("distance", "sc").length()>0) {
        	p.add(new Phrase(" Distance vision (sc) ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentVision("distance", "sc"),getFont()));
        	addVisionAssessment=true;
        }
        if(mf.getVisionAssessmentVision("distance", "cc").length()>0) {
        	p.add(new Phrase(" Distance vision (cc) ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentVision("distance", "cc"),getFont()));
        	addVisionAssessment=true;
        }
        if(mf.getVisionAssessmentVision("distance", "ph").length()>0) {
        	p.add(new Phrase(" Distance vision (ph) ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentVision("distance", "ph"),getFont()));
        	addVisionAssessment=true;
        }
        if(mf.getVisionAssessmentVision("near", "sc").length()>0) {
        	p.add(new Phrase(" Near vision (sc) ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentVision("near", "sc"),getFont()));
        	addVisionAssessment=true;
        }
        if(mf.getVisionAssessmentVision("near", "cc").length()>0) {
        	p.add(new Phrase(" Near vision (cc) ",boldFont));
        	p.add(new Phrase(mf.getVisionAssessmentVision("near", "cc"),getFont()));
        	addVisionAssessment=true;
        }
        p.add(new Phrase("\n\n"));
        if(addVisionAssessment) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addManifestVision=false;
        p.add(new Phrase("MANIFEST VISION:  ",getFont()));
        if(mf.getManifestDistance().length()>0) {
        	p.add(new Phrase("Manifest distance ",boldFont));
        	p.add(new Phrase(mf.getManifestDistance(),getFont()));
        	addManifestVision=true;
        }
        if(mf.getManifestNear().length()>0) {
        	p.add(new Phrase(" Manifest near ",boldFont));
        	p.add(new Phrase(mf.getManifestNear(),getFont()));
        	addManifestVision=true;
        }
        if(mf.getCycloplegicRefraction().length()>0) {
        	p.add(new Phrase(" Cycloplegic refraction ",boldFont));
        	p.add(new Phrase(mf.getCycloplegicRefraction(),getFont()));
        	addManifestVision=true;
        }
        p.add(new Phrase("\n\n"));
        if(addManifestVision) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addIop=false;
        p.add(new Phrase("INTRAOCULAR PRESSURE:  ",getFont()));
        if(mf.getNCT().length()>0) {
        	p.add(new Phrase("NCT ",boldFont));
        	p.add(new Phrase(mf.getNCT(),getFont()));
        	addIop=true;
        }
        if(mf.getApplanation().length()>0) {
        	p.add(new Phrase(" Applanation ",boldFont));
        	p.add(new Phrase(mf.getApplanation(),getFont()));
        	addIop=true;
        }
        if(mf.getCCT().length()>0) {
        	p.add(new Phrase(" Central corneal thickness ",boldFont));
        	p.add(new Phrase(mf.getCCT(),getFont()));
        	addIop=true;
        }
        p.add(new Phrase("\n\n"));
        if(addIop) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addOtherExam=false;
        p.add(new Phrase("OTHER EXAM:  ",getFont()));
        if(mf.getColourVision().length()>0) {
        	p.add(new Phrase("Colour vision ",boldFont));
        	p.add(new Phrase(mf.getColourVision(),getFont()));
        	addOtherExam=true;
        }
        if(mf.getPupil().length()>0) {
        	p.add(new Phrase(" Pupil ",boldFont));
        	p.add(new Phrase(mf.getPupil(),getFont()));
        	addOtherExam=true;
        }
        if(mf.getAmslerGrid().length()>0) {
        	p.add(new Phrase(" Amsler grid ",boldFont));
        	p.add(new Phrase(mf.getAmslerGrid(),getFont()));
        	addOtherExam=true;
        }
        if(mf.getPAM().length()>0) {
        	p.add(new Phrase(" Potential acuity meter ",boldFont));
        	p.add(new Phrase(mf.getPAM(),getFont()));
        	addOtherExam=true;
        }
        if(mf.getConfrontation().length()>0) {
        	p.add(new Phrase(" Confrontation fields ",boldFont));
        	p.add(new Phrase(mf.getConfrontation(),getFont()));
        	addOtherExam=true;
        }
        p.add(new Phrase("\n\n"));
        if(addOtherExam) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addEom=false;
        p.add(new Phrase("EOM/STEREO:  ",getFont()));
        if(mf.getEomStereo().length()>0) {
        	p.add(new Phrase(mf.getEomStereo(),getFont()));
        	addEom=true;
        }
        p.add(new Phrase("\n\n"));
        if(addEom) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addAnteriorSegment=false;
        p.add(new Phrase("ANTERIOR SEGMENT:  ",getFont()));
        if(mf.getCornea().length()>0) {
        	p.add(new Phrase("Cornea ",boldFont));
        	p.add(new Phrase(mf.getCornea(),getFont()));
        	addAnteriorSegment=true;
        }
        if(mf.getConjuctivaSclera().length()>0) {
        	p.add(new Phrase(" Conjunctiva/Sclera ",boldFont));
        	p.add(new Phrase(mf.getConjuctivaSclera(),getFont()));
        	addAnteriorSegment=true;
        }
        if(mf.getAnteriorChamber().length()>0) {
        	p.add(new Phrase(" Anterior chamber ",boldFont));
        	p.add(new Phrase(mf.getAnteriorChamber(),getFont()));
        	addAnteriorSegment=true;
        }
        if(mf.getAngle().length()>0) {
        	p.add(new Phrase(" Angle ",boldFont));
        	p.add(new Phrase(mf.getAngle(),getFont()));
        	addAnteriorSegment=true;
        }
        if(mf.getIris().length()>0) {
        	p.add(new Phrase(" Iris ",boldFont));
        	p.add(new Phrase(mf.getIris(),getFont()));
        	addAnteriorSegment=true;
        }
        if(mf.getLens().length()>0) {
        	p.add(new Phrase(" Lens ",boldFont));
        	p.add(new Phrase(mf.getLens(),getFont()));
        	addAnteriorSegment=true;
        }
        p.add(new Phrase("\n\n"));
        if(addAnteriorSegment) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addPosteriorSegment=false;
        p.add(new Phrase("POSTERIOR SEGMENT:  ",getFont()));
        if(mf.getDisc().length()>0) {
        	p.add(new Phrase("Optic disc ",boldFont));
        	p.add(new Phrase(mf.getDisc(),getFont()));
        	addPosteriorSegment=true;
        }
        if(mf.getCdRatio().length()>0) {
        	p.add(new Phrase(" C/D ratio ",boldFont));
        	p.add(new Phrase(mf.getCdRatio(),getFont()));
        	addPosteriorSegment=true;
        }
        if(mf.getMacula().length()>0) {
        	p.add(new Phrase(" Macula ",boldFont));
        	p.add(new Phrase(mf.getMacula(),getFont()));
        	addPosteriorSegment=true;
        }
        if(mf.getRetina().length()>0) {
        	p.add(new Phrase(" Retina ",boldFont));
        	p.add(new Phrase(mf.getRetina(),getFont()));
        	addPosteriorSegment=true;
        }
        if(mf.getVitreous().length()>0) {
        	p.add(new Phrase(" Vitreous ",boldFont));
        	p.add(new Phrase(mf.getVitreous(),getFont()));
        	addPosteriorSegment=true;
        }
        p.add(new Phrase("\n\n"));
        if(addPosteriorSegment) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addExternal=false;
        p.add(new Phrase("EXTERNAL/ORBIT:  ",getFont()));
        if(mf.getFace().length()>0) {
        	p.add(new Phrase("Face ",boldFont));
        	p.add(new Phrase(mf.getFace(),getFont()));
        	addExternal=true;
        }
        if(mf.getUpperLid().length()>0) {
        	p.add(new Phrase(" Upper lid ",boldFont));
        	p.add(new Phrase(mf.getUpperLid(),getFont()));
        	addExternal=true;
        }
        if(mf.getLowerLid().length()>0) {
        	p.add(new Phrase(" Lower lid ",boldFont));
        	p.add(new Phrase(mf.getLowerLid(),getFont()));
        	addExternal=true;
        }
        if(mf.getPunctum().length()>0) {
        	p.add(new Phrase(" Punctum ",boldFont));
        	p.add(new Phrase(mf.getPunctum(),getFont()));
        	addExternal=true;
        }
        if(mf.getLacrimalLake().length()>0) {
        	p.add(new Phrase(" Lacrimal lake ",boldFont));
        	p.add(new Phrase(mf.getLacrimalLake(),getFont()));
        	addExternal=true;
        }
        if(mf.getRetropulsion().length()>0) {
        	p.add(new Phrase(" Retropulsion ",boldFont));
        	p.add(new Phrase(mf.getRetropulsion(),getFont()));
        	addExternal=true;
        }
        if(mf.getHertel().length()>0) {
        	p.add(new Phrase(" Hertel ",boldFont));
        	p.add(new Phrase(mf.getHertel(),getFont()));
        	addExternal=true;
        }
        p.add(new Phrase("\n\n"));
        if(addExternal) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addNasolacrimal=false;
        p.add(new Phrase("NASOLACRIMAL DUCT:  ",getFont()));
        if(mf.getLacrimalIrrigation().length()>0) {
        	p.add(new Phrase("Lacrimal irrigation ",boldFont));
        	p.add(new Phrase(mf.getLacrimalIrrigation(),getFont()));
        	addNasolacrimal=true;
        }
        if(mf.getNLD().length()>0) {
        	p.add(new Phrase(" Nasolacrimal duct ",boldFont));
        	p.add(new Phrase(mf.getNLD(),getFont()));
        	addNasolacrimal=true;
        }
        if(mf.getDyeDisappearance().length()>0) {
        	p.add(new Phrase(" Dye disappearance ",boldFont));
        	p.add(new Phrase(mf.getDyeDisappearance(),getFont()));
        	addNasolacrimal=true;
        }
        p.add(new Phrase("\n\n"));
        if(addNasolacrimal) {
        	getDocument().add(p);
        }

        p = new Paragraph();
        boolean addEyelidMeasurement=false;
        p.add(new Phrase("EYELID MEASUREMENT:  ",getFont()));
        if(mf.getMarginReflexDistance().length()>0) {
        	p.add(new Phrase("Margin reflex distance ",boldFont));
        	p.add(new Phrase(mf.getMarginReflexDistance(),getFont()));
        	addEyelidMeasurement=true;
        }
        if(mf.getLevatorFunction().length()>0) {
        	p.add(new Phrase(" Levator function ",boldFont));
        	p.add(new Phrase(mf.getLevatorFunction(),getFont()));
        	addEyelidMeasurement=true;
        }
        if(mf.getInferiorScleralShow().length()>0) {
        	p.add(new Phrase(" Inferior scleral show ",boldFont));
        	p.add(new Phrase(mf.getInferiorScleralShow(),getFont()));
        	addEyelidMeasurement=true;
        }
        if(mf.getLagophthalmos().length()>0) {
        	p.add(new Phrase(" Lagophthalmos ",boldFont));
        	p.add(new Phrase(mf.getLagophthalmos(),getFont()));
        	addEyelidMeasurement=true;
        }
        if(mf.getBlink().length()>0) {
        	p.add(new Phrase(" Blink ",boldFont));
        	p.add(new Phrase(mf.getBlink(),getFont()));
        	addEyelidMeasurement=true;
        }
        if(mf.getCNVii().length()>0) {
        	p.add(new Phrase(" Cranial Nerve VII function ",boldFont));
        	p.add(new Phrase(mf.getCNVii(),getFont()));
        	addEyelidMeasurement=true;
        }
        if(mf.getBells().length()>0) {
        	p.add(new Phrase(" Bell's phenonmenon ",boldFont));
        	p.add(new Phrase(mf.getBells(),getFont()));
        	addEyelidMeasurement=true;
        }
        p.add(new Phrase("\n\n"));
        if(addEyelidMeasurement) {
        	getDocument().add(p);
        }

    }

    public void printPhotos(String contextPath, List<org.oscarehr.common.model.Document> photos) throws DocumentException {
    	writer.setStrictImageSequence(true);

    	if(photos.size()>0) {
	    	Font obsfont = new Font(getBaseFont(), FONTSIZE, Font.UNDERLINE);
	        Paragraph p = new Paragraph();
	        p.setAlignment(Paragraph.ALIGN_LEFT);
	        Phrase phrase = new Phrase(LEADING, "\n\n", getFont());
	        p.add(phrase);
	        phrase = new Phrase(LEADING, "Photos:", obsfont);
	        p.add(phrase);
	        getDocument().add(p);
    	}

    	for(org.oscarehr.common.model.Document doc:photos) {
    		Image img = null;
    		try {
    			String location = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR").trim() + doc.getDocfilename();
    			logger.info("adding image " + location);
    			img = Image.getInstance(location);
    		} catch(IOException e) {
    			MiscUtils.getLogger().error("error:",e);
    			continue;
    		}
    		img.scaleToFit(getDocument().getPageSize().getWidth()-getDocument().leftMargin()-getDocument().rightMargin(), getDocument().getPageSize().getHeight());
    		
    		Chunk chunk = new Chunk(img,getDocument().getPageSize().getWidth()-getDocument().leftMargin()-getDocument().rightMargin(), getDocument().getPageSize().getHeight());
    		
    		Paragraph p = new Paragraph(); 
    		p.add(img);    		
    		p.add(new Phrase("Description:"+doc.getDocdesc(),getFont()));
    		getDocument().add(p);


        }
    }

    public void printDiagrams(List<EFormValue> diagrams) throws DocumentException {
    	writer.setStrictImageSequence(true);

    	EFormValueDao eFormValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");

        if(diagrams.size()>0) {
        	Font obsfont = new Font(getBaseFont(), FONTSIZE, Font.UNDERLINE);
	        Paragraph p = new Paragraph();
	        p.setAlignment(Paragraph.ALIGN_LEFT);
	        Phrase phrase = new Phrase(LEADING, "\n\n", getFont());
	        p.add(phrase);
	        phrase = new Phrase(LEADING, "Diagrams:", obsfont);
	        p.add(phrase);
	        getDocument().add(p);
        }

        for(EFormValue value:diagrams) {
	    	//this is a form from our group, and our appt
	    	String imgPath = OscarProperties.getInstance().getProperty("eform_image");
	    	EFormValue imageName = eFormValueDao.findByFormDataIdAndKey(value.getFormDataId(),"image");
	    	EFormValue drawData = eFormValueDao.findByFormDataIdAndKey(value.getFormDataId(),"DrawData");
	    	EFormValue subject = eFormValueDao.findByFormDataIdAndKey(value.getFormDataId(),"subject");

	    	String image = imgPath + File.separator + imageName.getVarValue();
	    	logger.debug("image for eform is " + image);
	    	GraphicalCanvasToImage convert = new GraphicalCanvasToImage();
	    	File tempFile = null;
	    	try {
	    		tempFile = File.createTempFile("graphicImg", ".png");
	    		FileOutputStream fos = new FileOutputStream(tempFile);
	    		convert.convertToImage(image, drawData.getVarValue(), "PNG", fos);
	    		logger.debug("converted image is " + tempFile.getName());
	    		fos.close();
	    	}catch(IOException e) {
	    		logger.error("Error",e);
	    		if(tempFile!=null) {
	    			tempFile.delete();
	    		}
	    		continue;
	    	}

	   		Image img = null;
    		try {
    			logger.info("adding diagram " + tempFile.getAbsolutePath());
    			img = Image.getInstance(tempFile.getAbsolutePath() );
    		} catch(IOException e) {
    			logger.error("error:",e);
    			continue;
    		}
    		img.scaleToFit(getDocument().getPageSize().getWidth()-getDocument().leftMargin()-getDocument().rightMargin(), getDocument().getPageSize().getHeight()); 
    		Paragraph p = new Paragraph(); 
    		p.add(img);    		
    		p.add(new Phrase("Subject:"+subject.getVarValue(),getFont()));
    		getDocument().add(p);

	    	tempFile.deleteOnExit();
        }

    }

	private String getRefName(Demographic d) {
		String referral = d.getFamilyDoctor();

		if (referral == null || referral.length()==0)
			return new String();

		int start = referral.indexOf("<rd>");
		int end = referral.indexOf("</rd>");
		String ref = new String();

		if (start >= 0 && end >= 0) {
			String subreferral = referral.substring(start + 4, end);
			if (!"".equalsIgnoreCase(subreferral.trim())) {
				ref = subreferral;

			}
		}
		return ref;
	}
}
