/*
 * 
 */
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


package oscar.eform;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

/**
 * Class UploadServlet is a servlet to upload the text/html file to the database
 * 2002-12-30
*/
public class UploadServlet extends HttpServlet{
  static final int MAX_SIZE = 102400;
  String fileString="" ;
  String form_name="" ;
  String subject="" ;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public void doPost(HttpServletRequest request,HttpServletResponse response){
    HttpSession session = request.getSession(false);
    if (session == null) return;
    if (session.getAttribute("user") == null || !((String) session.getAttribute("userprofession")).equalsIgnoreCase("admin") ) return;

    ServletOutputStream out=null;
    DataInputStream in=null;
    FileOutputStream fileOut=null;

    try   {
      response.setContentType("text/html");
      out = response.getOutputStream();
    }  catch (IOException e)  {
      System.out.println("Error getting output stream.");
      System.out.println("Error description: " + e);
      return;
    }

    try  {
      String contentType = request.getContentType();
      if(contentType != null && contentType.indexOf("multipart/form-data") != -1)      {
        in = new DataInputStream(request.getInputStream());
        int formDataLength = request.getContentLength();

        byte dataBytes[] = new byte[formDataLength];
        //read file into byte array
        int bytesRead = 0;
        int totalBytesRead = 0;
        int sizeCheck = 0;
        while (totalBytesRead < formDataLength) {
          sizeCheck = totalBytesRead + in.available();
          if (sizeCheck > MAX_SIZE) {
            out.println("Sorry, file is too large to upload.");
            return;
          }
          bytesRead = in.read(dataBytes, totalBytesRead, formDataLength);
          totalBytesRead += bytesRead;
        }

        String file = new String(dataBytes);
        //release memory
        dataBytes = null;
        int lastIndex = contentType.lastIndexOf("=");
        String boundary = contentType.substring(lastIndex+1, contentType.length());

        // get form_name
        if (file.indexOf("name=\"form_name\"") > 0) {
          form_name = file.substring(file.indexOf("name=\"form_name\"") + "name=\"form_name\"".length());
          form_name = form_name.substring(form_name.indexOf("\n")+1);
          form_name = form_name.substring(form_name.indexOf("\n")+1);
          form_name = form_name.substring(0,form_name.indexOf("\n"));
        }

        // get subject
        if (file.indexOf("name=\"subject\"") > 0){
          subject = file.substring(file.indexOf("name=\"subject\"") + "name=\"subject\"".length());
          subject = subject.substring(subject.indexOf("\n")+1);
          subject = subject.substring(subject.indexOf("\n")+1);
          subject = subject.substring(0,subject.indexOf("\n"));
        }
        //************** saveFile  name
        String saveFile = file.substring(file.indexOf("filename=\"") + "filename=\"".length());
        saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
        saveFile = saveFile.substring(saveFile.lastIndexOf("\\")+1, saveFile.indexOf("\"")); //only for windows path

        /*remove boundary markers and other multipart/form-data tags from beginning of upload file section*/
        int pos; //position in upload file
        //find position of upload file section of request
        pos = file.indexOf("filename=\"");
        //find position of content-disposition line
        pos = file.indexOf("\n",pos)+1;
        //find position of content-type line
        pos = file.indexOf("\n",pos)+1;
        //find position of blank line
        pos = file.indexOf("\n",pos)+1;
        /*find the location of the next boundary marker (marking the end of the upload file data)*/
        int boundaryLocation = file.indexOf(boundary,pos)-4;
        //upload file lies between pos and boundaryLocation

        // **********  this is the file String
        fileString  = file.substring(pos,boundaryLocation);
        form_name=form_name.substring(0,form_name.length()-1);
 
        try { 
          (new oscar.eform.EfmDataOpt()).save_eform(form_name,saveFile,subject,fileString);
        } catch(Exception e){}

        fileString = ""; 

        // send back message to user
	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " + "Transitional//EN\">");
        out.println("<head>");
        out.println("<script language=\"JavaScript\">");
        out.println("setTimeout(\"top.location.href = '../eform/uploadhtml.jsp'\",500);");
	      out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.println("File upload successfully.<br> Please wait for 1 seconds ."); 
        out.println("</body>");
 
      } //end if
    } catch(Exception e)    {
      try {
        //print error message to standard out
        System.out.println("Error in doPost: " + e);
        //send error message to client
        out.println("An unexpected error has occurred.");
        out.println("Error description: " + e);
      } catch (Exception f) {}
    }
  }
}

