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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%

  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):("null") ;
  String form_no = request.getParameter("formId")!=null?request.getParameter("formId"):("0") ;
  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, oscar.util.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../../appointment/errorpage.jsp"%>

<jsp:useBean id="riskDataBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="risks"
	class="oscar.decision.DesAntenatalPlannerRisks_99_12" scope="page" />
<jsp:useBean id="checklist"
	class="oscar.decision.DesAnnualReviewPlannerChecklist" scope="page" />
<%@ include file="../../admin/dbconnection.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.DesAnnualReviewPlan" %>
<%@page import="org.oscarehr.common.dao.DesAnnualReviewPlanDao" %>
<%
	DesAnnualReviewPlanDao desAnnualReviewPlanDao = SpringUtils.getBean(DesAnnualReviewPlanDao.class);
%>
<%@page import="org.oscarehr.common.model.Demographic" %>           
<%@page import="org.oscarehr.common.dao.DemographicDao" %>     
<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
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

	DesAnnualReviewPlan darp = desAnnualReviewPlanDao.search(Integer.parseInt(form_no),Integer.parseInt(demographic_no));

	if (darp != null) {
  		String risk_content      = darp.getRiskContent();
  		String checklist_content = darp.getChecklistContent();
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
  Demographic d = demographicDao.getDemographic(demographic_no);
  int age = 0;
  String sex = null, patientName =null;
  if (d != null) {
    age = UtilDateUtilities.calcAge(d.getYearOfBirth(), d.getMonthOfBirth(),d.getDateOfBirth());
    sex = d.getSex();
	patientName = d.getFormattedName();
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
