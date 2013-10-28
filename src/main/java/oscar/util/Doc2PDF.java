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


package oscar.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.w3c.tidy.Tidy;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 *
 * @author root
 */
public class Doc2PDF {
    private static Logger logger=MiscUtils.getLogger(); 

    public static void parseJSP2PDF(HttpServletRequest request, HttpServletResponse response, String uri, String jsessionid) {

        try {

            // step 2:
            // we create a writer that listens to the document
            // and directs a XML-stream to a file
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);

            BufferedInputStream in = GetInputFromURI(jsessionid, uri);
            ByteArrayOutputStream tidyout = new ByteArrayOutputStream();

            tidy.parse(in, tidyout);

            MiscUtils.getLogger().debug(tidyout.toString());
            String documentTxt = AddAbsoluteTag(request, tidyout.toString(), uri);

            PrintPDFFromHTMLString(response, documentTxt);

        }

        catch (Exception e) {
            logger.error("", e);
        }

    }

    // Convert named file to PDF on stdout...
    public static int topdf(HttpServletRequest request, HttpServletResponse response, String filename)// I - Name of file to convert
    {
        String command; // Command string
        Process process; // Process for HTMLDOC
        Runtime runtime; // Local runtime object
        java.io.InputStream input; // Output from HTMLDOC
        int bytes; // Number of bytes

        // Construct the command string
        command = "htmldoc --quiet --webpage -t pdf " + filename;

        try {

            // Run the process and wait for it to complete...
            runtime = Runtime.getRuntime();

            // Create a new HTMLDOC process...
            process = runtime.exec(command);

            // Get stdout from the process and a buffer for the data...
            input = process.getInputStream();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // Compress the data
            byte[] buf = new byte[1024];

            // Read output from HTMLDOC until we have it all...
            while ((bytes = input.read(buf)) > 0)
                bos.write(buf, 0, bytes);

            PrintPDFFromBytes(response, bos.toByteArray());

            // Return the exit status from HTMLDOC...
            return(process.waitFor());
        }
        catch (Exception e) {
            // An error occurred - send it to stderr for the web server...
            logger.error(e.toString() + " caught while running:\n\n");
            logger.error("    " + command + "\n");
            logger.error("", e);
            return(1);
        }
    }

    // Main entry for htmldoc class
    public static void HTMLDOC(HttpServletRequest request, HttpServletResponse response, String url)// I - Command-line args
    {
        //String server_name, // SERVER_NAME env var
        //server_port, // SERVER_PORT env var
        //path_info, // PATH_INFO env var
        String query_string, // QUERY_STRING env var
        filename; // File to convert

        filename = url;

        if ((query_string = System.getProperty("QUERY_STRING")) != null) {
            filename = filename + "?" + query_string;
        }

        // Convert the file to PDF and send to the web client...
        topdf(request, response, filename);

        return;
    }

    public static void parseString2PDF(HttpServletRequest request, HttpServletResponse response, String docText) {

        try {

            // step 2:
            // we create a writer that listens to the document
            // and directs a XML-stream to a file
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);

            BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(docText.getBytes()));
            ByteArrayOutputStream tidyout = new ByteArrayOutputStream();

            tidy.parse(in, tidyout);

            PrintPDFFromHTMLString(response, AddAbsoluteTag(request, tidyout.toString(), ""));

        }

        catch (Exception e) {
        	logger.error("Unexpected error", e);
        }

    }

    public static String parseString2Bin(HttpServletRequest request, HttpServletResponse response, String docText) {

        try {

            // step 2:
            // we create a writer that listens to the document
            // and directs a XML-stream to a file
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);

            BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(docText.getBytes()));
            ByteArrayOutputStream tidyout = new ByteArrayOutputStream();

            tidy.parse(in, tidyout);

            String testFile = GetPDFBin(response, AddAbsoluteTag(request, tidyout.toString(), ""));

            return testFile;

        }

        catch (Exception e) {
        	logger.error("Unexpected error", e);
            return null;
        }

    }

    public static void SavePDF2File(String fileName, String docBin) {

        try {

            FileOutputStream ostream = new FileOutputStream(fileName);

            ObjectOutputStream p = new ObjectOutputStream(ostream);

            p.writeBytes(docBin);

            p.flush();
            ostream.close();

        }
        catch (IOException ioe) {
            MiscUtils.getLogger().debug("IO error: " + ioe);
        }
    }

    public static BufferedInputStream GetInputFromURI(String jsessionid, String uri) {

        BufferedInputStream in = null;
        try {

            URL url = new URI(uri + ";jsessionid=" + jsessionid).toURL();

            MiscUtils.getLogger().debug(" " + uri + ";jsessionid=" + jsessionid);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            in = new BufferedInputStream(conn.getInputStream());

        }
        catch (Exception e) {
        	logger.error("Unexpected error", e);
        }
        return in;
    }

    public static String GetPDFBin(HttpServletResponse response, String docText) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            InputStream is = new ByteArrayInputStream(docText.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            return(new String(Base64.encodeBase64(baos.toByteArray())));
        }
        catch (Exception e) {
        	logger.error("Unexpected error", e);
        }
        return null;

    }
    public static void PrintPDFFromBin(HttpServletResponse response, String docBin) {

        // step 1: creation of a document-object

        try {

            byte[] binDecodedArray = Base64.decodeBase64(docBin.getBytes());

            PrintPDFFromBytes(response, binDecodedArray);
            return;

        }

        catch (Exception e) {
        	logger.error("Unexpected error", e);
        }

    }

    public static void PrintPDFFromBytes(HttpServletResponse response, byte[] docBytes) {

        try {
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            // setting the content type
            response.setContentType("application/pdf");

            OutputStream o = response.getOutputStream();
            response.setContentLength(docBytes.length);

            InputStream is = new BufferedInputStream(new ByteArrayInputStream(docBytes));

            byte[] buf = new byte[32 * 1024]; // 32k buffer

            int nRead = 0;
            while ((nRead = is.read(buf)) != -1) {
                o.write(buf, 0, nRead);
            }

            o.flush();
            o.close(); // *important* to ensure no more jsp output
            return;
        }

        catch (Exception e) {
        	logger.error("Unexpected error", e);
        }

    }

    public static void PrintPDFFromHTMLString(HttpServletResponse response, String docText) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            InputStream is = new ByteArrayInputStream(docText.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            byte[] binArray = baos.toByteArray();
            PrintPDFFromBytes(response, binArray);
        }
        catch (Exception e) {
        	logger.error("Unexpected error", e);
        }
    }

    public static String AddAbsoluteTag(HttpServletRequest request, String docText, String uri) {

        String absolutePath = "";

        docText = docText.replaceAll("src='/", "src='");
        docText = docText.replaceAll("src=\"/", "src=\"");
        docText = docText.replaceAll("src=/", "src=");

        if (request.getProtocol().toString().equals("HTTP/1.1")) {
            absolutePath = "http://";
        }
        else {
            absolutePath = "https://";
        }

        absolutePath += request.getRemoteHost() + ":" + request.getServerPort() + "" + request.getContextPath() + "/";

        docText = docText.replaceAll("src='", "src='" + absolutePath);
        docText = docText.replaceAll("src=\"", "src=\"" + absolutePath);

        return docText;
    }

    public static Vector getXMLTagValue(String xml, String section) throws Exception {
        String xmlString = xml;

        Vector v = new Vector();
        String beginTagToSearch = "<" + section + ">";
        String endTagToSearch = "</" + section + ">";

        // Look for the first occurrence of begin tag
        int index = xmlString.indexOf(beginTagToSearch);

        while (index != -1) {
            // Look for end tag
            // DOES NOT HANDLE <section Blah />
            int lastIndex = xmlString.indexOf(endTagToSearch);

            // Make sure there is no error
            if ((lastIndex == -1) || (lastIndex < index)) throw new Exception("Parse Error");

            // extract the substring
            String subs = xmlString.substring((index + beginTagToSearch.length()), lastIndex);

            // Add it to our list of tag values
            v.addElement(subs);

            // Try it again. Narrow down to the part of string which is not 
            // processed yet.
            try {
                xmlString = xmlString.substring(lastIndex + endTagToSearch.length());
            }
            catch (Exception e) {
                xmlString = "";
            }

            // Start over again by searching the first occurrence of the begin tag 
            // to continue the loop.

            index = xmlString.indexOf(beginTagToSearch);
        }

        return v;
    }

}
