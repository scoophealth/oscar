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
package org.oscarehr.hospitalReportManager;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentCommentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToProviderDao;
import org.oscarehr.hospitalReportManager.dao.HRMProviderConfidentialityStatementDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentComment;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Display Demographic Info (hrmReport.getLegalName, getHCN, getHCNVersion, getGender, getDateOfBirthAsString, getAddress1/2/City)
Display when it was received (hrmReportTime - attribute)
Display # of duplicate (hrmDuplicateNum)
Display similar reports (allDocumentsWithRelationship)

If Binary -> try to display add'l content (image gif/jpg/png, pdf, tiff, rtf,html)

Display Confidentiality statement
Display Report Date
Display Assigned providers
Display Report Class
Display accompanying subclass
Display Category
Display who signed off
Display description
Display comments

Display unique ID, sending facility, sending facility report no, date/time report,  result status

if duplicates - report history (id,report date, date received)


 * @author marc
 *
 */
public class HrmPDFCreator extends PdfPageEventHelper {

	private static Logger logger = MiscUtils.getLogger();
	private OutputStream os;

	private Document document;
	private PdfWriter writer;

	private BaseFont bf;
	private Font font;
	private Font boldFont;
	//private Font headerFont;
	//private Font infoFont;
	//private ResourceBundle oscarR;
	
	//private Integer id;
	private HRMDocument hrmDocument;
	private HRMReport report;
	
	
	private HRMDocumentDao hrmDocumentDao = SpringUtils.getBean(HRMDocumentDao.class);
	private HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
	private HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");
	//private HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
	private HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
	private HRMDocumentCommentDao hrmDocumentCommentDao = (HRMDocumentCommentDao) SpringUtils.getBean("HRMDocumentCommentDao");
	private HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public HrmPDFCreator(LoggedInInfo loggedInInfo, OutputStream os, HRMDocument hrmDocument, String providerNo) {
        this.os = os;
       // this.id = hrmDocumentId;

        //hrmDocument = hrmDocumentDao.find(hrmDocumentId);
        
        this.hrmDocument = hrmDocument;
        Integer hrmDocumentId = hrmDocument.getId();
        
        if (hrmDocument != null) {
            logger.debug("reading repotFile : "+hrmDocument.getReportFile());
            report = HRMReportParser.parseReport(loggedInInfo, hrmDocument.getReportFile());
            
            if (report != null) {
            	List<HRMDocumentToDemographic> demographicLinkList = hrmDocumentToDemographicDao.findByHrmDocumentId(hrmDocument.getId().toString());
                HRMDocumentToDemographic demographicLink = (demographicLinkList.size() > 0 ? demographicLinkList.get(0) : null);
               
                List<HRMDocumentToProvider> providerLinkList = hrmDocumentToProviderDao.findByHrmDocumentIdNoSystemUser(hrmDocument.getId().toString());
                
                List<HRMDocumentSubClass> subClassList = hrmDocumentSubClassDao.getSubClassesByDocumentId(hrmDocument.getId());
                
                HRMDocumentToProvider thisProviderLink = hrmDocumentToProviderDao.findByHrmDocumentIdAndProviderNo(hrmDocument.getId().toString(), loggedInInfo.getLoggedInProviderNo());
                
                HRMDocumentSubClass hrmDocumentSubClass=null;
                if (subClassList!= null)
                {
                	for (HRMDocumentSubClass temp : subClassList)
                	{
                		if (temp.isActive())
                		{
                			hrmDocumentSubClass=temp;
                			break;
                		}
                	}
                }
                
                HRMCategory category = null;
                if (hrmDocumentSubClass != null) {
                    category = hrmCategoryDao.findBySubClassNameMnemonic(hrmDocumentSubClass.getSubClass()+':'+hrmDocumentSubClass.getSubClassMnemonic());
                }
                else
                {
                	category=hrmCategoryDao.findBySubClassNameMnemonic("DEFAULT");
                }

                // Get all the other HRM documents that are either a child, sibling, or parent
                List<HRMDocument> allDocumentsWithRelationship = hrmDocumentDao.findAllDocumentsWithRelationship(hrmDocument.getId());                

                List<HRMDocumentComment> documentComments = hrmDocumentCommentDao.getCommentsForDocument(hrmDocumentId);
                
                String confidentialityStatement = hrmProviderConfidentialityStatementDao.getConfidentialityStatementForProvider(loggedInInfo.getLoggedInProviderNo());
             
                /*
                String duplicateLabIdsString=StringUtils.trimToNull(request.getParameter("duplicateLabIds"));
                Map<Integer,Date> dupReportDates = new HashMap<Integer,Date>();
                Map<Integer,Date> dupTimeReceived = new HashMap<Integer,Date>();
                
                if (duplicateLabIdsString!=null) {
                	String[] duplicateLabIdsStringSplit=duplicateLabIdsString.split(",");
                	for (String tempId : duplicateLabIdsStringSplit) {
                		HRMDocument doc = hrmDocumentDao.find(Integer.parseInt(tempId));
                		dupReportDates.put(Integer.parseInt(tempId),doc.getReportDate());
                		dupTimeReceived.put(Integer.parseInt(tempId),doc.getTimeReceived());
                	}
                
                }
                */
            }
        }
       
    } 
    
    public void printPdf() throws IOException, DocumentException{

        // check that we have data to print
        if (report == null)
            throw new DocumentException();

         //Create the document we are going to write to
        document = new Document();
  
        writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);

        //Set page event, function onEndPage will execute each time a page is finished being created
        writer.setPageEvent(this);

        document.setPageSize(PageSize.LETTER);
        document.addTitle("HRM Report");
        document.addCreator("OSCAR");
        document.open();

        //Create the fonts that we are going to use
        bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font = new Font(bf, 9, Font.NORMAL);
        boldFont = new Font(bf, 10, Font.BOLD);
      //  redFont = new Font(bf, 9, Font.NORMAL, Color.RED);

        
        // add the header table containing the patient and lab info to the document
        createInfoTable();
        
        addBinaryData();

        // add end of report table
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
        document.add(table);

        document.close();

        os.flush();
    }
    
    private void createInfoTable() throws DocumentException {

        //Create patient info table
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        float[] pInfoWidths = {2f, 4f, 3f, 2f};
        PdfPTable pInfoTable = new PdfPTable(pInfoWidths);
        cell.setPhrase(new Phrase("Patient Name: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getLegalName(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Home Phone: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getPhoneNumber(), font));
        pInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Address: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getAddressLine1() + "\n" + ((report.getAddressLine2() != null)?(report.getAddressLine2() + "\n"):"") + report.getAddressCity() + "\n" + report.getPostalCode(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("DOB: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getDateOfBirthAsString(), font));
        pInfoTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Gender: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getGender(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("HCN: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getHCN() + " " + report.getHCNVersion() + "(" + report.getHCNProvinceCode() + ")", font));
        pInfoTable.addCell(cell);
        
        
        float[] pTransactionWidths = {2f, 4f, 3f, 2f};
        PdfPTable pTransactionTable = new PdfPTable(pTransactionWidths);
        cell = new PdfPCell(new Phrase("Report Details", boldFont));
        cell.setBackgroundColor(new Color(210, 212, 255));
        cell.setPadding(3);
        cell.setColspan(4);
        pTransactionTable.addCell(cell);
        
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPhrase(new Phrase("Report Date: ", boldFont));
        pTransactionTable.addCell(cell);
        cell.setPhrase(new Phrase(sdf.format(hrmDocument.getReportDate()), font));
        pTransactionTable.addCell(cell);
        cell.setPhrase(new Phrase("Observation Date: ", boldFont));
        pTransactionTable.addCell(cell);
        String reportDate = (report.getFirstReportEventTime() != null ? report.getFirstReportEventTime().getTime().toString() : 
			((report.getFirstAccompanyingSubClassDateTime() != null ? report.getFirstAccompanyingSubClassDateTime().getTime().toString() : "")));
        cell.setPhrase(new Phrase(reportDate, font));
        pTransactionTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Report Class: ", boldFont));
        pTransactionTable.addCell(cell);
        cell.setPhrase(new Phrase(report.getFirstReportClass(), font));
        pTransactionTable.addCell(cell);
        cell.setPhrase(new Phrase("Report SubClass: ", boldFont));
        pTransactionTable.addCell(cell);
        
        List<List<Object>> subClassListFromReport = report.getAccompanyingSubclassList();
        String subClassStr = "";
        if (subClassListFromReport.size() > 0) {
	        for (List<Object> subClass : subClassListFromReport) {
	        	if(subClassStr.length()>0) {
	        		subClassStr += "\n";
	        	}
	        	subClassStr += "(" + subClass.get(1) +")" +  subClass.get(2);
	        }
        } else {
        	String[] subClassFromReport = report.getFirstReportSubClass().split("\\^");
			if (subClassFromReport.length == 2) {
				if(subClassStr.length()>0) {
	        		subClassStr += "\n";
	        	}
	        	subClassStr += subClassFromReport[1];
			}
        }
        cell.setPhrase(new Phrase(subClassStr, font));
        pTransactionTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Duplicates Received ", boldFont));
        pTransactionTable.addCell(cell);
        cell.setPhrase(new Phrase(hrmDocument.getNumDuplicatesReceived()!=null?hrmDocument.getNumDuplicatesReceived().toString():"0", font));
        pTransactionTable.addCell(cell);
        
        List<HRMDocumentSubClass> subClassList = hrmDocumentSubClassDao.getSubClassesByDocumentId(hrmDocument.getId());
        
        HRMDocumentSubClass hrmDocumentSubClass=null;
        if (subClassList!= null)
        {
        	for (HRMDocumentSubClass temp : subClassList)
        	{
        		if (temp.isActive())
        		{
        			hrmDocumentSubClass=temp;
        			break;
        		}
        	}
        }
        HRMCategory category = null;
        if (hrmDocumentSubClass != null) {
            category = hrmCategoryDao.findBySubClassNameMnemonic(hrmDocumentSubClass.getSubClass()+':'+hrmDocumentSubClass.getSubClassMnemonic());
        }
        else
        {
        	category=hrmCategoryDao.findBySubClassNameMnemonic("DEFAULT");
        }
        
        
        cell.setPhrase(new Phrase("Category", boldFont));
        pTransactionTable.addCell(cell);
        cell.setPhrase(new Phrase(category!=null?category.getCategoryName():"", font));
        pTransactionTable.addCell(cell);
        
        
        float[] tableWidths = {2f};
        PdfPTable table = new PdfPTable(tableWidths);
        
        cell = new PdfPCell(new Phrase("Patient Info", boldFont));
        cell.setBackgroundColor(new Color(210, 212, 255));
        cell.setPadding(3);
        table.addCell(cell);
       
        
        
        table = addTableToTable(table, pInfoTable, 1);
        table = addTableToTable(table, pTransactionTable, 1);
        
        if(!report.isBinary()) {
        	float[] pDataWidths = {1f};
            PdfPTable pDataTable = new PdfPTable(pDataWidths);
            cell = new PdfPCell(new Phrase(report.getFirstReportTextContent(), font));
            pDataTable.addCell(cell);
            table = addTableToTable(table, pDataTable, 1);
        }
        
        if(report.isBinary() && !checkIfBinaryIsSupported()) {
        	float[] pDataWidths = {1f};
            PdfPTable pDataTable = new PdfPTable(pDataWidths);
            pDataTable.addCell(cell);
            cell = new PdfPCell(new Phrase("This report contains attachments not included in this printed report.", boldFont));
            table = addTableToTable(table, pDataTable, 1);
        }
        
        table.setWidthPercentage(100);

        document.add(table);
        
    }
    
    private boolean checkIfBinaryIsSupported() {
    	String ext = report.getFileExtension();
    	
    	if(!report.isBinary()) {
    		return true;
    	}
    	
    	if(".pdf".equals(ext) || ".jpg".equals(ext) || ".gif".equals(ext) || ".png".equals(ext)) {
    		return true;
    	}
    	
    	return false;
    }
    
    private void addBinaryData() throws IOException, BadElementException, DocumentException {
    	if(!report.isBinary()) {
    		return;
    	}
    	PdfContentByte cb = writer.getDirectContent();
    	
    	String reportFileData = report.getFileData();
    	String ext = report.getFileExtension();
    	byte[] data = report.getBinaryContent();
    	
    	
    	if(".pdf".equals(ext)) {
    		PdfReader reader = new PdfReader(data);
    		reader.consolidateNamedDestinations();
    		
    		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
    		
    	} else if(".jpg".equals(ext) || ".gif".equals(ext) || ".png".equals(ext)) {
    		Image image1 = Image.getInstance(data);
    		document.add(image1);
    	} else if(".tiff".equals(ext)) {
    		/*
    		RandomAccessFileOrArray raf1 = new RandomAccessFileOrArray(data);
    		int noOfPages = TiffImage.getNumberOfPages(raf1); 
    		Image img = null;
    		for(int i = 1; i <= noOfPages; i++){ 
    			document.newPage();
    			img = TiffImage.getTiffImage(raf1, i); 
    			document.add(img); 
    		}
    		*/
    	} else if(".rtf".equals(ext)) {
    		//RtfWriter2 writer2 = RtfWriter2.getInstance(document,os);
    		//writer.get
    		//ByteArrayInputStream rtfStream = new ByteArrayInputStream(data);
    		//writer2.importRtfDocument(rtfStream, null);
    	} else if(".html".equals(ext)) {
    	}
    	
    }
    
    
    private PdfPTable addTableToTable(PdfPTable main, PdfPTable add, int colspan){
        PdfPCell cell = new PdfPCell(add);
        cell.setPadding(3);
        cell.setColspan(colspan);
        main.addCell(cell);
        return main;
    }
    
}
