<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%
  if(session.getValue("patient") == null)    response.sendRedirect("logout.jsp");
  String demographic_no = (String) session.getAttribute("demo_no");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*,java.net.*"
	errorPage="../errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
%>

<%
  String [][] dbQueries=new String[][] {
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"search_encounter", "select * from encounter where demographic_no = ? order by encounter_date desc, encounter_time desc"},
   };

   //associate each operation with an output JSP file -- displaymode
   String[][] responseTargets=new String[][] {
     {"Add Record" , "demographicaddarecord.jsp"},
   };
   apptMainBean.doConfigure(dbQueries,responseTargets);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT SUMMARY</title>
<link rel="stylesheet" href="../web.css">
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<center>
<table border="0" cellspacing="0" cellpadding="0" width="650">
	<tr>
		<td>

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#486ebd">
				<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT
				SUMMARY</font></th>
			</tr>
		</table>
		<%
   ResultSet rsdemo = null;
   String demoname=null,dob=null,gender=null,hin=null,roster=null;
   int dob_year = 0, dob_month = 0, dob_date = 0;
   rsdemo = apptMainBean.queryResults(demographic_no, "search_detail"); //dboperation=search_demograph
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
  String xml_Problem_List="",xml_Medication="",xml_Alert="",xml_Family_Social_History="",strTemp="";
  if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);

   rsdemo = null;
   DemographicAccessory da = demographicAccessoryDao.find(Integer.parseInt(demographic_no));
   if(da != null) {

     String content=da.getContent();
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

		<table width="100%" border="0">
			<tr>
				<td align="left"><font color="blue"><%=demoname%> <i><%=""+age%></i>
				<%=gender%> <i>RS: <%=roster==null?"NONE":roster%></i></font></td>
				</td>
			</tr>
		</table>
		<table width="100%" border="1" bgcolor="white" cellpadding="0"
			cellspacing="0">
			<tr bgcolor="#339999">
				<td width="50%" align="center"><b>Problem List</b></td>
				<td width="50%" align="center"><b>Medication</b></td>
			</tr>
			<tr>
				<td><font size="-1"><pre>&nbsp;<%=xml_Problem_List%></pre></font></td>
				<td><font size="-1"><pre>&nbsp;<%=xml_Medication%></pre></font></td>
			</tr>
			<tr bgcolor="#339999">
				<td align="center"><b>Allergy/Alert</b></td>
				<td align="center"><b>Family Social History</b></td>
			</tr>
			<tr>
				<td><font size="-1"><pre>&nbsp;<%=xml_Alert%></pre></font></td>
				<td><font size="-1"><pre>&nbsp;<%=xml_Family_Social_History%></pre></font></td>
			</tr>
		</table>

		<p>
		<table width="100%" border="0" bgcolor="#ffffcc">
			<tr>
				<td><font size="-1"> <b>Encounter History</b></font></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<%
   rsdemo = null;
   rsdemo = apptMainBean.queryResults(demographic_no, "search_encounter");

   while (rsdemo.next()) {
%> &nbsp;<%=rsdemo.getString("encounter_date")%> <%=rsdemo.getString("encounter_time")%><font
					color="blue"> <%
     String historysubject = rsdemo.getString("subject")==null?"No Subject":rsdemo.getString("subject").equals("")?"No Subject":rsdemo.getString("subject");
     StringTokenizer st=new StringTokenizer(historysubject,":");
     String strForm="", strTemplateURL="";
     while (st.hasMoreTokens()) {
       strForm = (new String(st.nextToken())).trim();
       break;
     }

     if(strForm.toLowerCase().compareTo("form")==0 && st.hasMoreTokens()) {
       strTemplateURL = "template" + (new String(st.nextToken())).trim().toLowerCase()+".jsp";
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
%>
				</td>
			</tr>
		</table>


		</td>
	</tr>
</table>
</center>
</body>
</html>
