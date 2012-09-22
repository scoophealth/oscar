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
package org.oscarehr.casemgmt.service;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class PromoTextStamper extends FooterSupport {

	private String text;

	public PromoTextStamper(String promoText, int offset) {
		setBaseOffset(offset);
		this.text = promoText;
	}

	/**
	 * Adds promo text, date and current page number to each page
	 * 
	 * @param writer
	 * @param document
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();

		float textBase = document.bottom() - getBaseOffset();
		float width = document.getPageSize().getWidth();
		float center = width / 2.0f;

		cb.beginText();
		cb.setFontAndSize(getFont(), getFontSize());

		cb.setTextMatrix(document.left(), textBase);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, center, textBase, 0);
		cb.endText();
		cb.restoreState();
	}

}
