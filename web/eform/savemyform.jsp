<%--  
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
--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  int fid = Integer.parseInt(request.getParameter("fid")) ; 
  String form_provider = (String) session.getAttribute("user");
  String form_name = request.getParameter("form_name"); //(session.getValue("form_name")).toString() ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  
<%@ page import = "java.sql.ResultSet,oscar.util.*,oscar.eform.*, java.util.Enumeration, org.w3c.dom.*"   %> 
<jsp:useBean id="dataBean" scope="session" class="oscar.eform.EfmDataOpt" />
<jsp:useBean id="beanMakeForm" scope="session" class="oscar.eform.EfmMakeForm" />
<%  
  // this requested data is for gererating the target HTML string
  String label = "" ;
  String date = "" ;
  String currentproblems = "" ;
  String currentmedications = "" ;
  String familysocialhistory  = "" ;
  String alert = "" ;
  String subject = "" ;
  String doctor = "" ;
  String address = "";
  String addressLine = "" ;
  String name = "" ;
  String params = "" ;
  
  Enumeration e = request.getParameterNames() ;

  if (request.getParameter("label")!=null){
    label = UtilMisc.charEscape(request.getParameter("label"), '\''); 
  }
  if (request.getParameter("date")!=null){
    date = UtilMisc.charEscape(request.getParameter("date"), '\''); 
  }
  if (request.getParameter("doctor")!=null){
    doctor = UtilMisc.charEscape(request.getParameter("doctor"), '\''); 
  }
  if (request.getParameter("address")!=null){
    address = UtilMisc.charEscape(request.getParameter("address"), '\''); 
  }
  if (request.getParameter("addressLine")!=null){
    addressLine = UtilMisc.charEscape(request.getParameter("addressLine"), '\''); 
  }
  if (request.getParameter("name")!=null){
    name = UtilMisc.charEscape(request.getParameter("name"), '\''); 
  }
  if (request.getParameter("currentproblems")!=null){
    currentproblems = UtilMisc.charEscape(request.getParameter("currentproblems"), '\''); 
  }
  if (request.getParameter("currentmedications")!=null){
    currentmedications = UtilMisc.charEscape(request.getParameter("currentmedications"), '\''); 
  }
  if (request.getParameter("familysocialhistory")!=null){
    familysocialhistory = UtilMisc.charEscape(request.getParameter("familysocialhistory"), '\''); 
  }
  if (request.getParameter("alert")!=null){
    alert = UtilMisc.charEscape(request.getParameter("alert"), '\''); 
  }
  if (request.getParameter("subject")!=null){
    subject = UtilMisc.charEscape(request.getParameter("subject"), '\''); 
  }

  //Section that creates XML, still needs to be inserted somewhere into a MYSQL table
  try{
  Document doc = UtilXML.newDocument();
  Element root = UtilXML.addNode(doc, form_name);
    while (e.hasMoreElements()){
        params = (String)e.nextElement();
        Element paramElem = UtilXML.addNode(root, params);
        paramElem.appendChild(doc.createTextNode(request.getParameter(params)));
        //System.out.println(UtilXML.toXML(doc));
    }
  }
  catch(Exception ex){
  ex.printStackTrace();
  }
  

  
// "formString" - from eforom
  String formString = dataBean.getFormString(fid);

// mix "formString" with new inputing data
  EfmPrepData prepData = new EfmPrepData(request, formString) ;  
  String form_data = beanMakeForm.addContent(formString, prepData.getMeta(), prepData.getValue(), prepData.getTagSymbol() ) ;
  
// save form
  dataBean.save_eform_data (demographic_no,fid,form_name,subject,form_provider,form_data);

//response.sendRedirect("MyForm.jsp?");
  response.sendRedirect("myform.jsp?demographic_no="+demographic_no);
%>  