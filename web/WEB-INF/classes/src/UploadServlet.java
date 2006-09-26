//// -----------------------------------------------------------------------------------------------------------------------
//// *
//// *
//// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
//// * This software is published under the GPL GNU General Public License. 
//// * This program is free software; you can redistribute it and/or 
//// * modify it under the terms of the GNU General Public License 
//// * as published by the Free Software Foundation; either version 2 
//// * of the License, or (at your option) any later version. * 
//// * This program is distributed in the hope that it will be useful, 
//// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
//// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
//// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
//// * along with this program; if not, write to the Free Software 
//// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
//// * 
//// * <OSCAR TEAM>
//// * This software was written for the 
//// * Department of Family Medicine 
//// * McMaster Unviersity 
//// * Hamilton 
//// * Ontario, Canada 
//// *
//// -----------------------------------------------------------------------------------------------------------------------
//import bean.DBOperator;
//import bean.Utility;
//import java.io.*;
//import javax.servlet.http.*;
//import javax.servlet.*;
//
//public class UploadServlet extends HttpServlet{
// static final int MAX_SIZE = 102400;
//  String rootPath ="" ;
//  String successMessage="" ;
//  String fileString="" ;
//  String form_name="" ;
//  String subject="" ;
//
//  public void init(ServletConfig config) throws ServletException
//  {
//    super.init(config);
//    fileString = "";
//    form_name ="";
//    rootPath = config.getInitParameter("RootPath");
//    if (rootPath == null)
//    {
//      rootPath = "/";
//    }
//    successMessage = config.getInitParameter("SuccessMessage");
//    if (successMessage == null)
//    {
//      successMessage = "File upload complete!";
//    }
//  }
//
//  public void doPost(HttpServletRequest request,HttpServletResponse response){
//
//    ServletOutputStream out=null;
//    DataInputStream in=null;
//    FileOutputStream fileOut=null;
//    try
//    {
//      /*set content type of response and get handle to output
//        stream in case we are unable to redirect client*/
//      response.setContentType("text/plain");
//      out = response.getOutputStream();
//    }
//    catch (IOException e)
//    {
//      //print error message to standard out
//      System.out.println("Error getting output stream.");
//      System.out.println("Error description: " + e);
//      return;
//    }
//    try
//    {
//      //get content type of client request
//      String contentType = request.getContentType();
//      //make sure content type is multipart/form-data
//      if(contentType != null && contentType.indexOf(
//        "multipart/form-data") != -1)
//      {
//        //open input stream from client to capture upload file
//        in = new DataInputStream(request.getInputStream());
//        //get length of content data
//        int formDataLength = request.getContentLength();
//        //allocate a byte array to store content data
//        byte dataBytes[] = new byte[formDataLength];
//        //read file into byte array
//        int bytesRead = 0;
//        int totalBytesRead = 0;
//        int sizeCheck = 0;
//        while (totalBytesRead < formDataLength)
//        {
//          //check for maximum file size violation
//          sizeCheck = totalBytesRead + in.available();
//          if (sizeCheck > MAX_SIZE)
//          {
//            out.println("Sorry, file is too large to upload.");
//            return;
//          }
//          bytesRead = in.read(dataBytes, totalBytesRead,
//            formDataLength);
//          totalBytesRead += bytesRead;
//        }
//        //create string from byte array for easy manipulation
//        String file = new String(dataBytes);
//        //since byte array is stored in string, release memory
//        dataBytes = null;
//        /*get boundary value (boundary is a unique string that
//          separates content data)*/
//        int lastIndex = contentType.lastIndexOf("=");
//        String boundary = contentType.substring(lastIndex+1,
//          contentType.length());
//        //get Directory web variable from request
//        String directory="";
//
//// get form_name
//if (file.indexOf("name=\"form_name\"") > 0){
//  form_name = file.substring(file.indexOf("name=\"form_name\"")+15);
//  form_name = form_name.substring(form_name.indexOf("\n"));
//  form_name = form_name.substring(form_name.indexOf("\n")+1);
//  form_name = form_name.substring(form_name.indexOf("\n")+1);
//  form_name = form_name.substring(0,form_name.indexOf("\n"));
//}
//
//// get form_name
//if (file.indexOf("name=\"subject\"") > 0){
//  subject = file.substring(file.indexOf("name=\"subject\"")+13);
//  subject = subject.substring(subject.indexOf("\n"));
//  subject = subject.substring(subject.indexOf("\n")+1);
//  subject = subject.substring(subject.indexOf("\n")+1);
//  subject = subject.substring(0,subject.indexOf("\n"));
//}
//
//        if (file.indexOf("name=\"Directory\"") > 0)
//        {
//          directory = file.substring(
//            file.indexOf("name=\"Directory\""));
//          //remove carriage return
//          directory = directory.substring(
//            directory.indexOf("\n")+1);
//          //remove carriage return
//          directory = directory.substring(
//            directory.indexOf("\n")+1);
//          //get Directory
//          directory = directory.substring(0,
//            directory.indexOf("\n")-1);
//          /*make sure user didn't select a directory higher in
//            the directory tree*/
//          if (directory.indexOf("..") > 0)
//          {
//            out.println("Security Error: You can't upload " +
//              "to a directory higher in the directory tree.");
//            return;
//          }
//        }
//        //get SuccessPage web variable from request
//        String successPage="";
//        if (file.indexOf("name=\"SuccessPage\"") > 0)
//        {
//          successPage = file.substring(
//            file.indexOf("name=\"SuccessPage\""));
//          //remove carriage return
//          successPage = successPage.substring(
//            successPage.indexOf("\n")+1);
//          //remove carriage return
//          successPage = successPage.substring(
//            successPage.indexOf("\n")+1);
//          //get success page
//          successPage = successPage.substring(0,
//            successPage.indexOf("\n")-1);
//        }
//        //get OverWrite flag web variable from request
//        String overWrite;
//        if (file.indexOf("name=\"OverWrite\"") > 0)
//        {
//          overWrite = file.substring(
//            file.indexOf("name=\"OverWrite\""));
//          //remove carriage return
//          overWrite = overWrite.substring(
//            overWrite.indexOf("\n")+1);
//          //remove carriage return
//          overWrite = overWrite.substring(
//            overWrite.indexOf("\n")+1);
//          //get overwrite flag
//          overWrite = overWrite.substring(0,
//            overWrite.indexOf("\n")-1);
//        }
//        else
//        {
//          overWrite = "false";
//        }
//        //get OverWritePage web variable from request
//        String overWritePage="";
//        if (file.indexOf("name=\"OverWritePage\"") > 0)
//        {
//          overWritePage = file.substring(
//            file.indexOf("name=\"OverWritePage\""));
//          //remove carriage return
//          overWritePage = overWritePage.substring(
//            overWritePage.indexOf("\n")+1);
//          //remove carriage return
//          overWritePage = overWritePage.substring(
//            overWritePage.indexOf("\n")+1);
//          //get overwrite page
//          overWritePage = overWritePage.substring(0,
//            overWritePage.indexOf("\n")-1);
//        }
//        //get filename of upload file
//
//
////*************** saveFile  name
//
//        String saveFile = file.substring(
//          file.indexOf("filename=\"")+10);
//        saveFile = saveFile.substring(0,
//          saveFile.indexOf("\n"));
//        saveFile = saveFile.substring(
//          saveFile.lastIndexOf("\\")+1,
//          saveFile.indexOf("\""));
//        /*remove boundary markers and other multipart/form-data
//          tags from beginning of upload file section*/
//        int pos; //position in upload file
//        //find position of upload file section of request
//        pos = file.indexOf("filename=\"");
//        //find position of content-disposition line
//        pos = file.indexOf("\n",pos)+1;
//        //find position of content-type line
//        pos = file.indexOf("\n",pos)+1;
//        //find position of blank line
//        pos = file.indexOf("\n",pos)+1;
//        /*find the location of the next boundary marker
//          (marking the end of the upload file data)*/
//        int boundaryLocation = file.indexOf(boundary,pos)-4;
//        //upload file lies between pos and boundaryLocation
//
//// **********  this is the file String
//
//       fileString  = file.substring(pos,boundaryLocation);
//
////********  save to dabase
//       fileString = new Utility().moveSingleQuote(fileString);
// 
//       DBOperator dbo = new DBOperator();
//
//       form_name=form_name.substring(0,form_name.length()-1);
// 
//      try { 
////          dbo.save_eForms(form_name,saveFile,fileString);
//          dbo.save_eForms(form_name,saveFile,subject,fileString);
//      }       catch(Exception e){}
//
//       fileString = ""; 
//
//// send back message to user
//	   out.println("<head>");
//	   out.println("<script language=\"JavaScript\">");
//           out.println("setTimeout(\"top.location.href = '../e_form/UploadHtml.jsp'\",500);");
//	   out.println("</script>");
//	   out.println("</head>");
//	   out.println("<body>");
//	   out.println("File upload successfully.<br> Please wait for 1 seconds ."); 
//	   out.println("</body>");
// 
// 
//        file = file.substring(pos,boundaryLocation);
//        //build the full path of the upload file
//        String fileName = new String(rootPath + directory +
//          saveFile);
//        //create File object to check for existence of file
//        File checkFile = new File(fileName);
//
//      }
//
// 
// 
//    }
//    catch(Exception e)
//    {
//       try
//       {
//        //print error message to standard out
//        System.out.println("Error in doPost: " + e);
//        //send error message to client
//        out.println("An unexpected error has occurred.");
//        out.println("Error description: " + e);
//       }
//       catch (Exception f) {}
//    }
//
//  }
//}
