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
 * LabPDFCreator.java
 *
 * Created on November 27, 2007, 9:43 AM
 *
 */

package oscar.oscarLab.ca.all.pageUtil;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.io.IOUtils;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.CLSHandler;
import oscar.oscarLab.ca.all.parsers.ExcellerisOntarioHandler;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MEDITECHHandler;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.PATHL7Handler;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;


/**
 *
 * @author wrighd
 */
public class LabPDFCreator extends PdfPageEventHelper{
    private OutputStream os;
    private boolean isUnstructuredDoc = false;
    private boolean isReportData = Boolean.FALSE;
    private MessageHandler handler;
    private List<MessageHandler>handlers = new ArrayList<MessageHandler>();
    
    private int versionNum;
    private String[] multiID;
    private String id;

    private Document document;
    private BaseFont bf;
    private Font font;
    private Font boldFont;
    private String dateLabReceived;
    
    private List<String> embeddedDocumentsToAppend = new ArrayList<String>();
    List<String> allLicenseNames = new ArrayList<String>();
	
	public static byte[] getPdfBytes(String segmentId, String providerNo) throws IOException, DocumentException
    {
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();

    	LabPDFCreator labPDFCreator=new LabPDFCreator(baos, segmentId, providerNo);
    	labPDFCreator.printPdf();

    	return(baos.toByteArray());
    }
	
	 public LabPDFCreator() {
		 // Default constructor.
	 }

    /** Creates a new instance of LabPDFCreator */
    public LabPDFCreator(HttpServletRequest request, OutputStream os) {
    	this(os, (request.getParameter("segmentID")!=null?request.getParameter("segmentID"):(String)request.getAttribute("segmentID")), (request.getParameter("providerNo")!=null?request.getParameter("providerNo"):(String)request.getAttribute("providerNo")));
    }

    public LabPDFCreator(OutputStream os, String segmentId, String providerNo) {
        this.os = os;
        this.id = segmentId;

      //Need date lab was received by OSCAR
        Hl7TextMessageDao hl7TxtMsgDao = (Hl7TextMessageDao)SpringUtils.getBean("hl7TextMessageDao");
        Hl7TextMessage hl7TextMessage = hl7TxtMsgDao.find(Integer.parseInt(segmentId));
        java.util.Date date = hl7TextMessage.getCreated();
        String stringFormat = "yyyy-MM-dd HH:mm";
        dateLabReceived = UtilDateUtilities.DateToString(date, stringFormat);

        // create handler
        this.handler = Factory.getHandler(id);

        // determine lab version
        String multiLabId = Hl7textResultsData.getMatchingLabs(id);
        this.multiID = multiLabId.split(",");

        int i=0;
        while (!multiID[i].equals(id)){
            i++;
        }
        this.versionNum = i+1;
    } 
    //Creates an rtf file for viha rtf labs
    public void printRtf()throws IOException, DocumentException{
    	//create an input stream from the rtf string bytes
    	byte[] rtfBytes = handler.getOBXResult(0, 0).getBytes();
    	ByteArrayInputStream rtfStream = new ByteArrayInputStream(rtfBytes);
    	
    	//create & open the document we are going to write to and its writer
    	document = new Document();
    	RtfWriter2 writer = RtfWriter2.getInstance(document,os);
    	document.setPageSize(PageSize.LETTER);
    	document.addTitle("Title of the Document");
    	document.addCreator("OSCAR");
    	document.open();
    	
        //Create the fonts that we are going to use
        bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, 11, Font.NORMAL);
        boldFont = new Font(bf, 12, Font.BOLD);

        //add the patient information
        addRtfPatientInfo();
        
        //add the results
    	writer.importRtfDocument(rtfStream, null);
    	
    	document.close();
    	os.flush();
    }
    public void printPdf() throws IOException, DocumentException{

        // check that we have data to print
        if (handler == null) {
            throw new DocumentException();
        }

        //Create the document we are going to write to
        document = new Document();
        PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);

       
        //Set page event, function onEndPage will execute each time a page is finished being created
        writer.setPageEvent(this);

        document.setPageSize(PageSize.LETTER);
        document.addTitle("OSCAR Laboratory Report");
        document.addCreator("OSCAR");
        document.open();

        //Create the fonts that we are going to use
        bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, 9, Font.NORMAL);
        boldFont = new Font(bf, 10, Font.BOLD);

        // add the header table containing the patient and lab info to the document
        createInfoTable();

        // add the tests and test info for each header
        ArrayList<String> headers = handler.getHeaders();
        for (int i=0; i < headers.size(); i++) {
        	
        	String specimenSource = null;
        	String specimenDescription = null;
        	
        	if( ( handler instanceof MEDITECHHandler )  &&  ( "MIC".equals(  ((MEDITECHHandler) handler).getSendingApplication() ) ) ) {
				specimenSource = ((MEDITECHHandler) handler).getSpecimenSource(i);
				specimenSource = "SPECIMEN SOURCE: " + specimenSource;
				specimenDescription = ((MEDITECHHandler) handler).getSpecimenDescription(i);
				specimenDescription = "SPECIMEN DESCRIPTION: " + specimenDescription;
        	}
        	
            addLabCategory( headers.get(i), specimenSource, specimenDescription );
        }
        
        // It's not exactly clear that this block does anything. 
        for(MessageHandler extraHandler:handlers) {
        	ArrayList<String> extraHeaders = extraHandler.getHeaders();
            for (int i=0; i < extraHeaders.size(); i++)
                addLabCategory( extraHeaders.get(i) , extraHandler);
        }

    
        
        	       
        // add end of report table
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPhrase(new Phrase("  "));
        table.addCell(cell);
        cell.setBorder(15);
        cell.setBackgroundColor(new Color(210, 212, 255));
		if(handler.getMsgType().equals("CLS")){
			cell.setPhrase(new Phrase("Legend:  A=Abnormal  L=Low  H=High  C=Critical", boldFont));
		}
		else
		{
        	cell.setPhrase(new Phrase("END OF REPORT", boldFont));
		}
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        document.add(table);

        if(handler.getMsgType().equals("ExcellerisON")) {
        	PdfPTable table2 = new PdfPTable(1);
        	 table2.setWidthPercentage(100);
        	 for(String x : allLicenseNames) {
	             PdfPCell cell2 = new PdfPCell();
	             cell2.setBorder(0);
	             cell2.setPhrase(new Phrase(x,new Font(bf, 9, Font.NORMAL)));
	             table2.addCell(cell2);
        	 }
        	 document.add(table2);
        }
        
       
        document.close();

        os.flush();
    }
	 
    public void addEmbeddedDocuments(File currentPDF, OutputStream os) {
    	List<Object> alist = new ArrayList<Object>();
    	
    	InputStream mainPDF = null;
    	try {
    		
    		mainPDF = new FileInputStream(currentPDF);
    		
    		alist.add(mainPDF);
    		
    		for(String data : embeddedDocumentsToAppend) {
    			InputStream tmp = new ByteArrayInputStream(	Base64.decodeBase64(data));
    			alist.add(tmp);
    		}
    	
    		ConcatPDF.concat(alist, os);
    	} catch(Exception e) {
    		MiscUtils.getLogger().error("Error", e);
    	} finally {
    		IOUtils.closeQuietly(mainPDF);
    	}
    }

    /*
	 * Given the name of a lab category this method will add the category
	 * header, the test result headers and the test results for that category.
	 */
    private void addLabCategory(String header, MessageHandler extraHandler) throws DocumentException {
    	addLabCategory(header, extraHandler, null, null);
    }
    
    private void addLabCategory(String header, String specimenSource, String specimenDescription) throws DocumentException {
    	addLabCategory(header, null, specimenSource, specimenDescription);
    }
    
	private void addLabCategory(String header, MessageHandler extraHandler, String specimenSource, String specimenDescription ) throws DocumentException {
		String currentLicenseNo = null, lastLicenseNo = null;
		
		MessageHandler handler = (extraHandler!=null)?extraHandler:this.handler;
		if(handler.getMsgType().equals("PATHL7")){
			this.isUnstructuredDoc = ((PATHL7Handler) handler).unstructuredDocCheck(header);
		} else if(handler.getMsgType().equals("CLS"))
		{
			this.isUnstructuredDoc = ((CLSHandler) handler).isUnstructured();
		} 

		PdfPCell cell = new PdfPCell();
		float[] mainTableWidths;
		PdfPTable table = null;
		
		if( ( handler instanceof MEDITECHHandler && ((MEDITECHHandler) handler).isReportData() ) 
				|| ( handler instanceof PATHL7Handler && ((PATHL7Handler) handler).isReportData() ) ) {
			
			table = new PdfPTable(2);
			table.setWidthPercentage(100);
			this.isReportData = Boolean.TRUE;

			
		} else {
		
			if(isUnstructuredDoc){
				if(handler.getMsgType().equals("CLS"))
				{
					mainTableWidths = new float[] { 5f, 10f, 3f, 2f};
				} else
				{
					mainTableWidths = new float[] { 5f, 12f, 3f};
				}
			}else{
				if(handler.getMsgType().equals("ExcellerisON")) {
					mainTableWidths = new float[] {5f, 3f, 1f, 3f, 2f, 4f, 2f,2f };
				} else {
					mainTableWidths = new float[] {5f, 3f, 1f, 3f, 2f, 4f, 2f };
				}
			}
		
			table = new PdfPTable(mainTableWidths);
			table.setWidthPercentage(100);
			
			if(isUnstructuredDoc){
				table.setHeaderRows(1);
			}
			else{
				table.setHeaderRows(3);
			}
	
			// category name
			if(!isUnstructuredDoc){
				
				// blank filler
				cell.setPadding(3);
				cell.setPhrase(new Phrase("  "));				
				cell.setBorder(0);				
				if(handler.getMsgType().equals("ExcellerisON")) {
					cell.setColspan(8);
				} else { 
					cell.setColspan(7);
				}
				table.addCell(cell);
				
				// lab title. ie: PT Panel, CBC
				cell.setBorder(15);
				cell.setPadding(3);
				cell.setColspan(2);
				cell.setPhrase(new Phrase(header.replaceAll("<br\\s*/*>", "\n"),
						new Font(bf, 12, Font.BOLD)));
				table.addCell(cell);
				
				// place holder after lab title
				cell.setPhrase(new Phrase("  "));
				cell.setBorder(0);
				if(handler.getMsgType().equals("ExcellerisON")) {
					cell.setColspan(6);
				} else {
					cell.setColspan(5);
				}
				table.addCell(cell);
			}

			// table headers
			if(isUnstructuredDoc){
				cell.setColspan(1);
				cell.setBorder(Rectangle.BOX);	
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(new Color(210, 212, 255));
				cell.setPhrase(new Phrase("Test Name(s)", boldFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("Result", boldFont));
				table.addCell(cell);
				
				if(handler.getMsgType().equals("CLS"))
				{
					cell.setPhrase(new Phrase("Date/Time Collected", boldFont));
					table.addCell(cell);
					cell.setPhrase(new Phrase("Status", boldFont));
					table.addCell(cell); 
				} else 
				{
					cell.setPhrase(new Phrase("Date/Time Completed", boldFont));
					table.addCell(cell);
				}
				
			} else{
				
				if( (handler instanceof MEDITECHHandler) && ( "MIC".equals( ( (MEDITECHHandler) handler ).getSendingApplication() ) ) ) {

					cell.setPhrase(new Phrase(specimenSource, boldFont));
					if(handler.getMsgType().equals("ExcellerisON")) {
						cell.setColspan(8);
					} else {
						cell.setColspan(7);
					}
					
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					cell.setColspan(1);
					
					
					cell.setPhrase(new Phrase(specimenDescription, boldFont));
					cell.setColspan(7);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);
					cell.setColspan(1);
				}
				
				cell.setColspan(1);
				cell.setBorder(Rectangle.BOX);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(new Color(210, 212, 255));
				cell.setPhrase(new Phrase("Test Name(s)", boldFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("Result", boldFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("Abn", boldFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("Reference Range", boldFont));
				table.addCell(cell);
				cell.setPhrase(new Phrase("Units", boldFont));
				table.addCell(cell);
				if(handler.getMsgType().equals("CLS")){
					cell.setPhrase(new Phrase("Date/Time Collected", boldFont));
				}
				else
				{
					cell.setPhrase(new Phrase("Date/Time Completed", boldFont));
				}
				table.addCell(cell);
				cell.setPhrase(new Phrase("Status", boldFont));
				table.addCell(cell); 
				
				if(handler.getMsgType().equals("ExcellerisON")) { 
					cell.setPhrase(new Phrase("Lab Lic #", boldFont));
					table.addCell(cell);
				}
				
			}
		} // end alternate to Meditech Unstructured doc.
		
		// add test results
		int obrCount = handler.getOBRCount();

		// reset the borders
		cell.setBorder(Rectangle.NO_BORDER);

		if (handler.getMsgType().equals("MEDVUE")) {

			cell.setPhrase(new Phrase(handler.getRadiologistInfo(), boldFont));
			cell.setColspan(7);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell.setPaddingLeft(100);
			cell.setColspan(7);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPhrase(new Phrase(handler.getOBXComment(1, 1, 1)
					.replaceAll("<br\\s*/*>", "\n"), font));
			table.addCell(cell);
		} else {
			for (int j = 0; j < obrCount; j++) {
				boolean obrFlag = false;
				int obxCount = handler.getOBXCount(j);
				for (int k = 0; k < obxCount; k++) {
					
					if(handler.getMsgType().equals("ExcellerisON")) {
						lastLicenseNo = currentLicenseNo;
						currentLicenseNo = ((ExcellerisOntarioHandler)handler).getLabLicenseNo(j, k);
						String licenseName = ((ExcellerisOntarioHandler)handler).getLabLicenseName(j, k);
						if(!allLicenseNames.contains(licenseName)) {
							allLicenseNames.add(licenseName);
						}
					}
					
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					if (handler.getOBXCommentCount(j, k) > 0) {
						cell.setBorder( Rectangle.NO_BORDER );
					}
					cell.setBorderColor( Color.lightGray );
					cell.setBackgroundColor( Color.white );
					
					String obxName = handler.getOBXName(j, k);
					
					boolean isAllowedDuplicate = false;
					if(handler.getMsgType().equals("PATHL7")){
						//if the obxidentifier and result name are any of the following, they must be displayed (they are the Excepetion to Excelleris TX/FT duplicate result name display rules)
						if((handler.getOBXName(j, k).equals("Culture") && handler.getOBXIdentifier(j, k).equals("6463-4")) || 
								(handler.getOBXName(j, k).equals("Organism") && (handler.getOBXIdentifier(j, k).equals("X433") || handler.getOBXIdentifier(j, k).equals("X30011")))){
		   					isAllowedDuplicate = true;
		   				}
					}
					if (!handler.getOBXResultStatus(j, k).equals("TDIS")) {

						// ensure that the result is a real result
						if ((!handler.getOBXResultStatus(j, k).equals("DNS") && !obxName.equals("") && header.equals(handler.getObservationHeader(j, k))) 
								|| (handler.getMsgType().equals("EPSILON") && header.equals(handler.getOBXIdentifier(j,k)) && !obxName.equals("")) 
								|| (handler.getMsgType().equals("PFHT") && !obxName.equals("") && header.equals(handler.getObservationHeader(j,k)))) { // <<-- DNS only needed for
													// MDS messages
							String obrName = handler.getOBRName(j);
							// add the obrname if necessary
							if ( !obrFlag && !obrName.equals("")
									&& ( !(obxName.contains(obrName) && obxCount < 2 && !isUnstructuredDoc) ) ) {
	
								cell.setPhrase(new Phrase(obrName, boldFont));
								if(handler.getMsgType().equals("ExcellerisON")) { 
									cell.setColspan(8);
								} else {
									cell.setColspan(7);
								}
								cell.setBorderColor(Color.black);
								table.setWidthPercentage(100);
								table.addCell(cell);
								cell.setBorderColor( Color.lightGray );
								cell.setColspan(1);
								obrFlag = true;
							}

							// add the obx results and info
							Font lineFont = new Font(bf, 9, Font.NORMAL, getTextColor(handler,handler.getOBXAbnormalFlag(j,k)));

							if( this.isReportData ) {
								cell.setColspan(2);
								cell.setBorder(Rectangle.NO_BORDER);
								cell.setBorderColor(Color.white);
								cell.setPadding(0);
								cell.setPaddingLeft(10);

								if( handler instanceof PATHL7Handler &&
									"".equals( ( (PATHL7Handler) handler).getOBXSubId( j,k ) ) ) {
									PdfPTable infoTable = new PdfPTable(2);
									infoTable.setWidthPercentage(100);
									cell.setPhrase( new Phrase( handler.getOBXName(j, k).replaceAll("<br\\s*/*>", " "), lineFont ) );
									infoTable.addCell(cell);
									cell.setPhrase( new Phrase( handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", " "), lineFont ) );
									infoTable.addCell(cell);
									table.addCell(infoTable);
								} else {		
									String data = handler.getOBXResult(j, k);
									if("".equals(handler.getOBXResult(j, k))) {
										data = "\n";
									}
									int colspan = cell.getColspan();
									
									if(j == 0 && k == 0) {
										cell.setColspan(colspan-1);
										cell.setNoWrap(true);
										cell.setPhrase( new Phrase(data.replaceAll("<br\\s*/*>", "\n"), lineFont ) );
										table.addCell(cell);
										
										cell.setColspan(1);
										int ha = cell.getHorizontalAlignment();
										cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
										cell.setPhrase( new Phrase(handler.getTimeStamp(j, k), lineFont ) );
										table.addCell(cell);
										cell.setHorizontalAlignment(ha);
									} else {
										cell.setPhrase( new Phrase(data.replaceAll("<br\\s*/*>", "\n"), lineFont ) );
										table.addCell(cell);
									}
									
								}
	
							} else if( isUnstructuredDoc ){

								//if there are duplicate obxNames, display only the first 
								cell.setBorder(Rectangle.NO_BORDER);				
								cell.setPadding(0);
								cell.setPaddingTop(3);
								
								if( ! "MEDITECH".equalsIgnoreCase( handler.getMsgType() ) ) { 
									if( ( ( handler.getOBXIdentifier(j, k).equalsIgnoreCase(handler.getOBXIdentifier(j, k-1)) && (obxCount>1) ) 
											|| ( obxName.equalsIgnoreCase(obrName) ) ) ){
										cell.setPhrase(new Phrase("", lineFont));
										table.addCell(cell);
									}else {
										String indent = "   ";
										if(handler.getMsgType().equals("ExcellerisON")) {
											indent="";
										}
										if(!StringUtils.isEmpty(indent)) {
											cell.setPaddingLeft(3);
											indent="";
										}
										cell.setPhrase(new Phrase((obrFlag ? indent : "")+ obxName, lineFont));
										table.addCell(cell);
										cell.setPaddingLeft(0);
									}
								}
								
								cell.setPhrase( new Phrase( handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", "\n").replace("\t","\u00a0\u00a0\u00a0\u00a0"), lineFont ) );
								table.addCell(cell);
	
								//if there are duplicate Times, display only the first 
								if(! "MEDITECH".equalsIgnoreCase(handler.getMsgType() ) ){ 
									if( handler.getTimeStamp(j, k).equals(handler.getTimeStamp(j, k-1)) && (obxCount>1) ){
										cell.setPhrase(new Phrase("", lineFont));		
										table.addCell(cell); 
									}else {
										cell.setPhrase(new Phrase(handler.getTimeStamp(j, k), lineFont));		
										table.addCell(cell);
									}
								}
								
								if(handler.getMsgType().equals("CLS")) {
									cell.setPhrase(new Phrase(handler.getOBXResultStatus(j, k), lineFont));
									table.addCell(cell);
								}
							
								if(handler.getMsgType().equals("ExcellerisON")) { 
									cell.setPhrase(new Phrase(!currentLicenseNo.equals(lastLicenseNo)?currentLicenseNo:"", lineFont));
									table.addCell(cell);
								}
								cell.setBorder(Rectangle.BOTTOM);
								cell.setPadding(5);
							} else {

								if(!isAllowedDuplicate 
										&& (obxCount>1) 
										&& k > 0 
										&& handler.getOBXIdentifier(j, k).equals(handler.getOBXIdentifier(j, k-1)) 
										&& (handler.getOBXValueType(j, k).equals("TX") || handler.getOBXValueType(j, k).equals("FT"))){
									cell.setPhrase(new Phrase("", lineFont));
									table.addCell(cell);
								}else{
									String indent = "   ";
									if(handler.getMsgType().equals("ExcellerisON")) {
										indent="";
									}
									if(!StringUtils.isEmpty(indent)) {
										cell.setPaddingLeft(3);
										indent="";
									}
									cell.setPhrase(new Phrase((obrFlag ? indent : "") + obxName, lineFont));
									table.addCell(cell);
									cell.setPaddingLeft(0);
									
								}
								
								boolean isLongText =false;
								
								if((handler.getMsgType().equals("ExcellerisON") || handler.getMsgType().equals("PATHL7")) && StringUtils.isEmpty(handler.getOBXReferenceRange(j, k)) ) {
									if("FT".equals(handler.getOBXValueType(j,k)) && (handler.getOBXReferenceRange(j,k).isEmpty() && handler.getOBXUnits(j,k).isEmpty())) {
										isLongText=true;
									}
								}
								
								if( handler.getMsgType().equals("PATHL7") ){
									
									if(handler.getOBXValueType(j,k).equals("ED")) {
										if(((PATHL7Handler)handler).isLegacy(j,k)) {
											embeddedDocumentsToAppend.add(((PATHL7Handler)handler).getLegacyOBXResult(j, k));
										} else {
											embeddedDocumentsToAppend.add(handler.getOBXResult(j, k));
										}
											
										cell.setPhrase(new Phrase("PDF Report (Appended to end of Laboratory Report)", lineFont));
										table.addCell(cell);
									} else {
										cell.setPhrase(new Phrase(handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", "\n").replace("\t","\u00a0\u00a0\u00a0\u00a0"), lineFont));
										//if this PATHL7 result is from CDC/SG and is greater than 100 characters
										if((handler.getOBXResult(j, k).length() > 100) && (handler.getPatientLocation().equals("SG") || handler.getPatientLocation().equals("CDC"))){

											//if the Abn, Reference Range and Units are empty or equal to null, give the long result the use of those columns
											if(( handler.getOBXAbnormalFlag(j, k) == null ||handler.getOBXAbnormalFlag(j, k).isEmpty()) &&
											( handler.getOBXReferenceRange(j, k) == null || handler.getOBXReferenceRange(j, k).isEmpty()) &&
											(handler.getOBXUnits(j, k) == null || handler.getOBXUnits(j, k).isEmpty())){
												isLongText = true;
												cell.setColspan(4);
												table.addCell(cell);
											}else{
												//else use the 6 remaining columns, and add a new empty cell that takes the first two columns(Test & Results). 
												//This will allow the corresponding Abn, RR and Units to be printed beneath the long result in the appropriate columns
												cell.setColspan(6);
												table.addCell(cell);
												cell.setPhrase(new Phrase("", lineFont));
												cell.setColspan(2);
												table.addCell(cell);
											}
										}else{
											if(isLongText) {
												cell.setColspan(4);
											}
											// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
											table.addCell(cell);
										}
										cell.setColspan(1);
									}
									
									
									
								} else { // end PATHHL7 labs
									
									if(isLongText) {
										cell.setColspan(4);
									}
									if(handler instanceof ExcellerisOntarioHandler &&  handler.getOBXValueType(j,k).equals("ED")) {
										embeddedDocumentsToAppend.add(handler.getOBXResult(j, k));
										cell.setPhrase(new Phrase("PDF Report (Appended to end of Laboratory Report)", lineFont));
										table.addCell(cell);
									} else {
										cell.setPhrase(new Phrase(handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", "\n"), lineFont));
										table.addCell(cell);
									}
									cell.setColspan(1);
								}

								String abnFlag = handler.getOBXAbnormalFlag(j, k);
								
								if(!isLongText){//if the Abn, RR and Unit columns have not been occupied above
									if(handler.getMsgType().equals("PATHL7")){
										cell.setPhrase(new Phrase(abnFlag, lineFont));
									} else if("CLS".equals(handler.getMsgType())) {
										cell.setPhrase(new Phrase(
											(handler.isOBXAbnormal(j, k) ?
												handler.getOBXAbnormalFlag(j, k) :
												""),
											lineFont));
									} else if("ExcellerisON".equals(handler.getMsgType())) {
										cell.setPhrase(new Phrase(StringUtils.trimToEmpty(abnFlag), lineFont));
									} else {
										if (abnFlag == null || abnFlag.trim().equals(""))
											abnFlag = "N";
										cell.setPhrase(new Phrase(abnFlag, lineFont));
									}
									
									table.addCell(cell);
									cell.setPhrase(new Phrase(handler.getOBXReferenceRange(j, k), lineFont));
									table.addCell(cell);
									cell.setPhrase(new Phrase(handler.getOBXUnits(j, k), lineFont));
									table.addCell(cell);
								}// end of isLongText
								
								cell.setPhrase(new Phrase(handler.getTimeStamp(j, k), lineFont));
								table.addCell(cell);
								cell.setPhrase(new Phrase(handler.getOBXResultStatus(j, k), lineFont));
								table.addCell(cell);
								
								if(handler.getMsgType().equals("ExcellerisON")) { 
									cell.setPhrase(new Phrase(!currentLicenseNo.equals(lastLicenseNo)?currentLicenseNo:"", lineFont));
									table.addCell(cell);
									
								}
							} // end else not unstructured.
							
							if(!handler.getMsgType().equals("PFHT")) {
								// add obx comments
								if (handler.getOBXCommentCount(j, k) > 0) {
									cell.setBorder(Rectangle.BOTTOM);
									
								//	cell.setBorderColor(Color.white);

									for (int l = 0; l < handler.getOBXCommentCount(j, k); l++) {
	
										cell.setPhrase(new Phrase("",font));
										cell.setColspan(1);
										table.addCell(cell);
										
										if(handler.getMsgType().equals("ExcellerisON")) { 
											cell.setColspan(8);
										} else {
											cell.setColspan(7);
										}
										cell.setPhrase(new Phrase(handler.getOBXComment(j, k, l).replaceAll("<br\\s*/*>", "\n"), font));
										table.addCell(cell);
	
									}

									cell.setBorderColor(Color.lightGray);
									cell.setColspan(1);
								}
								cell.setColspan(1);
							}
						// end if not DNS
						} else if (
								( handler.getMsgType().equals("EPSILON") && header.equals(handler.getOBXIdentifier(j,k)) && obxName.equals("") ) 
								|| ( handler.getMsgType().equals("PFHT") && obxName.equals("")&& header.equals(handler.getObservationHeader(j,k)) )
								|| ( handler.getMsgType().equals("MEDITECH") && obxName.equals("") ) 
							){

							cell.setBorder(Rectangle.NO_BORDER);
							cell.setBorderColor(Color.white);
							cell.setPadding(0);
							cell.setPaddingLeft(10);
							cell.setColspan(7);

							table.setWidthPercentage(100);
							cell.setPhrase( new Phrase( handler.getOBXResult(j, k).replaceAll( "<br\\s*/*>", "\n" ), font ) );							
							table.addCell(cell);
							
							cell.setColspan(1);
							cell.setBorder(Rectangle.BOTTOM);
							cell.setBorderColor(Color.lightGray);
							cell.setPadding(5);
						}
						if (handler.getMsgType().equals("PFHT") && !handler.getNteForOBX(j,k).equals("") && handler.getNteForOBX(j,k)!=null) {

							cell.setPaddingLeft(100);
							cell.setColspan(7);

							cell.setPhrase(new Phrase(handler.getNteForOBX(j, k).replaceAll("<br\\s*/*>", "\n"),font));
							table.addCell(cell);
							cell.setPaddingLeft(5);
							cell.setColspan(1);
							
							if (handler.getOBXCommentCount(j, k) > 0) {
								cell.setBorder(Rectangle.BOTTOM);
								cell.setColspan(7);

								for (int l = 0; l < handler.getOBXCommentCount(
										j, k); l++) {

									cell.setPhrase(new Phrase(handler
											.getOBXComment(j, k, l).replaceAll(
													"<br\\s*/*>", "\n"), font));
									table.addCell(cell);

								}

								cell.setColspan(1);
							}
						}
						
					} else {
						if (handler.getOBXCommentCount(j, k) > 0) {

							if(handler.getMsgType().equals("ExcellerisON")) { 
								cell.setColspan(8);
							} else {
								cell.setColspan(7);
							}

							for (int l = 0; l < handler
									.getOBXCommentCount(j, k); l++) {

								cell.setPhrase(new Phrase(handler
										.getOBXComment(j, k, l).replaceAll(
												"<br\\s*/*>", "\n"), font));
								table.addCell(cell);

							}

							cell.setColspan(1);
						}
					} // if (!handler.getOBXResultStatus(j, k).equals("TDIS"))
				}
				
			if (!handler.getMsgType().equals("PFHT")) {
				// add obr comments
				if (handler.getObservationHeader(j, 0).equals(header)) {
					if(handler.getMsgType().equals("ExcellerisON")) { 
						cell.setColspan(8);
					} else {
						cell.setColspan(7);
					}
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					for (int k = 0; k < handler.getOBRCommentCount(j); k++) {
						// the obrName should only be set if it has not been
						// set already which will only have occured if the
						// obx name is "" or if it is the same as the obr name
						if (!obrFlag && handler.getOBXName(j, 0).equals("")) {

							cell.setPhrase(new Phrase(handler.getOBRName(j),
									boldFont));
							table.addCell(cell);
							obrFlag = true;
						}

						if (handler.getMsgType().equals("TRUENORTH")) {
							try {
								Phrase phrase= new Phrase();
								StringReader strReader = new StringReader(handler.getOBRComment(j, k));
								@SuppressWarnings("rawtypes")
                                ArrayList p = HTMLWorker.parseToList(strReader, null);
								strReader.close();
								for (int h=0; h<p.size();h++) {
									phrase.add(p.get(h));
									phrase.add("\n");
								}
								cell.setPhrase(phrase);
							} catch (Exception e) {
					            throw new ExceptionConverter(e);
					        }
							
						} else {
							int colSpan = cell.getColspan();
							cell.setColspan(1);
							cell.setPhrase(new Phrase("",font));
							table.addCell(cell);
							cell.setColspan(colSpan-1);
							cell.setPhrase(new Phrase(handler.getOBRComment(j, k)
									.replaceAll("<br\\s*/*>", "\n"), font));
						}
						table.addCell(cell);
						cell.setPadding(3);
					}
					cell.setColspan(1);
				}
			}
			} // for (j)

		}// if (isMEDVUE)

		document.add(table);

	}


    /*
     *  getTextColor will return the the color corresponding to the abnormal
     *  status of the result.
     */
    private Color getTextColor(MessageHandler handler, String abn){
        Color ret = Color.BLACK;
        if ( abn != null && ( abn.equals("A") || abn.startsWith("H")) ){
            ret = Color.RED;
        }else if ( abn != null && abn.startsWith("L")){
            ret = Color.BLUE;
        }
        
        return ret;
    }


    /*
     *  createInfoTable creates and adds the table at the top of the document
     *  which contains the patient and lab information
     */
    private void createInfoTable() throws DocumentException{

        //Create patient info table
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        float[] pInfoWidths = {2f, 4f, 3f, 2f};
        PdfPTable pInfoTable = new PdfPTable(pInfoWidths);
        cell.setPhrase(new Phrase("Patient Name: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getPatientName(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Home Phone: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getHomePhone(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Date of Birth: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getDOB(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Work Phone: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getWorkPhone(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Age: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getAge(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Sex: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getSex(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Health #: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getHealthNum(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getMsgType().equals("ExcellerisON")?"Reported by: ":"Patient Location: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getPatientLocation(), font));
        pInfoTable.addCell(cell);

        //Create results info table
        PdfPTable rInfoTable = new PdfPTable(2);
        cell.setPhrase(new Phrase("Date of Service: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getServiceDate(), font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Date Received: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(dateLabReceived, font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Report Status: ", boldFont));
        rInfoTable.addCell(cell);
        if(handler.getMsgType().equals("PATHL7")){
        	cell.setPhrase(new Phrase((handler.getOrderStatus().equals("F") ? "Final" : (handler.getOrderStatus().equals("C") ? "Corrected" : "Preliminary")), font));
        	rInfoTable.addCell(cell);
        }else{
        	//(  handler.getOrderStatus().equals("X") ? "DELETED": handler.getOrderStatus())
        	
        cell.setPhrase(new Phrase((handler.getOrderStatus().equals("F") ? "Final" : (handler.getOrderStatus().equals("C") ? "Corrected" :  handler.getOrderStatus().equals("P") ? "Partial": handler.getOrderStatus().equals("X") ? "DELETED": handler.getOrderStatus())), font));
        rInfoTable.addCell(cell);}
        cell.setPhrase(new Phrase("Client Ref. #: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getClientRef(), font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Accession #: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getAccessionNum(), font));
        rInfoTable.addCell(cell);
        if(handler.getMsgType().equals("ExcellerisON") && !((ExcellerisOntarioHandler)handler).getAlternativePatientIdentifier().isEmpty()) {
        	cell.setPhrase(new Phrase("Reference #: ", boldFont));
            rInfoTable.addCell(cell);
            cell.setPhrase(new Phrase(((ExcellerisOntarioHandler)handler).getAlternativePatientIdentifier(), font));
            rInfoTable.addCell(cell);
        }

        //Create client table
        float[] clientWidths = {2f, 3f};
        Phrase clientPhrase = new Phrase();
        PdfPTable clientTable = new PdfPTable(clientWidths);
        clientPhrase.add(new Chunk("Requesting Client:  ", boldFont));
        clientPhrase.add(new Chunk(handler.getDocName(), font));
        cell.setPhrase(clientPhrase);
        clientTable.addCell(cell);

        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("cc: Client:  ", boldFont));
        clientPhrase.add(new Chunk(handler.getCCDocs(), font));
        cell.setPhrase(clientPhrase);
        clientTable.addCell(cell);

        //Create header info table
        float[] tableWidths = {2f, 1f};
        PdfPTable table = new PdfPTable(tableWidths);
        if ( multiID != null && multiID.length > 1 ){
            cell = new PdfPCell(new Phrase("Version: "+versionNum+" of "+multiID.length, boldFont));
            cell.setBackgroundColor(new Color(210, 212, 255));
            cell.setPadding(3);
            cell.setColspan(2);
            table.addCell(cell);
        }
        cell = new PdfPCell(new Phrase("Detail Results: Patient Info", boldFont));
        cell.setBackgroundColor(new Color(210, 212, 255));
        cell.setPadding(3);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Results Info", boldFont));
        table.addCell(cell);

        // add the created tables to the document
        table = addTableToTable(table, pInfoTable, 1);
        table = addTableToTable(table, rInfoTable, 1);
        table = addTableToTable(table, clientTable, 2);

        table.setWidthPercentage(100);

        document.add(table);
    }
    /**
     * Since pdfPtable used in createInfotable() is not properly supported in RTF, 
     * add the patient information to the RTF document using chunks and paragraphs
     */
    private void addRtfPatientInfo() throws DocumentException{
    	Paragraph patientInfo = new Paragraph();
    	
    	Phrase clientPhrase = new Phrase();
    	clientPhrase.add(new Chunk("Patient Name: ", boldFont));
        clientPhrase.add(new Chunk(handler.getPatientName() +"\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Home Phone: ", boldFont));
        clientPhrase.add(new Chunk(handler.getHomePhone()+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Date of Birth: ", boldFont));
        clientPhrase.add(new Chunk(handler.getDOB()+"\t\t\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Work Phone: ", boldFont));
        clientPhrase.add(new Chunk(handler.getWorkPhone()+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Age: ", boldFont));
        clientPhrase.add(new Chunk(handler.getAge()+"\t\t\t\t\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Sex: ", boldFont));
        clientPhrase.add(new Chunk(handler.getSex()+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Health #: ", boldFont));
        clientPhrase.add(new Chunk(handler.getHealthNum()+"\t\t\t\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk(handler.getMsgType().equals("ExcellerisON")?"Reported by: ":"Patient Location: ", boldFont));
        clientPhrase.add(new Chunk(handler.getPatientLocation()+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Date of Service: ", boldFont));
        clientPhrase.add(new Chunk(handler.getServiceDate()+"\t\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Date Received: ", boldFont));
        clientPhrase.add(new Chunk(dateLabReceived+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Report Status: ", boldFont));
        clientPhrase.add(new Chunk((handler.getOrderStatus().equals("F") ? "Final" : (handler.getOrderStatus().equals("C") ? "Corrected" : "Preliminary"))+"\t\t\t\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Client Ref. #: ", boldFont));
        clientPhrase.add(new Chunk(handler.getClientRef()+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Accession #: ", boldFont));
        clientPhrase.add(new Chunk(handler.getAccessionNum()+"\t\t\t\t\t", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("Requesting Client:  ", boldFont));
        clientPhrase.add(new Chunk(handler.getDocName()+"\n", font));
        patientInfo.add(clientPhrase);
        
        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("cc: Client:  ", boldFont));
        clientPhrase.add(new Chunk(handler.getCCDocs()+"\n\n", font));
        patientInfo.add(clientPhrase);
        
        document.add(patientInfo);
    }
    /*
     *  addTableToTable(PdfPTable main, PdfPTable add) adds the table 'add' as
     *  a cell spanning 'colspan' columns to the table main.
     */
    private PdfPTable addTableToTable(PdfPTable main, PdfPTable add, int colspan){
        PdfPCell cell = new PdfPCell(add);
        cell.setPadding(3);
        cell.setColspan(colspan);
        main.addCell(cell);
        return main;
    }


    /*
     *  onEndPage is a page event that occurs when a page has finished being created.
     *  It is used to add header and footer information to each page.
     */
    public void onEndPage(PdfWriter writer, Document document){
        try {

            Rectangle page = document.getPageSize();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            int pageNum = document.getPageNumber();
            float width = page.getWidth();
            float height = page.getHeight();

            //add patient name header for every page but the first.
            if (pageNum > 1){
                cb.beginText();
                cb.setFontAndSize(bf, 8);
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, handler.getPatientName(), 575, height - 30, 0);
                cb.endText();

            }


        // throw any exceptions
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }        
    }

	public boolean isUnstructuredDoc() {
		return isUnstructuredDoc;
	}

	public void setUnstructuredDoc(boolean isUnstructuredDoc) {
		this.isUnstructuredDoc = isUnstructuredDoc;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}
	
	public void closeOs() throws IOException {
		if( this.os != null ) {
			flushOs();
			os.close();
		}
	}
	
	private void flushOs() throws IOException {
		if( this.os != null ) {
			os.flush();
		}
	}

	public MessageHandler getHandler() {
		return handler;
	}

	public void setHandler(MessageHandler handler) {
		this.handler = handler;
	}

	public List<MessageHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<MessageHandler> handlers) {
		this.handlers = handlers;
	}

}
