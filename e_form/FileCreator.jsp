<!--  
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
-->

<%@ page import = "bean.TextFileReader" %> 
<%@ page import = "java.io.File" %> 
<%@ page import="java.io.IOException" %> 
<%@ page import="java.io.PrintWriter" %> 
<%@ page import="java.io.FileOutputStream" %> 
<jsp:useBean id="file_reader" class="bean.TextFileReader" scope="session"/> 
<jsp:useBean id="file_operator" class="bean.TextFileOperator" scope="session"/> 
<jsp:useBean id="beanDBOperator" class="bean.DBOperator" scope="session"/> 
<html> 
<head><title>Read a text file</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</head> 
<%  
//************ creator = "000000 " had to be changed
       
     String form_creator = "000000" ;
     String file_name = request.getParameter("file_name");

// this is the first created file
     String new_file_name = file_name.substring(0,file_name.indexOf("."))+".jsp";

// this is the second created file
     String read_new_file_name = "read_"+file_name.substring(0,file_name.indexOf("."))+".jsp";
 

//******************************************************* root !!!!!!!!
     File file = new File(".");
     //String fileRoot = file.getAbsolutePath()+ "../webapps/oscar_sfhc/e_form/";
      String fileRoot = file.getAbsolutePath();
             fileRoot = fileRoot.replace('\\','/');
             fileRoot = fileRoot.substring(0,fileRoot.length()-6) +  "/webapps/oscar_sfhc/e_form/";;

// read from the .html file     
         String fileName = fileRoot + "upload_html/"+ file_name;
// .jsp file
         String nameOfTextFile = fileRoot + "jsps/"+ new_file_name;
// read_.jsp file
         String nameOfTextFileA = fileRoot +"jsps/"+ read_new_file_name;


// read and write 111 **************
   file_reader.setFileName(fileName);
   if (file_reader.getFileName() != "") { 

        String str = file_reader.getContent();
  
 // write file

	try { 
            PrintWriter pw = new PrintWriter(new FileOutputStream(nameOfTextFile)); 
            pw.println(str); 
          //clean up 
            pw.close(); 
        } catch(IOException e) { 
            out.println(e.getMessage()); 
        } 

   } else {  
         out.println(file_reader.getErrorMessage()); 

   } 
 

// read and write 222 **************

   file_reader.setFileName(fileName);
   if (file_reader.getFileName() != "") { 

        String strA = file_reader.getContent_read_consultation_request();

	try { 
            PrintWriter pwA = new PrintWriter(new FileOutputStream(nameOfTextFileA)); 
            pwA.println(strA); 
          //clean up 
            pwA.close(); 
        } catch(IOException e) { 
            out.println(e.getMessage()); 
        } 
    

   } else {  
         out.println(file_reader.getErrorMessage()); 

   } 

%> 

<body background="images/gray_bg.jpg" >

<p><font size="4">&nbsp;&nbsp; </font></p>
<p>&nbsp;</p>
<p><font size="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
The file &quot;<%=file_name%>&quot; have been uploaded to server and a new<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
file &quot;<%=new_file_name%>&quot; have been created.
 </font></p>
<p><font size="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;
</font> </p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
   <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:window.close()">Close window </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </p>
  <p>&nbsp; </p>
  <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   </p>
 
<p>&nbsp;</p>

</body> 
</html> 
