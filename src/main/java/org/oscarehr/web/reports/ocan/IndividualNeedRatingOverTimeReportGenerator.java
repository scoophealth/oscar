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

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.web.reports.ocan.beans.OcanIndividualNeedsOverTimeBean;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class IndividualNeedRatingOverTimeReportGenerator {

	@SuppressWarnings("unused")
	private Logger logger = MiscUtils.getLogger();

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	private Font titleFont = new Font(Font.TIMES_ROMAN, 30,Font.BOLD);
	private Font boldText = new Font(Font.HELVETICA,10,Font.BOLD);
	private Font normalText = new Font(Font.HELVETICA,10,Font.NORMAL);

	private String consumerName;
	private String staffName;
	private Date reportDate;
	private OcanIndividualNeedsOverTimeBean reportBean;


	public IndividualNeedRatingOverTimeReportGenerator() {

	}

	public void generateReport(OutputStream os) throws Exception  {
		Document d = new Document (PageSize.A4.rotate());
		d.setMargins(20, 20, 20, 20);
		PdfWriter writer = PdfWriterFactory.newInstance(d, os, FontSettings.HELVETICA_10PT);
		// PdfWriter writer = PdfWriter.getInstance (d, os);
		writer.setStrictImageSequence(true);
		d.open ();

		//header
		Paragraph p = new Paragraph("Individual Need Rating Over Time",titleFont);
		p.setAlignment(Element.ALIGN_CENTER);
		d.add(p);
		d.add(Chunk.NEWLINE);

		//purpose
		Paragraph purpose = new Paragraph();
		purpose.add(new Chunk("Purpose of Report:",boldText));
		purpose.add(new Phrase("The purpose of this report is to show change over time in a specific Need Rating for an individual Consumer. It adds up the number of needs across all Domains grouped by Need Rating (e.g. Unmet Needs, Met Needs, No Needs, Unknown) for all selected OCANs that were conducted with the Consumer and displays the results in an individual need rating line graph. Each line graph that is displayed compares the Consumer and the Staff's perspective. The staff may share this report with their Consumer as well.",normalText));
		d.add(purpose);
		d.add(Chunk.NEWLINE);

		//report parameters
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(0);
		table.addCell(makeCell(createFieldNameAndValuePhrase("Consumer Name:",reportBean.getConsumerName()),Element.ALIGN_LEFT));
		table.addCell(makeCell(createFieldNameAndValuePhrase("Report Date:",dateFormatter.format(reportBean.getReportDate())),Element.ALIGN_RIGHT));
		table.addCell(makeCell(createFieldNameAndValuePhrase("Staff Name:",reportBean.getStaffName()),Element.ALIGN_LEFT));
		table.addCell("");
		d.add(table);
		d.add(Chunk.NEWLINE);

		int height = 260;

		if(reportBean.isShowUnmetNeeds()) {
			d.add(Image.getInstance(reportBean.getUnmetNeedsChart().createBufferedImage((int)PageSize.A4.rotate().getWidth()-40, height), null));
		}

		if(reportBean.isShowMetNeeds()) {
			d.add(Image.getInstance(reportBean.getMetNeedsChart().createBufferedImage((int)PageSize.A4.rotate().getWidth()-40, height), null));
		}

		if(reportBean.isShowNoNeeds()) {
			d.add(Image.getInstance(reportBean.getNoNeedsChart().createBufferedImage((int)PageSize.A4.rotate().getWidth()-40, height), null));
		}
		if(reportBean.isShowUnknownNeeds()) {
			d.add(Image.getInstance(reportBean.getUnknownNeedsChart().createBufferedImage((int)PageSize.A4.rotate().getWidth()-40, height), null));
		}




		d.close();
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

	public OcanIndividualNeedsOverTimeBean getReportBean() {
		return reportBean;
	}

	public void setReportBean(OcanIndividualNeedsOverTimeBean reportBean) {
		this.reportBean = reportBean;
	}

	public static void main(String args[]) throws Exception {

		IndividualNeedRatingOverTimeReportGenerator report = new IndividualNeedRatingOverTimeReportGenerator();

		report.setReportBean(new OcanIndividualNeedsOverTimeBean());
		report.setConsumerName("John Doe");
		report.setStaffName("Marc Dumontier");
		report.setReportDate(new Date());

		report.generateReport(new FileOutputStream ("/home/nick/sample.pdf"));
	}
}
