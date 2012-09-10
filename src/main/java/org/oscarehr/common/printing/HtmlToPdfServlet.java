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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

import oscar.OscarProperties;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class HtmlToPdfServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("content");

		// convert the content provide into PDF
		byte[] pdf = convertToPdf(req, content);
		// make sure we show footer information
		pdf = appendFooter(pdf);
		// and finally stream it to the user
		stream(resp, pdf, true);
	}

	public static byte[] stamp(byte[] content) throws Exception {
		PdfReader reader = null;
		ByteArrayOutputStream os = null;
		PdfStamper stamper = null;

		try {
			reader = new PdfReader(content);
			os = new ByteArrayOutputStream(content.length);
			stamper = new PdfStamper(reader, os);

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PdfContentByte over = stamper.getOverContent(i);
				BaseFont font = FontSettings.HELVETICA_6PT.createFont();

				over.setFontAndSize(font, 10);
				float center = reader.getPageSize(i).getWidth() / 2.0f;
				
				// TODO Consider refactoring this into is own class 
				printText(over, "Page " + i + " of " + reader.getNumberOfPages(), center, 35);
				printText(over, OscarProperties.getConfidentialityStatement(), center, 25);
			}
			
			// this is ugly, but there is no flush method for readers in itext....
			stamper.close();
			reader.close();
			os.flush();
			
			return os.toByteArray();
		} finally {
			if (os != null) IOUtils.closeQuietly(os);
		}
	}

	private static void printText(PdfContentByte over, String text, float x, float y) {
		over.beginText();
		over.showTextAligned(PdfContentByte.ALIGN_CENTER, text, x, y, 0);
		over.endText();
	}

	/**
	 * Streams the specified content as the response
	 * 
	 * @param resp
	 * 		Response to stream PDF bytes to
	 * @param pdf
	 * 		PDF bytes to be streamed
	 * @param setHeaders
	 * 		Flag indicating if the Content-Disposition header and content type should be set 
	 * @throws IOException
	 */
	public static void stream(HttpServletResponse resp, byte[] pdf, boolean setHeaders) throws IOException {
		resp.setContentLength(pdf.length);
		if (setHeaders) {
			resp.setContentType("application/pdf");
			resp.setHeader("Content-Disposition", "attachment; filename=printout.pdf");
		}

		OutputStream ros = null;
		try {
			ros = resp.getOutputStream();
			ros.write(pdf);
			ros.flush();
		} finally {
			if (ros != null) {
				IOUtils.closeQuietly(ros);
			}
		}
	}

	/**
	 * Appends footer to the provided PDF
	 * 
	 * @param pdf
	 * 		PDF to append footer to
	 * @return
	 * 		Returns the newly generated PDF with the footer appended
	 * @throws IOException
	 * 	IOException is thrown in case PDF can not be changed 
	 */
	public static byte[] appendFooter(byte[] pdf) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(pdf.length); // assume new content is shorter
		try {
			appendFooter(pdf, baos);

			baos.flush();
			pdf = baos.toByteArray();
		} finally {
			if (baos != null) baos.close();
		}
		return pdf;
	}

	private static void appendFooter(byte[] pdf, ByteArrayOutputStream baos) {
		PdfReader reader = null;
		PdfWriter writer = null;
		Document document = null;

		try {
			// do initialization
			reader = new PdfReader(pdf);
			document = new Document(PageSize.LETTER);
			writer = PdfWriterFactory.newInstance(document, baos, FontSettings.HELVETICA_6PT);
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			// copy pages
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				cb.addTemplate(writer.getImportedPage(reader, i), 0, 0);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to append document footer", e);
		} finally {
			close(document);
			close(writer);
			close(reader);
		}
	}

	private static void close(PdfReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unable to close reader", e);
			}
		}
	}

	private static void close(PdfWriter writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unable to close writer", e);
			}
		}
	}

	private static void close(Document document) {
		if (document != null && document.isOpen()) {
			try {
				document.close();
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unable to close document", e);
			}
		}
	}

	private byte[] convertToPdf(HttpServletRequest req, String content) throws IOException, FileNotFoundException {
		File contentFile = File.createTempFile("pdfservlet.", ".html");
		contentFile.deleteOnExit();

		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(contentFile));
			IOUtils.write(content, os, req.getCharacterEncoding());
		} finally {
			if (os != null) IOUtils.closeQuietly(os);
		}

		byte[] pdf = WKHtmlToPdfUtils.convertToPdf(contentFile.getAbsolutePath());
		return pdf;
	}

}
