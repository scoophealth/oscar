/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.util;

import org.apache.log4j.Category;

//import java.text.*;
import java.text.DateFormat;
import java.text.ParseException;

import java.io.FileOutputStream;
import java.io.*;  

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXParseException;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.SAXmyHtmlHandler;

import com.lowagie.text.Chunk;
import com.lowagie.text.Paragraph;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.w3c.tidy.*;
import java.net.*;
import com.lowagie.text.html.HtmlParser;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import com.lowagie.text.pdf.PdfEncodings;
import sun.misc.*;


/**
 *
 * @author root
 */
public class Doc2PDF{
    
      
    public static void parseJSP2PDF( HttpServletRequest request, HttpServletResponse response, String uri, String jsessionid ) {
        

       
        try {
           
            // step 2:
            // we create a writer that listens to the document
            // and directs a XML-stream to a file
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);

          
            BufferedInputStream in = GetInputFromURI( jsessionid, uri);
            ByteArrayOutputStream tidyout = new ByteArrayOutputStream();
            
                        
            tidy.parse(in, tidyout);
            
            String documentTxt = AddAbsoluteTag(request, tidyout.toString(), uri);
                    
            
            PrintPDFFromHTMLString(response, documentTxt);

        }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }

        
    }
    
    


    public static void parseString2PDF( HttpServletRequest request, HttpServletResponse response, String docText ) {
        

        
        try {
             
            // step 2:
            // we create a writer that listens to the document
            // and directs a XML-stream to a file
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
          
            BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(docText.getBytes()));
            ByteArrayOutputStream tidyout = new ByteArrayOutputStream();          
                        
            tidy.parse(in, tidyout);
                                
            
            PrintPDFFromHTMLString(response,  AddAbsoluteTag(request, tidyout.toString(), "") );         

        }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }

        
    }    
    
    public static String parseString2Bin ( HttpServletRequest request, HttpServletResponse response, String docText ) {
        

        
        try {
           
            // step 2:
            // we create a writer that listens to the document
            // and directs a XML-stream to a file
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
          
            BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(docText.getBytes()));
            ByteArrayOutputStream tidyout = new ByteArrayOutputStream();          
                        
            tidy.parse(in, tidyout);
            
            String testFile = GetPDFBin(response,  AddAbsoluteTag(request, tidyout.toString(), "") );
            
            return testFile;
                    

        }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
            return null;
        }

        
    }   
    
    public static void SavePDF2File( String fileName, String docBin ) {
       
        FileOutputStream fos;
        DataOutputStream ds;
        try {

            FileOutputStream ostream = new FileOutputStream(fileName);

            ObjectOutputStream p = new ObjectOutputStream(ostream);

            p.writeBytes(docBin);


            p.flush();
            ostream.close();

        } 
        catch (IOException ioe) {
                   System.out.println( "IO error: " + ioe ); 
        }
    }
    public static BufferedInputStream GetInputFromURI(String jsessionid, String uri ) {
        
        BufferedInputStream in = null;
        try {
            
/*
            URL urltt = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) urltt.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(true);
            connection.connect();

            in = new BufferedInputStream( connection.getInputStream() );          
 */
            
 
            URL url = new URI( uri + ";jsessionid=" + jsessionid ).toURL();
            
            System.out.println( " " +  uri + ";jsessionid=" + jsessionid) ;
            
            HttpURLConnection conn=  (HttpURLConnection) url.openConnection();
            
            in = new BufferedInputStream( conn.getInputStream() );          
            
        }                   
        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }
        return in;
    }
    
    public static String GetPDFBin ( HttpServletResponse response, String docText ) {
        // step 1: creation of a document-object
        Document document = new Document(PageSize.A4, 80, 50, 30, 65);      
        
       
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();      
            PdfWriter.getInstance(document, baos );
            
            // step 3: we create a parser and set the document handler
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            

            // step 4: we parse the document
            // use input stream  
            parser.parse( new ByteArrayInputStream(docText.getBytes()), new SAXmyHtmlHandler(document));                       
            
            document.close();
            
            return new sun.misc.BASE64Encoder().encode(baos.toByteArray());
            
            
        }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }
        return null;
                
    }
    
    public static void PrintPDFFromBin ( HttpServletResponse response, String docBin ) {
       
        // step 1: creation of a document-object
        
        try {

/* 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();      
            
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            // setting the content type
            response.setContentType("application/pdf");
            
            // the content length is needed for MSIE!!!
            response.setContentLength(100000);

            
            // write ByteArrayOutputStream to the ServletOutputStream
            ServletOutputStream out = response.getOutputStream();

            out.println(docBin);
            out.flush();              
*/
  

            byte[] binDecodedArray =  new sun.misc.BASE64Decoder().decodeBuffer(docBin);
            
            PrintPDFFromBytes( response, binDecodedArray  );
            return;

            /*
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            // setting the content type
            response.setContentType("application/pdf");
            
            OutputStream o = response.getOutputStream();
            response.setContentLength( docBin.length() );

            InputStream is = new BufferedInputStream(new ByteArrayInputStream(docBin.getBytes()));

            byte[] buf = new byte[ 32 * 1024]; // 32k buffer

            int nRead = 0;
            while( (nRead=is.read(buf)) != -1 ) {
                o.write(buf, 0, nRead);
                
                
            }
            
            o.flush();            
            o.close();// *important* to ensure no more jsp output
            return; 
            */
                           
        }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }
                
        
    }
    
    public static void PrintPDFFromBytes( HttpServletResponse response, byte[] docBytes ) {
    
        try {
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            // setting the content type
            response.setContentType("application/pdf");
            
            OutputStream o = response.getOutputStream();
            response.setContentLength( docBytes.length );

            InputStream is = new BufferedInputStream(new ByteArrayInputStream(docBytes ) ) ;

            byte[] buf = new byte[ 32 * 1024]; // 32k buffer

            int nRead = 0;
            while( (nRead=is.read(buf)) != -1 ) {
                o.write(buf, 0, nRead);
            }
            
            o.flush();            
            o.close(); // *important* to ensure no more jsp output
            return; 
       }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }
                          
    }
    
    public static void PrintPDFFromHTMLString ( HttpServletResponse response, String docText ) {
 
        // step 1: creation of a document-object
        Document document = new Document(PageSize.A4, 80, 50, 30, 65);      
        
       
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();      
            PdfWriter.getInstance(document, baos );
            
            // step 3: we create a parser and set the document handler
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            

            // step 4: we parse the document
            // use input stream        
            parser.parse( new ByteArrayInputStream(docText.getBytes()), new SAXmyHtmlHandler(document));                       
            
                   
            document.close();
            
            // String yourString = new String(theBytesOfYourString, "UTF-8");
            // byte[] theBytesOfYourString = yourString.getBytes("UTF-8");
            
            byte[] binArray = baos.toByteArray();
            
            
            PrintPDFFromBytes( response, binArray  );
            
            
            
          
            /*
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            // setting the content type
            response.setContentType("application/pdf");
            
            // the content length is needed for MSIE!!!
            response.setContentLength(baos.size());

            
            // write ByteArrayOutputStream to the ServletOutputStream
            ServletOutputStream out = response.getOutputStream();
                                    
            baos.writeTo(out);
            out.flush();  
 
            */
        }

        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage()  );
        }
        
    }
    
    public static String AddAbsoluteTag( HttpServletRequest request, String docText, String uri ) {

        String absolutePath = "";
        
        docText  = docText.replaceAll( "src='/", "src='"  );        
        docText  = docText.replaceAll( "src=\"/", "src=\"" );
        docText  = docText.replaceAll( "src=/", "src=" );
              
        if ( request.getProtocol().toString().equals("HTTP/1.1") ) {
            absolutePath = "http://";
        }
        else {
            absolutePath = "https://";
        }

        absolutePath += request.getRemoteHost() + ":" + request.getServerPort() + "" + request.getContextPath() + "/";
                
        docText  = docText.replaceAll( "src='", "src='" + absolutePath );
        docText  = docText.replaceAll( "src=\"", "src=\"" + absolutePath );        
        
        
        return docText;
    }
    

    public static Vector getXMLTagValue(String xml, String section) throws Exception
    {
        String xmlString = xml;
        
        Vector v = new Vector();
        String beginTagToSearch = "<" + section + ">";
        String endTagToSearch = "</" + section + ">";

        // Look for the first occurrence of begin tag
        int index = xmlString.indexOf(beginTagToSearch);


        while(index != -1)
        {
                // Look for end tag
                // DOES NOT HANDLE <section Blah />
                int lastIndex = xmlString.indexOf(endTagToSearch);

                // Make sure there is no error
                if((lastIndex == -1) || (lastIndex < index))
                        throw new Exception("Parse Error");

                // extract the substring
                String subs = xmlString.substring((index + beginTagToSearch.length()), lastIndex) ;

                // Add it to our list of tag values
                v.addElement(subs);

                 // Try it again. Narrow down to the part of string which is not 
                 // processed yet.
                try
                {
                        xmlString = xmlString.substring(lastIndex + endTagToSearch.length());
                }
                catch(Exception e)
                {
                        xmlString = "";
                }

                 // Start over again by searching the first occurrence of the begin tag 
                 // to continue the loop.

                index = xmlString.indexOf(beginTagToSearch);
        }		
        
        return v;
    }
        
}
