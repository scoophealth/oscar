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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->


<%
  
  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):("null") ;
  String form_no = request.getParameter("formId")!=null?request.getParameter("formId"):("0") ;
  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, oscar.util.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../../appointment/errorpage.jsp"%>
<jsp:useBean id="plannerBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="riskDataBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="risks"
	class="oscar.decision.DesAntenatalPlannerRisks_99_12" scope="page" />
<jsp:useBean id="checklist"
	class="oscar.decision.DesAnnualReviewPlannerChecklist" scope="page" />
<%@ include file="../../admin/dbconnection.jsp"%>
<% 
String [][] dbQueries=new String[][] { 
{"search_demographic", "select last_name,first_name,sex,year_of_birth,month_of_birth,date_of_birth from demographic where demographic_no = ?" }, 
{"search_des", "select * from desannualreviewplan where form_no <= ? and demographic_no = ? order by form_no desc, des_date desc, des_time desc limit 1 " }, 
};
plannerBean.doConfigure(dbQueries);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Planner</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript" language="Javascript">

</script>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<%
  String[] param2 = {form_no, demographic_no};
  ResultSet rsdemo = plannerBean.queryResults(param2, "search_des");
  while (rsdemo.next()) { 
    String risk_content = rsdemo.getString("risk_content");
    String checklist_content = rsdemo.getString("checklist_content");
%>
<xml id="xml_list">
<planner>
<%=checklist_content%>
</planner>
</xml>
<%
    //set the riskdata bean from xml file
    Properties savedar1risk1 = risks.getRiskName(oscarVariables.getProperty("tomcat_path") +"webapps/"+ oscarVariables.getProperty("project_home") + "/decision/annualreview/desannualreviewplannerrisk.xml");
  	StringBuffer tt; 
    for (Enumeration e = savedar1risk1.propertyNames() ; e.hasMoreElements() ;) {
      tt = new StringBuffer().append(e.nextElement());
      if(SxmlMisc.getXmlContent(risk_content, savedar1risk1.getProperty(tt.toString()))!= null) 
        riskDataBean.setProperty(tt.toString(), savedar1risk1.getProperty(tt.toString()));  //set riskno
    }
  }

  //find the age and sex of the patient from demographic table
  rsdemo = plannerBean.queryResults(demographic_no, "search_demographic");
  int age = 0;
  String sex = null, patientName =null;
  while (rsdemo.next()) { 
    age = UtilDateUtilities.calcAge(rsdemo.getString("year_of_birth"), rsdemo.getString("month_of_birth"),rsdemo.getString("date_of_birth"));
    sex = rsdemo.getString("sex");
	patientName = rsdemo.getString("last_name") + ", "+ rsdemo.getString("first_name");
  }
  if (age>=65 && age <=85){
    riskDataBean.setProperty("999", "checked" ); 
    if (sex.equals("f") || sex.equals("F"))
      riskDataBean.setProperty("998", "checked" ); 
    if (sex.equals("m") || sex.equals("M"))
      riskDataBean.setProperty("997", "checked" ); 
    if (age>=65 && age<=69)
      riskDataBean.setProperty("996", "checked" ); 
  }

%>
<table bgcolor='silver' width='100%' cellspacing=0 cellpadding=0>
	<tr>
		<td><font color='blue'><%=patientName +" "+ sex +" "+ age%>
		</font></td>
		<td align="right"><input type="button" name="submit"
			value="Print" onclick="window.print();" /> <input type="button"
			value="  Exit  " onclick="javascript:window.close();" /></td>
	</tr>
</table>

<%
  out.println(checklist.doStuff(new String(oscarVariables.getProperty("tomcat_path") +"webapps/"+ oscarVariables.getProperty("project_home") + "/decision/annualreview/desannualreviewplannerriskchecklist.xml"), riskDataBean));
%>

</body>
</html>