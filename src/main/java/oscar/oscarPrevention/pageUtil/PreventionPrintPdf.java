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


/*
 * PreventionPrintPdf.java
 *
 * Created on March 12, 2007, 4:05 PM
 */

package oscar.oscarPrevention.pageUtil;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarClinic.ClinicData;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
/*
 * @author rjonasz
 */
public class PreventionPrintPdf {

    private int curPage;
    private float upperYcoord;
    private ColumnText ct;
    private Document document;
    private PdfContentByte cb;
    
    private final int LINESPACING = 1;
    private final float LEADING = 12;
    
    private final Map<String,String> readableStatuses = new HashMap<String,String>();
    
    /** Creates a new instance of PreventionPrintPdf */
    public PreventionPrintPdf() {
    	readableStatuses.put("0","Completed or Normal");
    	readableStatuses.put("1","Refused");
    	readableStatuses.put("2","Ineligible");
    }
    
    public void printPdf(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");  //octet-stream
        response.setHeader("Content-Disposition", "attachment; filename=\"Prevention.pdf\"");
        String[] headerIds = request.getParameterValues("printHP");
        printPdf(headerIds, request, response.getOutputStream());
    }
    
    public void printPdf(String[] headerIds, HttpServletRequest request, OutputStream outputStream) throws IOException, DocumentException{
        
        //make sure we have data to print      
        if( headerIds == null )
            throw new DocumentException();
        
        String demoNo = request.getParameter("demographicNo");
        DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
        Demographic demo = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
        
        if (demo == null) 
            throw new DocumentException();
        
        //Create the document we are going to write to
        document = new Document();
        // PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        PdfWriter writer = PdfWriterFactory.newInstance(document, outputStream, FontSettings.HELVETICA_10PT);
        document.setPageSize(PageSize.LETTER);
                
        //Create the font we are going to print to       
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, Color.BLACK);     
               
        StringBuilder demoInfo = new StringBuilder(demo.getSexDesc()).append(" Age: ").append(demo.getAge()).append(" (").append(demo.getBirthDayAsString()).append(")")
                .append(" HIN: (").append(demo.getHcType()).append(") ").append(demo.getHin()).append(" ").append(demo.getVer());                                                                 
              
        //Header will be printed at top of every page beginning with p2
        Phrase titlePhrase = new Phrase(16, "Preventions", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.BOLD, Color.BLACK));
        titlePhrase.add(Chunk.NEWLINE);
        titlePhrase.add(new Chunk(demo.getFormattedName(),FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, Color.BLACK)));
        titlePhrase.add(Chunk.NEWLINE);         
        titlePhrase.add(new Chunk(demoInfo.toString(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
        
        String mrp = request.getParameter("mrp");
        if (mrp != null && OscarProperties.getInstance().getBooleanProperty("mrp_model","yes")){
        	Properties prop = (Properties) request.getSession().getAttribute("providerBean");
                titlePhrase.add(Chunk.NEWLINE);
                titlePhrase.add(new Chunk("MRP: " + prop.getProperty(mrp,"unknown"), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, Color.BLACK)));
        }
        
        HeaderFooter header = new HeaderFooter(titlePhrase,false);        
        header.setAlignment(HeaderFooter.ALIGN_RIGHT);
        header.setBorder(Rectangle.BOTTOM);
        document.setHeader(header);  
        document.open();
        cb = writer.getDirectContent();
        
        //Clinic Address Information
        ClinicData clinicData = new ClinicData();
        clinicData.refreshClinicData();
 
        StringBuilder clinicAddrCont = new StringBuilder(clinicData.getClinicCity()).append(", ").append(clinicData.getClinicProvince()).append(" ").append(clinicData.getClinicPostal());       
                
        Paragraph clinicParagraph = new Paragraph(LEADING, clinicData.getClinicName(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, Color.BLACK));
        clinicParagraph.add(Chunk.NEWLINE);
        clinicParagraph.add(new Chunk(clinicData.getClinicAddress(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
        clinicParagraph.add(Chunk.NEWLINE);
        clinicParagraph.add(new Chunk(clinicAddrCont.toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
        clinicParagraph.add(Chunk.NEWLINE);
        clinicParagraph.add(new Chunk("Ph.",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        clinicParagraph.add(new Chunk(clinicData.getClinicPhone(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
        clinicParagraph.add(new Chunk(" Fax.",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        clinicParagraph.add(new Chunk(clinicData.getClinicFax(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
        clinicParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(clinicParagraph);
        
        //get top y-coord for starting to print columns
        upperYcoord = document.top() - header.getHeight() -(clinicParagraph.getLeading()*4f) - font.getCalculatedLeading(LINESPACING);
        
        int subIdx;
        String preventionHeader, procedureAge, procedureDate, procedureStatus;
        
        //1 - obtain number of lines of incoming prevention data
        boolean showComments = OscarProperties.getInstance().getBooleanProperty("prevention_show_comments", "true");        
                      
        //3 - Start the column
        ct = new ColumnText(cb);
        ct.setSimpleColumn(document.left(), document.bottom(), document.right()/2f, upperYcoord);
        
        curPage = 1;
        
        boolean onColumnLeft = true;
                        
        //now we can start to print the prevention data
        for(int idx = 0; idx < headerIds.length; ++idx) {

            preventionHeader = request.getParameter("preventionHeader" + headerIds[idx]);
            Phrase procHeader = new Phrase(LEADING, "Prevention " + preventionHeader + "\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD, Color.BLACK));
            ct.addText(procHeader);
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.setIndent(0);
            ct.setFollowingIndent(0);
            float titleYPos = ct.getYLine();
            
            //check whether the Prevention Title can fit on the page
            boolean writeTitleOk = true;
            int status = ct.go(true);
            if (ColumnText.hasMoreText(status)) {
                writeTitleOk = false;
            }
                
            subIdx = 0;
           
            while( (procedureAge = request.getParameter("preventProcedureAge" + headerIds[idx] + "-" + subIdx)) != null ) {
                procedureDate = request.getParameter("preventProcedureDate" + headerIds[idx] + "-" + subIdx);
                procedureStatus = request.getParameter("preventProcedureStatus" + headerIds[idx] + "-" + subIdx);
                procedureStatus = readableStatuses.get(procedureStatus);              
                
                if( procedureStatus == null ) {
                	procedureStatus = "N/A";
                }
              
                //Age                
                Phrase procedure = new Phrase(LEADING, "Age:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.BLACK));
                procedure.add(new Chunk(procedureAge,FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
                procedure.add(Chunk.NEWLINE);
                
                //Date
                procedure.add("Date:");
                procedure.add(new Chunk(procedureDate, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
                procedure.add(Chunk.NEWLINE);
                
                //Status
                procedure.add("Status:");
                procedure.add(new Chunk(procedureStatus, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
                procedure.add(Chunk.NEWLINE);
                
                String procedureComments = null;
                if (showComments) { 
                    procedureComments = request.getParameter("preventProcedureComments" + headerIds[idx] + "-" + subIdx);
                    if (procedureComments != null && !procedureComments.isEmpty()){
                        procedure.add("Comments:");
                        procedure.add(Chunk.NEWLINE);                        
                    }
                }
                         
                //check if the Date/Age/Comments title can fit on the page.
                ct.addText(procedure);
                ct.setAlignment(Element.ALIGN_LEFT);
                ct.setIndent(10);
                ct.setFollowingIndent(0);
                float detailYPos = ct.getYLine();
                status = ct.go(true);
                
                boolean writeDetailOk = true;
                if (ColumnText.hasMoreText(status)) {
                    writeDetailOk = false;
                }
                
                //Comments
                Phrase commentsPhrase = new Phrase(LEADING, "", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, Color.BLACK));
                if (showComments && procedureComments != null && !procedureComments.isEmpty()){
                    commentsPhrase.add(procedureComments);
                    commentsPhrase.add(Chunk.NEWLINE);                                                                
                }
                
                commentsPhrase.add(Chunk.NEWLINE);
               
                //Check if the comments can fit on the page
                ct.addText(commentsPhrase);
                ct.setAlignment(Element.ALIGN_JUSTIFIED);
                ct.setIndent(25);
                ct.setFollowingIndent(25);
                float commentYPos = ct.getYLine();
                status = ct.go(true);
                
                boolean writeCommentsOk = true;
                if (ColumnText.hasMoreText(status)) {
                    writeCommentsOk = false;
                }                                

                 boolean proceedWrite = true;
                 if (writeDetailOk && writeCommentsOk) {
                                        
                    //write on the same column and page
                    if (subIdx == 0) {
                        if (writeTitleOk) {
                            //we still need to write the title
                            ct.addText(procHeader);
                            ct.setAlignment(Element.ALIGN_LEFT);
                            ct.setYLine(titleYPos);
                            ct.setIndent(0);
                            ct.setFollowingIndent(0);                     
                            ct.go(); 
                        }
                        else {
                            proceedWrite = false;
                        }
                    }
                    
                    if (proceedWrite) {
                        //Date and Age
                        ct.addText(procedure);
                        ct.setAlignment(Element.ALIGN_LEFT);
                        ct.setYLine(detailYPos);
                        ct.setIndent(10);
                        ct.setFollowingIndent(0);
                        ct.go();

                        //Comments
                        ct.addText(commentsPhrase);
                        ct.setAlignment(Element.ALIGN_JUSTIFIED);
                        ct.setYLine(commentYPos);
                        ct.setIndent(25);
                        ct.setFollowingIndent(25);
                        ct.go();
                    }
                }
                else {
                    proceedWrite = false;
                }
                    
                //We can't fit the prevention we are printing into the current column on the current page we are printing to 
                if (!proceedWrite) {
                                        
                    if (onColumnLeft) {  
                        //Print to the right column (i.e. we are printing to the current page)
                        onColumnLeft = false;
                        ct.setSimpleColumn(document.right()/2f, document.bottom(), document.right(), upperYcoord);                                                
                    }
                    else {
                        //Print to the left column (i.e. we are starting a new page)
                        onColumnLeft = true;
                        ColumnText.showTextAligned(cb, Phrase.ALIGN_CENTER, new Phrase("-" + curPage + "-"), document.right()/2f, document.bottom()-(document.bottomMargin()/2f), 0f);
                        addPromoText();                       
                        upperYcoord = document.top() - header.getHeight() - font.getCalculatedLeading(LINESPACING);
                        document.newPage();
                        
                        curPage++;
                        ct.setSimpleColumn(document.left(), document.bottom(), document.right()/2f, upperYcoord);
                    }
                    
                    //Title (if we are starting to print a new prevention, use the Prevention name as title, otherwise if we 
                    //are in the middle of printing a prevention that has multiple items, identify this as a continued prevention
                    if (subIdx != 0) {
                        Phrase contdProcHeader = new Phrase(LEADING, "Prevention " + preventionHeader + " (cont'd)\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.ITALIC, Color.BLACK));
                        ct.setText(contdProcHeader);
                    } else { 
                        ct.setText(procHeader);
                    }
                    ct.setAlignment(Element.ALIGN_LEFT);
                    ct.setIndent(0);
                    ct.setFollowingIndent(0);                          
                    ct.go();
                    titleYPos = ct.getYLine();
                    

                    //Date and Age
                    ct.setText(procedure);
                    ct.setAlignment(Element.ALIGN_LEFT);
                    ct.setIndent(10);
                    ct.setFollowingIndent(0);                    
                    ct.go();                   

                    //Comments
                    ct.setText(commentsPhrase);
                    ct.setAlignment(Element.ALIGN_JUSTIFIED);
                    ct.setIndent(25);
                    ct.setFollowingIndent(25);                   
                    ct.go();                   
                    
                }

                ++subIdx;
            }
            
        }
        
        //Make sure last page has the footer
        ColumnText.showTextAligned(cb, Phrase.ALIGN_CENTER, new Phrase("-" + curPage + "-"), document.right()/2f, document.bottom()-(document.bottomMargin()/2f), 0f);
        addPromoText(); 
        
        document.close();
    }
            
    private void addPromoText() throws DocumentException, IOException{
        if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null){
            cb.beginText();
            cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252,BaseFont.NOT_EMBEDDED), 6);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"), PageSize.LETTER.getWidth()/2, 5, 0);
            cb.endText();
        }
    }
}
