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
package oscar.oscarLab.ca.all.pageUtil;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;
import oscar.oscarLab.ca.all.util.Utilities;


public class OLISLabPDFCreator extends PdfPageEventHelper{
    private OutputStream os;

    private final String FINAL_CODE = "F";
    private final String REPORT_FINAL = "Final";
    private final String REPORT_PARTIAL = "Partial";
    
  
    private OLISHL7Handler handler;
    private int versionNum;
    private String[] multiID;
    private String id;

    private Document document;
    private BaseFont bf;
    private BaseFont cf;
    private Font font;
    private Font boldFont;
    private Font redFont;
    private Font blueFont;
    private Font categoryHeadFont;
    private Font commentFont;
    private Font subscriptFont;
  //  private String dateLabReceived;
    
    private String category = "";
	private String newCategory = "";

	private Logger logger = MiscUtils.getLogger();
	
	public static byte[] getPdfBytes(String segmentId, String providerNo) throws IOException, DocumentException
    {
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();

    	LabPDFCreator labPDFCreator=new LabPDFCreator(baos, segmentId, providerNo);
    	labPDFCreator.printPdf();

    	return(baos.toByteArray());
    }

    /** Creates a new instance of LabPDFCreator */
    public OLISLabPDFCreator(HttpServletRequest request, OutputStream os) {
		this(os, request, request.getParameter("segmentID")!=null?request.getParameter("segmentID"):(String)request.getAttribute("segmentID"));
    }

    public OLISLabPDFCreator(OutputStream os, HttpServletRequest request, String segmentId) {
        this.os = os;
        this.id = segmentId;

        // determine lab version
		String multiLabId = Hl7textResultsData.getMatchingLabs(id);
		this.multiID = multiLabId.split(",");

		int i=0;
		while (!multiID[i].equals(id)){
			i++;
		}
		this.versionNum = i+1;

        if(!segmentId.equals("0")){ // OLIS lab that is stored in chart has a segmentID that is not 0
			//Need date lab was received by OSCAR
			Hl7TextMessageDao hl7TxtMsgDao = (Hl7TextMessageDao)SpringUtils.getBean("hl7TextMessageDao");
			Hl7TextMessage hl7TextMessage = hl7TxtMsgDao.find(Integer.parseInt(segmentId));
			java.util.Date date = hl7TextMessage.getCreated();
			String stringFormat = "yyyy-MM-dd HH:mm";
		//	dateLabReceived = UtilDateUtilities.DateToString(date, stringFormat);

			// create handler
			this.handler = (OLISHL7Handler) Factory.getHandler(id);
		}
		else{ // OLIS lab not saved to chart has a segmentId of 0
			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

			String uuidToAdd = request.getParameter("uuid");
			String fileName = System.getProperty("java.io.tmpdir") + "/olis_" + uuidToAdd + ".response";
			String hl7Parsed = "";
			try {
				if (Files.exists(Paths.get(fileName))) {
					ArrayList<String> hl7Body = Utilities.separateMessages(fileName);
					for (String hl7Text : hl7Body){
						hl7Parsed += hl7Text.replace("\\E\\", "\\SLASHHACK\\").replace("µ", "\\MUHACK\\").replace("\\H\\", "\\.H\\").replace("\\N\\", "\\.N\\");
					}
					// set
					java.util.Date date = new java.util.Date();
					String stringFormat = "yyyy-MM-dd HH:mm";
					//dateLabReceived = UtilDateUtilities.DateToString(date, stringFormat);
					//create handler
					this.handler = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", hl7Parsed);
				}
			} catch (IOException ioe) {
				//Reading file failed
				MiscUtils.getLogger().error("Couldn't print requested OLIS lab.", ioe);
				request.setAttribute("result", "Error");
			}
			catch (Exception e){
				// separating message failed
				MiscUtils.getLogger().error("Couldn't print requested OLIS lab.", e);
				request.setAttribute("result", "Error");
			}
		}
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
        PdfWriter writer = PdfWriter.getInstance(document, os);

        //Set page event, function onEndPage will execute each time a page is finished being created
        writer.setPageEvent(this);

        document.setPageSize(PageSize.LETTER);
        document.addTitle("Title of the Document");
        document.addCreator("OSCAR");
        document.open();

        //Create the fonts that we are going to use
        bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, 9, Font.NORMAL);
        boldFont = new Font(bf, 9, Font.BOLD);
        redFont = new Font(bf, 9, Font.NORMAL, Color.RED);
        blueFont = new Font(bf, 9, Font.NORMAL, Color.BLUE);
        categoryHeadFont = new Font(bf, 12, Font.BOLD);
        commentFont = new Font(cf, 9, Font.NORMAL);
        subscriptFont = new Font(bf, 6, Font.NORMAL);
        

        // add the header table containing the patient and lab info to the document
        createInfoTable();

        // add the tests and test info for each header
        ArrayList<String> headers = handler.getHeaders();
        int obr;
        int lineNum = 0;
        for (int i=0; i < headers.size(); i++){
        	//Gets the mapped OBR for the current index
        	obr = handler.getMappedOBR(i);
        	lineNum = obr + 1;
        	//If the current lineNum is not a childOBR
        	if (!handler.isChildOBR(lineNum)){
        		//Calls on the addOLISLabCategory function passing the header at the current obr, and the obr itself
        		addOLISLabCategory(headers.get(obr), obr);
        	}
        }

        /*// add end of report table
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPhrase(new Phrase("  "));
        table.addCell(cell);
        cell.setBorder(15);
        cell.setBackgroundColor(new Color(210, 212, 255));
        cell.setPhrase(new Phrase("END OF REPORT", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        document.add(table);*/

        document.close();

        os.flush();
    }
    
    /*
	 * Given the name of a lab category this method will add the category
	 * header, the test result headers and the test results for that category.
	 */
	private void addOLISLabCategory(String header, Integer obr) throws DocumentException {	
		Color categoryBackground = new Color(255, 204, 0);
		Color separatorColour = new Color(0, 51, 153);
		
		//Creates a separator cell for separation between results
		PdfPCell separator = new PdfPCell();
		separator.setColspan(2);
		separator.setBorder(0);
		separator.setBackgroundColor(separatorColour);
		separator.setFixedHeight(1f);
		
		
		//Category Table Variables
		float[] categoryTableWidths;
		categoryTableWidths = new float[] {2f, 3f};
		PdfPTable categoryTable = new PdfPTable(categoryTableWidths);
		categoryTable.setWidthPercentage(100);
		categoryTable.setKeepTogether(true);
		
		//Main Table Variables
		float[] mainTableWidths;
		//Unused column is 3f
		mainTableWidths = new float[] {8f, 3f, 1f, 3f, 2f, 2f };
		PdfPTable table = new PdfPTable(mainTableWidths);
		table.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		//Sets the current category as a newCategory
		newCategory = handler.getOBRCategory(obr);
		
		//If it is a different category, then add a new category header to the category table
		if (!category.equals(newCategory)){
			categoryTable.addCell(separator);
			//Adds the Category name to the table
			cell = new PdfPCell();
			cell.setColspan(2);
			cell.setBorder(0);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(categoryBackground);
			cell.setPhrase(new Phrase(newCategory, categoryHeadFont));
			categoryTable.addCell(cell);
			//The new category becomes the current category
			category = newCategory;
		}
		
		//Adds a separator
		categoryTable.addCell(separator);
		
		//Renews the cell so that it is clean
		cell = new PdfPCell();
		cell.setBorder(0);
		Phrase categoryPhrase = new Phrase();
		categoryPhrase.setFont(boldFont);
		//Replaces 
		categoryPhrase.add(header.replaceAll("<br\\s*/*>", "\n"));
		//Gets the point of care and outputs message if it exists
		String poc = handler.getPointOfCare(obr);
		if (!stringIsNullOrEmpty(poc)){
			categoryPhrase.setFont(subscriptFont);
			categoryPhrase.add("\n\nTest perofrmed at patient location");
		}
		
		//Checks if the OBR is blocked
		Boolean blocked = handler.isOBRBlocked(obr);
		if (blocked){
			categoryPhrase.setFont(new Font(bf, 7, Font.NORMAL, Color.RED));
			categoryPhrase.add("\n\n(Do Not Disclose Without Explicit Patient Consent");
		}
		
		cell.setPhrase(categoryPhrase);
		categoryTable.addCell(cell);
		cell.setBorder(0);
		//Sets the specimen source and request status
		float[] specimenTableWidths = {1f, 4f}; 
		PdfPTable specimenTable = new PdfPTable(specimenTableWidths);
		//If there is a specimen source
		if (!stringIsNullOrEmpty(handler.getObrSpecimenSource(obr))){
			cell.setPhrase(new Phrase("Specimen Source: ", boldFont));
			specimenTable.addCell(cell);
			cell.setPhrase(new Phrase(handler.getObrSpecimenSource(obr), font));
			specimenTable.addCell(cell);
			cell.setBorder(0);
		}
		
		cell.setBorder(0);
		//Outputs the request status
		cell.setPhrase(new Phrase("Request Status: ", boldFont));
		specimenTable.addCell(cell);
		cell.setPhrase(new Phrase(handler.getObrStatus(obr), font));
		specimenTable.addCell(cell);
		
		//Adds the specimen table to the category table
		cell = new PdfPCell(specimenTable);
		cell.setBorder(0);
		categoryTable.addCell(cell);
		
		//Adds a small separator between the top row and the collection table
		cell = new PdfPCell();
		cell.setColspan(2);
		cell.setBorder(0);
		cell.setFixedHeight(1f);
		categoryTable.addCell(cell);
		
		//Creates the collection table and adds it to the category table
		PdfPTable collectionTable = createCollectionTable(obr);
		cell = new PdfPCell(collectionTable);
		cell.setBorder(0);
		cell.setColspan(2);
		categoryTable.addCell(cell);
		
		cell = new PdfPCell();
		cell.setBorder(0);
		String primaryFacility = handler.getPerformingFacilityName();
		String performingFacility = handler.getOBRPerformingFacilityName(obr);
		if (!primaryFacility.equals(performingFacility) && !stringIsNullOrEmpty(performingFacility)){
			cell.setPhrase(new Phrase("Performing Facility: ", boldFont));
			categoryTable.addCell(cell);
			cell.setPhrase(new Phrase(performingFacility, font));
			categoryTable.addCell(cell);
			cell.setPhrase(new Phrase("Address: ", boldFont));
			categoryTable.addCell(cell);
			cell.setPhrase(new Phrase(getFullAddress(handler.getPerformingFacilityAddress(obr)), font));
			categoryTable.addCell(cell);
		}
		String diagnosis = handler.getDiagnosis(obr);
		if (!stringIsNullOrEmpty(diagnosis)){
			cell.setPhrase(new Phrase("Diagnosis: ", font));
			categoryTable.addCell(cell);
			cell.setPhrase(new Phrase(diagnosis, font));
			categoryTable.addCell(cell);
		}
		
		cell = new PdfPCell();
		//Column Headers
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
		cell.setPhrase(new Phrase("Status", boldFont));
		table.addCell(cell);

		cell.setBorder(12);
		cell.setBorderColor(Color.BLACK); // cell.setBorderColor(Color.WHITE);
		cell.setBackgroundColor(new Color(255, 255, 255));
		
		boolean obrFlag = false;
		int obxCount = handler.getOBXCount(obr);
		String collectorsComment = handler.getCollectorsComment(obr);
		int obx = 0;
		
		if (!stringIsNullOrEmpty(collectorsComment)){
			cell.setColspan(7);
			Phrase collectorsCommentPhrase = new Phrase();
			collectorsCommentPhrase.setFont(font);
			collectorsCommentPhrase.add("Comments: " + handler.formatString(collectorsComment));
			
			collectorsCommentPhrase.setFont(subscriptFont);
			collectorsCommentPhrase.add("\t\t" + handler.getCollectorsCommentSourceOrganization(obr));
			cell.setPhrase(collectorsCommentPhrase);
			table.addCell(cell);
			
			cell.setColspan(1);
		}
		
		
		if (handler.getObservationHeader(obr, 0).equals(header)){
			int commentCount = handler.getOBRCommentCount(obr);
			for (int comment = 0; comment < commentCount; comment++){
				String obxNN = handler.getOBXName(obr, 0);
				if (!obrFlag && obxNN.equals("")){
					cell.setPhrase(new Phrase(handler.getOBRName(comment), font));
					table.addCell(cell);
					cell.setPhrase(new Phrase(handler.getObrSpecimenSource(comment), font));
					table.addCell(cell);
					cell.setColspan(5);
					table.addCell(cell);
					obrFlag = true;
				}
				
				String obrComment = handler.formatString(handler.getOBRComment(obr, comment)).replaceAll("<br\\s*/*>", "\n");
				String sourceOrg = handler.getOBRSourceOrganization(obr, comment);
				
				cell.setColspan(6);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				Phrase obrCommentPhrase = new Phrase();
				obrCommentPhrase.setFont(font);
				obrCommentPhrase.add(obrComment);
				obrCommentPhrase.setFont(subscriptFont);
				obrCommentPhrase.add("\t\t" + sourceOrg);
				cell.setPhrase(obrCommentPhrase);
				table.addCell(cell);
			}
		}
		
		for (int count = 0; count < obxCount; count++){
			obx = handler.getMappedOBX(obr, count);
			String obxName = handler.getOBXName(obr, obx);
			boolean b1 = false;
			boolean b2 = false;
			boolean b3 = false;
			
			boolean fail = true;
			
			try{
				b1 = !handler.getOBXResultStatus(obr, obx).equals("DNS");
				b2 = !stringIsNullOrEmpty(obxName);
				String obsHeader = handler.getObservationHeader(obr, obx);
				b3 = obsHeader.equals(header);
				fail = false;
				
			}catch(Exception e){
				logger.info("ERROR: " + e);
			}
			
			if (!fail && b1 && b2 && b3){
				String obrName = handler.getOBRName(obr);
				b1 = !obrFlag && !stringIsNullOrEmpty(obrName);
				b2 = !(obxName.contains(obrName));
				b3 = obxCount < 2;
				
				if (b1 && b2 && b3){
					obrFlag = true;
				}
				
				String status = handler.getOBXResultStatus(obr, obx).trim();
				String statusMsg = "";
				try{
					statusMsg = OLISHL7Handler.getTestResultStatusMessage(handler.getOBXResultStatus(obr, obx).charAt(0));
				}
				catch(Exception e){
					statusMsg = "";
				}

				//Creates a new font used on the line
				Font lineFont = new Font(font);
				String abnormal = handler.getOBXAbnormalFlag(obr, obx);
				
				//If the abnormal status starts with L then the font color is blue
				if (abnormal!= null && abnormal.startsWith("L")){
					lineFont = blueFont;
				}
				//If the abnormal status starts with an A, H, or isOBXAbnormal returns true then the font color is blue 
				else if (abnormal != null && (abnormal.equals("A") || abnormal.startsWith("H") || handler.isOBXAbnormal(obr, obx))){
					lineFont = redFont;
				}
				
				Font statusMsgFont = new Font(lineFont);
				
				//Gets the font style to be used in the table according to the status
				if (status != null && status.startsWith("W")){
					lineFont.setStyle(Font.STRIKETHRU);
				}
				//Creates a new phrase to hold the display name
				Phrase obxDisplayName = new Phrase();
				//Sets the font and replaces all breaks with a new line
				obxDisplayName.setFont(lineFont);
				obxDisplayName.add(obxName.replaceAll("<br\\s*/*>", "\n"));
				
				//Checks the abnormal nature of the test and adds the necessary portion to the displayName
				String abnormalNature = handler.getNatureOfAbnormalTest(obr, obx);
				if (!stringIsNullOrEmpty(abnormalNature)){
					obxDisplayName.setFont(subscriptFont);
					obxDisplayName.add("\t\t" + abnormalNature);
				}
				
				
				String obxValueType = handler.getOBXValueType(obr, obx).trim();
				if (obxValueType.equals("ST") && handler.renderAsFT(obr,obx)) {
					obxValueType = "FT";
				}
				else if (obxValueType.equals("TX") && handler.renderAsNM(obr,obx)) {
					obxValueType = "NM";
				}
				else if (obxValueType.equals("FT") && handler.renderAsNM(obr,obx)) {
					obxValueType = "NM";
				}
				
				//Sets the cell border to 15 so that the cells in the table are completely bordered instead of just left and right borders
				cell.setBorder(15);
				
				//Checks the obxValueType and populates the table row with the proper data
				if (obxValueType.equals("NM") || obxValueType.equals("ST") || obxValueType.equals("SN")){
					//Checks if it is Ancillary and obxValueType is not SN, adds Patient Observation row to table
					if (handler.isAncillary(obr, obx) && !obxValueType.equals("SN")){
						cell.setColspan(6);
						cell.setPhrase(new Phrase("Patient Observation", font));
						table.addCell(cell);
					}
					
					cell.setColspan(1);
					//Adds the columns for the current Value Type
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(obxDisplayName);
					table.addCell(cell);
					
					 
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

					//If the type does not equal SN, then outputs normal OBX result, if it is SN then outputs SNResult
					if (!obxValueType.equals("SN"))
						cell.setPhrase(new Phrase(handler.formatString(handler.getOBXResult(obr, obx)).replaceAll("<br\\s*/*>", "\n"), lineFont));
					else
						cell.setPhrase(new Phrase(handler.formatString(handler.getOBXSNResult(obr, obx)).replaceAll("<br\\s*/*>", "\n"), lineFont));

					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(handler.getOBXAbnormalFlag(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(new Phrase(handler.getOBXReferenceRange(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(new Phrase(handler.formatString(handler.getOBXUnits(obr, obx)), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(statusMsg, statusMsgFont));
					table.addCell(cell);
				}
				else if (obxValueType.equals("TX") || obxValueType.equals("FT")){
					//Adds the columns for the current Value Type
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(6);
					cell.setPhrase(obxDisplayName);
					table.addCell(cell);
					
					cell.setColspan(5);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPhrase(new Phrase(handler.formatString(handler.getOBXResult(obr, obx)).replaceAll("<br\\s*/*>", "\n"), lineFont));
					table.addCell(cell);
					
					cell.setColspan(1);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(statusMsg, statusMsgFont));
					table.addCell(cell);
				}
				//Combines the TM, DT, and TS displays into one to reduce redundant code since the only difference between them is the OBX Results that are retrieved
				else if(obxValueType.equals("TM") || obxValueType.equals("DT") || obxValueType.equals("TS")){
					cell.setColspan(1);
					//Adds the columns for the current Value Type
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(obxDisplayName);
					table.addCell(cell);
					
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					
					//Gets the OBX result based on the value type
					if(obxValueType.equals("TM")){
						cell.setPhrase(new Phrase(handler.formatString(handler.getOBXTMResult(obr, obx)).replaceAll("<br\\s*/*>", "\n"), lineFont));
					}
					else if(obxValueType.equals("DT")){
						cell.setPhrase(new Phrase(handler.formatString(handler.getOBXDTResult(obr, obx)).replaceAll("<br\\s*/*>", "\n"), lineFont));
					}
					else{
						cell.setPhrase(new Phrase(handler.formatString(handler.getOBXTSResult(obr, obx)).replaceAll("<br\\s*/*>", "\n"), lineFont));
					}
					
					table.addCell(cell);
					
					cell.setColspan(3);
					cell.setPhrase(new Phrase("", lineFont));
					table.addCell(cell);
					
					cell.setColspan(1);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(statusMsg, statusMsgFont));
					table.addCell(cell);
				}
				else if (obxValueType.equals("ED")){
					//Adds the columns for the current row
					cell.setColspan(1);
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(obxDisplayName);
					table.addCell(cell);
					
					cell.setColspan(3);
					cell.setPhrase(new Phrase("", lineFont));
					table.addCell(cell);
					
					cell.setColspan(1);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPhrase(new Phrase(handler.getOBXUnits(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(statusMsg, statusMsgFont));
					table.addCell(cell);
					
					cell.setColspan(6);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(new Phrase("Attachment omitted from printing", lineFont));
					table.addCell(cell);
					
				}
				else if(obxValueType.equals("CE")){
					//Adds the columns for the current Value Type
					cell.setColspan(6);
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(obxDisplayName);
					table.addCell(cell);
					
					cell.setColspan(5);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPhrase(new Phrase(handler.getOBXCEName(obr, obx), font));
					table.addCell(cell);
					
					cell.setColspan(1);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(statusMsg, statusMsgFont));
					table.addCell(cell);
					
					//If the status is final
					if(handler.isStatusFinal(handler.getOBXResultStatus(obr, obx).charAt(0))){
						String parentId = handler.getOBXCEParentId(obr, obx);
						//If there is a parent ID then outputs a table for Agent and Sensitivity
						if (!stringIsNullOrEmpty(parentId)){
							float[] ceTableWidths = {2f, 3f};
							PdfPTable ceTable = new PdfPTable(ceTableWidths);
							ceTable.setWidthPercentage(10f);
							
							
							//Column Headers
							cell.setColspan(1);
							//Enables the borders with the bitwise combination of 7 (1 top, 2 bottom, 4 left)
							cell.setBorder(7);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setPhrase(new Phrase("Agent", boldFont));
							ceTable.addCell(cell);
							//Enables the borders with the bitwise combination of 11 (1 top, 2 bottom, 8 right)
							cell.setBorder(11);
							cell.setPhrase(new Phrase("Sensitivity", boldFont));
							ceTable.addCell(cell);
							cell.setBorder(12);
							
							cell.setColspan(1);
							int childOBR = handler.getChildOBR(parentId) - 1;
							//If the childOBR does not equal -1
							if (childOBR != -1){
								//Gets the Gets the childOBR length
								int childLength = handler.getOBXCount(childOBR);
								//For each child obr, outputs it
								for (int ceIndex = 0; ceIndex < childLength; ceIndex++){
									Font strikeoutFont = new Font(bf, 9, Font.STRIKETHRU);
									String ceStatus = handler.getOBXResultStatus(childOBR, ceIndex).trim();
                                    boolean ceStrikeout = ceStatus != null && ceStatus.startsWith("W");
                                    Phrase ceName = new Phrase();
                                    Phrase ceSense = new Phrase();
                                    //If the font should be strikethrough
                                    if (ceStrikeout){
                                    	ceName.setFont(strikeoutFont);
                                    	ceSense.setFont(strikeoutFont);
                                    }
                                    ceName.add(handler.getOBXCESensitivity(childOBR,ceIndex));
                                    ceSense.add(handler.getOBXName(childOBR,ceIndex));
                                    cell.setPhrase(ceName);
                                    ceTable.addCell(cell);
                                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    cell.setPhrase(ceSense);
                                    ceTable.addCell(cell);
                                    
                                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								}
							}
							
							//Adds the ceTable to the main table
							cell = new PdfPCell(ceTable);
							cell.setBorder(12);
					        cell.setColspan(6);
					        //For the table, sets the padding to 
					        cell.setPaddingLeft(220);
					        cell.setPaddingRight(220);
					        table.addCell(cell);
					        //Sets the padding back to 0
					        cell.setPaddingLeft(0);
					        cell.setPaddingRight(0);
					        
					        if (category.toUpperCase().trim().equals("MICROBIOLOGY")){
					        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					        	cell.setPhrase(new Phrase("S=Sensitive R=Resistant I=Intermediate MS=Moderately Sensitive VS=Very Sensitive", font));
					        	table.addCell(cell);
					        }
					        cell.setColspan(1);
						}
					}
				}
				else{
					//Adds the columns for the current Value Type
					cell.setColspan(1);
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(obxDisplayName);
					table.addCell(cell);
					
					 
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPhrase(new Phrase(handler.getOBXResult(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(handler.getOBXAbnormalFlag(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(new Phrase(handler.getOBXReferenceRange(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setPhrase(new Phrase(handler.getOBXUnits(obr, obx), lineFont));
					table.addCell(cell);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPhrase(new Phrase(statusMsg, statusMsgFont));
					table.addCell(cell);
				}
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				//If there is an obs method, outputs it
				String obsMethod = handler.getOBXObservationMethod(obr, obx);
				if (!stringIsNullOrEmpty(obsMethod)){
					cell.setColspan(6);
					cell.setPhrase(new Phrase("Observation Method: " + obsMethod, font));
					table.addCell(cell);
					cell.setColspan(0);
				}
				//If there is an obsDate, outputs it
				String obsDate = handler.getOBXObservationDate(obr, obx);
				if (!stringIsNullOrEmpty(obsDate)){
					cell.setColspan(6);
					cell.setPhrase(new Phrase("Observation Date: " + obsDate, font));
					table.addCell(cell);
					cell.setColspan(0);
				}
				
				cell.setColspan(6);
				cell.setBorder(12);
				//For each comment, outputs it
				for(int commentCount = 0; commentCount < handler.getOBXCommentCount(obr, obx); commentCount++){
					Phrase comment = new Phrase();
					comment.setFont(commentFont);
					comment.add(handler.getOBXComment(obr, obx, commentCount).replaceAll("<br\\s*/*>", "\n").replaceAll("&nbsp;", "\u00A0"));
					comment.setFont(subscriptFont);
					comment.add("\t\t" + handler.getOBXSourceOrganization(obr, obx, commentCount));
					cell.setPhrase(comment);
					table.addCell(cell);
				}
			}
		}
		
		PdfPTable borderedCategoryTable = new PdfPTable(1);
		borderedCategoryTable.setWidthPercentage(100);
		cell = new PdfPCell(categoryTable);
		cell.setBorder(15);
		borderedCategoryTable.addCell(cell);
		
		document.add(borderedCategoryTable);
		document.add(table);

	}

    
    /*
     *  createInfoTable creates and adds the table at the top of the document
     *  which contains the patient and lab information
     */
    private void createInfoTable() throws DocumentException{
    	
    	String fullAddress = "";
    	
        //Create patient info table
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        float[] pInfoWidths = {2f, 3f};
        PdfPTable pInfoTable = new PdfPTable(pInfoWidths);
        cell.setPhrase(new Phrase("Health #: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getHealthNum(), font));
        pInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Patient Name: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getPatientName(), font));
        pInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Date of Birth: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getDOB(), font));
        pInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Age: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getAge(), font));
        pInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Sex: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getSex(), font));
        pInfoTable.addCell(cell);
        
        //Patient Address
    	for (HashMap<String, String> address : handler.getPatientAddresses()){
    		//Adds the address type to the table
    		cell.setPhrase(new Phrase(address.get("Address Type") + ": ", boldFont));
            pInfoTable.addCell(cell);
            //Gets the full address
            fullAddress = getFullAddress(address);
            //Sets the cell's phrase and adds the cell to the table
            cell.setPhrase(new Phrase(fullAddress, font));
            pInfoTable.addCell(cell);
    	}
    	//Patient Home Phone
        ArrayList<HashMap<String,String>> homePhones = handler.getPatientHomeTelecom();
    	for(HashMap<String, String> homePhone : homePhones){
        	Phrase phonePhrase = new Phrase();
        	//Adds the phone's use
        	cell.setPhrase(new Phrase("Home: ", boldFont));
        	pInfoTable.addCell(cell);
        	
        	//Adds the phone number and useCode to the phrase
        	phonePhrase.setFont(font);
        	phonePhrase.add(getPhone(homePhone));
        	phonePhrase.setFont(subscriptFont);
        	phonePhrase.add(homePhone.get("useCode"));
        	//Adds the phrase to the table
        	cell.setPhrase(phonePhrase);
        	pInfoTable.addCell(cell);
        }
    	//Patient Work Telephone
        ArrayList<HashMap<String, String>> workPhones = handler.getPatientWorkTelecom();
        for (HashMap<String, String> workPhone : workPhones){
        	Phrase phonePhrase = new Phrase();
        	//Adds the phone's use
        	cell.setPhrase(new Phrase("Work: ", boldFont));
        	pInfoTable.addCell(cell);
        	//Adds the phone number and useCode
        	phonePhrase.setFont(font);
        	phonePhrase.add(getPhone(workPhone));
        	phonePhrase.setFont(subscriptFont);
        	phonePhrase.add(workPhone.get("useCode"));
        	//Adds the phrase to the table
        	cell.setPhrase(phonePhrase);
        	pInfoTable.addCell(cell);
        }
        
        
        //Create results info table
        PdfPTable rInfoTable = new PdfPTable(2);
        cell.setPhrase(new Phrase("Report Status: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getOrderStatus() == FINAL_CODE ? REPORT_FINAL : REPORT_PARTIAL, font));
        rInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Order Id: ", boldFont));
        rInfoTable.addCell(cell);
        Phrase orderIdPhrase = new Phrase();
        orderIdPhrase.setFont(font);
        orderIdPhrase.add(handler.getAccessionNum());
        orderIdPhrase.setFont(subscriptFont);
        orderIdPhrase.add("\t\t" + handler.getAccessionNumSourceOrganization());
        
        cell.setPhrase(orderIdPhrase);
        rInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Order Date: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getOrderDate(), font));
        rInfoTable.addCell(cell);
        
        if(!stringIsNullOrEmpty(handler.getLastUpdateInOLISUnformated())){
	        cell.setPhrase(new Phrase("Last Updated In OLIS: ", boldFont));
	        rInfoTable.addCell(cell);
	        cell.setPhrase(new Phrase(handler.getLastUpdateInOLIS(), font));
	        rInfoTable.addCell(cell);
        }
        
        if(!stringIsNullOrEmpty(handler.getSpecimenReceivedDateTime())){
	        cell.setPhrase(new Phrase("Specimen Received: ", boldFont));
	        rInfoTable.addCell(cell);
	        cell.setPhrase(new Phrase(handler.getSpecimenReceivedDateTime(), font));
	        rInfoTable.addCell(cell);
        }
        
        HashMap<String,String> address;
        address = handler.getOrderingFacilityAddress();
        if (!stringIsNullOrEmpty(handler.getOrderingFacilityName())){
	        cell.setPhrase(new Phrase("Ordering Facility: ", boldFont));
	        rInfoTable.addCell(cell);
	        cell.setPhrase(new Phrase(handler.getOrderingFacilityName(), font));
	        rInfoTable.addCell(cell);
	        
	        if (address != null && address.size() > 0){
		        cell.setPhrase(new Phrase("Address: ", boldFont));
		        rInfoTable.addCell(cell);
		        cell.setPhrase(new Phrase(getFullAddress(handler.getOrderingFacilityAddress()), font));
		        rInfoTable.addCell(cell);
	        }
        }
        
        cell.setPhrase(new Phrase("Ordering Provider: ", boldFont));
        rInfoTable.addCell(cell);   
        cell.setPhrase(getDoctorNamePhrase(handler.getDocName()));
        rInfoTable.addCell(cell);
        
        address = handler.getOrderingProviderAddress(); 
        if (address != null && address.size() > 0){
	        cell.setPhrase(new Phrase("Address: ", boldFont));
	    	rInfoTable.addCell(cell);
	        fullAddress = getFullAddress(handler.getOrderingProviderAddress());
	        cell.setPhrase(new Phrase(fullAddress, font));
	        rInfoTable.addCell(cell);
        }
        
        for(HashMap<String, String> phone : handler.getOrderingProviderPhones()){
        	String phoneNumber = "";
        	//Adds the phone's use
        	cell.setPhrase(new Phrase(phone.get("useCode") + ": ", boldFont));
        	rInfoTable.addCell(cell);
        	//Adds the phone number
        	phoneNumber = getPhone(phone);
        	cell.setPhrase(new Phrase(phoneNumber, font));
        	rInfoTable.addCell(cell);
        }
        
        if (!stringIsNullOrEmpty(handler.getAttendingProviderName())){
        	cell.setPhrase(new Phrase("Attending Provider: ", boldFont));
        	rInfoTable.addCell(cell);
            cell.setPhrase(getDoctorNamePhrase(handler.getAttendingProviderName()));
            rInfoTable.addCell(cell);
        }
        
        if (!stringIsNullOrEmpty(handler.getAdmittingProviderName())){
        	cell.setPhrase(new Phrase("Admitting Provider: ", boldFont));
        	rInfoTable.addCell(cell);
            cell.setPhrase(getDoctorNamePhrase(handler.getAdmittingProviderName()));
            rInfoTable.addCell(cell);
        }
    
        String primaryFacility = handler.getPerformingFacilityName();
        String reportingFacility = handler.getReportingFacilityName();
        
        if (!stringIsNullOrEmpty(primaryFacility)){
        	//Determines if the performing facility is also the reporting facility and adds it and the name
        	String facilityRole = "Performing " + (primaryFacility.equals(reportingFacility) ? "and Reporting " : "") + "Facility: ";
        	cell.setPhrase(new Phrase(facilityRole, boldFont));
        	rInfoTable.addCell(cell);
        	cell.setPhrase(new Phrase(primaryFacility, font));
        	rInfoTable.addCell(cell);
        	//Creates the format for the address and adds it
        	address = handler.getPerformingFacilityAddress();
        	if (address != null && address.size() > 0){
        		cell.setPhrase(new Phrase("Address: ", boldFont));
            	rInfoTable.addCell(cell);
        		fullAddress = getFullAddress(address);
        		cell.setPhrase(new Phrase(fullAddress, font));
        		rInfoTable.addCell(cell);
        	}
        }
        
        if (!stringIsNullOrEmpty(reportingFacility) && !reportingFacility.equals(primaryFacility)){
        	//Adds reporting facility name
        	cell.setPhrase(new Phrase("Reporting Facility: ", boldFont));
        	rInfoTable.addCell(cell);
        	cell.setPhrase(new Phrase(reportingFacility, font));
        	rInfoTable.addCell(cell);
        	
        	
        	//Creates the format for the address and adds it
        	address = handler.getReportingFacilityAddress();
        	if (address != null && address.size() > 0){
        		cell.setPhrase(new Phrase("Address: ", boldFont));
            	rInfoTable.addCell(cell);
            	
        		fullAddress = getFullAddress(address);
        		cell.setPhrase(new Phrase(fullAddress, font));
        		rInfoTable.addCell(cell);;
        	}
        }
        
        
        //Create client table
        PdfPTable clientTable = new PdfPTable(1);

        cell.setPhrase(getCCDocNamesPhrase(handler.getCCDocs()));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        clientTable.addCell(cell);
        
        //Create comment table
        Phrase commentPhrase = new Phrase();
        PdfPTable commentTable = new PdfPTable(1);
        commentTable.setWidthPercentage(100);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(1);
        cell.setPhrase(new Phrase("Report Comments: ", boldFont));
        commentTable.addCell(cell);
        for (int comment = 0; comment < handler.getReportCommentCount(); comment++){
        	commentPhrase.clear();
        	
        	cell.setPaddingLeft(10);
        	commentPhrase.setFont(font);
        	commentPhrase.add(handler.formatString(handler.getReportComment(comment)).replaceAll("<br\\s*/*>", "\n"));
        	commentPhrase.setFont(subscriptFont);
        	commentPhrase.add("\t\t" + handler.getReportSourceOrganization(comment));
        	cell.setPhrase(commentPhrase);
        	commentTable.addCell(cell);
        }
        

        
        //Create header info table
        float[] tableWidths = {2f, 3f};
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
        cell.setPadding(5);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Results Info", boldFont));
        table.addCell(cell);

        // add the created tables to the document
        table = addTableToTable(table, pInfoTable, 1);
        table = addTableToTable(table, rInfoTable, 1);
        table = addTableToTable(table, clientTable, 2);
        table = addTableToTable(table, commentTable, 2);

        table.setWidthPercentage(100);

        document.add(table);
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

            //add footer for every page
            cb.beginText();
            cb.setFontAndSize(bf, 8);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "-"+pageNum+"-", width/2, 30, 0);
            cb.endText();


            // add promotext as footer if it is enabled
            if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null){
                cb.beginText();
                cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252,BaseFont.NOT_EMBEDDED), 6);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"), width/2, 19, 0);
                cb.endText();
            }

        // throw any exceptions
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    public String getAddressFieldIfNotNullOrEmpty(HashMap<String,String> address, String key) {
    	return getAddressFieldIfNotNullOrEmpty(address, key, true);
    }
    
    public String getAddressFieldIfNotNullOrEmpty(HashMap<String,String> address, String key, boolean newLine) {
    	String value = address.get(key);
    	if (stringIsNullOrEmpty(value)) { return ""; }
    	String result = value + (newLine ? "\n" : "");
    	return result;
    }
    
    public boolean stringIsNullOrEmpty(String s) {
    	return s == null || s.trim().length() == 0;
    }
    
    public String getFullAddress(HashMap<String, String> address){
    	
    	String city = getAddressFieldIfNotNullOrEmpty(address, "City", false);
    	String province = getAddressFieldIfNotNullOrEmpty(address, "Province", false);
    	
    	String fullAddress = "";
    	fullAddress += getAddressFieldIfNotNullOrEmpty(address, "Street Address");
    	fullAddress += getAddressFieldIfNotNullOrEmpty(address, "Other Designation");
    	fullAddress += getAddressFieldIfNotNullOrEmpty(address, "Postal Code");
    	fullAddress += city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "\n");
    	fullAddress += getAddressFieldIfNotNullOrEmpty(address, "Country", false);
    	
    	return fullAddress;
    }
    
    public String getPhone(HashMap<String, String> phone){
    	
    	String phoneNumber = "";
    	if (phone.get("email") != null){
    		phoneNumber = phone.get("email");
    	}
    	else{
    		String countryCode = phone.get("countryCode");
   			if (stringIsNullOrEmpty(countryCode)) {
   				countryCode = "";
   			}

   			String localNumber = phone.get("localNumber");
   			if (!stringIsNullOrEmpty(localNumber) && localNumber.length() > 4) {
   				localNumber = localNumber.substring(0,3) + "-" + localNumber.substring(3);
   			}
   			else { localNumber = ""; }
   			
   			String areaCode = phone.get("areaCode");
   			if (!stringIsNullOrEmpty(areaCode)) {
   				areaCode = " ("+areaCode+") ";
   			}
   			else { areaCode = ""; }
   			
   			String extension = phone.get("extension");
   			if (!stringIsNullOrEmpty(extension)) {
   				extension = " x" + extension;
   			}
   			else { extension = ""; }
   			
   			phoneNumber = countryCode + areaCode + localNumber + extension;
    	}
    	
    	return phoneNumber;
    }
    
	public PdfPTable createCollectionTable(Integer obr){
    	PdfPTable collectionTable = new PdfPTable(2);
    	//Sets the default cell's border to 0 in case completeRow() needs to add in a cell
    	collectionTable.getDefaultCell().setBorder(0);
    	//Declares innerTable to keep nice spacing in the cells 
    	PdfPTable innerTable = new PdfPTable(1);
    	//Declares a collectionCell to be used only to add the innerTable to the collection table
    	PdfPCell collectionCell;
    	//Normal cell to be used for addition to the innerTable
    	PdfPCell cell = new PdfPCell();
    	cell.setBorder(0);
    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	
    	//Gets the data from the handler
    	String collectionDateTime = handler.getCollectionDateTime(obr);
        String specimenCollectedBy = handler.getSpecimenCollectedBy(obr);
        String collectionVolume = handler.getCollectionVolume(obr);
        String noOfSampleContainers = handler.getNoOfSampleContainers(obr);
        
        //Checks if the collectionDateTime string is not null
        if (!stringIsNullOrEmpty(collectionDateTime)) {
        	//Adds the header and the value of the collection date time
        	cell.setPhrase(new Phrase("Collection Date/Time", boldFont));
        	innerTable.addCell(cell);
        	cell.setPhrase(new Phrase(collectionDateTime, font));
        	innerTable.addCell(cell);
        	//Adds the inner table to the collectionCell
        	collectionCell = new PdfPCell(innerTable);
        	collectionCell.setBorder(0);
        	//Adds the collectionCell to the collectionTable
        	collectionTable.addCell(collectionCell);
        }
        //Checks if the specimen collected by string is not null
        if (!stringIsNullOrEmpty(specimenCollectedBy)) {
        	//Resets the inner table
        	innerTable = new PdfPTable(1);
        	//Adds the header and the value of the specimen collected by
        	cell.setPhrase(new Phrase("Specimen Collected By", boldFont));
        	innerTable.addCell(cell);
        	cell.setPhrase(new Phrase(specimenCollectedBy, font));
        	innerTable.addCell(cell);
        	//Adds the inner table to the collectionCell
        	collectionCell = new PdfPCell(innerTable);
        	collectionCell.setBorder(0);
        	//Adds the collectionCell to the collectionTable
        	collectionTable.addCell(collectionCell);
        }
        //Checks if the collection volume string is not null
        if (!stringIsNullOrEmpty(collectionVolume)) {
        	//Resets the inner table
        	innerTable = new PdfPTable(1);
        	//Adds the header and the value of the collection volume
        	cell.setPhrase(new Phrase("Collection Volume", boldFont));
        	innerTable.addCell(cell);
        	cell.setPhrase(new Phrase(collectionVolume, font));
        	innerTable.addCell(cell);
        	//Adds the inner table to the collectionCell
        	collectionCell = new PdfPCell(innerTable);
        	collectionCell.setBorder(0);
        	//Adds the collectionCell to the collectionTable
        	collectionTable.addCell(collectionCell);
        }
        //Checks if the no. of sample containers string is not null
        if (!stringIsNullOrEmpty(noOfSampleContainers)) {
        	//Resets the inner table
        	innerTable = new PdfPTable(1);
        	//Adds the header and the value of the no. of sample containers
        	cell.setPhrase(new Phrase("No. of Sample Containers", boldFont));
        	innerTable.addCell(cell);
        	cell.setPhrase(new Phrase(noOfSampleContainers, font));
        	innerTable.addCell(cell);
        	//Adds the inner table to the collectionCell
        	collectionCell = new PdfPCell(innerTable);
        	collectionCell.setBorder(0);
        	//Adds the collectionCell to the collectionTable
        	collectionTable.addCell(collectionCell);
        }
        //Completes the current row in case there is only one cell in it
        collectionTable.completeRow();
        //Returns the collection table
        return collectionTable;
    }
    
    /**
     * Takes a string of docNames, specifically the one returned from handler.getCCDocNames()
     * Converts the string into a phrase containing all doc names
     * @param docNames
     * @return ccDocNames
     */
    private Phrase getCCDocNamesPhrase(String docNames){
    	Phrase ccDocNames = new Phrase();
    	String[] splitNames;
    	
    	ccDocNames.setFont(boldFont);
    	ccDocNames.add("cc: Client:  ");
    	ccDocNames.setFont(font);
    	
    	splitNames = docNames.split(", ");
    	
    	for(String docName : splitNames){
    		
    		for(Object chunk : getDoctorNamePhrase(docName).getChunks()){
    			ccDocNames.add(chunk);
    		}
    		ccDocNames.add(new Chunk(", ", font));
    	}
    	ccDocNames.remove(ccDocNames.size() - 1);
    	
    	return ccDocNames;
    }
    /**
     * Takes a doctor name string and turns it into a phrase that contains the doctor 
     * name in normal font, and then their MD number in the smaller font
     * @param doctorName
     * @return doctorPhrase
     */
    private Phrase getDoctorNamePhrase(String doctorName){
    	
    	Integer openSpanStart = doctorName.indexOf("<");
        String mdNumber = "";
        
        if (openSpanStart != -1){
        	Integer openSpanEnd = doctorName.indexOf(">");
        	Integer closeSpanStart = doctorName.indexOf("<", openSpanEnd);
        	Integer closeSpanEnd = doctorName.indexOf(">", closeSpanStart);

        	mdNumber = doctorName.substring(openSpanEnd + 1, closeSpanStart);
        	doctorName = doctorName.substring(0, openSpanStart);
        }
        
        Phrase doctorPhrase = new Phrase();
        //doctorPhrase.setFont(font);
        doctorPhrase.add(new Chunk(doctorName, font));
        //doctorPhrase.setFont(subscriptFont);
        doctorPhrase.add(new Chunk("\t" + mdNumber, subscriptFont));
        
        return doctorPhrase;
    }
}