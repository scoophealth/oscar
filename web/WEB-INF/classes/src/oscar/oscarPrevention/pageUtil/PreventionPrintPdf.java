/*
 * PreventionPrintPdf.java
 *
 * Created on March 12, 2007, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarPrevention.pageUtil;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oscar.OscarProperties;
import oscar.oscarClinic.ClinicData;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.util.Map;
/**
 *
 * @author rjonasz
 */
public class PreventionPrintPdf {
    
    private int numLines;
    private int maxLines;
    private int totalLinesWritten;
    private int linesToBeWritten;
    private int linesWritten;
    private int numPages;
    private int curPage;
    private float upperYcoord;
    private float pageHeight;
    private ColumnText ct;
    private Document document;
    private PdfContentByte cb;
    
    private final int LINESPACING = 1;
    private final float LEADING = 12;
    private final float FONTSIZE = 12;
    private final int NUMCOLS = 2;
    
    /** Creates a new instance of PreventionPrintPdf */
    public PreventionPrintPdf() {
    }
    
    public void printPdf(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");  //octet-stream
        response.setHeader("Content-Disposition", "attachment; filename=\"Prevention.pdf\"");
        String[] headerIds = request.getParameterValues("printHP");
        printPdf(headerIds, request, response.getOutputStream());
    }
    
    public void printPdf(String[] headerIds, HttpServletRequest request, OutputStream outputStream) throws IOException, DocumentException{
        //make sure we have data to print
        //String[] headerIds = request.getParameterValues("printHP");
        if( headerIds == null )
            throw new DocumentException();
        
        
        
        //Create the document we are going to write to
        document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.setPageSize(PageSize.LETTER);
        document.open();
        
        //Create the font we are going to print to
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, FONTSIZE, Font.NORMAL);
        float leading = font.leading(LINESPACING);
        
        //set up document title and header
        String title = "Preventions for " + request.getParameter("nameAge");
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
        
        //Write title with top and bottom borders on p1
        cb = writer.getDirectContent();
        cb.setColorStroke(new Color(0,0,0));
        cb.setLineWidth(0.5f);
        
        cb.moveTo(document.left(), document.top());
        cb.lineTo(document.right(), document.top());
        cb.stroke();
        cb.setFontAndSize(bf, FONTSIZE);
        
        upperYcoord = document.top() - (font.leading(LINESPACING)*2f);
        cb.beginText();
        for( int idx = 0; idx < clinic.length; ++idx ) {
            cb.showTextAligned(cb.ALIGN_CENTER, clinic[idx], document.right()/2f, upperYcoord,0f);
            upperYcoord -= font.leading(LINESPACING);
        }
        
        cb.endText();
        cb.moveTo(document.left(), upperYcoord);
        cb.lineTo(document.right(), upperYcoord);
        cb.stroke();
        
        //get top y-coord for starting to print columns
        upperYcoord = cb.getYTLM() - font.leading(LINESPACING*2f);
        
        int subIdx;
        String preventionHeader, procedureAge, procedureDate;
        
        //1 - obtain number of lines of incoming prevention data
        numLines = 0;
        for(int idx = 0; idx < headerIds.length; ++idx) {
            ++numLines;
            subIdx = 0;
            while( request.getParameter("preventProcedureAge" + headerIds[idx] + "-" + subIdx) != null ) {
                ++subIdx;
                numLines += 3;
            }
            numLines += 2;
        }
        
        //2 - calculate max num of lines a page can hold and number of pages of data we have
        pageHeight = upperYcoord - document.bottom();
        maxLines = (int)Math.floor((double)pageHeight/((double)font.leading(LINESPACING)+4d));
        numPages = (int)Math.ceil((double)numLines/((double)maxLines*NUMCOLS));
        
        //3 - Start the column
        ct = new ColumnText(cb);
        ct.setSimpleColumn(document.left(), document.bottom(), document.right()/2f, upperYcoord);
        
        linesToBeWritten = linesWritten = 0;
        boolean pageBreak = false;
        
        curPage = 1;
        totalLinesWritten = 0;
        
        //add promotext to current page
        addPromoText();
        
        //if we have > 1 element but less than a page of data, shrink maxLines so we can try to balance text in columns
        if( headerIds.length > 1 ) {
            if( curPage == numPages ) {
                maxLines = (int)numLines / NUMCOLS;
            }
        }
        
        //now we can start to print the prevention data
        for(int idx = 0; idx < headerIds.length; ++idx) {
            
            linesToBeWritten = 4;  //minimum lines for header and one prevention item
            pageBreak = checkColumnFill(ct, "", font, pageBreak); //if necessary break before we print prevention header

            preventionHeader = request.getParameter("preventionHeader" + headerIds[idx]);
            Phrase procHeader = new Phrase(LEADING, "Prevention " + preventionHeader + "\n", font);
            ct.addText(procHeader);
            subIdx = 0;
            
            while( (procedureAge = request.getParameter("preventProcedureAge" + headerIds[idx] + "-" + subIdx)) != null ) {
                procedureDate = request.getParameter("preventProcedureDate" + headerIds[idx] + "-" + subIdx);
                
                linesToBeWritten = 3;
                pageBreak = checkColumnFill(ct, preventionHeader, font, pageBreak);
                
                Phrase procedure = new Phrase(LEADING, "     " + procedureAge + "\n", font);
                procedure.add("     " + procedureDate + "\n\n");
                ct.addText(procedure);
                ct.go();
                linesWritten += ct.getLinesWritten();
                totalLinesWritten += ct.getLinesWritten();
                ++subIdx;
            }
            
        }
        
        ct.showTextAligned(cb, Phrase.ALIGN_CENTER, new Phrase("-" + curPage + "-"), document.right()/2f, document.bottom()-(document.bottomMargin()/2f), 0f);
        
        document.close();
    }
    
    private boolean breakPage(boolean pageBreak, float upperYcoord) {
        
        if( pageBreak ) {
            ct.showTextAligned(cb, Phrase.ALIGN_CENTER, new Phrase("-" + curPage + "-"), document.right()/2f, document.bottom()-(document.bottomMargin()/2f), 0f);
            ++curPage;
            try{
                document.newPage();
                
                // add promo text to new page
                addPromoText();
                
            }catch(Exception e){}
            if( curPage == numPages ) {
                maxLines = (numLines - totalLinesWritten) / NUMCOLS;
            }
            ct.setSimpleColumn(document.left(), document.bottom(), document.right()/2f, upperYcoord);
            pageBreak = false;
            
        } else {
            ct.setSimpleColumn(document.right()/2f, document.bottom(), document.right(), upperYcoord);
            pageBreak = true;
        }
        
        return pageBreak;
    }
    
    private boolean checkColumnFill(ColumnText ct, String preventionHeader, Font font, boolean pageBreak) {
        if( linesWritten + linesToBeWritten > maxLines ) {
            
            //reset upperYcoord for pages 2 and above
            if( curPage == 1 && pageBreak) {
                upperYcoord = document.top() - font.leading(LINESPACING);
                pageHeight = upperYcoord - document.bottom();
                maxLines = (int)Math.floor((double)pageHeight/((double)font.leading(LINESPACING)+4d));
                numPages = (int)Math.ceil(((double)numLines-totalLinesWritten)/((double)maxLines*NUMCOLS)) + 1;
            }
            pageBreak = breakPage(pageBreak, upperYcoord);
            linesWritten = 0;
            if( preventionHeader.length() > 0 ) {
                Phrase procHeaderContd = new Phrase(LEADING, "Prevention " + preventionHeader + " cont'd\n", font);
                ct.addText(procHeaderContd);
                ++linesToBeWritten;
            }
            
        }
        
        return pageBreak;
    }
    
    private void addPromoText() throws DocumentException, IOException{
        if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null){
            cb.beginText();
            cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252,BaseFont.NOT_EMBEDDED), 6);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"), PageSize.LETTER.width()/2, 5, 0);
            cb.endText();
        }
    }
}
