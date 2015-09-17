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
<%@page import="org.oscarehr.common.model.DemographicStudyPK"%>
<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.util.*" errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.DemographicStudy" %>
<%@page import="org.oscarehr.common.dao.DemographicStudyDao" %>
<%@page import="org.oscarehr.common.model.Study" %>
<%@page import="org.oscarehr.common.dao.StudyDao" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%
	DemographicStudyDao demographicStudyDao = SpringUtils.getBean(DemographicStudyDao.class);
    StudyDao studyDao = SpringUtils.getBean(StudyDao.class);
%>
<%
    //this is a quick independent page to let you add studying patient.

    String demographic_no = request.getParameter("demographic_no")!=null ? request.getParameter("demographic_no") : "0";
    String curUser_no = (String) session.getAttribute("user");
    String strLimit1 = request.getParameter("limit1")!=null ? request.getParameter("limit1") : "0";
    String strLimit2 = request.getParameter("limit2")!=null ? request.getParameter("limit2") : "18";
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>




<%
    if (request.getParameter("submit")!=null && request.getParameter("submit").equals("Update")) {
		//if it is a array, need to have loop to insert the records for study_no
		String[] study_no = request.getParameterValues("study_no");
        String datetime = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy-MM-dd HH:mm:ss");

        int rowsAffected = demographicStudyDao.removeByDemographicNo(Integer.parseInt(demographic_no));
		if (study_no != null) {
			for (int i = 0; i < study_no.length; i++) {
				DemographicStudy ds = new DemographicStudy();
				ds.setId(new DemographicStudyPK());
				ds.getId().setDemographicNo(Integer.parseInt(demographic_no));
				ds.getId().setStudyNo(Integer.parseInt(study_no[i]));
				ds.setProviderNo(curUser_no);
				ds.setTimestamp(new java.util.Date());
				demographicStudyDao.persist(ds);
			}
		}

		out.println("<script language='JavaScript'>window.close();opener.refreshstudy();</script>);");
    }
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT STUDY SEARCH RESULTS</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepColor%>">
		<th>PATIENT STUDY RECORDS</th>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td align="left"><%=request.getParameter("name")!=null?request.getParameter("name"):""%></td>
	</tr>
</table>

<CENTER>
<table width="100%" border="0" bgcolor="#ffffff">
	<form method="post" name="study" action="demographicstudyselect.jsp">
	<input type="hidden" name="demographic_no" value="<%=demographic_no%>">
	<tr bgcolor="<%=deepColor%>">
		<TH width="5%"></TH>
		<TH width="10%">Study Name</TH>
		<TH width="15%">Description</TH>
	</tr>
	<%
    
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;

    for(Study s : studyDao.findAll()) {
    
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
        DemographicStudy ds = demographicStudyDao.findByDemographicNoAndStudyNo(Integer.parseInt(demographic_no),s.getId());
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align='center'><input type="checkbox" name="study_no"
			value="<%=s.getId()%>"
			<%=demographic_no.equals(ds!=null?ds.getId().getDemographicNo().toString():"0") ? "checked" : "" %>></td>
		<td><%=s.getStudyName()%></td>
		<td align="center"><%=s.getDescription()%></td>
	</tr>
	<%
  }
%>
	<tr>
		<td>&nbsp;</td>
		<td></td>
		<td></td>
	</tr>
	<tr align="center" bgcolor="<%=weakColor%>">
		<td colspan='3'><input type="submit" name="submit" value="Update">
		<input type="button" name="button" value="Exit"
			onClick="window.close()"></td>
	</tr>
</table>

</form>
</CENTER>
</body>
</html>
