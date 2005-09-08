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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
/*
 * 
 */


package oscar.eform.upload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;

import oscar.OscarProperties;

public class UploadImage extends HttpServlet{
    final static int BUFFER = 2048;
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
        int c;
        int count;
        byte data[] = new byte[BUFFER];
        byte data1[] = new byte[BUFFER/2];
        byte data2[] = new byte[BUFFER/2];
        byte enddata[] = new byte[2];
        
        
        HttpSession session = request.getSession(true);
        String backupfilepath = ((String) session.getAttribute("homepath"))!=null?((String) session.getAttribute("homepath")):"null" ;
        
        count=request.getContentType().indexOf('=');
        String temp = request.getContentType().substring(count+1);
        String filename = "", foldername="", fileheader="", forwardTo="", function="", function_id="", filedesc="", creator="", doctype="", docxml="";
        String home_dir="", doc_forward="";
        String userHomePath = System.getProperty("user.home", "user.dir");
        
        Properties ap = OscarProperties.getInstance();
        foldername = ap.getProperty("eform_image");        

        boolean isMultipart = FileUpload.isMultipartContent(request);
        //		 Create a new file upload handler
        DiskFileUpload upload = new DiskFileUpload();

        try {
            //		 Parse the request
            List items = upload.parseRequest(request);
            //Process the uploaded items
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    //String name = item.getFieldName();
                    //String value = item.getString(); 
                    //System.out.println("Fieldname: " + item.getFieldName());
                } else {
                    String pathName = item.getName();  
                    String [] fullFile = pathName.split("[/|\\\\]");
            		File savedFile = new File(foldername, fullFile[fullFile.length-1]);
                    //System.out.println(item.getName() + "fullFile: " + fullFile[fullFile.length-1]);
                    fileheader = fullFile[fullFile.length-1];
                    System.out.println(fileheader + "uploaded to \n" +
                                      foldername);
                    item.write(savedFile);
                }
            }
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
        
        // Call the output page.
        PrintWriter out = response.getWriter();
        out.println("<head>");
	out.println("<script language=\"JavaScript\">");
	out.println("setTimeout(\"top.location.href = '../eform/uploadimages.jsp'\",1000);");
	out.println("</script>");
	out.println("</head>");
	out.println("<body>");
	out.println("File upload successfully.<br> Please wait for 1 seconds to go to \"modify\" page"); 
	out.println("</body>");
    }
    
}