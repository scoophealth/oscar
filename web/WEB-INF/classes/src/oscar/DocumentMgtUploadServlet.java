// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.GregorianCalendar;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.zip.*;

public class DocumentMgtUploadServlet extends HttpServlet{
  final static int BUFFER = 2048;
  public java.util.Date today;
  public String output;
  public SimpleDateFormat formatter;


  public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
	  int c;
    int count;
    byte data[] = new byte[BUFFER];
    byte data1[] = new byte[BUFFER/2];
    byte data2[] = new byte[BUFFER/2];
    byte enddata[] = new byte[2];


 HttpSession session = request.getSession(true);
     //String backupfilepath = ((String) session.getAttribute("homepath"))!=null?((String) session.getAttribute("homepath")):"null" ;
  formatter = new SimpleDateFormat("yyyyMMddHmmss");
  today = new java.util.Date();
  output = formatter.format(today);

    count=request.getContentType().indexOf('=');
    String temp = request.getContentType().substring(count+1);
    String filename = "test.txt", fileoldname="", foldername="", fileheader="", forwardTo="", function="", function_id="", filedesc="", creator="", doctype="", docxml="";
  String home_dir="", doc_forward="";
 String userHomePath = System.getProperty("user.home", "user.dir");
  //    System.out.println(userHomePath);
      //File pFile = new File(userHomePath, backupfilepath+".properties");
      //FileInputStream pStream = new FileInputStream(pFile.getPath());

      Properties ap = new Properties();
      //ap.load(pStream);
      //Main configuration file. This file must be saved on WEB-INF at the webapp diretory.
      //The file name is defined on the page an its read as a parameter (propName).
      String pathSeparator = System.getProperty("file.separator");
      String mainConfigFileName = getServletContext().getRealPath("")+pathSeparator+"WEB-INF"+pathSeparator+"oscar_mcmaster.properties";
      ap.load(new FileInputStream(new File(mainConfigFileName)));
      
      forwardTo  = ap.getProperty("DOC_FORWARD");
      foldername = ap.getProperty("DOCUMENT_DIR");
      //pStream.close();

   // function = request.getParameter("function");
   // function_id = request.getParameter("functionid");
   // filedesc = request.getParameter("filedesc");
   // creator = request.getParameter("creator");

   ServletInputStream sis = request.getInputStream();
    BufferedOutputStream dest = null;
    FileOutputStream fos = null;
    boolean bwri = false;
    boolean bfbo = true;
    boolean benddata = false;
    boolean bf = false;
    byte boundary[] = temp.getBytes();

    while (bf?true:((count = sis.readLine(data, 0, BUFFER)) != -1)) {
      bf = false;
     	benddata = false;
     	if(count==2 && data[0]==13 && data[1]==10) {
     		enddata[0] = 13;
     		enddata[1] = 10;
     		for(int i=0;i<BUFFER;i++) data[i]=0;

     		count = sis.readLine(data, 0, BUFFER);
     	  if(count==2 && data[0]==13 && data[1]==10) {
    		  dest.write(enddata, 0, 2);
          bf = true;
          continue;
        } else {
          benddata = true;
        }
      }
     	String s = new String(data,2,temp.length());
     	if(temp.equals(s)) {
    		if(benddata) break;
     		if((c =sis.readLine(data1, 0, BUFFER)) != -1) {
     			filename = new String(data1);
      		if(filename.length()>2 && filename.indexOf("filename")!=-1) {
     			  filename = filename.substring(filename.lastIndexOf('\\')+1,filename.lastIndexOf('\"'));
            //System.out.println("filename: "+filename);
        fileheader = output +  filename;
            fos = new FileOutputStream(foldername+ output + filename);
            dest = new BufferedOutputStream(fos, BUFFER);
          }
       		c =sis.readLine(data2, 0, BUFFER);
     		  if((c =sis.readLine(data2, 0, BUFFER)) != -1) {
       		  bwri = bfbo?true:false;
          }
     		}
     		bfbo = bfbo?false:true;
     		for(int i=0;i<BUFFER;i++) data[i]=0;
     		continue;
     	} //end period

   		if(benddata) {
   			benddata = false;
   			dest.write(enddata, 0, 2);
   		  for(int i=0;i<2;i++) enddata[i]=0;
   		}
      if(bwri) {
       	dest.write(data, 0, count);
     		for(int i=0;i<BUFFER;i++) data[i]=0;
      }
    } //end while
    //dest.flush();
    dest.close();
    sis.close();


       DocumentBean documentBean = new DocumentBean();

         request.setAttribute("documentBean", documentBean);


        documentBean.setFilename(fileheader);

       //  documentBean.setFileDesc(filedesc);

       //  documentBean.setFoldername(foldername);

       //  documentBean.setFunction(function);

       //  documentBean.setFunctionID(function_id);

       //  documentBean.setCreateDate(fileheader);

       //  documentBean.setDocCreator(creator);





    // Call the output page.

		 RequestDispatcher dispatch = getServletContext().getRequestDispatcher(forwardTo);
		 dispatch.forward(request, response);
  }

}
