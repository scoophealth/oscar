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
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.CLSHandler;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.PATHL7Handler;
import oscar.util.UtilDateUtilities;

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


/**
 *
 * @author wrighd
 */
public class LabPDFCreator extends PdfPageEventHelper{
    private OutputStream os;
    private boolean isUnstructuredDoc = false;
    private MessageHandler handler;
    List<MessageHandler>handlers = new ArrayList<MessageHandler>();
    
    private int versionNum;
    private String[] multiID;
    private String id;

    private Document document;
    private BaseFont bf;
    private Font font;
    private Font boldFont;
    private String dateLabReceived;

	public static byte[] getPdfBytes(String segmentId, String providerNo) throws IOException, DocumentException
    {
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();

    	LabPDFCreator labPDFCreator=new LabPDFCreator(baos, segmentId, providerNo);
    	labPDFCreator.printPdf();

    	return(baos.toByteArray());
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
     //   redFont = new Font(bf, 11, Font.NORMAL, Color.RED);
        
        //add the patient information
        addRtfPatientInfo();
        
        //add the results
    	writer.importRtfDocument(rtfStream, null);
    	
    	document.close();
    	os.flush();
    }
    public void printPdf() throws IOException, DocumentException{

        // check that we have data to print
        if (handler == null)
            throw new DocumentException();

        //response.setContentType("application/pdf");  //octet-stream
        //response.setHeader("Content-Disposition", "attachment; filename=\""+handler.getPatientName().replaceAll("\\s", "_")+"_LabReport.pdf\"");

        //Create the document we are going to write to
        document = new Document();
        //PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        // PdfWriter writer = PdfWriter.getInstance(document, os);
        PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);

        //Set page event, function onEndPage will execute each time a page is finished being created
        writer.setPageEvent(this);

        document.setPageSize(PageSize.LETTER);
        document.addTitle("Title of the Document");
        document.addCreator("OSCAR");
        document.open();

        //Create the fonts that we are going to use
        bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, 9, Font.NORMAL);
        boldFont = new Font(bf, 10, Font.BOLD);
      //  redFont = new Font(bf, 9, Font.NORMAL, Color.RED);

        // add the header table containing the patient and lab info to the document
        createInfoTable();

        // add the tests and test info for each header
        ArrayList<String> headers = handler.getHeaders();
        for (int i=0; i < headers.size(); i++){
            addLabCategory( headers.get(i) ,null);
        }
        
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

        document.close();

        os.flush();
    }
	 		

    /*
	 * Given the name of a lab category this method will add the category
	 * header, the test result headers and the test results for that category.
	 */
	private void addLabCategory(String header, MessageHandler extraHandler) throws DocumentException {
		MessageHandler handler = (extraHandler!=null)?extraHandler:this.handler;
		if(handler.getMsgType().equals("PATHL7")){
			this.isUnstructuredDoc = ((PATHL7Handler) handler).unstructuredDocCheck(header);
		} else if(handler.getMsgType().equals("CLS"))
		{
			this.isUnstructuredDoc = ((CLSHandler) handler).isUnstructured();
		}
		
		float[] mainTableWidths;
		if(isUnstructuredDoc){
			if(handler.getMsgType().equals("CLS"))
			{
				mainTableWidths = new float[] { 5f, 10f, 3f, 2f};
			} else
			{
				mainTableWidths = new float[] { 5f, 12f, 3f};
			}
		}else{
			mainTableWidths = new float[] {5f, 3f, 1f, 3f, 2f, 4f, 2f };
		}
		
		PdfPTable table = new PdfPTable(mainTableWidths);
		if(isUnstructuredDoc){
			table.setHeaderRows(1);}
		else{
		table.setHeaderRows(3);}
		table.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell();
		// category name
		if(!isUnstructuredDoc){
		cell.setPadding(3);
		cell.setPhrase(new Phrase("  "));
		cell.setBorder(0);
		cell.setColspan(7);
		table.addCell(cell);
		cell.setBorder(15);
		cell.setPadding(3);
		cell.setColspan(2);
		cell.setPhrase(new Phrase(header.replaceAll("<br\\s*/*>", "\n"),
				new Font(bf, 12, Font.BOLD)));
		table.addCell(cell);
		cell.setPhrase(new Phrase("  "));
		cell.setBorder(0);
		cell.setColspan(5);
		table.addCell(cell);}

		// table headers
		if(isUnstructuredDoc){
			cell.setColspan(1);
			cell.setBorder(15);
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
		cell.setColspan(1);
		cell.setBorder(15);
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
		table.addCell(cell); }


		// add test results
		int obrCount = handler.getOBRCount();
		int linenum = 0;
		cell.setBorder(12);
		cell.setBorderColor(Color.BLACK); // cell.setBorderColor(Color.WHITE);
		cell.setBackgroundColor(new Color(255, 255, 255));

		if (handler.getMsgType().equals("MEDVUE")) {

			//cell.setBackgroundColor(getHighlightColor(linenum));
			linenum++;
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
						if ((!handler.getOBXResultStatus(j, k).equals("DNS")
								&& !obxName.equals("")
								&& header.equals(handler.getObservationHeader(j, k))) || 
								(handler.getMsgType().equals("EPSILON") && header.equals(handler.getOBXIdentifier(j,k)) && !obxName.equals("")) 
								|| (handler.getMsgType().equals("PFHT") && !obxName.equals("") && header.equals(handler.getObservationHeader(j,k)))) { // <<-- DNS only needed for
													// MDS messages
							String obrName = handler.getOBRName(j);
							// add the obrname if necessary
							if (!obrFlag
									&& !obrName.equals("")
									&& (!(obxName.contains(obrName) && obxCount < 2 && !isUnstructuredDoc))) {
								// cell.setBackgroundColor(getHighlightColor(linenum));
								linenum++;
								cell.setPhrase(new Phrase(obrName, boldFont));
								cell.setColspan(7);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								table.addCell(cell);
								cell.setColspan(1);
								obrFlag = true;
							}

							// add the obx results and info
							Font lineFont = new Font(bf, 8, Font.NORMAL,
									getTextColor(handler,handler.getOBXAbnormalFlag(j,
											k)));
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							if(isUnstructuredDoc){
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								//if there are duplicate obxNames, display only the first 
								if((handler.getOBXIdentifier(j, k).equalsIgnoreCase(handler.getOBXIdentifier(j, k-1)) && (obxCount>1)) || (obxName.equalsIgnoreCase(obrName))){
									cell.setPhrase(new Phrase("", lineFont));
									table.addCell(cell);
								}else {
									cell.setPhrase(new Phrase((obrFlag ? "   " : "")+ obxName, lineFont));
									table.addCell(cell);
								}
								cell.setPhrase(new Phrase(handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", "\n").replace("\t","\u00a0\u00a0\u00a0\u00a0"), lineFont));				
								table.addCell(cell);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								//if there are duplicate Times, display only the first 
								if(handler.getTimeStamp(j, k).equals(handler.getTimeStamp(j, k-1)) && (obxCount>1)){
									cell.setPhrase(new Phrase("", lineFont));		
									table.addCell(cell); 
								}else {
									cell.setPhrase(new Phrase(handler.getTimeStamp(j, k), lineFont));		
									table.addCell(cell);
								}
								if(handler.getMsgType().equals("CLS"))
								{
									cell.setPhrase(new Phrase(handler
											.getOBXResultStatus(j, k), lineFont));
									table.addCell(cell);
								}
							} else{
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							if(!isAllowedDuplicate && (obxCount>1) && k > 0 && handler.getOBXIdentifier(j, k).equals(handler.getOBXIdentifier(j, k-1)) && (handler.getOBXValueType(j, k).equals("TX") || handler.getOBXValueType(j, k).equals("FT"))){
								cell.setPhrase(new Phrase("", lineFont));
								table.addCell(cell);
							}
							else{
							cell.setPhrase(new Phrase((obrFlag ? "   " : "")
									+ obxName, lineFont));
							table.addCell(cell);}
							boolean isLongText =false;
							if(handler.getMsgType().equals("PATHL7")){
								cell.setPhrase(new Phrase(handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", "\n").replace("\t","\u00a0\u00a0\u00a0\u00a0"), lineFont));
								//if this PATHL7 result is from CDC/SG and is greater than 100 characters
								if((handler.getOBXResult(j, k).length() > 100) && (handler.getPatientLocation().equals("SG") || handler.getPatientLocation().equals("CDC"))){
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									//if the Abn, Reference Range and Units are empty or equal to null, give the long result the use of those columns
									if(( handler.getOBXAbnormalFlag(j, k) == null ||handler.getOBXAbnormalFlag(j, k).isEmpty()) &&
									( handler.getOBXReferenceRange(j, k) == null || handler.getOBXReferenceRange(j, k).isEmpty()) &&
									(handler.getOBXUnits(j, k) == null || handler.getOBXUnits(j, k).isEmpty())){
										isLongText = true;
										cell.setColspan(4);
										table.addCell(cell);
									}else{//else use the 6 remaining columns, and add a new empty cell that takes the first two columns(Test & Results). 
										//This will allow the corresponding Abn, RR and Units to be printed beneath the long result in the appropriate columns
										cell.setColspan(6);
										table.addCell(cell);
										cell.setPhrase(new Phrase("", lineFont));
										cell.setColspan(2);
										table.addCell(cell);
									}
								}else{
									cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
									table.addCell(cell);}
							}else{
							cell.setPhrase(new Phrase(handler
									.getOBXResult(j, k).replaceAll(
											"<br\\s*/*>", "\n"), lineFont));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(cell);}
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							
							String abnFlag = handler.getOBXAbnormalFlag(j, k);
							if(!isLongText){//if the Abn, RR and Unit columns have not been occupied above
								if(handler.getMsgType().equals("PATHL7")){
									cell.setPhrase(new Phrase(abnFlag, lineFont));
								} 
								else if("CLS".equals(handler.getMsgType())) 
								{
									cell.setPhrase(new Phrase(
										(handler.isOBXAbnormal(j, k) ?
											handler.getOBXAbnormalFlag(j, k) :
											""),
										lineFont));
								}
								else
								{
									if (abnFlag == null || abnFlag.trim().equals(""))
										abnFlag = "N";
									cell.setPhrase(new Phrase(
										abnFlag, lineFont));
								}
								table.addCell(cell);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								cell.setPhrase(new Phrase(handler
										.getOBXReferenceRange(j, k), lineFont));
								table.addCell(cell);
								cell.setPhrase(new Phrase(
										handler.getOBXUnits(j, k), lineFont));
								table.addCell(cell);}// end of isLongText
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setPhrase(new Phrase(handler
									.getTimeStamp(j, k), lineFont));
							table.addCell(cell);
							cell.setPhrase(new Phrase(handler
									.getOBXResultStatus(j, k), lineFont));
							table.addCell(cell);}
							
						if(!handler.getMsgType().equals("PFHT")) {
							// add obx comments
							if (handler.getOBXCommentCount(j, k) > 0) {
								// cell.setBackgroundColor(getHighlightColor(linenum));
								linenum++;
								cell.setPaddingLeft(100);
								cell.setColspan(7);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								for (int l = 0; l < handler.getOBXCommentCount(
										j, k); l++) {

									cell.setPhrase(new Phrase(handler
											.getOBXComment(j, k, l).replaceAll(
													"<br\\s*/*>", "\n"), font));
									table.addCell(cell);

								}
								cell.setPadding(3);
								cell.setColspan(1);
							}
						}
						// if (DNS)
						} else if ((handler.getMsgType().equals("EPSILON") && header.equals(handler.getOBXIdentifier(j,k)) && obxName.equals("")) || (handler.getMsgType().equals("PFHT") && obxName.equals("")&& header.equals(handler.getObservationHeader(j,k)))){
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							cell.setPaddingLeft(100);
							cell.setColspan(7);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setPhrase(new Phrase(handler
									.getOBXResult(j, k).replaceAll(
											"<br\\s*/*>", "\n"), font));
							table.addCell(cell);
							cell.setPadding(3);
							cell.setColspan(1);
						
						}
						if (handler.getMsgType().equals("PFHT") && !handler.getNteForOBX(j,k).equals("") && handler.getNteForOBX(j,k)!=null) {
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							cell.setPaddingLeft(100);
							cell.setColspan(7);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setPhrase(new Phrase(handler.getNteForOBX(j, k).replaceAll("<br\\s*/*>", "\n"),font));
							table.addCell(cell);
							cell.setPadding(3);
							cell.setColspan(1);
							
							if (handler.getOBXCommentCount(j, k) > 0) {
								// cell.setBackgroundColor(getHighlightColor(linenum));
								linenum++;
								cell.setPaddingLeft(100);
								cell.setColspan(7);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								for (int l = 0; l < handler.getOBXCommentCount(
										j, k); l++) {

									cell.setPhrase(new Phrase(handler
											.getOBXComment(j, k, l).replaceAll(
													"<br\\s*/*>", "\n"), font));
									table.addCell(cell);

								}
								cell.setPadding(3);
								cell.setColspan(1);
							}
						}
					}else {
						if (handler.getOBXCommentCount(j, k) > 0) {
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							cell.setPaddingLeft(100);
							cell.setColspan(7);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							for (int l = 0; l < handler
									.getOBXCommentCount(j, k); l++) {

								cell.setPhrase(new Phrase(handler
										.getOBXComment(j, k, l).replaceAll(
												"<br\\s*/*>", "\n"), font));
								table.addCell(cell);

							}
							cell.setPadding(3);
							cell.setColspan(1);
						}
					} // if (!handler.getOBXResultStatus(j, k).equals("TDIS"))
				}
				
			if (!handler.getMsgType().equals("PFHT")) {
				// add obr comments
				if (handler.getObservationHeader(j, 0).equals(header)) {
					cell.setColspan(7);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					for (int k = 0; k < handler.getOBRCommentCount(j); k++) {
						// the obrName should only be set if it has not been
						// set already which will only have occured if the
						// obx name is "" or if it is the same as the obr name
						if (!obrFlag && handler.getOBXName(j, 0).equals("")) {
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;

							cell.setPhrase(new Phrase(handler.getOBRName(j),
									boldFont));
							table.addCell(cell);
							obrFlag = true;
						}

						// cell.setBackgroundColor(getHighlightColor(linenum));
						linenum++;
						//cell.setPaddingLeft(100);
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
     *  getHighlightColor will return the background color of the current result
     *  line, this is determined by the line number
     */
 /*
    private Color getHighlightColor(int linenum){
        Color ret = new Color(225,225,255);
        if ((linenum % 2) == 1)
            ret = new Color(245,245,255);

        return ret;
    }
*/
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
        cell.setPhrase(new Phrase("Patient Location: ", boldFont));
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
        cell.setPhrase(new Phrase((handler.getOrderStatus().equals("F") ? "Final" : (handler.getOrderStatus().equals("C") ? "Corrected" : "Partial")), font));
        rInfoTable.addCell(cell);}
        cell.setPhrase(new Phrase("Client Ref. #: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getClientRef(), font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Accession #: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getAccessionNum(), font));
        rInfoTable.addCell(cell);

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
        if (multiID.length > 1){
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
        clientPhrase.add(new Chunk("Patient Location: ", boldFont));
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

}
