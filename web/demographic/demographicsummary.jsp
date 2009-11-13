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
  
  if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Create Encounter Report") ) {
    if(true) {
      out.clear();
      pageContext.forward("../report/reportencounterhistory.jsp"); //forward request&response to the target page
      return;
    }
  }
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*,java.net.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="demosummaryBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%@ include file="../admin/dbconnection.jsp"%>
<%
  String [][] dbQueries=new String[][] {
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"search_demographicaccessory", "select * from demographicaccessory where demographic_no=?"},
    {"search_encounter", "select * from encounter where demographic_no = ? order by encounter_date desc, encounter_time desc"},
    {"search_encounterdetail", "select * from encounter where encounter_no=?"},
    {"delete_encounter1", "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(?,?,'encounter',?,?)"},
    {"delete_encounter2", "delete from encounter where encounter_no = ?"},
   };
   String[][] responseTargets=new String[][] {   };
   demosummaryBean.doConfigure(dbQueries,responseTargets);
%>

<% //delete the selected records
  if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Delete") ) {
    ResultSet rs = null;
    int ii = Integer.parseInt(request.getParameter("encounternum"));  
    String[] param =new String[4];
    String content=null, keyword=null, datetime=null;
    GregorianCalendar now=new GregorianCalendar();
    datetime  =now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
    
    for(int i=0;i<=ii;i++) {
      if(request.getParameter("encounter_no"+i)==null) {
// System.out.println("      1     ");
        continue;
      }
       
      rs = demosummaryBean.queryResults(request.getParameter("encounter_no"+i), "search_encounterdetail");
      while (rs.next()) { 
        keyword = demosummaryBean.getString(rs,"encounter_date");
        content = "<encounter_no>"+demosummaryBean.getString(rs,"encounter_no")+"</encounter_no>"+ "<demographic_no>"+demosummaryBean.getString(rs,"demographic_no")+"</demographic_no>"+ "<encounter_date>"+demosummaryBean.getString(rs,"encounter_date")+"</encounter_date>";
        content += "<encounter_time>"+demosummaryBean.getString(rs,"encounter_time")+"</encounter_time>"+ "<provider_no>"+demosummaryBean.getString(rs,"provider_no")+"</provider_no>"+ "<subject>"+demosummaryBean.getString(rs,"subject")+"</subject>";
        content += "<content>"+demosummaryBean.getString(rs,"content")+"</content>" +"<encounterattachment>"+demosummaryBean.getString(rs,"encounterattachment")+"</encounterattachment>";
      }
      
	    param[0]=user_no;
	    param[1]=datetime;
	    param[2]=keyword;
	    param[3]=content;
      
      int rowsAffected = demosummaryBean.queryExecuteUpdate(param, "delete_encounter1");
      if(rowsAffected ==1) {
        rowsAffected = demosummaryBean.queryExecuteUpdate(request.getParameter("encounter_no"+i), "delete_encounter2");
      } else {
        response.sendRedirect("index.htm");
      }
    } //end for loop
  }
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT SUMMARY</title>
<link rel="stylesheet" href="../web.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--



//-->
</script>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="silver">
		<th align=CENTER NOWRAP><font face="Helvetica" color="navy">PATIENT
		SUMMARY</font></th>
	</tr>
</table>
<%
   ResultSet rsdemo = null;
   String demoname=null,dob=null,gender=null,hin=null,roster=null;
   int dob_year = 0, dob_month = 0, dob_date = 0;
   rsdemo = demosummaryBean.queryResults(request.getParameter("demographic_no"), "search_detail"); //dboperation=search_demograph
   while (rsdemo.next()) { 
     demoname=rsdemo.getString("last_name")+", "+rsdemo.getString("first_name");
     dob_year = Integer.parseInt(rsdemo.getString("year_of_birth"));
     dob_month = Integer.parseInt(rsdemo.getString("month_of_birth"));
     dob_date = Integer.parseInt(rsdemo.getString("date_of_birth"));
     dob=dob_year+"-"+dob_month+"-"+dob_date;
     gender=rsdemo.getString("sex");
     hin=rsdemo.getString("hin");
     roster=rsdemo.getString("roster_status");
   }

	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  String xml_Problem_List="",xml_Medication="",xml_Alert="",xml_Family_Social_History="",strTemp="", content="";
  if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);

   rsdemo = null;
   rsdemo = demosummaryBean.queryResults(request.getParameter("demographic_no"), "search_demographicaccessory"); //dboperation=search_demograph
   if (rsdemo.next()) { 
     content=rsdemo.getString("content");
	 strTemp = SxmlMisc.getXmlContent(content, "<xml_Problem_List>","</xml_Problem_List>");
     xml_Problem_List = strTemp==null?"":strTemp;
	 strTemp = SxmlMisc.getXmlContent(content, "<xml_Medication>","</xml_Medication>");
     xml_Medication = strTemp==null?"":strTemp;
	 strTemp = SxmlMisc.getXmlContent(content, "<xml_Alert>","</xml_Alert>");
     xml_Alert = strTemp==null?"":strTemp;
	 strTemp = SxmlMisc.getXmlContent(content, "<xml_Family_Social_History>","</xml_Family_Social_History>");
     xml_Family_Social_History = strTemp==null?"":strTemp;

   } 
%>
<xml id="xml_list">
<encounter>
<%=content%>
</encounter>
</xml>

<table width="100%" border="0">
	<tr>
		<td align="left"><font color="blue"><%=demoname%> <i><%=""+age%></i>
		<%=gender%> <i>RS: <%=roster==null?"NONE":roster%></i></font></td>
		<td align="right"><input type="submit" name="submit"
			value=" Print " onClick="window.print()"> <input
			type="button" name="Button" value="Cancel" onClick="window.close()">
		</td>
	</tr>
</table>
<table width="100%" border="1" bgcolor="white" cellpadding="0"
	cellspacing="1" datasrc='#xml_list'>
	<tr bgcolor="#eeeeee">
		<td width="50%" align="center"><b>Problem List</b></td>
		<td width="50%" align="center"><b>Medication</b></td>
	</tr>
	<tr>
		<td><font size="-1">
		<div datafld='xml_Problem_List'></div>
		</font></td>
		<td><font size="-1">
		<div datafld='xml_Medication'></div>
		</font></td>
	</tr>
	<tr bgcolor="#eeeeee">
		<td align="center"><b>Allergy/Alert</b></td>
		<td align="center"><b>Family Social History</b></td>
	</tr>
	<tr>
		<td><font size="-1">
		<div datafld='xml_Alert'></div>
		</font></td>
		<td><font size="-1">
		<div datafld='xml_Family_Social_History'></div>
		</font></td>
	</tr>
</table>

<p>
<form name="encounterrep" method="post" action="demographicsummary.jsp">
<table width="100%" border="0" bgcolor="ivory">
	<tr>
		<td><font size="-1"> <b>Encounter History</b></font></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF">
		<%
   rsdemo = null;
   rsdemo = demosummaryBean.queryResults(request.getParameter("demographic_no"), "search_encounter");
   int i=0;
   while (rsdemo.next()) { 
     i++;
%> &nbsp;<%=rsdemo.getString("encounter_date")%> <%=rsdemo.getString("encounter_time")%>

		<input type="checkbox" name="<%="encounter_no"+i%>"
			value="<%=rsdemo.getString("encounter_no")%>"> <font
			color="blue"> <%
     String historysubject = rsdemo.getString("subject")==null?"No Subject":rsdemo.getString("subject").equals("")?"No Subject":rsdemo.getString("subject");
     StringTokenizer st=new StringTokenizer(historysubject,":");
     //System.out.println(" history = " + historysubject);
     String strForm="", strTemplateURL="";
     while (st.hasMoreTokens()) {
       strForm = (new String(st.nextToken())).trim();
       break;
     }

     if(strForm.toLowerCase().compareTo("form")==0 && st.hasMoreTokens()) {
       strTemplateURL = "template" + (new String(st.nextToken())).trim()+".jsp";
%> <a href=#
			onClick="popupPage(600,800,'../provider/providercontrol.jsp?encounter_no=<%=rsdemo.getString("encounter_no")%>&dboperation=search_encountersingle&displaymodevariable=<%=strTemplateURL%>&displaymode=vary&bNewForm=0')"><%=rsdemo.getString("subject")==null?"No Subject":rsdemo.getString("subject").equals("")?"No Subject":rsdemo.getString("subject")%>
		</a></font><br>
		<%
     } else if(strForm.compareTo("")!=0) {
%> <a href=#
			onClick="popupPage(400,600,'../provider/providercontrol.jsp?encounter_no=<%=rsdemo.getString("encounter_no")%>&template=<%=strForm%>&dboperation=search_encountersingle&displaymode=encountersingle')"><%=rsdemo.getString("subject")==null?"No Subject":rsdemo.getString("subject").equals("")?"No Subject":rsdemo.getString("subject")%>
		</a></font><br>
		<%
     }
   }     
   demosummaryBean.closePstmtConn();
%>
		</td>
	</tr>
	<tr bgcolor="#eeeeee">
		<td align="center"><input type="hidden" name="encounternum"
			value="<%=i%>"> <input type="hidden" name="demographic_no"
			value="<%=request.getParameter("demographic_no")%>"> <input
			type="submit" name="submit" value="Create Encounter Report">
		<input type="submit" name="submit" value="Delete"><input
			type="button" name="button" value="Cancel" onClick="window.close()">
		</td>
	</tr>
</table>
</form>
<center></center>
</body>
</html>