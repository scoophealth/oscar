/*
 *
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
 * CaseManagementPrintPdf.java
 *
 * Created on January 17, 2008, 11:17 AM
 *
 *
 *
 */

package org.oscarehr.casemgmt.service;

import javax.servlet.http.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Chunk;
import com.lowagie.text.Phrase;
import com.lowagie.text.Paragraph;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import oscar.OscarProperties;
import oscar.oscarClinic.ClinicData;
import org.oscarehr.casemgmt.model.CaseManagementNote;

/**
 *
 * @author rjonasz
 */
public class CaseManagementPrintPdf {
    private HttpServletRequest request;
    private HttpServletResponse response;
        
    private float upperYcoord;   
    private Document document;
    private PdfContentByte cb;
    
    private final int LINESPACING = 1;
    private final float LEADING = 12;
    private final float FONTSIZE = 10;
    private final int NUMCOLS = 2;
    
    /** Creates a new instance of CaseManagementPrintPdf */
    public CaseManagementPrintPdf(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void printPdf(ArrayList<CaseManagementNote>notes) throws IOException, DocumentException{
        
        //Create the document we are going to write to
        document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,response.getOutputStream());
        document.setPageSize(PageSize.LETTER);
        document.open();
        
        //Create the font we are going to print to
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, FONTSIZE, Font.NORMAL);
        float leading = font.leading(LINESPACING);
        
        //set up document title and header
        String title = "Case Management Notes for " + (String)request.getAttribute("demoName");
        ClinicData clinicData = new ClinicData();
        clinicData.refreshClinicData();
        String[] clinic = new String[] {clinicData.getClinicName(), clinicData.getClinicAddress(),
        clinicData.getClinicCity() + ", " + clinicData.getClinicProvince(),
        clinicData.getClinicPostal(), clinicData.getClinicPhone(), title };
        
        //Header will be printed at top of every page beginning with p2
        Phrase headerPhrase = new Phrase(LEADING, title, font);
        HeaderFooter header = new HeaderFooter(headerPhrase,false);
        header.setAlignment(HeaderFooter.ALIGN_CENTER);
        document.setHeader(header);
        
        //Footer contains page numbers and date printed
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");   
        Date date = new Date();
        String now = "Printed " + formatter.format(date) + "\nPage ";
        Phrase footerPhrase = new Phrase(LEADING, now, font);
        HeaderFooter footer = new HeaderFooter(footerPhrase, true);
        footer.setAlignment(HeaderFooter.ALIGN_CENTER);
        document.setFooter(footer);
        
        //Write title with top and bottom borders on p1
        cb = writer.getDirectContent();
        cb.setColorStroke(new Color(0,0,0));
        cb.setLineWidth(0.5f);
        
        cb.moveTo(document.left(), document.top());
        cb.lineTo(document.right(), document.top());
        cb.stroke();
        cb.setFontAndSize(bf, FONTSIZE);
        
        upperYcoord = document.top() - (font.leading(LINESPACING)*2f);
       
        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_CENTER);
        Phrase phrase = new Phrase();

        for( int idx = 0; idx < clinic.length; ++idx ) {
            phrase.add(clinic[idx] + "\n");
            upperYcoord -= phrase.leading();
        }
        
        p.add(phrase);
        document.add(p);
        
        cb.moveTo(document.left(), upperYcoord);
        cb.lineTo(document.right(), upperYcoord);
        cb.stroke();                                      
                
        CaseManagementNote note;             
        Font obsfont = new Font(bf, FONTSIZE, Font.UNDERLINE);
        Chunk chunk;
                
        //Print notes
        for( int idx = 0; idx < notes.size(); ++idx ) {
            note = notes.get(idx);        
            p = new Paragraph();
            p.setSpacingBefore(font.leading(LINESPACING)*2f);
            phrase = new Phrase(LEADING, "", font);              
            chunk = new Chunk("Observation Date: " + formatter.format(note.getObservation_date()) + "\n", obsfont);
            phrase.add(chunk);            
            phrase.add(note.getNote() + "\n");            
            p.add(phrase);
            document.add(p);
        }
        
        document.close();
    }
    
}
