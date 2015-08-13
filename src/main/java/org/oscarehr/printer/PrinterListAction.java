/**
 * Copyright (c) 2012- Centre de Medecine Integree
 *
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
 * This software was written for
 * Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
 * as part of the OSCAR McMaster EMR System
 */
package org.oscarehr.printer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.struts.actions.DispatchAction;

public class PrinterListAction extends DispatchAction {

    public ActionForward generatePrinterListInPDF(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = null;
        Document document = null;
        PdfWriter writer = null;
        OutputStream os = null;
        
        try {
            document = new Document();
            baos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph(" "));

            String javascript
                    = "this.disclosed = true;"
                    + "if (this.external && this.hostContainer) {"
                    + "   try{"
                    + "      this.hostContainer.postMessage(app.printerNames);"
                    + "   }"
                    + "   catch(e){"
                    + "      app.alert(e.message);"
                    + "   }"
                    + "}";

            PdfAction action
                    = PdfAction.javaScript(javascript, writer);

            writer.setOpenAction(action);

            document.close();

            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");

            response.setContentLength(baos.size());
            os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        } finally {
            if(os!=null) { os.close(); }
            if(document!=null) { document.close(); }
            if(writer!=null) { writer.close(); }
            if(baos!=null) { baos.close(); }
      }  
        return null;
    }

}
