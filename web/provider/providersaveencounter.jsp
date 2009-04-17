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
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.sql.*, java.util.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD AN ENCOUNTER RECORD</font></th>
	</tr>
</table>
<%
  //update the demo accessary data
  String content=null;//default is not null temp=null, 
  long numRecord=1, rowsAffected=0;
  content="<xml_Problem_List>"+SxmlMisc.replaceHTMLContent(request.getParameter("xml_Problem_List"))+"</xml_Problem_List>"+ "<xml_Medication>"+SxmlMisc.replaceHTMLContent(request.getParameter("xml_Medication"))+"</xml_Medication>"+ "<xml_Alert>"+SxmlMisc.replaceHTMLContent(request.getParameter("xml_Alert"))+"</xml_Alert>" +"<xml_Family_Social_History>"+SxmlMisc.replaceHTMLContent(request.getParameter("xml_Family_Social_History"))+"</xml_Family_Social_History>";

  String[] param0 = new String[2];
  param0[0]=request.getParameter("demographic_no");
  param0[1]=content; 
  String[] param2 = new String[2];
  param2[0]=content;
  param2[1]=request.getParameter("demographic_no");
  List<Map> resultList = oscarSuperManager.find("providerDao", "search_demographicaccessorycount", new Object[] {request.getParameter("demographic_no")});
  for (Map demo : resultList) {
    numRecord=(Long)demo.get("count(demographic_no)");
  }
  if(numRecord==0) {
    rowsAffected = oscarSuperManager.update("providerDao", "add_demographicaccessory", param0);
  } else {
    rowsAffected = oscarSuperManager.update("providerDao", "update_demographicaccessory", param2);
  }
  if (rowsAffected !=1) { out.println("Error"); return;}


  //get demographic string
  String demoname=null,address=null,phone=null,dob=null,gender=null,hin=null,ver=null,roster=null,fd=null;
  int dob_year = 0, dob_month = 0, dob_date = 0;
  resultList = oscarSuperManager.find("providerDao", "search_demograph", new Object[] {request.getParameter("demographic_no")});
  for (Map demo : resultList) {
    demoname="<xml_name>" +SxmlMisc.replaceHTMLContent(demo.get("last_name")+", "+demo.get("first_name")) +"</xml_name>";
    address="<xml_address>" +SxmlMisc.replaceHTMLContent(demo.get("address") +",  "+demo.get("city") +",  "+demo.get("province") +"  "+ demo.get("postal")) +"</xml_address>";
	phone="<xml_hp>" +demo.get("phone") +"</xml_hp>";
    dob_year = Integer.parseInt((String)demo.get("year_of_birth"));
    dob_month = Integer.parseInt((String)demo.get("month_of_birth"));
    dob_date = Integer.parseInt((String)demo.get("date_of_birth"));
    dob="<xml_dob>" +dob_year+"-"+dob_month+"-"+dob_date +"</xml_dob>";
    gender="<xml_sex>" +demo.get("sex") +"</xml_sex>";
    hin="<xml_hin>" +demo.get("hin") +"</xml_hin>";
    ver="<xml_ver>" +demo.get("ver") +"</xml_ver>";
    roster="<xml_roster>" +demo.get("roster_status") +"</xml_roster>";
    // fd="<xml_fd>" +SxmlMisc.replaceHTMLContent((demo.get("family_doctor")==null?"":demo.get("family_doctor"))) +"</xml_fd>";
  }
  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
  String xml_demo = demoname + address + phone + dob + gender + hin + ver + roster +"<xml_age>"+age+"</xml_age>";
  //String xml_demo = demoname + address + phone + dob + gender + hin + ver + roster + fd +"<xml_age>"+age+"</xml_age>";
  
  //save encounter
  String subject="";//default is not null temp=null, 
  content=xml_demo + SxmlMisc.createXmlDataString(request, "xml_");

  subject = request.getParameter("xml_subjectprefix")==null?"":".: " ; //(request.getParameter("xml_subjectprefix") +": ") ;
  subject += request.getParameter("xml_subject")+ " |"+ request.getParameter("attachmentdisplay");
  
  //System.out.println(subject+content);
  String[] param =new String[7];
  param[0]=request.getParameter("demographic_no");
	param[1]=curYear+"-"+(curMonth)+"-"+curDay; //request.getParameter("encounter_date");
	param[2]=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND); //request.getParameter("encounter_time"); //MyDateFormat.getTimeXX_XX_XX(request.getParameter("encounter_time"));
	param[3]=user_no; //request.getParameter("user_no");
	param[4]=subject;
	param[5]=content;
	param[6]=URLDecoder.decode(request.getParameter("encounterattachment")).replace('^','&'); //restore the '&' symbol
  rowsAffected = oscarSuperManager.update("providerDao", request.getParameter("dboperation"), param);
  if (rowsAffected == 1) {
    //if need to delete the previous edit enc. to recyclebin
    if(request.getParameter("del_encounter_no")!=null && !request.getParameter("del_encounter_no").equals("null") ) {
      String[] param3 = new String[4];
      resultList = oscarSuperManager.find("providerDao", "search_encountersingle", new Object[] {request.getParameter("del_encounter_no")});
      for (Map encounter : resultList) {
        content = "<encounter_no>"+encounter.get("encounter_no")+"</encounter_no>"+ "<demographic_no>"+encounter.get("demographic_no")+"</demographic_no>"+ "<encounter_date>"+encounter.get("encounter_date")+"</encounter_date>";
        content += "<encounter_time>"+encounter.get("encounter_time")+"</encounter_time>"+ "<provider_no>"+encounter.get("provider_no")+"</provider_no>"+ "<subject>"+encounter.get("subject")+"</subject>";
        content += "<content>"+encounter.get("content")+"</content>" +"<encounterattachment>"+encounter.get("encounterattachment")+"</encounterattachment>";
	      param3[0]=user_no;
	      param3[1]=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
	      param3[2]=String.valueOf(encounter.get("encounter_date")); //keyword
	      param3[3]=content;
      }
      int rowsAffected2 = oscarSuperManager.update("providerDao", "delete_encounter1", param3);
      if (rowsAffected2 == 1) {
        rowsAffected2 = oscarSuperManager.update("providerDao", "delete_encounter2", new Object[] {request.getParameter("del_encounter_no")});
      }
    }
  
    if(request.getParameter("submit") != null && request.getParameter("submit").equals("Save & Print Preview") ) {  //response.sendRedirect("providerencounterprint.jsp"); //
      //get encounter_no
      String[] param1 = new String[4];
      param1[0]=request.getParameter("demographic_no");
	  param1[1]=curYear+"-"+(curMonth)+"-"+curDay; //request.getParameter("encounter_date");
	  param1[2]=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND); //request.getParameter("encounter_time"); 
	  param1[3]=request.getParameter("user_no");
      resultList = oscarSuperManager.find("providerDao", "search_encounter_no", param1);
      String encounter_no = "0";
      for (Map encounter : resultList) {
        encounter_no = String.valueOf(encounter.get("encounter_no"));
      }
      if (true) {
        out.clear();
        pageContext.forward("providerencounterprint.jsp?encounter_no="+encounter_no);
        return;
      }
    } 
%>
<p>
<h1>Successful Addition of an Encounter Record.</h1>
</p>
<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script> <%
  }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
  }
%>
<p></p>
<hr width="90%"/>
<input type="button" value="Close this window" onClick="window.close()">
</center>
</body>
</html>