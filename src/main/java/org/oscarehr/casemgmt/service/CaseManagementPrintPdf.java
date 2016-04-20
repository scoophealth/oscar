/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.casemgmt.service;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import oscar.OscarProperties;
import oscar.oscarClinic.ClinicData;

/**
 *
 * @author rjonasz
 */
public class CaseManagementPrintPdf {

    private HttpServletRequest request;
    private OutputStream os;

    private float upperYcoord;
    private Document document;
    private PdfContentByte cb;
    private BaseFont bf;
    private Font font;
    private boolean newPage = false;

    private SimpleDateFormat formatter;

    public final int LINESPACING = 1;
    public final float LEADING = 12;
    public final float FONTSIZE = 10;
    public final int NUMCOLS = 2;

    /** Creates a new instance of CaseManagementPrintPdf */
    public CaseManagementPrintPdf(HttpServletRequest request, OutputStream os) {
        this.request = request;
        this.os = os;
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
    }

    public HttpServletRequest getRequest() {
    	return request;
    }

    public OutputStream getOutputStream() {
    	return os;
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

    public void printDocHeaderFooter() throws IOException, DocumentException {
        //Create the document we are going to write to
        document = new Document();
        PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_12PT);
        
        // writer.setPageEvent(new EndPage());
        document.setPageSize(PageSize.LETTER);
        document.open();

        //Create the font we are going to print to
        bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, FONTSIZE, Font.NORMAL);


        //set up document title and header
        ResourceBundle propResource = ResourceBundle.getBundle("oscarResources");
        String title = propResource.getString("oscarEncounter.pdfPrint.title") + " " + (String)request.getAttribute("demoName") + "\n";
        String gender = propResource.getString("oscarEncounter.pdfPrint.gender") + " " + (String)request.getAttribute("demoSex") + "\n";
        String dob = propResource.getString("oscarEncounter.pdfPrint.dob") + " " + (String)request.getAttribute("demoDOB") + "\n";
        String age = propResource.getString("oscarEncounter.pdfPrint.age") + " " + (String)request.getAttribute("demoAge") + "\n";
        String mrp = propResource.getString("oscarEncounter.pdfPrint.mrp") + " " + (String)request.getAttribute("mrp") + "\n";
        String[] info = null;
        if("true".equals(OscarProperties.getInstance().getProperty("print.includeMRP", "true"))) {
        	info = new String[] { title, gender, dob, age, mrp };
        } else {
        	info = new String[] { title, gender, dob, age};
        }

        ClinicData clinicData = new ClinicData();
        clinicData.refreshClinicData();
        String[] clinic = new String[] {clinicData.getClinicName(), clinicData.getClinicAddress(),
        clinicData.getClinicCity() + ", " + clinicData.getClinicProvince(),
        clinicData.getClinicPostal(), clinicData.getClinicPhone()};

        if("true".equals(OscarProperties.getInstance().getProperty("print.useCurrentProgramInfoInHeader", "false"))) {
        	ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
        	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        	ProgramProvider pp = programManager2.getCurrentProgramInDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
    		if(pp != null) {
    			Program program = pp.getProgram();
    			clinic = new String[] {
    			program.getDescription(),
    			program.getAddress(),
    			program.getPhone()
    			};
    		}
        }
        //Header will be printed at top of every page beginning with p2
        Phrase headerPhrase = new Phrase(LEADING, title, font);
        HeaderFooter header = new HeaderFooter(headerPhrase,false);
        header.setAlignment(HeaderFooter.ALIGN_CENTER);
        document.setHeader(header);

        //Write title with top and bottom borders on p1
        cb = writer.getDirectContent();
        cb.setColorStroke(new Color(0,0,0));
        cb.setLineWidth(0.5f);

        cb.moveTo(document.left(), document.top());
        cb.lineTo(document.right(), document.top());
        cb.stroke();
        //cb.setFontAndSize(bf, FONTSIZE);

        upperYcoord = document.top() - (font.getCalculatedLeading(LINESPACING)*2f);

        ColumnText ct = new ColumnText(cb);
        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_LEFT);
        Phrase phrase = new Phrase();
        Phrase dummy = new Phrase();
        for( int idx = 0; idx < clinic.length; ++idx ) {
            phrase.add(clinic[idx] + "\n");
            dummy.add("\n");
            upperYcoord -= phrase.getLeading();
        }

        dummy.add("\n");
        ct.setSimpleColumn(document.left(), upperYcoord, document.right()/2f, document.top());
        ct.addElement(phrase);
        ct.go();

        p.add(dummy);
        document.add(p);

        //add patient info
        phrase = new Phrase();
        p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        for( int idx = 0; idx < info.length; ++idx ) {
            phrase.add(info[idx]);
        }

        ct.setSimpleColumn(document.right()/2f, upperYcoord, document.right(), document.top());
        p.add(phrase);
        ct.addElement(p);
        ct.go();

        cb.moveTo(document.left(), upperYcoord);
        cb.lineTo(document.right(), upperYcoord);
        cb.stroke();
        upperYcoord -= phrase.getLeading();

    }

    public void printRx(String demoNo) throws DocumentException {
        printRx(demoNo,null);
    }
    public void printRx(String demoNo,List<CaseManagementNote> cpp) throws DocumentException {
        if( demoNo == null )
            return;

        if( newPage )
            document.newPage();
        else
            newPage = true;

        Paragraph p = new Paragraph();
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Phrase phrase = new Phrase(LEADING, "", obsfont);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        phrase.add("Patient Rx History");
        p.add(phrase);
        document.add(p);

        Font normal = new Font(bf, FONTSIZE, Font.NORMAL);

        oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
        oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
        arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));


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

    public void printNotes(List<CaseManagementNote>notes) throws DocumentException{

        CaseManagementNote note;
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Paragraph p;
        Phrase phrase;
        Chunk chunk;

        if( newPage )
            document.newPage();
        else
            newPage = true;

        //Print notes
        for( int idx = 0; idx < notes.size(); ++idx ) {
            note = notes.get(idx);
            p = new Paragraph();
            //p.setSpacingBefore(font.leading(LINESPACING)*2f);
            phrase = new Phrase(LEADING, "", font);
            chunk = new Chunk("Documentation Date: " + formatter.format(note.getObservation_date()) + "\n", obsfont);
            phrase.add(chunk);
            phrase.add(note.getNote() + "\n\n");
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
                promoTxt = "";
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
