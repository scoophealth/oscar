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

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  int fid = Integer.parseInt(request.getParameter("fid")) ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  
<%@ page import = "java.sql.ResultSet,oscar.*" %> 
<jsp:useBean id="beanDBOperator" scope="session" class="bean.DBOperator" />
<jsp:useBean id="beanMakeForm" scope="session" class="bean.MakeForm" />
<jsp:useBean id="beanUtility" scope="session" class="bean.Utility" />
<jsp:useBean id="beanFormData" scope="session" class="bean.FormData" />
 
<%  
  // this requested data is for gererating the target HTML string
  String label = "" ;
  String date = "" ;
  String currentproblems = "" ;
  String currentmedications = "" ;
  String familysocialhistory  = "" ;
  String alert = "" ;
  String subject = "" ;

  int numname = 20;
  String[][] namelist = new String [numname][2];
  for(int i=0; i<numname; i++) {
    for(int j=0; j<2; j++) {
      if(i<9) namelist[i][j] = "name_0" + (i+1);
      else namelist[i][j] = "name_" + (i+1);
    }
  }

   if (request.getParameter("label")!=null){
      label = Misc.charEscape(request.getParameter("label"), '\''); 
   }
   if (request.getParameter("date")!=null){
      date = request.getParameter("date"); 
      date = beanUtility.moveMarks(date); 
   }
   if (request.getParameter("currentproblems")!=null){
      currentproblems = Misc.charEscape(request.getParameter("currentproblems"), '\''); 
   }
   if (request.getParameter("currentmedications")!=null){
      currentmedications = Misc.charEscape(request.getParameter("currentmedications"), '\''); 
   }
   if (request.getParameter("familysocialhistory")!=null){
      familysocialhistory = Misc.charEscape(request.getParameter("familysocialhistory"), '\''); 
   }
   if (request.getParameter("alert")!=null){
      alert = Misc.charEscape(request.getParameter("alert"), '\''); 
   }
   if (request.getParameter("subject")!=null){
      subject = Misc.charEscape(request.getParameter("subject"), '\''); 
   }

  for(int i=0; i<numname; i++ ) {
    if (request.getParameter( namelist[i][0] )!=null){
      namelist[i][1] = request.getParameter(namelist[i][0]); 
      namelist[i][1] = beanUtility.moveMarks(namelist[i][1]); 
      namelist[i][1] = Misc.charEscape((namelist[i][1]), '\''); 
    }else{
      namelist[i][1] = ""; 
    }
  }

   String formString = beanDBOperator.getFormString(fid);
   String newFormString = beanMakeForm.mixNewForm(formString,label,date,currentproblems,currentmedications,familysocialhistory,alert,subject,namelist[0][1] ,namelist[1][1] ,namelist[2][1] ,namelist[3][1] ,namelist[4][1] ,namelist[5][1] ,namelist[6][1] ,namelist[7][1] ,namelist[8][1] ,namelist[9][1] ,namelist[10][1] ,namelist[11][1] ,namelist[12][1] ,namelist[13][1] ,namelist[14][1] ,namelist[15][1] ,namelist[16][1] ,namelist[17][1] ,namelist[18][1] ,namelist[19][1] );

	 String form_provider = "---" ; 
   String form_name = (session.getValue("form_name")).toString() ; 
      
   String form_date;
   if (date!=""){
	      form_date = date; 
   }else{
	      form_date = beanUtility.getToday(); 
   }

	 String form_data = newFormString ; 
   beanDBOperator.save_eForms_data (demographic_no,fid,form_name,subject,form_date,form_provider,form_data);
   response.sendRedirect("MyForm.jsp?demographic_no="+demographic_no);
%>
  

  