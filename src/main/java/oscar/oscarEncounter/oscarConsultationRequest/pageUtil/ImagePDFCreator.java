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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;

public class ImagePDFCreator extends PdfPageEventHelper {

	private static Logger logger = MiscUtils.getLogger();
	private HttpServletRequest request;
	private OutputStream os;

	private Document document;

	/**
	 * Prepares a ConsultationPDFCreator instance to print a consultation request to PDF.
	 * @param request contains the information necessary to construct the consultation request
	 * @param os the output stream where the PDF will be written
	 */
	public ImagePDFCreator(HttpServletRequest request, OutputStream os) {
		this.request = request;
		this.os = os;
	   
	}

	/**
	 * Prints the consultation request.
	 * @throws IOException when an error with the output stream occurs
	 * @throws DocumentException when an error in document construction occurs
	 */
	public void printPdf() throws IOException, DocumentException {

		Image image;
		try {
			image = Image.getInstance((String)request.getAttribute("imagePath"));
		} catch (Exception e) {
			logger.error("Unexpected error:", e);
			throw new DocumentException(e);
		} 
		
		// Create the document we are going to write to
		document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, os);

		
		document.setPageSize(PageSize.LETTER);
		ResourceBundle.getBundle("oscarResources",request.getLocale())
			.getString("oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgImage");
		document.addCreator("OSCAR");
		document.open();
		
		int type = image.getOriginalType(); 
		if (type == Image.ORIGINAL_TIFF) {
			// The following is composed of code from com.lowagie.tools.plugins.Tiff2Pdf modified to create the 
			// PDF in memory instead of on disk
			RandomAccessFileOrArray ra = new RandomAccessFileOrArray((String)request.getAttribute("imagePath"));
            int comps = TiffImage.getNumberOfPages(ra);
 			boolean adjustSize = false;
 			PdfContentByte cb = writer.getDirectContent();
             for (int c = 0; c < comps; ++c) {            	 
                 Image img = TiffImage.getTiffImage(ra, c + 1);
                 if (img != null) {
                 	if (adjustSize) {
     					document.setPageSize(new Rectangle(img.getScaledWidth(), img.getScaledHeight()));
                         document.newPage();
                 		img.setAbsolutePosition(0, 0);
                 	}
                 	else {
                 		if (img.getScaledWidth() > 500 || img.getScaledHeight() > 700) {
                 			img.scaleToFit(500, 700);
                 		}
                 		img.setAbsolutePosition(20, 20);
                         document.newPage();
                         document.add(new Paragraph((String)request.getAttribute("imageTitle") + " - page " + (c + 1)));
                 	}
                    cb.addImage(img);
                    
                 }
             }
             ra.close();
		}
		else {
			PdfContentByte cb = writer.getDirectContent();
			if (image.getScaledWidth() > 500 || image.getScaledHeight() > 700) {
     			image.scaleToFit(500, 700);
     		}
			image.setAbsolutePosition(20, 20);
			cb.addImage(image);
		}
		document.close();
	}
}
