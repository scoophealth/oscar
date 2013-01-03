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
package org.oscarehr.common.printing;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.oscarehr.casemgmt.service.PageNumberStamper;
import org.oscarehr.casemgmt.service.PromoTextStamper;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class PdfWriterFactory {

	public static PdfContentByte setFont(PdfContentByte pdfContentByte, FontSettings settings) {
		pdfContentByte.setFontAndSize(settings.createFont(), settings.getFontSize());
		return pdfContentByte;
	}
	
	/**
	 * Creates a new instance of the PDF writer.
	 * 
	 * @param document
	 * @param stream
	 * @param settings
	 * @return PdfWriter
	 */
	public static PdfWriter newInstance(Document document, OutputStream stream, FontSettings settings) {
		PdfWriter result;
		try {
			result = PdfWriter.getInstance(document, stream);
		} catch (DocumentException e) {
			MiscUtils.getLogger().error("Unable to create new PdfWriter instance", e);
			return null;
		}

		String confidentialtyStatement = OscarProperties.getConfidentialityStatement();
		PromoTextStamper pts = new PromoTextStamper(confidentialtyStatement, 30);
		pts.setFontSize(settings.getFontSize());
		result.setPageEvent(pts);

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String promoText = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") + " " + f.format(new Date());
		pts = new PromoTextStamper(promoText, 20);
		pts.setFontSize(settings.getFontSize());
		result.setPageEvent(pts);

		PageNumberStamper pns = new PageNumberStamper(10);
		pns.setFontSize(settings.getFontSize());
		result.setPageEvent(pns);

		return result;
	}

}
