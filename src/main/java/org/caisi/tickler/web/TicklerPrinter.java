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
package org.caisi.tickler.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.oscarehr.common.model.Tickler;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;

import oscar.OscarProperties;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class TicklerPrinter {

	private OutputStream os;

	private Document document;
	private BaseFont bf;
	private Font font, boldFont;
	private boolean newPage = false;

	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

	public final int LINESPACING = 1;
	public final float LEADING = 12;
	public final float FONTSIZE = 10;
	public final int NUMCOLS = 2;

	private PdfWriter writer;

	private Tickler tickler;

	public TicklerPrinter(Tickler tickler, OutputStream os) {
		this.os = os;
		this.tickler = tickler;

		document = null;
		writer = null;
		bf = null;
		font = null;
		boldFont = null;
	}

	public void start() throws DocumentException, IOException {
		//Create the font we are going to print to
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		font = new Font(bf, FONTSIZE, Font.NORMAL);
		boldFont = new Font(bf, FONTSIZE, Font.BOLD);

		document = new Document();
		writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);
		writer.setStrictImageSequence(true);

		document.setPageSize(PageSize.LETTER);
		document.open();
	}

	public Font getFont() {
		return font;
	}

	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public Document getDocument() {
		return document;
	}

	public boolean getNewPage() {
		return newPage;
	}

	public void setNewPage(boolean b) {
		this.newPage = b;
	}

	public BaseFont getBaseFont() {
		return bf;
	}

	protected Paragraph getParagraph(String value) {
		Paragraph p = new Paragraph(value, font);
		return p;
	}

	public void footer() {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();

		Date now = new Date();
		String promoTxt = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
		if (promoTxt == null) {
			promoTxt = new String();
		}

		String strFooter = promoTxt + " " + formatter.format(now);

		float textBase = document.bottom();
		cb.beginText();
		cb.setFontAndSize(font.getBaseFont(), FONTSIZE);
		Rectangle page = document.getPageSize();
		float width = page.getWidth();

		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, strFooter, (width / 2.0f), textBase - 20, 0);

		strFooter = "-" + writer.getPageNumber() + "-";
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, strFooter, (width / 2.0f), textBase - 10, 0);

		cb.endText();
		cb.restoreState();
	}

	public void printDocHeaderFooter() throws DocumentException {
		document.resetHeader();
		document.resetFooter();

		String headerTitle = "Tickler re: " + tickler.getDemographic().getFormattedName() + " DOB:" + tickler.getDemographic().getFormattedDob();

		if (newPage) {
			document.newPage();
			newPage = false;
		}

		//Header will be printed at top of every page beginning with p2
		Phrase headerPhrase = new Phrase(LEADING, headerTitle, boldFont);

		getDocument().add(headerPhrase);
		getDocument().add(new Phrase("\n"));
	}

	private void addStandardTableEntry(PdfPTable table, String name, String value) {
		PdfPCell cell1 = new PdfPCell(getParagraph(name + ":"));

		cell1.setBorder(PdfPCell.BOTTOM);
		cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		PdfPCell cell2 = new PdfPCell(getParagraph(value));
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setBorder(PdfPCell.BOTTOM);

		table.addCell(cell1);
		table.addCell(cell2);
	}

	public void printTicklerInfo() throws DocumentException {

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(50f);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		addStandardTableEntry(table, "Created By", tickler.getProvider().getFormattedName());
		addStandardTableEntry(table, "Last Updated", formatter.format(tickler.getUpdateDate()));
		addStandardTableEntry(table, "Service Date", formatter.format(tickler.getServiceDate()));
		addStandardTableEntry(table, "Assigned To", tickler.getAssignee().getFormattedName());
		addStandardTableEntry(table, "Priority", tickler.getPriority().toString());
		addStandardTableEntry(table, "Status", tickler.getStatusWeb());
		addStandardTableEntry(table, "Program", (tickler.getProgram() != null) ? tickler.getProgram().getName() : "N/A");

		getDocument().add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(70f);

		PdfPCell cell1 = new PdfPCell(getParagraph("Message"));

		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setHorizontalAlignment(Element.ALIGN_LEFT);

		table.addCell(cell1);

		cell1 = new PdfPCell(getParagraph(tickler.getMessage()));
		cell1.setHorizontalAlignment(Element.ALIGN_LEFT);

		table.addCell(cell1);

		getDocument().add(table);

	}

	public void printBlankLine() throws DocumentException {
		document.add(new Phrase("\n"));
	}

	public void finish() {
		document.close();
	}

	class EndPage extends PdfPageEventHelper {
		private Date now;
		private String promoTxt;

		public EndPage() {
			now = new Date();
			promoTxt = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
			if (promoTxt == null) {
				promoTxt = new String();
			}
		}

		public void onEndPage(PdfWriter writer, Document document) {
			//Footer contains page numbers and date printed on all pages
			PdfContentByte cb = writer.getDirectContent();
			cb.saveState();

			String strFooter = promoTxt + " " + formatter.format(now);

			float textBase = document.bottom();
			cb.beginText();
			cb.setFontAndSize(font.getBaseFont(), FONTSIZE);
			Rectangle page = document.getPageSize();
			float width = page.getWidth();
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, strFooter, (width / 2.0f), textBase - 20, 0);

			strFooter = "-" + writer.getPageNumber() + "-";
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, strFooter, (width / 2.0f), textBase - 10, 0);

			cb.endText();
			cb.restoreState();
		}
	}

}
