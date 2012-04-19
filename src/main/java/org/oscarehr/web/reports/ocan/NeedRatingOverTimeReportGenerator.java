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
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.web.reports.ocan.beans.OcanConsumerStaffNeedBean;
import org.oscarehr.web.reports.ocan.beans.OcanNeedRatingOverTimeNeedBreakdownBean;
import org.oscarehr.web.reports.ocan.beans.OcanNeedRatingOverTimeSummaryOfNeedsBean;

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

public class NeedRatingOverTimeReportGenerator {

	@SuppressWarnings("unused")
	private Logger logger = MiscUtils.getLogger();

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	private Font titleFont = new Font(Font.TIMES_ROMAN, 30,Font.BOLD);
	private Font boldText = new Font(Font.HELVETICA,10,Font.BOLD);
	private Font normalText = new Font(Font.HELVETICA,10,Font.NORMAL);

	private String consumerName;
	private String staffName;
	private Date reportDate;
	private List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsBeanList;
	private List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsNoDomainBeanList;
	private List<String> domains;
	private List<OcanNeedRatingOverTimeNeedBreakdownBean> needBreakdownListByOCAN;


	public NeedRatingOverTimeReportGenerator() {

	}

	public void generateReport(OutputStream os) throws Exception  {
		Document d = new Document (PageSize.A4.rotate());
		d.setMargins(20, 20, 20, 20);
		PdfWriter writer = PdfWriter.getInstance (d, os);
		writer.setStrictImageSequence(true);
		d.open ();

		//header
		Paragraph p = new Paragraph("Needs Over Time (Consumer and Staff)",titleFont);
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
		table.addCell(makeCell(createFieldNameAndValuePhrase("Consumer Name:",getConsumerName()),Element.ALIGN_LEFT));
		table.addCell(makeCell(createFieldNameAndValuePhrase("Report Date:",dateFormatter.format(getReportDate())),Element.ALIGN_RIGHT));
		table.addCell(makeCell(createFieldNameAndValuePhrase("Staff Name:",getStaffName()),Element.ALIGN_LEFT));
		table.addCell("");
		d.add(table);
		d.add(Chunk.NEWLINE);


		//loop here...groups of 3
		int loopNo = 1;
		List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryBeanList = new ArrayList<OcanNeedRatingOverTimeSummaryOfNeedsBean>();
		summaryBeanList.addAll(this.summaryOfNeedsBeanList);

		while(true) {
			if(summaryBeanList.size()==0){
				break;
			}
			List<OcanNeedRatingOverTimeSummaryOfNeedsBean> currentBeanList = new ArrayList<OcanNeedRatingOverTimeSummaryOfNeedsBean>();
			for(int x=0;x<3;x++) {
				if(summaryBeanList.size()==0){
					break;
				}
				currentBeanList.add(summaryBeanList.remove(0));
			}

			//summary of needs
			PdfPTable summaryOfNeedsTable = null;
			if(currentBeanList.size()==1) {
				summaryOfNeedsTable = new PdfPTable(3);
				summaryOfNeedsTable.setWidthPercentage(100f-52.8f);
				summaryOfNeedsTable.setWidths(new float[]{0.26f,0.12f,0.12f});
			}
			if(currentBeanList.size()==2) {
				summaryOfNeedsTable = new PdfPTable(6);
				summaryOfNeedsTable.setWidthPercentage(100f-26.4f);
				summaryOfNeedsTable.setWidths(new float[]{0.26f,0.12f,0.12f,0.024f,0.12f,0.12f});
			}
			if(currentBeanList.size()==3) {
				summaryOfNeedsTable = new PdfPTable(9);
				summaryOfNeedsTable.setWidthPercentage(100f);
				summaryOfNeedsTable.setWidths(new float[]{0.26f,0.12f,0.12f,0.024f,0.12f,0.12f,0.024f,0.12f,0.12f});
			}
			summaryOfNeedsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			summaryOfNeedsTable.setHeaderRows(3);

			addSummaryOfNeedsHeader(summaryOfNeedsTable,currentBeanList, loopNo);
			addSummaryOfNeedsRow(summaryOfNeedsTable,"Unmet Needs","unmet",currentBeanList);
			addSummaryOfNeedsRow(summaryOfNeedsTable,"Met Needs","met",currentBeanList);
			addSummaryOfNeedsRow(summaryOfNeedsTable,"No Needs","no",currentBeanList);
			addSummaryOfNeedsRow(summaryOfNeedsTable,"Unknown Needs","unknown",currentBeanList);

			d.add(summaryOfNeedsTable);
			d.add(Chunk.NEWLINE);

			if(summaryBeanList.size() == 0) {
				break;
			}
			loopNo++;
		}


		//BREAKDOWN OF SUMMARY OF NEEDS


		//loop here...groups of 3
		loopNo = 1;
		List<OcanNeedRatingOverTimeNeedBreakdownBean> breakdownBeanList = new ArrayList<OcanNeedRatingOverTimeNeedBreakdownBean>();
		breakdownBeanList.addAll(this.needBreakdownListByOCAN);
		OcanNeedRatingOverTimeNeedBreakdownBean lastBreakDownBean = null;
		while(true) {
			if(breakdownBeanList.size()==0){
				break;
			}
			List<OcanNeedRatingOverTimeNeedBreakdownBean> currentBeanList = new ArrayList<OcanNeedRatingOverTimeNeedBreakdownBean>();
			for(int x=0;x<3;x++) {
				if(breakdownBeanList.size()==0){
					break;
				}
				currentBeanList.add(breakdownBeanList.remove(0));
			}

			//summary of needs
			PdfPTable summaryOfNeedsTable = null;
			if(currentBeanList.size()==1) {
				if(lastBreakDownBean == null) {
					summaryOfNeedsTable = new PdfPTable(3);
					summaryOfNeedsTable.setWidthPercentage(100f-52.8f);
					summaryOfNeedsTable.setWidths(new float[]{0.26f,0.12f,0.12f});
				} else {
					summaryOfNeedsTable = new PdfPTable(4);
					summaryOfNeedsTable.setWidthPercentage(100f-52.8f);
					summaryOfNeedsTable.setWidths(new float[]{0.26f-0.024f,0.024f,0.12f,0.12f});
				}
			}
			if(currentBeanList.size()==2) {
				if(lastBreakDownBean == null) {
					summaryOfNeedsTable = new PdfPTable(6);
					summaryOfNeedsTable.setWidthPercentage(100f-26.4f);
					summaryOfNeedsTable.setWidths(new float[]{0.26f,0.12f,0.12f,0.024f,0.12f,0.12f});
				} else {
					summaryOfNeedsTable = new PdfPTable(7);
					summaryOfNeedsTable.setWidthPercentage(100f-26.4f);
					summaryOfNeedsTable.setWidths(new float[]{0.26f-0.024f,0.024f,0.12f,0.12f,0.024f,0.12f,0.12f});
				}
			}
			if(currentBeanList.size()==3) {
				if(lastBreakDownBean == null) {
					summaryOfNeedsTable = new PdfPTable(9);
					summaryOfNeedsTable.setWidthPercentage(100f);
					summaryOfNeedsTable.setWidths(new float[]{0.26f,0.12f,0.12f,0.024f,0.12f,0.12f,0.024f,0.12f,0.12f});
				} else {
					summaryOfNeedsTable = new PdfPTable(10);
					summaryOfNeedsTable.setWidthPercentage(100f);
					summaryOfNeedsTable.setWidths(new float[]{0.26f-0.024f,0.024f,0.12f,0.12f,0.024f,0.12f,0.12f,0.024f,0.12f,0.12f});
				}
			}
			summaryOfNeedsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			addSummaryOfNeedsDomainHeader(summaryOfNeedsTable,currentBeanList, loopNo);
			for(int x=0;x<domains.size();x++) {
				addSummaryOfNeedsDomainRow(summaryOfNeedsTable,x,getDomains(),currentBeanList, lastBreakDownBean);
			}

			d.add(summaryOfNeedsTable);
			d.add(Chunk.NEWLINE);

			if(breakdownBeanList.size() == 0) {
				break;
			}
			if(currentBeanList.size()==3) {
				lastBreakDownBean = currentBeanList.get(2);
			}
			loopNo++;
		}

		JFreeChart chart = generateNeedsOverTimeChart();
		BufferedImage image = chart.createBufferedImage((int)PageSize.A4.rotate().getWidth()-40, 350);
		Image image2 = Image.getInstance(image, null);
		d.add(image2);

		d.close ();
	}

	private void addSummaryOfNeedsHeader(PdfPTable summaryOfNeedsTable, List<OcanNeedRatingOverTimeSummaryOfNeedsBean> currentBeanList, int loopNo) {

		PdfPCell headerCell = new PdfPCell();
		headerCell.setPhrase(new Phrase("Summary of Needs" + (loopNo>1?" (Contd.)":""),new Font(Font.HELVETICA, 20,Font.BOLD)));

		headerCell.setColspan(currentBeanList.size()*3);
		headerCell.setVerticalAlignment(Element.ALIGN_TOP);
		headerCell.setBorder(0);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryOfNeedsTable.addCell(headerCell);

		Font f = new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE);
		//row1
		PdfPCell c2 = null;

		c2 = new PdfPCell();
		c2.setBorder(0);
		summaryOfNeedsTable.addCell(c2);
		if(currentBeanList.size()>0) {
			c2 = new PdfPCell();
			c2.setColspan(2);
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase(currentBeanList.get(0).getOcanName()+"\n"+dateFormatter.format(currentBeanList.get(0).getOcanDate()),f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size()>1) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setColspan(2);
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase(currentBeanList.get(1).getOcanName()+"\n"+dateFormatter.format(currentBeanList.get(1).getOcanDate()),f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size()>2) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setColspan(2);
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase(currentBeanList.get(2).getOcanName()+"\n"+dateFormatter.format(currentBeanList.get(2).getOcanDate()),f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}

		//row2
		c2 = new PdfPCell();
		c2.setBorder(0);
		summaryOfNeedsTable.addCell(c2);

		if(currentBeanList.size() > 0) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Consumer",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Staff",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size() > 1) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Consumer",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Staff",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size() > 2) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Consumer",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Staff",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
	}

	private void addSummaryOfNeedsDomainHeader(PdfPTable summaryOfNeedsTable, List<OcanNeedRatingOverTimeNeedBreakdownBean> currentBeanList, int loopNo) {

		Font f = new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE);
		//row1
		PdfPCell c2 = null;

		c2 = new PdfPCell();
		c2.setBorder(0);
		summaryOfNeedsTable.addCell(c2);

		if(loopNo > 1) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
		}

		if(currentBeanList.size()>0) {
			c2 = new PdfPCell();
			c2.setColspan(2);
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase(currentBeanList.get(0).getOcanName()+"\n"+dateFormatter.format(currentBeanList.get(0).getOcanDate()),f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size()>1) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setColspan(2);
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase(currentBeanList.get(1).getOcanName()+"\n"+dateFormatter.format(currentBeanList.get(1).getOcanDate()),f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size()>2) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setColspan(2);
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase(currentBeanList.get(2).getOcanName()+"\n"+dateFormatter.format(currentBeanList.get(2).getOcanDate()),f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}

		//row2
		c2 = new PdfPCell();
		c2.setBackgroundColor(Color.BLUE);
		c2.setPhrase(new Phrase("Domains",f));
		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryOfNeedsTable.addCell(c2);

		if(loopNo > 1) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
		}

		if(currentBeanList.size() > 0) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Consumer",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Staff",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size() > 1) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Consumer",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Staff",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size() > 2) {
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Consumer",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLUE);
			c2.setPhrase(new Phrase("Staff",f));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}

	}

	private void addSummaryOfNeedsRow(PdfPTable summaryOfNeedsTable,String needType, String needTypeKey, List<OcanNeedRatingOverTimeSummaryOfNeedsBean> currentBeanList) {
		//row3
		PdfPCell c2 = new PdfPCell();
		c2.setBackgroundColor(Color.BLUE);
		c2.setPhrase(new Phrase(needType,new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE)));
		summaryOfNeedsTable.addCell(c2);

		if(currentBeanList.size()>0 ){
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.GREEN);
			c2.setPhrase(new Phrase(String.valueOf(currentBeanList.get(0).getConsumerNeedMap().get(needTypeKey)),new Font(Font.HELVETICA,14,Font.BOLD)));
			c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.LIGHT_GRAY);
			c2.setPhrase(new Phrase(String.valueOf(currentBeanList.get(0).getStaffNeedMap().get(needTypeKey)),new Font(Font.HELVETICA,14,Font.BOLD)));
			c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size()>1 ){
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.GREEN);
			c2.setPhrase(new Phrase(String.valueOf(currentBeanList.get(1).getConsumerNeedMap().get(needTypeKey)),new Font(Font.HELVETICA,14,Font.BOLD)));
			c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.LIGHT_GRAY);
			c2.setPhrase(new Phrase(String.valueOf(currentBeanList.get(1).getStaffNeedMap().get(needTypeKey)),new Font(Font.HELVETICA,14,Font.BOLD)));
			c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryOfNeedsTable.addCell(c2);
		}
		if(currentBeanList.size()>2 ){
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.GREEN);
			c2.setPhrase(new Phrase(String.valueOf(currentBeanList.get(2).getConsumerNeedMap().get(needTypeKey)),new Font(Font.HELVETICA,14,Font.BOLD)));
			c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.LIGHT_GRAY);
			c2.setPhrase(new Phrase(String.valueOf(currentBeanList.get(2).getStaffNeedMap().get(needTypeKey)),new Font(Font.HELVETICA,14,Font.BOLD)));
			c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryOfNeedsTable.addCell(c2);
		}

	}


	private void addSummaryOfNeedsDomainRow(PdfPTable summaryOfNeedsTable, int rowNum, List<String> domains, List<OcanNeedRatingOverTimeNeedBreakdownBean> ocans, OcanNeedRatingOverTimeNeedBreakdownBean lastBreakDownBean) {

		PdfPCell c2 = new PdfPCell();
		c2.setBackgroundColor(Color.BLUE);
		c2.setPhrase(new Phrase(domains.get(rowNum),new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE)));
		summaryOfNeedsTable.addCell(c2);

		if(lastBreakDownBean != null) {
			boolean checkmark=false;
			if(!ocans.get(0).getNeeds().get(rowNum).getConsumerNeed().equals(lastBreakDownBean.getNeeds().get(rowNum).getConsumerNeed())) {
				checkmark=true;
			}
			if(!ocans.get(0).getNeeds().get(rowNum).getStaffNeed().equals(lastBreakDownBean.getNeeds().get(rowNum).getStaffNeed())) {
				checkmark=true;
			}
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			if(checkmark) {
				c2.setPhrase(new Phrase("X",new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE)));
			}
			summaryOfNeedsTable.addCell(c2);
		}

		if(ocans.size()>0 ){
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.GREEN);
			c2.setPhrase(new Phrase(ocans.get(0).getNeeds().get(rowNum).getConsumerNeed(),new Font(Font.HELVETICA,14,(ocans.get(0).getNeeds().get(rowNum).getConsumerNeed().equals("Unmet Need")?Font.BOLD:Font.NORMAL),(ocans.get(0).getNeeds().get(rowNum).getConsumerNeed().equals("Unmet Need")?Color.RED:Color.BLACK))));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.LIGHT_GRAY);
			c2.setPhrase(new Phrase(ocans.get(0).getNeeds().get(rowNum).getStaffNeed(),new Font(Font.HELVETICA,14,(ocans.get(0).getNeeds().get(rowNum).getStaffNeed().equals("Unmet Need")?Font.BOLD:Font.NORMAL),(ocans.get(0).getNeeds().get(rowNum).getStaffNeed().equals("Unmet Need")?Color.RED:Color.BLACK))));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(ocans.size()>1 ){
			boolean checkmark=false;
			if(!ocans.get(1).getNeeds().get(rowNum).getConsumerNeed().equals((ocans.get(0).getNeeds().get(rowNum).getConsumerNeed()))) {
				checkmark=true;
			}
			if(!ocans.get(1).getNeeds().get(rowNum).getStaffNeed().equals((ocans.get(0).getNeeds().get(rowNum).getStaffNeed()))) {
				checkmark=true;
			}
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			if(checkmark) {
				c2.setPhrase(new Phrase("X",new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE)));
			}
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.GREEN);
			c2.setPhrase(new Phrase(ocans.get(1).getNeeds().get(rowNum).getConsumerNeed(),new Font(Font.HELVETICA,14,(ocans.get(1).getNeeds().get(rowNum).getConsumerNeed().equals("Unmet Need")?Font.BOLD:Font.NORMAL),(ocans.get(1).getNeeds().get(rowNum).getConsumerNeed().equals("Unmet Need")?Color.RED:Color.BLACK))));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.LIGHT_GRAY);
			c2.setPhrase(new Phrase(ocans.get(1).getNeeds().get(rowNum).getStaffNeed(),new Font(Font.HELVETICA,14,(ocans.get(1).getNeeds().get(rowNum).getStaffNeed().equals("Unmet Need")?Font.BOLD:Font.NORMAL),(ocans.get(1).getNeeds().get(rowNum).getStaffNeed().equals("Unmet Need")?Color.RED:Color.BLACK))));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}
		if(ocans.size()>2 ){
			boolean checkmark=false;
			if(!ocans.get(2).getNeeds().get(rowNum).getConsumerNeed().equals((ocans.get(1).getNeeds().get(rowNum).getConsumerNeed()))) {
				checkmark=true;
			}
			if(!ocans.get(2).getNeeds().get(rowNum).getStaffNeed().equals((ocans.get(1).getNeeds().get(rowNum).getStaffNeed()))) {
				checkmark=true;
			}
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.BLACK);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			if(checkmark) {
				c2.setPhrase(new Phrase("X",new Font(Font.HELVETICA,14,Font.BOLD,Color.WHITE)));
			}
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.GREEN);
			c2.setPhrase(new Phrase(ocans.get(2).getNeeds().get(rowNum).getConsumerNeed(),new Font(Font.HELVETICA,14,(ocans.get(2).getNeeds().get(rowNum).getConsumerNeed().equals("Unmet Need")?Font.BOLD:Font.NORMAL),(ocans.get(2).getNeeds().get(rowNum).getConsumerNeed().equals("Unmet Need")?Color.RED:Color.BLACK))));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
			c2 = new PdfPCell();
			c2.setBackgroundColor(Color.LIGHT_GRAY);
			c2.setPhrase(new Phrase(ocans.get(2).getNeeds().get(rowNum).getStaffNeed(),new Font(Font.HELVETICA,14,(ocans.get(2).getNeeds().get(rowNum).getStaffNeed().equals("Unmet Need")?Font.BOLD:Font.NORMAL),(ocans.get(2).getNeeds().get(rowNum).getStaffNeed().equals("Unmet Need")?Color.RED:Color.BLACK))));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryOfNeedsTable.addCell(c2);
		}

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


	private JFreeChart generateNeedsOverTimeChart() {


		CategoryDataset dataset = this.createDataset();

		JFreeChart chart = ChartFactory.createStackedBarChart(
	            "Needs over Time (Consumer and Staff)",      // chart title
	            "Assessments",                      // x axis label
	            "# of Domain",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );

		chart.setBackgroundPaint(Color.LIGHT_GRAY);

		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		KeyToGroupMap map = new KeyToGroupMap("G1");
		map.mapKeyToGroup("Consumer (Unknown)", "G1");
		map.mapKeyToGroup("Consumer (No Needs)", "G1");
		map.mapKeyToGroup("Consumer (Met Needs)", "G1");
		map.mapKeyToGroup("Consumer (Unmet Needs)", "G1");

		map.mapKeyToGroup("Staff (Unknown)", "G2");
		map.mapKeyToGroup("Staff (No Needs)", "G2");
		map.mapKeyToGroup("Staff (Met Needs)", "G2");
		map.mapKeyToGroup("Staff (Unmet Needs)", "G2");

		renderer.setSeriesToGroupMap(map);
		renderer.setItemMargin(0.1);
		//renderer.setItemLabelsVisible(true);
		renderer.setMaximumBarWidth(15);


		Paint p1 = Color.GREEN;
		renderer.setSeriesPaint(0, p1);
		renderer.setSeriesPaint(4, p1);
		renderer.setSeriesPaint(7, p1);

		Paint p2 = Color.BLUE;
		renderer.setSeriesPaint(1, p2);
		renderer.setSeriesPaint(5, p2);
		renderer.setSeriesPaint(8, p2);


		 Paint p3 = new Color(255,255,153);
		 renderer.setSeriesPaint(2, p3);
		 renderer.setSeriesPaint(6, p3);
		 renderer.setSeriesPaint(9, p3);


		 Paint p4 = Color.ORANGE;
		 renderer.setSeriesPaint(3, p4);
		 renderer.setSeriesPaint(7, p4);
		 renderer.setSeriesPaint(10, p4);




		SubCategoryAxis domainAxis = new SubCategoryAxis("Assessments");
		domainAxis.setCategoryMargin(0.05);
		domainAxis.addSubCategory("Consumer");
		domainAxis.addSubCategory("Staff");
		domainAxis.setMinorTickMarkInsideLength(10);
		domainAxis.setMinorTickMarkInsideLength(10);
		domainAxis.setMinorTickMarksVisible(true);


		 CategoryPlot plot = (CategoryPlot) chart.getPlot();
		 plot.setDomainAxis(domainAxis);
		 plot.setRenderer(renderer);

		 LegendItemCollection result = new LegendItemCollection();
		 LegendItem item1 = new LegendItem("Unknown",Color.GREEN);
		 LegendItem item2 = new LegendItem("No Needs",Color.BLUE);
		 LegendItem item3 = new LegendItem("Met Needs",new Color(255,255,153));
		 LegendItem item4 = new LegendItem("Unmet Needs",Color.ORANGE);

		  result.add(item1);
		  result.add(item2);
		  result.add(item3);
		  result.add(item4);

		plot.setFixedLegendItems(result);
		plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.black);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(new Range(0,24),true,false);
        rangeAxis.setTickUnit(new NumberTickUnit(4));

        plot.getDomainAxis().setCategoryMargin(0.35);

        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.BASELINE_LEFT));

        return chart;
	}

	private CategoryDataset createDataset() {
		  DefaultCategoryDataset result = new DefaultCategoryDataset();

		  for(OcanNeedRatingOverTimeSummaryOfNeedsBean bean:summaryOfNeedsNoDomainBeanList) {
			  result.addValue(bean.getConsumerNeedMap().get("unknown"), "Consumer (Unknown)",bean.getOcanName());
			  result.addValue(bean.getConsumerNeedMap().get("no"), "Consumer (No Needs)",bean.getOcanName());
			  result.addValue(bean.getConsumerNeedMap().get("met"), "Consumer (Met Needs)",bean.getOcanName());
			  result.addValue(bean.getConsumerNeedMap().get("unmet"), "Consumer (Unmet Needs)",bean.getOcanName());
			  result.addValue(bean.getStaffNeedMap().get("unknown"), "Staff (Unknown)",bean.getOcanName());
			  result.addValue(bean.getStaffNeedMap().get("no"), "Staff (No Needs)",bean.getOcanName());
			  result.addValue(bean.getStaffNeedMap().get("met"), "Staff (Met Needs)",bean.getOcanName());
			  result.addValue(bean.getStaffNeedMap().get("unmet"), "Staff (Unmet Needs)",bean.getOcanName());
		  }

		  return result;
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


	public List<OcanNeedRatingOverTimeSummaryOfNeedsBean> getSummaryOfNeedsBeanList() {
		return summaryOfNeedsBeanList;
	}

	public void setSummaryOfNeedsBeanList(
			List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsBeanList) {
		this.summaryOfNeedsBeanList = summaryOfNeedsBeanList;
	}

	public List<OcanNeedRatingOverTimeSummaryOfNeedsBean> getSummaryOfNeedsNoDomainBeanList() {
		return summaryOfNeedsNoDomainBeanList;
	}

	public void setSummaryOfNeedsNoDomainBeanList(
			List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsNoDomainBeanList) {
		this.summaryOfNeedsNoDomainBeanList = summaryOfNeedsNoDomainBeanList;
	}

	public List<String> getDomains() {
		return domains;
	}

	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public List<OcanNeedRatingOverTimeNeedBreakdownBean> getNeedBreakdownListByOCAN() {
		return needBreakdownListByOCAN;
	}

	public void setNeedBreakdownListByOCAN(
			List<OcanNeedRatingOverTimeNeedBreakdownBean> needBreakdownListByOCAN) {
		this.needBreakdownListByOCAN = needBreakdownListByOCAN;
	}

	public static void main(String args[]) throws Exception {

		List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsBeanList = new ArrayList<OcanNeedRatingOverTimeSummaryOfNeedsBean>();
		summaryOfNeedsBeanList.add(generateSummaryOfNeedsBean("Current OCAN",new Date(),6,6,3,3,13,13,2,2));
		summaryOfNeedsBeanList.add(generateSummaryOfNeedsBean("Previous OCAN",new Date(),4,3,8,9,9,10,3,2));
		//summaryOfNeedsBeanList.add(generateSummaryOfNeedsBean("Reassessment 1",new Date(),4,3,8,9,9,10,3,2));
		//summaryOfNeedsBeanList.add(generateSummaryOfNeedsBean("Reassessment 2",new Date(),4,3,8,9,9,10,3,2));
		summaryOfNeedsBeanList.add(generateSummaryOfNeedsBean("Initial OCAN",new Date(),3,7,6,3,13,12,2,2));

		List<OcanNeedRatingOverTimeNeedBreakdownBean> needBreakdownList = new ArrayList<OcanNeedRatingOverTimeNeedBreakdownBean>();
		needBreakdownList.add(generateOcanNeedRatingOverTimeNeedBreakdownBean("Current OCAN", new Date(),
				new String[]{"Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Met Need","Met Need","Met Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","Unknown","Unknown"},
				new String[]{"Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Met Need","Unmet Need","Met Need","No Need","Met Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","No Need","Unknown","Unknown"}));

		needBreakdownList.add(generateOcanNeedRatingOverTimeNeedBreakdownBean("Previous OCAN", new Date(),
				new String[]{"Met Need","Unmet Need","Met Need","Met Need","Met Need","Unmet Need","Unmet Need","Unmet Need","Met Need","Met Need","No Need","No Need","No Need","No Need","Met Need","No Need","No Need","No Need","No Need","No Need","Unknown","Met Need","Unknown","Unknown"},
				new String[]{"Met Need","Unmet Need","Met Need","Met Need","Met Need","Met Need","Unmet Need","Unmet Need","Met Need","Met Need","No Need","No Need","No Need","No Need","Met Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"}));

/*
		needBreakdownList.add(generateOcanNeedRatingOverTimeNeedBreakdownBean("Reassessment 1", new Date(),
				new String[]{"Met Need","Unmet Need","No Need","No Need","No Need","Unmet Need","Met Need","Met Need","Met Need","Met Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"},
				new String[]{"Unmet Need","Unmet Need","No Need","Met Need","No Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"}));


		needBreakdownList.add(generateOcanNeedRatingOverTimeNeedBreakdownBean("Reassessment 2", new Date(),
				new String[]{"Met Need","Unmet Need","No Need","No Need","No Need","Unmet Need","Met Need","Met Need","Met Need","Met Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"},
				new String[]{"No Need","Unmet Need","No Need","Met Need","No Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"}));


		needBreakdownList.add(generateOcanNeedRatingOverTimeNeedBreakdownBean("Reassessment 3", new Date(),
				new String[]{"Met Need","Unmet Need","No Need","No Need","No Need","Unmet Need","Met Need","Met Need","Met Need","Met Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"},
				new String[]{"Met Need","Unmet Need","No Need","Met Need","No Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"}));

*/
		needBreakdownList.add(generateOcanNeedRatingOverTimeNeedBreakdownBean("Initial OCAN", new Date(),
				new String[]{"Met Need","Unmet Need","No Need","No Need","No Need","Unmet Need","Met Need","Met Need","Met Need","Met Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"},
				new String[]{"Met Need","Unmet Need","No Need","Met Need","No Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","Unmet Need","No Need","No Need","No Need","No Need","Unmet Need","No Need","No Need","No Need","No Need","No Need","No Need","Met Need","Unknown","Unknown"}));

		List<String> domains =  new ArrayList<String>();
		String[] arrDomains = new String[]{"Physical Health","Alcohol","Company","Money","Benefits",
				"Self-Care","Looking After The Home","Basic Education","Transport","Daytime Activities",
				"Accommmodation","Food","Psychotic Symptoms","Info on Conditions and Treatment",
				"Psychological Distress","Safety to Self","Safety to Others","Drugs","Other Addictions"
				,"Child Care","Other Dependents","Telephone","Intimate Relationships","Sexual Expression"};
		for(int x=0;x<arrDomains.length;x++) {
			domains.add(arrDomains[x]);
		}

		NeedRatingOverTimeReportGenerator report = new NeedRatingOverTimeReportGenerator();

		report.setConsumerName("John Doe");
		report.setStaffName("Marc Dumontier");
		report.setReportDate(new Date());

		report.setSummaryOfNeedsBeanList(summaryOfNeedsBeanList);
		report.setNeedBreakdownListByOCAN(needBreakdownList);
		report.setDomains(domains);

		report.generateReport(new FileOutputStream ("/home/marc/sample.pdf"));
	}

	private static OcanNeedRatingOverTimeNeedBreakdownBean generateOcanNeedRatingOverTimeNeedBreakdownBean(String name, Date date,
			String[] consumerNeed, String[] staffNeed) {
		OcanNeedRatingOverTimeNeedBreakdownBean bean = new OcanNeedRatingOverTimeNeedBreakdownBean();
		bean.setOcanName(name);
		bean.setOcanDate(date);
		for(int x=0;x<consumerNeed.length;x++) {
			bean.getNeeds().add(new OcanConsumerStaffNeedBean(consumerNeed[x], staffNeed[x]));
		}

		return bean;
	}

	private static OcanNeedRatingOverTimeSummaryOfNeedsBean generateSummaryOfNeedsBean(String name, Date date,
			int consumerUnmet, int staffUnmet, int consumerMet, int staffMet, int consumerNo, int staffNo,
			int consumerUnknown, int staffUnknown) {

		OcanNeedRatingOverTimeSummaryOfNeedsBean bean = new OcanNeedRatingOverTimeSummaryOfNeedsBean();
		bean.setOcanName(name);
		bean.setOcanDate(date);
		bean.getConsumerNeedMap().put("unmet", consumerUnmet);
		bean.getConsumerNeedMap().put("met", consumerMet);
		bean.getConsumerNeedMap().put("no", consumerNo);
		bean.getConsumerNeedMap().put("unknown", consumerUnknown);
		bean.getStaffNeedMap().put("unmet", staffUnmet);
		bean.getStaffNeedMap().put("met", staffMet);
		bean.getStaffNeedMap().put("no", staffNo);
		bean.getStaffNeedMap().put("unknown", staffUnknown);
		return bean;
	}

}
