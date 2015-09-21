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
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.util.StringUtils"%>
<%@ page
	import="oscar.oscarDemographic.data.*, org.oscarehr.common.model.Demographic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
String sex=request.getParameter("sex");
String age=request.getParameter("age");
String demo=request.getParameter("demo");


if(!StringUtils.isNullOrEmpty(demo)){
    DemographicData dd = new DemographicData();
    Demographic d = dd.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demo);  
    sex = d.getSex();
    age = d.getAge();
}
%>



<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html:base />
<script type="text/javascript" language=javascript>
   function popperup(vheight,vwidth,varpage,pageName) { //open a new popup window
     var page = varpage;
     windowprops = "height="+vheight+",width="+vwidth+",status=yes,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=100,left=100";
     var popup=window.open(varpage, pageName, windowprops);
     popup.pastewin = opener;     
     popup.focus();
     close();
   }
  </script>
</head>

<body>
<table border=0 cellpadding="0" cellspacing="1" width="100%"
	class="layerTable" bgcolor="#FFFFFF">
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(650,775,'http://www.mcw.edu/calculators/body-mass-index.htm','BodyMassIndex');">
		<bean:message key="oscarEncounter.Index.bodyMass" /> </a></td>
	</tr>
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(525,775,'calculators/CoronaryArteryDiseaseRiskPrediction.jsp?sex=<%= sex%>&age=<%=age%>','CoronaryArteryDiseaseRisk');">
		<bean:message key="oscarEncounter.Index.coronary" /> </a></td>
	</tr>
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(525,775,'calculators/riskcalc/index.html?sex=<%= sex%>&age=<%=age%>','CoronaryArteryDiseaseRisk');">
		Framingham/UKPDS Risk Calculator </a></td>
	</tr>
        <tr>
            <td align="center" class="menuLayer">
                <a href="javascript: function myFunction() {return false; }" onclick="popperup(525,775,'http://cvrisk.mvm.ed.ac.uk/calculator/calc.asp','CVRisk');">CV Risk</a>
            </td>
	</tr>
        
        
        
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(525,775,'calculators/OsteoporoticFracture.jsp?sex=<%=sex%>&age=<%=age%>','OsteoporoticFracture');">
		<bean:message key="oscarEncounter.Index.msgOsteoporotic" /> </a></td>
	</tr>
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(650,775,'http://www.mcw.edu/calculators/pregnancydate.htm','PregancyCalculator');">
		<bean:message key="oscarEncounter.Index.pregnancy" /> </a></td>
	</tr>
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(400,500,'calculators/SimpleCalculator.jsp','SimpleCalc');">
		<bean:message key="oscarEncounter.Index.simpleCalculator" /> </a></td>
	</tr>
	<tr>
		<td align="center" class="menuLayer"><a
			href="javascript: function myFunction() {return false; }"
			onclick="popperup(650,775,'calculators/GeneralCalculators.jsp','GeneralConversions'); ">
		<bean:message key="oscarEncounter.Index.generalConversions" /> </a></td>
	</tr>
</table>
</body>
</html:html>
