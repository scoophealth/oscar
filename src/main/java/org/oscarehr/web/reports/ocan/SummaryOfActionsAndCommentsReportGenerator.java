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


package org.oscarehr.web.reports.ocan;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.web.reports.ocan.beans.SummaryOfActionsAndCommentsDomainBean;
import org.oscarehr.web.reports.ocan.beans.SummaryOfActionsAndCommentsOCANBean;
import org.oscarehr.web.reports.ocan.beans.SummaryOfActionsAndCommentsReportBean;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class SummaryOfActionsAndCommentsReportGenerator {

	@SuppressWarnings("unused")
	private Logger logger = MiscUtils.getLogger();

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	private Font titleFont = new Font(Font.TIMES_ROMAN, 30,Font.BOLD);
	private Font boldText = new Font(Font.HELVETICA,10,Font.BOLD);
	private Font normalText = new Font(Font.HELVETICA,10,Font.NORMAL);

	private String consumerName;
	private String staffName;
	private Date reportDate;
	private SummaryOfActionsAndCommentsReportBean reportBean;
	private boolean includeComments = false;


	public SummaryOfActionsAndCommentsReportGenerator() {

	}

	public void generateReport(OutputStream os) throws Exception  {
		Document d = new Document (PageSize.A4.rotate());
		d.setMargins(20, 20, 20, 20);
		PdfWriter writer = PdfWriter.getInstance (d, os);
		writer.setStrictImageSequence(true);
		d.open ();

		//header
		Paragraph p = new Paragraph("Summary of Actions and Comments",titleFont);
		p.setAlignment(Element.ALIGN_CENTER);
		d.add(p);
		d.add(Chunk.NEWLINE);

		//purpose
		Paragraph purpose = new Paragraph();
		purpose.add(new Chunk("Purpose of Report:",boldText));
		purpose.add(new Phrase("This report displays a summary of Actions and Comments (both for the Consumer and the Staff) that were recorded in the selected OCANs for an individual Consumer. The report lists all the Actions and Comments associated to OCANs and Domains that were chosen by the Staff to be displayed. The Actions also list who is responsible for the Action and the Review Date for the Action. The Domains are categorized by need rating within within the current OCAN as well as displaying the need ratings from the previous OCANs. In the case of different need ratings by the Consumer and the Mental Health Worker, the higher need rating determines the category. The need ratings from highest to lowest are Unmet Needs, Met Needs, No Needs, and Unknown. The need rating given by the Consumer and the Staff are also displayed next to the Comments for each OCAN. This information can be passed along to other organizations if requested.",normalText));
		d.add(purpose);
		d.add(Chunk.NEWLINE);

		//report parameters
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(0);
		table.addCell(makeCell(createFieldNameAndValuePhrase("Consumer Name:",getConsumerName()),Element.ALIGN_LEFT));
		table.addCell(makeCell(createFieldNameAndValuePhrase("Report Date:",dateFormatter.format(getReportDate())),Element.ALIGN_RIGHT));
		table.addCell(makeCell(createFieldNameAndValuePhrase("Staff Name:",getStaffName()),Element.ALIGN_LEFT));
		table.addCell("");
		d.add(table);
		d.add(Chunk.NEWLINE);

		if(reportBean == null) {
			d.close();
			return;
		}

		List<SummaryOfActionsAndCommentsDomainBean> unMetCategory = reportBean.getUnmetNeeds();
		List<SummaryOfActionsAndCommentsDomainBean> metCategory = reportBean.getMetNeeds();
		List<SummaryOfActionsAndCommentsDomainBean> noCategory = reportBean.getNoNeeds();
		List<SummaryOfActionsAndCommentsDomainBean> unknownCategory = reportBean.getUnknown();

		PdfPTable unmetTable = null;
		if(unMetCategory.size()>0) {
			unmetTable = createNeedHeader("Unmet Needs");
		}
		for(SummaryOfActionsAndCommentsDomainBean domain:unMetCategory) {
			if(domain.getOcanBeans().size()>0) {
				createDomainHeader(unmetTable,domain.getDomainName());
				for(SummaryOfActionsAndCommentsOCANBean ocanBean:domain.getOcanBeans()) {
					createOcanEntry(unmetTable, ocanBean);
				}
			}
		}
		if(unmetTable!=null) {
			d.add(unmetTable);
			d.add(Chunk.NEWLINE);
		}

		PdfPTable metTable = null;
		if(metCategory.size()>0) {
			metTable = createNeedHeader("Met Needs");
		}
		for(SummaryOfActionsAndCommentsDomainBean domain:metCategory) {
			if(domain.getOcanBeans().size()>0) {
				createDomainHeader(metTable,domain.getDomainName());
				for(SummaryOfActionsAndCommentsOCANBean ocanBean:domain.getOcanBeans()) {
					createOcanEntry(metTable, ocanBean);
				}
			}
		}
		if(metTable!=null) {
			d.add(metTable);
			d.add(Chunk.NEWLINE);
		}

		PdfPTable noTable = null;
		if(noCategory.size()>0) {
			noTable = createNeedHeader("No Needs");
		}
		for(SummaryOfActionsAndCommentsDomainBean domain:noCategory) {
			if(domain.getOcanBeans().size()>0) {
				createDomainHeader(noTable,domain.getDomainName());
				for(SummaryOfActionsAndCommentsOCANBean ocanBean:domain.getOcanBeans()) {
					createOcanEntry(noTable, ocanBean);
				}
			}
		}
		if(noTable!=null) {
			d.add(noTable);
			d.add(Chunk.NEWLINE);
		}


		PdfPTable unknownTable = null;
		if(unknownCategory.size()>0) {
			unknownTable = createNeedHeader("Unknown");
		}
		for(SummaryOfActionsAndCommentsDomainBean domain:unknownCategory) {
			if(domain.getOcanBeans().size()>0) {
				createDomainHeader(unknownTable,domain.getDomainName());
				for(SummaryOfActionsAndCommentsOCANBean ocanBean:domain.getOcanBeans()) {
					createOcanEntry(unknownTable, ocanBean);
				}
			}
		}
		if(unknownTable!=null) {
			d.add(unknownTable);
		}

		d.close();
	}

	private String convertNeedToWord(String key) {
		if(key == null)
			return "";
		if(key.equals("2")) {
			return "Unmet Need";
		}
		if(key.equals("1")) {
			return "Met Need";
		}
		if(key.equals("0")) {
			return "No Need";
		}
		if(key.equals("9")) {
			return "Unknown";
		}
		return "";
	}

	private void createOcanEntry(PdfPTable table, SummaryOfActionsAndCommentsOCANBean ocanBean) {
		Font f = new Font(Font.HELVETICA,12,Font.BOLD,Color.BLACK);
		//header
		PdfPCell c1 = new PdfPCell();
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setPhrase(new Phrase("Rating",f));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(2);
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase(ocanBean.getOcanName() + " - " + dateFormatter.format(ocanBean.getOcanDate()),f));
		table.addCell(c1);

		//actions
		c1 = new PdfPCell();
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setBorderWidthBottom(0);
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase("Actions",f));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setPhrase(new Phrase(ocanBean.getActions(),normalText));
		table.addCell(c1);

		//By Whom
		c1 = new PdfPCell();
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setBorderWidthBottom(0);
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase("By Whom",f));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setPhrase(new Phrase(ocanBean.getByWhom(),normalText));
		table.addCell(c1);

		//review date
		c1 = new PdfPCell();
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setBorderWidthBottom(0);
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase("Review Date",f));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setPhrase(new Phrase(ocanBean.getReviewDate(),normalText));
		table.addCell(c1);

		//consumer
		c1 = new PdfPCell();
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase(convertNeedToWord(ocanBean.getConsumerNeedRating()),boldText));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase("Consumer Comments",f));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setPhrase(new Phrase(includeComments?ocanBean.getConsumerComments():"",normalText));
		table.addCell(c1);

		//staff
		c1 = new PdfPCell();
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase(convertNeedToWord(ocanBean.getStaffNeedRating()),boldText));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c1.setPhrase(new Phrase("Staff Comments",f));
		table.addCell(c1);
		c1 = new PdfPCell();
		c1.setColspan(1);
		c1.setPhrase(new Phrase(includeComments?ocanBean.getStaffComments():"",normalText));
		table.addCell(c1);
	}

	private void createDomainHeader(PdfPTable table, String name) {
		Font f = new Font(Font.HELVETICA,14,Font.BOLD,Color.BLACK);
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setBorder(0);
		table.addCell(emptyCell);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setColspan(2);
		headerCell.setPhrase(new Phrase(name,f));
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setBackgroundColor(Color.LIGHT_GRAY);
		table.addCell(headerCell);
	}

	private PdfPTable createNeedHeader(String name) throws DocumentException {
		Font whiteFont = new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE);
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{0.10f,0.20f,0.70f});
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setBorder(0);
		table.addCell(emptyCell);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setColspan(2);
		headerCell.setPhrase(new Phrase(name,whiteFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setBackgroundColor(Color.LIGHT_GRAY);
		table.addCell(headerCell);
		return table;
	}

	private Phrase createFieldNameAndValuePhrase(String fn, String fv) {
		Chunk fieldName = new Chunk(fn,boldText);
		Chunk fieldValue = new Chunk(fv,normalText);
		Phrase field = new Phrase();
		field.add(fieldName);
		field.add(fieldValue);
		return field;
	}

	private PdfPCell makeCell(Phrase phrase, int alignment) {
		PdfPCell c2 = new PdfPCell();
		c2.setPhrase(phrase);
		c2.setHorizontalAlignment(alignment);
		c2.setBorder(0);
		return c2;
	}


	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public SummaryOfActionsAndCommentsReportBean getReportBean() {
		return reportBean;
	}

	public void setReportBean(SummaryOfActionsAndCommentsReportBean reportBean) {
		this.reportBean = reportBean;
	}


	public boolean isIncludeComments() {
		return includeComments;
	}

	public void setIncludeComments(boolean includeComments) {
		this.includeComments = includeComments;
	}

	public static void main(String args[]) throws Exception {

		SummaryOfActionsAndCommentsReportGenerator report = new SummaryOfActionsAndCommentsReportGenerator();

		report.setConsumerName("John Doe");
		report.setStaffName("Marc Dumontier");
		report.setReportDate(new Date());

		report.generateReport(new FileOutputStream ("/home/marc/sample.pdf"));
	}
}
