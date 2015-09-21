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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Obr"%>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Pid"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7LinkDao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Link" %>

<%  

	Hl7LinkDao linkDao = SpringUtils.getBean(Hl7LinkDao.class);
	
    if(request.getParameterValues("chk")!= null){
    	String[] values = request.getParameterValues("chk");
    	for(int i = 0; i < values.length; ++i){
    		Hl7Link l = linkDao.find(Integer.parseInt(values[i]));
    		if(l != null) {
    			l.setStatus("N");
    			linkDao.merge(l);
    		}
    	}
    }

    for(Object[] o : linkDao.findMagicLinks()) {
    	Demographic demo = (Demographic) o[0];
    	Hl7Pid pid = (Hl7Pid) o[0];
    	Hl7Link link = (Hl7Link) o[0];
    	
		Hl7Link h = new Hl7Link();
		h.setId(pid.getId());
		h.setDemographicNo(demo.getDemographicNo());
		linkDao.persist(h);
    }
	
    if(request.getParameter("demo_id") != null && request.getParameter("pid") != null){
    	
    	linkDao.remove(linkDao.find(Integer.parseInt(request.getParameter("pid"))));
    	
    	Hl7Link h = new Hl7Link();
    	h.setId(Integer.parseInt(request.getParameter("pid")));
    	h.setDemographicNo(Integer.parseInt(request.getParameter("demo_id")));
    	linkDao.persist(h);
    	
    }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR oscarPathNET - Patient Linking</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
var demo;
var lab;
window.name= 'LabIndex';
function PopupDemo(pid){
	demo = window.open('demo_select.jsp?postTo=index.jsp?pid=' + pid + '-demo_id=','Patients','height=500,width=750,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	demo.focus();
	return false;
}
function PopupLab(pid){
	lab = window.open('report.jsp?pid=' + pid,'Lab','height=500,width=900,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	lab.focus();
	return false;
}
</script>
</head>
<body>
<form action="index.jsp" method="post">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="#D3D3D3">
	<tr>
		<td height="40" width="25"></td>
		<td width="50%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - Patient Linking</font></font></b></font>
		</td>
		<td class="Text" align="right"><a href="searchreports.jsp">Search
		Lab Reports</a>&nbsp;</td>
	</tr>
</table>
<table width="100%" cellspacing="2" cellpadding="2">
	<tr bgcolor="E6E6E6">
		<td class="Text" align="center" colspan="6"
			style="border-right: #464646 1px solid;">Lab Information</td>
		<td class="Text" align="center" colspan="3"
			style="border-left: #464646 1px solid;">Demographic Information</td>
	</tr>
	<tr>
		<td class="Header">Valid</td>
		<td class="Header">Patient</td>
		<td class="Header">BirthDate</td>
		<td class="Header">Gender</td>
		<td class="Header">Ordered By</td>
		<td class="Header" style="border-right: #464646 1px solid;">Copies
		To</td>
		<td class="Header" style="border-left: #464646 1px solid;">Demo.
		No.</td>
		<td class="Header">Name</td>
		<td class="Header">BirthDate</td>
	</tr>
	<%
		boolean other = true;
		for(Object[] o : linkDao.findLabs()) {
			Hl7Pid pid = (Hl7Pid) o[0]; 
			Hl7Link link = (Hl7Link) o[1]; 
			Hl7Obr obr = (Hl7Obr) o[2];
			Demographic demo = (Demographic) o[3];
%>
	<tr class="<%=(other? "WhiteBG" : "LightBG")%>">
		<td class="Text"><input type="checkbox" name="chk"
			value="<%=pid.getId()%>" /></td>
		<td class="Text"><a href="#"
			onclick="return PopupLab('<%=pid.getId()%>');"><%=oscar.Misc.check(pid.getPatientName(), "")%></a></td>
		<td class="Text">
			<%=ConversionUtils.toDateString(pid.getDateOfBirth())%>
		</td>
		<td class="Text" align="center">
			<%=oscar.Misc.check(pid.getSex(), "")%>
		</td>
		<td class="Text">
			<%=oscar.Misc.check(obr.getOrderingProvider(), "").replaceAll("~", ",<br/>")%>
		</td>
		<td class="Text" style="border-right: #464646 1px solid;">
			<%=oscar.Misc.check(obr.getResultCopiesTo(), "").replaceAll("~", ",<br/>")%>
		</td>
		<td class="Text" style="border-left: #464646 1px solid;">
			<a href="#" onclick="return PopupDemo('<%=link.getId()%>');">
				<%=oscar.Misc.check("" + demo.getDemographicNo(), "select")%>
			</a>
		</td>
		<td class="Text">
			<%= demo.getFullName()%>
		</td>
		<td class="Text">
			<%=	demo.getBirthDayAsString() %>
		</td>
	</tr>
	<%
			other=!other;
		}
%>
	<tr>
		<td colspan="9" align="left"><input type="submit" name="submit"
			value="Link" /></td>
	</tr>
</table>
</form>
</body>
</html:html>
